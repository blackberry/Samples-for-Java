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

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFMessageUtils;
import net.rim.device.api.io.nfc.push.NDEFMessageBuilder;
import nfc.sample.peer2peer.ui.NfcSnepResponderScreen;

public class NfcSnepResponderMsgBuilder implements NDEFMessageBuilder {

    private NfcSnepResponderScreen _screen;

    public NfcSnepResponderMsgBuilder(NfcSnepResponderScreen screen) {
        this._screen = screen;
    }

    public NDEFMessage[] buildNDEFMessages() {
        Utilities.log("XXXX NfcSnepResponder Received Push Request");
        _screen.logEvent("rcvd:Received Push Request");

        NDEFMessage[] listOfNdefMessages = null;
        NDEFMessage myNdefMessage;

        try {
            Utilities.log("XXXX NfcSnepResponder Constructing Media Type: " + Constants.VCARD_MIME_TYPE);
            _screen.logEvent("send:Constructing Media Type: " + Constants.VCARD_MIME_TYPE);

            Utilities.log("XXXX NfcSnepResponder Constructing vCard Media Type NDEF Message: " + Constants.VCARD_DATA);
            _screen.logEvent("send:Constructing vCard Media Type NDEF Message: " + Constants.VCARD_DATA);

            myNdefMessage = NDEFMessageUtils.createMediaTypeNDEFMessage(Constants.VCARD_MIME_TYPE,
                    Constants.VCARD_DATA.getBytes("US-ASCII"));

            listOfNdefMessages = new NDEFMessage[] { myNdefMessage };

        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcSnepResponder Exception on creating NDEF Message: " + e.getMessage());
            _screen.logEvent("warn:Failed to create NDEFMessage");

        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcSnepResponder Exception on conversion of vCard data: " + e.getMessage());
            _screen.logEvent("warn:Exception on conversion of vCard data");
        }

        return listOfNdefMessages;
    }
}
