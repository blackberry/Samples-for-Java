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

import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;

/**
 * 
 */
public class UIExampleButtonScreen extends UIExampleScreen
{
    
    public UIExampleButtonScreen() {
        super( 0 );
        setTitle("Custom Button Fields");
        
        

        /*
        LabelField firstLabel = new LabelField( "SimpleButtonField. A clean looking button." );
        firstLabel.setPadding(5, 5, 5, 5);
        add( firstLabel );
        
        // Button Examples  
        HorizontalFieldManager simpleButtonManager2 = new HorizontalFieldManager();
        SimpleButtonField submitButton = new SimpleButtonField("Submit", 0xDDDDDD, 0xFFFFFF, 0x666666, 0x0000FF, 0xDDDDDD, 0x333333 );
        submitButton.setMargin(0,4,0,4);
        simpleButtonManager2.add( submitButton );
        SimpleButtonField cancelButton2 = new SimpleButtonField("Cancel", 0xDDDDDD, 0xFFFFFF, 0x666666, 0x0000FF, 0xDDDDDD, 0x333333 );
        cancelButton2.setMargin(0,4,0,4);
        simpleButtonManager2.add( cancelButton2 );
        simpleButtonManager2.setPadding(5,5,5,5);
        add( simpleButtonManager2 );
        
       
        LabelField roundLabel = new LabelField( "SimpleRoundButtonField. Definable Roundness." );
        roundLabel.setPadding(5, 5, 5, 5);
        add( roundLabel );
       
        // Rounded Buttons in Colour
        HorizontalFieldManager simpleButtonManager3 = new HorizontalFieldManager();
        SimpleRoundButtonField submitButton2 = new SimpleRoundButtonField( "Submit", 7, 7, 0xDDDDDD, 0xFFFFFF, 0x666666, 0x0000FF );
        submitButton2.setMargin(0,4,0,4);
        simpleButtonManager3.add( submitButton2 );
        SimpleRoundButtonField cancelButton3 = new SimpleRoundButtonField( "Cancel", 7, 7, 0xDDDDDD, 0xFFFFFF, 0x666666, 0x0000FF );
        cancelButton3.setMargin(0,4,0,4);
        simpleButtonManager3.add( cancelButton3 );
        simpleButtonManager3.setPadding(5,5,5,5);
        add( simpleButtonManager3 );
        submitButton2.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                Dialog.alert( "Hello World" );
            }
        } );
        cancelButton3.setChangeListener( new FieldChangeListener( ) {
            public void fieldChanged( Field field, int context ) {
                Dialog.alert( "Hello World" );
            }
        } );
        */
        
        LabelField embossedLabel = new LabelField( "EmbossedButtonField. A bit more button like." );
        embossedLabel.setPadding(5, 5, 5, 5);
        add( embossedLabel );
       
        HorizontalFieldManager embossedManager = new HorizontalFieldManager();
        EmbossedButtonField okButton1 = new EmbossedButtonField( "OK" );
        EmbossedButtonField cancelButton4 = new EmbossedButtonField( "Cancel" );
        okButton1.setMargin(0,4,0,4);
        cancelButton4.setMargin(0,4,0,4);
        embossedManager.add( okButton1 );
        embossedManager.add( cancelButton4 );
        embossedManager.setPadding(5,5,5,5);
        add( embossedManager );
        
        
        LabelField managerLabel = new LabelField( "Combining custom buttons that USE_ALL_WIDTH with the Justified EvenlySpaced HorizontalFieldManager lets you create large target areas." );
        managerLabel.setPadding(5, 5, 5, 5);
        add( managerLabel );
        
        JustifiedEvenlySpacedHorizontalFieldManager toolbarOne = new JustifiedEvenlySpacedHorizontalFieldManager();
        EmbossedButtonField okButton5 = new EmbossedButtonField( "OK", USE_ALL_WIDTH );
        EmbossedButtonField cancelButton5 = new EmbossedButtonField( "Cancel", USE_ALL_WIDTH );
        okButton5.setMargin(0,5,0,5);
        cancelButton5.setMargin(0,5,0,5);
        toolbarOne.add( okButton5 );
        toolbarOne.add( cancelButton5 );
        toolbarOne.setMargin( 20, 0, 20, 0);
        add( toolbarOne );
        
        
        JustifiedEvenlySpacedHorizontalFieldManager toolbarTwo = new JustifiedEvenlySpacedHorizontalFieldManager();
        EmbossedButtonField b1 = new EmbossedButtonField( "One", USE_ALL_WIDTH );
        EmbossedButtonField b2 = new EmbossedButtonField( "Two", USE_ALL_WIDTH );
        EmbossedButtonField b3 = new EmbossedButtonField( "Three", USE_ALL_WIDTH );
        b1.setMargin(0,5,0,5);
        b2.setMargin(0,5,0,5);
        b3.setMargin(0,5,0,5);
        toolbarTwo.add( b1 );
        toolbarTwo.add( b2 );
        toolbarTwo.add( b3 );
        toolbarTwo.setMargin( 20, 0, 20, 0);
        add( toolbarTwo );
        
    }
}
