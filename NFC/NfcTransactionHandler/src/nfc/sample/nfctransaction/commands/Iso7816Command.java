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
import javax.microedition.apdu.APDUConnection;
import javax.microedition.io.Connector;

import net.rim.device.api.command.AlwaysExecutableCommand;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.util.ByteArrayUtilities;
import nfc.sample.nfctransaction.Constants;
import nfc.sample.nfctransaction.Settings;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.ui.NfcTransScreen;
import nfc.sample.nfctransaction.ui.buttons.MultiStateButtonField;

/**
 * This command sends a ISO7816-4 SELECT APDU using the JSR177 API followed by a proprietary APDU. For this to work, the target
 * SecureElement must contain an applet with the AID used here. So developers who just run this code to see what happens are
 * likely to see an error from this command. This is to be expected however.
 * 
 * Authors: John Murray and Martin Woolley
 * 
 */

public class Iso7816Command extends AlwaysExecutableCommand {

    public void execute(ReadOnlyCommandMetadata metadata, Object context) {
        MultiStateButtonField btn = (MultiStateButtonField) context;
        if(btn.getMsbState() == Constants.BTN_SELECT_OFF) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " ISO 7816-4 command exiting because button is in OFF state");
            return;
        }
        Settings settings = Settings.getInstance();
        NfcTransScreen screen = NfcTransScreen.getInstance();
        Utilities.log("XXXX " + Thread.currentThread().getName() + " selecting applet");
        APDUConnection apduConn = null;
        // this is an example and completely proprietary APDU included solely to demonstrate the basic use of JSR-177
        // Decode:
        // 0x80 : CLASS = proprietary
        // 0x01 : INS   = invented command! Interpretation will require the selected applet to understand this command
        // 0x00 : P1    = null
        // 0x00 : P1    = null
        byte[] command = { (byte) 0x80, (byte) 0x01, (byte) 0x00, (byte) 0x00 };
        try {
            // Open an APDUConnection to our applet. This results in an ISO 7816-4 SELECT command being sent by the JSR-177 API
            String connection_string = Utilities.makeApduConnectionString(settings.getRegistered_aid());
            Utilities.log("XXXX " + Thread.currentThread().getName() + " ISO 7816-4 command: opening APDUConnection to " + connection_string);
            apduConn = (APDUConnection) Connector.open(Constants.CONNECTION_STRING);
            Utilities.log("XXXX " + Thread.currentThread().getName() + " ISO 7816-4 command: APDUConnection opened");
            screen.setUserMessage("Selected AID OK");
            // Send the APDU and wait for a response - expect an error here unless your applet supports the command that was sent
            Utilities.log("XXXX " + Thread.currentThread().getName() + " ISO 7816-4 command: sending APDU");
            byte[] ret = apduConn.exchangeAPDU(command);
            String resp_string = ByteArrayUtilities.byteArrayToHex(ret);
            Utilities.log("XXXX " + Thread.currentThread().getName() + " ISO 7816-4 command: response=" + resp_string);
            screen.setUserMessage("APDU response=" + resp_string);
            // Close the logical channel connection
            apduConn.close();
        } catch(Exception e) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " ISO 7816-4 command failed:" + e.getClass().getName() + ":" + e.getMessage());
            screen.setUserMessage("Error. Select failed: check log");
        }
    }

}
