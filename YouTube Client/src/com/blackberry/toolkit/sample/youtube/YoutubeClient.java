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

import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;

public class YoutubeClient extends UiApplication {

	public static final long UID = 0x5b2e5a6c0e0e8af1L; // com.blackberry.toolkit.sample.youtube.YoutubeClient

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventLogger.register(UID,
				"com.blackberry.toolkit.sample.youtube.YoutubeClient",
				EventLogger.VIEWER_STRING);
		UiApplication app = new YoutubeClient();
		app.enterEventDispatcher();
	}

	public YoutubeClient() {
		pushScreen(new StartScreen());
	}

	public static void log(String message) {
		if (message != null) {
			System.out.println("YoutubeClient --> " + message);
			EventLogger.logEvent(UID, message.getBytes());
		}
	}

}
