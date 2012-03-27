package nfc.sample.nfctransaction.ui;
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
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;

public class GraphicalButton extends BitmapField {
	
	private boolean is_focused = false;
	private Bitmap unfocused;
	private Bitmap focused;
	private int state=0;
	
	public GraphicalButton(Bitmap unfocused, Bitmap focused, boolean is_focused, long style) {
		super(unfocused,style);
		this.unfocused = unfocused;
		this.focused = focused;
		this.is_focused = is_focused;		
	}
	
	public void paint(Graphics g) {
		if (is_focused) {
			g.drawBitmap(0, 0, focused.getWidth(), focused.getHeight(), focused, 0, 0);
		} else {
			g.drawBitmap(0, 0, unfocused.getWidth(), unfocused.getHeight(), unfocused, 0, 0);
		}
	}
	
	public void setFocused(boolean focus_state) {
		is_focused = focus_state;
	}

	public boolean isFocused() {
		return is_focused;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
