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
package nfc.sample.tictactoe.game;

import net.rim.device.api.ui.UiApplication;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.protocol.ProtocolMessageMasterBid;
import nfc.sample.tictactoe.tiles.MultiStateButtonField;
import nfc.sample.tictactoe.ui.TileChangedListener;

public class GameState {

    public static final int GAME_STATE_CHANGE_MY_TURN_NOW=1;
    public static final int GAME_STATE_CHANGE_THEIR_TURN_NOW=2;
    public static final int GAME_STATE_CHANGE_GAME_OVER_WON=3;
    public static final int GAME_STATE_CHANGE_GAME_OVER_STALE_MATE=4;
    public static final int GAME_STATE_CHANGE_NEW_GAME_STARTED=5;
    
    private static GameState _game_state;
    
    private GameStateChangeListener game_state_change_listener;
    private int current_game_state;

    private int current_tile = -1;
    private MultiStateButtonField[] _tiles;
    
    private int tile_count_this_turn=0;

    // indicates whether a tile is 0=unoccupied,1=nought,2=cross
    private int[] tile_states = new int[9];
    private boolean[] locked = new boolean[9];

    private boolean board_locked;

    private boolean game_over = false;
    private boolean bid_sent = false;
    
    private ProtocolMessageMasterBid my_bid_sent;
    private ProtocolMessageMasterBid other_bid_received;

    // tracks the last tile updated during a turn so if the player changes their mind we can undo this update
    private int last_turn;

    private TileChangedListener _listener;

    private GameState() {
        last_turn = Constants.NO_PREV_TURN;
    }

    public synchronized static GameState getInstance() {
        if(_game_state == null) {
            _game_state = new GameState();
        }
        return _game_state;
    }

    public void setGameStateChangeListener(GameStateChangeListener game_state_change_listener) {
        this.game_state_change_listener = game_state_change_listener;
    }
    
    public void initialiseGameState() {
        for(int i = 0; i < 9; i++) {
            tile_states[i] = Constants.TILE_STATE_BLANK;
            locked[i] = false;
            setUiState(i, Constants.TILE_STATE_BLANK, false);
        }
        current_tile = Constants.INITIAL_TILE;;
        last_turn = Constants.NO_PREV_TURN;
        game_over = false;
        current_game_state=GAME_STATE_CHANGE_NEW_GAME_STARTED;
        if (game_state_change_listener != null) {
            game_state_change_listener.onGameStateChange(GAME_STATE_CHANGE_NEW_GAME_STARTED);
        }
    }

    public void setTileUiObjects(MultiStateButtonField[] tiles) {
        _tiles = tiles;
    }

    public void setTileChangedListener(TileChangedListener listener) {
        _listener = listener;
    }

    public void updateTileThisPlayer(int tile, int players_symbol) {
        Utilities.log("XXXX updateTileThisPlayer(tile=" + tile + ",symbol=" + players_symbol + ",current tile state=" + tile_states[tile] + ",last_turn=" + last_turn);
        boolean tile_set = false;
        if(tile_states[tile] == Constants.TILE_STATE_BLANK) {
            Utilities.log("XXXX updateTileThisPlayer setting tile to players symbol:" + players_symbol);
            tile_states[tile] = players_symbol;
            setUiState(tile, players_symbol, true);
            tile_count_this_turn++;
            _listener.tileChanged(tile);
            tile_set = true;
            if (hasWon(players_symbol)) {
                current_game_state=GAME_STATE_CHANGE_GAME_OVER_WON;
                game_over = true;
                if (game_state_change_listener != null) {
                    game_state_change_listener.onGameStateChange(GAME_STATE_CHANGE_GAME_OVER_WON);
                }
            } else {
                if (isBoardFull()) {
                    current_game_state=GAME_STATE_CHANGE_GAME_OVER_STALE_MATE;
                    game_over = true;
                    if (game_state_change_listener != null) {
                        game_state_change_listener.onGameStateChange(GAME_STATE_CHANGE_GAME_OVER_STALE_MATE);
                    }
                }
            }
        } else {
            if(tile_states[tile] == players_symbol && !locked[tile]) {
                Utilities.log("XXXX updateTileThisPlayer setting tile to blank square");
                tile_states[tile] = Constants.TILE_STATE_BLANK;
                setUiState(tile, Constants.TILE_STATE_BLANK, true);
                tile_set = false;
            } else {
                Utilities.log("XXXX updateTileThisPlayer ignoring request as tile contains other player's symbol or is locked from previous turn");
                tile_set = false;
            }
        }
        if(tile_set && last_turn != Constants.NO_PREV_TURN && last_turn != tile && !locked[tile]) {
            Utilities.log("XXXX updateTileThisPlayer setting last turn's tile [" + last_turn + "] to BLANK");
            tile_states[last_turn] = Constants.TILE_STATE_BLANK;
            setUiState(last_turn, Constants.TILE_STATE_BLANK, false);
        }
        if(tile_set) {
            last_turn = tile;
        }
    }

    public void updateTileOtherPlayer(int tile, int players_symbol) {
        Utilities.log("XXXX updateTileOtherPlayer(tile=" + tile + ",symbol=" + players_symbol + ",current tile state=" + tile_states[tile]);
        // assume this was a valid move!
        Utilities.log("XXXX updateTileOtherPlayer setting tile to players symbol:" + players_symbol);
        tile_states[tile] = players_symbol;
        locked[tile] = true;
        setUiState(tile, players_symbol, true);
        last_turn = Constants.NO_PREV_TURN;
        current_game_state=GAME_STATE_CHANGE_MY_TURN_NOW;
        if (game_state_change_listener != null) {
            game_state_change_listener.onGameStateChange(GAME_STATE_CHANGE_MY_TURN_NOW);
        }
    }
    
    public void setGameState(int new_state) {
        current_game_state = new_state;
        if (game_state_change_listener != null) {
            game_state_change_listener.onGameStateChange(new_state);
        }
    }

    private void setUiState(int tile, int symbol, boolean give_focus) {
        _tiles[tile].setMsbState(symbol);
        synchronized(UiApplication.getEventLock()) {
            if(give_focus) {
                _tiles[tile].setFocus();
                _tiles[tile].setFocusedImage();
            } else {
                _tiles[tile].updateImage(false);
            }
        }
    }

    public int getTileState(int tile) {
        return tile_states[tile];
    }

    public void setStartOfTurn() {
        last_turn = Constants.NO_PREV_TURN;
    }

    public boolean isBoardLocked() {
        return board_locked;
    }

    public void setBoardLocked(boolean locked) {
        board_locked = locked;
    }

    public boolean isBoardFull() {
        for(int i = 0; i < 9; i++) {
            if(tile_states[i] == Constants.TILE_STATE_BLANK) {
                return false;
            }
        }
        return true;
    }

    public boolean hasWon(int symbol) {
        // horizontal lines
        if(tile_states[0] == symbol && tile_states[1] == symbol && tile_states[2] == symbol) {
            return true;
        }
        if(tile_states[3] == symbol && tile_states[4] == symbol && tile_states[5] == symbol) {
            return true;
        }
        if(tile_states[6] == symbol && tile_states[7] == symbol && tile_states[8] == symbol) {
            return true;
        }

        // vertical lines
        if(tile_states[0] == symbol && tile_states[3] == symbol && tile_states[6] == symbol) {
            return true;
        }
        if(tile_states[1] == symbol && tile_states[4] == symbol && tile_states[7] == symbol) {
            return true;
        }
        if(tile_states[2] == symbol && tile_states[5] == symbol && tile_states[8] == symbol) {
            return true;
        }

        // diagonal lines
        if(tile_states[0] == symbol && tile_states[4] == symbol && tile_states[8] == symbol) {
            return true;
        }

        if(tile_states[2] == symbol && tile_states[4] == symbol && tile_states[6] == symbol) {
            return true;
        }

        return false;
    }

    public int[] getWinningLine(int symbol) {

        int[] line = { -1, -1, -1 };

        // horizontal lines
        if(tile_states[0] == symbol && tile_states[1] == symbol && tile_states[2] == symbol) {
            line[0] = 0;
            line[1] = 1;
            line[2] = 2;
        }
        if(tile_states[3] == symbol && tile_states[4] == symbol && tile_states[5] == symbol) {
            line[0] = 3;
            line[1] = 4;
            line[2] = 5;
        }
        if(tile_states[6] == symbol && tile_states[7] == symbol && tile_states[8] == symbol) {
            line[0] = 6;
            line[1] = 7;
            line[2] = 8;
        }

        // vertical lines
        if(tile_states[0] == symbol && tile_states[3] == symbol && tile_states[6] == symbol) {
            line[0] = 0;
            line[1] = 3;
            line[2] = 6;
        }
        if(tile_states[1] == symbol && tile_states[4] == symbol && tile_states[7] == symbol) {
            line[0] = 1;
            line[1] = 4;
            line[2] = 7;
        }
        if(tile_states[2] == symbol && tile_states[5] == symbol && tile_states[8] == symbol) {
            line[0] = 2;
            line[1] = 5;
            line[2] = 8;
        }

        // diagonal lines
        if(tile_states[0] == symbol && tile_states[4] == symbol && tile_states[8] == symbol) {
            line[0] = 0;
            line[1] = 4;
            line[2] = 8;
        }

        if(tile_states[2] == symbol && tile_states[4] == symbol && tile_states[6] == symbol) {
            line[0] = 2;
            line[1] = 4;
            line[2] = 6;
        }

        return line;
    }

    public int getCurrent_tile() {
        return current_tile;
    }

    public void setCurrent_tile(int current_tile) {
        Utilities.log("XXXX GameState.setCurrent_tile:" + current_tile);
        this.current_tile = current_tile;
    }

    public void lockBoard() {
        setBoardLocked(true);
        for(int i = 0; i < 9; i++) {
            if(tile_states[i] != Constants.TILE_STATE_BLANK) {
                locked[i] = true;
            } else {
                locked[i] = false;
            }
        }
        last_turn = Constants.NO_PREV_TURN;
    }

    public boolean isGame_over() {
        return game_over;
    }

    public void setGame_over(boolean game_over) {
        this.game_over = game_over;
    }

    public int getTile_count_this_turn() {
        return tile_count_this_turn;
    }

    public void setTile_count_this_turn(int tile_count_this_turn) {
        this.tile_count_this_turn = tile_count_this_turn;
    }

    public boolean isBid_sent() {
        return bid_sent;
    }

    public void setBid_sent(boolean bid_sent) {
        this.bid_sent = bid_sent;
    }

    public ProtocolMessageMasterBid getMy_bid_sent() {
        return my_bid_sent;
    }

    public void setMy_bid_sent(ProtocolMessageMasterBid my_bid_sent) {
        this.my_bid_sent = my_bid_sent;
    }

    public ProtocolMessageMasterBid getOther_bid_received() {
        return other_bid_received;
    }

    public void setOther_bid_received(ProtocolMessageMasterBid other_bid_received) {
        this.other_bid_received = other_bid_received;
    }

}
