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
package nfc.sample.tictactoe.protocol;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.NDEFRecord;
import net.rim.device.api.io.nfc.push.NDEFPushManager;
import net.rim.device.api.io.nfc.readerwriter.ReaderWriterManager;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.nfc.NfcReceiver;
import nfc.sample.tictactoe.nfc.NfcSender;
import nfc.sample.tictactoe.nfc.SnepCallBack;

public class GameProtocol {

    private GameMessageProcessor game_message_processor;
    private ReaderWriterManager nfc_manager;
    private NDEFPushManager ndef_push_manager;
    private SnepCallBack status_monitor;
    private NfcSender sender;
    private NfcReceiver receiver;
    private int push_id;
    private boolean listening = false;
    private byte last_message_type_sent;

    public GameProtocol(GameMessageProcessor game_message_processor) throws NFCException {
        this.game_message_processor = game_message_processor;
        receiver = new NfcReceiver(game_message_processor);
        ndef_push_manager = NDEFPushManager.getInstance();
        nfc_manager = ReaderWriterManager.getInstance();
    }

    public void sendMasterBid(ProtocolMessageMasterBid my_bid) throws NFCException {
        Utilities.log("XXXX sending master bid:" + my_bid.toString());
        sender = new NfcSender(my_bid);
        status_monitor = new SnepCallBack(game_message_processor);
        push_id = ndef_push_manager.pushNDEF(sender, status_monitor);
        last_message_type_sent = Constants.PROTOCOL_MASTER_BID;
        Utilities.log("XXXX push_id=" + push_id);
    }

    public void prepareTurnOver(ProtocolMessageTurnOver turn_over) throws NFCException {
        Utilities.log("XXXX prepareTurnOver:" + turn_over.toString());
        nfc_manager.removeNDEFMessageListener(NDEFRecord.TNF_EXTERNAL, Constants.SNEP_DOMAIN + ":" + Constants.SNEP_TYPE);
        listening = false;
        sender = new NfcSender(turn_over);
        status_monitor = new SnepCallBack(game_message_processor);
        push_id = ndef_push_manager.pushNDEF(sender, status_monitor);
        last_message_type_sent = Constants.PROTOCOL_TURN_OVER;
    }

    public void resetBidState(ProtocolMessageResetBidState reset) throws NFCException {
        Utilities.log("XXXX sending reset bid state message:" + reset.toString());
        sender = new NfcSender(reset);
        status_monitor = new SnepCallBack(game_message_processor);
        push_id = ndef_push_manager.pushNDEF(sender, status_monitor);
        last_message_type_sent = Constants.PROTOCOL_RESET_BID_STATE;
        Utilities.log("XXXX push_id=" + push_id);
    }

    public void disableMessaging() throws NFCException {
        Utilities.log("XXXX disabling NFC messaging");
        nfc_manager.removeNDEFMessageListener(NDEFRecord.TNF_EXTERNAL, Constants.SNEP_DOMAIN + ":" + Constants.SNEP_TYPE);
        ndef_push_manager.cancelNDEFPush(push_id);
        listening = false;
    }

    public void cancelPushRegistration() throws NFCException {
        Utilities.log("XXXX canceling push registration");
        ndef_push_manager.cancelNDEFPush(push_id);
    }

    public void listenForMessages() {
        Utilities.log("XXXX listenForMessages");
        try {
            if(!listening) {
                nfc_manager.addNDEFMessageListener(receiver, NDEFRecord.TNF_EXTERNAL, Constants.SNEP_DOMAIN + ":" + Constants.SNEP_TYPE);
                listening = true;
            }
        } catch(NFCException e) {
            Utilities.log("XXXX listenForMessages:" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public void stopListeningForMessages() {
        Utilities.log("XXXX stopListeningForMessages");
        try {
            nfc_manager.removeNDEFMessageListener(NDEFRecord.TNF_EXTERNAL, Constants.SNEP_DOMAIN + ":" + Constants.SNEP_TYPE);
            listening = false;
        } catch(NFCException e) {
            Utilities.log("XXXX stopListeningForMessages:" + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public byte getLast_message_type() {
        return last_message_type_sent;
    }

    public void setLast_message_type(byte last_message_type) {
        this.last_message_type_sent = last_message_type;
    }

}
