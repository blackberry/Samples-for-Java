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


public class JustifiedHorizontalFieldManager extends Manager
{
	private static final int SYSTEM_STYLE_SHIFT = 32;
	
    public Field _leftField;
    public Field _rightField;
    
    private boolean _giveLeftFieldPriority;
    
    public JustifiedHorizontalFieldManager( Field leftField, Field rightField, boolean giveLeftFieldPriority )
    {
        this( leftField, rightField, giveLeftFieldPriority, Field.USE_ALL_WIDTH );
    }

    public JustifiedHorizontalFieldManager( Field leftField, Field rightField, boolean giveLeftFieldPriority, long style )
    {
        super( style );
        
        _leftField = leftField;
        _rightField = rightField;
        
        add( _leftField );
        add( _rightField );
        
        _giveLeftFieldPriority = giveLeftFieldPriority;
    }
    
    public JustifiedHorizontalFieldManager( boolean giveLeftFieldPriority, long style )
    {
        super( style );
        _giveLeftFieldPriority = giveLeftFieldPriority;
    }
    
    public void addLeftField( Field field )
    {
        if( _leftField != null ) {
            throw new IllegalStateException();
        }
        _leftField = field;
        add( _leftField );
    }
    
    public void addRightField( Field field )
    {
        if( _rightField != null ) {
            throw new IllegalStateException();
        }
        _rightField = field;
        add( _rightField );
    }
    
    public int getPreferredWidth()
    {
        return _leftField.getPreferredWidth() + _rightField.getPreferredWidth();
    }
    
    public int getPreferredHeight()
    {
        return Math.max( _leftField.getPreferredHeight(), _rightField.getPreferredHeight() );
    }
    
    protected void sublayout( int width, int height )
    {
        Field firstField;
        Field secondField;
        if( _giveLeftFieldPriority ) {
            firstField = _leftField;
            secondField = _rightField;
        } else {
            firstField = _rightField;
            secondField = _leftField;
        }

        int maxHeight = 0;

        int availableWidth = width;
        availableWidth -= _leftField.getMarginLeft();
        availableWidth -= Math.max( _leftField.getMarginRight(), _rightField.getMarginLeft() );
        availableWidth -= _rightField.getMarginRight();

        layoutChild( firstField, availableWidth, height - firstField.getMarginTop() - firstField.getMarginBottom() );
        maxHeight = Math.max( maxHeight, firstField.getMarginTop() + firstField.getHeight() + firstField.getMarginBottom() );
        availableWidth -= firstField.getWidth();
        
        layoutChild( secondField, availableWidth, height - secondField.getMarginTop() - secondField.getMarginBottom() );
        maxHeight = Math.max( maxHeight, secondField.getMarginTop() + secondField.getHeight() + secondField.getMarginBottom() );
        availableWidth -= secondField.getWidth();

        if( !isStyle( Field.USE_ALL_HEIGHT ) ) {
            height = maxHeight;
        }
        if( !isStyle( Field.USE_ALL_WIDTH ) ) {
            width -= availableWidth;
        }

        setPositionChild( _leftField, _leftField.getMarginLeft(), getFieldY( _leftField, height ) );
        setPositionChild( _rightField, width - _rightField.getWidth() - _rightField.getMarginRight(), getFieldY( _rightField, height ) );
   
        setExtent( width, height );
    }

    private int getFieldY( Field field, int height )
    {
        switch( (int)( ( field.getStyle() & FIELD_VALIGN_MASK ) >> SYSTEM_STYLE_SHIFT ) ) {
            case (int)( FIELD_BOTTOM >> SYSTEM_STYLE_SHIFT ):
                return height - field.getHeight() - field.getMarginBottom();
            case (int)( FIELD_VCENTER >> SYSTEM_STYLE_SHIFT ):
                return field.getMarginTop() + ( height - field.getMarginTop() - field.getHeight() - field.getMarginBottom() ) / 2;
            default:
                return field.getMarginTop();
        }
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
        if( oldField == newField ) {
            // Nothing to do
            return;
        }
        
        if( oldField == _leftField ) {
            _leftField = newField;
        } else if( oldField == _rightField ) {
            _rightField = newField;
        }
        add( newField );
        delete( oldField );
    }

    
    
}    
