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


package com.samples.toolkit.ui.component;

import com.samples.toolkit.ui.container.PillButtonSet;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;

/**
 * A custom button field
 */
public class PillButtonField extends BaseButtonField
{
    private static final int CORNER_RADIUS = 18;
    
    public static int DRAWPOSITION_LEFT = 0;
    public static int DRAWPOSITION_RIGHT = 1;
    public static int DRAWPOSITION_MIDDLE = 2;
    public static int DRAWPOSITION_SINGLE = 3;
    
    public static final int COLOUR_BORDER              = 0x212121;
    public static final int COLOUR_BORDER_FOCUS        = 0x212121;
    public static final int COLOUR_BORDER_SELECTED     = 0x212121;
    public static final int COLOUR_TEXT                = 0xD6D6D6;
    public static final int COLOUR_TEXT_FOCUS          = 0xFFFFFF;
    public static final int COLOUR_TEXT_SELECTED       = 0xFFFFFF;
    public static final int COLOUR_BACKGROUND          = 0x727272;
    public static final int COLOUR_BACKGROUND_FOCUS    = 0x125DDE;
    public static final int COLOUR_BACKGROUND_SELECTED = 0x32427E;

    private static final int XPADDING = Display.getWidth() <= 320 ? 6 : 8;
    private static final int YPADDING = Display.getWidth() <= 320 ? 5 : 7;
    
    private String _text;
    private Font _buttonFont;
    private boolean _pressed = false;
    private boolean _selected = false;
    
    private int _width;
    private int _height;
    
    private int _drawPosition = -1;
    
    public PillButtonField( String text )
    {
        super( Field.FOCUSABLE );
        _text = text;
    }
    
    /**
     * DRAWPOSITION_LEFT | DRAWPOSITION_RIGHT | DRAWPOSITION_MIDDLE | DRAWPOSITION_SINGLE
     * Determins how the field is drawn (border corners)
     */
    public void setDrawPosition( int drawPosition )
    {
        _drawPosition = drawPosition;
    }
    
    public void setSelected( boolean selected )
    {
        _selected = selected;
        invalidate();
    }
    
    public int getPreferredWidth()
    {
        return 2 * XPADDING + _buttonFont.getAdvance( _text );   // not actually used
    }

    public int getPreferredHeight()
    {
        return 2 * YPADDING + _buttonFont.getHeight();
    }
        
    protected void layout( int width, int height )
    {
        _buttonFont = getFont();
        setExtent( width, getPreferredHeight() );
        _width = getWidth();
        _height = getHeight();
    }
    
    protected void onUnfocus()
    {
        super.onUnfocus();
        if( _pressed ) {
            _pressed = false;
            invalidate();
        }
    }
    
    /**
     * A public way to click this button
     */
    public void clickButton() 
    {
        Manager manager = getManager();
        if( manager instanceof PillButtonSet ) {
            ( ( PillButtonSet ) manager ).setSelectedField( this );
        }
        super.clickButton();
    }
    
    protected boolean navigationClick(int status, int time) {
        _pressed = true;
        invalidate();
        return super.navigationClick( status, time );
    }
    
    protected boolean navigationUnclick(int status, int time) {
        _pressed = false;
        invalidate();
        return true;
    }
    
    protected void paint( Graphics g )
    {
        int oldColour = g.getColor();
        try {
            int foregroundColor;
            if ( _pressed || _selected  ) {
                foregroundColor = COLOUR_TEXT_SELECTED;
            } else if( g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ) {
                foregroundColor = COLOUR_TEXT_FOCUS;
            } else {
                foregroundColor = COLOUR_TEXT;
            }
            
            g.setColor( foregroundColor );
            g.drawText( _text, 0, YPADDING, DrawStyle.HCENTER, _width ); 
        } finally {
            g.setColor( oldColour );
        }
    }
    
    protected void paintBackground( Graphics g )
    {
        int oldColour = g.getColor();
        
        int backgroundColor;
        int borderColor;
        
        if( _pressed ) {
            backgroundColor = COLOUR_BACKGROUND_SELECTED;
            borderColor = COLOUR_BORDER_SELECTED;
        } else if ( g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ) {
            backgroundColor = COLOUR_BACKGROUND_FOCUS ;
            borderColor = COLOUR_BORDER_FOCUS;
        }else if( _selected ) {
            backgroundColor = COLOUR_BACKGROUND_SELECTED;
            borderColor = COLOUR_BORDER_SELECTED;
        } else {
            backgroundColor = COLOUR_BACKGROUND;
            borderColor = COLOUR_BORDER;
        }
        
        try {
            if( _drawPosition == 0 ) {
                // Left
                drawButtonBackground( g, 0, 0, getWidth() + CORNER_RADIUS, getHeight(), backgroundColor, borderColor );
                drawSeparator( g, 0, 0, getWidth(), getHeight(), false, true );
            } else if( _drawPosition == 1 ) {
                // Right 
                drawButtonBackground( g, -CORNER_RADIUS, 0, getWidth() + CORNER_RADIUS, getHeight(), backgroundColor, borderColor );
                drawSeparator( g, 0, 0, getWidth(), getHeight(), true, false );
            } else if( _drawPosition == 2 ) {
                // Middle
                drawButtonBackground( g, -CORNER_RADIUS, 0, getWidth() + 2 * CORNER_RADIUS, getHeight(), backgroundColor, borderColor );
                drawSeparator( g, 0, 0, getWidth(), getHeight(), true, true );
            } else {
                // Single
                drawButtonBackground( g, 0, 0, getWidth(), getHeight(), backgroundColor, borderColor );
            }
            
        } finally {
            g.setColor( oldColour );
        }
    }
    
    private void drawButtonBackground( Graphics g, int x, int y, int width, int height, int backgroundColor, int borderColor )
    {
        int oldAlpha = g.getGlobalAlpha();
        try {
            g.setColor( backgroundColor );
            g.fillRoundRect( x, y, width, height, CORNER_RADIUS, CORNER_RADIUS );
            
            g.setGlobalAlpha( 0x44 );
            g.setColor( 0xFFFFFF ); // White highlight
            g.drawRoundRect( x, _selected ? y : y + 1, width, _height - 1, CORNER_RADIUS, CORNER_RADIUS );
            g.setColor( 0x000000 ); // Black lowlight
            g.drawRoundRect( x, _selected ? y + 1: y, width, height - 1, CORNER_RADIUS, CORNER_RADIUS );
            
            g.setGlobalAlpha( 0xFF );
            g.setColor( borderColor );
            g.drawRoundRect( x, y, width, height, CORNER_RADIUS, CORNER_RADIUS );
        } finally {
            g.setGlobalAlpha( oldAlpha );
        }
    }
    
    private void drawSeparator( Graphics g, int x, int y, int width, int height, boolean left, boolean right )
    {
        int oldAlpha = g.getGlobalAlpha();
        try {
            width--;
            g.setGlobalAlpha( 0x33 );
            if( left ) {
                g.setColor( 0x000000 ); // Black lowlight
                g.drawLine( 0, y, 0, y + height );
            } 
            if( right ){
                g.setColor( 0xFFFFFF ); // White highlight
                g.drawLine( width, y, width, y + height );
            }
        } finally {
            g.setGlobalAlpha( oldAlpha );
        }
    }
}

