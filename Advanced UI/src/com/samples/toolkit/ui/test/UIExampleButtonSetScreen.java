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

import com.samples.toolkit.ui.container.*;

import net.rim.device.api.ui.component.*;

/**
 * 
 */
public class UIExampleButtonSetScreen extends UIExampleScreen
{
    
    public UIExampleButtonSetScreen() {
        super( 0 );
        setTitle("Vertical Button Set");
        

        LabelField allWidthLabel = new LabelField( "If the USE_ALL_WIDTH style bit is used the buttons extend across the available area." );
        allWidthLabel.setPadding(5, 5, 5, 5);
        add( allWidthLabel );
               

        VerticalButtonFieldSet setOne = new VerticalButtonFieldSet( USE_ALL_WIDTH );
        setOne.setMargin( 15, 15, 15 ,15 );
        
        ButtonField resetButton = new ButtonField( "Reset to Default" );
        ButtonField cancelButton = new ButtonField( "Cancel" );
        resetButton.setMargin(  5, 5, 5 ,5 );
        cancelButton.setMargin( 5, 5, 5 ,5 );
        
        setOne.add( resetButton );
        setOne.add( cancelButton );
        
        add( setOne );
        
        
        
        LabelField otherLabel = new LabelField( "Otherwise, they expand to the width of the other largest button." );
        otherLabel.setPadding(5, 5, 5, 5);
        add( otherLabel );
        
        VerticalButtonFieldSet setTwo = new VerticalButtonFieldSet();
        setTwo.setMargin( 15, 15, 15 ,15 );
        
        ButtonField facebookButton = new ButtonField( "Facebook" );
        ButtonField myspaceButton = new ButtonField( "MySpace" );
        ButtonField twitterButton = new ButtonField( "Twitter" );
        ButtonField emailButton = new ButtonField( "Email" );
        ButtonField smsButton = new ButtonField( "SMS" );
        
        facebookButton.setMargin(  5, 5, 5 ,5 );
        myspaceButton.setMargin( 5, 5, 5 ,5 );
        twitterButton.setMargin( 5, 5, 5 ,5 );
        emailButton.setMargin( 5, 5, 5 ,5 );
        smsButton.setMargin( 5, 5, 5 ,5 );
        
        setTwo.add( facebookButton );
        setTwo.add( myspaceButton );
        setTwo.add( twitterButton );
        setTwo.add( emailButton );
        setTwo.add( smsButton );
        
        add( setTwo );
    
        
        LabelField horizontalLabel = new LabelField( "The HorizontalButtonFieldSet will align the buttons horizontally with equal width." );
        horizontalLabel.setPadding(5, 5, 5, 5);
        add( horizontalLabel );
        
        HorizontalButtonFieldSet setThree = new HorizontalButtonFieldSet();
        
        ButtonField saveButton = new ButtonField( "Save" );
        ButtonField discardButton = new ButtonField( "Discard" );
        ButtonField cancelButton2 = new ButtonField( "Cancel" );
        
        saveButton.setMargin(  5, 5, 5 ,5 );
        discardButton.setMargin( 5, 5, 5 ,5 );
        cancelButton2.setMargin( 5, 5, 5 ,5 );
        
        setThree.add( saveButton );
        setThree.add( discardButton );
        setThree.add( cancelButton2 );
        
        add( setThree );
    
    
        LabelField horizontalLabel2 = new LabelField( "It can handle any number of children" );
        horizontalLabel2.setPadding(5, 5, 5, 5);
        add( horizontalLabel2 );
        
        HorizontalButtonFieldSet setFour = new HorizontalButtonFieldSet();
        
        ButtonField textButton = new ButtonField( "SMS" );
        ButtonField mmsButton = new ButtonField( "MMS" );
        
        textButton.setMargin(  5, 5, 5 ,5 );
        mmsButton.setMargin( 5, 5, 5 ,5 );
        
        setFour.add( textButton );
        setFour.add( mmsButton );
        
        add( setFour );
    
                
        
    }
}
