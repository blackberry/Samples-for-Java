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


public class JustifiedVerticalFieldManager extends Manager
{
    public Field _topField;
    public Field _bottomField;
    
    private boolean _giveTopFieldPriority;
    
    public JustifiedVerticalFieldManager( Field topField, Field bottomField, boolean giveTopFieldPriority )
    {
        this( topField, bottomField, giveTopFieldPriority, Field.USE_ALL_HEIGHT );
    }

    public JustifiedVerticalFieldManager( Field topField, Field bottomField, boolean giveTopFieldPriority, long style )
    {
        super( style );
        
        _topField = topField;
        _bottomField = bottomField;
        
        add( _topField );
        add( _bottomField );
        
        _giveTopFieldPriority = giveTopFieldPriority;
    }
    
    protected void sublayout( int width, int height )
    {
        Field firstField;
        Field secondField;
        if( _giveTopFieldPriority ) {
            firstField = _topField;
            secondField = _bottomField;
        } else {
            firstField = _bottomField;
            secondField = _topField;
        }

        int maxWidth = 0;
        
        int availableHeight = height;
        availableHeight -= _topField.getMarginTop();
        availableHeight -= Math.max( _topField.getMarginBottom(), _bottomField.getMarginTop() );
        availableHeight -= _bottomField.getMarginBottom();

        layoutChild( firstField, width - firstField.getMarginLeft() - firstField.getMarginRight(), availableHeight );
        maxWidth = Math.max( maxWidth, firstField.getMarginLeft() + firstField.getWidth() + firstField.getMarginRight() );
        availableHeight -= firstField.getHeight();
        
        layoutChild( secondField, width - secondField.getMarginLeft() - secondField.getMarginRight(), availableHeight );
        maxWidth = Math.max( maxWidth, secondField.getMarginLeft() + secondField.getWidth() + secondField.getMarginRight() );
        availableHeight -= secondField.getHeight();
        
        if( !isStyle( Field.USE_ALL_HEIGHT ) ) {
            height -= availableHeight;
        }
        if( !isStyle( Field.USE_ALL_WIDTH ) ) {
            width = maxWidth;
        }
        
        setPositionChild( _topField, getFieldX( _topField, width ), _topField.getMarginTop() );
        setPositionChild( _bottomField, getFieldX( _bottomField, width ), height - _bottomField.getHeight() - _bottomField.getMarginBottom() );

        setExtent( width, height );
    }


    private int getFieldX( Field field, int width )
    {
        if( field.isStyle( Field.FIELD_RIGHT ) ) {
            return width - field.getWidth() - field.getMarginRight();
        } else if( field.isStyle( Field.FIELD_RIGHT ) ) {
            return field.getMarginLeft() + ( width - field.getMarginLeft() - field.getWidth() - field.getMarginRight() ) / 2;
        } else {
            return field.getMarginLeft();
        }
    }
    
    public Field getTopField()
    {
        return _topField;
    }
    
    public Field getBottomField()
    {
        return _bottomField;
    }
    
    public void replace( Field oldField, Field newField )
    {
        if( oldField == newField ) {
            // Nothing to do
            return;
        }
        
        if( oldField == _topField ) {
            _topField = newField;
        } else if( oldField == _bottomField ) {
            _bottomField = newField;
        }
        add( newField );
        delete( oldField );
    }
}    
