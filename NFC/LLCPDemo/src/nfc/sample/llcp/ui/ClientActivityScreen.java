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
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.llcp.Constants;

public final class ClientActivityScreen extends MainScreen {

    private ClientActivityScreen _screen;

    private HorizontalFieldManager icon_row = new HorizontalFieldManager();
    private Bitmap sc_icon = Bitmap.getBitmapResource("sc_icon.png");
    private Bitmap reader_icon = Bitmap.getBitmapResource("reader_icon.png");

    private BitmapField icon;

    private String response_text = "Send to other device";

    private SeparatorField separator1 = new SeparatorField(Field.USE_ALL_WIDTH);
    private SeparatorField separator2 = new SeparatorField(Field.USE_ALL_WIDTH);

    private RichTextField heading = new RichTextField();

    private Font heading_font;

    private AlternatingListField _log = new AlternatingListField(Color.WHITE,Color.LIGHTGRAY,Field.USE_ALL_WIDTH);

    private int _emulation_type;

    /**
     * Creates a new NfcVirtTargScreen object
     */
    public ClientActivityScreen(int emulation_type) {

        super(MainScreen.HORIZONTAL_SCROLL);
        this._screen = this;
        _emulation_type = emulation_type;

        setTitle("LLCPDemo V" + Constants.MYAPP_VERSION);

//        if(emulation_type == Constants.EMULATE_SC) {
//            emulateSmartCard();
//        } else {
//        }

        Font default_font = Font.getDefault();
        heading_font = default_font.derive(Font.BOLD, 24);

        heading.setText("Event Log (newest items first)");
        heading.setFont(heading_font);

        add(separator1);
        add(heading);
        add(separator2);
        add(_log);

    }

    private void emulateSmartCard() {
        icon = new BitmapField(sc_icon);
        icon_row.add(icon);
        icon_row.add(new LabelField(" - emulating smart card"));
        add(icon_row);
//        mi_response.setCommandContext(this);
//        mi_response.setCommand(new Command(new ResponseTextCommand()));
//        addMenuItem(mi_response);

    }

    public void logEvent(String event_message) {
        _log.insert(0, event_message);
        _log.setSelectedIndex(0);
    }

    public String getResponse_text() {
        return response_text;
    }

    public void setResponse_text(String response_text) {
        this.response_text = response_text;
    }


}
