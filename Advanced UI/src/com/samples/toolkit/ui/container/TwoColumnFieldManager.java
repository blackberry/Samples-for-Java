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

import net.rim.device.api.ui.*;


public class TwoColumnFieldManager extends Manager
{
	private static final int SYSTEM_STYLE_SHIFT = 32;
    private static final int MAX_EXTENT = Integer.MAX_VALUE >> 1;  // Copied from VerticalFieldManager

    
    protected int _maxLeftColumnWidth;
    protected int _leftColumnWidth;
    
    
    public TwoColumnFieldManager( int maxLeftColumnWidth ) 
    {
        this( maxLeftColumnWidth, 0 );
    }

    public TwoColumnFieldManager( int maxLeftColumnWidth, long style ) 
    {
        super( style );
        _maxLeftColumnWidth = maxLeftColumnWidth;
    }

    public void sublayout( int width, int height )
    {
        int layoutHeight = isStyle( Manager.VERTICAL_SCROLL ) ? MAX_EXTENT : height;
        int layoutWidth = width;

        int maxLeftFieldWidth = 0;
        
        int numFields = getFieldCount();
        for( int i = 0; i < numFields; i++ ) {
            Field currentField = getField( i );
            if( currentField instanceof TwoColumnField ) {
               int currentLeftFieldWidth = ((TwoColumnField) currentField).layoutLeft( layoutWidth, layoutHeight );
               maxLeftFieldWidth = Math.max( currentLeftFieldWidth, maxLeftFieldWidth );
           }
        }
        
        int maxWidth = 0;
        int actualHeight = 0;
        int previousMarginBottom = 0;
        
        _leftColumnWidth = Math.min( maxLeftFieldWidth, _maxLeftColumnWidth );
        for( int i = 0; i < numFields; i++ ) {
            
            Field currentField = getField( i );
            if( currentField instanceof TwoColumnField ) {
               ((TwoColumnField) currentField).setLeftColumnWidth( _leftColumnWidth );
            }

            actualHeight += Math.max( currentField.getMarginTop(), previousMarginBottom );
            
            int availableChildWidth = layoutWidth - currentField.getMarginLeft() - currentField.getMarginRight();
            int availableChildHeight = layoutHeight - actualHeight - currentField.getMarginBottom();
            
            layoutChild( currentField, availableChildWidth, availableChildHeight );
            maxWidth = Math.max( maxWidth, currentField.getMarginLeft() + currentField.getWidth() + currentField.getMarginRight() );
            actualHeight += currentField.getHeight();

            previousMarginBottom = currentField.getMarginBottom();
        }


        // Now set the field positions
        int actualWidth = isStyle( USE_ALL_WIDTH ) ? layoutWidth : maxWidth;
        actualHeight = 0;
        previousMarginBottom = 0;
        
        for( int i = 0; i < numFields; i++ ) {
        
            Field currentField = getField( i );
            actualHeight += Math.max( currentField.getMarginTop(), previousMarginBottom );
            
            int availableChildWidth = actualWidth - currentField.getMarginLeft() - currentField.getMarginRight();

            int childX;
            switch( (int)( ( currentField.getStyle() & FIELD_HALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
                case (int)( FIELD_RIGHT >> SYSTEM_STYLE_SHIFT ):
                    childX = actualWidth - currentField.getWidth() - currentField.getMarginRight();
                    break;
                case (int)( FIELD_HCENTER >> SYSTEM_STYLE_SHIFT ):
                    childX = ( actualWidth - currentField.getMarginLeft() - currentField.getWidth() - currentField.getMarginRight() ) / 2;
                    break;
                default:
                    childX = currentField.getMarginLeft();
                    break;
            }
            
            // The vertical margins can overlap
            setPositionChild( currentField, childX, actualHeight );
            
            actualHeight += currentField.getHeight();
            previousMarginBottom = currentField.getMarginBottom();

        }
        
        actualHeight += previousMarginBottom;

        setVirtualExtent( actualWidth, actualHeight );
        setExtent( Math.min( actualWidth, width ), Math.min( actualHeight, height ) );
    }
    
    
}
