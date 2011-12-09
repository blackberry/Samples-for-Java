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

package com.blackberry.toolkit.ui.component.banner;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

/**
 * Basic LabelField that uses the appropriate font and color for the Banner.
 * 
 * @author twindsor
 * @since 1.1
 * @version 1.2 (Aug 2011)
 */
public class Title extends LabelField {

	public Title(String text, long style) {
		super(text, style);
		setFont(BannerFont.getFont());
	}

	protected void paint(Graphics graphics) {
		graphics.setColor(BannerFont.getFontColor()); // Banner font color
		super.paint(graphics);
	}
}
