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

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;



public class UIExampleFieldSetIndexScreen extends UIExampleScreen
{

    private Bitmap _caret = Bitmap.getBitmapResource( "chevron_right_black_12x18.png" );
    
    private ForegroundManager _foreground;
    
    public UIExampleFieldSetIndexScreen() {

        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT );
        setTitle("Field Set Examples");

        _foreground = new ForegroundManager();
        
        
        
        LabelField header = new LabelField( "Choose a style from the list below:" );
        header.setMargin( 15, 5, 0, 20 );
        _foreground.add( header );
        
        
        ListStyleButtonSet buttonSet = new ListStyleButtonSet();
        
        ListStyleButtonField link = new ListStyleButtonField( "BlackBerry 6 Style", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                 Border titleBorder   = BorderFactory.createBitmapBorder( new XYEdges( 4, 12, 4, 12 ), Bitmap.getBitmapResource( "fieldset2_title_border.png" ) );
                 Border contentBorder = BorderFactory.createBitmapBorder( new XYEdges( 4, 12, 4, 12 ), Bitmap.getBitmapResource( "fieldset2_body_border.png" ) );
                 pushScreen( new UIExampleFieldSetScreen( titleBorder, contentBorder ) );
            }
        } );
        buttonSet.add( link );
        
        link = new ListStyleButtonField( "Custom 1", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                Border titleBorder   = BorderFactory.createBitmapBorder( new XYEdges( 8, 9, 3, 9 ), Bitmap.getBitmapResource( "fieldset_title_border.png" ) );
                Border contentBorder = BorderFactory.createBitmapBorder( new XYEdges( 6, 9, 10, 9 ), Bitmap.getBitmapResource( "fieldset_body_border.png" ) );
                pushScreen( new UIExampleFieldSetScreen( titleBorder, contentBorder ) );
            }
        } );
        buttonSet.add( link );

        _foreground.add( buttonSet );
        add( _foreground );
        
    }
    
    private void pushScreen( Screen toPush ) 
    {
        UiApplication.getUiApplication().pushScreen( toPush );
    }
    
}
