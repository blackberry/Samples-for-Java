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
package nfc.sample.llcp.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.llcp.Constants;
import nfc.sample.llcp.Utilities;

public final class ActivityScreen extends MainScreen {

    private ActivityScreen _screen;

    private HorizontalFieldManager icon_row = new HorizontalFieldManager();
    private Bitmap snd_icon = Bitmap.getBitmapResource("snd_icon.png");
    private Bitmap rcv_icon = Bitmap.getBitmapResource("rcv_icon.png");

    private BitmapField icon;

    private String text = "";

    private SeparatorField separator1 = new SeparatorField(Field.USE_ALL_WIDTH);
    private SeparatorField separator2 = new SeparatorField(Field.USE_ALL_WIDTH);

    private RichTextField heading = new RichTextField();

    private Font heading_font;

    private AlternatingListField _log = new AlternatingListField(Color.WHITE,Color.LIGHTGRAY,Field.USE_ALL_WIDTH);

    private int _emulation_type;

    public ActivityScreen(int role) {
        super(MainScreen.HORIZONTAL_SCROLL);
        Utilities.log("XXXX " + Thread.currentThread().getName() + " : " +"constructing ActivityScreen");
        this._screen = this;
        _emulation_type = role;

        setTitle("LLCPDemo V" + Constants.MYAPP_VERSION);

        if(role == Constants.SND) {
            indicateSender();
        } else {
            indicateReceiver();
        }

        Font default_font = Font.getDefault();
        heading_font = default_font.derive(Font.BOLD, 24);

        heading.setText("Event Log (newest items first)");
        heading.setFont(heading_font);

        add(separator1);
        add(heading);
        add(separator2);
        add(_log);

    }

    private void indicateSender() {
        icon = new BitmapField(snd_icon);
        icon_row.add(icon);
        icon_row.add(new LabelField(" - LLCP sender"));
        add(icon_row);
    }

    private void indicateReceiver() {
        icon = new BitmapField(rcv_icon);
        icon_row.add(icon);
        icon_row.add(new LabelField(" - LLCP receiver"));
        add(icon_row);
    }

    public void logEvent(String event_message) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " : " +event_message);
        synchronized (UiApplication.getEventLock()) {
            _log.insert(0, event_message);
            _log.setSelectedIndex(0);
        }
    }

    public String getResponse_text() {
        return text;
    }

    public void setResponse_text(String response_text) {
        this.text = response_text;
    }

}
