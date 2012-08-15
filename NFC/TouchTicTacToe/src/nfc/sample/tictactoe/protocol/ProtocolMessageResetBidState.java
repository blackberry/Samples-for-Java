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
package nfc.sample.tictactoe.protocol;

import net.rim.device.api.util.ByteArrayUtilities;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;

public class ProtocolMessageResetBidState extends ProtocolMessage {

    public ProtocolMessageResetBidState() {
        message_id = Constants.PROTOCOL_RESET_BID_STATE;
    }
    
    public byte[] marshall() {
        byte [] bytes = new byte[1];
        bytes [0] = message_id;
        return bytes;
    }

    public void demarshall(byte[] bytes) {
        Utilities.log("XXXX demarshalling:"+ByteArrayUtilities.byteArrayToHex(bytes));
        if (bytes.length != 1) {
            Utilities.log("XXXX invalid parameter length passed to ProtocolMessageResetBidState demarshall:"+ bytes.length);
            return;
        }
        message_id = bytes[0];
    }

    public String toString() {
        String string = super.toString() + ",ProtocolMessageResetBidState";
        return string;
     }
}
