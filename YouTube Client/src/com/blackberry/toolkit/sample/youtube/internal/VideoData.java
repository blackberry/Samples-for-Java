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

package com.blackberry.toolkit.sample.youtube.internal;

import java.lang.ref.WeakReference;

import com.blackberry.toolkit.sample.youtube.BitmapFieldButton;

import net.rim.device.api.system.Bitmap;

public class VideoData {
	private String _title;
	private Bitmap _thumbnail;
	private int _thumbWidth;
	public int getThumbWidth() {
		return _thumbWidth;
	}

	public void setThumbWidth(int thumbWidth) {
		_thumbWidth = thumbWidth;
	}

	public int getThumbHeight() {
		return _thumbHeight;
	}

	public void setThumbHeight(int thumbHeight) {
		_thumbHeight = thumbHeight;
	}

	private int _thumbHeight;
	private String _url;
	private String _description;
	private WeakReference _bitmapField;

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public Bitmap getThumbnail() {
		return _thumbnail;
	}

	public void setThumbnail(Bitmap thumbnail) {
		_thumbnail = thumbnail;
	}

	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setBitmapField(BitmapFieldButton bitmapField) {
		this._bitmapField = new WeakReference(bitmapField);
	}

	public BitmapFieldButton getBitmapField() {
		if (_bitmapField != null) {
			return (BitmapFieldButton) _bitmapField.get();
		}
		return null;
	}

}
