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
import net.rim.device.api.ui.UiApplication;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.ui.TextDetailsProvider;

public class NfcService implements TextDetailsProvider {

	private static NfcService nfc_service;

	public static synchronized NfcService getInstance() {
		if (nfc_service == null) {
			nfc_service = new NfcService();
		}
		return nfc_service;
	}

	public boolean isNfcEnabled() throws NFCException {
		// checks that NFC is enabled and that the required NFC service is
		// available
		Utilities.log("XXXX " + Thread.currentThread().getName() + " establishing NFC service status");
		int service_status = NFCManager.getInstance().getAvailableNFCServices();
		Utilities.log("XXXX " + Thread.currentThread().getName() + " " + Utilities.getNFCServicesSummary());
		boolean nfc_ok = (service_status != NFCManager.NFC_NONE);
		Utilities.log("XXXX " + Thread.currentThread().getName() + " NFC enabled="+nfc_ok);
		return nfc_ok;
	}

	public void promptToEnableNFC() throws NFCException {
		synchronized (UiApplication.getEventLock()) {
			NFCManager.getInstance().enableNFCByPrompt();
		}
	}

	public String getTextDetails() {
		int service_status;
		try {
			service_status = NFCManager.getInstance().getAvailableNFCServices();
			return Utilities.getNfcStatusDescription(service_status);
		} catch (NFCException e) {
			Utilities.log("XXXX " + Thread.currentThread().getName() + " " + e.getClass().getName()+":"+e.getMessage());
			return "Error establishing NFC service description";
		}
	}

}
