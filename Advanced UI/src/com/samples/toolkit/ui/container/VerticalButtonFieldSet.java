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

import com.samples.toolkit.ui.*;

import net.rim.device.api.ui.*;

/**
 * VerticalButtonFieldSet aligns a group of buttons vertically
 * so that they have equal widths
 * 
 * This Manager does not respect Horizontal style bits on the child nodes
 * since they're all fully justified
 * 
 * If USE_ALL_WIDTH is passed into this manager then the children 
 * will also use all available width
 */
public class VerticalButtonFieldSet extends Manager 
{
    
    public VerticalButtonFieldSet( )
    {
        this( Field.FIELD_HCENTER );
    } 
    
    public VerticalButtonFieldSet( long style ) 
    {
        super( style );
    }
    
    protected void sublayout( int width, int height ) 
    {
        int maxWidth   = 0;
        int numChildren = this.getFieldCount();
        
        if( isStyle( USE_ALL_WIDTH ) ) {
            // use all the width
            maxWidth = width;
        
        } else {
                    
            for( int i = 0; i < numChildren; i++ ) {
                Field currentField = getField( i );
                int currentPreferredWidth = currentField.getPreferredWidth() + FieldDimensionUtilities.getBorderWidth( currentField );
                maxWidth  = Math.max( maxWidth, currentPreferredWidth );
            }
        }
        
        int prevTopMargin = 0;
        int usedHeight = 0;
        int x;
        for( int i = 0; i < numChildren; i++ ) {
            
            Field currentField = getField( i );
            int currentPreferredWidth = currentField.getPreferredWidth() + FieldDimensionUtilities.getBorderWidth( currentField );
            if( currentPreferredWidth < maxWidth ) {
                int newPadding = ( maxWidth - currentPreferredWidth ) / 2; 
                currentField.setPadding( currentField.getPaddingTop(), newPadding, currentField.getPaddingBottom(), newPadding );
            }
            layoutChild( currentField, maxWidth, height );
            
            usedHeight += Math.max( prevTopMargin, currentField.getMarginBottom() );
            x = ( maxWidth - currentField.getWidth() ) / 2;
            setPositionChild( currentField, x, usedHeight );
            usedHeight += currentField.getHeight();
            prevTopMargin = currentField.getMarginBottom();
        }
        setExtent( maxWidth, usedHeight );
    }
    
    protected boolean navigationMovement(int dx, int dy, int status, int time) 
    {
        int focusIndex = getFieldWithFocusIndex();                   
        if ( dx < 0 && focusIndex == 0 ) {
            // we cannot go left
            return true;
        }
        if( dx > 0 && focusIndex == getFieldCount()-1 ) {
            // we cannot go right
            return true;
        }
        return super.navigationMovement( dx, dy, status, time );
    }    
}
