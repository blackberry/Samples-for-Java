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
import net.rim.device.api.ui.decor.*;


public class UIExampleBitmapBorderScreen extends UIExampleScreen
{
    public UIExampleBitmapBorderScreen() 
    {
        Field field;
        
        field = new LabelField( "Edit Field Style", FIELD_HCENTER );
        field.setMargin( 40, 0, 10, 0 );
        field.setBorder(
            BorderFactory.createBitmapBorder( 
                new XYEdges( 10, 9, 9, 11 ),
                Bitmap.getBitmapResource( "border_edit.png" )
            )
        );

        add( field );
        

        field = new LabelField( "Speech Bubble", FIELD_HCENTER );
        field.setMargin( 10, 0, 10, 0 );
        field.setBorder(
            BorderFactory.createBitmapBorder( 
                new XYEdges( 16, 16, 27, 23 ), 
                Bitmap.getBitmapResource( "border_bubble_left.png" ) 
            )
        );

        add( field );
        

        field = new LabelField( "Blue Outline", FIELD_HCENTER );
        field.setMargin( 10, 0, 10, 0 );
        field.setBorder(
            BorderFactory.createBitmapBorder( 
                new XYEdges( 18, 18, 18, 18 ), 
                Bitmap.getBitmapResource( "border_blue_outline.png" ) 
            )
        );

        add( field );
    }
}
