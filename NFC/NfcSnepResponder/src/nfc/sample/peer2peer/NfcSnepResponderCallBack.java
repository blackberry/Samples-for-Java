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

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.push.NDEFPushStatusCallback;
import nfc.sample.peer2peer.ui.NfcSnepResponderScreen;

public class NfcSnepResponderCallBack implements NDEFPushStatusCallback {

    private NfcSnepResponderScreen _screen;

    public NfcSnepResponderCallBack(NfcSnepResponderScreen screen) {
        this._screen = screen;
    }

    public void onNDEFPushStatusUpdate(NDEFMessage ndefMessage, int status) {

        String textStatus;
        byte [] ndefMessageAsBytes;
        
        switch (status) {
        
            case NDEFPushStatusCallback.SUCCESS:
                textStatus = "SUCCESS";
                break;
    
            case NDEFPushStatusCallback.REJECTED:
                textStatus = "REJECTED";
                break;
                
            case NDEFPushStatusCallback.PUSH_ERROR:
                textStatus = "PUSH_ERROR";
                break;
                
            case NDEFPushStatusCallback.INVALID_DATA:
                textStatus = "INVALID_DATA";
                break;
                
            case NDEFPushStatusCallback.DATA_TOO_LARGE:
                textStatus = "DATA_TOO_LARGE";
                break;
                
            default:
                textStatus = "UNDEFINED";
                break;
        }
        
        Utilities.log("XXXX NfcSnepResponder Received Status Callback: " + textStatus);
        _screen.logEvent("rcvd:Received Status Callback: " + textStatus);
    
        try {
            ndefMessageAsBytes = ndefMessage.getBytes();
            _screen.logEvent("rcvd:Callback msg " + Utilities.formatAsHexPairs(Constants.DISPLAY_COLUMN_WIDTH, ndefMessageAsBytes));
            
        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcSnepResponder exception examining NDEFMesage: " + e.getMessage());
            _screen.logEvent("rcvd:Exception examining NDEFMesage: " + e.getMessage());
        }
    }
}
