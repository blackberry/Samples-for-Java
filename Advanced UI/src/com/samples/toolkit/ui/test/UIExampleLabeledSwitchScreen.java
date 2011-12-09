/*
* Copyright (c) 2011 Research In Motion Limited.
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


package com.samples.toolkit.ui.test;

import com.samples.toolkit.ui.component.*;
import com.samples.toolkit.ui.container.*;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;

/**
 * 
 */
public class UIExampleLabeledSwitchScreen extends UIExampleScreen
{
    
    public UIExampleLabeledSwitchScreen()
    {
        setTitle("LabeledSwitch Example");
        
        LabelField switchLabel = new LabelField( "LabeledSwitch. Like a switch, but with a small state label." );
        switchLabel.setPadding(5, 5, 5, 5);
        add( switchLabel );
        
        Bitmap switch_left = Bitmap.getBitmapResource("switch_left.png");
        Bitmap switch_right = Bitmap.getBitmapResource("switch_right.png");
        Bitmap switch_left_focus = Bitmap.getBitmapResource("switch_left_focus.png");
        Bitmap switch_right_focus = Bitmap.getBitmapResource("switch_right_focus.png");

        LabeledSwitch callSwitch = new LabeledSwitch(switch_left, switch_right, switch_left_focus, switch_right_focus, "on", "off", true );
        JustifiedHorizontalFieldManager phoneCalls = new JustifiedHorizontalFieldManager( new LabelField( "Phone Calls" ), callSwitch, false, USE_ALL_WIDTH );
        phoneCalls.setPadding(5,5,5,5);
        add(phoneCalls);

        LabeledSwitch msgSwitch = new LabeledSwitch(switch_left, switch_right, switch_left_focus, switch_right_focus, "on", "off", false );
        JustifiedHorizontalFieldManager messages = new JustifiedHorizontalFieldManager( new LabelField( "Messages" ), msgSwitch, false, USE_ALL_WIDTH );
        messages.setPadding(5,5,5,5);
        add(messages);
        
        LabeledSwitch reminderSwitch = new LabeledSwitch(switch_left, switch_right, switch_left_focus, switch_right_focus, "on", "off", true );
        JustifiedHorizontalFieldManager reminders = new JustifiedHorizontalFieldManager( new LabelField( "Reminders" ), reminderSwitch, false, USE_ALL_WIDTH );
        reminders.setPadding(5,5,5,5);
        add(reminders);
    }
}
