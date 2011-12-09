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

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleManager;

import com.webtrends.mobile.analytics.rim.WebtrendsConfig;
import com.webtrends.mobile.analytics.rim.WebtrendsConfigurator;
import com.webtrends.mobile.analytics.rim.WebtrendsDataCollector;
import com.webtrends.mobile.rim.WebtrendsUiApplication;

/**
 * This class extends the UiApplication class, providing a graphical user
 * interface.
 */
public class MyApp extends WebtrendsUiApplication {
	/**
	 * Entry point for application
	 * 
	 * @param args
	 *            Command line arguments (not used)
	 */
	public static void main(String[] args) {
		// Create a new instance of the application and make the currently
		// running thread the application's event dispatch thread.
		MyApp theApp = new MyApp();
		theApp.enterEventDispatcher();
	}

	/**
	 * Creates a new MyApp object
	 */
	public MyApp() {
		WebtrendsConfigurator.LoadConfigFile(new AnalyticsConfig());
		WebtrendsDataCollector wtDC = WebtrendsDataCollector.getInstance();
		wtDC.Initialize();
		// Push a screen onto the UI stack for rendering.
		pushScreen(new MyScreen());
	}

	private class AnalyticsConfig extends WebtrendsConfig {

		public String wt_dc_app_name() {
			return ApplicationDescriptor.currentApplicationDescriptor().getName();
		}

		public String wt_dc_app_version() {
			return ApplicationDescriptor.currentApplicationDescriptor().getVersion();
		}

		public String wt_dc_dcsid() {
			return "dcssp2rzw5bv0hk35mx7god5y_7f8z"; // Analytics Demo
		}

		public String wt_dc_timezone() {
			return "-5";
		}

		public String wt_dc_app_category() {
			return "Utilities";
		}

		public String wt_dc_app_publisher() {
			return CodeModuleManager.getModuleVendor(ApplicationDescriptor.currentApplicationDescriptor().getModuleHandle());
		}
	}
}
