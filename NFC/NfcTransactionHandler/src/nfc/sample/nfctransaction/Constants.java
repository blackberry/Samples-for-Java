package nfc.sample.nfctransaction;
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
import net.rim.device.api.ui.Color;

public class Constants {
    public static String VERSION = "1.1.5";

    public static final int SE_SIM=0;
    public static final int SE_EMB=1;
    public static String [] SE_NAMES = {"SIM","EMBEDDED"};
    
    // SIM 1
//    public static final byte[][] MY_AIDS = { { 0x4E, 0x46, 0x43, 0x54, 0x65, 0x73, 0x74, 0x65, 0x72, 0x20, 0x31, 0x2E, 0x30 }   };
//    public static final String CONNECTION_STRING = "apdu:0;target=4E.46.43.54.65.73.74.65.72.20.31.2E.30";
    // SIM 2
    public static final byte[][] MY_AIDS = {  { 0x6E, 0x66, 0x63, 0x74, 0x65, 0x73, 0x74, 0x30, 0x31 } };
    public static final String CONNECTION_STRING = "apdu:0;target=6E.66.63.74.65.73.74.30.31";

    public static long UI_IS_RUNNING = 0xcb4b95cab37dbc72L;

    public static final int BACKGROUND_COLOUR = Color.BLACK;

    // navigation
    public static final int INDEX_CE = 0;
    public static final int INDEX_CLEAR = 1;
    public static final int INDEX_CLOSE = 2;
    public static final int INDEX_STATUS = 3;
    public static final int INDEX_KILL = 4;

    // LEDs
    public static final int LED_OFF = 0;
    public static final int LED_ON = 1;
    public static final int LED_ERR = 2;

    // buttons
    public static final int BTN_DEFAULT = 0;
    public static final int BTN_CE_ON = 0;
    public static final int BTN_CE_OFF = 1;
    public static final int BTN_SELECT_OFF = 1;
    public static final int BTN_SELECT_ON = 0;

    public static final String AID_SPACE = "--.--.--.--.--.--.--.--.--";

    public static final String READY = "Ready";
}
