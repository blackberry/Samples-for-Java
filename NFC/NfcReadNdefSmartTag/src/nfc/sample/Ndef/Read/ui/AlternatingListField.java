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
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;
import nfc.sample.Ndef.Read.Utilities;

public class AlternatingListField extends ObjectListField {

    private int _bg_even;
    private int _bg_odd;

    public AlternatingListField(int bg_even, int bg_odd, long style) {
        super(style);
        _bg_even = bg_even;
        _bg_odd = bg_odd;
    }

    public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width) {
        if(index != getSelectedIndex()) {
            if(Utilities.isOdd(index)) {
                graphics.setBackgroundColor(_bg_odd);
            } else {
                graphics.setBackgroundColor(_bg_even);
            }
            graphics.clear();
        }
        super.drawListRow(listField, graphics, index, y, width);
    }

}
