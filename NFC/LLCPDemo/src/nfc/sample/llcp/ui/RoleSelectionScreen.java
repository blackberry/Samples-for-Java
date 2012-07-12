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
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.llcp.Constants;
import nfc.sample.llcp.buttons.MsbConfig;
import nfc.sample.llcp.buttons.MsbState;
import nfc.sample.llcp.buttons.MultiStateButtonField;
import nfc.sample.llcp.commands.ReceiveCommand;
import nfc.sample.llcp.commands.SendCommand;

public class RoleSelectionScreen extends MainScreen {
    
    private Bitmap rcv_focused = Bitmap.getBitmapResource("receiver_focus.png");
    private Bitmap rcv_unfocused = Bitmap.getBitmapResource("receiver_no_focus.png");
    private Bitmap rcv_clicked = Bitmap.getBitmapResource("receiver_clicked.png");

    private Bitmap snd_focused = Bitmap.getBitmapResource("sender_focus.png");
    private Bitmap snd_unfocused = Bitmap.getBitmapResource("sender_no_focus.png");
    private Bitmap snd_clicked = Bitmap.getBitmapResource("sender_clicked.png");
    
    private MultiStateButtonField msbf_rcv;
    private MultiStateButtonField msbf_snd;
    
    private LabelField ui_msg=new LabelField("",Field.FIELD_HCENTER);
    
    private int focused_button = 0;

    private FocusChangeListener focus_listener = new FocusChangeListener() {

        public void focusChanged(Field field, int eventType) {
            if (field == msbf_rcv) {
                focused_button = 0;
            }
            if (field == msbf_snd) {
                focused_button = 1;
            }
        }
    };

    public RoleSelectionScreen() {
        super(USE_ALL_HEIGHT | USE_ALL_WIDTH | FIELD_HCENTER | FIELD_VCENTER | NO_VERTICAL_SCROLL);
        setTitle("LLCPDemo V" + Constants.MYAPP_VERSION);

        AbsoluteFieldManager btn_row = new AbsoluteFieldManager();
        // bitmaps are 120 x 120
        int hgap = (int) ((Display.getWidth() - 240) / 3);
        int vgap = (int) (Display.getHeight() - 120) / 2;
        int x1= hgap;
        int x2= x1 + 120 + hgap;
        int y = (int) (vgap * 0.75);
        
        MsbConfig snd_btn_config = new MsbConfig();
        MsbState snd_btn_state = new MsbState(Constants.SND_BTN_STATE, "Send data over LLCP", "Send data over LLCP");
        snd_btn_state.setbmp_focused(snd_focused);
        snd_btn_state.setbmp_unfocused(snd_unfocused);
        snd_btn_state.setbmp_clicked(snd_clicked);
        snd_btn_state.setbmp_unclicked(snd_focused);
        snd_btn_config.addState(snd_btn_state);
        msbf_snd = new MultiStateButtonField(snd_btn_config, new SendCommand(), 0, Field.FIELD_HCENTER);
        btn_row.add(msbf_snd,x1,y);

        MsbConfig rcv_btn_config = new MsbConfig();
        MsbState rcv_btn_state = new MsbState(Constants.RCV_BTN_STATE, "Receive data over LLCP", "Receive data over LLCP");
        rcv_btn_state.setbmp_focused(rcv_focused);
        rcv_btn_state.setbmp_unfocused(rcv_unfocused);
        rcv_btn_state.setbmp_clicked(rcv_clicked);
        rcv_btn_state.setbmp_unclicked(rcv_focused);
        rcv_btn_config.addState(rcv_btn_state);
        msbf_rcv = new MultiStateButtonField(rcv_btn_config, new ReceiveCommand(), 0, Field.FIELD_HCENTER);
        btn_row.add(msbf_rcv,x2,y);

        msbf_rcv.setFocusListener(focus_listener);
        msbf_snd.setFocusListener(focus_listener);

        add(btn_row);
        add(ui_msg);
    }

    private void setMessage(String message) {
        synchronized (UiApplication.getEventLock()) {
            ui_msg.setText(message);
        }
        
    }

}
