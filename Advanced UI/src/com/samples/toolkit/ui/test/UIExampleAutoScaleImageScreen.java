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
import net.rim.device.api.ui.decor.*;


public class UIExampleAutoScaleImageScreen extends UIExampleScreen
{
    public UIExampleAutoScaleImageScreen()
    {
        Field field;
        Manager manager;
     
        EncodedImage normalImage = EncodedImage.getEncodedImageResource( "logo_new_large.png" );
        long style = Field.FOCUSABLE | AutoScaleImageField.REDUCE_TO_WIDTH | AutoScaleImageField.REDUCE_TO_WIDTH;
        
        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 40, 20, 10, 20 );
        manager.setPadding( 16, 16, 16, 16 );
        manager.setBackground(
            BackgroundFactory.createSolidBackground( 0x79BC00 )
        );
        
        field = new AutoScaleImageField( normalImage, normalImage, style );

        manager.add( field );
        add( manager );
        

        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 20, 40, 10, 40 );
        manager.setPadding( 16, 16, 16, 16 );
        manager.setBackground(
            BackgroundFactory.createSolidBackground( 0x79BC00 )
        );
        
        field = new AutoScaleImageField( normalImage, normalImage, style );

        manager.add( field );
        add( manager );

        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 20, 60, 10, 60 );
        manager.setPadding( 16, 16, 16, 16 );
        manager.setBackground(
            BackgroundFactory.createSolidBackground( 0x79BC00 )
        );
        
        field = new AutoScaleImageField( normalImage, normalImage, style );

        manager.add( field );
        add( manager );

        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 20, 80, 10, 80 );
        manager.setPadding( 16, 16, 16, 16 );
        manager.setBackground(
            BackgroundFactory.createSolidBackground( 0x79BC00 )
        );
        
        field = new AutoScaleImageField( normalImage, normalImage, style );

        manager.add( field );
        add( manager );
        
    }
}
