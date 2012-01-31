package nfc.sample.Ndef.Write.ui;
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
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.Ndef.Write.Constants;
import nfc.sample.Ndef.Write.buttons.MsbConfig;
import nfc.sample.Ndef.Write.buttons.MsbState;
import nfc.sample.Ndef.Write.buttons.MultiStateButtonField;
import nfc.sample.Ndef.Write.commands.CreateCustomTagCommand;
import nfc.sample.Ndef.Write.commands.CreateSpTagCommand;
import nfc.sample.Ndef.Write.commands.CreateTextTagCommand;
import nfc.sample.Ndef.Write.commands.CreateUriTagCommand;

public class TypeSelectionScreen extends MainScreen {
    
    private Bitmap uri_focused = Bitmap.getBitmapResource("uri_type_focus.png");
    private Bitmap uri_unfocused = Bitmap.getBitmapResource("uri_type_no_focus.png");
    private Bitmap uri_clicked = Bitmap.getBitmapResource("uri_type_clicked.png");

    private Bitmap sp_focused = Bitmap.getBitmapResource("sp_type_focus.png");
    private Bitmap sp_unfocused = Bitmap.getBitmapResource("sp_type_no_focus.png");
    private Bitmap sp_clicked = Bitmap.getBitmapResource("sp_type_clicked.png");

    private Bitmap text_focused = Bitmap.getBitmapResource("text_type_focus.png");
    private Bitmap text_unfocused = Bitmap.getBitmapResource("text_type_no_focus.png");
    private Bitmap text_clicked = Bitmap.getBitmapResource("text_type_clicked.png");

    private Bitmap custom_focused = Bitmap.getBitmapResource("custom_type_focus.png");
    private Bitmap custom_unfocused = Bitmap.getBitmapResource("custom_type_no_focus.png");
    private Bitmap custom_clicked = Bitmap.getBitmapResource("custom_type_clicked.png");

    private MultiStateButtonField msbf_uri;
    private MultiStateButtonField msbf_sp;
    private MultiStateButtonField msbf_text;
    private MultiStateButtonField msbf_custom;
    
    private LabelField ui_msg=new LabelField("Ready",Field.FIELD_HCENTER);
    
    private int focused_button = 0;

    private FocusChangeListener focus_listener = new FocusChangeListener() {

        public void focusChanged(Field field, int eventType) {
            if (field == msbf_uri) {
                focused_button = 0;
                setMessage("Create URI type tag");
            }
            if (field == msbf_sp) {
                focused_button = 1;
                setMessage("Create Smart Poster type tag");
            }
            if (field == msbf_text) {
                focused_button = 2;
                setMessage("Create Text type tag");
            }
            if (field == msbf_custom) {
                focused_button = 3;
                setMessage("Create custom type tag");
            }
        }

    };

    public TypeSelectionScreen() {
        super(USE_ALL_HEIGHT | USE_ALL_WIDTH | FIELD_HCENTER | FIELD_VCENTER | NO_VERTICAL_SCROLL);
        setTitle("NfcWriteNdefSmartTag V" + Constants.MYAPP_VERSION);

        AbsoluteFieldManager mgr = new AbsoluteFieldManager();
        // bitmaps are 120 x 120
        int hgap = (int) ((Display.getWidth() - 240) / 3);
        int vgap = (int) (Display.getHeight() - 240) / 3;
        int x1= hgap;
        int x2= x1 + 120 + hgap;
        int y1 = vgap;
        int y2 = y1 + 120 + vgap;

        MsbConfig uri_btn_config = new MsbConfig();
        MsbState uri_btn_state = new MsbState(Constants.BTN_STATE, "Create URI type tag", "Create URI type tag");
        uri_btn_state.setbmp_focused(uri_focused);
        uri_btn_state.setbmp_unfocused(uri_unfocused);
        uri_btn_state.setbmp_clicked(uri_clicked);
        uri_btn_state.setbmp_unclicked(uri_focused);
        uri_btn_config.addState(uri_btn_state);
        msbf_uri = new MultiStateButtonField(uri_btn_config, new CreateUriTagCommand(), 0, Field.FIELD_HCENTER);
        mgr.add(msbf_uri,x1,y1);
        msbf_uri.setFocusListener(focus_listener);

        MsbConfig sp_btn_config = new MsbConfig();
        MsbState sp_btn_state = new MsbState(Constants.BTN_STATE, "Create Smart Poster type tag", "Create Smart Poster type tag");
        sp_btn_state.setbmp_focused(sp_focused);
        sp_btn_state.setbmp_unfocused(sp_unfocused);
        sp_btn_state.setbmp_clicked(sp_clicked);
        sp_btn_state.setbmp_unclicked(sp_focused);
        sp_btn_config.addState(sp_btn_state);
        msbf_sp = new MultiStateButtonField(sp_btn_config, new CreateSpTagCommand(), 0, Field.FIELD_HCENTER);
        mgr.add(msbf_sp,x2,y1);
        msbf_sp.setFocusListener(focus_listener);

        MsbConfig text_btn_config = new MsbConfig();
        MsbState text_btn_state = new MsbState(Constants.BTN_STATE, "Create Text type tag", "Create Text type tag");
        text_btn_state.setbmp_focused(text_focused);
        text_btn_state.setbmp_unfocused(text_unfocused);
        text_btn_state.setbmp_clicked(text_clicked);
        text_btn_state.setbmp_unclicked(text_focused);
        text_btn_config.addState(text_btn_state);
        msbf_text = new MultiStateButtonField(text_btn_config, new CreateTextTagCommand(), 0, Field.FIELD_HCENTER);
        mgr.add(msbf_text,x1,y2);
        msbf_text.setFocusListener(focus_listener);

        MsbConfig custom_btn_config = new MsbConfig();
        MsbState custom_btn_state = new MsbState(Constants.BTN_STATE, "Create custom type tag", "Create custom type tag");
        custom_btn_state.setbmp_focused(custom_focused);
        custom_btn_state.setbmp_unfocused(custom_unfocused);
        custom_btn_state.setbmp_clicked(custom_clicked);
        custom_btn_state.setbmp_unclicked(custom_focused);
        custom_btn_config.addState(custom_btn_state);
        msbf_custom = new MultiStateButtonField(custom_btn_config, new CreateCustomTagCommand(), 0, Field.FIELD_HCENTER);
        mgr.add(msbf_custom,x2,y2);
        msbf_custom.setFocusListener(focus_listener);

//        mgr.add(ui_msg,x1,10);

        add(mgr);
    }

    private void setMessage(String message) {
        synchronized (UiApplication.getEventLock()) {
            ui_msg.setText(message);
        }
        
    }
    
    protected boolean navigationMovement(int dx, int dy, int status, int time) {

        int x_dir = 0;
        int y_dir = 0;

        if (dx > 0) {
            x_dir = 1;
        }
        if (dx < 0) {
            x_dir = -1;
        }

        if (dy > 0) {
            y_dir = 1;
        }
        if (dy < 0) {
            y_dir = -1;
        }
        setFocusedButton(x_dir, y_dir);
        return true;
    }

    // uri  : sp
    // text : custom
    private void setFocusedButton(int x_dir, int y_dir) {
        switch (focused_button) {
        case 0:
            if (x_dir == 1 && y_dir == 0) {
                focused_button = 1;
                msbf_sp.setFocus();
                return;
            }
            if (x_dir == 0 && y_dir == 1) {
                focused_button = 2;
                msbf_text.setFocus();
                return;
            }
            if (x_dir == 1 && y_dir == 1) {
                focused_button = 3;
                msbf_custom.setFocus();
                return;
            }
            break;
        case 1:
            if (x_dir == -1 && y_dir == 0) {
                focused_button = 0;
                msbf_uri.setFocus();
                return;
            }
            if (x_dir == 0 && y_dir == 1) {
                focused_button = 3;
                msbf_custom.setFocus();
                return;
            }
            if (x_dir == -1 && y_dir == 1) {
                focused_button = 2;
                msbf_uri.setFocus();
                return;
            }
            break;
        case 2:
            if (x_dir == 1 && y_dir == 0) {
                focused_button = 3;
                msbf_custom.setFocus();
                return;
            }
            if (x_dir == 0 && y_dir == -1) {
                focused_button = 0;
                msbf_uri.setFocus();
                return;
            }
            if (x_dir == 1 && y_dir == -1) {
                focused_button = 1;
                msbf_sp.setFocus();
                return;
            }
            break;
        case 3:
            if (x_dir == -1 && y_dir == 0) {
                focused_button = 2;
                msbf_text.setFocus();
                return;
            }
            if (x_dir == 0 && y_dir == -1) {
                focused_button = 1;
                msbf_sp.setFocus();
                return;
            }
            if (x_dir == -1 && y_dir == -1) {
                focused_button = 0;
                msbf_uri.setFocus();
                return;
            }
            break;
        }
    }


}
