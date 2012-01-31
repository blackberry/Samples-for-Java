package nfc.sample.Ndef.Write;
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
import net.rim.device.api.io.nfc.readerwriter.ReaderWriterManager;
import net.rim.device.api.io.nfc.readerwriter.Target;
import net.rim.device.api.system.RuntimeStore;
import nfc.sample.Ndef.Write.ui.TagCreatorScreen;



public class DetectionListenerManager {

    private static DetectionListenerManager _mgr;

    private RuntimeStore rts = RuntimeStore.getRuntimeStore();

    private TagCreatorScreen _log_screen;
    
    private DetectionListenerManager() {
    }

    public synchronized static DetectionListenerManager getInstance() {
        if(_mgr == null) {
            _mgr = new DetectionListenerManager();
        }
        return _mgr;
    }

    public boolean is_listening() {
        Boolean state = (Boolean) rts.get(Constants.LISTENER_STATE_TOKEN);
        if (state != null) {
            Utilities.log("XXXX isListening:"+state);
            return state.booleanValue();
        } else {
            Utilities.log("XXXX isListening: token not found. defaulting to false");
            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(false));
            return false;
        }
    }

    public void registerListener(NfcWriteNdefSmartTagListener nfcWriteNdefSmartTagListener) {

        ReaderWriterManager nfcManager;

        Utilities.log("XXXX NfcWriteNdefSmartTag about to add Detection Listener");

        try {
            nfcManager = ReaderWriterManager.getInstance();
            nfcManager.addDetectionListener(nfcWriteNdefSmartTagListener, new int[]{Target.NDEF_TAG});

            Utilities.log("XXXX NfcWriteNdefSmartTag add Detection Listener success");
            logEvent("Detection Listener added");

            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(true));

        } catch (NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcWriteNdefSmartTag NFCException on register");
            logEvent("Detection Listener add failed");
        }
    }

    public void unRegisterListener(NfcWriteNdefSmartTagListener nfcWriteNdefSmartTagListener) {

        ReaderWriterManager nfcManager;
        
        Utilities.log("XXXX NfcWriteNdefSmartTag about to remove Detection Listener");
        
        try {
            nfcManager = ReaderWriterManager.getInstance();
            /*
             * This is the converse of the call to register our interest in
             * tags of a certain type.
            */
            nfcManager.removeDetectionListener(nfcWriteNdefSmartTagListener);
            
            Utilities.log("XXXX NfcWriteNdefSmartTag remove Detcetion Listener success");
            logEvent("Detection Listener removed");

            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(false));

        } catch (NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcWriteNdefSmartTag NFCException on unregister");
            logEvent("Detection Listener remove failed");
        }
    }
    public void set_log_screen(TagCreatorScreen _log_screen) {
        this._log_screen = _log_screen;
    }
    
    private void logEvent(String msg) {
        if (_log_screen != null) {
            _log_screen.logEvent(msg);
        }
    }
}
