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
import net.rim.device.api.ui.component.LabelField;


public class HyperlinkButtonField extends LabelField
{
    private int _textColour;
    private int _textColourFocus;
    private int _highlightColour;
    
    private int _menuOrdinal;
    private int _menuPriority;
    
    private XYRect _tmpRect = new XYRect();
    

    public HyperlinkButtonField( String text, int textColour, int highlightColour, int menuOrdinal, int menuPriority )
    {
        this( text, textColour, textColour, highlightColour, menuOrdinal, menuPriority );
    }
        
    public HyperlinkButtonField( String text, int textColour, int textColourFocus, int highlightColour, int menuOrdinal, int menuPriority )
    {
        this( text, textColour, textColourFocus, highlightColour, menuOrdinal, menuPriority, 0 );
    }


    public HyperlinkButtonField( String text, int textColour, int textColourFocus, int highlightColour, int menuOrdinal, int menuPriority, long style )
    {
        super( text, Field.FOCUSABLE | style );

        _textColour = textColour;
        _textColourFocus = textColourFocus;
        _highlightColour = highlightColour;
        _menuOrdinal = menuOrdinal;
        _menuPriority = menuPriority;
    }
    
    public void applyFont()
    {
        Font underlineFont = getFont().derive( Font.UNDERLINED );
        setFont( underlineFont );   
    }
    
    protected void paint( Graphics g ) 
    {
        int oldColour = g.getColor();
        try {           
            if(g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ) {
                g.setColor( _textColourFocus );
            } else {
                g.setColor( _textColour );
            }
            super.paint( g );
        } finally {
            g.setColor( oldColour );
        }
    }
    
    protected void drawFocus( Graphics g, boolean on ) 
    {
        getFocusRect( _tmpRect );

        boolean oldDrawStyleFocus = g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS );
        int oldBackgroundColour = g.getBackgroundColor();
        
        boolean notEmpty = g.pushContext( _tmpRect.x, _tmpRect.y, _tmpRect.width, _tmpRect.height, 0, 0 );
        try {
            if( notEmpty ) {
                if( on ) {
                    g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, true );
                    g.setBackgroundColor( _highlightColour );
                }
                g.clear();
                paint( g );
            }
        } finally {
            g.popContext();
            g.setBackgroundColor( oldBackgroundColour );
            g.setDrawingStyle( Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus );
        }
    }
            
            
    protected boolean keyChar( char character, int status, int time ) 
    {
        if( character == Characters.ENTER ) {
            fieldChangeNotify( 0 );
            return true;
        }
        return super.keyChar( character, status, time );
    }

    protected boolean trackwheelClick( int status, int time ) {        
        keyChar(Characters.ENTER, status, time );            
        return true;
    }

    protected boolean invokeAction( int action ) 
    {
        switch( action ) {
            case ACTION_INVOKE: {
                fieldChangeNotify( 0 );
                return true;
            }
        }
        return super.invokeAction( action );
    }
            

    public void setDirty( boolean dirty ) 
    {
        // We never want to be dirty or muddy
    }
    
            
    public void setMuddy( boolean muddy ) 
    {
        // We never want to be dirty or muddy
    }
    
    
    public String getMenuText()
    {
        return getText();
    }
    

    /**
     * Returns a MenuItem that could be used to invoke this link.
     */
    public MenuItem getMenuItem()
    {
        if( _menuOrdinal < 0
         || _menuPriority < 0 ) {
            return null;
        }
        return new MenuItem( getMenuText(), _menuOrdinal, _menuPriority ) {
            public void run() {
                fieldChangeNotify( 0 );
            }
        };
    }
            
}
