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

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.llcp.Constants;
import nfc.sample.llcp.nfc.LlcpSender;

public class ClientTextScreen extends MainScreen {

    private EditField text = new EditField("Send to other device: ", "Hello! LLCP is useful!");
    private ButtonField btn_continue = new ButtonField("Continue", ButtonField.CONSUME_CLICK);
    
    private FieldChangeListener listener = new FieldChangeListener() {

        public void fieldChanged(Field field, int context) {
            if (field == btn_continue) {
                ActivityScreen screen = new ActivityScreen(Constants.SND);
                LlcpSender sender = new LlcpSender(screen,text.getText());
                sender.start();
                UiApplication.getUiApplication().pushScreen(screen);
            }
        }
    };

    public ClientTextScreen() {
        setTitle("LLCPDemo V" + Constants.MYAPP_VERSION);
        add(text);
        add(btn_continue);
        btn_continue.setChangeListener(listener);        
    }

    public boolean onSavePrompt() {
        return true;
    }

}
