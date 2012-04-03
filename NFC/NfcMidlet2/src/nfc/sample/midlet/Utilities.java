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
 * 
 * 
 * This sample application illustrates various aspects of NFC card emulation.
 * 
 * Authors: John Murray and Martin Woolley
 * 
 */
package nfc.sample.midlet;

import net.rim.device.api.system.EventLogger;

public class Utilities {

	static final long MYAPP_ID = 0x3f0ac1041e84838L;

	public static void initLogging() {
		EventLogger.register(MYAPP_ID, "NfcMidlet2", EventLogger.VIEWER_STRING);
	}

	public static void log(String log_msg) {
		System.out.println(log_msg);
		boolean ok = EventLogger.logEvent(MYAPP_ID, log_msg.getBytes(), EventLogger.INFORMATION);
	}


}
