//#preprocess

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

package com.samples.toolkit.ui;

import net.rim.device.api.ui.*;

//#ifndef VER_4.1.0 | VER_4.2.0 | VER_4.2.1 | VER_4.3.0 | VER_4.5.0
import net.rim.device.api.ui.decor.*;
//#endif


public class FieldDimensionUtilities
{
    private FieldDimensionUtilities() { }
    
    public static int getBorderWidth( Field field )
    {
        int width = 0;

      //#ifdef VER_4.1.0 | VER_4.2.0 | VER_4.2.1 | VER_4.3.0 | VER_4.5.0
        width = field.getWidth() - field.getContentWidth() - field.getPaddingLeft() - field.getPaddingRight();
      //#else
        Border border = field.getBorder();
        if( border != null ) {
            width = border.getLeft() + border.getRight();
        }
      //#endif
        return width;
    }
    
    public static int getBorderHeight( Field field )
    {
        int height = 0;
        
      //#ifdef VER_4.1.0 | VER_4.2.0 | VER_4.2.1 | VER_4.3.0 | VER_4.5.0
        height = field.getWidth() - field.getContentHeight() - field.getPaddingTop() - field.getPaddingBottom();
      //#else
        Border border = field.getBorder();
        if( border != null ) {
            height = border.getTop() + border.getBottom();
        }
      //#endif
        return height;
    }

    public static int getBorderAndPaddingWidth( Field field )
    {
        int width = 0;
      //#ifdef VER_4.1.0 | VER_4.2.0 | VER_4.2.1 | VER_4.3.0 | VER_4.5.0
        width = field.getWidth() - field.getContentWidth();
      //#else
        width = field.getPaddingLeft() + field.getPaddingRight();
        Border border = field.getBorder();
        if( border != null ) {
            width += border.getLeft() + border.getRight();
        }
      //#endif
        return width;
    }

    public static int getBorderAndPaddingHeight( Field field )
    {
        int height = 0;
      //#ifdef VER_4.1.0 | VER_4.2.0 | VER_4.2.1 | VER_4.3.0 | VER_4.5.0
        height = field.getHeight() - field.getContentHeight();
      //#else
        height = field.getPaddingTop() + field.getPaddingBottom();
        Border border = field.getBorder();
        if( border != null ) {
            height += border.getTop() + border.getBottom();
        }
      //#endif
        return height;
    }

}
