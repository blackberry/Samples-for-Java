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

package com.samples.toolkit.ui.decor;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;


/**
 * A helper class for applying an alpha mask to a Bitmap
 * 
 * The BitmapMask is created with a green image, and a set of edges. 
 * It is reusable (can be applied to multiple images)
 * 
 * The green value become the alpha channel of the bitmaps passed into applyMask()
 * Black (0x00) becomes transparent
 * Green (0xFF) remains opaque
 */
public class BitmapMask
{
    private final int _alphaTop;
    private final int _alphaRight;
    private final int _alphaBottom;
    private final int _alphaLeft;

    private final int _alphaWidth;
    private final int _alphaHeight;
    private final int _alphaInsideWidth;
    private final int _alphaInsideHeight;
    
    private final int[] _alphaARGB;
    private final int[] _cornerARGB;

    /**
     * Bitmap mask 
     * @param edges the top, right, bottom, and left edges sizes
     * @param alpha the green bitmap which will become the mask
     */
    public BitmapMask( XYEdges edges, Bitmap alpha )
    {
        //Initialize edges
        if( edges == null ) {
            throw new IllegalArgumentException( "BitmapMask: XYEdges edges is null." );
        }
        if( edges.isEmpty() ) {
            throw new IllegalArgumentException( "BitmapMask: XYEdges edges is empty." );
        }
        
        _alphaTop    = edges.top;
        _alphaRight  = edges.right;
        _alphaBottom = edges.bottom;
        _alphaLeft   = edges.left;
        
        // Initalize bitmap properties
        if( alpha == null ) {
            throw new IllegalArgumentException("BitmapMask: alpha is null.");
        }
        
        _alphaWidth  = alpha.getWidth();
        _alphaHeight = alpha.getHeight();
        
        _alphaInsideWidth  = _alphaWidth  - _alphaLeft   - _alphaRight;
        _alphaInsideHeight = _alphaHeight - _alphaBottom - _alphaTop;
        if( ( _alphaInsideWidth < 0 ) || ( _alphaInsideHeight < 0 ) ) {
            throw new IllegalArgumentException("BitmapMask: Invalid alpha width and height.");
        }
        
        _alphaARGB = new int[ _alphaWidth * _alphaHeight ];
        alpha.getARGB( _alphaARGB, 0, _alphaWidth, 0, 0, _alphaWidth, _alphaHeight );
        
        for( int i=_alphaARGB.length-1; i>=0; i-- ) {
            _alphaARGB[ i ] = ( _alphaARGB[ i ] << 16 ) & 0xff000000;
        }

        _cornerARGB = new int[ Math.max( _alphaLeft, _alphaRight ) * Math.max( _alphaTop, _alphaBottom ) ];
    }
    
    public int getTop() 
    {
        return _alphaTop;
    }

    public int getRight() 
    {
        return _alphaRight;
    }

    public int getBottom() 
    {
        return _alphaBottom;
    }

    public int getLeft() 
    {
        return _alphaLeft;
    }
    
    public int getWidth() 
    {
        return _alphaWidth;
    }

    public int getHeight() 
    {
        return _alphaHeight;
    }

    /**
     * Apply the mask to the given bitmap
     *
     * @param targetBitmap this bitmaps ARGB data will be modified with the alpha data from the mask
     */
    public void applyMask( Bitmap targetBitmap ) 
    {
        int targetWidth  = targetBitmap.getWidth();
        int targetHeight = targetBitmap.getHeight();
        
        int[] bufferARGB = new int[ targetWidth ];
        
        int targetTop    = Math.min( _alphaTop,    targetHeight );
        int targetRight  = Math.min( _alphaRight,  targetWidth );
        int targetBottom = Math.min( _alphaBottom, targetHeight );
        int targetLeft   = Math.min( _alphaLeft,   targetWidth );

        int targetInsideBottomY = targetHeight - targetBottom;
        int targetInsideTopY = targetTop;

        int targetInsideRightX = targetWidth - targetRight;
        int targetInsideLeftX = targetLeft;

        int targetInsideWidth  = Math.max( targetInsideRightX  - targetInsideLeftX,  0 );
        int targetInsideHeight = Math.max( targetInsideBottomY - targetInsideTopY,   0 );


        if( _alphaInsideWidth > 0) {
            if( targetTop > 0 ) {
                drawTileMask( _alphaLeft, 0, _alphaInsideWidth, _alphaTop,  
                              targetLeft, 0, targetInsideWidth, targetTop, targetBitmap, bufferARGB );    // top
            }
            if( targetBottom > 0 ) {
                drawTileMask( _alphaLeft, _alphaHeight - _alphaBottom, _alphaInsideWidth, _alphaBottom, 
                              targetLeft, targetHeight - targetBottom, targetInsideWidth, targetBottom, targetBitmap, bufferARGB );    // bottom
            }
        }
        if( _alphaInsideHeight > 0 ) {
            if( targetLeft > 0 ) {
                drawTileMask( 0, _alphaTop, _alphaLeft, _alphaInsideHeight,  
                              0, targetTop, targetLeft, targetInsideHeight, targetBitmap, bufferARGB );    // left
            }
            if( targetRight > 0 ) {
                drawTileMask( _alphaWidth - _alphaRight, _alphaTop, _alphaRight, _alphaInsideHeight,  
                              targetWidth - targetRight, targetTop, targetRight, targetInsideHeight, targetBitmap, bufferARGB );    // right
            }
        }
        if( targetTop > 0 ) {
            if( targetLeft > 0) {
                drawCornerMask( 0, 0, _alphaLeft, _alphaTop,  
                                0, 0, targetBitmap );    // left top
            }
            if( targetRight > 0 ) {
                drawCornerMask( _alphaLeft + _alphaInsideWidth, 0, _alphaRight, _alphaTop,  
                                targetInsideRightX, 0, targetBitmap ); // right top
            }
        }
        if( targetBottom > 0 ) {
            if( targetLeft > 0 ) {
                drawCornerMask( 0, _alphaHeight - _alphaBottom, _alphaLeft, _alphaBottom, 
                                0, targetHeight - targetBottom, targetBitmap ); // left bottom
            }
            if( targetRight > 0 ) {
                drawCornerMask( _alphaWidth - _alphaRight, _alphaHeight - _alphaBottom, _alphaRight, _alphaBottom, 
                                targetWidth - targetRight, targetHeight - targetBottom, targetBitmap ); // right bottom
            }
        }
    }

    /**
     * Apply a portion of the mask to the destination area. Tile if destination area is larger than source area
     */
    private void drawTileMask( int sourceX, int sourceY, int sourceWidth, int sourceHeight, 
                               int destX,   int destY,   int destWidth,   int destHeight, 
                               Bitmap targetBitmap, int[] bufferARGB )
    {
        int sourceOffsetStart = sourceY * _alphaWidth + sourceX;
        int sourceOffsetMax   = ( sourceY + sourceHeight ) + _alphaWidth;
        int sourceOffset = sourceOffsetStart;
        
        for( int y=0; y<destHeight; y++, destY++ ) {
            
            targetBitmap.getARGB( bufferARGB, 0, destWidth, destX, destY, destWidth, 1 );

            for( int x=0; x<destWidth; x++ ) {
                bufferARGB[ x ] = ( bufferARGB[ x ] & 0x00ffffff ) | _alphaARGB[ sourceOffset + x % sourceWidth ];
            }
            
            targetBitmap.setARGB( bufferARGB, 0, destWidth, destX, destY, destWidth, 1 );

            sourceOffset += _alphaWidth;
            if( sourceOffset >= sourceOffsetMax ) {
                sourceOffset = sourceOffsetStart;
            }
        }
    }
        
        
    /**
     * Apply a portion of the mask to the destination area. Tile if destination area is smaller than source area
     */
    private void drawCornerMask( int sourceX, int sourceY, int sourceWidth, int sourceHeight, 
                                 int destX,   int destY,    
                                 Bitmap targetBitmap )
    {
        int sourceOffset = sourceY * _alphaWidth + sourceX;
        int destOffset = 0;
        
        targetBitmap.getARGB( _cornerARGB, 0, sourceWidth, destX, destY, sourceWidth, sourceHeight );

        for( int y=0; y<sourceHeight; y++ ) {
            
            for( int x=0; x<sourceWidth; x++, sourceOffset++, destOffset++ ) {
                _cornerARGB[ destOffset] = ( _cornerARGB[ destOffset ] & 0x00ffffff ) | _alphaARGB[ sourceOffset ];
            }
            sourceOffset += _alphaWidth - sourceWidth;
        }

        targetBitmap.setARGB( _cornerARGB, 0, sourceWidth, destX, destY, sourceWidth, sourceHeight );
    }
        
        
}





