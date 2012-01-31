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
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.BadFormatException;
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFRecord;
import net.rim.device.api.io.nfc.ndef.rtd.SmartPosterRecord;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.UiApplication;
import nfc.sample.Ndef.Read.ui.NfcReadNdefSmartTagScreen;

/**
 * 
 * This implement runnable and is a unit of work that was scheduled from the NDEF MEssage handler to the main event loop so that
 * we can update screen fields in relative safely
 * 
 * The main processing here is to parse the NDEF Message into component records and then parse each record in turn
 * 
 * At this time the NDEF record payload is simply rendered as a hex string and a character representation output to the screen for
 * the user to admire.
 * 
 * More detailed parsing will be added later.
 * 
 */
public class NfcReadNdefSmartTagUpdate implements Runnable {

    private NDEFMessage _message;
    private NfcReadNdefSmartTagScreen _screen;

    private RuntimeStore rts = RuntimeStore.getRuntimeStore();

    public NfcReadNdefSmartTagUpdate() {
        super();
    }

    public NfcReadNdefSmartTagUpdate(NDEFMessage message, NfcReadNdefSmartTagScreen screen) {
        this();
        this._message = message;
        this._screen = screen;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     * 
     * This is the runnable unit of work that was scheduled from the NDEF Message handler
     */
    public void run() {
     
        NDEFRecord[] records = _message.getRecords();
        
        try {
            Utilities.log("XXXX using helper classes to parse NDEF message");
            SmartPosterRecord sp = new SmartPosterRecord(records[0]);
            String uri = sp.getUri();
            String id = sp.getId();
            String mime = sp.getMimeType();
            int size = sp.getSize();
            String type = sp.getType();
            byte [] payload = sp.getPayload();
//            _screen.logEvent("id="+id);
            _screen.logEvent("type="+type);
            _screen.logEvent("URI="+uri);
//            _screen.logEvent("MIME="+mime);
//            _screen.logEvent("size="+size);
            _screen.logEvent("Payload size="+payload.length);
        } catch(NFCException e) {
            Utilities.log("XXXX NFCException using helper classes to parse NDEF message:"+e.getMessage());
        }
        
    }

    
    /*
     * This code has been replaced by the new 7.1 Helper classes. Kept here for reference purposes.
     */
    
    private void diyParsing() {
        /*
         * The NDEF Message may consist of a number of NDEF records
         */
        NDEFRecord[] records = _message.getRecords();
        /*
         * This is the number of NDEF records in the NDEF message
         */
        int numRecords = records.length;

        Utilities.log("XXXX NfcReadNdefSmartTagUpdate in runnable #records=" + numRecords);

        /*
         * Only unpick the message if it contains a non-zero number of records
         */
        if(numRecords > 0) {
            /*
             * Work our way through each record in the message in turn
             */
            for(int j = numRecords - 1; j >= 0; j--) {
                Utilities.log("XXXX NfcReadNdefSmartTagUpdate processing NDEF record#=" + j);
                /*
                 * Extract the NDEF record payload as an array of bytes
                 * 
                 * This is an example of what an NDEF Smart Poster payload looks like representing:
                 * 
                 * Text (en_US): This is a title for the BBC URI URI : http://www.bbc.co.uk
                 * 
                 * Original length 59 decimal bytes
                 * 
                 * NDEF PayLoad:
                 * 
                 * 99 01 25 01 54 31 05 65 6E 2D :..%.T1.en-: 55 53 54 68 69 73 20 69 73 20 :USThis is : 61 20 74 69 74 6C 65 20
                 * 66 6F :a title fo: 72 20 74 68 65 20 42 42 43 20 :r the BBC : 55 52 49 59 01 0A 01 55 32 01 :URIY...U2.: 62 62
                 * 63 2E 63 6F 2E 75 6B :bbc.co.uk :
                 * 
                 * Notice that "http://www." is so common it has a special binary encoding and only the remainder of the URL is
                 * text encoded "bbc.co.uk"
                 */
                byte[] payloadBytes = records[j].getPayload();
                /*
                 * Parse and display
                 */
                StringBuffer hexPayload = new StringBuffer();
                StringBuffer characterPayload = new StringBuffer();

                String hexpair;

                int numberOfHexPairs = 8;

                for(int i = 0; i < payloadBytes.length; i++) {
                    hexpair = byte2HexPair(payloadBytes[i]);
                    characterPayload.append(byte2Ascii(payloadBytes[i]));

                    hexPayload.append(hexpair + " " + (((i + 1) % numberOfHexPairs == 0) ? "\n" : ""));
                    characterPayload.append((((i + 1) % numberOfHexPairs == 0) ? "\n" : ""));
                }
                /*
                 * Parse the elements of the NDEF record It should identify WELL_KNOWN, TEXT or URI and "Sp" for smart poster
                 */
                String record = "Root NDEF Record\nID: " + records[j].getId() + "\nType: " + records[j].getType() + "\nTNF: "
                        + records[j].getTypeNameFormat() + "\nPayload Len: " + records[j].getPayload().length
                        + "\nHex Payload: \n" + hexPayload + "\nCharacter Payload: \n" + characterPayload;
                _screen.logEvent(record);

                /*
                 * If we recognise this as a Smart Tag Type "Sp"
                 */
                if("Sp".equals(records[j].getType())) {
                    Utilities.log("XXXX NfcReadNdefSmartTagUpdate recognised a Smart Poster Message");
                    try {
                        NDEFMessage smartPosterMessage = new NDEFMessage(records[j].getPayload());
                        NDEFRecord[] spRecords = smartPosterMessage.getRecords();
                        int numSpRecords = spRecords.length;
                        Utilities.log("XXXX NfcReadNdefSmartTagUpdate in runnable #records=" + numSpRecords);

                        if(numSpRecords > 0) {
                            Utilities.log("XXXX NfcReadNdefSmartTagUpdate processing Smart Poster Message");
                            for(int k = numSpRecords - 1; k >= 0; k--) {
                                Utilities.log("XXXX NfcReadNdefSmartTagUpdate processing SP record#=" + k);

                                byte[] spPayloadBytes = spRecords[k].getPayload();

                                hexPayload = new StringBuffer();
                                characterPayload = new StringBuffer();

                                for(int i = 0; i < spPayloadBytes.length; i++) {
                                    hexpair = byte2HexPair(spPayloadBytes[i]);
                                    characterPayload.append(byte2Ascii(spPayloadBytes[i]));

                                    hexPayload.append(hexpair + " " + (((i + 1) % numberOfHexPairs == 0) ? "\n" : ""));
                                    characterPayload.append((((i + 1) % numberOfHexPairs == 0) ? "\n" : ""));
                                }

                                String spRecordLog = "Subsidiary NDEF Record\nID: " + spRecords[k].getId() + "\nType: "
                                        + spRecords[k].getType() + "\nTNF: " + spRecords[k].getTypeNameFormat()
                                        + "\nPayload Len: " + spRecords[k].getPayload().length + "\nHex Payload: \n" + hexPayload
                                        + "\nCharacter Payload: \n" + characterPayload;
                                _screen.logEvent(spRecordLog);

                                if((spRecords[k].getTypeNameFormat() == NDEFRecord.TNF_WELL_KNOWN)
                                        && "T".equals(spRecords[k].getType())) {
                                    /*
                                     * Well Known Type "T" is a TEXT record
                                     */
                                    String text = new String();
                                    String langCode = new String();

                                    int statusByte = spPayloadBytes[0]; // bit 7 indicates UTF-8 if 0, UTF-16 if 1. Bits 5..0 len
                                                                        // of IANA language code.
                                    boolean is_utf16 = Utilities.isUtf16Encoded(statusByte);
                                    int iana_language_code_len = Utilities.getIanaLanguageCodeLength(statusByte);

                                    // extract the IANA language code as an ASCII string
                                    byte[] iana_lang_code_bytes = new byte[iana_language_code_len];
                                    if(iana_language_code_len > 0) {
                                        for(int m = 0; m < iana_language_code_len; m++) {
                                            iana_lang_code_bytes[m] = spPayloadBytes[m + 1];
                                        }
                                    }
                                    langCode = new String(iana_lang_code_bytes, "US-ASCII");

                                    // extract the text which may be UTF-8 or UTF-16 encoded depending on bit 7 of the status byte
                                    byte[] text_bytes = new byte[spPayloadBytes.length - iana_language_code_len + 1];
                                    int i = 0;
                                    for(int m = iana_language_code_len + 1; m < spPayloadBytes.length; m++) {
                                        text_bytes[i] = spPayloadBytes[m];
                                        i++;
                                    }
                                    if(!is_utf16) {
                                        text = new String(text_bytes, "UTF-8");
                                    } else {
                                        text = new String(text_bytes, "UTF-16");
                                    }

                                    _screen.logEvent("Language: " + langCode);
                                    _screen.logEvent("Text: " + text);

                                } else if((spRecords[k].getTypeNameFormat() == NDEFRecord.TNF_WELL_KNOWN)
                                        && "U".equals(spRecords[k].getType())) {
                                    /*
                                     * Well Known Type "U" is a URI record
                                     */
                                    StringBuffer urlBuffer = new StringBuffer();
                                    int urlOffset = 1;

                                    // Handle the most common short prefixes

                                    if(spPayloadBytes[0] == (byte) 0x01) {
                                        urlBuffer.append("http://www.");

                                    } else if(spPayloadBytes[0] == (byte) 0x02) {
                                        urlBuffer.append("https://");

                                    } else if(spPayloadBytes[0] == (byte) 0x03) {
                                        urlBuffer.append("http://");

                                    } else if(spPayloadBytes[0] == (byte) 0x04) {
                                        urlBuffer.append("https://");
                                    }

                                    // extract the URI which must be UTF-8 encoded
                                    byte[] uri_bytes = new byte[spPayloadBytes.length - 1];

                                    int i = 0;
                                    for(int m = urlOffset; m < spPayloadBytes.length; m++) {
                                        uri_bytes[i] = spPayloadBytes[m];
                                        i++;
                                    }
                                    urlBuffer.append(new String(uri_bytes, "UTF-8"));
                                    _screen.logEvent("URL: " + urlBuffer);
                                }
                            }
                        } else {
                            Utilities.log("XXXX NfcReadNdefSmartTagUpdate empty Smart Poster Message");
                        }

                    } catch(BadFormatException e) {
                        Utilities.log("XXXX NfcReadNdefSmartTagUpdate bad NDEF format message");
                        e.printStackTrace();
                    } catch(NFCException e) {
                        Utilities.log("XXXX NfcReadNdefSmartTagUpdate NFC Exception");
                        e.printStackTrace();
                    } catch(UnsupportedEncodingException e) {
                        Utilities.log("XXXX NfcReadNdefSmartTagUpdate UnsupportedEncodingException");
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Utilities.log("XXXX NfcReadNdefSmartTagUpdate no records in message");
            _screen.logEvent("This is not the tag you're looking for!\n" + "It contains no records!\n"
                    + "You should search elsewhere!");
        }

    }

    /*
     * Helper to represent a byte as a printable ASCII character
     */
    private char byte2Ascii(byte b) {
        char character = '.';
        if((20 <= b) && (126 >= b)) {
            character = (char) b;
        }
        return character;
    }

    /*
     * Helper to represent a byte as a printable hex pair.
     */
    private String byte2HexPair(byte b) {
        String hex;
        hex = "00" + Integer.toHexString(b);
        hex = hex.substring(hex.length() - 2).toUpperCase();
        return hex;
    }
}
