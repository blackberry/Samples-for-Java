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
package nfc.sample.tictactoe;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Utilities {

    private static final long MYAPP_ID = 0x4c38c8a7798397d1L;

    public static void log(String logMessage) {
        boolean ok = EventLogger.logEvent(MYAPP_ID, logMessage.getBytes(), EventLogger.INFORMATION);
    }

    public static void initLogging() {
        EventLogger.register(MYAPP_ID, "TouchTicTacToe", EventLogger.VIEWER_STRING);
    }

    public static boolean isOdd(int number) {
        int remainder = number % 2;
        return remainder == 1;
    }

    public static byte[] toBytes(int value) {
        byte [] bytes = new byte[4];
        bytes [0] = (byte) ((value >>> 24) & 0xFF);
        bytes [1] = (byte) ((value >>> 16) & 0xFF);
        bytes [2] = (byte) ((value >>>  8) & 0xFF);
        bytes [3] = (byte) ( value         & 0xFF);
        return bytes;
    }

    public static int toInt(byte[] bytes, int start) {
        if((bytes.length - start) < 3) {
            Utilities.log("XXXX toInt method received invalid length start position; insifficient bytes:" + bytes.length + "," + start);
            return 0;
        }
        int i = ((bytes[start] & 0xFF) << 24) + ((bytes[start + 1] & 0xFF) << 16) + ((bytes[start + 2] & 0xFF) << 8) + (bytes[start + 3] & 0xFF);
        return i;
    }
    
    public static void popupMessage(String message) {
        synchronized(UiApplication.getEventLock()) {
            Dialog.inform(message);
        }
    }

}
