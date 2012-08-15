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
package nfc.sample.tictactoe.nfc;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFMessageUtils;
import net.rim.device.api.io.nfc.push.NDEFMessageBuilder;
import net.rim.device.api.util.ByteArrayUtilities;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.protocol.ProtocolMessage;

public class NfcSender implements NDEFMessageBuilder {

    private ProtocolMessage _message;

    public NfcSender(ProtocolMessage message) {
        _message = message;
    }

    public NDEFMessage[] buildNDEFMessages() {

        Utilities.log("XXXX NfcSender sending ProtocolMessage:" + _message);

        NDEFMessage[] ndef_messages = null;
        NDEFMessage game_message;

        try {

            byte[] payload = _message.marshall();
            Utilities.log("XXXX NfcSender sending payload 0x" + ByteArrayUtilities.byteArrayToHex(payload));            
            game_message = NDEFMessageUtils.createExternalTypeMessage(Constants.SNEP_DOMAIN, Constants.SNEP_TYPE, payload);

            ndef_messages = new NDEFMessage[] { game_message };

        } catch(NFCException e) {
            Utilities.log("XXXX NfcSender:" + e.getClass().getName() + ":" + e.getMessage());
        }
        return ndef_messages;
    }

    public ProtocolMessage get_message() {
        return _message;
    }

    public void set_message(ProtocolMessage _message) {
        this._message = _message;
    }

}
