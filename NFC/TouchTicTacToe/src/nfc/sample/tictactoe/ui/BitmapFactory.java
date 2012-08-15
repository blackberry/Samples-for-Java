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
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import nfc.sample.tictactoe.Constants;

public class BitmapFactory {
    
    public static Bitmap getBackground(int width, int height) {
        Bitmap bg = new Bitmap(width, height);
        Graphics pen = Graphics.create(bg);
        int x1 = width / 3;
        int x2 = x1 * 2;
        int y1 = height / 3;
        int y2 = y1 * 2;
        pen.setColor(Color.BLACK);
        pen.drawLine(x1, 0, x1, height);
        pen.drawLine(x2, 0, x2, height);
        pen.drawLine(0, y1, width, y1);
        pen.drawLine(0, y2, width, y2);
        return bg;
    }
    
    public static Bitmap getBlankUnfocused() {
        int width  = Display.getWidth();
        int height = Display.getHeight();
        UiConfig uiconfig = UiConfigFactory.getUiConfig(width,height);
        Bitmap blank_unfocused = Bitmap.getBitmapResource(uiconfig.getTileNameBlankUnfocus());
        return blank_unfocused;
    }

    public static Bitmap getNoughtUnfocused() {
        int width  = Display.getWidth();
        int height = Display.getHeight();
        UiConfig uiconfig = UiConfigFactory.getUiConfig(width,height);
        Bitmap nought_unfocused = Bitmap.getBitmapResource(uiconfig.getTileNameNoughtUnfocus());
        return nought_unfocused;
    }

    public static Bitmap getCrossUnfocused() {
        int width  = Display.getWidth();
        int height = Display.getHeight();
        UiConfig uiconfig = UiConfigFactory.getUiConfig(width,height);
        Bitmap cross_unfocused = Bitmap.getBitmapResource(uiconfig.getTileNameCrossUnfocus());
        return cross_unfocused;
    }

    public static Bitmap getNewGameScreen() {
        int width  = Display.getWidth();
        int height = Display.getHeight();
        UiConfig uiconfig = UiConfigFactory.getUiConfig(width,height);
        Bitmap new_game = Bitmap.getBitmapResource(uiconfig.getNewGameScreenName());
        return new_game;
    }
    
    public static Bitmap addStateIndicator(Bitmap unfocused_bmp, int color) {
        int width=unfocused_bmp.getWidth();
        int height=unfocused_bmp.getHeight();
        int [] argb = new int[width * height];
        unfocused_bmp.getARGB(argb, 0, width, 0, 0, width, height);
        Bitmap new_bmp = new Bitmap(width, height);
        new_bmp.setARGB(argb, 0, width, 0, 0, width, height);
        Graphics pen = Graphics.create(new_bmp);
        pen.setGlobalAlpha(Constants.TRANSPARENCY);
        pen.setColor(color);
        pen.fillRect(0, 0, width, height);
        return new_bmp;
    }

}
