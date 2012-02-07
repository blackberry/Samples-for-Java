//#preprocessor

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

import java.lang.Math;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.util.*;

public class SliderField extends Field
{
    private final static int STATE_NORMAL  = 0;
    private final static int STATE_FOCUSED = 1;
    private final static int STATE_PRESSED = 2;
    
    private final static int NUM_STATES    = 3;
    

    private Bitmap[] _thumb        = new Bitmap[ NUM_STATES ];
    private Bitmap[] _progress     = new Bitmap[ NUM_STATES ];
    private Bitmap[] _base         = new Bitmap[ NUM_STATES ];
    private Bitmap[] _progressTile = new Bitmap[ NUM_STATES ];
    private Bitmap[] _baseTile     = new Bitmap[ NUM_STATES ];

    private int _thumbWidth;
    private int _thumbHeight;
    private int _progressWidth;
    private int _progressHeight;
    private int _baseWidth;
    private int _baseHeight;
    
    private int _leftCapWidth;
    private int _rightCapWidth;
    

    private boolean _focused;
    private boolean _pressed;

    private int _numValues;
    private int _currentValue;

    private int _preferredHeight;

    // These values can be cached in layout() for use in paint()
    private int _baseY;
    private int _progressY;
    private int _thumbY;
    private int _trackWidth;
    
    // Used to tile the base and progress bitmaps
    private int _rop; 

    
    public SliderField( Bitmap normalThumb,  Bitmap normalProgress,  Bitmap normalBase,
                        Bitmap focusedThumb, Bitmap focusedProgress, Bitmap focusedBase,
                        int numValues, int initialValue, int leftCapWidth, int rightCapWidth )
    {
        this( normalThumb,  normalProgress,  normalBase, 
              focusedThumb, focusedProgress, focusedBase, 
              null,         null,            null, 
              numValues, initialValue, leftCapWidth, rightCapWidth, 0 );
    }
    
    public SliderField( Bitmap normalThumb,  Bitmap normalProgress,  Bitmap normalBase,
                        Bitmap focusedThumb, Bitmap focusedProgress, Bitmap focusedBase,
                        int numValues, int initialValue, int leftCapWidth, int rightCapWidth, long style )
    {
        this( normalThumb,  normalProgress,  normalBase, 
              focusedThumb, focusedProgress, focusedBase, 
              null,         null,            null, 
              numValues, initialValue, leftCapWidth, rightCapWidth, style );
    }
    
    public SliderField( Bitmap normalThumb,  Bitmap normalProgress,  Bitmap normalBase,
                        Bitmap focusedThumb, Bitmap focusedProgress, Bitmap focusedBase,
                        Bitmap pressedThumb, Bitmap pressedProgress, Bitmap pressedBase,
                        int numValues, int initialValue, int leftCapWidth, int rightCapWidth )
    {
        this( normalThumb,  normalProgress,  normalBase, 
              focusedThumb, focusedProgress, focusedBase, 
              pressedThumb, pressedProgress, pressedBase, 
              numValues, initialValue, leftCapWidth, rightCapWidth, 0 );
    }
    
    public SliderField( Bitmap normalThumb,  Bitmap normalProgress,  Bitmap normalBase,
                        Bitmap focusedThumb, Bitmap focusedProgress, Bitmap focusedBase,
                        Bitmap pressedThumb, Bitmap pressedProgress, Bitmap pressedBase,
                        int numValues, int initialValue, int leftCapWidth, int rightCapWidth, long style )
    {
        super( style );

        if( numValues < 2 || initialValue >= numValues ) {
            throw new IllegalArgumentException( "invalid value parameters" );
        }
        if( normalThumb == null  || normalProgress == null  || normalBase == null 
         || focusedThumb == null || focusedProgress == null || focusedBase == null ) {
            throw new IllegalArgumentException( "thumb, normal, focused  are required" );
        }
        
        _thumbWidth = normalThumb.getWidth();
        _thumbHeight = normalThumb.getHeight();
        _progressWidth = normalProgress.getWidth();
        _progressHeight = normalProgress.getHeight();
        _baseWidth = normalBase.getWidth();
        _baseHeight = normalBase.getHeight();
        
        if( focusedThumb.getWidth() != _thumbWidth || focusedThumb.getHeight() != _thumbHeight
         || focusedProgress.getHeight() != _progressHeight || focusedBase.getHeight() != _baseHeight ) {
            throw new IllegalArgumentException( "all base bitmaps and all progress bitmaps must be the same height" );
        }
        if( pressedThumb != null && pressedProgress != null && pressedBase != null ) {
            if( pressedThumb.getWidth() != _thumbWidth || pressedThumb.getHeight() != _thumbHeight
             || pressedProgress.getHeight() != _progressHeight || pressedBase.getHeight() != _baseHeight ) {
                throw new IllegalArgumentException( "all base bitmaps and all progress bitmaps must be the same height" );
            }
        }
        
        _leftCapWidth = leftCapWidth;
        _rightCapWidth = rightCapWidth;

        _rop = Graphics.ROP_SRC_COPY;
        
        initBitmaps( normalThumb,  normalProgress,  normalBase,  STATE_NORMAL  );
        initBitmaps( focusedThumb, focusedProgress, focusedBase, STATE_FOCUSED );
        
        if( pressedThumb != null && pressedProgress != null && pressedBase != null ) {
            initBitmaps( pressedThumb, pressedProgress, pressedBase, STATE_PRESSED );
        } else {
            // The pressed images are optional -- if they aren't provided, just use the focused images
            _thumb[ STATE_PRESSED ]        = _thumb[ STATE_FOCUSED ];
            _progress[ STATE_PRESSED ]     = _progress[ STATE_FOCUSED ];
            _base[ STATE_PRESSED ]         = _base[ STATE_FOCUSED ];
            _progressTile[ STATE_PRESSED ] = _progressTile[ STATE_FOCUSED ];
            _baseTile[ STATE_PRESSED ]     = _baseTile[ STATE_FOCUSED ];
        }
        
        _preferredHeight = Math.max( _thumbHeight, Math.max( _progressHeight, _baseHeight ) );

        _numValues = numValues;
        setValue( initialValue );
    }
                    
    /**
     * Cuts up the background image and margins you provide
     * to make the left, center, and right bitmaps
     */
    public void initBitmaps( Bitmap thumb, Bitmap progress, Bitmap base, int state )
    {
        if( progress.getWidth() <= _leftCapWidth
         || base.getWidth() <= _rightCapWidth ) {
            throw new IllegalArgumentException();
        }
 
         if( thumb.hasAlpha() || progress.hasAlpha() || base.hasAlpha() ) {
             // If any one of our images has an alpha channel we need to use ROP_SRC_ALPHA instead of ROP_SRC_COPY
             _rop = Graphics.ROP_SRC_ALPHA;
         }
       
        _thumb[ state ] = thumb;
        _progress[ state ] = progress;
        _base[ state ] = base;

        int[] argbCopyBuffer;

        // Create the progress tile (i.e. to the left of the thumb)
        int progressTileWidth = progress.getWidth() - _leftCapWidth;
        int progressTileHeight = progress.getHeight();

        Bitmap progressTile = new Bitmap( progressTileWidth, progressTileHeight );

        argbCopyBuffer = new int[ progressTileWidth * progressTileHeight ];
        progress.getARGB( argbCopyBuffer, 0, progressTileWidth, _leftCapWidth, 0, progressTileWidth, progressTileHeight );
        progressTile.setARGB( argbCopyBuffer, 0, progressTileWidth, 0, 0, progressTileWidth, progressTileHeight );

        // Create the base tile (i.e. to the right of the thumb)
        int baseTileWidth = base.getWidth() - _rightCapWidth;
        int baseTileHeight = base.getHeight();

        Bitmap baseTile = new Bitmap( baseTileWidth, baseTileHeight );

        argbCopyBuffer = new int[ baseTileWidth * baseTileHeight ];
        base.getARGB( argbCopyBuffer, 0, baseTileWidth, 0, 0, baseTileWidth, baseTileHeight );
        baseTile.setARGB( argbCopyBuffer, 0, baseTileWidth, 0, 0, baseTileWidth, baseTileHeight );

        _progressTile[ state ] = progressTile;
        _baseTile[ state ] = baseTile;
    }

    public void setValue( int newValue ) 
    {
        if( newValue < 0 || newValue >= _numValues ){
            throw new IllegalArgumentException();
        }
        _currentValue = newValue;
        fieldChangeNotify( FieldChangeListener.PROGRAMMATIC );
        invalidate();
    }

    public int getValue() 
    {
        return _currentValue;
    }

    public int getNumValues() 
    {
        return _numValues;
    }
    
    public int getPreferredWidth() 
    {
        return Integer.MAX_VALUE;
    }

    public int getPreferredHeight() 
    {
        return _preferredHeight;
    }

    protected void layout( int width, int height ) 
    {
        width = Math.min( width, getPreferredWidth() );
        height = Math.min( height, getPreferredHeight() );

        // Cache some values for paint() and input handling
        _progressY = ( height - _progressHeight ) / 2;
        _baseY = ( height - _baseHeight ) / 2;
        _thumbY = ( height - _thumbHeight ) / 2;
        
        // The thumb should ride along the track from one end cap to the other but not cover up either one
        _trackWidth = width - _leftCapWidth - _rightCapWidth;
        
        setExtent( width, height );
    }
    

public void paint( Graphics g )
{
    int contentWidth = getContentWidth();

    int thumbX = _leftCapWidth + ( _trackWidth - _thumbWidth ) * _currentValue / ( _numValues - 1 );
    int transitionX = thumbX + _thumbWidth / 2;
    
    int currentState = _pressed ? STATE_PRESSED : ( _focused ? STATE_FOCUSED : STATE_NORMAL );
    
    Bitmap thumb        = _thumb[ currentState ];
    Bitmap progress     = _progress[ currentState ];
    Bitmap base         = _base[ currentState ];
    Bitmap progressTile = _progressTile[ currentState ];
    Bitmap baseTile     = _baseTile[ currentState ];

    // Draw the left cap, and tile the progress region
    g.drawBitmap( 0, _progressY, _leftCapWidth, _progressHeight, progress, 0, 0 );
    g.tileRop( _rop, _leftCapWidth, _progressY, transitionX - _leftCapWidth, _progressHeight, progressTile, 0, 0 );
    
    // Draw the right cap, and tile the base region
    g.drawBitmap( contentWidth - _rightCapWidth, _baseY, _rightCapWidth, _baseHeight, base, _baseWidth - _rightCapWidth, 0 );
    g.tileRop( _rop, transitionX, _baseY, contentWidth - transitionX - _rightCapWidth, _baseHeight, baseTile, 0, 0 );
    
    // Draw the thumb
    g.drawBitmap( thumbX, _thumbY, _thumbWidth, _thumbHeight, thumb, 0, 0 );
}

protected void drawFocus( Graphics g, boolean on )
    {
        // The paint() method looks after drawing the focus for us
    }
    
    protected void onFocus( int direction )
    {
        _focused = true;
        invalidate();
        super.onFocus( direction );
    }
    
    protected void onUnfocus()
    {
        _focused = false;
        invalidate();
        super.onUnfocus();
    }

//#ifndef VER_4.6.1 | VER_4.6.0 | VER_4.5.0 | VER_4.2.1 | VER_4.2.0
       
    protected boolean touchEvent( TouchEvent message )
    {
        int event = message.getEvent();
        switch( event ) {
            
            case TouchEvent.CLICK:
            case TouchEvent.DOWN:
                // If we currently have the focus, we still get told about a click in a different part of the screen
                if( touchEventOutOfBounds( message ) ) {
                    return false;
                }
                // fall through
                
            case TouchEvent.MOVE:
                _pressed = true;
                setValueByTouchPosition( message.getX( 1 ) );
                fieldChangeNotify( 0 );
                return true;
                
            case TouchEvent.UNCLICK:
            case TouchEvent.UP:
                _pressed = false;
                invalidate();
                return true;
                
            default:
                return false;
        }
    }
    
    private boolean touchEventOutOfBounds( TouchEvent message )
    {
        int x = message.getX( 1 );
        int y = message.getY( 1 );
        return ( x < 0 || y < 0 || x > getWidth() || y > getHeight() );
    }
        
    private void setValueByTouchPosition( int x )
    {
        _currentValue = MathUtilities.clamp( 0, ( x - _leftCapWidth ) * _numValues / _trackWidth, _numValues - 1 );
        invalidate();
    }
        

//#endif

    protected boolean navigationMovement( int dx, int dy, int status, int time ) 
    {
        if( _pressed ) {
            if( dx > 0 || dy > 0 ) {
                incrementValue();
            } else {
                decrementValue();
            }
            fieldChangeNotify( 0 );
            return true;
        }
        return super.navigationMovement( dx, dy, status, time);
    }

    private void incrementValue() 
    {
        if( _currentValue + 1 < _numValues ) {
            _currentValue++;
            invalidate();
        }
    }

    private void decrementValue() 
    {
        if( _currentValue > 0 ) {
            _currentValue--;
            invalidate();
        }
    }

    protected boolean invokeAction( int action ) 
    {
        if( action == ACTION_INVOKE ) {
            togglePressed();
            return true;
        }
        return false;
    }

    protected boolean keyChar( char key, int status, int time ) 
    {
        if( key == Characters.SPACE || key == Characters.ENTER ) {
            togglePressed();
            return true;
        }
        return false;
    }

    protected boolean trackwheelClick( int status, int time ) 
    {
        togglePressed();
        return true;
    }

    private void togglePressed() 
    {
        _pressed = !_pressed;
        invalidate();
    }

}
