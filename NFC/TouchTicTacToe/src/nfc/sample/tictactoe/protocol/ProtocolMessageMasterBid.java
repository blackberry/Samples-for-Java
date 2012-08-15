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

import java.util.Random;

import net.rim.device.api.util.ByteArrayUtilities;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;

public class ProtocolMessageMasterBid extends ProtocolMessage {

    private int bid;
    
    public ProtocolMessageMasterBid() {
        message_id = Constants.PROTOCOL_MASTER_BID;
        Random rand = new Random(System.currentTimeMillis());
        bid = rand.nextInt(1000000);
    }
    
    public byte[] marshall() {
        byte [] bid_bytes = Utilities.toBytes(bid);
        byte [] bytes = new byte[1+bid_bytes.length];
        bytes [0] = message_id;
        System.arraycopy(bid_bytes, 0, bytes, 1, bid_bytes.length);
        return bytes;
    }

    public void demarshall(byte[] bytes) {
        Utilities.log("XXXX demarshalling:"+ByteArrayUtilities.byteArrayToHex(bytes));
        if (bytes.length != 5) {
            Utilities.log("XXXX invalid parameter length passed to ProtocolMessageMasterBid demarshall:"+ bytes.length);
            return;
        }
        message_id = bytes[0];
        bid = Utilities.toInt(bytes,1);
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String toString() {
        String string = super.toString() + ",ProtocolMessageMasterBid" + ",bid="+bid;
        return string;
     }
}
