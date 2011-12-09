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


public class BitmapSliderField extends Field
{
    Bitmap _imageThumb;
    
    Bitmap _imageSlider;
    Bitmap _imageSliderLeft;
    Bitmap _imageSliderCenter;
    Bitmap _imageSliderRight;
    
    Bitmap _imageSliderFocus;
    Bitmap _imageSliderFocusLeft;
    Bitmap _imageSliderFocusCenter;
    Bitmap _imageSliderFocusRight;

    private int _numStates;
    private int _currentState;
    private boolean _selected;

    private int _xLeftBackMargin;
    private int _xRightBackMargin;
    
    private int _thumbWidth;
    private int _thumbHeight;
        
    private int _totalHeight;
    private int _totalWidth;
    
    private int _rop; // for tiling

    private static MenuItem _changeOptionsItem = new ChangeOptionMenuItem();

    private int _backgroundColours[];
    private int _backgroundSelectedColours[];

    private int _defaultSelectColour = 0x977DED;
    private int _defaultBackgroundColour = 0xEEEEEE;
    private int _defaultHoverColour = 0x999999;
    
    /**
     * Bitmap thumb - The image that is used as the state indicator, it moves sideways
     * Bitmap sliderBackground - the background image is stretched to fit, only stretched horizontally
     * int numStates - the number of increments. NOTE: this DOES NOT include zero position (will be one larger than you say)
     * int initialState - NOTE: this INCLUDES ZERO POSITION.
     * int xLeftBackMargin - the amount of the background image to be used as the left side of image
     * int xRightBackMargin - the amount of background image to be used as right side of image
     *                       Everything between the left and right side gets tiled
     *
     * pre: NumStates must be greater or equal to initialState
     * pre: NumStates must be greater than 1
     */
    public BitmapSliderField( Bitmap thumb
                , Bitmap sliderBackground
                , int numStates
                , int initialState
                , int xLeftBackMargin
                , int xRightBackMargin )
    {
        this( thumb, sliderBackground, sliderBackground, numStates, initialState, xLeftBackMargin, xRightBackMargin, FOCUSABLE );
    }
    
    public BitmapSliderField( Bitmap thumb
                , Bitmap sliderBackground
                , int numStates
                , int initialState
                , int xLeftBackMargin
                , int xRightBackMargin
                , long style )
    {  
        this( thumb, sliderBackground, sliderBackground, numStates, initialState, xLeftBackMargin, xRightBackMargin, style );
    }
    
    public BitmapSliderField( Bitmap thumb
                , Bitmap sliderBackground
                , Bitmap sliderBackgroundFocus
                , int numStates
                , int initialState
                , int xLeftBackMargin
                , int xRightBackMargin )
    {
        this( thumb, sliderBackground, sliderBackgroundFocus, numStates, initialState, xLeftBackMargin, xRightBackMargin, FOCUSABLE );
    }
    
    public BitmapSliderField( Bitmap thumb
                , Bitmap sliderBackground
                , Bitmap sliderBackgroundFocus
                , int numStates
                , int initialState
                , int xLeftBackMargin
                , int xRightBackMargin
                , long style )
    {
        super( style );

        if( initialState > numStates || numStates < 2 ){
        }
        _imageThumb = thumb;
        _imageSlider = sliderBackground;
        _imageSliderFocus = sliderBackgroundFocus;
        _numStates = numStates;
        setState( initialState );

        _xLeftBackMargin = xLeftBackMargin;
        _xRightBackMargin = xRightBackMargin;

        _rop = _imageSlider.hasAlpha() ? Graphics.ROP_SRC_ALPHA : Graphics.ROP_SRC_COPY;

        _thumbWidth = thumb.getWidth();
        _thumbHeight = thumb.getHeight();
        
        initBitmaps();
    }
                    

    /**
     * colours - An array of colours, one for each of the states
     * selectColours - An array of colours, one for each selected state
     */
    public BitmapSliderField( Bitmap thumb
                , Bitmap sliderBackground
                , int numStates
                , int initialState
                , int xLeftBackMargin
                , int xRightBackMargin
                , int[] colours
                , int[] selectColours )
    {
        this(thumb, sliderBackground, sliderBackground, numStates, initialState, xLeftBackMargin, xRightBackMargin, FOCUSABLE );

        if( colours.length != numStates+1 ){
            throw new IllegalArgumentException();
        }
        _backgroundColours = colours;
        _backgroundSelectedColours = selectColours;
    }

    /**
     * Cuts up the background image and margins you provide
     * to make the left, center, and right bitmaps
     */
    public void initBitmaps()
    {
        int height = _imageSlider.getHeight();

        _imageSliderLeft = new Bitmap( _xLeftBackMargin, height );
        _imageSliderCenter = new Bitmap( _imageSlider.getWidth() - _xRightBackMargin - _xLeftBackMargin, height);
        _imageSliderRight = new Bitmap( _xRightBackMargin, height );

        copy( _imageSlider, 0, 0, _xLeftBackMargin, height, _imageSliderLeft );
        copy( _imageSlider, _xLeftBackMargin, 0, _imageSlider.getWidth() - _xRightBackMargin - _xLeftBackMargin, height, _imageSliderCenter);
        copy( _imageSlider, _imageSlider.getWidth() - _xRightBackMargin, 0, _xRightBackMargin, height, _imageSliderRight);
        
        _imageSliderFocusLeft = new Bitmap( _xLeftBackMargin, height );
        _imageSliderFocusCenter = new Bitmap( _imageSlider.getWidth() - _xRightBackMargin - _xLeftBackMargin, height);
        _imageSliderFocusRight = new Bitmap( _xRightBackMargin, height );
        
        copy( _imageSliderFocus, 0, 0, _xLeftBackMargin, height, _imageSliderFocusLeft );
        copy( _imageSliderFocus, _xLeftBackMargin, 0, _imageSlider.getWidth() - _xRightBackMargin - _xLeftBackMargin, height, _imageSliderFocusCenter);
        copy( _imageSliderFocus, _imageSlider.getWidth() - _xRightBackMargin, 0, _xRightBackMargin, height, _imageSliderFocusRight);
    }

    private void copy(Bitmap src, int x, int y, int width, int height, Bitmap dest) {
        int[] argbData = new int[width * height];
        src.getARGB(argbData, 0, width, x, y, width, height);
        for(int tx = 0; tx < dest.getWidth(); tx += width) {
            for(int ty = 0; ty < dest.getHeight(); ty += height) {
                dest.setARGB(argbData, 0, width, tx, ty, width, height);
            }
        }
    }

    /**
     * Change the state of the switch. Zero is far left
     * @param on - if true, the switch will be set to on state
     */
    public void setState(int newState) {
        if( newState > _numStates ){
            throw new IllegalArgumentException();
        } else {
            _currentState = newState;
            invalidate();
        }
    }

    /**
     * @return The current state that is selected
     *          State zero is on the far left, and increase to the right
     */
    public int getState() {
        return _currentState;
    }

    /**
     * @return The number of states the slider has
     */
    public int getNumStates() {
        return _numStates;
    }
    
    /**
     * @return The current background colour being used
     */
    public int getColour() {
        if(_backgroundSelectedColours != null) {
            return _backgroundSelectedColours[getState()];
        }
        return 0x000000;
    }
    
    public int getPreferredWidth() {
        return _totalWidth;
    }

    public int getPreferredHeight() {
        return _totalHeight;
    }

    // Similar to TextFields layout()
    protected void layout( int width, int height ) {
        if (width < 0 || height < 0)
            throw new IllegalArgumentException();

        // We'll take all we can get
        _totalWidth = width;
        
        // The largest of the two image heights
        _totalHeight = Math.max(_imageSlider.getHeight(), _imageThumb.getHeight());
        
        setExtent( _totalWidth, _totalHeight );
    }

    public void paint( Graphics g )
    {
        // Calculate the slider position
        int sliderHeight = _imageSlider.getHeight();
        int sliderBackYOffset = ( _totalHeight - sliderHeight ) >> 1;
        
        // Determine a Background Color for the slider
        int backgroundColor = _defaultBackgroundColour;
        if( _backgroundSelectedColours != null || _backgroundColours != null ) {
            
            if( _selected ) {
                backgroundColor = _backgroundSelectedColours != null ? _backgroundSelectedColours[getState()] : _defaultSelectColour;
            } else if(g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS)) {
                backgroundColor = _backgroundColours != null ? _backgroundColours[getState()] : _defaultHoverColour;
            } else {
                backgroundColor = _defaultBackgroundColour;
            }
        }
        g.setColor( backgroundColor );
        g.fillRect( 1, sliderBackYOffset + 1, _totalWidth - 2, sliderHeight - 2 );
    
        if(g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS)) {    
            paintSliderBackground( g, _imageSliderFocusLeft, _imageSliderFocusCenter, _imageSliderFocusRight );
        } else {
            paintSliderBackground( g, _imageSliderLeft, _imageSliderCenter, _imageSliderRight );
        }
        
        // Calculate the thumb position
        int thumbXOffset = ( ( _totalWidth - _thumbWidth ) * _currentState ) / _numStates;
        
        // Draw the thumb
        g.drawBitmap( thumbXOffset, ( _totalHeight - _thumbHeight ) >> 1, _thumbWidth, _thumbHeight, _imageThumb, 0, 0 );
    }

    private void paintSliderBackground( Graphics g, Bitmap left, Bitmap middle, Bitmap right ) 
    {
        int sliderHeight = _imageSlider.getHeight();
        int sliderBackYOffset = ( _totalHeight - sliderHeight ) >> 1;
        
        // Left
        g.drawBitmap( 0, sliderBackYOffset, _xLeftBackMargin, sliderHeight, left, 0, 0 );//lefttop
        // Middle
        g.tileRop( _rop, _xRightBackMargin, sliderBackYOffset, _totalWidth - _xLeftBackMargin - _xRightBackMargin, sliderHeight, middle, 0, 0 );//top
        // Right
        g.drawBitmap( _totalWidth - _xRightBackMargin, sliderBackYOffset, _xRightBackMargin, sliderHeight, right, 0, 0 );//lefttop
    }

    public void paintBackground( Graphics g ) 
    {
        // nothing
    }

    protected void drawFocus( Graphics g, boolean on )
    {
        boolean oldDrawStyleFocus = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS );
        try {
            if( on ) {
                g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, true );
            }
            paint( g );
        } finally {
            g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus );
        }
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
            isOutOfBounds = true;
        }
        switch(message.getEvent()) {
            case TouchEvent.CLICK:
            case TouchEvent.MOVE:
            	if( isOutOfBounds ){ return true; }// consume
                _selected = true; // Pressed effect
                
                // update state
                int stateWidth = getExtent().width / _numStates;
                int numerator = x / stateWidth;
                int denominator = x % stateWidth;
                if( denominator > stateWidth / 2 ) {
                    numerator++;
                }
                _currentState = numerator;
                invalidate();
                
                isConsumed = true;
                break;
            case TouchEvent.UNCLICK:
                if(isOutOfBounds) {
                    _selected = false; // Reset presssed effect
                    return true;
                }
                
                // A field change notification is only sent on UNCLICK to allow for recovery
                // should the user cancel, i.e. click and move off the button
    
                _selected = false; // Reset pressed effect
                
                // Update state
                stateWidth = getExtent().width / _numStates;
                numerator = x / stateWidth;
                denominator = x % stateWidth;
                if( denominator > stateWidth / 2 ) {
                    numerator++;
                }
                _currentState = numerator;
                invalidate();
                
                fieldChangeNotify(0);
                
                isConsumed = true;
                break;
        }
        return isConsumed;
    }
//#endif

    protected boolean navigationMovement(int dx, int dy, int status, int time) 
    {
        if( _selected && dx != 0 )
        {
            if(dx > 0 || dy > 0) {
                incrementState();
                fieldChangeNotify( 0 );
                return true;
            } else if(dx < 0 || dy < 0) {
                decrementState();
                fieldChangeNotify( 0 );
                return true;
            }
        }
        return super.navigationMovement( dx, dy, status, time);
    }

    public void decrementState() {
        if(_currentState > 0) {
            _currentState--;
            invalidate();
        }
    }

    public void incrementState() {
        if(_currentState < _numStates) {
            _currentState++;
            invalidate();
        }
    }


    /**
     * Invokes an action on this field.  The only action that is recognized by this method is
     * {@link Field#ACTION_INVOKE}, which toggles this field from being checked and unchecked.
    */
    protected boolean invokeAction(int action) {
        switch(action) {
            case ACTION_INVOKE: {
                toggleSelected();
                return true;
            }
        }
        return false;
    }

    /**
     * Listens for the space key to be pressed
     * When pressed, state is toggles
     */
    protected boolean keyChar( char key, int status, int time ) {
        if( key == Characters.SPACE || key == Characters.ENTER ) {
            toggleSelected();
            return true;
        }

        return false;
    }

    protected boolean trackwheelClick( int status, int time ) {
        if( isEditable() ) {
            toggleSelected();
            return true;
        }
        return super.trackwheelClick(status, time);
    }

    /**
     * Toggles the state of the switch
     */
    private void toggleSelected() {
        _selected = !_selected;
        invalidate();
    }

    public void setDirty( boolean dirty )
    {
        // We never want to be dirty or muddy
    }

    public void setMuddy( boolean muddy )
    {
        // We never want to be dirty or muddy
    }

    protected void makeContextMenu(ContextMenu contextMenu) {
        super.makeContextMenu(contextMenu);
        if((Ui.getMode() < Ui.MODE_ADVANCED) && isEditable()) {
            contextMenu.addItem(_changeOptionsItem);
        }
    }

    /**
     * @category Internal InnerClass
     */
    static class ChangeOptionMenuItem extends MenuItem {
        ChangeOptionMenuItem() {
            super("Select", 30270, 10);
        }

        ChangeOptionMenuItem(String text) {
            super(text, 30270, 10);
        }

        public void run() {
            BitmapSliderField theSwitch = (BitmapSliderField)getTarget();
            theSwitch.keyChar(Characters.SPACE, 0, 0); //menu item simulates space
        }

        public int getPriority() {
            return 100 + (getTarget().isMuddy() ? 1000 : 0);
        }
    };
}
