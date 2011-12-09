//#preprocessor

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
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;

import com.blackberry.toolkit.device.util.SoftwareVersion;
import com.blackberry.toolkit.ui.images.BitmapFile;

//#ifdef BlackBerrySDK4.2.1 | BlackBerrySDK4.3.0 | BlackBerrySDK4.5.0 | BlackBerrySDK4.6.0 | BlackBerrySDK4.6.1 | BlackBerrySDK4.7.0
import com.blackberry.toolkit.ui.images.ImageManipulator;

//#endif

/**
 * Class for displaying the banner fields:
 * <ul>
 * <li>Battery Status</li>
 * <li>Time</li>
 * <li>Notifications</li>
 * <li>Wireless Status</li>
 * <li>Active Phone call</li>
 * <li>Roaming</li>
 * </ul>
 * 
 * @author twindsor
 * 
 * @version 1.6 (Aug 2011)
 */
public class Banner extends Manager {

	public static final int BATTERY_VISIBLE_LOW_OR_CHARGING = BatteryStatus.BATTERY_VISIBLE_LOW_OR_CHARGING;
	public static final int BATTERY_VISIBLE_ALWAYS = BatteryStatus.BATTERY_VISIBLE_ALWAYS;

	// Values copied from BlackBerry SDK 6.0
	public static final int PROPERTY_BATTERY_VISIBILITY = 0;
	public static final int PROPERTY_WIFI_VISIBILITY = 1;
	public static final int PROPERTY_CELLULAR_VISIBILITY = 2;

	public static final int PROPERTY_VALUE_DEFAULT = 0;
	public static final int PROPERTY_VALUE_ON = 1;
	public static final int PROPERTY_VALUE_OFF = 2;

	private int batteryVisibility = PROPERTY_VALUE_DEFAULT;
	private int wifiVisibility = PROPERTY_VALUE_DEFAULT;
	private int cellularVisibility = PROPERTY_VALUE_DEFAULT;
	private boolean signalIsAdded = false;

	private final BatteryStatus battery;
	private Notifications notifications;
	private TimeDisplay timeDisplay;
	private WirelessStatus wirelessStatus;
	private WirelessStatus wifi;
	private Field title;
	private BitmapField icon;
	private PhoneIndicator phone;
	private RoamingIndicator roam;

	private int hPadding = 0;
	private int vPadding = 0;
	private int interiorPadding = 0;

	private static String[] buckets = { SoftwareVersion.VER_421, SoftwareVersion.VER_460, SoftwareVersion.VER_700 };
	private static boolean isDimension = false;
	private static Bitmap backgroundImage;
	private static boolean BB7 = false;

	static {
		int bucket = SoftwareVersion.whichBucket(buckets);
		switch (bucket) {
		case 0:
			isDimension = true;
			backgroundImage = EncodedImage.getEncodedImageResource("background.png").getBitmap();
			break;
		case 1:
			break;
		case 2:
		default:
			BB7 = true;
			break;
		}
	}

	/**
	 * Create a default Banner
	 */
	public Banner() {
		super(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL | Manager.FIELD_VCENTER | Manager.USE_ALL_WIDTH);
		addTitle("");
		battery = new BatteryStatus();
		add(battery);
		phone = new PhoneIndicator();
		add(phone);
		// Portrait and Storm devices have a different layout than wide devices,
		// except BB7
		String deviceName = DeviceInfo.getDeviceName();
		boolean landscape = Display.getWidth() > Display.getHeight();
		if (!BB7 && landscape && deviceName != null && !(deviceName.startsWith("95"))) {
			vPadding = 1;
			hPadding = 9;
			interiorPadding = 4;
		} else {
			vPadding = 1;
			hPadding = 1;
			interiorPadding = 4;
		}
	}

	/**
	 * Add an icon using the file URL. Will scale the image if it is larger than
	 * the space available.
	 * 
	 * @param iconFile
	 *            full URL to an icon on the filesystem.
	 * @since 1.3
	 */
	public void addIcon(String iconFile) {
		Bitmap image = (new BitmapFile(iconFile)).getBitmap();
		if (image != null) {
			addIcon(image);
		}
	}

	/**
	 * Add an icon using the Bitmap. Will scale the image if it is larger than
	 * the space available.
	 * 
	 * @param icon
	 */
	public void addIcon(Bitmap icon) {
		if (icon == null) {
			return;
			// TODO does the API throw an Exception?
		}
		// Scale to same height as the other elements.
		Bitmap image = scaleImage(icon);

		if (this.icon != null) {
			this.icon.setBitmap(image);
		} else {
			this.icon = new BitmapField(image);
		}
		add(this.icon);
	}

	/**
	 * Scale the image down if it's larger than the title bar size
	 * 
	 * @param image
	 *            the original image
	 * @return the scaled image
	 */
	private Bitmap scaleImage(Bitmap image) {
		int dimension = BannerFont.getFontHeight();
		if (!isDimension) {
			dimension -= 2; // old theme is better with full size image.
		}
		if (image.getHeight() > dimension) {
			Bitmap scaled = new Bitmap(dimension, dimension);
//#ifdef BlackBerrySDK4.2.1 | BlackBerrySDK4.3.0 | BlackBerrySDK4.5.0 | BlackBerrySDK4.6.0 | BlackBerrySDK4.6.1 | BlackBerrySDK4.7.0
			ImageManipulator manip = new ImageManipulator(image);
			manip.scaleInto(scaled, ImageManipulator.FILTER_BILINEAR, ImageManipulator.SCALE_TO_FIT);
//#else
			image.scaleInto(scaled, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FIT);
//#endif
			return scaled;
		}
		return image;

	}

	/**
	 * Remove the Icon
	 */
	public void removeIcon() {
		if (this.icon != null) {
			delete(icon);
		}
		icon = null;
	}

	/**
	 * Add a title using the String given
	 * 
	 * @param title
	 * @since 1.2
	 */
	public void addTitle(String title) {
		if (this.title instanceof LabelField) {
			LabelField titleLabel = (LabelField) this.title;
			titleLabel.setText(title);
		} else {
			if (this.title != null) {
				this.delete(this.title);
			}
			this.title = new Title(title, Title.ELLIPSIS);
			add(this.title);
		}
	}

	/**
	 * Remove the title if it exists
	 * 
	 * @since 1.2
	 */
	public void removeTitle() {
		if (this.title != null) {
			this.delete(this.title);
		}
		this.title = null;
	}

	/**
	 * Add the clock display
	 * 
	 * @since 1.2
	 */
	public void addClock() {
		if (timeDisplay == null) {
			timeDisplay = new TimeDisplay();
			add(timeDisplay);
		}
	}

	/**
	 * Remove the clock if it exists
	 * 
	 * @since 1.2
	 */
	public void removeClock() {
		if (timeDisplay != null) {
			delete(timeDisplay);
			timeDisplay = null;
		}
	}

	/**
	 * Add display of new mail messages
	 * 
	 * @since 1.2
	 */
	public void addNotifications() {
		if (notifications == null) {
			notifications = new Notifications();
			add(notifications);
		}
	}

	/**
	 * Remove display of the mail messages
	 * 
	 * @since 1.2
	 */
	public void removeNotifications() {
		if (notifications != null) {
			delete(notifications);
			notifications = null;
		}
	}

	/**
	 * Add the Wireless signal indicator, using the default display options.
	 * 
	 * @since 1.2
	 */
	private void addCellularIndicator() {
		if (wirelessStatus == null) {
			wirelessStatus = new WirelessStatus();
			add(wirelessStatus);
		}
	}

	/**
	 * Remove the signal indicator if it exists.
	 * 
	 * @since 1.2
	 */
	private void removeCellularIndicator() {
		if (wirelessStatus != null) {
			delete(wirelessStatus);
			wirelessStatus = null;
		}
	}

	/**
	 * Add the Signal Indicators
	 */
	public void addSignalIndicator() {
		signalIsAdded = true;
		addCellularIndicator();
		addRoamingIndicator();
		if (cellularVisibility == PROPERTY_VALUE_ON || cellularVisibility == PROPERTY_VALUE_DEFAULT) {
			wirelessStatus.setDisplaySetting(WirelessStatus.DISPLAY_FULL);
		} else {
			wirelessStatus.setDisplaySetting(WirelessStatus.DISPLAY_SIGNAL);
		}
		if (wifiVisibility == PROPERTY_VALUE_ON || wifiVisibility == PROPERTY_VALUE_DEFAULT) {
			addWifiIndicator();
		}
	}

	/**
	 * Remove the Signal Indicators
	 */
	public void removeSignalIndicator() {
		signalIsAdded = false;
		removeCellularIndicator();
		removeRoamingIndicator();
		removeWifiIndicator();
	}

	/**
	 * Add the Wifi signal indicator
	 * 
	 * @since 1.2
	 */
	private void addWifiIndicator() {
		if (wifi == null && ((RadioInfo.getSupportedWAFs() & RadioInfo.WAF_WLAN) != 0)) {
			// Only add Wifi if a Wifi radio is present (this is important for
			// old devices).
			wifi = new WirelessStatus(WirelessStatus.DISPLAY_WIFI, false);
			add(wifi);
		}
	}

	/**
	 * Remove the Wifi signal indicator if it exists.
	 * 
	 * @since 1.2
	 */
	private void removeWifiIndicator() {
		if (wifi != null) {
			delete(wifi);
			wifi = null;
		}
	}

	/**
	 * Add the Roaming indicator
	 * 
	 * @since 1.4
	 */
	private void addRoamingIndicator() {
		if (roam == null) {
			roam = new RoamingIndicator();
			add(roam);
		}
	}

	/**
	 * Remove the Roaming indicator if it exists.
	 * 
	 * @since 1.4
	 */
	private void removeRoamingIndicator() {
		if (roam != null) {
			delete(roam);
			roam = null;
		}
	}

	/**
	 * Set the battery display options
	 * 
	 * @param propertyKey
	 *            One of PROPERTY_BATTERY_VISIBILITY,
	 *            PROPERTY_CELLULAR_VISIBILITY, or PROPERTY_WIFI_VISIBILITY
	 * @param propertyValue
	 *            one of PROPERTY_VALUE_ON or PROPERTY_VALUE_OFF
	 * @throws IllegalArgumentException
	 *             if propertyKey or propertyValue is not valid
	 * @since 1.3
	 */
	public void setPropertyValue(int propertyKey, int propertyValue) throws IllegalArgumentException {
		boolean argumentsOkay = false;
		if (propertyKey == PROPERTY_BATTERY_VISIBILITY
				&& (propertyValue == PROPERTY_VALUE_DEFAULT || propertyValue == BATTERY_VISIBLE_ALWAYS || propertyValue == BATTERY_VISIBLE_LOW_OR_CHARGING)) {
			argumentsOkay = true;
		} else if ((propertyKey == PROPERTY_CELLULAR_VISIBILITY || propertyKey == PROPERTY_WIFI_VISIBILITY)
				&& (propertyValue == PROPERTY_VALUE_OFF || propertyValue == PROPERTY_VALUE_ON || propertyValue == PROPERTY_VALUE_DEFAULT)) {
			argumentsOkay = true;
		}
		if (!argumentsOkay) {
			// TODO Duplicate exception message
			throw new IllegalArgumentException();
		}
		switch (propertyKey) {
		case PROPERTY_BATTERY_VISIBILITY:
			batteryVisibility = propertyValue;
			if (battery != null) {
				battery.setDisplaySetting(batteryVisibility);
			}
			break;
		case PROPERTY_CELLULAR_VISIBILITY:
			cellularVisibility = propertyValue;
			if (signalIsAdded == true) {
				if (cellularVisibility == PROPERTY_VALUE_ON || cellularVisibility == PROPERTY_VALUE_DEFAULT) {
					wirelessStatus.setDisplaySetting(WirelessStatus.DISPLAY_FULL);
				} else if (cellularVisibility == PROPERTY_VALUE_OFF) {
					wirelessStatus.setDisplaySetting(WirelessStatus.DISPLAY_SIGNAL);
				}
			}
			break;
		case PROPERTY_WIFI_VISIBILITY:
			wifiVisibility = propertyValue;
			if (signalIsAdded == true) {
				if (wifiVisibility == PROPERTY_VALUE_ON || wifiVisibility == PROPERTY_VALUE_DEFAULT) {
					addWifiIndicator();
				} else if (wifiVisibility == PROPERTY_VALUE_OFF) {
					removeWifiIndicator();
				}
			}
			break;
		}
	}

	/**
	 * Get the value of the property
	 * 
	 * @param propertyKey
	 * @return the value set for the specified property
	 * @throws IllegalArgumentException
	 *             if propertyKey is not valid
	 */
	public int getPropertyValue(int propertyKey) {
		switch (propertyKey) {
		case PROPERTY_BATTERY_VISIBILITY:
			return batteryVisibility;
		case PROPERTY_CELLULAR_VISIBILITY:
			return cellularVisibility;
		case PROPERTY_WIFI_VISIBILITY:
			return wifiVisibility;
		default:
			// TODO Duplicate exception message
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Layout the banner
	 * 
	 * @param width
	 * @param height
	 */
	protected void sublayout(int width, int height) {
		int fullWidth = width;

		width -= (hPadding << 1);
		int availableWidth = width;
		// each field reduces our available width
		int maxHeight = 0; // Calculate max height as we go
		int fieldCount = getFieldCount();
		Field child;
		for (int i = 0; i < fieldCount; i++) {
			child = getField(i);
			if (hasField(title) && child == title) {
				continue;
				// Layout title last, separately
			}
			layoutChild(child, availableWidth, height);
			XYRect childExtent = child.getExtent();
			availableWidth -= childExtent.width;
			if (hasField(icon) && child == icon) {
				// skip the interior padding loss.
			} else {
				availableWidth -= interiorPadding;
			}
			maxHeight = Math.max(maxHeight, childExtent.height);
		}
		// Layout Title with remaining space
		if (hasField(title)) {
			child = title;
			layoutChild(child, availableWidth, height);
			XYRect childExtent = child.getExtent();
			availableWidth -= childExtent.width + interiorPadding;
			maxHeight = Math.max(maxHeight, childExtent.height);
		}
		// Set Positions
		if (hasField(icon)) {
			int childY = determineTop(vPadding, icon.getExtent().height, maxHeight, DrawStyle.VCENTER);
			setPositionChild(icon, hPadding, childY);
		}
		if (hasField(title)) {
			int childX = determineLeft(title, width, icon, DrawStyle.LEFT);
			int childY = determineTop(vPadding, title.getExtent().height, maxHeight, DrawStyle.VCENTER);
			setPositionChild(title, childX, childY);
		}
		if (hasField(timeDisplay)) {
			int childX = determineLeft(timeDisplay, width, new Field[] { icon, title }, DrawStyle.LEFT);
			int childY = determineTop(vPadding, timeDisplay.getExtent().height, maxHeight, DrawStyle.VCENTER);
			setPositionChild(timeDisplay, childX, childY);
		}
		if (hasField(wirelessStatus)) {
			int childX = determineLeft(wirelessStatus, width, new Field[0], DrawStyle.RIGHT);
			int drawStyle = DrawStyle.BOTTOM;
			if (isDimension) {
				drawStyle = DrawStyle.VCENTER;
			}
			int childY = determineTop(vPadding, wirelessStatus.getExtent().height, maxHeight, drawStyle);
			setPositionChild(wirelessStatus, childX, childY);
		}
		if (hasField(wirelessStatus) && hasField(roam)) {
			int childY = determineTop(vPadding, roam.getExtent().height, maxHeight, DrawStyle.VCENTER);
			int childX = determineLeft(roam, width, wirelessStatus, DrawStyle.RIGHT);
			setPositionChild(roam, childX, childY);
		}
		if (hasField(wifi)) {
			int childY = determineTop(vPadding, wifi.getExtent().height, maxHeight, DrawStyle.VCENTER);
			int childX = determineLeft(wifi, width, new Field[] { wirelessStatus, roam }, DrawStyle.RIGHT);
			setPositionChild(wifi, childX, childY);
		}
		if (hasField(phone)) {
			int childY = determineTop(vPadding, phone.getExtent().height, maxHeight, DrawStyle.VCENTER);
			int childX = determineLeft(phone, width, new Field[] { wirelessStatus, wifi, roam }, DrawStyle.RIGHT);
			setPositionChild(phone, childX, childY);
		}
		if (hasField(battery)) {
			int childY = determineTop(vPadding, battery.getExtent().height, maxHeight, DrawStyle.VCENTER);
			int childX = determineLeft(battery, width, new Field[] { wirelessStatus, wifi, phone, roam }, DrawStyle.RIGHT);
			setPositionChild(battery, childX, childY);
		}
		if (hasField(notifications)) {
			int childX = determineLeft(notifications, width, new Field[] { wirelessStatus, wifi, battery, phone, roam }, DrawStyle.RIGHT);
			setPositionChild(notifications, childX, vPadding);
		}

		int totalHeight = maxHeight + (vPadding >> 1);
		setVirtualExtent(fullWidth, totalHeight);
		setExtent(fullWidth, totalHeight);
	}

	/**
	 * Encapsulates some checks to see if the field is in the Manager.
	 * 
	 * @param field
	 *            the field to check.
	 * @return true if the Field exists and is attached to this Manager.
	 */
	private boolean hasField(Field field) {
		if (field != null && field.getManager() == this) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * With the padding, height, height of the line, place the item's top
	 * position using the given draw style
	 * 
	 * @param vPadding
	 *            vertical padding to include
	 * @param height
	 *            height of the item
	 * @param lineHeight
	 *            height of the available space
	 * @param drawStyle
	 *            one of DrawStyle.VCENTER or DrawStyle.BOTTOM
	 * @return top of the item
	 * @see DrawStyle
	 */
	protected int determineTop(int vPadding, int height, int lineHeight, int drawStyle) {
		int top = vPadding;
		if (drawStyle == DrawStyle.VCENTER) {
			top += (lineHeight - height) >> 1;
		} else if (drawStyle == DrawStyle.BOTTOM) {
			top += (lineHeight - height + vPadding);
		}
		return top;
	}

	/**
	 * Determine the left position of a field, to the right or left, based on
	 * another field that is closer to the given side.
	 * 
	 * @param field
	 *            the field to determine the position of.
	 * @param availableWidth
	 *            the width of the display.
	 * @param other
	 *            the other field which takes priority.
	 * @param style
	 *            DrawStyle.LEFT or DrawStyle.RIGHT.
	 * @return the x position for the left of the field.
	 */
	private int determineLeft(Field field, int availableWidth, Field other, int style) {
		return determineLeft(field, availableWidth, new Field[] { other }, style);
	}

	/**
	 * Determine the left position of a field, to the right or left, based on
	 * other fields that are closer to the given side.
	 * 
	 * @param field
	 *            the field to determine the position of.
	 * @param availableWidth
	 *            the width of the display.
	 * @param other
	 *            the other fields which take priority.
	 * @param style
	 *            DrawStyle.LEFT or DrawStyle.RIGHT.
	 * @return the x position for the left of the field.
	 */
	private int determineLeft(Field field, int availableWidth, Field[] others, int style) {
		int childX = 0;
		if (hasField(field)) {
			XYRect extent = field.getExtent();
			if (style == DrawStyle.RIGHT) {
				childX = hPadding + availableWidth - extent.width;
				for (int i = 0; i < others.length; i++) {
					if (hasField(others[i]) && others[i].getExtent().width > 0) {
						childX -= others[i].getExtent().width;
						childX -= interiorPadding;
					}
				}
			} else if (style == DrawStyle.LEFT) {
				childX = hPadding;
				for (int i = 0; i < others.length; i++) {
					if (hasField(others[i]) && others[i].getExtent().width > 0) {
						childX += others[i].getExtent().width;
						childX += interiorPadding;
					}
				}
			}
		}
		return childX;
	}

	protected void paint(Graphics graphics) {
		if (isDimension) {
			graphics.drawBitmap(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), backgroundImage, 0, 0);
		} else {
			graphics.setBackgroundColor(Color.BLACK);
			graphics.clear();
		}
		super.paint(graphics);
	}

	/**
	 * Banner does not accept focus.
	 */
	public boolean isFocusable() {
		return false;
	}

}
