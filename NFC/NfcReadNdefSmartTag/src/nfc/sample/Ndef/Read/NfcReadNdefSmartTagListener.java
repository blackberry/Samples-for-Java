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
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFMessageListener;
import net.rim.device.api.ui.UiApplication;
import nfc.sample.Ndef.Read.ui.NfcReadNdefSmartTagScreen;

/**
 * 
 * This class implement the NDEFMessageListener interface and it's primary function is to receive NDEF message notifications when
 * a smart tag is brought into the NFC antenna's range
 * 
 */
public class NfcReadNdefSmartTagListener implements NDEFMessageListener {

    private static NfcReadNdefSmartTagListener _listener;

    private NfcReadNdefSmartTagScreen _screen = NfcReadNdefSmartTagScreen.getInstance();

    private NfcReadNdefSmartTagListener() {
    }

    public synchronized static NfcReadNdefSmartTagListener getInstance() {
        if(_listener == null) {
            _listener = new NfcReadNdefSmartTagListener();
        }
        return _listener;
    }

    /*
     * This is where we get passed an NDEFMessage
     * 
     * Schedule a runnable to handle it in the main event processing loop
     */
    public void onNDEFMessageDetected(NDEFMessage message) {
        Utilities.log("XXXX NfcReadNdefSmartTagListener detected an NDEF message");
        if (!_screen.isDisplayed()) {
            Utilities.log("XXXX NfcReadNdefSmartTagListener thinks event log screen is not displayed so pushing it");
            synchronized(UiApplication.getEventLock()) {
                UiApplication.getUiApplication().pushScreen(_screen);
            }
        } else {
            Utilities.log("XXXX NfcReadNdefSmartTagListener thinks event log screen is already displayed");
        }
        UiApplication.getUiApplication().invokeLater(new NfcReadNdefSmartTagUpdate(message, _screen));
    }
}
