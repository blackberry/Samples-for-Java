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
import net.rim.device.api.ui.component.*;



public class UIExampleListStyleButtonSetScreen extends UIExampleScreen
{
    private Bitmap _caret = Bitmap.getBitmapResource( "chevron_right_black_15x22.png" );
    private ForegroundManager _foreground;    
        
    public UIExampleListStyleButtonSetScreen() {
        
        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT );
        setTitle( "ListStyleButtonField Sets" );
        
        _foreground = new ForegroundManager();
        
        
        LabelField verticalLabel = new LabelField( "This field set will automatically round the top and bottom fields, creating a strong association between the elements. Good for grouping related objects in Options screens." );
        verticalLabel.setPadding(5, 15, 5, 15);
        _foreground.add( verticalLabel );
        
        ListStyleButtonSet buttonSet = new ListStyleButtonSet();
       
        ListStyleButtonField link = new ListStyleButtonField( "About", _caret );
        buttonSet.add( link );
        
        link = new ListStyleButtonField( "Owner", _caret );
        buttonSet.add( link );
        
        link = new ListStyleButtonField( "Status", _caret );
        buttonSet.add( link );
        
        _foreground.add( buttonSet );
        
        
        LabelField horizontalLabel = new LabelField( "HorizontalListStyleButtonSet will stack the fields horizontally, giving each an equal amount of space." );
        horizontalLabel.setPadding(5, 15, 5, 15);
        _foreground.add( horizontalLabel );
        
        LabelField horizontalLabel2 = new LabelField( "The ListStyleButtonField also supports Drawstyle.LEFT | RIGHT | HCENTER style bits for text alignment" );
        horizontalLabel2.setPadding(5, 15, 5, 15);
        _foreground.add( horizontalLabel2 );
        
        HorizontalListStyleButtonSet exampleSet = new HorizontalListStyleButtonSet();
        exampleSet.add( new ListStyleButtonField( "Shuffle", DrawStyle.HCENTER ) );
        exampleSet.add( new ListStyleButtonField( "Stop", DrawStyle.HCENTER ) );
        _foreground.add( exampleSet );
        
        add( _foreground );
    }
    
}
