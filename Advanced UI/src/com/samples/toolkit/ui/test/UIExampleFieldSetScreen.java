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
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.component.*;


/**
 * Illustrates the use of the FieldSet
 */
public class UIExampleFieldSetScreen extends UIExampleScreen
{
    
    public UIExampleFieldSetScreen( Border titleBorder, Border contentBorder ) {

        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT );
        setTitle("Field Set Examples");

        ForegroundManager foreground = new ForegroundManager();
        
        FieldSet set = new FieldSet( "Sound", titleBorder, contentBorder, USE_ALL_WIDTH );
        set.add( new LabelField( "Volume" ) );
        set.setMargin( 10, 10, 10, 10 );
        
        SliderField slider = new SliderField( 
            Bitmap.getBitmapResource( "slider2_thumb_normal.png" ),  Bitmap.getBitmapResource( "slider2_progress_normal.png" ),  Bitmap.getBitmapResource( "slider2_base_normal.png" ),
            Bitmap.getBitmapResource( "slider2_thumb_focused.png" ), Bitmap.getBitmapResource( "slider2_progress_focused.png" ), Bitmap.getBitmapResource( "slider2_base_focused.png"),
            10, 0, 16, 16, FOCUSABLE );
        set.add( slider );
        
        foreground.add( set );

        set = new FieldSet( "Font", titleBorder, contentBorder, USE_ALL_WIDTH );
        set.setMargin( 10, 10, 10, 10 );
        
        TwoColumnFieldManager columns = new TwoColumnFieldManager( Display.getWidth() / 2, USE_ALL_WIDTH );
        columns.add( new TwoColumnField( new LabelField( "Kerning", FIELD_VCENTER ), new ButtonField( "Test", FIELD_RIGHT ), USE_ALL_WIDTH ) );
        columns.add( new TwoColumnField( new LabelField( "Truncate", FIELD_VCENTER ), new ButtonField( "Test", FIELD_RIGHT ), USE_ALL_WIDTH ) );
        set.add( columns );
        
        foreground.add( set );
        
        
        set = new FieldSet( "Battery", titleBorder, contentBorder, USE_ALL_WIDTH );
        set.setMargin( 10, 10, 10, 10 );
        
        HorizontalButtonFieldSet buttonSet = new HorizontalButtonFieldSet();
        
        ButtonField saveButton = new ButtonField( "Eject" );
        ButtonField discardButton = new ButtonField( "Cancel" );
        
        saveButton.setMargin(  5, 5, 5, 5 );
        discardButton.setMargin( 5, 5, 5 ,5 );
        
        buttonSet.add( saveButton );
        buttonSet.add( discardButton );
        
        set.add( new LabelField( "Eject battery now?" ) );
        set.add( buttonSet );
    
        foreground.add( set );
        
        
        set = new FieldSet( "Sound", titleBorder, contentBorder, USE_ALL_WIDTH );

        add( foreground );
        
    }
}
