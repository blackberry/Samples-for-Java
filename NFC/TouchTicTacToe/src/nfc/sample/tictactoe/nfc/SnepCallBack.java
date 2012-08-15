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
import net.rim.device.api.io.nfc.push.NDEFPushStatusCallback;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.protocol.GameMessageProcessor;

public class SnepCallBack implements NDEFPushStatusCallback {
    
    GameMessageProcessor _processor;
    
    public SnepCallBack(GameMessageProcessor processor) {
        _processor = processor;
    }

    public void onNDEFPushStatusUpdate(NDEFMessage message, int status) {

        String status_text;

        switch(status) {

        case NDEFPushStatusCallback.SUCCESS:
            status_text = "Ready";
            break;
        case NDEFPushStatusCallback.REJECTED:
            status_text = "REJECTED";
            break;
        case NDEFPushStatusCallback.PUSH_ERROR:
            status_text = "PUSH_ERROR";
            break;
        case NDEFPushStatusCallback.INVALID_DATA:
            status_text = "INVALID_DATA";
            break;
        case NDEFPushStatusCallback.DATA_TOO_LARGE:
            status_text = "DATA_TOO_LARGE";
            break;
        default:
            status_text = "UNDEFINED";
            break;
        }
        Utilities.log("XXXX SNEP message status:"+status_text);
        _processor.sentMessagestatusUpdate(message, status, status_text);
    }
}