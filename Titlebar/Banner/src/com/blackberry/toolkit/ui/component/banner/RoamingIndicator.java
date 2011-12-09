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

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.RadioStatusListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;

import com.blackberry.toolkit.device.util.SoftwareVersion;

/**
 * Field to display Active Calls as seen in the Banner
 * 
 * @author twindsor
 * @since 1.4 of Banner
 * @version 1.1 (Aug 2011)
 */
public class RoamingIndicator extends Field implements RadioStatusListener {

	private static int ROAM_IMAGE_WIDTH;
	private static int ROAM_IMAGE_HEIGHT;
	private static Bitmap ROAM_IMAGE;

	private boolean _isRoaming;

	private static String[] buckets = { SoftwareVersion.VER_421, SoftwareVersion.VER_460, SoftwareVersion.VER_700 };

	static {
		String device = DeviceInfo.getDeviceName();
		int bucket = SoftwareVersion.whichBucket(buckets);
		String roamImageName;
		switch (bucket) {
		case 0:
			ROAM_IMAGE_WIDTH = 14;
			ROAM_IMAGE_HEIGHT = 14;
			roamImageName = "roam.png";
			break;
		case 1:
			if (Display.getWidth() < 360) {
				// Smaller images for 8520 and 8530
				ROAM_IMAGE_WIDTH = 14;
				ROAM_IMAGE_HEIGHT = 14;
				roamImageName = "roam46small.png";
			} else {
				ROAM_IMAGE_WIDTH = 21;
				ROAM_IMAGE_HEIGHT = 21;
				roamImageName = "roam46.png";
			}
			break;
		case 2:
		default:
			if (device.startsWith("99") || device.startsWith("98")) {
				ROAM_IMAGE_WIDTH = 28;
				ROAM_IMAGE_HEIGHT = 28;
				roamImageName = "roam70.png";
			} else {
				ROAM_IMAGE_WIDTH = 21;
				ROAM_IMAGE_HEIGHT = 21;
				roamImageName = "roam70small.png";
			}
			break;
		}
		ROAM_IMAGE = EncodedImage.getEncodedImageResource(roamImageName).getBitmap();
	}

	/**
	 * Create the field
	 */
	public RoamingIndicator() {
		roamingCheck();
	}

	public int getPreferredHeight() {
		return ROAM_IMAGE_HEIGHT;
	}

	public int getPreferredWidth() {
		int width = 0;
		if (_isRoaming) {
			width += ROAM_IMAGE_WIDTH;
		}
		return width;
	}

	protected void layout(int width, int height) {
		// Calc width.
		width = Math.min(width, getPreferredWidth());

		// Calc height.
		height = Math.min(height, getPreferredHeight());

		// Set dimensions.
		setExtent(width, height);
	}

	protected void paint(Graphics graphics) {
		if (_isRoaming) {
			graphics.drawBitmap(0, 0, ROAM_IMAGE_WIDTH, ROAM_IMAGE_HEIGHT, ROAM_IMAGE, 0, 0);
		}
	}

	/**
	 * When displayed, start listening for events.
	 */
	protected void onDisplay() {
		UiApplication.getUiApplication().addRadioListener(RadioInfo.getSupportedWAFs(), this);
	}

	/**
	 * When this field is removed from the stack, remove the listeners.
	 */
	protected void onUndisplay() {
		UiApplication.getUiApplication().removeRadioListener(this);
	}

	public void baseStationChange() {
		// ignore
	}

	public void networkScanComplete(boolean success) {
		// ignore
	}

	public void networkServiceChange(int networkId, int service) {
		roamingCheck();
	}

	public void networkStarted(int networkId, int service) {
		roamingCheck();
	}

	public void networkStateChange(int state) {
		roamingCheck();
	}

	public void pdpStateChange(int apn, int state, int cause) {
		roamingCheck();
	}

	public void radioTurnedOff() {
		roamingCheck();
	}

	public void signalLevel(int level) {
		// ignore
	}

	private void roamingCheck() {
		int service = RadioInfo.getNetworkService();
		boolean roaming = isRoaming(service);
		if (roaming != _isRoaming) {
			_isRoaming = roaming;
			updateLayout();
		}
	}

	private boolean isRoaming(int service) {
		if ((service & RadioInfo.NETWORK_SERVICE_ROAMING) != 0) {
			if (!((service & RadioInfo.NETWORK_SERVICE_SUPPRESS_ROAMING) != 0)) {
				return true;
			}
		}
		return false;
	}

}
