
package nfc.sample.peer2peer.buttons;
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

public class MsbState {

	public static final int DEFAULT_STATE=0;
	
	private int state_id; // zero is the default state
	private String state_description; // e.g. "Service is currently ON"
	private String state_label; // for display on buttons e.g. "ON"

	private Bitmap bmp_unfocused;
	private Bitmap bmp_focused;
	private Bitmap bmp_clicked;
	private Bitmap bmp_unclicked;

	public MsbState(int state_id, String state_description, String state_label) {
		this.state_id = state_id;
		this.state_description = state_description;
		this.state_label = state_label;
	}

	public boolean equals(Object obj) {
		if (obj instanceof MsbState) {
			if (((MsbState) obj).getState_id() == this.state_id) {
				return true;
			}
		}
		return false;
	}

	public int getState_id() {
		return state_id;
	}

	public void setState_id(int state_id) {
		this.state_id = state_id;
	}

	public String getState_description() {
		return state_description;
	}

	public void setState_description(String state_description) {
		this.state_description = state_description;
	}

	public String getState_label() {
		return state_label;
	}

	public void setState_label(String state_label) {
		this.state_label = state_label;
	}

	public Bitmap getbmp_unfocused() {
		return bmp_unfocused;
	}

	public void setbmp_unfocused(Bitmap bmp_unfocused) {
		this.bmp_unfocused = bmp_unfocused;
	}

	public Bitmap getbmp_focused() {
		return bmp_focused;
	}

	public void setbmp_focused(Bitmap bmp_focused) {
		this.bmp_focused = bmp_focused;
	}

	public Bitmap getbmp_clicked() {
		return bmp_clicked;
	}

	public void setbmp_clicked(Bitmap bmp_clicked) {
		this.bmp_clicked = bmp_clicked;
	}

	public Bitmap getbmp_unclicked() {
		return bmp_unclicked;
	}

	public void setbmp_unclicked(Bitmap bmp_unclicked) {
		this.bmp_unclicked = bmp_unclicked;
	}

}
