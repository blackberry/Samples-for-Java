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

import net.rim.device.api.io.nfc.emulation.VirtualISO14443Part4TargetCallback;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.Arrays;
import nfc.sample.virtual.target.ui.NfcVirtTargScreen;

public class NfcVirtTargListener implements VirtualISO14443Part4TargetCallback {

    private NfcVirtTargScreen _screen;

    public NfcVirtTargListener(NfcVirtTargScreen nfcVirtTargScreen) {
        this();
        this._screen = nfcVirtTargScreen;
        Utilities.log("XXXX NfcVirtTargListener in constructor");
    }

    public NfcVirtTargListener() {
        super();
    }

    public void onVirtualTargetEvent(int targetEvent) {
        /*
         * This detects events related to the activation or deactivation of the virtual target by an external reader.
         * 
         * In the case of this sample application this will be another BlackBerry device.
         */
        String displayableTargetEvent;

        switch(targetEvent) {
        case VirtualISO14443Part4TargetCallback.DEACTIVATED:
            displayableTargetEvent = "Deactivated";
            break;
        case VirtualISO14443Part4TargetCallback.EMULATION_STOPPED:
            displayableTargetEvent = "Emulation stopped";
            break;
        case VirtualISO14443Part4TargetCallback.SELECTED:
            displayableTargetEvent = "Target Selected";
            break;
        default:
            displayableTargetEvent = "Unrecognised event type";
            break;
        }

        Utilities.log("XXXX NfcVirtTargListener onVirtualTargetEvent:" + displayableTargetEvent);
        _screen.logEvent("info:" + displayableTargetEvent);
    }

    public byte[] processCommand(byte[] command) {

        /*
         * This method receives APDUs that originate from the reader that has activated this virtual target.
         * 
         * The basic protocol sequence that is of importance is as follows:
         * 
         * Device acting as virtual target
         * 
         * Receives C-APDU <- A0 37 00 01 99 00
         * 
         * Sends R-APDU -> 53 65 6e 64 20 74 6f 20 6f 74 68 65 72 20 64 65 76 69 63 65 90 00 Text from screen == S e n d t o o t h
         * e r d e v i c e
         * 
         * There are some other C-APDU's that may need to be handled and responded to.
         * 
         * The reader tries figure out if virtual target is NDE capable as a Type 4 tag.
         * 
         * It does this by sending C-APDUs that are part of the NFC Forum Type-4 tag documentation. This sample show how to handle
         * the NDEF Tag Application Select C-APDU which tries to SELECT the NDEF application on the target if it exists. In this
         * example we are only interested in performing a simple proprietary C-/R-APDU exchange so we reject the select operation
         * as follows:
         * 
         * Receives C-APDU <- 00 A4 04 00 07 D2 76 00 00 85 01 00      (for NFC Forum Type 4 version 1.0 spec)
         * 
         * or
         * 
         * Receives C-APDU <- 00 A4 04 00 07 D2 76 00 00 85 01 01 00   (for NFC Forum Type 4 version 2.0 spec)
         * 
         * Sends R-APDU -> 6A 82
         * 
         * An SW1/SW2 of 6A/82 indicates that NDEF is not SELECTable as an application.
         * 
         * All other C-APDUs are rejected in a similar manner.
         */

        int widthOfData = Constants.DISPLAY_COLUMN_WIDTH;

        String hexPayload = Utilities.formatAsHexPairs(widthOfData, command);
        String characterPayload = Utilities.formatAsPrintable(widthOfData, command);

        String eventlog_msg = "";

        String record = "Command received\n" + "Command Len: " + command.length + "\nHex Payload: \n" + hexPayload
                + "\nCharacter Payload: \n" + characterPayload;

        Utilities.log("XXXX NfcVirtTargListener processCommand=" + hexPayload);
        eventlog_msg = "rcvd:" + hexPayload;
        _screen.logEvent(eventlog_msg);

        String responseText = _screen.getResponse_text();
        Utilities.log("XXXX NfcVirtTargListener processCommand using user defined text value:" + responseText);
        
        byte[] responseTextBytes = responseText.getBytes();
        byte[] responseBytes = new byte[responseTextBytes.length + CommonADPUs.goodResponseRapdu.length];

        if(Arrays.equals(command, CommonADPUs.simpleISO14443Capdu)) {

            // This is the simple proprietary C-APDU that we use to requests the screen contents

            System.arraycopy(responseTextBytes, 0, responseBytes, 0, responseTextBytes.length);

            System.arraycopy(CommonADPUs.goodResponseRapdu, 0, responseBytes, responseTextBytes.length,
                    CommonADPUs.goodResponseRapdu.length);

            record = "resp:" + "Len: " + responseText.length() + "\nHex Response: \n"
                    + Utilities.formatAsHexPairs(widthOfData, responseBytes) + "\nCharacter Response: \n"
                    + Utilities.formatAsPrintable(widthOfData, responseBytes);

            eventlog_msg = "resp:" + Utilities.formatAsHexPairs(widthOfData, responseBytes);

            Utilities.log("XXXX NfcVirtTargListener Response sent=" + Utilities.formatAsHexPairs(widthOfData, responseBytes));
            _screen.logEvent(eventlog_msg);

            return responseBytes;

        } else if(Arrays.equals(command, CommonADPUs.ndefTagApplicationSelectV0Capdu) ||
                  Arrays.equals(command, CommonADPUs.ndefTagApplicationSelectV1Capdu)) {

            // For this simple example say we don't support NDEF by rejecting both forms of 
            // the NDEF Application AIDs (V0 and V1) that are defined by the NFC Forum Specification !

            record = "Responding to an NDEF Tag App Select Command\n" + "Len: " + CommonADPUs.noNdefSupportRapdu.length
                    + "\nHex Response: \n" + Utilities.formatAsHexPairs(widthOfData, CommonADPUs.noNdefSupportRapdu);

            eventlog_msg = "resp:"+Utilities.formatAsHexPairs(widthOfData, CommonADPUs.noNdefSupportRapdu);
            
            Utilities.log("XXXX NfcVirtTargListener Response sent="
                    + Utilities.formatAsHexPairs(widthOfData, CommonADPUs.noNdefSupportRapdu));
            
            _screen.logEvent(eventlog_msg);

            return CommonADPUs.noNdefSupportRapdu;

        } else {

            // Also reject all other C-APDUs in this example with INS not supported

            record = "Not Supported Resonse sent\n" + "Len: " + CommonADPUs.insNotSupportedRapdu.length + "\nHex Response: \n"
                    + Utilities.formatAsHexPairs(widthOfData, CommonADPUs.insNotSupportedRapdu);

            Utilities.log("XXXX NfcVirtTargListener Unsupported APDU received:" + hexPayload);
            Utilities.log("XXXX NfcVirtTargListener Warning sent="
                    + Utilities.formatAsHexPairs(widthOfData, CommonADPUs.insNotSupportedRapdu));
            
            eventlog_msg = "resp:"+Utilities.formatAsHexPairs(widthOfData, CommonADPUs.insNotSupportedRapdu);
            
            _screen.logEvent(eventlog_msg);

            return CommonADPUs.insNotSupportedRapdu;
        }
    }
}
