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
package nfc.sample.tictactoe;
import net.rim.device.api.ui.Color;

public interface Constants {

    public static final String MYAPP_VERSION = "1.0.3";
    public static final String MYAPP_NAME = "Touch Tic Tac Toe";
    
    public static final String [] WINNER_MESSAGE = {"YOU HAVE WON!!","Touch devices to start new game"};
    public static final String [] STALE_MATE_MESSAGE = {"Stale mate! No more moves.","Touch devices to start new game"};
    
    public static final int BOARD_STATE_OPEN=0;
    public static final int BOARD_STATE_LOCKED=1;
    public static final int NO_PREV_TURN=-1;
    public static final int TILE_STATE_BLANK=0;
    public static final int TILE_STATE_NOUGHT=1;
    public static final int TILE_STATE_CROSS=2;
    public static final int SELECT_STATE_NOUGHT=0;
    public static final int SELECT_STATE_CROSS=0;
    public static final int STATUS_COLOUR = Color.RED;
    public static final int FOCUSED_COLOUR = Color.GOLD;
    public static final int CLICKED_COLOUR = Color.RED;
    public static final int WIN_LINE_COLOUR = Color.DARKORANGE;
    public static final int STALE_MATE_TILE_COLOUR = Color.BLUE;
    public static final int TRANSPARENCY = 30;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int PLAYER_SYMBOL_NOUGHT = 1;
    public static final int PLAYER_SYMBOL_CROSS = 2;
    public static final long MESSAGE_TIME = 5000;
    public static final int INITIAL_TILE = 4;
    
    // protocol
    public static final int PROTOCOL_MASTER_BID=1;
    public static final int PROTOCOL_TURN_OVER=2;
    public static final int PROTOCOL_RESET_BID_STATE=3;
    public static final String SNEP_DOMAIN = "com.blackberry.nfc.sample";
    public static final String SNEP_TYPE = "G1"; // Game #1

}