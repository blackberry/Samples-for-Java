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


import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFMessageListener;
import net.rim.device.api.io.nfc.ndef.NDEFRecord;
import net.rim.device.api.util.ByteArrayUtilities;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.protocol.GameMessageProcessor;
import nfc.sample.tictactoe.protocol.ProtocolMessage;

public class NfcReceiver implements NDEFMessageListener {

    GameMessageProcessor _processor;
    
    public NfcReceiver(GameMessageProcessor processor) {
        _processor = processor;
    }

    public void onNDEFMessageDetected(NDEFMessage msg) {
        NDEFRecord[] records = msg.getRecords();
        if (records.length > 0) {
            byte[] payload = records[0].getPayload();
            Utilities.log("XXXX NfcReceiver payload=" + ByteArrayUtilities.byteArrayToHex(payload));
            ProtocolMessage pmsg = ProtocolMessage.makeMessage(payload);
            _processor.gameMessage(pmsg);
        }
        
    }

}
