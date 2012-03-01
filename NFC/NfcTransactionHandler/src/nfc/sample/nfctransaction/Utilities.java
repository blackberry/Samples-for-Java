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
import java.util.Vector;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.NFCManager;
import net.rim.device.api.io.nfc.emulation.TechnologyType;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import nfc.sample.nfctransaction.nfc.CardEmulation;

public class Utilities {

    static final long MYAPP_ID = 0x8172320e10e61b12L;
    
    private static Font system_font = Font.getDefault();

    public static void initLogging() {
        EventLogger.register(MYAPP_ID, "NfcTransactionHandler", EventLogger.VIEWER_STRING);
    }

    public static void log(String log_msg) {
        System.out.println(log_msg);
        boolean ok = EventLogger.logEvent(MYAPP_ID, log_msg.getBytes(), EventLogger.INFORMATION);
    }

    public static String[] split(String whole_string, char delimiter) {
        String delim = String.valueOf(delimiter);
        Vector parts = new Vector();
        String part = "";
        int end = whole_string.length();
        for(int i = 0; i < end; i++) {
            String one_char = whole_string.substring(i, i + 1);
            if(!one_char.equals(delim)) {
                part = part + one_char;
            } else {
                parts.addElement(part);
                part = new String();
            }
        }
        parts.addElement(part);
        String[] string_parts = new String[parts.size()];
        int size = parts.size();
        for(int i = 0; i < size; i++) {
            string_parts[i] = (String) parts.elementAt(i);
        }
        return string_parts;
    }

    public static String hexPresentation(String aids_received) {
        // assumes a valid series of 1 or more hex strings, separated by commas
        // e.g. 999999,111111
        String hexified = "0x";
        int len = aids_received.length();
        int i = 0;
        while(i < len) {
            if(aids_received.charAt(i) != ',') {
                hexified = hexified + aids_received.charAt(i);
                i++;
                hexified = hexified + aids_received.charAt(i);
                if(i < (len - 1)) {
                    hexified = hexified + ":";
                }
            } else {
                hexified = hexified + aids_received.charAt(i);
            }
            i++;
        }
        return hexified;
    }

    public static String getNfcStatusDescription(int service_status) {
        boolean unknown = true;
        String status = "";
        if(service_status == NFCManager.NFC_NONE) {
            status = status + "NFC_NONE (" + NFCManager.NFC_NONE + ")\n";
            unknown = false;
        }
        if((service_status & NFCManager.NFC_ALL) == NFCManager.NFC_ALL) {
            status = status + "NFC_ALL (" + NFCManager.NFC_ALL + ")\n";
            unknown = false;
        }

        if((service_status & NFCManager.NFC_EMBEDDED_SECURE_ELEMENT_CARD_EMULATION) == NFCManager.NFC_EMBEDDED_SECURE_ELEMENT_CARD_EMULATION) {
            status = status + "NFC_EMBEDDED_SECURE_ELEMENT_CARD_EMULATION ("
                    + NFCManager.NFC_EMBEDDED_SECURE_ELEMENT_CARD_EMULATION + ")\n";
            unknown = false;
        }

        if((service_status & NFCManager.NFC_TAG_CARD_EMULATION) == NFCManager.NFC_TAG_CARD_EMULATION) {
            status = status + "NFC_TAG_CARD_EMULATION (" + NFCManager.NFC_TAG_CARD_EMULATION + ")\n";
            unknown = false;
        }

        if((service_status & NFCManager.NFC_TAG_CARD_READER_WRITER) == NFCManager.NFC_TAG_CARD_READER_WRITER) {
            status = status + "NFC_TAG_CARD_READER_WRITER (" + NFCManager.NFC_TAG_CARD_READER_WRITER + ")";
            unknown = false;
        }

        if((service_status & NFCManager.NFC_UICC_CARD_EMULATION) == NFCManager.NFC_UICC_CARD_EMULATION) {
            status = status + "NFC_UICC_CARD_EMULATION (" + NFCManager.NFC_UICC_CARD_EMULATION + ")";
            unknown = false;
        }

        if((service_status & NFCManager.NFC_UICC_CARD_EMULATION_PERSISTENT) == NFCManager.NFC_UICC_CARD_EMULATION_PERSISTENT) {
            status = status + "NFC_UICC_CARD_EMULATION_PERSISTENT (" + NFCManager.NFC_UICC_CARD_EMULATION_PERSISTENT + ")\n";
            unknown = false;
        }

        if(unknown) {
            status = status + "Unknown code (" + service_status + ")\n";
        }

        return status;
    }

    public static String getTechnologyTypesNames(int types) {
        boolean unknown = true;
        String type_names = "NFC technology types: ";
        if((types & TechnologyType.FELICA) == TechnologyType.FELICA) {
            type_names = type_names + "FELICA (" + TechnologyType.FELICA + "),";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443A) == TechnologyType.ISO14443A) {
            type_names = type_names + "ISO14443A (" + TechnologyType.ISO14443A + "),";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443A_MIFARE) == TechnologyType.ISO14443A_MIFARE) {
            type_names = type_names + "ISO14443A_MIFARE (" + TechnologyType.ISO14443A_MIFARE + "),";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443B) == TechnologyType.ISO14443B) {
            type_names = type_names + "ISO14443B (" + TechnologyType.ISO14443B + "),";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443B_PRIME) == TechnologyType.ISO14443B_PRIME) {
            type_names = type_names + "ISO14443B_PRIME (" + TechnologyType.ISO14443B_PRIME + "),";
            unknown = false;
        }
        if((types & TechnologyType.ISO15693) == TechnologyType.ISO15693) {
            type_names = type_names + "ISO15693 (" + TechnologyType.ISO15693 + "),";
            unknown = false;
        }
        if((types & TechnologyType.NONE) == TechnologyType.NONE) {
            type_names = type_names + "NONE (" + TechnologyType.NONE + "),";
            unknown = false;
        }
        if(unknown) {
            type_names = type_names + "Unknown technology types (" + types + "),";
        }

        return type_names.substring(0, type_names.length() - 1);
    }
    
    public static String makeApduConnectionString(String AID) {
        String connection_string = "apdu:0;target="; 
        boolean done=false;
        int i=0;
        while (!done) {
            connection_string = connection_string + AID.substring(i, i+2);
            i = i + 2;
            if (i < (AID.length() - 1)) {
                connection_string = connection_string + ".";
            } else {
              done = true;  
            }
        }
        return connection_string;        
    }

    public static String onOrOff(boolean state) {
        if(state == true) {
            return "ON";
        } else {
            return "OFF";
        }
    }

    public static boolean isUiRunning() {
        RuntimeStore rts = RuntimeStore.getRuntimeStore();
        Utilities.log("XXXX isUiRunning()");
        Object ui_running_token = rts.get(Constants.UI_IS_RUNNING);
        if(ui_running_token != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void setUiNotRunningIndication() {
        RuntimeStore rts = RuntimeStore.getRuntimeStore();
        rts.remove(Constants.UI_IS_RUNNING);
    }

    public static String getNFCServicesSummary() {
        String summary = ",";
        try {
            int service_status = NFCManager.getInstance().getAvailableNFCServices();
            if(service_status == NFCManager.NFC_NONE) {
                summary = "NFC OFF";
                return summary;
            }
            summary = "NFC ON: ";
            if((service_status & NFCManager.NFC_EMBEDDED_SECURE_ELEMENT_CARD_EMULATION) == NFCManager.NFC_EMBEDDED_SECURE_ELEMENT_CARD_EMULATION) {
                summary = summary + "eSE CE,";
            }

            if((service_status & NFCManager.NFC_TAG_CARD_EMULATION) == NFCManager.NFC_TAG_CARD_EMULATION) {
                summary = summary + "VIRTUAL TAG,";
            }

            if((service_status & NFCManager.NFC_TAG_CARD_READER_WRITER) == NFCManager.NFC_TAG_CARD_READER_WRITER) {
                summary = summary + "TAG R/W,";
            }

            if((service_status & NFCManager.NFC_UICC_CARD_EMULATION) == NFCManager.NFC_UICC_CARD_EMULATION) {
                summary = summary + "UICC CE,";
            }

            if((service_status & NFCManager.NFC_UICC_CARD_EMULATION_PERSISTENT) == NFCManager.NFC_UICC_CARD_EMULATION_PERSISTENT) {
                summary = summary + "UICC CE PERS,";
            }
            summary = summary.substring(0, summary.length() - 1);
            return summary;
        } catch(NFCException e) {
            return "Error establishing status";
        }
    }

    public static String getCeStatusSummary() {
        boolean unknown = true;
        int types;
        try {
            types = CardEmulation.getInstance().getTechnologyTypes();
        } catch(NFCException e) {
            return "Error establishing status";
        }
        String type_names = "";
        if((types & TechnologyType.FELICA) == TechnologyType.FELICA) {
            type_names = type_names + "FELICA,";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443A) == TechnologyType.ISO14443A) {
            type_names = type_names + "ISO14443A,";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443A_MIFARE) == TechnologyType.ISO14443A_MIFARE) {
            type_names = type_names + "ISO14443A_MIFARE,";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443B) == TechnologyType.ISO14443B) {
            type_names = type_names + "ISO14443B,";
            unknown = false;
        }
        if((types & TechnologyType.ISO14443B_PRIME) == TechnologyType.ISO14443B_PRIME) {
            type_names = type_names + "ISO14443B_PRIME,";
            unknown = false;
        }
        if((types & TechnologyType.ISO15693) == TechnologyType.ISO15693) {
            type_names = type_names + "ISO15693,";
            unknown = false;
        }
        if(unknown) {
            if((types & TechnologyType.NONE) == TechnologyType.NONE) {
                type_names = type_names + "NONE,";
                unknown = false;
            }
        }
        if(unknown) {
            type_names = type_names + "Unknown,";
        }

        return type_names.substring(0, type_names.length() - 1);
    }

    public static int ledBooleanToInt(boolean on) {
        if(on) {
            return Constants.LED_ON;
        } else {
            return Constants.LED_OFF;
        }
    }

    public static void popupMessage(String message) {
        synchronized(UiApplication.getEventLock()) {
            Dialog.inform(message);
        }
    }

    public static void popupAlert(String message) {
        synchronized(UiApplication.getEventLock()) {
            Dialog.alert(message);
        }
    }

    public static int getTextWidth(String text) {
        return system_font.getBounds(text);
    }
    
}
