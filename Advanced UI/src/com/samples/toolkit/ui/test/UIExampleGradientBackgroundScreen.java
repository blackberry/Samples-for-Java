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


import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;


public class UIExampleGradientBackgroundScreen extends UIExampleScreen
{
    public UIExampleGradientBackgroundScreen() 
    {
        Field field;
        Manager manager;
        
        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 40, 20, 10, 20 );
        manager.setPadding( 24, 0, 24, 0 );
        manager.setBackground( 
            BackgroundFactory.createLinearGradientBackground( Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK )
        );

        field = new LabelField( "Light To Dark", FIELD_HCENTER );

        manager.add( field );
        add( manager );
        

        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 10, 20, 10, 20 );
        manager.setPadding( 24, 0, 24, 0 );
        manager.setBackground( 
            BackgroundFactory.createLinearGradientBackground( Color.BLUE, Color.GREEN, Color.GREEN, Color.BLUE )
        );

        field = new LabelField( "Blue To Green", FIELD_HCENTER );

        manager.add( field );
        add( manager );


        manager = new VerticalFieldManager( USE_ALL_WIDTH );
        manager.setMargin( 10, 20, 10, 20 );
        manager.setPadding( 24, 0, 24, 0 );
        manager.setBackground( 
            BackgroundFactory.createLinearGradientBackground( Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN )
        );

        field = new LabelField( "Four Corners", FIELD_HCENTER );

        manager.add( field );
        add( manager );
        
    }
}
