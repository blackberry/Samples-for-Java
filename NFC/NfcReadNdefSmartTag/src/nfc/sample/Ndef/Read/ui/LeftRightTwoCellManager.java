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
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;

public class LeftRightTwoCellManager extends Manager {
    
    private int _padding_left;
    private int _padding_right;
    private int _padding_top;
    
    
    public LeftRightTwoCellManager(int padding_left, int padding_right, int padding_top, long style) {
        super(style);
        _padding_left = padding_left;
        _padding_right = padding_right;
        _padding_top = padding_top;
    }

    protected void sublayout(int width, int height) {
        Field field;

        // Field 0 goes to the left, Field 1 to the right 
        int y = _padding_top;

        if (getFieldCount() > 0) {
            field = getField(0);
            setPositionChild(field, _padding_left, y);
            layoutChild(field, field.getPreferredWidth(), field.getPreferredHeight());
        }

        if (getFieldCount() > 1) {
            field = getField(1);
            int field_width = field.getPreferredWidth();
            int x = Display.getWidth() - field_width - _padding_right;
            setPositionChild(field, x, y);
            layoutChild(field, field.getPreferredWidth(), field.getPreferredHeight());
        }

        // Set the manager's dimensions
        setExtent(width, getPreferredHeight());
    }

    // All of available width
    public int getPreferredWidth() {
        int pw = Display.getWidth();
        return pw;
    }

    // 10% of available height
    public int getPreferredHeight() {
        int ph = Display.getHeight() / 10;
        return ph;
    }

    public void add(Field field) {
        if (getFieldCount() < 2) {
            super.add(field);
        } else {
            throw new IllegalStateException("Manager already has the maximum of 2 fields as members");
        }
    }
    
}