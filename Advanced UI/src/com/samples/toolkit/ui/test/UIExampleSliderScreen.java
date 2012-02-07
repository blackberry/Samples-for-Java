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
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.decor.*;

public class UIExampleSliderScreen extends UIExampleScreen
{
    
    public UIExampleSliderScreen() 
    {
        super( 0 );
        setTitle( "Slider Example" );
        
        SliderField slider;
        
        slider = new SliderField( 
            Bitmap.getBitmapResource( "slider_thumb_normal.png" ),  Bitmap.getBitmapResource( "slider_progress_normal.png" ),  Bitmap.getBitmapResource( "slider_base_normal.png" ),
            Bitmap.getBitmapResource( "slider_thumb_focused.png" ), Bitmap.getBitmapResource( "slider_progress_focused.png" ), Bitmap.getBitmapResource( "slider_base_focused.png"),
            Bitmap.getBitmapResource( "slider_thumb_pressed.png" ), Bitmap.getBitmapResource( "slider_progress_pressed.png" ), Bitmap.getBitmapResource( "slider_base_pressed.png"),
            10, 0, 12, 12, FOCUSABLE );
        slider.setPadding( 20, 20, 20, 20 );
        slider.setBackground( BackgroundFactory.createSolidBackground( 0xD3D3D3 ) );
        add( slider );

        slider = new SliderField( 
            Bitmap.getBitmapResource( "slider2_thumb_normal.png" ),  Bitmap.getBitmapResource( "slider2_progress_normal.png" ),  Bitmap.getBitmapResource( "slider2_base_normal.png" ),
            Bitmap.getBitmapResource( "slider2_thumb_focused.png" ), Bitmap.getBitmapResource( "slider2_progress_focused.png" ), Bitmap.getBitmapResource( "slider2_base_focused.png"),
            20, 0, 16, 16, FOCUSABLE );
        slider.setPadding( 20, 20, 20, 20 );
        add( slider );

        slider = new SliderField( 
            Bitmap.getBitmapResource( "slider3_thumb_normal.png" ),  Bitmap.getBitmapResource( "slider3_progress_normal.png" ),  Bitmap.getBitmapResource( "slider3_base_normal.png" ),
            Bitmap.getBitmapResource( "slider3_thumb_focused.png" ), Bitmap.getBitmapResource( "slider3_progress_focused.png" ), Bitmap.getBitmapResource( "slider3_base_focused.png"),
            Bitmap.getBitmapResource( "slider3_thumb_pressed.png" ), Bitmap.getBitmapResource( "slider3_progress_pressed.png" ), Bitmap.getBitmapResource( "slider3_base_pressed.png"),
            5, 0, 8, 8, FOCUSABLE );
        slider.setPadding( 20, 20, 20, 20 );
        slider.setBackground( BackgroundFactory.createSolidBackground( 0xD3D3D3 ) );
        add( slider );

    }
}
