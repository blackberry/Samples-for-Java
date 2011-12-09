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



/**
 * 
 */
public class UIExampleHyperlinkScreen extends UIExampleScreen
{
    public UIExampleHyperlinkScreen() {
        
        setTitle( "HyperlinkButtonField" );
        
        addLink( "BitmapButton" );
        addLink( "Custom Buttons" );
        addLink( "Calendar Range Selection" );
        addLink( "Colour Picker" );
        addLink( "Gauge" );
        addLink( "ListStyleButton" );
        addLink( "Searchable Collection" );
        addLink( "Slider" );
        addLink( "Switch" );
        addLink( "Transitions" );
        addLink( "Transition Cube" );
    }
    
    private void addLink( final String label )
    {
        add( new MyHyperlinkButtonField( label ) );
    }
    
    class MyHyperlinkButtonField extends HyperlinkButtonField
    {
        MyHyperlinkButtonField( String label ) {
            super( label, 0x0000FF, 0xFFFFFF, 0x0000FF, 0, 0 );
            setPadding( 8, 5, 8, 5 );
        }
    }
}
