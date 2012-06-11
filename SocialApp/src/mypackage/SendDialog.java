/*
* Copyright (c) 2012 Research In Motion Limited.
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

package mypackage;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;

public class SendDialog extends Dialog{
	public SendDialog(int type, String message, int defaultChoice, Bitmap bitmap){
		super(type, message, defaultChoice, bitmap, Dialog.NO_HORIZONTAL_SCROLL);		
		setBackground(BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 200));
		setBorder(BorderFactory.createBitmapBorder(new XYEdges(9, 9, 9, 9), Bitmap.getBitmapResource("border.png")));		
	}

	public static void inform(int type, String message, int defaultChoice, Bitmap bitmap){
		new SendDialog(type, message, defaultChoice, bitmap).show();
	} 
}
