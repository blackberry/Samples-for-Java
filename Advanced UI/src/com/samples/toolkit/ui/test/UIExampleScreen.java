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

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;


/**
 * The base screen for all the screens in the UIExample Project
 */
public class UIExampleScreen extends MainScreen 
{  
    UIExampleScreen( ) 
    {
        this( 0 );
    }
    
    UIExampleScreen( long style ) 
    {
        super( style );
    }
        
    public void setTitle( String title )
    {
        LabelField titleLabel = new LabelField( title );
        titleLabel.setPadding( 4, 0, 3, 4 );
        titleLabel.setFont( titleLabel.getFont().derive( Font.PLAIN, titleLabel.getFont().getHeight() + 2 ) );
        super.setTitle( titleLabel );
    }
}
