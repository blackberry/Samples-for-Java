package nfc.sample.peer2peer;
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

import java.io.UnsupportedEncodingException;

import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Utilities implements Constants {

	private static final long MYAPP_ID = 0xbe5dad5a0de22925L;
	private static final String MYAPP_NAME = "NfcSnepResponder";
	private static final int UTF_16_TEXT = 0x80;
	private static final int IANA_LANGUAGE_CODE_LEN_MASK = 0x1F;

    /*
	 * Generic logging routine that writes entries to the event log and also to
	 * STDOUT when debugging
	 */
	public static void log(String logMessage) {
		System.out.println(logMessage);
		try {
		    boolean ok = EventLogger.logEvent(MYAPP_ID, logMessage.getBytes("US-ASCII"), EventLogger.INFORMATION);
        } catch(UnsupportedEncodingException e) {
        }
	}

    public static void popupMessage(String message) {
        synchronized(UiApplication.getEventLock()) {
            Dialog.inform(message);
        }
    }

	public static void initLogging() {
		EventLogger.register(MYAPP_ID, MYAPP_NAME, EventLogger.VIEWER_STRING);
	}
	
	public static boolean isUtf16Encoded(int status_byte) {
		return (status_byte == (status_byte & UTF_16_TEXT));
	}
	
	public static int getIanaLanguageCodeLength(int status_byte) {
		return status_byte & IANA_LANGUAGE_CODE_LEN_MASK;
	}
	
	/*
	 * Helper to represent a byte as a printable ASCII character
	 */
	public static char byte2Ascii(byte b) {
		char character = '.'; 
		if ((20 <= b) && (126 >= b)) {
			character = (char) b;
		}
		return character;
	}

	/*
	 * Helper to represent a byte as a printable hex pair.
	 */
	public static String byte2HexPair(byte b) {
		String hex;
		hex = "00" + Integer.toHexString(b);
		hex = hex.substring(hex.length() - 2).toUpperCase();
		return hex;
	}

	public static String formatAsHexPairs(int widthOfRow, byte[] dataToFormat) {

		StringBuffer hexFormattedData = new StringBuffer();
		String hexpair;

		for (int i = 0; i < dataToFormat.length; i++) {
			hexpair = Utilities.byte2HexPair(dataToFormat[i]);
			hexFormattedData.append(hexpair + " "
					+ (((i + 1)       % widthOfRow == 0) ? "\n" : ""));
		}
		
		return hexFormattedData.toString();
	}

	public static String formatAsPrintable(int widthOfRow, byte[] dataToFormat) {
		
		StringBuffer printableFormattedData = new StringBuffer();
		for (int i = 0; i < dataToFormat.length; i++) {
			printableFormattedData.append(Utilities.byte2Ascii(dataToFormat[i]));
			printableFormattedData
					.append((((i + 1) % widthOfRow == 0) ? "\n" : ""));
		}

		return printableFormattedData.toString();
	}

	public static boolean isOdd(int number) {
        int remainder = number % 2;
        return remainder == 1;
    }
}
