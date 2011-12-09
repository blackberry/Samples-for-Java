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

package com.samples.toolkit.ui.component;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;


public class ColoredLabelField extends LabelField
{ 	
	int _foregroundColor;
	int _foregroundColorFocus;

	public ColoredLabelField( String label, int foregroundColor, long style ) {
		this( label, foregroundColor,foregroundColor, style );
	}
	
	public ColoredLabelField( String label, int foregroundColor, int foregroundColorFocus, long style ) {
		super( label, style );
		_foregroundColor = foregroundColor;
		_foregroundColorFocus = foregroundColorFocus;
	}

	protected void paint( Graphics g ) 
    {
        int oldColour = g.getColor();
        try {
            g.setColor( g.isDrawingStyleSet( Graphics.DRAWSTYLE_FOCUS ) ? _foregroundColorFocus : _foregroundColor );
            super.paint( g );
        } finally {
            g.setColor( oldColour );
        }
    }
}