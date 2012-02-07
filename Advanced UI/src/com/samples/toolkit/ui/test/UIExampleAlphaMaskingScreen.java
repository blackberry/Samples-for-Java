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

package com.samples.toolkit.ui.test;


import com.samples.toolkit.ui.component.*;
import com.samples.toolkit.ui.container.*;
import com.samples.toolkit.ui.decor.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

/**
 * 
 */
public class UIExampleAlphaMaskingScreen extends UIExampleScreen
{
    private FlowFieldManager _flow1;
    private FlowFieldManager _flow2;
    private BitmapMask _mask;
    
    public UIExampleAlphaMaskingScreen() {
        super( 0 );
        setTitle("Alpha BitmapMask Examples");
        
        Bitmap maskImage = Bitmap.getBitmapResource( "mask.png" );
        
        _flow1 = new FlowFieldManager();
        _flow2 = new FlowFieldManager();
        
        addLabel( "Image Mask" );
        
        _mask = new BitmapMask( new XYEdges( 9, 9, 9, 9 ), maskImage );
        
        BitmapField maskPreview = new BitmapField( maskImage );
        maskPreview.setMargin( 15, 15, 15, 15 );
        add( maskPreview );
        
        
        addImage( "golf.png" );
        addImage( "grass.png" );
        addImage( "storm.png" );
        addImage( "violin.png" );
        
        
        addMaskedImage( "golf.png" );
        addMaskedImage( "grass.png" );
        addMaskedImage( "storm.png" );
        addMaskedImage( "violin.png" );
        
        _flow1.setPadding( 15, 5, 15, 5 );
        _flow2.setPadding( 15, 5, 15, 5 );
        
        addLabel( "Original Images" );
        add( _flow1 );
        addLabel( "Masked Images" );
        add( _flow2 );
    }
    
    private void addLabel( String title )
    {
        LabelField imageField = new LabelField( title );
        imageField.setMargin( 5, 5, 5, 5 );
        add( imageField );
    } 
    
    private void addImage( String filename )
    {
        BitmapField imageField = new BitmapField( Bitmap.getBitmapResource( filename ) );
        imageField.setMargin( 5, 5, 5, 5 );
        _flow1.add( imageField );
    } 
    
    private void addMaskedImage( String filename )
    {
        Bitmap imageToMask = Bitmap.getBitmapResource( filename );
        _mask.applyMask( imageToMask );
        BitmapField imageField = new BitmapField( imageToMask );
        imageField.setMargin( 5, 5, 5, 5 );
        _flow2.add( imageField );
    } 
}
