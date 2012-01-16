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
package nfc.sample.virtual.target;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.microedition.io.Connector;

import net.rim.device.api.io.nfc.readerwriter.DetectionListener;
import net.rim.device.api.io.nfc.readerwriter.ISO14443Part4Connection;
import net.rim.device.api.io.nfc.readerwriter.Target;
import net.rim.device.api.ui.UiApplication;
import nfc.sample.virtual.target.ui.NfcVirtTargScreen;

public class NfcVirtDetectionListener implements DetectionListener {

    private NfcVirtTargScreen _screen;

    public NfcVirtDetectionListener(NfcVirtTargScreen screen) {
        this();
        this._screen = screen;
        Utilities.log("XXXX NfcVirtDetectionListener in constructor");
    }

    public NfcVirtDetectionListener() {
        super();
    }

    public void onTargetDetected(Target target) {

        Utilities.log("XXXX onTargetDetected detected a target");
        _screen.logEvent("info:Target detected");

        if(target.isType(Target.ISO_14443_3)) {
            Utilities.log("XXXX onTargetDetected target is ISO_14443_3");
            _screen.logEvent("info:Target is ISO_14443_3");
        }

        if(target.isType(Target.ISO_14443_4)) {
            Utilities.log("XXXX onTargetDetected target is ISO_14443_4");
            _screen.logEvent("info:Target is ISO_14443_4");
        }

        if(target.isType(Target.NDEF_TAG)) {
            Utilities.log("XXXX onTargetDetected target is NDEF_TAG");
            _screen.logEvent("info:Target is NDEF_TAG");
        }

        if(target.isType(Target.ISO_14443_3) || target.isType(Target.ISO_14443_4) || target.isType(Target.NDEF_TAG)) {

            Enumeration targetProperties = target.getProperties();

            if(targetProperties != null) {

                for(Enumeration e = targetProperties; e.hasMoreElements();) {

                    String targetPropertyName = (String) e.nextElement();
                    String targetPropertyValue = target.getProperty(targetPropertyName);

                    if(!targetPropertyName.equals("SerialNumber")) {

                        String messageText = "info:Property(" + targetPropertyName + ")=" + targetPropertyValue;

                        Utilities.log("XXXX onTargetDetected " + messageText);
                        _screen.logEvent(messageText);

                    } else {
                        byte[] serialNumber=null;
                        try {
                            serialNumber = targetPropertyValue.getBytes("US-ASCII");
                        } catch(UnsupportedEncodingException e1) {
                        }

                        String messageText = "info:Property(" + targetPropertyName + ")="
                                + Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, serialNumber);

                        Utilities.log("XXXX onTargetDetected " + messageText);
                        _screen.logEvent(messageText);
                    }
                }
            }
        }

        if(target.isType(Target.ISO_14443_4)) {
            
            // This is the target type we are interested it. This should be the other BlackBerry 
            // device entering the RF field and emulating a virtual ISO_14443_4 target

            Utilities.log("XXXX onTargetDetected attempting to communicate with ISO_14443_4 target");
            _screen.logEvent("info:Attempting to communicate with ISO_14443_4 target");

            String targetURI = target.getUri(Target.ISO_14443_4);

            ISO14443Part4Connection targetISO14443Part4Connection = null;

            byte[] tranceiveResponse = null;

            try {

                /*
                 *    Now that we have an ISO_14443_4 target in the RF field we will send our simple
                 *    proprietary C-APDU to request the contents of the screen on the other device
                 * 
                 *    The basic protocol sequence that is of importance is as follows:
                 *    
                 *    Device acting as card reader/writer
                 *    
                 *    Sends    C-APDU  -> A0 37 00 01 99  00
                 *    
                 *    Receives R-APDU  <- 53 65 6e 64 20 74 6f 20 6f 74 68 65 72 20 64 65 76 69 63 65 90 00 
                 *    Text from screen ==  S  e  n  d     t  o     o  t  h  e  r     d  e  v  i  c  e
                 *    
                 */
                
                
                String record = "Sending command\n" + "Command Len: " + CommonADPUs.simpleISO14443Capdu.length + "\nPayload: \n"
                        + Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, CommonADPUs.simpleISO14443Capdu);

                Utilities.log("XXXX onTargetDetected: " + record);
                String eventlog_msg="send:"+Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, CommonADPUs.simpleISO14443Capdu);
                _screen.logEvent(eventlog_msg);

                targetISO14443Part4Connection = (ISO14443Part4Connection) Connector.open(targetURI);
                tranceiveResponse = targetISO14443Part4Connection.transceive(CommonADPUs.simpleISO14443Capdu);

                record = "Received:\n" + " Len: " + tranceiveResponse.length + "\nPayload: \n"
                        + Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, tranceiveResponse);
                eventlog_msg = "rcvd:"+Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, tranceiveResponse);

                Utilities.log("XXXX onTargetDetected: " + record);
                _screen.logEvent(eventlog_msg);

                /*
                 * Parse the R-APDU received in response to our command
                 * It contains a payload if it's > 2 bytes in length ( SW1/SW2 status bytes are 2 bytes in length )
                 */
                
                if(tranceiveResponse.length > 2) {
                    byte[] statusCode = new byte[2];
                    byte[] responseMessage = new byte[tranceiveResponse.length - statusCode.length];

                    // parse out the response text and SW1/SW2 fields
                    
                    System.arraycopy(tranceiveResponse, 0, responseMessage, 0, responseMessage.length);
                    System.arraycopy(tranceiveResponse, responseMessage.length, statusCode, 0, statusCode.length);

                    record = "Response details\n" + "Status: \n"
                            + Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, statusCode) + "\nPayload (hex): \n"
                            + Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, responseMessage)
                            + "\nPayload (printable): \n"
                            + Utilities.formatAsPrintable(Constants.DISPLAY_COLUMN_WIDTH, responseMessage);

                    eventlog_msg="resp:status="+ Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, statusCode);
                    _screen.logEvent(eventlog_msg);

                    eventlog_msg="resp:"+ Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, responseMessage);
                    _screen.logEvent(eventlog_msg);

                    eventlog_msg="resp:"+ Utilities.formatAsPrintable(Constants.DISPLAY_COLUMN_WIDTH, responseMessage);
                    _screen.logEvent(eventlog_msg);

                    // Display the text from the other device
                    
                    _screen.logEvent("data:" + new String(responseMessage, "UTF-8"));
                } else {
                    _screen.logEvent("info:Unable to parse response");
                }

            } catch(IOException e) {
                e.printStackTrace();
                Utilities.log("XXXX onTargetDetected Exception: " + e.getMessage());
                _screen.logEvent("info:Exception: " + e.getMessage());
            }

        } else {
            Utilities.log("XXXX onTargetDetected this application only works with a ISO_14443_4 target");
            _screen.logEvent("info:This application only works with a ISO_14443_4 target");
        }
    }
}
