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

public class ProtocolMessageTurnOver extends ProtocolMessage {

    private int _tile_changed;
    private int _symbol_played;
    
    public ProtocolMessageTurnOver() {
        message_id = Constants.PROTOCOL_TURN_OVER;
    }
    
    public byte[] marshall() {
        byte [] tile_changed_bytes = Utilities.toBytes(_tile_changed);
        byte [] symbol_played_bytes = Utilities.toBytes(_symbol_played);
        byte [] bytes = new byte[9];
        bytes [0] = message_id;
        System.arraycopy(tile_changed_bytes, 0, bytes, 1, 4);
        System.arraycopy(symbol_played_bytes, 0, bytes, 5, 4);
        return bytes;
    }

    public void demarshall(byte[] bytes) {
        Utilities.log("XXXX demarshalling:"+ByteArrayUtilities.byteArrayToHex(bytes));
        if (bytes.length != 9) {
            Utilities.log("XXXX invalid parameter length passed to ProtocolTurnOver demarshall:"+ bytes.length);
            return;
        }
        message_id = bytes[0];
        _tile_changed = Utilities.toInt(bytes,1);
        _symbol_played = Utilities.toInt(bytes,5);
    }

    public String toString() {
        String string = super.toString() + ",ProtocolMessageTurnOver" + ",tile_changed="+_tile_changed+",symbol_changed="+_symbol_played;
        return string;
     }

    public int get_tile_changed() {
        return _tile_changed;
    }

    public void set_tile_changed(int _tile_changed) {
        this._tile_changed = _tile_changed;
    }

    public int get_symbol_played() {
        return _symbol_played;
    }

    public void set_symbol_played(int _symbol_played) {
        this._symbol_played = _symbol_played;
    }
}
