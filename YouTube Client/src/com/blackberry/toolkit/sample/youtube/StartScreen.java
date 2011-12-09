/*
* Copyright (c) 2011 Research In Motion Limited.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.blackberry.toolkit.sample.youtube;

import java.util.Vector;

import javax.microedition.content.Invocation;
import javax.microedition.content.Registry;

import net.rim.device.api.content.BlackBerryContentHandler;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.StandardTitleBar;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.image.ImageFactory;
import net.rim.device.api.util.StringProvider;

import com.blackberry.toolkit.sample.youtube.internal.Dispatcher;
import com.blackberry.toolkit.sample.youtube.internal.ResponseListener;
import com.blackberry.toolkit.sample.youtube.internal.VideoData;

public class StartScreen extends MainScreen implements ResponseListener {

	private Runnable runSearch = new RunSearch(0);
	private ClickEditField searchBox = new ClickEditField(runSearch);
	private ButtonField searchGo = new ButtonField("Search", ButtonField.CONSUME_CLICK);
	private GridFieldManager searchManager = new GridFieldManager(1, 2, 0);
	private HorizontalFieldManager nextPreviousManager = new HorizontalFieldManager();
	private ButtonField searchNext = new ButtonField("More", ButtonField.CONSUME_CLICK);
	private ButtonField searchPrevious = new ButtonField("Back", ButtonField.CONSUME_CLICK);
	private GridFieldManager searchResults = new GridFieldManager(1, 1, 0);
	private volatile int currentIndex = 1;
	private int maxResults = 10;
	private static Bitmap _clock = EncodedImage.getEncodedImageResource("img/clock.PNG").getBitmap();
	private static Bitmap _icon = EncodedImage.getEncodedImageResource("img/icon.png").getBitmap();
	private VerticalFieldManager main = new VerticalFieldManager();

	public StartScreen() {
		XYEdges edges = new XYEdges(4, 4, 4, 4);
		main.setPadding(edges);
		searchBox.setPadding(edges);
		searchBox.setBorder(BorderFactory.createRoundedBorder(edges, Color.CORNFLOWERBLUE, Border.STYLE_SOLID));
		searchManager.setColumnProperty(0, GridFieldManager.AUTO_SIZE, 0);
		searchManager.setColumnProperty(1, GridFieldManager.PREFERRED_SIZE, 0);
		searchManager.add(searchBox);
		searchGo.setRunnable(runSearch);
		searchManager.add(searchGo);
		main.add(searchManager);
		main.add(new SeparatorField());
		main.add(searchResults);
		searchPrevious.setEnabled(false);
		searchNext.setEnabled(false);
		searchPrevious.setRunnable(new RunSearch(-maxResults));
		searchNext.setRunnable(new RunSearch(maxResults));
		nextPreviousManager.add(searchPrevious);
		nextPreviousManager.add(searchNext);
		main.add(nextPreviousManager);
		add(main);
		StandardTitleBar titlebar = new StandardTitleBar();
		titlebar.addTitle("YouTube Client");
		titlebar.addSignalIndicator();
		titlebar.addIcon(_icon);
		setTitleBar(titlebar);

		addMenuItem(new BasicMenuItem("Search", runSearch));
	}

	private class ClickEditField extends BasicEditField {
		private Runnable _action;

		public ClickEditField(Runnable action) {
			super(NO_NEWLINE);
			_action = action;
		}

		protected boolean invokeAction(int action) {
			if (_action != null) {
				UiApplication.getUiApplication().invokeLater(_action);
				return true;
			}
			return false;

		}
	}

	private class BasicMenuItem extends MenuItem {
		private Runnable _action;

		public BasicMenuItem(String label, Runnable action) {
			super(new StringProvider(label), 0, 100);
			_action = action;
			setIcon(ImageFactory.createImage("img/search.png"));
		}

		public void run() {
			if (_action != null) {
				_action.run();
			}
		}
	}

	private class RunSearch implements Runnable {
		private int _offset = 0;

		public RunSearch(int offset) {
			_offset = offset;
		}

		public void run() {
			currentIndex += _offset;
			if (currentIndex < 1) {
				currentIndex = 1;
			}
			Dispatcher.getInstance().search(searchBox.getText(), currentIndex, maxResults, StartScreen.this);
		}
	}

	// ResponseListener Implementation
	public void searchResponse(Vector results) {
		if (results != null && results.size() > 0) {
			// Create the list of videos
			GridFieldManager resultsList = new GridFieldManager(results.size() * 2, 2, GridFieldManager.USE_ALL_WIDTH
					| GridFieldManager.USE_ALL_HEIGHT);
			resultsList.setColumnProperty(0, GridFieldManager.PREFERRED_SIZE, 0);
			resultsList.setColumnProperty(1, GridFieldManager.AUTO_SIZE, 0);
			resultsList.setColumnPadding(1);
			resultsList.setCellPadding(0);
			for (int i = 0; i < results.size(); i++) {
				VideoData data = (VideoData) results.elementAt(i);
				// Thumbnail
				BitmapFieldButton image = new BitmapFieldButton(_clock, BitmapField.FOCUSABLE);
				image.setSpace(2, 2);
				image.setCommandAction(new ThumbnailClick(data));
				data.setBitmapField(image);
				resultsList.add(image);
				// Text column
				VerticalFieldManager vfm = new VerticalFieldManager();
				vfm.add(new LabelField(data.getTitle(), LabelField.NON_FOCUSABLE));
				vfm.add(new LabelField(data.getDescription(), LabelField.USE_ALL_HEIGHT | LabelField.NON_FOCUSABLE
						| LabelField.ELLIPSIS));
				resultsList.add(vfm);
				// Break
				resultsList.add(new NullField(NullField.NON_FOCUSABLE));
				resultsList.add(new SeparatorField());
			}

			UiApplication.getUiApplication().invokeLater(new ResultsUpdater(resultsList));
		}

	}

	private class ThumbnailClick implements Runnable {
		private VideoData _data;

		public ThumbnailClick(VideoData data) {
			_data = data;
		}

		public void run() {
			// CHAPI invocation
			Invocation invoke = new Invocation(_data.getUrl(), null, BlackBerryContentHandler.ID_MEDIA_CONTENT_HANDLER, false,
					null);
			try {
				Registry.getRegistry(YoutubeClient.class.getName()).invoke(invoke);
			} catch (Throwable t) {
				YoutubeClient.log("Failure invoking video: " + t.toString());
			}
		}
	}

	private class ResultsUpdater implements Runnable {
		private GridFieldManager _results;

		public ResultsUpdater(GridFieldManager results) {
			_results = results;
		}

		public void run() {
			if (currentIndex > 1) {
				// put a previous button
				searchPrevious.setEnabled(true);
			} else {
				searchPrevious.setEnabled(false);
			}
			if (_results.getRowCount() > 2) {
				// Put a next button
				searchNext.setEnabled(true);
			} else {
				searchNext.setEnabled(false);
			}
			main.replace(searchResults, _results);
			updateLayout();
			searchResults = _results;
			searchResults.getFieldAtIndex(0).setFocus();

		}

	}

	public void thumbRetrieved(VideoData data) {
		UiApplication.getUiApplication().invokeLater(new ThumbUpdater(data));
	}

	public class ThumbUpdater implements Runnable {
		private VideoData _data;

		public ThumbUpdater(VideoData data) {
			_data = data;
		}

		public void run() {
			if (_data.getBitmapField() != null) {
				_data.getBitmapField().setBitmap(_data.getThumbnail());
				// updateLayout();
			}

		}
	}

	public boolean isDirty() {
		return false;
	}

}
