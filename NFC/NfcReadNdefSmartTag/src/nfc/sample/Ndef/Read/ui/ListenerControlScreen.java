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
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.Ndef.Read.Constants;
import nfc.sample.Ndef.Read.NdefListenerManager;
import nfc.sample.Ndef.Read.Utilities;
import nfc.sample.Ndef.Read.buttons.MsbConfig;
import nfc.sample.Ndef.Read.buttons.MsbState;
import nfc.sample.Ndef.Read.buttons.MultiStateButtonField;
import nfc.sample.Ndef.Read.commands.RegisterListenerCommand;
import nfc.sample.Ndef.Read.commands.UnregisterListenerCommand;

public class ListenerControlScreen extends MainScreen {
    
    private static ListenerControlScreen _listener_control_screen;

    private NdefListenerManager _listener_mgr = NdefListenerManager.getInstance();

    private Bitmap reg_focused = Bitmap.getBitmapResource("reg_focus.png");
    private Bitmap reg_unfocused = Bitmap.getBitmapResource("reg_no_focus.png");
    private Bitmap reg_clicked = Bitmap.getBitmapResource("reg_clicked.png");

    private Bitmap unr_focused = Bitmap.getBitmapResource("unr_focus.png");
    private Bitmap unr_unfocused = Bitmap.getBitmapResource("unr_no_focus.png");
    private Bitmap unr_clicked = Bitmap.getBitmapResource("unr_clicked.png");

    private Bitmap reg_led = Bitmap.getBitmapResource("led_on.png");
    private Bitmap unr_led = Bitmap.getBitmapResource("led_off.png");

    private MultiStateButtonField msbf_sc;
    private MultiStateButtonField msbf_sr;
    private BitmapField led;
    
    public synchronized static ListenerControlScreen getInstance() {
        if (_listener_control_screen == null) {
            _listener_control_screen = new ListenerControlScreen();
        }
        return _listener_control_screen;
    }
    
    private ListenerControlScreen() {
        super(USE_ALL_HEIGHT | USE_ALL_WIDTH | FIELD_HCENTER | FIELD_VCENTER | NO_VERTICAL_SCROLL);
        setTitle("NFC Read NDEF SmartTag v" + Constants.MYAPP_VERSION);
        
        Utilities.log("XXXX constructing ListenerControlScreen");
        
        AbsoluteFieldManager icon_manager = new AbsoluteFieldManager();
        // button bitmaps are 120 x 120
        int hgap = (int) ((Display.getWidth() - 240) / 3);
        int vgap = (int) (Display.getHeight() - 120) / 2;
        int x1= hgap;
        int x2= x1 + 120 + hgap;
        int y = (int) (vgap * 0.75);
        
        // position the little LED in the top right hand corner
        int led_x = (Display.getWidth() - reg_led.getWidth() - 5);
        int led_y = 5;
       
        MsbConfig sc_btn_config = new MsbConfig();
        MsbState sc_btn_state = new MsbState(Constants.REG_BTN_STATE, "Register Listener", "Register Listener");
        sc_btn_state.setbmp_focused(reg_focused);
        sc_btn_state.setbmp_unfocused(reg_unfocused);
        sc_btn_state.setbmp_clicked(reg_clicked);
        sc_btn_state.setbmp_unclicked(reg_focused);
        sc_btn_config.addState(sc_btn_state);
        msbf_sc = new MultiStateButtonField(sc_btn_config, new RegisterListenerCommand(), 0, Field.FIELD_HCENTER);
        icon_manager.add(msbf_sc,x1,y);

        MsbConfig sr_btn_config = new MsbConfig();
        MsbState sr_btn_state = new MsbState(Constants.UNR_BTN_STATE, "Unregister Listener", "Unregister Listener");
        sr_btn_state.setbmp_focused(unr_focused);
        sr_btn_state.setbmp_unfocused(unr_unfocused);
        sr_btn_state.setbmp_clicked(unr_clicked);
        sr_btn_state.setbmp_unclicked(unr_focused);
        sr_btn_config.addState(sr_btn_state);
        msbf_sr = new MultiStateButtonField(sr_btn_config, new UnregisterListenerCommand(), 0, Field.FIELD_HCENTER);
        icon_manager.add(msbf_sr,x2,y);

        if (_listener_mgr.is_listening()) {
            led = new BitmapField(reg_led);
        } else {
            led = new BitmapField(unr_led);
        }
        icon_manager.add(led,led_x, led_y);
        
        add(icon_manager);

        Utilities.log("XXXX done constructing ListenerControlScreen");

    }

    protected void onExposed() {
        setLed();
    }

    public void setLed() {
        if (_listener_mgr.is_listening()) {
            led.setBitmap(reg_led);
        } else {
            led.setBitmap(unr_led);
        }
    }
}
