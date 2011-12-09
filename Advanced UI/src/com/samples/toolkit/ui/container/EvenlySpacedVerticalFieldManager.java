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


public class EvenlySpacedVerticalFieldManager extends Manager 
{
	private static final int SYSTEM_STYLE_SHIFT = 32;
	
    public EvenlySpacedVerticalFieldManager( long style ) 
    {
        super( style );
    }
    
    
    protected void sublayout( int width, int height ) 
    {
        int availableHeight = height;

        // calculate remaining space after margins are considered
        int prevBottomMargin = 0;
        int numFields = getFieldCount();
        for( int i = 0; i < numFields; i++ ) {
            Field currentField = getField( i );
            availableHeight -= Math.max( prevBottomMargin, currentField.getMarginTop() );
            prevBottomMargin = currentField.getMarginBottom();
        }
        availableHeight -= prevBottomMargin;

        // Calculate the maximum amount of width needed by all the fields
        int maxWidth = 0;
        for( int i = 0; i < numFields; i++ ) {
            Field currentField = getField( i );
            int currentHorizontalMargins = currentField.getMarginLeft() + currentField.getMarginRight();
            layoutChild( currentField, width - currentHorizontalMargins, availableHeight );
            availableHeight -= currentField.getHeight();
            maxWidth = Math.max( maxWidth, currentField.getWidth() + currentHorizontalMargins );
        }
        
        int spaceBetweenFields = isStyle( USE_ALL_HEIGHT ) ? ( availableHeight / ( numFields + 1 ) ) : 0;

        prevBottomMargin = 0;
        int usedHeight = 0;
        int x;
        for( int i = 0; i < numFields; i++ ) {
            
            Field currentField = getField( i );
            
            switch( (int)( ( currentField.getStyle() & FIELD_HALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
                case (int)( FIELD_RIGHT >> SYSTEM_STYLE_SHIFT ):
                    x = maxWidth - currentField.getWidth() - currentField.getMarginRight();
                    break;
                case (int)( FIELD_LEFT >> SYSTEM_STYLE_SHIFT ):
                    x = currentField.getMarginLeft() + ( maxWidth - currentField.getMarginLeft() - currentField.getWidth() - currentField.getMarginRight() ) >> 1;
                    break;
                case (int)( FIELD_HCENTER >> SYSTEM_STYLE_SHIFT ):
                    x = ( maxWidth - currentField.getWidth() ) >> 1;
                    break;
                default:
                    x = currentField.getMarginLeft();
            }
            usedHeight += Math.max( prevBottomMargin, currentField.getMarginTop() ) + spaceBetweenFields;
            setPositionChild( currentField, x, usedHeight );
            usedHeight += currentField.getHeight();
            prevBottomMargin = currentField.getMarginBottom();
        }
        usedHeight += prevBottomMargin;
        if( isStyle( USE_ALL_HEIGHT ) ) {
            usedHeight = height;
        }
        setExtent( maxWidth, usedHeight );
    }
}
