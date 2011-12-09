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


package com.samples.toolkit.ui.container;

import com.samples.toolkit.ui.component.*;

import net.rim.device.api.ui.*;

/**
 * 
 */
public class HorizontalListStyleButtonSet extends JustifiedEvenlySpacedHorizontalFieldManager 
{
    public static final int MARGIN = 15;
    
    public HorizontalListStyleButtonSet()
    {
    }
    
    protected void sublayout( int maxWidth, int maxHeight )
    {
        Field child;
        int numChildren = this.getFieldCount();
        for( int index = 0; index < numChildren ; index++ ) {
            child = getField( index );
            if( child instanceof ListStyleButtonField ) {
                ListStyleButtonField button = ( (ListStyleButtonField) child );
                button.setDrawPosition( ListStyleButtonField.DRAWPOSITION_SINGLE );
                button.setMargin( MARGIN, MARGIN, MARGIN, MARGIN );
            }
        }
        
        super.sublayout( maxWidth, maxHeight );
    }
}
