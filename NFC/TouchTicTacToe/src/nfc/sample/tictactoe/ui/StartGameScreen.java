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

package nfc.sample.tictactoe.ui;
import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.push.NDEFPushStatusCallback;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.protocol.GameMessageProcessor;
import nfc.sample.tictactoe.protocol.GameProtocol;
import nfc.sample.tictactoe.protocol.ProtocolMessage;
import nfc.sample.tictactoe.protocol.ProtocolMessageMasterBid;

public class StartGameScreen extends MainScreen implements GameMessageProcessor {

    private GameProtocol proto;
    private Bitmap new_game_screen;
    private TimedLabelField status_message;

    private ProtocolMessageMasterBid my_bid;

    public StartGameScreen() {
        super(Field.USE_ALL_HEIGHT | Field.USE_ALL_WIDTH | Field.FIELD_HCENTER | Field.FIELD_VCENTER);
        AbsoluteFieldManager abmgr = new AbsoluteFieldManager();
        new_game_screen = BitmapFactory.getNewGameScreen();
        Background bg_new_game = BackgroundFactory.createBitmapBackground(new_game_screen);
        abmgr.setBackground(bg_new_game);
        status_message = new TimedLabelField(this, "");
        abmgr.add(status_message);
        add(abmgr);
    }

    public void onUiEngineAttached(boolean attached) {
        Utilities.log("XXXX onUiEngineAttached(" + attached + ")");
        if(attached) {
            initNewGameMessaging();
        }
    }

    public void onExposed() {
        Utilities.log("XXXX onExposed()");
        initNewGameMessaging();
    }

    public void initNewGameMessaging() {
        try {
            // send bid message over SNEP
            proto = new GameProtocol(this);
            my_bid = new ProtocolMessageMasterBid();
            proto.sendMasterBid(my_bid);
            proto.listenForMessages();
        } catch(NFCException e) {
            Utilities.log("XXXX StartGameScreen:" + e.getClass().getName() + ":" + e.getMessage());
            Utilities.popupMessage("Error: separate devices and try again!");
        }
    }

    public void gameMessage(ProtocolMessage message) {
        if(message instanceof ProtocolMessageMasterBid) {
            ProtocolMessageMasterBid other_bid = (ProtocolMessageMasterBid) message;
            int device_status = 0;
            boolean initialised = false;
            if(my_bid.getBid() > other_bid.getBid()) {
                Utilities.log("XXXX won the bid: player 1");
                device_status = Constants.PLAYER_1;
                initialised = true;
            } else {
                if(my_bid.getBid() < other_bid.getBid()) {
                    Utilities.log("XXXX lost the bid: player 2");
                    device_status = Constants.PLAYER_2;
                    initialised = true;
                } else {
                    Utilities.popupMessage("It didn't work, try again!");
                }
            }
            if(initialised) {
                try {
                    proto.disableMessaging();
                } catch(NFCException e) {
                    Utilities.log("XXXX StartGameScreen:" + e.getClass().getName() + ":" + e.getMessage());
                    Utilities.popupMessage("We had a problem, please try again");
                }
                final int player_no = device_status;
                final MainScreen next_screen;
                if(player_no == Constants.PLAYER_2) {
                    next_screen = GameScreen.getInstance(player_no);
                } else {
                    next_screen = new SymbolSelectionScreen();
                }
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().pushScreen(next_screen);
                    }
                });
            }
        }
    }

    public void sentMessagestatusUpdate(NDEFMessage ndefMessage, int status, String status_text) {
        synchronized(UiApplication.getEventLock()) {
            status_message.setLabelText(status_text);
        }
        if(status == NDEFPushStatusCallback.SUCCESS) {
            try {
                proto.cancelPushRegistration();
            } catch(NFCException e) {
                Utilities.log("XXXX exception in cancelPushRegistration:" + e.getClass().getName() + ":" + e.getMessage());
            }
        }
    }

}
