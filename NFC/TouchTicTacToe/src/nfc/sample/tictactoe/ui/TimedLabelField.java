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
package nfc.sample.tictactoe.ui;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import nfc.sample.tictactoe.Constants;

public class TimedLabelField extends LabelField implements Runnable {

    private String[] messages = new String[0];
    private Manager _container;
    private boolean continuous = false;

    public TimedLabelField(Manager container, String[] values) {
        super(values[0]);
        messages = values;
        _container = container;
        continuous = false;
    }

    public TimedLabelField(Manager container, String value) {
        super(value, Field.FIELD_HCENTER);
        messages = new String[1];
        messages[0] = value;
        _container = container;
        continuous = false;
    }

    public void run() {
        do {
            for(int i = 0; i < messages.length; i++) {
                // synchronized(UiApplication.getEventLock()) {
                final int text_inx = i;
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        setText(messages[text_inx]);
                    }
                });

                refresh();

                try {
                    Thread.sleep(Constants.MESSAGE_TIME);
                    // synchronized(UiApplication.getEventLock()) {
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            setText("");
                        }
                    });
                    refresh();
                } catch(final InterruptedException e) {
                    // synchronized(UiApplication.getEventLock()) {
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            setText(e.getClass().getName() + ":" + e.getMessage());
                        }
                    });
                }
            }
        } while(continuous);
    }

    public void setLabelText(String text) {
        continuous = false;
        messages = new String[1];
        messages[0] = text;
        Thread t = new Thread(this);
        t.start();
    }

    public void setLabelTexts(String[] texts, boolean continuous) {
        this.continuous = continuous;
        messages = texts;
        Thread t = new Thread(this);
        t.start();
    }

    private void refresh() {
        if(_container != null) {
            _container.invalidate();
        }
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }
}
