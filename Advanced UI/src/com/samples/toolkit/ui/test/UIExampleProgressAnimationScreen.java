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

public class UIExampleProgressAnimationScreen extends UIExampleScreen
{
    public UIExampleProgressAnimationScreen() {
        
        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT );
        setTitle( "Progress Animations" );
        
        EvenlySpacedHorizontalFieldManager spinners = new EvenlySpacedHorizontalFieldManager( USE_ALL_WIDTH );
        addSpinner( spinners, new ProgressAnimationField( Bitmap.getBitmapResource( "spinner2.png" ), 6, Field.FIELD_HCENTER ) );
        addSpinner( spinners, new ProgressAnimationField( Bitmap.getBitmapResource( "spinner.png" ), 5, Field.FIELD_HCENTER ) );
        
        add( spinners );
        
        LabelField exampleLabel = new LabelField( "The progress / spinner fields are created using a single image that has different frames in it. You pass in the image, and the number of equal size horizontal frames. It will take care of the animation." );
        exampleLabel.setMargin( 10, 10, 10, 10 );
        add( exampleLabel );
        
        BitmapField allFrames = new BitmapField( Bitmap.getBitmapResource( "spinner.png" ), Field.FIELD_HCENTER );
        allFrames.setMargin( 10, 10, 10, 10 );
        add( allFrames );
    }
    
    private void addSpinner( Manager parent, Field spinner )
    {
        spinner.setMargin( 15, 15, 15, 15 );
        parent.add( spinner );
    }
}




