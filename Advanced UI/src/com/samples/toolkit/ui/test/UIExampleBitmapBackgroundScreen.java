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


import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;


public class UIExampleBitmapBackgroundScreen extends UIExampleScreen
{
    public UIExampleBitmapBackgroundScreen() 
    {
        Field field;
        Manager manager;
        
        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 40, 20, 10, 20 );
        manager.setPadding( 48, 0, 48, 0 );
        manager.setBackground( 
            BackgroundFactory.createBitmapBackground( 
                Bitmap.getBitmapResource( "background_symbols_1.png" ),
                0, 0, Background.REPEAT_BOTH
            )
        );
        
        field = new LabelField( "Symbols 1", FIELD_HCENTER );
        field.setPadding( 16, 16, 16, 16 );
        field.setBackground(
            BackgroundFactory.createSolidTransparentBackground( Color.WHITE, 0x80 )
        );

        manager.add( field );
        add( manager );
        

        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 10, 20, 10, 20 );
        manager.setPadding( 48, 0, 48, 0 );
        manager.setBackground( 
            BackgroundFactory.createBitmapBackground( 
                Bitmap.getBitmapResource( "background_symbols_2.png" ),
                0, 0, Background.REPEAT_BOTH
            )
        );
        
        field = new LabelField( "Symbols 2", FIELD_HCENTER );
        field.setPadding( 16, 16, 16, 16 );
        field.setBackground(
            BackgroundFactory.createSolidTransparentBackground( Color.WHITE, 0x80 )
        );

        manager.add( field );
        add( manager );
        
       
        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 10, 20, 10, 20 );
        manager.setPadding( 48, 0, 48, 0 );
        manager.setBackground( 
            BackgroundFactory.createBitmapBackground( 
                Bitmap.getBitmapResource( "background_honeycomb_1.png" ),
                0, 0, Background.REPEAT_BOTH
            )
        );
        
        field = new LabelField( "Honeycomb", FIELD_HCENTER );
        field.setPadding( 16, 16, 16, 16 );
        field.setBackground(
            BackgroundFactory.createSolidTransparentBackground( Color.WHITE, 0x80 )
        );

        manager.add( field );
        add( manager );
        
        
        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 10, 20, 10, 20 );
        manager.setPadding( 48, 0, 48, 0 );
        manager.setBackground( 
            BackgroundFactory.createBitmapBackground( 
                Bitmap.getBitmapResource( "background_thatched_1.png" ),
                0, 0, Background.REPEAT_BOTH
            )
        );
        
        field = new LabelField( "Thatched", FIELD_HCENTER );
        field.setPadding( 16, 16, 16, 16 );
        field.setBackground(
            BackgroundFactory.createSolidTransparentBackground( Color.WHITE, 0x80 )
        );

        manager.add( field );
        add( manager );      
        
    }
    
}
