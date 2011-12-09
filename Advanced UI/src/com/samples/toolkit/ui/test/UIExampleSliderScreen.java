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


public class UIExampleSliderScreen extends UIExampleScreen
{
    
    public UIExampleSliderScreen() {
        super( 0 );
        setTitle("Slider Example");
        
        new ButtonField();
        
        LabelField sliderLabel = new LabelField( "Slider. Composed of a background stretched to fit, and a thumb image. Divided into n positions. " );
        sliderLabel.setPadding(5, 5, 5, 5);
        add( sliderLabel );
        
        Bitmap slider_back = Bitmap.getBitmapResource("slider.png");
        Bitmap slider_focus = Bitmap.getBitmapResource("slider_focus.png");
        Bitmap slider_thumb = Bitmap.getBitmapResource("slider_thumb.png");
        SliderField fifthSlider = new SliderField( slider_thumb, slider_back, slider_focus, 8 , 4, 10, 10 );
        fifthSlider.setPadding( 15, 5, 15, 5 );
        add( fifthSlider );

        SliderField sixthSlider = new SliderField( slider_thumb, slider_back, slider_focus, 8 , 4, 10, 10 );
        sixthSlider.setPadding( 15, 5, 15, 5 );
        add( sixthSlider );
    }
}
