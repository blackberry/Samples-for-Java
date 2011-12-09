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

package mypackage;

import com.webtrends.mobile.analytics.IllegalWebtrendsParameterValueException;
import com.webtrends.mobile.analytics.rim.WebtrendsDataCollector;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class MyScreen extends MainScreen {
	/**
	 * Creates a new MyScreen object
	 */
	public MyScreen() {
		// Set the displayed title of the screen
		setTitle("Analytics Example");

		addMenuItem(new SendAnalyticsEvent("AdClickEvent", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onAdClickEvent("/mainscreen", "Main Screen", "menu", null, "Demo Ad");
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("AdImpressionEvent", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onAdImpressionEvent("/mainscreen", "Main Screen", "menu", null,
							new String[] { "Demo Ad 1", "Demo Ad 2" });
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("ButtonClick", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onButtonClick("/mainscreen", "Main Screen", "menu", null);
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("ScreenView", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onScreenView("/mainscreen", "Main Screen", "menu", null, "Demo Content");
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("ConversionEvent", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onConversionEvent("/mainscreen", "Main Screen", "menu", null, "Demo Content",
							"Conversion Demo");
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("CustomEvent", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onCustomEvent("/mainscreen", "Main Screen", null);
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("MediaEvent", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onMediaEvent("/mainscreen", "Main Screen", "menu", null, "Media Content", "Demo Media",
							"music", "p");
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("ProductView", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onProductView("/mainscreen", "Main Screen", "menu", null, "Products", "item1", "SKU00001");
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

		addMenuItem(new SendAnalyticsEvent("SearchEvent", new Runnable() {

			public void run() {
				try {
					WebtrendsDataCollector.getInstance().onSearchEvent("/mainscreen", "Main Screen", "menu", null, "demo search", "demo results");
				} catch (IllegalWebtrendsParameterValueException e) {
					System.out.println(e.toString());
				}
			}
		}));

	}

	public boolean onClose() {
		try {
			WebtrendsDataCollector.getInstance().onApplicationTerminate("Application Terminate", null);
		} catch (IllegalWebtrendsParameterValueException err) {
			WebtrendsDataCollector.getLog().e(err.getMessage());
		}
		System.exit(0);
		return true;
	}

	private class SendAnalyticsEvent extends MenuItem {

		private Runnable action;

		public SendAnalyticsEvent(String text, Runnable action) {
			super(text, 100, 100);
			this.action = action;
		}

		public void run() {
			action.run();
		}

	}

}
