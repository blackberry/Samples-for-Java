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

public class BitmapGaugeField extends Field
{
    private Bitmap _imageBackground;
    private Bitmap _imageProgress;
    
    private Bitmap _imageCenterBackground;
    private Bitmap _imageCenterProgress;

    private int _leadingBackgroundClip;
    private int _trailingBackgroundClip;
    private int _leadingProgressClip;
    private int _trailingProgressClip;
                
    private int _rop;
    
    private int _numValues;
    private int _currentValue;
    
    private boolean _selected;
    private boolean _horizontal;
    
    private int _width;
    private int _height;
    
    /**
     * Bitmap status - The image that is used as the state indicator, it grows to fill the field
     * Bitmap gaugeBackground - the background image is stretched to fit
     * int numStates - the number of increments. NOTE: this DOES NOT include zero position (will be one larger than you say)
     * int initialState - NOTE: this INCLUDES ZERO POSITION.
     *
     * pre: NumStates must be greater or equal to initialState
     * pre: NumStates must be greater than 1
     */
    public BitmapGaugeField( Bitmap background
                , Bitmap progress
                , int numValues
                , int initialValue
                , int leadingBackgroundClip
                , int trailingBackgroundClip
                , int leadingProgressClip
                , int trailingProgressClip
                , boolean horizontal )
    {
        super( NON_FOCUSABLE );
        
        if( numValues <  1 ) {
            throw( new IllegalArgumentException() );
        } 
        
        if( leadingBackgroundClip < 0 || trailingBackgroundClip < 0
         || leadingProgressClip < 0 || trailingProgressClip < 0 ) {
            throw( new IllegalArgumentException() );
        }
        
        _imageBackground = background;
        _imageProgress   = progress;
        
        _horizontal      = horizontal;   
        _numValues       = numValues;  
        _currentValue    = initialValue;
        
        _leadingBackgroundClip  = leadingBackgroundClip;
        _trailingBackgroundClip = trailingBackgroundClip;
        _leadingProgressClip    = leadingProgressClip;
        _trailingProgressClip   = trailingProgressClip;
        
        _rop = _imageBackground.hasAlpha() ? Graphics.ROP_SRC_ALPHA : Graphics.ROP_SRC_COPY;
        
        setValue( MathUtilities.clamp( 0, initialValue, _numValues ) );

        
        if( _horizontal ) {
            _imageCenterBackground = new Bitmap( _imageBackground.getWidth() - _leadingBackgroundClip - _trailingBackgroundClip , _imageBackground.getHeight() );  
        } else {
            _imageCenterBackground = new Bitmap( _imageBackground.getWidth(), _imageBackground.getHeight() - _leadingBackgroundClip - _trailingBackgroundClip );  
        }
        makeBitmapTransparent( _imageCenterBackground );
        initTile( _imageBackground, leadingBackgroundClip, trailingBackgroundClip, _imageCenterBackground );
        
        if( _horizontal ) {
            _imageCenterProgress = new Bitmap( _imageProgress.getWidth() - _leadingProgressClip - _trailingProgressClip , _imageProgress.getHeight() );  
        } else {
            _imageCenterProgress = new Bitmap( _imageProgress.getWidth(), _imageProgress.getHeight() - _leadingProgressClip - _trailingProgressClip );  
        }
        makeBitmapTransparent( _imageCenterProgress );
        initTile( _imageProgress, leadingProgressClip, trailingProgressClip, _imageCenterProgress );
    }
    
    private void makeBitmapTransparent( Bitmap bitmap ) 
    {
        Graphics g = new Graphics( bitmap );
        g.setGlobalAlpha( 0x00 );
        g.clear();
    }

    /**
     * Change the state of the gauge. Zero is far left.
     * @param newState - the switch will be set to the specified state
     */
    public void setValue(int newValue)
    {
        if( newValue > _numValues ){
            throw new IllegalArgumentException();
        } else {
            _currentValue = _horizontal ? newValue : _numValues - newValue;
            invalidate();
        }
    }

    /**
     * @return The current value that is selected. State zero is on the far left/bottom, and increase to the right/top.
     */
    public int getValue()
    {
        return _horizontal ? _currentValue : _numValues - _currentValue;
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
        return _horizontal ? Display.getWidth() : Math.max( _imageBackground.getWidth(), _imageProgress.getWidth() );
    }

    public int getPreferredHeight() 
    {
        return _horizontal ? Math.max( _imageBackground.getHeight(), _imageProgress.getHeight() ) : Display.getHeight();
    }

    protected void layout( int width, int height ) 
    {
        _width  = Math.min( getPreferredWidth(),  width  );
        _height = Math.min( getPreferredHeight(), height );

        setExtent( _width, _height );
    }
    
    protected boolean navigationMovement(int dx, int dy, int status, int time) 
    {
    	if( _selected ) {
            if( _horizontal && dx > 0 || !_horizontal && dy < 0) {
                incrementState();
                fieldChangeNotify( 0 );
                return true;
            } else if( _horizontal && dx < 0 || !_horizontal && dy > 0) {
                decrementState();
                fieldChangeNotify( 0 );
                return true;
            }
        }
		return super.navigationMovement( dx, dy, status, time );
    }

    public void paint( Graphics g )
    {       
        if( _horizontal ) { 
            drawHorizontalGauge( g );
        } else {
            drawVerticalGauge( g );
        }
    }
    
    private void drawHorizontalGauge( Graphics g )
    {
        int progressWidth = (int) ((float) (_width - _leadingProgressClip - _trailingProgressClip) / _numValues * _currentValue) + _leadingProgressClip + _trailingProgressClip;
        
        drawHorizontalPill( g, _imageBackground, _imageCenterBackground, _leadingBackgroundClip, _trailingBackgroundClip, _width );
        drawHorizontalPill( g, _imageProgress, _imageCenterProgress, _leadingProgressClip, _trailingProgressClip, progressWidth );
    }
    
    private void drawHorizontalPill( Graphics g, Bitmap baseImage, Bitmap centerTile, int clipLeft, int clipRight, int width )
    {
        int yPosition = ( _height - baseImage.getHeight() ) >> 1;
        width = Math.max( width, clipLeft + clipRight );
        
        // Left
        g.drawBitmap( 0, yPosition, clipLeft, baseImage.getHeight(), baseImage, 0, 0);
        
        // Middle
        g.tileRop( _rop, clipLeft, yPosition, Math.max( 0, width - clipLeft - clipRight ), centerTile.getHeight(), centerTile, 0, 0);
        
        // Right
        g.drawBitmap( width - clipRight, yPosition, clipRight, baseImage.getHeight(), baseImage, baseImage.getWidth() - clipRight, 0);
    }
    
    private void drawVerticalGauge( Graphics g )
    {
        // TODO: The same for the vertical as we did for horizontal
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
        if( isEditable() ) {
            _selected = !_selected;
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * Cuts up the background image using the specified margins
     * to create the center portion to be tiled
     */
    private void initTile( Bitmap baseImage, int leadingMargin, int trailingMargin, Bitmap targetImage )
    {
        int height = baseImage.getHeight();
        int width  = baseImage.getWidth();
        
        int marginSize = leadingMargin + trailingMargin;
                                                  
        if( _horizontal ) {
            copy( baseImage, leadingMargin, 0, width - marginSize, height, targetImage );
        } else {
            copy( baseImage, 0, leadingMargin, width, height - marginSize, targetImage );
        }
    }

    private void copy(Bitmap src, int x, int y, int width, int height, Bitmap dest) 
    {
        int[] argbData = new int[width * height];
        src.getARGB(argbData, 0, width, x, y, width, height);
        for(int tx = 0; tx < dest.getWidth(); tx += width) {
            for(int ty = 0; ty < dest.getHeight(); ty += height) {
                dest.setARGB(argbData, 0, width, tx, ty, width, height);
            }
        }
    }
}
