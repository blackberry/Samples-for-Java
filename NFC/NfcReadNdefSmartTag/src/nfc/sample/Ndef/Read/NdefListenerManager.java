package nfc.sample.Ndef.Read;
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
import nfc.sample.Ndef.Read.ui.ListenerControlScreen;

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

    /*
     * This registers with the NFC Reader Writer Manager indicating our interest in receiving notification when Smart Tags come
     * within range of the NFC antenna
     */
    public void registerListener(NfcReadNdefSmartTagListener listener) {
        ReaderWriterManager nfcManager;
        Utilities.log("XXXX NfcReadNdefSmartTag about to add NDEF Message Listener");
        try {
            nfcManager = ReaderWriterManager.getInstance();
            /*
             * An NDEF record always contains:
             * 
             * * 3-bit TNF (Type Name Format) field: Indicates how to
             *   interpret the type field 
             * * Variable length type: Describes
             *   the record format 
             * * Variable length ID: A unique identifier
             *   for the record 
             * * Variable length payload: The actual data
             *   payload
             * 
             * The underlying record representation may be chunked across
             * several NDEF records when the payload is large.
             * 
             * NDEFRecord.TNF_WELL_KNOWN - indicates that we are interested
             * in NDEF records of Type Name Format (TNF): "WELL_KNOWN"
             * 
             * TNF_WELL_KNOWN included NFC RTD (Record Type Definitions)
             * types such as RTD_TEXT and RTD_URI meaning that they will
             * contain URIs or TEXT suitably encoded according to the NFC
             * RTD specification
             * 
             * "Sp" identifies the record type as Smart Poster - so we're
             * only interested in being notified of Smart Poster records
             * that contain plain text or URIs
             * 
             * The final parameter "true" identifies that the application
             * will be auto-started if it is not currently running when a
             * smart tag is brought within range of the NFC antenna.
             * 
             * This setting is persistent across device power resets.
             * 
             * When the application is restarted automatically on
             * presentation of a suitable smart tag to the device the
             * application will be re-started but it is necessary to
             * re-establish the NDEFMessageListener.
             * 
             * Upon registering the listener again the tag that caused the
             * application to be re-started is immediately presented to the
             * listener making what is really a two stage process seem
             * seamless.
             * 
             * It is important to re-start the listener within about 15 seconds
             * of the application being autostarted. Failure to do so will
             * result in the queued message being lost.
             * 
             * There is also no way to determine if the application was 
             * autostarted should you wish to automatically restart the 
             * listener in this case. Of course you could always leave your
             * own tracks in the sand using the Persistent Store.
             * 
             * This behaviour suggests that using the autostart option whilst
             * managing the setting up of the listener via the UI is a combination
             * that can lead to unexpected behaviour.
             * 
             */
            nfcManager.addNDEFMessageListener(listener,
                    NDEFRecord.TNF_WELL_KNOWN, "Sp", true);
            Utilities.log("XXXX NfcReadNdefSmartTag add NDEF Message Listener success");
            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(true));
            ListenerControlScreen.getInstance().setLed();

        } catch (NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcReadNdefSmartTag NFCException on register");
        }
    }

    /*
     * This unregisters with the NFC Reader Writer Manager We no longer want to be notified of Smart Tag events
     */
    public void unRegisterListener() {
        ReaderWriterManager nfcManager;
        Utilities.log("XXXX NfcReadNdefSmartTag about to remove NDEF Message Listener");
        try {
            nfcManager = ReaderWriterManager.getInstance();
            /*
             * This is the converse of the call to register our interest in tags of a certain type. It should identify the same
             * TNF and record type as when it was registered.
             */
            nfcManager.removeNDEFMessageListener(NDEFRecord.TNF_WELL_KNOWN, "Sp");
            Utilities.log("XXXX NfcReadNdefSmartTag remove NDEF Message Listener success");
            rts.replace(Constants.LISTENER_STATE_TOKEN, new Boolean(false));
            ListenerControlScreen.getInstance().setLed();
        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcReadNdefSmartTag NFCException on unregister");
        }
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

}
