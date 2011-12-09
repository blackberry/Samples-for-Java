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
import net.rim.device.api.system.SystemListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;

import com.blackberry.toolkit.device.util.SoftwareVersion;

/**
 * Battery Status indicator Field. Shows the current battery status that appears
 * the same as the main banner display. Images and status levels are supported
 * from 4.2.0 through 7.0.0.
 * 
 * <b>Note:</b> Uses the Software Version Library to determine software levels.
 * 
 * @version 1.5 (Aug 2011)
 * @author twindsor
 * 
 * 
 */
public class BatteryStatus extends Field implements SystemListener {

	private static byte[] BATTERY_THRESHOLDS;
	private static int BATTERY_IMAGE_WIDTH;
	private static int BATTERY_IMAGE_HEIGHT;
	private static String BATTERY_IMAGE_NAME;
	private static Bitmap BATTERY_IMAGE;

	// Values copied from StandardTitleBar in BlackBerry SDK 6.0
	public static final int BATTERY_VISIBLE_LOW_OR_CHARGING = 3;
	public static final int BATTERY_VISIBLE_ALWAYS = 4;

	private int _currentLevel;
	private int _currentStatus;
	private boolean _batteryLow;
	private int _displaySetting;

	private static String[] buckets = { SoftwareVersion.VER_420, SoftwareVersion.VER_460, SoftwareVersion.VER_461, SoftwareVersion.VER_600,
			SoftwareVersion.VER_700 };

	/**
	 * Creates a Battery Status indicator and initializes it to use the images
	 * and status change levels based on the current software version.
	 * 
	 * Images included only support 4.2.0 and above.
	 */
	public BatteryStatus() {
		String device = DeviceInfo.getDeviceName();
		int bucket = SoftwareVersion.whichBucket(buckets);
		switch (bucket) {
		case 0:
			/*
			 * 4.2.0 through 4.5.0 uses this set of images and thresholds.
			 * Thresholds for each of the battery levels. Values mark the lower
			 * bound of the level.
			 */
			BATTERY_THRESHOLDS = new byte[] { 0, 6, 16, 26, 36, 46, 56, 66, 76, 86, 96 };
			BATTERY_IMAGE_WIDTH = 55;
			BATTERY_IMAGE_HEIGHT = 22;
			BATTERY_IMAGE_NAME = "batterylevels.png";
			break;
		case 1:
			// 4.6.0 uses the same thresholds, but new images
			BATTERY_THRESHOLDS = new byte[] { 0, 6, 10, 26, 36, 46, 56, 66, 76, 86, 96 };
			BATTERY_IMAGE_WIDTH = 44;
			BATTERY_IMAGE_HEIGHT = 20;
			BATTERY_IMAGE_NAME = "batterylevels46.png";
			break;
		case 2:
			// 4.6.1 uses the images from 4.6.0, but new thresholds
			BATTERY_THRESHOLDS = new byte[] { 0, 6, 10, 21, 31, 41, 51, 61, 71, 81, 91 };
			if (Display.getWidth() < 360) {
				// Smaller images for 8520 and 8530
				BATTERY_IMAGE_WIDTH = 30;
				BATTERY_IMAGE_HEIGHT = 14;
				BATTERY_IMAGE_NAME = "batterylevels46small.png";
			} else {
				BATTERY_IMAGE_WIDTH = 44;
				BATTERY_IMAGE_HEIGHT = 20;
				BATTERY_IMAGE_NAME = "batterylevels46.png";
			}
			break;
		case 3:
			// 6.0.0
			BATTERY_THRESHOLDS = new byte[] { 0, 6, 10, 21, 31, 41, 51, 61, 71, 81, 91 };
			if (device.startsWith("97") || device.startsWith("96")) {
				BATTERY_IMAGE_WIDTH = 44;
				BATTERY_IMAGE_HEIGHT = 22;
				BATTERY_IMAGE_NAME = "batterylevels60wide.png";
			} else {
				BATTERY_IMAGE_WIDTH = 33;
				BATTERY_IMAGE_HEIGHT = 16;
				BATTERY_IMAGE_NAME = "batterylevels60.png";
			}
			break;
		case 4:
		default:
			// 7.0.0
			BATTERY_THRESHOLDS = new byte[] { 0, 6, 10, 21, 31, 41, 51, 61, 71, 81, 91 };
			if (device.startsWith("98")) {
				BATTERY_IMAGE_WIDTH = 40;
				BATTERY_IMAGE_HEIGHT = 22;
				BATTERY_IMAGE_NAME = "batterylevels70thin.png";
			} else if (device.startsWith("99")) {
				BATTERY_IMAGE_WIDTH = 50;
				BATTERY_IMAGE_HEIGHT = 25;
				BATTERY_IMAGE_NAME = "batterylevels70.png";
			} else {
				BATTERY_IMAGE_WIDTH = 39;
				BATTERY_IMAGE_HEIGHT = 19;
				BATTERY_IMAGE_NAME = "batterylevels70small.png";
			}
		}
		BATTERY_IMAGE = EncodedImage.getEncodedImageResource(BATTERY_IMAGE_NAME).getBitmap();

		_currentStatus = DeviceInfo.getBatteryStatus();
		_currentLevel = DeviceInfo.getBatteryLevel();
	}

	protected void layout(int width, int height) {
		// Calc width.
		width = Math.min(width, getPreferredWidth());

		// Calc height.
		height = Math.min(height, getPreferredHeight());

		// Set dimensions.
		setExtent(width, height);

	}

	/**
	 * Return the desired height, which is the height of the graphic
	 */
	public int getPreferredHeight() {
		// if (checkDisplay()) {
		return BATTERY_IMAGE_HEIGHT;
		// } else {
		// return 0;
		// }
	}

	/**
	 * Return the desired width, which is the width of the graphic
	 */
	public int getPreferredWidth() {
		if (checkDisplay()) {
			return BATTERY_IMAGE_WIDTH;
		} else {
			return 0;
		}
	}

	/**
	 * Set the Battery display setting
	 * 
	 * @param setting
	 *            one of BATTERY_ALWAYS, BATTERY_LOW_OR_CHARGING, or
	 *            BATTERY_DEFAULT.
	 * @since 1.2
	 */
	public void setDisplaySetting(int setting) {
		if (setting == BATTERY_VISIBLE_ALWAYS || setting == BATTERY_VISIBLE_LOW_OR_CHARGING) {
			if (_displaySetting != setting) {
				_displaySetting = setting;
				updateLayout();
			}
		}
	}

	protected void paint(Graphics graphics) {
		if (!checkDisplay()) {
			// don't paint anything if this is off
			return;
		}
		int left = getBatteryImageId(_currentLevel) * BATTERY_IMAGE_WIDTH;
		// Levels are in 2 rows, with the powered images on the second row.
		int top = 0;
		if (usePoweredGraphic()) {
			top = BATTERY_IMAGE_HEIGHT;
		}
		graphics.drawBitmap(0, 0, BATTERY_IMAGE_WIDTH, BATTERY_IMAGE_HEIGHT, BATTERY_IMAGE, left, top);
	}

	/**
	 * Get the current battery image id for painting. The battery image to be
	 * painted is one of 11 possible icons within the file. Based on the battery
	 * level, this will return the appropriate image id.
	 * 
	 * @param batteryLevel
	 *            the current battery level
	 * @return the id from 0-10 which corresponds to the image to use.
	 */
	protected int getBatteryImageId(int batteryLevel) {
		for (int i = BATTERY_THRESHOLDS.length - 1; i >= 0; i--) {
			if (batteryLevel >= BATTERY_THRESHOLDS[i]) {
				// Found the index to use
				return i;
			}
		}
		// Battery level is unknown
		return 11;
	}

	/**
	 * Check whether the device is being charged. If so, the image should show
	 * the power symbol.
	 * 
	 * @return true if the device is being charged.
	 */
	protected boolean usePoweredGraphic() {
		// May not be necessary to actually test all these values
		if ((_currentStatus & (DeviceInfo.BSTAT_CHARGING | DeviceInfo.BSTAT_AC_CONTACTS | DeviceInfo.BSTAT_IS_USING_EXTERNAL_POWER)) > 0) {
			return true;
		}
		return false;

	}

	/**
	 * Check the display settings whether this should be displayed or not
	 * 
	 * @return true if the setting is BATTERY_ALWAYS or the device is Charging
	 * @since 1.2
	 */
	protected boolean checkDisplay() {
		if (_displaySetting == BATTERY_VISIBLE_ALWAYS) {
			return true;
		} else {
			// Low or Charging state
			return (_batteryLow || usePoweredGraphic());
		}
	}

	/**
	 * When displayed, start listening for battery change events.
	 */
	protected void onDisplay() {
		UiApplication.getUiApplication().addSystemListener(this);
	}

	/**
	 * When this field is removed from the stack, remove the listener.
	 */
	protected void onUndisplay() {
		UiApplication.getUiApplication().removeSystemListener(this);
	}

	/**
	 * Notify that battery is now good.
	 * 
	 * @since 1.2
	 */
	public void batteryGood() {
		_batteryLow = false;
		if (_displaySetting == BATTERY_VISIBLE_LOW_OR_CHARGING) {
			updateLayout();
		}
	}

	/**
	 * Notify that battery is low.
	 * 
	 * @since 1.2
	 */
	public void batteryLow() {
		_batteryLow = true;
		if (_displaySetting == BATTERY_VISIBLE_LOW_OR_CHARGING) {
			updateLayout();
		}
	}

	/**
	 * Repaint when Battery status changes
	 */
	public void batteryStatusChange(int status) {
		if ((status & DeviceInfo.BSTAT_LEVEL_CHANGED) != 0) {
			int level = DeviceInfo.getBatteryLevel();
			if (getBatteryImageId(_currentLevel) != getBatteryImageId(level)) {
				_currentLevel = level;
				invalidate();
			}
		}
		if (status != _currentStatus) {
			_currentStatus = status;
			if (_displaySetting == BATTERY_VISIBLE_LOW_OR_CHARGING) {
				updateLayout();
			} else {
				invalidate();
			}
		}
	}

	/**
	 * Ignored
	 */
	public void powerOff() {

	}

	/**
	 * Ignored
	 */
	public void powerUp() {

	}

}
