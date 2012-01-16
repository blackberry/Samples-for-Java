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
package nfc.sample.virtual.target.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.virtual.target.Constants;
import nfc.sample.virtual.target.buttons.MsbConfig;
import nfc.sample.virtual.target.buttons.MsbState;
import nfc.sample.virtual.target.buttons.MultiStateButtonField;
import nfc.sample.virtual.target.commands.EmulateScCommand;
import nfc.sample.virtual.target.commands.EmulateSrCommand;

public class RoleSelectionScreen extends MainScreen {
    
    private Bitmap sc_focused = Bitmap.getBitmapResource("emulate_sc_focus.png");
    private Bitmap sc_unfocused = Bitmap.getBitmapResource("emulate_sc_no_focus.png");
    private Bitmap sc_clicked = Bitmap.getBitmapResource("emulate_sc_clicked.png");

    private Bitmap sr_focused = Bitmap.getBitmapResource("emulate_reader_focus.png");
    private Bitmap sr_unfocused = Bitmap.getBitmapResource("emulate_reader_no_focus.png");
    private Bitmap sr_clicked = Bitmap.getBitmapResource("emulate_reader_clicked.png");
    
    private MultiStateButtonField msbf_sc;
    private MultiStateButtonField msbf_sr;
    
    private LabelField ui_msg=new LabelField("",Field.FIELD_HCENTER);
    
    private int focused_button = 0;

    private FocusChangeListener focus_listener = new FocusChangeListener() {

        public void focusChanged(Field field, int eventType) {
            if (field == msbf_sc) {
                focused_button = 0;
                setMessage("BlackBerry will act like a smart card");
            }
            if (field == msbf_sr) {
                focused_button = 1;
                setMessage("BlackBerry will act like a smart card reader");
            }
        }

    };

    public RoleSelectionScreen() {
        super(USE_ALL_HEIGHT | USE_ALL_WIDTH | FIELD_HCENTER | FIELD_VCENTER | NO_VERTICAL_SCROLL);
        setTitle("NfcVirtualTargetFun V" + Constants.MYAPP_VERSION);

        AbsoluteFieldManager btn_row = new AbsoluteFieldManager();
        // bitmaps are 120 x 120
        int hgap = (int) ((Display.getWidth() - 240) / 3);
        int vgap = (int) (Display.getHeight() - 120) / 2;
        int x1= hgap;
        int x2= x1 + 120 + hgap;
        int y = (int) (vgap * 0.75);
        
        MsbConfig sc_btn_config = new MsbConfig();
        MsbState sc_btn_state = new MsbState(Constants.SC_BTN_STATE, "Emulate Smart Card", "Emulate Smart Card");
        sc_btn_state.setbmp_focused(sc_focused);
        sc_btn_state.setbmp_unfocused(sc_unfocused);
        sc_btn_state.setbmp_clicked(sc_clicked);
        sc_btn_state.setbmp_unclicked(sc_focused);
        sc_btn_config.addState(sc_btn_state);
        msbf_sc = new MultiStateButtonField(sc_btn_config, new EmulateScCommand(), 0, Field.FIELD_HCENTER);
        btn_row.add(msbf_sc,x1,y);

        MsbConfig sr_btn_config = new MsbConfig();
        MsbState sr_btn_state = new MsbState(Constants.SR_BTN_STATE, "Emulate Reader", "Emulate Reader");
        sr_btn_state.setbmp_focused(sr_focused);
        sr_btn_state.setbmp_unfocused(sr_unfocused);
        sr_btn_state.setbmp_clicked(sr_clicked);
        sr_btn_state.setbmp_unclicked(sr_focused);
        sr_btn_config.addState(sr_btn_state);
        msbf_sr = new MultiStateButtonField(sr_btn_config, new EmulateSrCommand(), 0, Field.FIELD_HCENTER);
        btn_row.add(msbf_sr,x2,y);

        msbf_sc.setFocusListener(focus_listener);
        msbf_sr.setFocusListener(focus_listener);

        add(btn_row);
        add(ui_msg);
    }

    private void setMessage(String message) {
        synchronized (UiApplication.getEventLock()) {
            ui_msg.setText(message);
        }
        
    }

}
