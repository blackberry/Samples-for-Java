package nfc.sample.peer2peer;

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
