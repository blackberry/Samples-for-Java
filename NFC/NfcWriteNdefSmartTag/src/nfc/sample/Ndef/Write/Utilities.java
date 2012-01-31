package nfc.sample.Ndef.Write;
/*
* Copyright (c) 2012 Research In Motion Limited.
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

import net.rim.device.api.system.EventLogger;

public class Utilities {

	private static final long MYAPP_ID = 0xb3a5da4f78ad294fL;

	/*
	 * Generic logging routine that writes entries to the event log and also to
	 * STDOUT when debugging
	 */
	public static void log(String logMessage) {
		System.out.println(logMessage);
		boolean ok = EventLogger.logEvent(MYAPP_ID, logMessage.getBytes(), EventLogger.INFORMATION);
	}

	public static void initLogging() {
		EventLogger.register(MYAPP_ID, "NfcWriteNdefSmartTag", EventLogger.VIEWER_STRING);
	}
	
    public static boolean isOdd(int number) {
        int remainder = number % 2;
        return remainder == 1;
    }

}
