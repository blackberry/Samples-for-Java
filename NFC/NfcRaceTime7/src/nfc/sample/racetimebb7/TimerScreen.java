package nfc.sample.racetimebb7;
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
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;

public final class TimerScreen extends MainScreen {

    public static TimerScreen screen;

    Bitmap[] digits = { Bitmap.getBitmapResource("60px-Seven-segment_0.png"), Bitmap.getBitmapResource("60px-Seven-segment_1.png"), Bitmap.getBitmapResource("60px-Seven-segment_2.png"),
            Bitmap.getBitmapResource("60px-Seven-segment_3.png"), Bitmap.getBitmapResource("60px-Seven-segment_4.png"), Bitmap.getBitmapResource("60px-Seven-segment_5.png"),
            Bitmap.getBitmapResource("60px-Seven-segment_6.png"), Bitmap.getBitmapResource("60px-Seven-segment_7.png"), Bitmap.getBitmapResource("60px-Seven-segment_8.png"),
            Bitmap.getBitmapResource("60px-Seven-segment_9.png") };

    Bitmap colon = Bitmap.getBitmapResource("60px-Seven-segment_colon.png");

    BitmapField hour1;
    BitmapField hour2;
    BitmapField colon1;
    BitmapField minute1;
    BitmapField minute2;
    BitmapField colon2;
    BitmapField second1;
    BitmapField second2;

    public synchronized static TimerScreen getTimerScreen() {
        if(screen == null) {
            screen = new TimerScreen();
        }
        return screen;
    }

    private TimerScreen() {
        super(Field.USE_ALL_HEIGHT | Field.USE_ALL_WIDTH | Screen.NO_VERTICAL_SCROLL);
        ColouredBackground bg = new ColouredBackground(Color.WHITE, Field.USE_ALL_HEIGHT | Field.USE_ALL_WIDTH | Screen.NO_VERTICAL_SCROLL);
        AbsoluteFieldManager ab_mgr = new AbsoluteFieldManager();
        hour1 = new BitmapField(digits[0]);
        hour2 = new BitmapField(digits[0]);
        minute1 = new BitmapField(digits[0]);
        minute2 = new BitmapField(digits[0]);
        second1 = new BitmapField(digits[0]);
        second2 = new BitmapField(digits[0]);
        colon1 = new BitmapField(colon);
        colon2 = new BitmapField(colon);
        bg.add(ab_mgr);

        int y = (Display.getHeight() - 112) / 2; // images are 112 pixels high
        int x = (Display.getWidth() - (8 * 60)) / 2; // and 60 pixels wide

        ab_mgr.add(hour1, x, y);
        x = x + 60;
        ab_mgr.add(hour2, x, y);
        x = x + 60;
        ab_mgr.add(colon1, x, y);
        x = x + 60;
        ab_mgr.add(minute1, x, y);
        x = x + 60;
        ab_mgr.add(minute2, x, y);
        x = x + 60;
        ab_mgr.add(colon2, x, y);
        x = x + 60;
        ab_mgr.add(second1, x, y);
        x = x + 60;
        ab_mgr.add(second2, x, y);
        add(bg);
    }

    public void updateTime(Time time) {
        synchronized(UiApplication.getEventLock()) {
            hour1.setBitmap(digits[time.getHour1()]);
            hour2.setBitmap(digits[time.getHour2()]);
            minute1.setBitmap(digits[time.getMinute1()]);
            minute2.setBitmap(digits[time.getMinute2()]);
            second1.setBitmap(digits[time.getSecond1()]);
            second2.setBitmap(digits[time.getSecond2()]);
            invalidate();
        }
    }
}
