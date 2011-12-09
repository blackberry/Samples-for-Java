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

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;


public class UIExampleListStyleButtonFieldScreen extends UIExampleScreen implements FieldChangeListener
{
    private UIExampleScreen _explanation;
    
    public UIExampleListStyleButtonFieldScreen() {
        
        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT );
        
        setTitle("ListStyleButtonField Example");
        
        Bitmap caret = Bitmap.getBitmapResource( "chevron_right_black_15x22.png" );
        
        ListStyleButtonField one   = new ListStyleButtonField( "Music", caret );
        one.setChangeListener( this );
        add( one );
        
        ListStyleButtonField two   = new ListStyleButtonField( "Photos", caret );
        two.setChangeListener( this );
        add( two );
        
        ListStyleButtonField three = new ListStyleButtonField( "Extras", caret );
        three.setChangeListener( this );
        add( three );
        
        ListStyleButtonField four  = new ListStyleButtonField( "Settings", caret );
        four.setChangeListener( this );
        add( four );
        
        ListStyleButtonField five  = new ListStyleButtonField( "Shuffle Songs", 0 );
        five.setChangeListener( this );
        add( five );
        
        _explanation = new UIExampleScreen();
        _explanation.setTitle( "ListStyleButtonField Explanation" );
        
        LabelField explanationLabel = new LabelField( "The ListStyleButtonField looks like a list row, but is just a simple button. Good for use with a small finite set of elements." );
        explanationLabel.setPadding(5, 5, 5, 5);
        _explanation.add( explanationLabel );
    }
    
    public void fieldChanged( Field field, int context )
    {
        UiApplication.getUiApplication().pushScreen( _explanation );
    }
    
}
