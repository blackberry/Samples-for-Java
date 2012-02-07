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

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

class MediaControlStyleField extends HorizontalFieldManager
{
    MediaControlStyleField()
    {
        super(Manager.FIELD_HCENTER);
        
        add( new BitmapButtonField(Bitmap.getBitmapResource("button_back_normal.png"), Bitmap.getBitmapResource("button_back_focus.png") ));
        add( new BitmapButtonField(Bitmap.getBitmapResource("button_play_normal.png"), Bitmap.getBitmapResource("button_play_focus.png") ));
        add( new BitmapButtonField(Bitmap.getBitmapResource("button_forward_normal.png"), Bitmap.getBitmapResource("button_forward_focus.png") ));
        
        setPadding( 5, 5, 5, 5 );
        setMargin( 10, 10, 10, 10 );
    }
    
    protected void paintBackground( Graphics g )
    {
        int oldColor = g.getColor();
        try {
            g.setColor( 0x222222 );
            g.fillRoundRect( 0, 0, getWidth(), getHeight(), 20, 20 );
            g.setColor( 0x000000 );
            g.drawRoundRect( 0, 0, getWidth(), getHeight(), 20 ,20 );
        } finally {
            g.setColor( oldColor );
        }
    }
    
}
/*
class RadioControlStyleField extends HorizontalFieldManager
{
    RadioControlStyleField()
    {
        super(Manager.FIELD_HCENTER);
        
        Bitmap nextPrevBurst = Bitmap.getBitmapResource("radio_next_prev_burst.png");
        BitmapBurstButtonField prev = new BitmapBurstButtonField( Bitmap.getBitmapResource("radio_prev_unfocus.png"), Bitmap.getBitmapResource("radio_prev_focus.png"), nextPrevBurst, Field.FIELD_VCENTER );
        BitmapBurstButtonField play = new BitmapBurstButtonField( Bitmap.getBitmapResource("radio_play_unfocus.png"), Bitmap.getBitmapResource("radio_play_focus.png"), Bitmap.getBitmapResource("radio_play_burst.png"), Field.FIELD_VCENTER );
        BitmapBurstButtonField next = new BitmapBurstButtonField( Bitmap.getBitmapResource("radio_next_unfocus.png"), Bitmap.getBitmapResource("radio_next_focus.png"), nextPrevBurst, Field.FIELD_VCENTER );
        
        prev.setMargin( 10, 10, 10, 10 );
        play.setMargin( 10, 10, 10, 10 );
        next.setMargin( 10, 10, 10, 10 );
        
        add( prev );
        add( play );
        add( next );
        
        setPadding( 10, 10, 10, 10 );
        setMargin( 10, 10, 10, 10 );
    }
    
    protected void paintBackground( Graphics g )
    {
        int oldColor = g.getColor();
        try {
            g.setColor( 0x222222 );
            g.fillRoundRect( 0, 0, getWidth(), getHeight(), 20, 20 );
            g.setColor( 0x000000 );
            g.drawRoundRect( 0, 0, getWidth(), getHeight(), 20 ,20 );
        } finally {
            g.setColor( oldColor );
        }
    }
    
}
*/

/**
 * 
 */
public class UIExampleBitmapButtonScreen extends UIExampleScreen
{
    
    public UIExampleBitmapButtonScreen() {
        super( 0 );
        setTitle("BitmapButton Example");
        
        LabelField firstLabel = new LabelField( "BitmapButtonField. Two images of the same dimension create a button." );
        firstLabel.setPadding(5, 5, 5, 5);
        add( firstLabel );
        
        add( new MediaControlStyleField() );
        
        /*
        LabelField burstLabel = new LabelField( "BitmapBurstButtonField. An animated burst occurs when the field gains focus" );
        burstLabel.setPadding(5, 5, 5, 5);
        add( burstLabel );
        
        add( new RadioControlStyleField() );
        */
        
    }
}
