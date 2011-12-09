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

public class LabeledSwitch extends Field
{
    private String _textOn;
    private String _textOff;
    
    private int _textWidth;
    private int _textHeight;
    
    private int _totalWidth;
    private int _totalHeight;
        
    private Bitmap _imageOn;
    private Bitmap _imageOnFocus;
    private Bitmap _imageOff;
    private Bitmap _imageOffFocus;
    
    private boolean _on;
    private boolean _selected;
    
    private Font _labelFont;
        
    private static MenuItem _changeOptionsItem = new ChangeOptionMenuItem();
    
    private int _textColour = 0x888888;
    private int _textColourFocus = 0x000000;
    
    private int _horizontalTextImageGap;
    
    private Bitmap _switchImage;  
    private String _labelText;  
            
    public LabeledSwitch( Bitmap imageOn
                , Bitmap imageOff
                , Bitmap imageOnFocus
                , Bitmap imageOffFocus
                , String textOn
                , String textOff
                , boolean onByDefault ) 
    {
        super( Field.FIELD_VCENTER );
        
        _textOn = textOn;
        _textOff = textOff;
        
        _imageOn = imageOn;
        _imageOff = imageOff;
        _imageOnFocus = imageOnFocus;
        _imageOffFocus = imageOffFocus;
        
        _on = onByDefault;
        _selected = false;
        
        _horizontalTextImageGap = _imageOn.getHeight() / 3;
        
    }
    
    public void applyFont()
    {
        _labelFont = getFont().derive( Font.PLAIN, _imageOn.getHeight()  );
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
        return _totalWidth;
    }
    
    public int getPreferredHeight() {
        return _totalHeight;
    }

    protected void layout( int width, int height ) {
    	// 
        _textWidth = Math.max( _labelFont.getAdvance( _textOn + "a" ), _labelFont.getAdvance( _textOff + "a" ) );
        _textHeight = _labelFont.getHeight();
        
        _totalWidth = _imageOn.getWidth() + _horizontalTextImageGap + _textWidth;
        _totalHeight = _imageOn.getHeight();
            
        setExtent( _totalWidth, _totalHeight );
    }
    
    public void paint( Graphics g ) 
    {
        Font oldFont = g.getFont();
        int oldColor = g.getColor();
        
        try { 
        
            if( _on ) {
                _switchImage = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? _imageOnFocus : _imageOn;
            } else {
                _switchImage = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? _imageOffFocus : _imageOff;
            }
            
            g.setFont( _labelFont );
        
            // Determine Label Colour
            g.setColor( g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? _textColourFocus : _textColour );
            
            // Label
            g.drawText( _on ? _textOn : _textOff, 0, ( getHeight() - _textHeight ) / 2, DrawStyle.RIGHT, _textWidth ); 
            
            // Image
            g.drawBitmap( _textWidth + _horizontalTextImageGap, 0, _switchImage.getWidth(), _switchImage.getHeight(), _switchImage, 0, 0 );
        
        } finally {
            g.setFont( oldFont );
            g.setColor( oldColor );
        }
    }
    
    public void paintBackground( Graphics g ) {}
    
    protected void drawFocus( Graphics g, boolean on ) 
    {
        // Paint() handles it all
        g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, true );
        paint( g );
    }
    
    protected boolean keyChar( char key, int status, int time )
    {
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
    
    protected boolean trackwheelClick( int status, int time )
    {        
        if( isEditable() ) {
            toggle();            
            return true;
        }
        return super.trackwheelClick(status, time);
    }
    
    /**
     * Toggles the state of the switch
     */
    private void toggle()
    {
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

    protected void makeContextMenu(ContextMenu contextMenu)
    {
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
            LabeledSwitch theSwitch = (LabeledSwitch)getTarget();
            theSwitch.toggle();
        }

        public int getPriority() {
            return 100 + (getTarget().isMuddy() ? 1000 : 0);
        }
    };
}
