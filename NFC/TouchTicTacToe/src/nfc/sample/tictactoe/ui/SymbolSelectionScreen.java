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
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.commands.ChooseCrossCommand;
import nfc.sample.tictactoe.commands.ChooseNoughtCommand;
import nfc.sample.tictactoe.game.GameState;
import nfc.sample.tictactoe.tiles.MsbConfig;
import nfc.sample.tictactoe.tiles.MsbState;
import nfc.sample.tictactoe.tiles.MultiStateButtonField;

public class SymbolSelectionScreen extends MainScreen {

    MultiStateButtonField msbf_nought;
    MultiStateButtonField msbf_cross;
    private UiConfig uiconfig;
    private int focused_tile = 0;

    public SymbolSelectionScreen() {
        super(Field.USE_ALL_HEIGHT | Field.USE_ALL_WIDTH);
        
        GameState game_state = GameState.getInstance();
        game_state.setGame_over(false);
        
        uiconfig = UiConfigFactory.getUiConfig(Display.getWidth(), Display.getHeight());
        Bitmap nought_unfocused = BitmapFactory.getNoughtUnfocused();
        Bitmap nought_focused = BitmapFactory.addStateIndicator(nought_unfocused, Constants.FOCUSED_COLOUR);
        Bitmap nought_clicked = BitmapFactory.addStateIndicator(nought_unfocused, Constants.CLICKED_COLOUR);

        Bitmap cross_unfocused = BitmapFactory.getCrossUnfocused();
        Bitmap cross_focused = BitmapFactory.addStateIndicator(cross_unfocused, Constants.FOCUSED_COLOUR);
        Bitmap cross_clicked = BitmapFactory.addStateIndicator(cross_unfocused, Constants.CLICKED_COLOUR);
        
        MsbConfig nought_btn_config = new MsbConfig();
        MsbState btn_state_nought = new MsbState(Constants.SELECT_STATE_NOUGHT, "", "");
        btn_state_nought.setbmp_focused(nought_focused);
        btn_state_nought.setbmp_unfocused(nought_unfocused);
        btn_state_nought.setbmp_clicked(nought_clicked);
        btn_state_nought.setbmp_unclicked(nought_focused);
        nought_btn_config.addState(btn_state_nought);

        msbf_nought = new MultiStateButtonField(nought_btn_config, new ChooseNoughtCommand(this), 0, Field.FIELD_HCENTER);
        msbf_nought.setFocusListener(focus_listener);

        MsbConfig cross_btn_config = new MsbConfig();
        MsbState tile_btn_state_cross = new MsbState(Constants.SELECT_STATE_CROSS, "", "");
        tile_btn_state_cross.setbmp_focused(cross_focused);
        tile_btn_state_cross.setbmp_unfocused(cross_unfocused);
        tile_btn_state_cross.setbmp_clicked(cross_clicked);
        tile_btn_state_cross.setbmp_unclicked(cross_focused);
        cross_btn_config.addState(tile_btn_state_cross);

        msbf_cross = new MultiStateButtonField(cross_btn_config, new ChooseCrossCommand(this), 0, Field.FIELD_HCENTER);
        msbf_cross.setFocusListener(focus_listener);
        
        // we know icons are all the same dimensions and we can fit three across the screen
        int icon_width=nought_focused.getWidth();
        int icon_height=nought_focused.getHeight();
        // spacer width must be 1/3 the width of an icon for even spacing
        int width_spacer = icon_width / 3;
        
        int icon_x = width_spacer;
        AbsoluteFieldManager mgr = new AbsoluteFieldManager();
        mgr.add(msbf_nought,icon_x,icon_height);
        icon_x = icon_x + icon_width + width_spacer;
        mgr.add(msbf_cross,icon_x,icon_height);
        add(mgr);
    }

    private FocusChangeListener focus_listener = new FocusChangeListener() {

        public void focusChanged(Field field, int eventType) {
            if(field == msbf_nought) {
                focused_tile = 0;
            } else {
                focused_tile = 1;
            }
        }
    };
    
    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        
        Utilities.log("XXXX navigationMovement: dx=" + dx + ",dy=" + dy);

        int x_dir = 0;

        if(dx > 0) {
            x_dir = 1;
        }
        if(dx < 0) {
            x_dir = -1;
        }

        setFocusedTile(x_dir);
        return true;
    }

    private void setFocusedTile(int x_dir) {
        Utilities.log("XXX setFocusedTile x_dir=" + x_dir);
        if (x_dir == 1) {
            if (focused_tile == 0) {
                focused_tile = 1;
            }
        } else {
            if (x_dir == -1) {
                if (focused_tile == 1) {
                    focused_tile = 0;
                }
            }
        }
        Utilities.log("XXX setFocusedTile focused_tile=" + focused_tile);
        if (focused_tile == 0) {
            msbf_nought.setFocus();
        } else {
            msbf_cross.setFocus();
        }
    }


}
