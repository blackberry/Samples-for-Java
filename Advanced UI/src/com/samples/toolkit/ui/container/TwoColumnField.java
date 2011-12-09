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

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.NullField;

public class TwoColumnField extends Manager
{
	private static final int SYSTEM_STYLE_SHIFT = 32;
    
    private Field _leftField;
    private Field _rightField;

    protected int _leftColumnWidth;
    
    protected static final int COLUMN_HPADDING = Display.getWidth() <= 320 ? 4 : 6;
    
    
    public TwoColumnField()
    {
        this( null, null );
    }
    
    public TwoColumnField( Field leftField, Field rightField ) 
    {
        this( leftField, rightField, 0 );
    }
    
    public TwoColumnField( Field leftField, Field rightField, long style ) 
    {
        super( style );

        if( leftField == null ) {
            leftField = new NullField();
        }
        if( rightField == null ) {
            rightField = new NullField();
        }

        _leftField = leftField;
        _rightField = rightField;
        
        add( _leftField );
        add( _rightField );
    }

    public Field getLeftField()
    {
        return _leftField;
    }
    
    public Field getRightField()
    {
        return _rightField;
    }
    
    public void replace( Field oldField, Field newField )
    {
        super.replace( oldField, newField );
        if( oldField == _leftField ) {
            _leftField = newField;
        } else if( oldField == _rightField ) {
            _rightField = newField;
        }
    }

    /**
     * Lays out the the left field and then return the width that it would like to have.
     */
    public int layoutLeft( int width, int height )
    {
        layoutChild( _leftField, width, height );
        return _leftField.getWidth();
    }
    
    public void setLeftColumnWidth( int leftColumnWidth )
    {
        _leftColumnWidth = leftColumnWidth;
    }
    
    protected void sublayout( int width, int height )
    {
        // TODO this code should respect margins, probably...
        
        if( !(getManager() instanceof TwoColumnFieldManager) ) {
            throw new IllegalStateException();
        }
        
        layoutChild( _leftField, _leftColumnWidth, height );
        layoutChild( _rightField, width - _leftColumnWidth - COLUMN_HPADDING, height );
        
        int actualWidth = isStyle( Manager.USE_ALL_WIDTH ) ? width : ( _leftColumnWidth + COLUMN_HPADDING + _rightField.getWidth() );
        int actualRightColumnWidth = actualWidth - _leftColumnWidth - COLUMN_HPADDING;
        
        int actualHeight = Math.max( _leftField.getHeight(), _rightField.getHeight() );
        
        int leftX;
        switch( (int)( ( _leftField.getStyle() & FIELD_HALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
            case (int)( FIELD_RIGHT >> SYSTEM_STYLE_SHIFT ):
                leftX = _leftColumnWidth - _leftField.getWidth();
                break;
            case (int)( FIELD_HCENTER >> SYSTEM_STYLE_SHIFT ):
                leftX = ( _leftColumnWidth - _leftField.getWidth() ) / 2;
                break;
            default:
                leftX = 0;
                break;
        }
        int leftY;
        switch( (int)( ( _leftField.getStyle() & FIELD_VALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
            case (int)( FIELD_BOTTOM >> SYSTEM_STYLE_SHIFT ):
                leftY = actualHeight - _leftField.getHeight();
                break;
            case (int)( FIELD_VCENTER >> SYSTEM_STYLE_SHIFT ):
                leftY = ( actualHeight - _leftField.getHeight() ) / 2;
                break;
            default:
                leftY = 0;
                break;
        }
        setPositionChild( _leftField, leftX, leftY );
        
        int rightX;
        switch( (int)( ( _rightField.getStyle() & FIELD_HALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
            case (int)( FIELD_RIGHT >> SYSTEM_STYLE_SHIFT ):
                rightX = actualWidth - _rightField.getWidth();
                break;
            case (int)( FIELD_HCENTER >> SYSTEM_STYLE_SHIFT ):
                rightX = actualWidth - actualRightColumnWidth  + ( actualRightColumnWidth - _rightField.getWidth() ) / 2;
                break;
            default:
                rightX = actualWidth - actualRightColumnWidth;
                break;
        }
        int rightY;
        switch( (int)( ( _rightField.getStyle() & FIELD_VALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
            case (int)( FIELD_BOTTOM >> SYSTEM_STYLE_SHIFT ):
                rightY = actualHeight - _rightField.getHeight();
                break;
            case (int)( FIELD_VCENTER >> SYSTEM_STYLE_SHIFT ):
                rightY = ( actualHeight - _rightField.getHeight() ) / 2;
                break;
            default:
                rightY = 0;
                break;
        }
        setPositionChild( _rightField, rightX, rightY );

        setExtent( actualWidth, actualHeight );
    }
    
}
