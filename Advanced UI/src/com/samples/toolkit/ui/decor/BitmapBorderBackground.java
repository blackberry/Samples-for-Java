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


//#ifndef VER_4.1.0 | VER_4.2.0 | VER_4.2.1 | VER_4.3.0 | VER_4.5.0


package com.samples.toolkit.ui.decor;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.decor.*;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.system.*;

public class BitmapBorderBackground extends Background
{
    private Bitmap _background;
    private Border _bitmapBorder;
    private XYEdges _edges;
    private int _fillColor = - 1;
    
    public BitmapBorderBackground( XYEdges edges, Bitmap background )
    {
        _background = background;
        _edges = edges;
        
        _bitmapBorder = BorderFactory.createBitmapBorder( _edges, _background );
    }
    
    public BitmapBorderBackground( XYEdges edges, Bitmap background, int fillColor )
    {
        this( edges, background );
        _fillColor = fillColor;
    }

    public void draw( Graphics g, XYRect rect )
    {
        _bitmapBorder.paint( g, rect );
        
        if( _fillColor != - 1 && rect.width > _edges.left + _edges.right && rect.height > _edges.top + _edges.bottom ) {
            int oldColor = g.getColor();
            try {
                g.setColor( _fillColor );
                g.fillRect( rect.x + _edges.left, rect.y + _edges.top, rect.width - _edges.left - _edges.right, rect.height - _edges.top - _edges.bottom ); 
            } finally {
                g.setColor( oldColor );
            }
        }
    }
    
    public boolean isTransparent()
    {
        return _background.hasAlpha();
    }
}
//#endif
