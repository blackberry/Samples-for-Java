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
package nfc.sample.llcp;

import java.io.UnsupportedEncodingException;

import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Utilities {

    private static final long MYAPP_ID = 0xde587f9e4b151ce7L;
    private static final String MYAPP_NAME = "LLCPDemo";

    public static void log(String logMessage) {
        try {
            boolean ok = EventLogger.logEvent(MYAPP_ID, logMessage.getBytes("US-ASCII"), EventLogger.INFORMATION);
        } catch(UnsupportedEncodingException e) {
        }
    }

    public static void initLogging() {
        EventLogger.register(MYAPP_ID, MYAPP_NAME, EventLogger.VIEWER_STRING);
    }

    public static void popupMessage(String message) {
        synchronized(UiApplication.getEventLock()) {
            Dialog.inform(message);
        }
    }
 
    public static boolean isOdd(int number) {
        int remainder = number % 2;
        return remainder == 1;
    }
}
