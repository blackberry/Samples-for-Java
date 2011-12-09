//#preprocess

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

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.util.*;

public class RatingField extends Field
{
    private Bitmap _ratingGlyph;
    private Bitmap _baseGlyph;
    private Bitmap _ratingGlyphFocus;
    private Bitmap _baseGlyphFocus;

    private Bitmap _ratingGlyphToDraw;
    private Bitmap _baseGlyphToDraw;
    
    private int _rop;
    
    private int _numValues;
    private int _currentValue;
    
    private boolean _selected;
    
    private int _width;
    private int _height;
    
    private int _maxGlyphWidth;
    private int _maxGlyphHeight;
    
    private boolean _isTouchscreen;
    
    public RatingField( 
                  Bitmap ratingGlyph
                , Bitmap baseGlyph
                , Bitmap ratingGlyphFocus
                , Bitmap baseGlyphFocus
                , int numValues
                , int initialValue
                , long style )
    {
        super( Field.FOCUSABLE | Field.EDITABLE | style );
        
        if( numValues <  1 || ratingGlyph == null || baseGlyph == null || ratingGlyphFocus == null || baseGlyphFocus == null ) {
            throw( new IllegalArgumentException() );
        } 
        
        _ratingGlyph      = ratingGlyph;
        _baseGlyph        = baseGlyph;
        _ratingGlyphFocus = ratingGlyphFocus;
        _baseGlyphFocus   = baseGlyphFocus;
        
        _numValues       = numValues;  
        _currentValue    = initialValue;
        
//#ifndef VER_4.6.1 | VER_4.6.0 | VER_4.5.0 | VER_4.2.1 | VER_4.2.0
        _isTouchscreen = Touchscreen.isSupported();
//#endif        
        
        _rop = _ratingGlyph.hasAlpha() ? Graphics.ROP_SRC_ALPHA : Graphics.ROP_SRC_COPY;
        
        setValue( initialValue );
        
        _maxGlyphWidth = Math.max( Math.max( Math.max( _ratingGlyph.getWidth(), _baseGlyph.getWidth() ), _ratingGlyphFocus.getWidth() ), _baseGlyphFocus.getWidth() );
        _maxGlyphHeight = Math.max( Math.max( Math.max( _ratingGlyph.getHeight(), _baseGlyph.getHeight() ), _ratingGlyphFocus.getHeight() ), _baseGlyphFocus.getHeight() );
    }
    
    /**
     * Change the state of the gauge. Zero is far left.
     * @param newState - the switch will be set to the specified state
     */
    public void setValue(int newValue)
    {
        _currentValue =  MathUtilities.clamp( 0, newValue, _numValues );
        fieldChangeNotify( 0 );
        invalidate();
    }

    /**
     * @return The current value that is selected. State zero is on the far left/bottom, and increase to the right/top.
     */
    public int getValue()
    {
        return _currentValue;
    }

    /**
     * @return The number of states the gauge has
     */
    public int getNumValues()
    {
        return _numValues;
    }
    
    public int getPreferredWidth() 
    {
        return _maxGlyphWidth * _numValues;
    }

    public int getPreferredHeight() 
    {
        return _maxGlyphHeight;
    }

    protected void layout( int width, int height ) 
    {
        _width  = Math.min( getPreferredWidth(),  width  );
        _height = Math.min( getPreferredHeight(), height );

        setExtent( _width, _height );
    }
    
    protected boolean navigationMovement(int dx, int dy, int status, int time) 
    {
        if( _selected && dx != 0 )
        {
            if( dx > 0 ) {
                incrementState();
            } else if( dx < 0 ) {
                decrementState();
            }
            fieldChangeNotify( 0 );
            return true;
        }
        return super.navigationMovement( dx, dy, status, time);
    }

    public void paint( Graphics g )
    {       
        boolean focus = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS );
        _ratingGlyphToDraw = focus ? _ratingGlyphFocus : _ratingGlyph;
        _baseGlyphToDraw = focus ? _baseGlyphFocus : _baseGlyph;
        
        int i = 0;
        int curX = 0;
        for( ; i < _currentValue; i++ ) {
            g.drawBitmap( curX, 0, _ratingGlyphToDraw.getWidth(), _ratingGlyphToDraw.getHeight(), _ratingGlyphToDraw, 0, 0 );
            curX += _maxGlyphWidth;
        }
        
        for( ; i < _numValues; i++ ) {
            g.drawBitmap( curX, 0, _baseGlyphToDraw.getWidth(), _baseGlyphToDraw.getHeight(), _baseGlyphToDraw, 0, 0 );
            curX += _maxGlyphWidth;
        }
    }
    
    protected void paintBackground(Graphics graphics)
    {
        // Do nothing
    }

    protected void drawFocus( Graphics g, boolean on )
    {
        boolean oldDrawStyleFocus = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS );
        try {
            if( on ) {
                g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, true );
            }
            paintBackground( g );
            paint( g );
        } finally {
            g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus );
        }
    }

    public void decrementState()
    {
        int currentValue = getValue();
        
        if( currentValue > 0) {
            setValue( currentValue - 1 );
            invalidate();
        }
    }

    public void incrementState()
    {
        int currentValue = getValue();
        
        if( currentValue < _numValues ) {
            setValue( currentValue + 1 );
            invalidate();
        }
    }

    /**
     * Invokes an action on this field.  The only action that is recognized by this method is
     * {@link Field#ACTION_INVOKE}, which toggles this field from being checked and unchecked.
    */
    protected boolean invokeAction(int action)
    {
        switch(action) {
            case ACTION_INVOKE: {
                if( toggleSelected() ) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean keyChar( char key, int status, int time )
    {
        if( key == Characters.SPACE || key == Characters.ENTER ) {
            if( toggleSelected() ) {
                return true;
            }
        }
        return false;
    }

    protected boolean trackwheelClick( int status, int time )
    {
        if( toggleSelected() ) {
            return true;
        }
        return super.trackwheelClick(status, time);
    }
    
    private boolean toggleSelected() 
    {
        if( !_isTouchscreen && isEditable() ) {
            _selected = !_selected;
            invalidate();
            return true;
        }
        return false;
    }
    
//#ifndef VER_4.6.1 | VER_4.6.0 | VER_4.5.0 | VER_4.2.1 | VER_4.2.0
    protected boolean touchEvent(TouchEvent message)
    {
        boolean isConsumed = false;
        boolean isOutOfBounds = false;
        int x = message.getX(1);
        int y = message.getY(1);
        // Check to ensure point is within this field
        if(x < 0 || y < 0 || x > getExtent().width || y > getExtent().height) {
            return false;
        }
        switch(message.getEvent()) {
            case TouchEvent.DOWN:
            case TouchEvent.MOVE:
                if(isOutOfBounds) return true; // consume
                _selected = true; // Pressed effect
                
                // update state
                int numerator = x / _maxGlyphWidth;
                int denominator = x % _maxGlyphWidth;
                if( denominator > _maxGlyphWidth / 2 ) {
                    numerator++;
                }
                setValue( numerator );
                
                break;
            case TouchEvent.UP:
                _selected = false; 
                
                if(isOutOfBounds) {
                    break;
                }
                
                // Update state
                numerator = x / _maxGlyphWidth;
                denominator = x % _maxGlyphWidth;
                if( denominator > _maxGlyphWidth / 2 ) {
                    numerator++;
                }
                setValue( numerator );
                
                break;
        }
        return super.touchEvent( message );
    }
//#endif
}
