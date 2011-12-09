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
public class HorizontalButtonFieldSet extends Manager 
{
    public static final int AXIS_SEQUENTIAL = 0;
    public static final int AXIS_VERTICAL   = 1 << 1;
    
    public HorizontalButtonFieldSet()
    {
        this( 0 );
    } 
    
    public HorizontalButtonFieldSet( long style ) 
    {
        super( style );
    }
    
    protected void sublayout( int width, int height )
    {
        int availableWidth = width;

        int numFields = getFieldCount();
        int maxPreferredWidth = 0;
        int maxHeight = 0;


        // There may be a few remaining pixels after dividing up the space
        // we must split up the space between the first and last buttons
        int fieldWidth = width / numFields;
        int firstFieldExtra = 0;
        int lastFieldExtra = 0;
        
        int unUsedWidth = width - fieldWidth * numFields;
        if( unUsedWidth > 0 ) {
            firstFieldExtra = unUsedWidth / 2;
            lastFieldExtra = unUsedWidth - firstFieldExtra;
        }
        
        int prevRightMargin = 0;
        
        // Layout the child fields, and calculate the max height
        for( int i = 0; i < numFields; i++ ) {
            
            int nextLeftMargin = 0;
            if( i < numFields - 1 ) {
                Field nextField = getField( i );
                nextLeftMargin = nextField.getMarginLeft();
            }
            
            Field currentField = getField( i );
            
            int widthForButton = fieldWidth;
            
            int leftMargin  = Math.max( prevRightMargin, currentField.getMarginLeft() ) / 2;
            int rightMargin = Math.max( nextLeftMargin, currentField.getMarginRight() ) / 2;
            if( i == 0 ) {
                widthForButton = fieldWidth + firstFieldExtra;
                leftMargin = currentField.getMarginLeft();
            } else if( i == numFields -1 ) {
                widthForButton = fieldWidth + lastFieldExtra;
                rightMargin = currentField.getMarginRight();
            }
            
            int currentVerticalMargins = currentField.getMarginTop() + currentField.getMarginBottom();
            int currentHorizontalMargins = leftMargin + rightMargin;
            
            widthForButton -= currentHorizontalMargins;
            
            int currentPreferredWidth = currentField.getPreferredWidth() + FieldDimensionUtilities.getBorderWidth( currentField );
            if( currentPreferredWidth < widthForButton ) {
                int newPadding = ( widthForButton - currentPreferredWidth ) / 2; 
                currentField.setPadding( currentField.getPaddingTop(), newPadding, currentField.getPaddingBottom(), newPadding );
            }
            layoutChild( currentField, widthForButton, height );
            maxHeight = Math.max( maxHeight, currentField.getHeight() + currentVerticalMargins );
   
            prevRightMargin = rightMargin;
            nextLeftMargin = 0;
        }

        // Now position the fields, respecting the Vertical style bits
        int usedWidth = 0;
        int y;
        prevRightMargin = 0;
        for( int i = 0; i < numFields; i++ ) {
            
            Field currentField = getField( i );
            int marginTop = currentField.getMarginTop();
            int marginBottom = currentField.getMarginBottom();
            int marginLeft = Math.max( currentField.getMarginLeft(), prevRightMargin );
            int marginRight = currentField.getMarginRight();
            
            if( currentField.isStyle( FIELD_BOTTOM ) ) {
                    y = maxHeight - currentField.getHeight() - currentField.getMarginBottom();
            } else if( currentField.isStyle( FIELD_VCENTER ) ) {
                    y = marginTop + ( maxHeight - marginTop - currentField.getHeight() - marginBottom ) >> 1;
            } else {
            	y = marginTop;
            }
            setPositionChild( currentField, usedWidth + marginLeft, y );
            usedWidth += currentField.getWidth() + marginLeft;
            prevRightMargin = marginRight;
        }
        setExtent( width, maxHeight );
    }  
}
