package nfc.sample.nfctransaction.commands;
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
import net.rim.device.api.command.AlwaysExecutableCommand;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.se.SecureElement;
import nfc.sample.nfctransaction.Constants;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.nfc.CardEmulation;
import nfc.sample.nfctransaction.nfc.NfcService;
import nfc.sample.nfctransaction.ui.NfcTransScreen;

// toggle the state of card emulation ON | OFF

public class ToggleCeCommand extends AlwaysExecutableCommand {

	private NfcService nfc_service = NfcService.getInstance();
	private CardEmulation ce = CardEmulation.getInstance();

	public void execute(ReadOnlyCommandMetadata metadata, Object context) {
		NfcTransScreen screen = NfcTransScreen.getInstance();
		Utilities.log("XXXX " + Thread.currentThread().getName() + " toggling card emulation");
		boolean nfc_enabled = false;
		try {
			nfc_enabled = nfc_service.isNfcEnabled();
			screen.setNfcLed(Utilities.ledBooleanToInt(nfc_enabled));
		} catch (NFCException e) {
			Utilities.log("XXXX " + Thread.currentThread().getName() + " exception when establishing NFC service status: " + e.getClass().getName() + ":"
					+ e.getMessage());
			screen.setUserMessage("Failed to establish NFC service status");
			screen.setNfcLed(Constants.LED_ERR);
			return;
		}

		if (!nfc_enabled) {
			Utilities.log("XXXX " + Thread.currentThread().getName() + " NFC seems to be OFF so prompting user to enable it");
			screen.setUserMessage("NFC is off");
			try {
				nfc_service.promptToEnableNFC();
				screen.setCeButtonState(ce.isCeEnabled(SecureElement.BATTERY_ON_MODE));
			} catch (NFCException e) {
				Utilities.log("XXXX " + Thread.currentThread().getName() + " exception when prompting to enable NFC: " + e.getClass().getName() + ":"
						+ e.getMessage());
				screen.setUserMessage("Failed when prompting to enable NFC");
			}
			return;
		}

		boolean ce_enabled = false;
		try {
			ce_enabled = ce.isCeEnabled(SecureElement.BATTERY_ON_MODE);
		} catch (NFCException e1) {
			Utilities
					.log("XXXX " + Thread.currentThread().getName() + " exception when checking CE status: " + e1.getClass().getName() + ":" + e1.getMessage());
			screen.setUserMessage("Error: could not establish CE status");
			screen.setCemLed(Constants.LED_ERR);
			return;
		}
		if (!ce_enabled) {
			try {
				if (nfc_service.isNfcEnabled()) {
					Utilities.log("XXXX " + Thread.currentThread().getName() + " NFC is ON so setting technology types");
					// Route ISO14443x to the Secure Element
					// for battery on, device on mode
					ce_enabled = ce.setRoutingOn(ce.getCurrentTechnologyTypes());
					if (ce_enabled) {
						screen.setCeButtonState(true);
						screen.setSelectButtonState(true);
						screen.setUserMessage("Card emulation enabled OK");
						screen.setCemLed(Constants.LED_ON);
					} else {
						screen.setUserMessage("Card emulation not enabled");
					}
				} else {
					Utilities.log("XXXX " + Thread.currentThread().getName() + " NFC seems to be OFF so prompting user to enable it");
					screen.setUserMessage("NFC is off");
					nfc_service.promptToEnableNFC();
				}
			} catch (NFCException e) {
				Utilities.log("XXXX " + Thread.currentThread().getName() + " exception when setting SE routing: " + e.getClass().getName() + ":"
						+ e.getMessage());
				screen.setUserMessage("Failed to set NFC routing");
				return;
			}
		} else {
			try {
				// Remove routing
				Utilities.log("XXXX " + Thread.currentThread().getName() + " NFC removing NFC SIM SE routing");
				ce_enabled = ce.setRoutingOff();
				if (!ce_enabled) {
					screen.setCeButtonState(false);
					screen.setSelectButtonState(false);
					screen.setUserMessage("Card emulation disabled OK");
					screen.setCemLed(Constants.LED_OFF);
				} else {
					screen.setUserMessage("Card emulation not disabled");
				}
			} catch (NFCException e) {
				Utilities.log("XXXX " + Thread.currentThread().getName() + " exception when setting SE routing: " + e.getClass().getName() + ":"
						+ e.getMessage());
				screen.setUserMessage("Failed to set NFC routing");
				return;
			}
		}

	}

}
