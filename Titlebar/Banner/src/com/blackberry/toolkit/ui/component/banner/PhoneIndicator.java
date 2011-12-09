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

import java.lang.ref.WeakReference;

import net.rim.blackberry.api.phone.Phone;
import net.rim.blackberry.api.phone.PhoneCall;
import net.rim.blackberry.api.phone.PhoneListener;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;

import com.blackberry.toolkit.device.util.SoftwareVersion;

/**
 * Field to display Active Calls as seen in the Banner
 * 
 * @author twindsor
 * @since 1.4 of Banner
 * @version 1.2 (Aug 2011)
 */
public class PhoneIndicator extends Field implements PhoneListener {

	private static int PHONE_IMAGE_WIDTH;
	private static int PHONE_IMAGE_HEIGHT;
	private static Bitmap PHONE_IMAGE;

	private boolean _phoneIsActive;

	// keep a reference to the UI app
	private WeakReference _app = new WeakReference(UiApplication.getUiApplication());

	private static String[] buckets = { SoftwareVersion.VER_421, SoftwareVersion.VER_460, SoftwareVersion.VER_600, SoftwareVersion.VER_700 };

	static {
		String device = DeviceInfo.getDeviceName();
		int bucket = SoftwareVersion.whichBucket(buckets);
		String phoneImageName;
		switch (bucket) {
		case 0:
			PHONE_IMAGE_WIDTH = 14;
			PHONE_IMAGE_HEIGHT = 14;
			phoneImageName = "phone.png";
			break;
		case 1:
			if (Display.getWidth() < 360) {
				// Smaller images for 8520 and 8530
				PHONE_IMAGE_WIDTH = 16;
				PHONE_IMAGE_HEIGHT = 16;
				phoneImageName = "phone46small.png";
			} else {
				PHONE_IMAGE_WIDTH = 23;
				PHONE_IMAGE_HEIGHT = 23;
				phoneImageName = "phone46.png";
			}
			break;
		case 2:
			if (device.startsWith("97") || device.startsWith("96")) {
				PHONE_IMAGE_WIDTH = 26;
				PHONE_IMAGE_HEIGHT = 26;
				phoneImageName = "phone60wide.png";
			} else {
				PHONE_IMAGE_WIDTH = 21;
				PHONE_IMAGE_HEIGHT = 21;
				phoneImageName = "phone60.png";
			}
			break;
		case 3:
		default:
			if (device.startsWith("98")) {
				PHONE_IMAGE_WIDTH = 25;
				PHONE_IMAGE_HEIGHT = 25;
				phoneImageName = "phone70thin.png";
			} else if (device.startsWith("99")) {
				PHONE_IMAGE_WIDTH = 22;
				PHONE_IMAGE_HEIGHT = 22;
				phoneImageName = "phone70wide.png";
			} else {
				PHONE_IMAGE_WIDTH = 22;
				PHONE_IMAGE_HEIGHT = 22;
				phoneImageName = "phone70small.png";
			}
		}
		PHONE_IMAGE = EncodedImage.getEncodedImageResource(phoneImageName).getBitmap();
	}

	/**
	 * Create the field
	 */
	public PhoneIndicator() {
		try {
			PhoneCall call = Phone.getActiveCall();
			if (call != null) {
				_phoneIsActive = true;
			}
		} catch (Exception e) {
			// carry on
		}
	}

	public int getPreferredHeight() {
		return PHONE_IMAGE_HEIGHT;
	}

	public int getPreferredWidth() {
		int width = 0;
		if (_phoneIsActive) {
			width += PHONE_IMAGE_WIDTH;
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
		if (_phoneIsActive) {
			graphics.drawBitmap(0, 0, PHONE_IMAGE_WIDTH, PHONE_IMAGE_HEIGHT, PHONE_IMAGE, 0, 0);
		}
	}

	private class CallStarted implements Runnable {
		public void run() {
			_phoneIsActive = true;
			updateLayout();
		}
	}

	private class CallEnded implements Runnable {
		public void run() {
			_phoneIsActive = false;
			updateLayout();
		}
	}

	/**
	 * When displayed, start listening for events.
	 */
	protected void onDisplay() {
		Phone.addPhoneListener(this);
	}

	/**
	 * When this field is removed from the stack, remove the listener.
	 */
	protected void onUndisplay() {
		Phone.removePhoneListener(this);
	}

	private void invokeLater(Runnable runnable) {
		if (_app != null) {
			UiApplication app = (UiApplication) _app.get();
			System.out.println(app.toString());
			if (app != null) {
				app.invokeLater(runnable);
			}
		}
	}

	public void callAdded(int arg0) {
		// ignore
	}

	public void callAnswered(int callId) {
		// ignore
	}

	public void callConferenceCallEstablished(int callId) {
		// ignore
	}

	public void callConnected(int callId) {
		invokeLater(new CallStarted());
	}

	public void callDirectConnectConnected(int callId) {
		// ignore
	}

	public void callDirectConnectDisconnected(int callId) {
		// ignore
	}

	public void callDisconnected(int callId) {
		invokeLater(new CallEnded());
	}

	public void callEndedByUser(int callId) {
		// ignore
	}

	public void callFailed(int callId, int reason) {
		// ignore
	}

	public void callHeld(int callId) {
		// ignore
	}

	public void callIncoming(int callId) {
		// ignore
	}

	public void callInitiated(int callid) {
		// ignore
	}

	public void callRemoved(int callId) {
		// ignore
	}

	public void callResumed(int callId) {
		// ignore
	}

	public void callWaiting(int callid) {
		// ignore
	}

	public void conferenceCallDisconnected(int callId) {
		// ignore
	}

}
