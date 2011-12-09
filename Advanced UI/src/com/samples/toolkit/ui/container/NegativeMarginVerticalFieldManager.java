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
public class NegativeMarginVerticalFieldManager extends Manager
{
    private static final int MAX_EXTENT = Integer.MAX_VALUE >> 1;

    public NegativeMarginVerticalFieldManager( long style ) 
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

        if( isStyle( Manager.VERTICAL_SCROLL ) && !isStyle( Manager.NO_VERTICAL_SCROLL ) ) {
            heightAvail = MAX_EXTENT;
        }

        int prevMarginBottom = 0;
        int marginTop = 0;
        int marginBottom = 0;
        int marginHorizontal = 0;
        int numFields = this.getFieldCount();

        for( int i = 0; i < numFields; ++i ) {
            field = getField( i );
            
            marginHorizontal =  field.getMarginLeft() + field.getMarginRight();
            marginTop = calculateVerticalMargin( prevMarginBottom, field.getMarginTop() );
            marginBottom = field.getMarginBottom();
            
            layoutChild( field, widthAvail - marginHorizontal, heightAvail - marginTop - marginBottom );
            
            heightAvail -= field.getHeight() + marginTop;
            height += field.getHeight() + marginTop;
            
            prevMarginBottom = marginBottom;
            
            // remember the largest width
            int marginAndWidth = marginHorizontal + field.getWidth() ;
            if( marginAndWidth > width ) {
                width = marginAndWidth;
            }
        }
        height += prevMarginBottom;
        
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
        prevMarginBottom = 0;
        for( int i = 0; i < numFields; ++i ) {
            field = getField( i );
            
            marginTop = calculateVerticalMargin( prevMarginBottom, field.getMarginTop() );
            
             if( field.isStyle( Field.FIELD_HCENTER ) ) {
                x = ( width - field.getWidth() ) / 2;
             } else if( field.isStyle( Field.FIELD_RIGHT ) ) {
                x = width - field.getWidth() - field.getMarginRight();
             } else {
                // Field.FIELD_LEFT
                x = field.getMarginLeft();
            }
                
            setPositionChild( field, x, y + marginTop );
            
            y += field.getHeight() + marginTop;
            prevMarginBottom = field.getMarginBottom();
        }
        
        setExtent( Math.min( width, maxWidth ), Math.min( height, maxHeight ) );
    }
    
    /**
     * To account for negative margins
     */
    private int calculateVerticalMargin( int prevMarginBottom, int marginTop ) 
    {
        int max = Math.max( prevMarginBottom, marginTop );
        int sum = prevMarginBottom + marginTop;
        if( sum < max ) {
            max += ( sum - max );
        }
        return max;
    }
    
}    
