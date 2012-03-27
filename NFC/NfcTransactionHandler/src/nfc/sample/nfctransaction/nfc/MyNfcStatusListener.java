package nfc.sample.nfctransaction.nfc;
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
import net.rim.device.api.io.nfc.NFCManager;
import net.rim.device.api.io.nfc.NFCStatusListener;
import net.rim.device.api.io.nfc.se.SecureElement;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.ui.NfcTransScreen;

// NFCStatusListener implementation that monitors the NFC service and updates the UI when it changes

public class MyNfcStatusListener implements NFCStatusListener {

    private NfcTransScreen screen = NfcTransScreen.getInstance();
    private CardEmulation ce = CardEmulation.getInstance();

    public void onStatusChange(int nfcServicesAvailable) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " onStatusChange:" + nfcServicesAvailable);
        boolean nfc_status = (nfcServicesAvailable != NFCManager.NFC_NONE);
        boolean ce_status = true;
        try {
            ce_status = ce.isCeEnabled(SecureElement.BATTERY_ON_MODE);
            Utilities.log("XXXX " + Thread.currentThread().getName() + " onStatusChange: CE state=" + ce_status);
        } catch(NFCException e) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " : "
                    + e.getClass().getName() + ":" + e.getMessage());
        }
        screen.nfcStateChanged(nfc_status, ce_status);
    }

}
