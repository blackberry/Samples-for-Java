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
import net.rim.device.api.ui.decor.*;


public class UIExampleSimpleBorderScreen extends UIExampleScreen
{
    public UIExampleSimpleBorderScreen() 
    {
        Field field;
        
        field = new LabelField( "Solid Black", FIELD_HCENTER );
        field.setMargin( 40, 0, 10, 0 );
        field.setPadding( 10, 10, 10, 10 );
        field.setBorder(
            BorderFactory.createSimpleBorder( new XYEdges( 2, 2, 2, 2 ) )
        );

        add( field );
        

        field = new LabelField( "Dotted Red", FIELD_HCENTER );
        field.setMargin( 10, 0, 10, 0 );
        field.setPadding( 10, 10, 10, 10 );
        field.setBorder(
            BorderFactory.createSimpleBorder( 
                new XYEdges( 2, 2, 2, 2 ), 
                new XYEdges( Color.RED, Color.RED, Color.RED, Color.RED ), 
                Border.STYLE_DOTTED 
            )
        );

        add( field );
        

        field = new LabelField( "Solid/Dashed Blue", FIELD_HCENTER );
        field.setMargin( 10, 0, 10, 0 );
        field.setPadding( 10, 10, 10, 10 );
        field.setBorder(
            BorderFactory.createSimpleBorder( 
                new XYEdges( 6, 2, 6, 2 ), 
                new XYEdges( Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE ), 
                new XYEdges( Border.STYLE_SOLID, Border.STYLE_DASHED, Border.STYLE_SOLID, Border.STYLE_DASHED )
            )
        );

        add( field );
        
        
        field = new LabelField( "Solid Colours", FIELD_HCENTER );
        field.setMargin( 10, 0, 10, 0 );
        field.setPadding( 10, 10, 10, 10 );
        field.setBorder(
            BorderFactory.createSimpleBorder(
                new XYEdges( 1, 2, 3, 2 ),
                new XYEdges( Color.GREEN, Color.AQUA, Color.BLUE, Color.CRIMSON ), 
                new XYEdges( Border.STYLE_SOLID, Border.STYLE_SOLID, Border.STYLE_SOLID, Border.STYLE_SOLID )
            )
        );

        add( field );
 
        
        field = new LabelField( "Solid Multicolor", FIELD_HCENTER );
        field.setMargin( 10, 0, 10, 0 );
        field.setPadding( 10, 10, 10, 10 );
        field.setBorder(
            BorderFactory.createSimpleBorder(
                new XYEdges( 2, 3, 3, 4 ), 
                new XYEdges( Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW), 
                new XYEdges( Border.STYLE_SOLID, Border.STYLE_SOLID, Border.STYLE_SOLID, Border.STYLE_SOLID )
            )
        );
        
        add( field );
        

    }
}
