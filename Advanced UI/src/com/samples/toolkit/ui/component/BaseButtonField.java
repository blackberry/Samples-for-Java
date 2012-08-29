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


/**
 * Implements all the stuff we don't want to do each time we need a new button
 */
public abstract class BaseButtonField extends Field 
{
    private XYRect _drawFocusTempRect = new XYRect();
    
    public BaseButtonField()
    {
        this( 0 );
    }
    
    public BaseButtonField( long style )
    {        
        super( Field.FOCUSABLE | style );
    }
        
    protected void layout( int width, int height )
    {
        setExtent( 10, 10 );
    }
    
    protected void drawFocus( Graphics g, boolean on )
    {
        getFocusRect( _drawFocusTempRect );
    
        boolean oldDrawStyleFocus = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS );
        boolean notEmpty = g.pushContext( _drawFocusTempRect.x, _drawFocusTempRect.y, _drawFocusTempRect.width, _drawFocusTempRect.height, 0, 0 );

        try {
            if( notEmpty ) {
                g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, on );
                paintBackground( g );
                paint( g );
            }
        } finally {
            g.popContext();
            g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus );
        }
    }
    
    protected boolean keyChar( char character, int status, int time ) 
    {
        if( character == Characters.ENTER ) {
            clickButton();
            return true;
        }
        return super.keyChar( character, status, time );
    }
    
    protected boolean navigationClick( int status, int time ) 
    {
        if (status != 0) clickButton(); 
        return true;    
    }
    
    protected boolean trackwheelClick( int status, int time )
    {        
        if (status != 0) clickButton();    
        return true;
    }
    
    protected boolean invokeAction( int action ) 
    {
        switch( action ) {
            case ACTION_INVOKE: {
                clickButton(); 
                return true;
            }
        }
        return super.invokeAction( action );
    }    

    protected boolean touchEvent( TouchEvent message )
    {
        int x = message.getX( 1 );
        int y = message.getY( 1 );
        if( x < 0 || y < 0 || x > getExtent().width || y > getExtent().height ) {
            // Outside the field
            return false;
        }
        switch( message.getEvent() ) {
       
            case TouchEvent.UNCLICK:
                clickButton();
                return true;
        }
        return super.touchEvent( message );
    }
    
    public void setDirty( boolean dirty ) {
        // We never want to be dirty or muddy
    }
     
    public void setMuddy( boolean muddy ) {
        // We never want to be dirty or muddy
    }
         
    /**
     * A public way to click this button
     */
    public void clickButton() 
    {
        fieldChangeNotify( 0 );
    }
}

