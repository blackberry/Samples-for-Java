package nfc.sample.Ndef.Read;
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

	private static final long MYAPP_ID = 0x6c1c321b9775fdb4L;
	
	private static final int UTF_16_TEXT = 0x80;
	
	private static final int IANA_LANGUAGE_CODE_LEN_MASK = 0x1F;

	/*
	 * Generic logging routine that writes entries to the event log and also to
	 * STDOUT when debugging
	 */
	public static void log(String logMessage) {
		System.out.println(logMessage);
		boolean ok = EventLogger.logEvent(MYAPP_ID, logMessage.getBytes(), EventLogger.INFORMATION);
	}

	public static void initLogging() {
		EventLogger.register(MYAPP_ID, "NfcReadNdefSmartTag", EventLogger.VIEWER_STRING);
	}
	
	public static boolean isUtf16Encoded(int status_byte) {
		return (status_byte == (status_byte & UTF_16_TEXT));
	}
	
	public static int getIanaLanguageCodeLength(int status_byte) {
		return status_byte & IANA_LANGUAGE_CODE_LEN_MASK;
	}
	
    public static boolean isOdd(int number) {
        int remainder = number % 2;
        return remainder == 1;
    }

}
