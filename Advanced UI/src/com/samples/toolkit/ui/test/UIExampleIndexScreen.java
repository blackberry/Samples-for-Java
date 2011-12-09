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


public class UIExampleIndexScreen extends UIExampleScreen
{
    private Bitmap _caret = Bitmap.getBitmapResource( "chevron_right_black_15x22.png" );
    private ForegroundManager _foreground;    
        
    public UIExampleIndexScreen() {
        
        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT );
        setTitle( "UI Example" );
        
        _foreground = new ForegroundManager();
        
        addHeading( "Buttons" );
        ListStyleButtonSet buttonSet = new ListStyleButtonSet();
       
        ListStyleButtonField link = new ListStyleButtonField( "BitmapButtonField", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleBitmapButtonScreen() );
            }
        } );
        buttonSet.add( link );
        
        link = new ListStyleButtonField( "Custom Buttons", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleButtonScreen() );
            }
        } );
        buttonSet.add( link );
        
        link = new ListStyleButtonField( "HyperlinkButtonField", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleHyperlinkScreen() );
            }
        } );
        buttonSet.add( link );
        
        link = new ListStyleButtonField( "ListStyleButtonField", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleListStyleButtonFieldScreen() );
            }
        } );
        buttonSet.add( link );
        
        _foreground.add( buttonSet );
        
        
        addHeading( "Fields" );
        ListStyleButtonSet sliderSet = new ListStyleButtonSet();
        
        link = new ListStyleButtonField( "Gauge", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleGaugeScreen() );
            }
        } );
        sliderSet.add( link );

        link = new ListStyleButtonField( "Labeled Switch", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleLabeledSwitchScreen() );
            }
        } );
        sliderSet.add( link );
        
        link = new ListStyleButtonField( "Progress Spinners", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleProgressAnimationScreen() );
            }
        } );
        sliderSet.add( link );
        
        link = new ListStyleButtonField( "Rating", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleRatingScreen() );
            }
        } );
        sliderSet.add( link );
        
        link = new ListStyleButtonField( "Slider", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleSliderScreen() );
            }
        } );
        sliderSet.add( link );
        

        _foreground.add( sliderSet );
        
        
        
        addHeading( "Managers" );
        ListStyleButtonSet managerSet = new ListStyleButtonSet();
        
        link = new ListStyleButtonField( "ButtonFieldSet", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleButtonSetScreen() );
            }
        } );
        managerSet.add( link );
        
        link = new ListStyleButtonField( "ListStyleButtonSet", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleListStyleButtonSetScreen() );
            }
        } );
        managerSet.add( link );
        
        link = new ListStyleButtonField( "NegativeMargins", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExampleNegativeMarginScreen() );
            }
        } );
        managerSet.add( link );
        
        link = new ListStyleButtonField( "PillButtonSet", _caret );
        link.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                pushScreen( new UIExamplePillButtonScreen() );
            }
        } );
        managerSet.add( link );
        
        
        _foreground.add( managerSet );
        
        
        
        
        add( _foreground );
    }
    
    private void pushScreen( Screen toPush ) 
    {
        UiApplication.getUiApplication().pushScreen( toPush );
    }
    
    private void addHeading( String label ) 
    {
        LabelField header = new LabelField( label );
        header.setMargin( 15, 5, 0, 20 );
        _foreground.add( header );
    }
    
}
