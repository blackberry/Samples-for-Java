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

public class UiConfig640x480 extends UiConfig {

    private int [] tile_x = {1, 215, 428, 1,   215, 428, 1,   215, 428};
    private int [] tile_y = {1, 1,   1,   162, 162, 162, 322, 322, 322};
    private int statusX = 0;
    private int statusY = 440;

    public int getScreenHeight() {
        return 360;
    }

    public int getScreenWidth() {
        return 480;
    }

    public String getBackgroundName() {
        return "board_640x480.png";
    }

    public String getTileNameBlankFocus() {
        return "blank_focus_213x160.png";
    }

    public String getTileNameBlankUnfocus() {
        return "blank_unfocus_213x160.png";
    }

    public String getTileNameBlankClicked() {
        return "blank_clicked_213x160.png";
    }

    public String getTileNameNoughtFocus() {
        return "nought_focus_213x160.png";
    }

    public String getTileNameNoughtUnfocus() {
        return "nought_unfocus_213x160.png";
    }

    public String getTileNameNoughtClicked() {
        return "nought_clicked_213x160.png";
    }

    public String getTileNameCrossFocus() {
        return "cross_focus_213x160.png";
    }

    public String getTileNameCrossUnfocus() {
        return "cross_unfocus_213x160.png";
    }

    public String getTileNameCrossClicked() {
        return "cross_clicked_213x160.png";
    }

    public String getNewGameScreenName() {
        return "new_game_640x480.png";
    }

    public int getTileX(int tile_no) {
        return tile_x[tile_no];
    }

    public int getTileY(int tile_no) {
        return tile_y[tile_no];
    }

    public int getStatusX() {
        return statusX;
    }

    public int getStatusY() {
        return statusY;
    }

}
