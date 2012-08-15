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

package nfc.sample.tictactoe.tiles;
import net.rim.device.api.command.AlwaysExecutableCommand;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.game.GameState;

public class MultiStateButtonField extends Field {

    private GameState game_state;
    private MsbConfig config;
    private int current_state;
    private Bitmap current_bitmap;
    private AlwaysExecutableCommand command;

    public MultiStateButtonField(MsbConfig config, AlwaysExecutableCommand command, int initial_state, long style) {
        super(style);
        this.config = config;
        this.command = command;
        this.current_state = initial_state;
        current_bitmap = config.getState(initial_state).getbmp_unfocused();
        game_state = GameState.getInstance();
    }

    protected void paint(Graphics graphics) {
        if(current_bitmap != null) {
            graphics.drawBitmap(0, 0, current_bitmap.getWidth(), current_bitmap.getHeight(), current_bitmap, 0, 0);
        } else {
            Utilities.log("XXXX " + config.getState(current_state).getState_label() + "-ERROR:current_bitmap=null");
        }
    }

    protected void drawFocus(Graphics graphics, boolean on) {
    }

    public void setMsbState(int state_id) {
        current_state = state_id;
        current_bitmap = config.getState(state_id).getbmp_unfocused();
    }

    public int getMsbState() {
        return current_state;
    }

    protected boolean navigationClick(int status, int time) {
        if(!game_state.isGame_over()) {
            setFocus();
            if(config.getState(current_state).getbmp_clicked() != null) {
                current_bitmap = config.getState(current_state).getbmp_clicked();
                invalidate();
            }
        }
        return true;
    }

    protected boolean navigationUnclick(int status, int time) {
        if(!game_state.isGame_over()) {
            if(config.getState(current_state).getbmp_unclicked() != null) {
                current_bitmap = config.getState(current_state).getbmp_unclicked();
                invalidate();
            }
            // pass button so we can check its state
            command.execute(null, this);
        }
        return true;
    }

    protected boolean touchEvent(TouchEvent message) {
        if(!game_state.isGame_over()) {
            if(message.getY(1) < 0) {
                return false;
            }
            if(message.getY(1) > getHeight()) {
                return false;
            }
            if(message.getX(1) < 0) {
                return false;
            }
            if(message.getX(1) > getWidth()) {
                return false;
            }

            setFocus();
            if(message.getEvent() == TouchEvent.DOWN && config.getState(current_state).getbmp_clicked() != null) {
                current_bitmap = config.getState(current_state).getbmp_clicked();
                invalidate();
                return true;
            }
            if(message.getEvent() == TouchEvent.UP && config.getState(current_state).getbmp_focused() != null) {
                current_bitmap = config.getState(current_state).getbmp_unclicked();
                invalidate();
                // command.execute(null, null);
                return true;
            }
        }
        return false;
    }

    protected void onFocus(int direction) {
        if(!game_state.isGame_over()) {
            if(config.getState(current_state).getbmp_focused() != null) {
                current_bitmap = config.getState(current_state).getbmp_focused();
                invalidate();
            }
        }
    }

    protected void onUnfocus() {
        if(!game_state.isGame_over()) {
            if(config.getState(current_state).getbmp_unfocused() != null) {
                current_bitmap = config.getState(current_state).getbmp_unfocused();
                invalidate();
            }
        }
    }

    public void setFocusedImage() {
        current_bitmap = config.getState(current_state).getbmp_focused();
        invalidate();
    }

    public void setImage(Bitmap image) {
        current_bitmap = image;
        invalidate();
    }

    public void updateImage(boolean focused) {
        if(focused) {
            current_bitmap = config.getState(current_state).getbmp_focused();
        } else {
            current_bitmap = config.getState(current_state).getbmp_unfocused();
        }
        invalidate();
    }

    public boolean isFocusable() {
        return true;
    }

    protected void layout(int width, int height) {
        setExtent(current_bitmap.getWidth(), current_bitmap.getHeight());
    }

    public int getPreferredWidth() {
        return current_bitmap.getWidth();
    }

    public int getPreferredHeight() {
        return current_bitmap.getHeight();
    }
}
