package nfc.sample.racetimebb7;

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
import net.rim.device.api.io.nfc.ndef.NDEFRecord;
import net.rim.device.api.io.nfc.readerwriter.ReaderWriterManager;
import net.rim.device.api.system.RuntimeStore;

public class NdefListenerManager {

    private static NdefListenerManager _mgr;

    private RuntimeStore rts = RuntimeStore.getRuntimeStore();

    private NdefListenerManager() {
    }

    public synchronized static NdefListenerManager getInstance() {
        if(_mgr == null) {
            _mgr = new NdefListenerManager();
        }
        return _mgr;
    }

    public void registerListener(Listener listener) {
//        unRegisterListener(); // just in case already registered
        ReaderWriterManager nfcManager;
        try {
            nfcManager = ReaderWriterManager.getInstance();
            nfcManager.addNDEFMessageListener(listener, NDEFRecord.TNF_EXTERNAL, "my.rim.com:myrecordtype", true);
            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(true));
            Utilities.log("XXXX listener registered");
        } catch(NFCException e) {
            Utilities.log("XXXX "+e.getClass().getName()+":"+e.getMessage());
        }
    }

    public void unRegisterListener() {
        ReaderWriterManager nfcManager;
        try {
            nfcManager = ReaderWriterManager.getInstance();
            nfcManager.removeNDEFMessageListener(NDEFRecord.TNF_EXTERNAL, "my.rim.com:myrecordtype");
            Utilities.log("XXXX NfcReadNdefSmartTag remove NDEF Message Listener success");
            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(false));
        } catch(NFCException e) {
            Utilities.log("XXXX "+e.getClass().getName()+":"+e.getMessage());
        }
    }

    public boolean is_listening() {
        Boolean state = (Boolean) rts.get(Constants.LISTENER_STATE_TOKEN);
        if(state != null) {
            return state.booleanValue();
        } else {
            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(false));
            return false;
        }
    }

}
