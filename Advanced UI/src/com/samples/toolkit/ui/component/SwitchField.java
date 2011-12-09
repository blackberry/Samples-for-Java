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

public class SwitchField extends Field
{
    Bitmap _imageOn;
    Bitmap _imageOff;
    Bitmap _imageOnFocus;
    Bitmap _imageOffFocus;
    Bitmap _switchImage;
    boolean _on;
    
    private static MenuItem _changeOptionsItem = new ChangeOptionMenuItem();
    
    public SwitchField( Bitmap imageOn
                , Bitmap imageOff
                , Bitmap imageOnFocus
                , Bitmap imageOffFocus
                , boolean onByDefault ) 
    {
        super( );
        
        _imageOn = imageOn;
        _imageOff = imageOff;
        _imageOnFocus = imageOnFocus;
        _imageOffFocus = imageOffFocus;
        _on = onByDefault;
    }
   
    /**
     * Change the state of the switch
     * @param on - if true, the switch will be set to on state
     */
    public void setOn(boolean on) {
        _on = on;
        invalidate();
    }
    
    public boolean getOnState() {
        return _on;
    }
    
    public boolean isFocusable() {
        return true;
    }
    
    public int getPreferredWidth() {
        return Math.max(_imageOn.getWidth(), _imageOff.getWidth());
    }
    
    public int getPreferredHeight() {
        return Math.max(_imageOn.getHeight(), _imageOff.getHeight());
    }

    protected void layout( int width, int height ) {
        setExtent( _imageOn.getWidth(), _imageOn.getHeight() );
    }
    
    public void paint( Graphics g ) 
    {
        if( _on ) {
            _switchImage = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? _imageOnFocus : _imageOn;
        } else {
            _switchImage = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? _imageOffFocus : _imageOff;
        }
        g.drawBitmap(0, 0, _switchImage.getWidth(), _switchImage.getHeight(), _switchImage, 0, 0 );
    }
    
    protected void drawFocus( Graphics g, boolean on ) 
    {
        g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, true );
        paint( g );
    }
    
    /**
     * Listens for the space key to be pressed
     * When pressed, state is toggles
     */
    protected boolean keyChar( char key, int status, int time ) {
        if( key == Characters.SPACE || key == Characters.ENTER ) {
            toggle();            
            return true;
        }

        return false;
    }
    
    protected boolean navigationClick( int status, int time ) 
    {
        toggle();            
        return true;    
    }
    
    protected boolean invokeAction( int action ) 
    {
        switch( action ) {
            case ACTION_INVOKE: {
                toggle(); 
                return true;
            }
        }
        return super.invokeAction( action );
    }
    
    protected boolean trackwheelClick( int status, int time ) {        
        if( isEditable() ) {
            toggle();            
            return true;
        }
        return super.trackwheelClick(status, time);
    }
    
    /**
     * Toggles the state of the switch
     */
    private void toggle() {
        _on = !_on;
        invalidate();
        fieldChangeNotify( 0 );
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
            super("Toggle", 30270, 10);
        }

        ChangeOptionMenuItem(String text) {
            super(text, 30270, 10);
        }

        public void run() {
            SwitchField theSwitch = (SwitchField)getTarget();
            theSwitch.keyChar(Characters.SPACE, 0, 0); //menu item simulates space
        }

        public int getPriority() {
            return 100 + (getTarget().isMuddy() ? 1000 : 0);
        }
    };
}
