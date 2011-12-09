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


import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;

import com.samples.toolkit.ui.component.*;
import com.samples.toolkit.ui.container.*;


public class UIExampleRatingScreen extends UIExampleScreen
{
    public UIExampleRatingScreen() {
        
        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT );
        setTitle( "RatingField Examples" );
        
        int labelColor = 0xFFFFFF; // white
        
        NegativeMarginVerticalFieldManager ratingManager = new NegativeMarginVerticalFieldManager( VERTICAL_SCROLL | USE_ALL_WIDTH | USE_ALL_HEIGHT ) {
            protected void paintBackground( Graphics g )
            {
                int oldColor = g.getColor();
                try {
                    g.setColor( 0x222222 );
                    g.fillRect( 0, getVerticalScroll(), getWidth(), getHeight() );
                } finally {
                    g.setColor( oldColor );
                }
            }   
        };

        
        ColoredLabelField labelOne = new ColoredLabelField( "Main Course", labelColor, Field.FIELD_HCENTER );
        labelOne.setMargin( 25, 0,7, 0 );
        
        RatingField rating = new RatingField( Bitmap.getBitmapResource( "rating_star.png" )
                                            , Bitmap.getBitmapResource( "rating_dot.png" )
                                            , Bitmap.getBitmapResource( "rating_star_focus.png" )
                                            , Bitmap.getBitmapResource( "rating_dot_focus.png" )
                                            , 5, 4, Field.FIELD_HCENTER );
        rating.setMargin( 7, 0, 15, 0 );
        ratingManager.add( labelOne );
        ratingManager.add( rating );
        
        
        ColoredLabelField labelTwo = new ColoredLabelField( "Wine", labelColor, Field.FIELD_HCENTER );
        labelTwo.setMargin( 25, 0, 7, 0 );
        
        RatingField rating2 = new RatingField( Bitmap.getBitmapResource( "rating_star.png" )
                                            , Bitmap.getBitmapResource( "rating_dot.png" )
                                            , Bitmap.getBitmapResource( "rating_star_focus.png" )
                                            , Bitmap.getBitmapResource( "rating_dot_focus.png" )
                                            , 5, 1, Field.FIELD_HCENTER );
        rating2.setMargin( 7, 0, 15, 0 );
        ratingManager.add( labelTwo );
        ratingManager.add( rating2 );
        
        
        ColoredLabelField labelThree = new ColoredLabelField( "Dessert", labelColor, Field.FIELD_HCENTER );
        labelThree.setMargin( 25, 0, 7, 0 );
        
        RatingField rating3 = new RatingField( Bitmap.getBitmapResource( "rating_star.png" )
                                            , Bitmap.getBitmapResource( "rating_dot.png" )
                                            , Bitmap.getBitmapResource( "rating_star_focus.png" )
                                            , Bitmap.getBitmapResource( "rating_dot_focus.png" )
                                            , 5, 5, Field.FIELD_HCENTER );
        rating3.setMargin( 7, 0, 15, 0 );
        ratingManager.add( labelThree );
        ratingManager.add( rating3 );
        
        
        VerticalButtonFieldSet buttonManager = new VerticalButtonFieldSet( USE_ALL_WIDTH );
        buttonManager.add( new ButtonField( "Save Review" ) );
        buttonManager.setMargin( 30, 30, 25, 30 );
        ratingManager.add( buttonManager );
        
        add( ratingManager );
        
    }
	
}

