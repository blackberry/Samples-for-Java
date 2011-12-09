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


public class EvenlySpacedHorizontalFieldManager extends Manager 
{
	private static final int SYSTEM_STYLE_SHIFT = 32;
	
    public EvenlySpacedHorizontalFieldManager( long style ) 
    {
        super( style );
    }
    
    protected void sublayout( int width, int height ) 
    {
        int availableWidth = width;

        int prevRightMargin = 0;
        int numFields = getFieldCount();
        for( int i = 0; i < numFields; i++ ) {
            Field currentField = getField( i );
            availableWidth -= Math.max( prevRightMargin, currentField.getMarginLeft() );
            prevRightMargin = currentField.getMarginRight();
        }
        availableWidth -= prevRightMargin;

        int maxHeight = 0;
        for( int i = 0; i < numFields; i++ ) {
            Field currentField = getField( i );
            int currentVerticalMargins = currentField.getMarginTop() + currentField.getMarginBottom();
            layoutChild( currentField, availableWidth, height - currentVerticalMargins );
            availableWidth -= currentField.getWidth();
            maxHeight = Math.max( maxHeight, currentField.getHeight() + currentVerticalMargins );
        }
        
        int spaceBetweenFields = isStyle( USE_ALL_WIDTH ) ? ( availableWidth / ( numFields + 1 ) ) : 0;

        prevRightMargin = 0;
        int usedWidth = 0;
        int y;
        for( int i = 0; i < numFields; i++ ) {
            
            Field currentField = getField( i );
            
            switch( (int)( ( currentField.getStyle() & FIELD_VALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
                case (int)( FIELD_BOTTOM >> SYSTEM_STYLE_SHIFT ):
                    y = maxHeight - currentField.getHeight() - currentField.getMarginBottom();
                    break;
                case (int)( FIELD_VCENTER >> SYSTEM_STYLE_SHIFT ):
                    y = currentField.getMarginTop() + ( maxHeight - currentField.getMarginTop() - currentField.getHeight() - currentField.getMarginBottom() ) >> 1;
                    break;
                default:
                    y = currentField.getMarginTop();
            }
            usedWidth += Math.max( prevRightMargin, currentField.getMarginLeft() ) + spaceBetweenFields;
            setPositionChild( currentField, usedWidth, y );
            usedWidth += currentField.getWidth();
            prevRightMargin = currentField.getMarginRight();
        }
        usedWidth += prevRightMargin;
        if( isStyle( USE_ALL_WIDTH ) ) {
            usedWidth = width;
        }
        setExtent( usedWidth, maxHeight );
    }
    
}
