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

import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;

abstract public class ProtocolMessage {
    
    byte message_id;
    
    public static ProtocolMessage makeMessage(byte [] data) {
        if(data.length >= 4) {
            byte messageid = data[0];
            switch(messageid) {
            case Constants.PROTOCOL_MASTER_BID:
                ProtocolMessageMasterBid pmsg_mb = new ProtocolMessageMasterBid();
                pmsg_mb.demarshall(data);
                Utilities.log("XXXX rcvd:"+pmsg_mb.toString());
                return pmsg_mb;
            case Constants.PROTOCOL_TURN_OVER:
                ProtocolMessageTurnOver pmsg_to = new ProtocolMessageTurnOver();
                pmsg_to.demarshall(data);
                Utilities.log("XXXX rcvd:"+pmsg_to.toString());
                return pmsg_to;
            }
        }
        return null;

    }
    
    
    abstract public byte [] marshall();
    
    abstract public void demarshall(byte [] bytes);
    
    public byte getMessage_id() {
        return message_id;
    }
    public void setMessage_id(byte message_id) {
        this.message_id = message_id;
    }

    public String toString() {
        return "message_id="+message_id;
    }

}
