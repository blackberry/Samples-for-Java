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
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.commands.SelectTileCommand;
import nfc.sample.tictactoe.game.GameState;
import nfc.sample.tictactoe.game.GameStateChangeListener;
import nfc.sample.tictactoe.protocol.GameMessageProcessor;
import nfc.sample.tictactoe.protocol.GameProtocol;
import nfc.sample.tictactoe.protocol.ProtocolMessage;
import nfc.sample.tictactoe.protocol.ProtocolMessageMasterBid;
import nfc.sample.tictactoe.protocol.ProtocolMessageResetBidState;
import nfc.sample.tictactoe.protocol.ProtocolMessageTurnOver;
import nfc.sample.tictactoe.tiles.MsbConfig;
import nfc.sample.tictactoe.tiles.MsbState;
import nfc.sample.tictactoe.tiles.MultiStateButtonField;

public class GameScreen extends MainScreen implements GameMessageProcessor, TileChangedListener, GameStateChangeListener {

    private static GameScreen _game_screen;
    private GameState game_state;
    private TimedLabelField status;
    private static int _player_no;
    private static int _symbol;

    private GameProtocol proto;
    private ProtocolMessageTurnOver turn_over;
    private ProtocolMessageMasterBid my_bid;
    private ProtocolMessageMasterBid other_bid;
    private ProtocolMessageResetBidState reset = new ProtocolMessageResetBidState();

    private MultiStateButtonField[] tiles = new MultiStateButtonField[9];
    private UiConfig uiconfig;

    private SelectTileCommand cmd_select_tile;

    private int focused_tile = 0;
    private int focused_tile_row = 0;
    private int focused_tile_col = 0;

    private Bitmap win_line_bmp;
    private Bitmap stale_mate_cross_bmp;
    private Bitmap stale_mate_nought_bmp;
    private Bitmap cross_unfocused;
    private Bitmap nought_unfocused;

    public synchronized static GameScreen getInstance(int player_no) {
        Utilities.log("XXXX GameScreen.getInstance(" + player_no + "): height=" + Display.getHeight() + ",width=" + Display.getWidth());
        _player_no = player_no;
        if(_game_screen == null) {
            _game_screen = new GameScreen();
        }
        _game_screen.initialiseGame();
        return _game_screen;
    }

    public synchronized static GameScreen getInstance(int player_no, int symbol) {
        Utilities.log("XXXX GameScreen.getInstance(" + player_no + "," + symbol + "): height=" + Display.getHeight() + ",width=" + Display.getWidth());
        _player_no = player_no;
        _symbol = symbol;
        if(_game_screen == null) {
            _game_screen = new GameScreen();
        }
        _game_screen.initialiseGame();
        return _game_screen;
    }

    private GameScreen() {
        super(USE_ALL_HEIGHT | USE_ALL_WIDTH | FIELD_HCENTER | FIELD_VCENTER | NO_VERTICAL_SCROLL);
        int width = Display.getWidth();
        int height = Display.getHeight();
        uiconfig = UiConfigFactory.getUiConfig(width, height);

        Bitmap blank_unfocused = BitmapFactory.getBlankUnfocused();
        Bitmap blank_focused = BitmapFactory.addStateIndicator(blank_unfocused, Constants.FOCUSED_COLOUR);
        Bitmap blank_clicked = BitmapFactory.addStateIndicator(blank_unfocused, Constants.CLICKED_COLOUR);

        nought_unfocused = BitmapFactory.getNoughtUnfocused();
        Bitmap nought_focused = BitmapFactory.addStateIndicator(nought_unfocused, Constants.FOCUSED_COLOUR);
        Bitmap nought_clicked = BitmapFactory.addStateIndicator(nought_unfocused, Constants.CLICKED_COLOUR);

        cross_unfocused = BitmapFactory.getCrossUnfocused();
        Bitmap cross_focused = BitmapFactory.addStateIndicator(cross_unfocused, Constants.FOCUSED_COLOUR);
        Bitmap cross_clicked = BitmapFactory.addStateIndicator(cross_unfocused, Constants.CLICKED_COLOUR);

        stale_mate_cross_bmp = BitmapFactory.addStateIndicator(cross_unfocused, Constants.STALE_MATE_TILE_COLOUR);
        stale_mate_nought_bmp = BitmapFactory.addStateIndicator(cross_unfocused, Constants.STALE_MATE_TILE_COLOUR);

        AbsoluteFieldManager abmgr = new AbsoluteFieldManager();

        cmd_select_tile = new SelectTileCommand();

        Background board = BackgroundFactory.createBitmapBackground(BitmapFactory.getBackground(width, height));
        abmgr.setBackground(board);
        // Each button has 3 states : blank|nought|cross
        int i = 0;
        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 3; c++) {
                MsbConfig tile_btn_config = new MsbConfig();
                MsbState tile_btn_state_blank = new MsbState(Constants.TILE_STATE_BLANK, "Blank Tile", "Blank Tile");
                tile_btn_state_blank.setbmp_focused(blank_focused);
                tile_btn_state_blank.setbmp_unfocused(blank_unfocused);
                tile_btn_state_blank.setbmp_clicked(blank_clicked);
                tile_btn_state_blank.setbmp_unclicked(blank_focused);
                tile_btn_config.addState(tile_btn_state_blank);
                MsbState tile_btn_state_nought = new MsbState(Constants.TILE_STATE_NOUGHT, "Nought", "Nought");
                tile_btn_state_nought.setbmp_focused(nought_focused);
                tile_btn_state_nought.setbmp_unfocused(nought_unfocused);
                tile_btn_state_nought.setbmp_clicked(nought_clicked);
                tile_btn_state_nought.setbmp_unclicked(nought_focused);
                tile_btn_config.addState(tile_btn_state_nought);
                MsbState tile_btn_state_cross = new MsbState(Constants.TILE_STATE_CROSS, "Cross", "Cross");
                tile_btn_state_cross.setbmp_focused(cross_focused);
                tile_btn_state_cross.setbmp_unfocused(cross_unfocused);
                tile_btn_state_cross.setbmp_clicked(cross_clicked);
                tile_btn_state_cross.setbmp_unclicked(cross_focused);
                tile_btn_config.addState(tile_btn_state_cross);

                MultiStateButtonField msbf_tile = new MultiStateButtonField(tile_btn_config, cmd_select_tile, 0, Field.FIELD_HCENTER);
                msbf_tile.setFocusListener(focus_listener);
                tiles[i] = msbf_tile;
                abmgr.add(msbf_tile, uiconfig.getTileX(i), uiconfig.getTileY(i));
                i++;
            }
        }

        add(abmgr);

        status = new TimedLabelField(this, "Touch devices to end turn!");
        status.setLabelText("You are player #" + _player_no);

        try {
            proto = new GameProtocol(this);
        } catch(NFCException e) {
            setStatusMessage("Error initialising NFC messaging");
            Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
        }

        game_state = GameState.getInstance();
        game_state.setTileUiObjects(tiles);
        game_state.setTileChangedListener(this);
        game_state.setGameStateChangeListener(this);

    }

    private void initialiseGame() {
        turn_over = new ProtocolMessageTurnOver();
        game_state.initialiseGameState();

        // focus on central tile
        focused_tile_row = 1;
        focused_tile_col = 1;

        synchronized(UiApplication.getEventLock()) {
            tiles[4].setFocus();
        }
        game_state.setCurrent_tile(Constants.INITIAL_TILE);

        if(_player_no == Constants.PLAYER_1) {
            cmd_select_tile.set_symbol(_symbol);
            game_state.setGameState(GameState.GAME_STATE_CHANGE_MY_TURN_NOW);
        } else {
            game_state.setBoardLocked(true);
            proto.listenForMessages();
        }

    }

    private void setStatusMessage(String msg) {
        synchronized(UiApplication.getEventLock()) {
            status.setLabelText(msg);
        }
    }

    private void setStatusMessage(String[] msg, boolean continuous) {
        synchronized(UiApplication.getEventLock()) {
            status.setLabelTexts(msg, continuous);
        }
    }

    private FocusChangeListener focus_listener = new FocusChangeListener() {

        public void focusChanged(Field field, int eventType) {
            for(int i = 0; i < 9; i++) {
                if(field == tiles[i] && !game_state.isGame_over()) {
                    focused_tile = i;
                    game_state.setCurrent_tile(i);
                    return;
                }
            }
        }
    };

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        if(!game_state.isGame_over()) {
            Utilities.log("XXXX navigationMovement: dx=" + dx + ",dy=" + dy);

            int x_dir = 0;
            int y_dir = 0;

            if(dx > 0) {
                x_dir = 1;
            }
            if(dx < 0) {
                x_dir = -1;
            }

            if(dy > 0) {
                y_dir = 1;
            }
            if(dy < 0) {
                y_dir = -1;
            }
            setFocusedTile(x_dir, y_dir);
            return true;
        } else {
            return false;
        }
    }

    private void setFocusedTile(int x_dir, int y_dir) {
        focused_tile_col = focused_tile_col + x_dir;
        focused_tile_row = focused_tile_row + y_dir;
        if(focused_tile_col > 2) {
            focused_tile_col = 2;
        }
        if(focused_tile_col < 0) {
            focused_tile_col = 0;
        }
        if(focused_tile_row > 2) {
            focused_tile_row = 2;
        }
        if(focused_tile_row < 0) {
            focused_tile_row = 0;
        }
        focused_tile = focused_tile_row * 3 + focused_tile_col;
        tiles[focused_tile].setFocus();
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        if(!status.getText().equals("")) {
            graphics.setColor(Constants.STATUS_COLOUR);
            graphics.drawText(status.getText(), uiconfig.getStatusX(), uiconfig.getStatusY(), Graphics.HCENTER, Display.getWidth());
        }
    }

    public void tileChanged(int tile) {
        turn_over.set_tile_changed(tile);
        turn_over.set_symbol_played(_symbol);
        if(!game_state.isGame_over() && !game_state.isBoardFull()) {
            Utilities.log("XXXX tileChanged tile_count_this_turn=" + game_state.getTile_count_this_turn());
            if(game_state.getTile_count_this_turn() == 1) {
                // get ready to send the turn over message when players touch devices again
                setStatusMessage("Touch devices to pass turn to other player");
                prepTurnOver();
            } else {
                if(game_state.getTile_count_this_turn() == 0) {
                    // tile was unset so switch off messaging
                    try {
                        proto.disableMessaging();
                    } catch(NFCException e) {
                        Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
                        setStatusMessage("Error: please try again");
                    }
                }
            }
        }
    }

    public void prepTurnOver() {
        try {
            proto.prepareTurnOver(turn_over);
            proto.listenForMessages();
        } catch(NFCException e) {
            Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
            setStatusMessage("Error: please try again");
        }
    }

    public static int get_symbol() {
        return _symbol;
    }

    public void gameMessage(ProtocolMessage message) {
        Utilities.log("XXXX gameMessage:" + message);
        // should indicate it's now our turn
        if(message instanceof ProtocolMessageTurnOver) {
            // OK, it's our turn now!
            ProtocolMessageTurnOver pmsg_to = (ProtocolMessageTurnOver) message;
            Utilities.log("XXXX gameMessage: ProtocolMessageTurnOver");
            game_state.setBoardLocked(false);
            int other_symbol = pmsg_to.get_symbol_played();
            // derive the symbol we're playing with from the one player 1 is using (they got to choose)
            if(other_symbol == Constants.PLAYER_SYMBOL_CROSS) {
                _symbol = Constants.PLAYER_SYMBOL_NOUGHT;
            } else {
                _symbol = Constants.PLAYER_SYMBOL_CROSS;
            }
            cmd_select_tile.set_symbol(_symbol);
            int other_player_tile = pmsg_to.get_tile_changed();
            game_state.updateTileOtherPlayer(other_player_tile, other_symbol);
            setStatusMessage("It's your turn now!");
            proto.stopListeningForMessages();
            return;
        }
        if(message instanceof ProtocolMessageMasterBid) {
            // from this screen, this means "let's start a new game"
            // we may or may not have already sent our bid

            Utilities.log("XXXX gameMessage: ProtocolMessageMasterBid: game_state.isBid_sent()=" + game_state.isBid_sent());
            boolean ok = true;

            other_bid = (ProtocolMessageMasterBid) message;
            game_state.setOther_bid_received(other_bid);
            // if we've received this message then one way or another it must be game over (maybe the other device has won)
            game_state.setGame_over(true);

            if(!game_state.isBid_sent()) {
                my_bid = new ProtocolMessageMasterBid();
                try {
                    proto.sendMasterBid(my_bid);
                    ok = true;
                } catch(NFCException e1) {
                    ok = false;
                    Utilities.log("XXXX " + e1.getClass().getName() + ":" + e1.getMessage());
                    setStatusMessage("Error - try again");
                }
            }

            proto.stopListeningForMessages();

            if(ok) {
                checkBids();
            }
            return;
        }
        if(message instanceof ProtocolMessageResetBidState) {
            Utilities.log("XXXX gameMessage: ProtocolMessageResetBidState");
            proto.stopListeningForMessages();
            setStatusMessage("Please touch devices again");
            resetBidStateTracking();
            return;
        }

    }

    private void checkBids() {
        Utilities.log("XXXX checkBids: game_state.getMy_bid_sent()=" + game_state.getMy_bid_sent() + ",game_state.getOther_bid_received()=" + game_state.getOther_bid_received());
        if(game_state.getMy_bid_sent() == null || game_state.getOther_bid_received() == null) {
            return;
        }
        int device_role = 0;
        if(my_bid.getBid() > other_bid.getBid()) {
            Utilities.log("XXXX won the bid: player 1");
            device_role = Constants.PLAYER_1;
        } else {
            if(my_bid.getBid() < other_bid.getBid()) {
                Utilities.log("XXXX lost the bid: player 2");
                device_role = Constants.PLAYER_2;
            } else {
                setStatusMessage("It didn't work, please try again!");
                return;
            }
        }

        try {
            proto.disableMessaging();
        } catch(NFCException e) {
            Utilities.log("XXXX GameScreen:" + e.getClass().getName() + ":" + e.getMessage());
            setStatusMessage("It didn't work, try again!");
            return;
        }

        resetBidStateTracking();

        final int player_no = device_role;
        final MainScreen next_screen;

        if(player_no == Constants.PLAYER_2) {
            next_screen = GameScreen.getInstance(player_no);
        } else {
            next_screen = new SymbolSelectionScreen();
        }

        final GameScreen this_screen = this;
        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                UiApplication.getUiApplication().popScreen(this_screen);
            }
        });

        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                UiApplication.getUiApplication().pushScreen(next_screen);
            }
        });
    }

    public void resetBidStateTracking() {
        game_state.setBid_sent(false);
        game_state.setMy_bid_sent(null);
        game_state.setOther_bid_received(null);
    }

    public void sentMessagestatusUpdate(NDEFMessage ndefMessage, int status, String status_text) {
        Utilities.log("XXXX push status=" + status_text + ",game_over=" + game_state.isGame_over());
        setStatusMessage(status_text);
        if(status == NDEFPushStatusCallback.SUCCESS) {
            if(proto.getLast_message_type() == Constants.PROTOCOL_TURN_OVER) {
                // we've "told" the other device it is now its turn so we switch to listening
                Utilities.log("XXXX push status=" + status_text + ",PROTOCOL_TURN_OVER");
                game_state.lockBoard();
                setStatusMessage("Board now locked");
                game_state.setGameState(GameState.GAME_STATE_CHANGE_THEIR_TURN_NOW);
                try {
                    proto.cancelPushRegistration();
                } catch(NFCException e) {
                    Utilities.log("XXXX exception in cancelPushRegistration:" + e.getClass().getName() + ":" + e.getMessage());
                }
            }
            if(proto.getLast_message_type() == Constants.PROTOCOL_MASTER_BID) {
                Utilities.log("XXXX push status=" + status_text + ",PROTOCOL_MASTER_BID");
                game_state.setMy_bid_sent(my_bid);
                game_state.setBid_sent(true);
                checkBids();
                try {
                    proto.cancelPushRegistration();
                } catch(NFCException e) {
                    Utilities.log("XXXX exception in cancelPushRegistration:" + e.getClass().getName() + ":" + e.getMessage());
                }
            }
            if(proto.getLast_message_type() == Constants.PROTOCOL_RESET_BID_STATE) {
                Utilities.log("XXXX push status=" + status_text + ",PROTOCOL_RESET_BID_STATE");
                // allow user to try again
                try {
                    setStatusMessage("Touch devices again");
                    proto.sendMasterBid(my_bid);
                } catch(NFCException e) {
                    Utilities.log("XXXX exception in sendMasterBid:" + e.getClass().getName() + ":" + e.getMessage());
                }
            }
        } else {
            // realistically, any other status is worth retrying (DATA_TOO_LARGE not likely with this application)
            if(proto.getLast_message_type() == Constants.PROTOCOL_MASTER_BID || proto.getLast_message_type() == Constants.PROTOCOL_RESET_BID_STATE) {
                resetBidStateTracking();
                try {
                    proto.resetBidState(reset);
                    setStatusMessage("Failed - Please try again");
                } catch(NFCException e) {
                    Utilities.log("XXXX exception in resetBidState:" + e.getClass().getName() + ":" + e.getMessage());
                }
            }
            if(proto.getLast_message_type() == Constants.PROTOCOL_TURN_OVER) {
                setStatusMessage("Touch devices again");
                prepTurnOver();
            }
        }
    }

    public void onGameStateChange(int new_game_state) {
        switch(new_game_state) {
        case GameState.GAME_STATE_CHANGE_THEIR_TURN_NOW:
            Utilities.log("XXXX GAME_STATE_CHANGE_THEIR_TURN_NOW");
            proto.listenForMessages();
            break;
        case GameState.GAME_STATE_CHANGE_MY_TURN_NOW:
            Utilities.log("XXXX GAME_STATE_CHANGE_MY_TURN_NOW");
            myTurn();
            break;
        case GameState.GAME_STATE_CHANGE_GAME_OVER_WON:
            Utilities.log("XXXX GAME_STATE_CHANGE_GAME_OVER_WON");
            gameWon();
            break;
        case GameState.GAME_STATE_CHANGE_GAME_OVER_STALE_MATE:
            Utilities.log("XXXX GAME_STATE_CHANGE_GAME_OVER_STALE_MATE");
            staleMate();
            break;
        case GameState.GAME_STATE_CHANGE_NEW_GAME_STARTED:
            Utilities.log("XXXX GAME_STATE_CHANGE_NEW_GAME_STARTED");
            break;
        }
    }

    private void myTurn() {
        game_state.setBoardLocked(false);
        game_state.setTile_count_this_turn(0);
    }

    private void gameWon() {
        game_state.lockBoard();
        proto.stopListeningForMessages(); // we may already be listening so remove the NDEF listener in anticipation of adding it
                                          // again shortly
        setStatusMessage(Constants.WINNER_MESSAGE, true);
        int[] win_line = game_state.getWinningLine(_symbol);
        if(_symbol == Constants.PLAYER_SYMBOL_CROSS) {
            win_line_bmp = BitmapFactory.addStateIndicator(cross_unfocused, Constants.WIN_LINE_COLOUR);
        } else {
            win_line_bmp = BitmapFactory.addStateIndicator(nought_unfocused, Constants.WIN_LINE_COLOUR);
        }
        for(int i = 0; i < 3; i++) {
            tiles[win_line[i]].setImage(win_line_bmp);
        }
        game_state.setGame_over(true);
        my_bid = new ProtocolMessageMasterBid();
        try {
            proto.sendMasterBid(my_bid);
            proto.listenForMessages();
        } catch(NFCException e) {
            Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
            setStatusMessage("It didn't work, please try again");
        }

        Utilities.popupMessage("You have won!");

    }

    private void staleMate() {
        game_state.lockBoard();
        setStatusMessage(Constants.STALE_MATE_MESSAGE, true);
        for(int i = 0; i < 9; i++) {
            if (game_state.getTileState(i) == Constants.PLAYER_SYMBOL_CROSS) {
                tiles[i].setImage(stale_mate_cross_bmp);
            } else {
                tiles[i].setImage(stale_mate_nought_bmp);
            }
        }
        game_state.setGame_over(true);
        my_bid = new ProtocolMessageMasterBid();
        try {
            proto.sendMasterBid(my_bid);
            proto.listenForMessages();
        } catch(NFCException e) {
            Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
            setStatusMessage("It didn't work, please try again");
        }
        Utilities.popupMessage("Stale mate!");
    }
    
    public boolean onClose() {
        try {
            proto.disableMessaging();
        } catch(NFCException e) {
        }
        return super.onClose();
    }
}
