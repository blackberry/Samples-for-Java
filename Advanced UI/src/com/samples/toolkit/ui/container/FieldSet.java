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
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;


public class FieldSet extends VerticalFieldManager
{
    private String _title;
    private Border _titleBorder;
    private Border _contentBorder;
    
    public FieldSet( String title, Border titleBorder, Border contentBorder, long style ) 
    {
        super( style );
        
        _title = title;
        _titleBorder = titleBorder;
        _contentBorder = contentBorder;
    }
    
    protected void applyFont()
    {
        setBorder( new FieldSetBorder( _title, getFont(), _titleBorder, _contentBorder ), true );
    }
    
    
    private static class FieldSetBorder extends Border 
    {
        private String _title;
        private Font _font;
        private Border _titleBorder;
        private Border _contentBorder;
        
        private Background _titleBackground;
        
        private int _titleAreaHeight;
        private int _titleBorderTopAndBottom;
        private int _titleBorderLeftAndRight;
        
        private XYRect _paintRect = new XYRect();
        
        
        public FieldSetBorder( String title, Font font, Border titleBorder, Border contentBorder )
        {
            super( getComposedBorderEdges( font, titleBorder, contentBorder ), 0 );
    
            _title = title;
            _font = font;
            _titleBorder = titleBorder;
            _contentBorder = contentBorder;
            
            _titleBackground = titleBorder.getBackground();
    
            _titleAreaHeight = titleBorder.getTop() + font.getHeight() + titleBorder.getBottom();
            _titleBorderTopAndBottom = titleBorder.getTop() + titleBorder.getBottom();
            _titleBorderLeftAndRight = titleBorder.getLeft() + titleBorder.getRight();
        }
        
        public static XYEdges getComposedBorderEdges( Font font, Border titleBorder, Border contentBorder )
        {
            if( titleBorder.getLeft() != contentBorder.getLeft()
            || titleBorder.getRight() != contentBorder.getRight() ) {
                throw new IllegalArgumentException( "borders must have matching left and right edges" );
            }
            
            return new XYEdges( 
                titleBorder.getTop() + font.getHeight() + titleBorder.getBottom() + contentBorder.getTop(),
                contentBorder.getRight(),
                contentBorder.getBottom(),
                contentBorder.getLeft() );
        }
        
        public void paint( Graphics graphics, XYRect rect )
        {
            // paint() is always called from the event thread so we don't have to worry about concurrent access to _paintRect
            _paintRect.set( rect.x, rect.y, rect.width, _titleAreaHeight );
            _titleBorder.paint( graphics, _paintRect );
            
            if( _titleBackground != null ) {
                _paintRect.x += _titleBorder.getLeft();
                _paintRect.y += _titleBorder.getTop();
                _paintRect.width -= _titleBorderLeftAndRight;
                _paintRect.height -= _titleBorderTopAndBottom;
                _titleBackground.draw( graphics, _paintRect );
            }
            
            _paintRect.set( rect.x, rect.y + _titleAreaHeight, rect.width, rect.height - _titleAreaHeight );
            _contentBorder.paint( graphics, _paintRect );
    
            Font oldFont = graphics.getFont();
            try {
                graphics.setFont( _font );
                graphics.drawText( _title, rect.x + _titleBorder.getLeft(), rect.y + _titleBorder.getTop(), DrawStyle.ELLIPSIS, rect.width - _titleBorderLeftAndRight );
            } finally {
                graphics.setFont( oldFont );
            }
        }
        
        public Background getBackground()
        {
            return _contentBorder.getBackground();
        }
        
    }    
    
}
