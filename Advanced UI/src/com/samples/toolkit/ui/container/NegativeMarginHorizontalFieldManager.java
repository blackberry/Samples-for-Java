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

/**
 * A basic vertical field manager that supports negative vertical margins
 * It also supports horizontal style bits.
 */
public class NegativeMarginHorizontalFieldManager extends Manager
{
    private static final int MAX_EXTENT = Integer.MAX_VALUE >> 1;

    public NegativeMarginHorizontalFieldManager( long style ) 
    {
        super( style );
    }
    
    protected void sublayout( int maxWidth, int maxHeight )
    {
        Field   field;
        int     width = 0;
        int     height = 0;

        // how much height do we have?
        int heightAvail = maxHeight;
        int widthAvail = maxWidth;

        if( isStyle( Manager.HORIZONTAL_SCROLL ) && !isStyle( Manager.NO_HORIZONTAL_SCROLL ) ) {
            widthAvail = MAX_EXTENT;
        }

        int prevMarginRight = 0;
        int marginLeft = 0;
        int marginRight = 0;
        int marginVertical = 0;
        int numFields = this.getFieldCount();

        for( int i = 0; i < numFields; ++i ) {
            field = getField( i );
            
            marginVertical =  field.getMarginTop() + field.getMarginBottom();
            marginLeft = calculateHorizontalMargin( prevMarginRight, field.getMarginLeft() );
            marginRight = field.getMarginRight();
            
            layoutChild( field, widthAvail - marginLeft - marginRight, heightAvail - marginVertical );
            
            widthAvail -= field.getHeight() + marginLeft;
            width += field.getHeight() + marginLeft;
            
            prevMarginRight = marginRight;
            
            // remember the largest width
            int marginAndHeight = marginVertical + field.getHeight() ;
            if( marginAndHeight > height ) {
                height = marginAndHeight;
            }
        }
        width += prevMarginRight;
        
        if( width < maxWidth && isStyle( Field.USE_ALL_WIDTH ) ) {
            width = maxWidth;
        }
        
        if( height < maxHeight && isStyle( Field.USE_ALL_HEIGHT ) ) {
            height = maxHeight;
        }
        
        setVirtualExtent( width, height );
        
        // Set positions
        int x = 0;
        int y = 0;
        prevMarginRight = 0;
        for( int i = 0; i < numFields; ++i ) {
            field = getField( i );
            
            marginLeft = calculateHorizontalMargin( prevMarginRight, field.getMarginLeft() );
            
            if( field.isStyle( Field.FIELD_TOP ) ) {
                y = field.getMarginTop();
            } else if( field.isStyle( Field.FIELD_BOTTOM ) ) {
                y = height - field.getHeight() - field.getMarginBottom();
            } else if( field.isStyle( Field.FIELD_VCENTER ) ) {
                y = ( height - field.getHeight() ) / 2;
            } 
                
            setPositionChild( field, x + marginLeft, y );
            
            x += field.getWidth() + marginLeft;
            prevMarginRight = field.getMarginRight();
        }
        
        setExtent( Math.min( width, maxWidth ), Math.min( height, maxHeight ) );
    }
    
    /**
     * To account for negative margins
     */
    private int calculateHorizontalMargin( int prevMarginRight, int marginLeft ) 
    {
        int max = Math.max( prevMarginRight, marginLeft );
        int sum = prevMarginRight + marginLeft;
        if( sum < max ) {
            max += ( sum - max );
        }
        return max;
    }
    
}    
