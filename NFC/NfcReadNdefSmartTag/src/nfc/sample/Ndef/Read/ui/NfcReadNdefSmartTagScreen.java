package nfc.sample.Ndef.Read.ui;
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
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.Ndef.Read.Constants;
import nfc.sample.Ndef.Read.NdefListenerManager;
import nfc.sample.Ndef.Read.Utilities;

/**
 * 
 * This is the main screen of the NFC NDEF reader application It has two buttons to register and unregister for NDEF messages as
 * well as a field where information can be presented to the user.
 * 
 */
public class NfcReadNdefSmartTagScreen extends MainScreen {

    private static NfcReadNdefSmartTagScreen _screen;
    private NdefListenerManager _listener_mgr = NdefListenerManager.getInstance();

    private RuntimeStore rts = RuntimeStore.getRuntimeStore();
    private static final Object OPEN_TOKEN = new Object();

    private AlternatingListField _log = new AlternatingListField(Color.WHITE, Color.LIGHTGRAY, Field.USE_ALL_WIDTH);
    
    private LeftRightTwoCellManager top_row = new LeftRightTwoCellManager(0,5,5,Field.USE_ALL_WIDTH);
    
    private LabelField heading = new LabelField();
    private Font heading_font;
    
    private Bitmap reg_led = Bitmap.getBitmapResource("led_on.png");
    private Bitmap unr_led = Bitmap.getBitmapResource("led_off.png");

    private BitmapField led;
    
    private SeparatorField separator1 = new SeparatorField(Field.USE_ALL_WIDTH);

    public synchronized static NfcReadNdefSmartTagScreen getInstance() {
        if(_screen == null) {
            _screen = new NfcReadNdefSmartTagScreen();
        }
        return _screen;
    }

    /*
     * Set up the screen with the buttons and data fields and make sure we can scroll
     */
    private NfcReadNdefSmartTagScreen() {
        super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR | MainScreen.HORIZONTAL_SCROLL);
        setTitle("NFC Read NDEF SmartTag v" + Constants.MYAPP_VERSION);
        this._screen = this;
        Utilities.log("XXXX NfcReadNdefSmartTag entered NfcReadNdefSmartTagScreen constructor");
        
        Font default_font = Font.getDefault();
        heading_font = default_font.derive(Font.BOLD, 24);

        heading.setText("Event Log (newest items first)");
        heading.setFont(heading_font);

        if (_listener_mgr.is_listening()) {
            led = new BitmapField(reg_led,Field.FIELD_RIGHT);
        } else {
            led = new BitmapField(unr_led,Field.FIELD_RIGHT);
        }

        top_row.add(heading);
        top_row.add(led);
        
        add(top_row);
        add(separator1);
        add(_log);

        Utilities.log("XXXX NfcReadNdefSmartTag leaving NfcReadNdefSmartTagScreen constructor");
    }

    public void logEvent(String event_message) {
        synchronized(UiApplication.getEventLock()) {
            _log.insert(0, event_message);
            _log.setSelectedIndex(0);
        }
    }

    public void setLed() {
        if (_listener_mgr.is_listening()) {
            led.setBitmap(reg_led);
        } else {
            led.setBitmap(unr_led);
        }
    }

    public boolean onSavePrompt() {
        return true;
    }
    
}
