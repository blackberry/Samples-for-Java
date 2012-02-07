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

import net.rim.device.api.math.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;


public class AutoScaleImageField extends Field
{
    public static final long REDUCE_TO_WIDTH  = 1L << 0;
    public static final long REDUCE_TO_HEIGHT = 1L << 1;
    
    private int _scale32;

    private EncodedImage _normalImage;
    private EncodedImage _normalScaledImage;
    
    private EncodedImage _focusedImage;
    private EncodedImage _focusedScaledImage;
    
    
    public AutoScaleImageField( EncodedImage normalImage, long style ) 
    { 
        this( normalImage, null, style );
    }

    public AutoScaleImageField( EncodedImage normalImage, EncodedImage focusedImage, long style ) 
    { 
        super( style );

        if( focusedImage == null ) {
            focusedImage = normalImage;
        }
        
        if(    normalImage.getWidth()  != focusedImage.getWidth() 
            || normalImage.getHeight() != focusedImage.getHeight() ) {
            throw new IllegalArgumentException();
        }

        _scale32 = Fixed32.ONE;

        _normalImage  = normalImage;
        _focusedImage = focusedImage;
    }

    protected void layout( int width, int height ) 
    {
        int imageWidth  = _normalImage.getWidth();
        int widthScale32 = Fixed32.ONE;
        if( isStyle( REDUCE_TO_WIDTH ) &&  imageWidth > width ) {
            widthScale32 = Fixed32.toFP( imageWidth ) / width;
        }
        
        int imageHeight = _normalImage.getHeight();
        int heightScale32 = Fixed32.ONE;
        if( isStyle( REDUCE_TO_HEIGHT ) &&  imageHeight > height ) {
            heightScale32 = Fixed32.toFP( imageHeight ) / height;
        }

        int scale32 = Math.max( widthScale32, heightScale32 );
        if( scale32 == Fixed32.ONE ) {
            // No scaling necessary
            _normalScaledImage  = _normalImage;
            _focusedScaledImage = _focusedImage;
        } else if( scale32 != _scale32 ) { 
            // We need to scale the images (and by a different factor from last time)
            _scale32 = scale32;
            _normalScaledImage = _normalImage.scaleImage32( scale32, scale32 );
            if( isStyle( Field.FOCUSABLE ) ) {
                _focusedScaledImage = _focusedImage.scaleImage32( scale32, scale32 );
            }
        }

        setExtent( Math.min( width, _normalScaledImage.getScaledWidth() ), Math.min( height, _normalScaledImage.getScaledHeight() ) );
    }
    
    protected void paint( Graphics g )
    {
        if( g.isDrawingStyleSet( g.DRAWSTYLE_FOCUS ) ) {
            g.drawImage( 0, 0, _focusedScaledImage.getScaledWidth(), _focusedScaledImage.getScaledHeight(), _focusedScaledImage, 0, 0, 0 );
        } else {
            g.drawImage( 0, 0, _normalScaledImage.getScaledWidth(), _normalScaledImage.getScaledHeight(), _normalScaledImage, 0, 0, 0 );
        }
    }
    
    protected void drawFocus( Graphics g, boolean on ) 
    {
        // The DRAWSTYLE_FOCUS bit should already be set properly, so we can just call paint()
        paint( g );
    }
/*    
    protected void onFocus( int direction)
    {
        super.onFocus( direction );
        invalidate();
    }
    
    protected void onUnfocus()
    {
        super.onUnfocus();
        invalidate();
    }
*/
}



