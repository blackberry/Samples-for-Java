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
package nfc.sample.peer2peer.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.peer2peer.Constants;
import nfc.sample.peer2peer.buttons.MsbConfig;
import nfc.sample.peer2peer.buttons.MsbState;
import nfc.sample.peer2peer.buttons.MultiStateButtonField;
import nfc.sample.peer2peer.commands.ResponderCommand;

public class RoleSelectionScreen extends MainScreen {
    
    private Bitmap vcardResponderFocused = Bitmap.getBitmapResource("vcard_responder_focus_v1.png");
    private Bitmap vcardResponderUnfocused = Bitmap.getBitmapResource("vcard_responder_no_focus_v1.png");
    private Bitmap vcardResponderClicked = Bitmap.getBitmapResource("vcard_responder_clicked_v1.png");

    private MultiStateButtonField msbfVcardResponder;
    
    private LabelField uiMsg=new LabelField("",Field.FIELD_HCENTER);
    
    private int focusedButton = 0;

    private FocusChangeListener focusListener = new FocusChangeListener() {

        public void focusChanged(Field field, int eventType) {
            if (field == msbfVcardResponder) {
                focusedButton = 0;
                setMessage("BlackBerry device will act as a vCard Responder");
            }
        }
    };

    public RoleSelectionScreen() {
        super(USE_ALL_HEIGHT | USE_ALL_WIDTH | FIELD_HCENTER | FIELD_VCENTER | NO_VERTICAL_SCROLL);
        setTitle("NfcSnepResponder V" + Constants.MYAPP_VERSION);

        AbsoluteFieldManager btnRow = new AbsoluteFieldManager();
        // bitmaps are 120 x 120
        int hgap = (int) ((Display.getWidth() - 120) / 2);
        int vgap = (int) (Display.getHeight() - 120) / 2;
        int x= hgap;
        int y = (int) (vgap * 0.75);
        
        MsbConfig vcardResponderBtnConfig = new MsbConfig();
        MsbState vcardResponderBtnState = new MsbState(Constants.VCARD_RESPONDER_BTN_STATE, 
                "vCard Responder", 
                "vCard Responder");
        vcardResponderBtnState.setbmp_focused(vcardResponderFocused);
        vcardResponderBtnState.setbmp_unfocused(vcardResponderUnfocused);
        vcardResponderBtnState.setbmp_clicked(vcardResponderClicked);
        vcardResponderBtnState.setbmp_unclicked(vcardResponderFocused);
        vcardResponderBtnConfig.addState(vcardResponderBtnState);
        msbfVcardResponder = new MultiStateButtonField(vcardResponderBtnConfig, 
                new ResponderCommand(), 0, Field.FIELD_HCENTER);
        btnRow.add(msbfVcardResponder,x,y);

        msbfVcardResponder.setFocusListener(focusListener);

        add(btnRow);
        add(uiMsg);
    }

    private void setMessage(String message) {
        synchronized (UiApplication.getEventLock()) {
            uiMsg.setText(message);
        }
    }
}
