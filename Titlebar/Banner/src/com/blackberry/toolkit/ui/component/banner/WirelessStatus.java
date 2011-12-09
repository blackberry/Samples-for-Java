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
import net.rim.device.api.system.Branding;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.CoverageStatusListener;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.IDENInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.RadioStatusListener;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.CharacterUtilities;

import com.blackberry.toolkit.device.util.SoftwareVersion;

/**
 * Wireless Status indicator Field. Shows Wireless Signal Strength, Network
 * Coverage type, and Wifi Coverage Indicators that appear the same as the main
 * banner display. Images and status levels are supported from 4.2.1 through
 * 7.0.0.
 * 
 * <b>Note:</b> Uses the Software Version Library to determine software levels.
 * 
 * @author twindsor
 * @version Version 1.6 (Aug 2011)
 */
public class WirelessStatus extends Field implements RadioStatusListener, CoverageStatusListener {

	// GSM radios use these thresholds (until 6.0)
	private static byte[] WIRELESS_THRESHOLDS_GSM = new byte[] { Byte.MIN_VALUE, -102, -93, -87, -78 };
	// CDMA radios use these thresholds
	private static byte[] WIRELESS_THRESHOLDS_CDMA = new byte[] { Byte.MIN_VALUE, -109, -105, -98, -90, -83 };
	// IDEN radios use these thresholds
	private static byte[] WIRELESS_THRESHOLDS_IDEN = new byte[] { Byte.MIN_VALUE, -103, -100, -94, -91, -88 };
	// 7.0 uses an array of images for Wifi signal strength
	private static byte[] WIFI_THRESHOLDS = null;

	// offset within the Wireless signal images
	private static int IMAGE_OFFSET_GSM = 3;
	private static final int IMAGE_OFFSET_CDMA = 2;
	private static final int IMAGE_OFFSET_IDEN = 2;
	// These settings will be set based on the device software version
	private static int WIRELESS_IMAGE_WIDTH;
	private static int WIRELESS_IMAGE_HEIGHT;
	private static Bitmap WIRELESS_IMAGE;
	private static Font DESCRIPTOR_FONT = Font.getDefault();
	private static int DESCRIPTOR_HEIGHT;
	private static int DESCRIPTOR_WIDTH;
	private static Bitmap WIFI_IMAGE;
	private static int WIFI_IMAGE_WIDTH;
	private static int WIFI_IMAGE_HEIGHT;

	// Constants copied to allow building in 4.2.1 and for consistency
	private static final int EDGE = RadioInfo.NETWORK_SERVICE_EDGE;
	private static final int UMTS = RadioInfo.NETWORK_SERVICE_UMTS;
	private static final int EVDO = RadioInfo.NETWORK_SERVICE_EVDO;
	private static final int EVDO_REVA = 131072;
	private static final int EVDO_REV0 = 65536;
	private static final int COVERAGE_BIS_B = 0x04;

	// Arrays of Coverage Status descriptors
	private static String[] CDMA = { "", "D", "D", "1x", "1X", "1xev", "1XEV", "3G", "3G \uF8EB" };
	private static String[] GSM = { "", "gsm", "GSM", "gprs", "GPRS", "edge", "EDGE", "3G", "3G \uF8EB", "3g", "3G \uF8EB" };
	private static String[] IDEN = { "", "NXTL", "MIKE" };
	private static final int CDMA_VOICE = 2;
	private static final int CDMA_1x = 3;
	private static final int CDMA_1X = 4;
	private static final int CDMA_1xev = 5;
	private static final int CDMA_1XEV = 6;
	private static final int CDMA_3G = 7;
	private static final int CDMA_3GBB = 8;
	private static final int GPP_gsm = 1;
	private static final int GPP_GSM = 2;
	private static final int GPP_gprs = 3;
	private static final int GPP_GPRS = 4;
	private static final int GPP_edge = 5;
	private static final int GPP_EDGE = 6;
	private static final int GPP_3G = 7;
	private static final int GPP_3GBB = 8;
	private static final int GPP_3g = 9;
	private static final int GPP_3GEDGEBB = 10;
	private static final int IDEN_NXTL = 1;
	private static final int IDEN_MIKE = 2;
	private static boolean use3GForCDMA;
	private static boolean BB7 = false;

	private static String[] buckets = { SoftwareVersion.VER_421, SoftwareVersion.VER_460, SoftwareVersion.VER_600, SoftwareVersion.VER_700 };

	// Display settings
	public static final int DISPLAY_FULL = 0;
	public static final int DISPLAY_SIGNAL = 1;
	public static final int DISPLAY_DESCRIPTOR = 2;
	public static final int DISPLAY_WIFI = 3;
	private static final int DISPLAY_OPTIONS = 4;
	private int displaySetting = DISPLAY_FULL;

	private boolean _collapsible = true;
	private int _currentWirelessImageId;
	private int _currentWiFiImageId;
	private String _currentText;

	static {
		String device = DeviceInfo.getDeviceName();
		int bucket = SoftwareVersion.whichBucket(buckets);
		String wirelessImageName;
		String wifiImageName;
		switch (bucket) {
		case 0:
			/*
			 * 4.2.1 through 4.5.0 uses this set of images.
			 */
			WIRELESS_IMAGE_WIDTH = 34;
			WIRELESS_IMAGE_HEIGHT = 12;
			wirelessImageName = "wirelesslevels.png";
			wifiImageName = "wifi.png";
			WIFI_IMAGE_WIDTH = 36;
			WIFI_IMAGE_HEIGHT = 18;
			break;
		case 1:
			// 4.6.0 and newer use these images.
			if (Display.getWidth() < 360) {
				// Smaller images for 8520 and 8530, etc.
				WIRELESS_IMAGE_WIDTH = 25;
				WIRELESS_IMAGE_HEIGHT = 12;
				wirelessImageName = "wirelesslevels46small.png";
				wifiImageName = "wifi46small.png";
				WIFI_IMAGE_WIDTH = 25;
				WIFI_IMAGE_HEIGHT = 12;
			} else {
				WIRELESS_IMAGE_WIDTH = 35;
				WIRELESS_IMAGE_HEIGHT = 14;
				wirelessImageName = "wirelesslevels46.png";
				wifiImageName = "wifi46.png";
				WIFI_IMAGE_WIDTH = 36;
				WIFI_IMAGE_HEIGHT = 15;
			}
			break;
		case 2:
			// 6.0.0 and newer use these images.
			if (device.startsWith("97") || device.startsWith("96")) {
				WIRELESS_IMAGE_WIDTH = 38;
				WIRELESS_IMAGE_HEIGHT = 16;
				wirelessImageName = "wirelesslevels60wide.png";
				wifiImageName = "wifi60wide.png";
				WIFI_IMAGE_WIDTH = 18;
				WIFI_IMAGE_HEIGHT = 15;
			} else {
				WIRELESS_IMAGE_WIDTH = 29;
				WIRELESS_IMAGE_HEIGHT = 12;
				wirelessImageName = "wirelesslevels60.png";
				wifiImageName = "wifi60.png";
				WIFI_IMAGE_WIDTH = 18;
				WIFI_IMAGE_HEIGHT = 14;
			}
			// 6.0 uses new values for the Signal thresholds
			WIRELESS_THRESHOLDS_GSM = new byte[] { Byte.MIN_VALUE, -108, -103, -98, -89, -80 };
			IMAGE_OFFSET_GSM = 2;
			break;
		case 3:
		default:
			// 7.0 uses new values for the Signal thresholds same as 6.0
			WIRELESS_THRESHOLDS_GSM = new byte[] { Byte.MIN_VALUE, -108, -103, -98, -89, -80 };
			IMAGE_OFFSET_GSM = 2;
			WIFI_THRESHOLDS = new byte[] { Byte.MIN_VALUE, -81, -71, -61 };
			if (device.startsWith("98")) {
				WIRELESS_IMAGE_WIDTH = 39;
				WIRELESS_IMAGE_HEIGHT = 16;
				wirelessImageName = "wirelesslevels70thin.png";
				wifiImageName = "wifi70thin.png";
				WIFI_IMAGE_WIDTH = 25;
				WIFI_IMAGE_HEIGHT = 20;
			} else if (device.startsWith("99")) {
				WIRELESS_IMAGE_WIDTH = 49;
				WIRELESS_IMAGE_HEIGHT = 19;
				wirelessImageName = "wirelesslevels70wide.png";
				wifiImageName = "wifi70wide.png";
				WIFI_IMAGE_WIDTH = 30;
				WIFI_IMAGE_HEIGHT = 24;
			} else {
				WIRELESS_IMAGE_WIDTH = 34;
				WIRELESS_IMAGE_HEIGHT = 14;
				wirelessImageName = "wirelesslevels70small.png";
				wifiImageName = "wifi70small.png";
				WIFI_IMAGE_WIDTH = 22;
				WIFI_IMAGE_HEIGHT = 18;
			}
			BB7 = true;
			break;
		}
		DESCRIPTOR_HEIGHT = BannerFont.getFontHeight();
		DESCRIPTOR_FONT = BannerFont.getFont();
		WIRELESS_IMAGE = EncodedImage.getEncodedImageResource(wirelessImageName).getBitmap();
		WIFI_IMAGE = EncodedImage.getEncodedImageResource(wifiImageName).getBitmap();
		DESCRIPTOR_WIDTH = Math.max(BannerFont.getMaxWidth(DESCRIPTOR_FONT, IDEN),
				Math.max(BannerFont.getMaxWidth(DESCRIPTOR_FONT, GSM), BannerFont.getMaxWidth(DESCRIPTOR_FONT, CDMA)));
		use3GForCDMA = use3GforCDMA(); // test once
	}

	/**
	 * Creates a Wireless Status indicator using the appropriate images.
	 * Supports 4.2.1 and above as the necessary API for getting signal level
	 * properly is in 4.2.1.
	 * 
	 * @param one
	 *            of the DISPLAY_* settings.
	 */
	public WirelessStatus(int settings, boolean collapsible) {
		_collapsible = collapsible;
		setDisplaySetting(settings, false);
		_currentText = getWirelessDescriptor();
		_currentWirelessImageId = getWirelessImageId();
		_currentWiFiImageId = getWifiImageId();
	}

	/**
	 * Default constructor which creates a field with both Network type and
	 * Signal Strength
	 */
	public WirelessStatus() {
		this(DISPLAY_FULL, true);
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
	 * Return the desired height, depending on the type of field displayed
	 */
	public int getPreferredHeight() {
		if (displaySetting == DISPLAY_WIFI) {
			return WIFI_IMAGE_HEIGHT;
		} else {
			// return WIRELESS_IMAGE_HEIGHT + BannerFont.getFont().getDescent();
			return Math.max(WIRELESS_IMAGE_HEIGHT + BannerFont.getFont().getDescent(), BannerFont.getFontHeight());
		}
	}

	/**
	 * Return the desired width, depending on the type of field displayed
	 */
	public int getPreferredWidth() {
		int width = 0;
		if (displaySetting == DISPLAY_DESCRIPTOR || displaySetting == DISPLAY_FULL) {
			width += getDescriptorWidth() + 2;
		}
		if (displaySetting == DISPLAY_SIGNAL || displaySetting == DISPLAY_FULL) {
			width += WIRELESS_IMAGE_WIDTH;
		}
		if (displaySetting == DISPLAY_WIFI) {
			if (_collapsible && _currentWiFiImageId < 0) {
				width = 0;
			} else {
				width = WIFI_IMAGE_WIDTH;
			}
		}
		return width;
	}

	private int getDescriptorWidth() {
		if (_collapsible) {
			return BannerFont.getWidth(DESCRIPTOR_FONT, _currentText);
		}
		return DESCRIPTOR_WIDTH;
	}

	public int getDisplaySetting() {
		return displaySetting;
	}

	/**
	 * Set the visual display to one of the following: DISPLAY_FULL,
	 * DISPLAY_SIGNAL, DISPLAY_DESCRIPTOR, or DISPLAY_WIFI
	 * 
	 * @param displaySetting
	 *            will default to DISPLAY_FULL
	 */
	public void setDisplaySetting(int displaySetting) {
		setDisplaySetting(displaySetting, true);
	}

	/**
	 * Set the visual display to one of the following: DISPLAY_FULL,
	 * DISPLAY_SIGNAL, DISPLAY_DESCRIPTOR, or DISPLAY_WIFI
	 * 
	 * @param displaySetting
	 *            will default to DISPLAY_FULL
	 * @param update
	 *            true to make a Layout Update if the setting has changed.
	 * @since 1.2
	 */
	private void setDisplaySetting(int displaySetting, boolean update) {
		int oldSetting = this.displaySetting;
		if (displaySetting >= 0 || displaySetting < DISPLAY_OPTIONS) {
			this.displaySetting = displaySetting;
		} else {
			this.displaySetting = DISPLAY_FULL;
		}
		if (oldSetting != this.displaySetting && update) {
			// avoid unnecessary paints
			updateLayout();
		}
	}

	protected void paint(Graphics graphics) {
		int signalPaintOffset = 0; // padding
		if (displaySetting == DISPLAY_DESCRIPTOR || displaySetting == DISPLAY_FULL) {
			graphics.setColor(BannerFont.getFontColor());// Banner font color
			graphics.setFont(DESCRIPTOR_FONT);
			int height = Math.max(WIRELESS_IMAGE_HEIGHT, BannerFont.getFont().getAscent());
			int drawStyle = DrawStyle.BASELINE;
			if (displaySetting == DISPLAY_FULL) {
				// only shift right if displaying signal too
				drawStyle |= DrawStyle.RIGHT;
			}
			graphics.drawText(_currentText, 0, height, drawStyle, getDescriptorWidth());
			signalPaintOffset += getDescriptorWidth() + 2;
		}
		if (displaySetting == DISPLAY_SIGNAL || displaySetting == DISPLAY_FULL) {
			// Select the region to draw based on image ID.
			int left = _currentWirelessImageId * WIRELESS_IMAGE_WIDTH;
			int top = Math.max(BannerFont.getFont().getAscent() - WIRELESS_IMAGE_HEIGHT, 0);
			graphics.drawBitmap(signalPaintOffset, top, WIRELESS_IMAGE_WIDTH, WIRELESS_IMAGE_HEIGHT, WIRELESS_IMAGE, left, 0);
		}
		if (displaySetting == DISPLAY_WIFI) {
			int left = _currentWiFiImageId * WIFI_IMAGE_WIDTH;
			if (left < 0) {
				return; // Wifi radio off
			}
			graphics.drawBitmap(0, 0, WIFI_IMAGE_WIDTH, WIFI_IMAGE_HEIGHT, WIFI_IMAGE, left, 0);
		}
	}

	/**
	 * Get the current wireless image id for painting. The image to be painted
	 * is one of 11 possible icons within the file. Based on the wireless level
	 * and network type, this will return the appropriate image id.
	 * 
	 * @return the id from the appropriate Threshold array which corresponds to
	 *         the image to use.
	 */
	protected int getWirelessImageId() {
		if (RadioInfo.getState() == RadioInfo.STATE_ON) {
			if ((RadioInfo.getNetworkService() & RadioInfo.NETWORK_SERVICE_EMERGENCY_ONLY) > 0) {
				// Emergency only is SOS
				return 8;
			}
			// default to GSM
			int wafs = RadioInfo.getActiveWAFs();
			byte[] thresholds = WIRELESS_THRESHOLDS_GSM;
			int imageOffset = IMAGE_OFFSET_GSM;
			int signalLevel = Byte.MIN_VALUE;
			if ((wafs & RadioInfo.WAF_CDMA) > 0) {
				thresholds = WIRELESS_THRESHOLDS_CDMA;
				imageOffset = IMAGE_OFFSET_CDMA;
				signalLevel = RadioInfo.getSignalLevel(RadioInfo.WAF_CDMA);
			} else if ((wafs & RadioInfo.WAF_3GPP) > 0) {
				thresholds = WIRELESS_THRESHOLDS_GSM;
				imageOffset = IMAGE_OFFSET_GSM;
				signalLevel = RadioInfo.getSignalLevel(RadioInfo.WAF_3GPP);
			} else if ((wafs & RadioInfo.WAF_IDEN) > 0) {
				thresholds = WIRELESS_THRESHOLDS_IDEN;
				imageOffset = IMAGE_OFFSET_IDEN;
				signalLevel = RadioInfo.getSignalLevel(RadioInfo.WAF_IDEN);
			}
			if (signalLevel == RadioInfo.LEVEL_NO_COVERAGE) {
				// No coverage image is at index 1
				return 1;
			}
			// There is at least some coverage.
			for (int i = thresholds.length - 1; i >= 0; i--) {
				if (signalLevel > thresholds[i]) {
					// Found the index to use
					return i + imageOffset;
				}
			}
			// signal level is unknown return SOS
			return 8;
		} else {
			// Radio is off, use index 0
			return 0;
		}
	}

	/**
	 * Determine the Wireless Coverage text description. ie: 1XEV, 3G, EDGE,
	 * GSM, 1x, gprs, edge, etc.
	 * 
	 * @return the text of the wireless coverage
	 */
	protected String getWirelessDescriptor() {
		if (RadioInfo.getState() == RadioInfo.STATE_ON) {

			if ((RadioInfo.getNetworkService() & RadioInfo.NETWORK_SERVICE_EMERGENCY_ONLY) > 0) {
				// Emergency only has no descriptor
				return GSM[0];
			}

			int wafs = RadioInfo.getActiveWAFs();
			int service = RadioInfo.getNetworkService();

			String[] descriptors = GSM;
			int descriptorIndex = 0;

			if ((wafs & RadioInfo.WAF_3GPP) > 0) {
				descriptors = GSM;
				if ((service & UMTS) > 0 && ((service & EDGE) > 0 || !hasData(service))) {
					// UMTS with EDGE and UMTS with No Data both use this
					// pattern of 3GBB/3g
					descriptorIndex = GPP_3GEDGEBB;
				} else if ((service & UMTS) > 0) {
					descriptorIndex = GPP_3GBB;
				} else if ((service & EDGE) > 0) {
					descriptorIndex = GPP_EDGE;
				} else if (hasData(service)) {
					descriptorIndex = GPP_GPRS;
				} else if (hasVoice(service)) {
					descriptorIndex = GPP_GSM;
				}
			} else if ((wafs & RadioInfo.WAF_CDMA) > 0) {
				descriptors = CDMA;
				if ((service & EVDO_REV0) > 0 || (service & EVDO_REVA) > 0 || (service & EVDO) > 0) {
					if (use3GForCDMA) {
						descriptorIndex = CDMA_3GBB;
					} else {
						descriptorIndex = CDMA_1XEV;
					}
				} else if (hasData(service)) {
					descriptorIndex = CDMA_1X;
				} else if (hasVoice(service)) {
					descriptorIndex = CDMA_VOICE;
				}
			} else if ((wafs & RadioInfo.WAF_IDEN) > 0) {
				StringBuffer descriptor;
				int mcc = IDENInfo.getHomeMCC();
				int ndc = IDENInfo.getHomeNDC();
				if (RadioInfo.getSignalLevel(RadioInfo.WAF_IDEN) == RadioInfo.LEVEL_NO_COVERAGE || (mcc == 0 && ndc == 0)) {
					// no coverage
					return IDEN[0];
				}
				if (mcc == 0x0302 && ndc == 0x360) {
					// Mike network
					descriptor = new StringBuffer(IDEN[IDEN_MIKE]);
				} else {
					descriptor = new StringBuffer(IDEN[IDEN_NXTL]);
				}
				if (!hasVoice(service)) {
					lowerCharacter(descriptor, 0);
				}
				if (!hasDirectConnect(service)) {
					lowerCharacter(descriptor, 1);
				}
				if (!hasData(service)) {
					lowerCharacter(descriptor, 2);
				}
				if (!isEmailAPNActive(RadioInfo.getActiveWAFs() & ~RadioInfo.WAF_WLAN)) {
					lowerCharacter(descriptor, 3);
				}
				// done - doesn't follow the same format as GSM/CDMA
				return descriptor.toString();
			}
			if (!isEmailAPNActive(RadioInfo.getActiveWAFs() & ~RadioInfo.WAF_WLAN) && descriptorIndex > 0) {
				descriptorIndex--;
			}
			return descriptors[descriptorIndex];
		} else {
			return GSM[0];
		}
	}

	/**
	 * Choose the appropriate Wifi Image (or none) depending on Wifi Coverage
	 * 
	 * @return the index in the image file to use
	 */
	protected int getWifiImageId() {
		int imageId = -1;
		// for 7.0, use different algorithm
		if (WIFI_THRESHOLDS != null) {
			if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN) != 0) {
				imageId = 1;
				int signalLevel = RadioInfo.getSignalLevel(RadioInfo.WAF_WLAN);
				for (int i = WIFI_THRESHOLDS.length - 1; i >= 0; i--) {
					if (signalLevel > WIFI_THRESHOLDS[i]) {
						// Found the index to use
						imageId = i + 2;
						break;
					}
				}
			}
			return imageId;
		} else {
			if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN) != 0) {
				// Wifi Radio On
				imageId = 0;
				// Connection to relay over WLAN
				boolean coverage = isEmailAPNActive(RadioInfo.WAF_WLAN);
				if (coverage) {
					imageId = 1;
				}
			}
			return imageId;
		}
	}

	/**
	 * Use the CoverageInfo API to determine if a relay connection is active on
	 * the given WAFs.
	 * 
	 * @param wafs
	 *            bitmask of WAFS from RadioInfo.
	 * @return true if there is a valid connection to either MDS or BIS, which
	 *         means that a relay connection is alive and the Banner should show
	 *         Connected/With Email.
	 */
	private boolean isEmailAPNActive(int wafs) {
		return CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS, wafs, false)
				|| CoverageInfo.isCoverageSufficient(COVERAGE_BIS_B, wafs, false);
	}

	/**
	 * Determine if the current service has Data Coverage
	 * 
	 * @param service
	 *            network service
	 * @return true if Data coverage is available
	 */
	private boolean hasData(int service) {
		if ((service & RadioInfo.NETWORK_SERVICE_DATA) != 0) {
			return true;
		}
		return false;
	}

	/**
	 * Determine if the current service has Voice Coverage
	 * 
	 * @param service
	 *            network service
	 * @return true if Voice coverage is available
	 */
	private boolean hasVoice(int service) {
		if ((service & RadioInfo.NETWORK_SERVICE_VOICE) != 0) {
			return true;
		}
		return false;
	}

	/**
	 * Determine if the current service has Direct Connect Coverage
	 * 
	 * @param service
	 *            network service
	 * @return true if Direct Connect coverage is available
	 */
	private boolean hasDirectConnect(int service) {
		if ((service & RadioInfo.NETWORK_SERVICE_DIRECT_CONNECT) != 0) {
			return true;
		}
		return false;
	}

	/**
	 * Make the character at the given index lowercase
	 * 
	 * @param buffer
	 * @param index
	 */
	private void lowerCharacter(StringBuffer buffer, int index) {
		buffer.setCharAt(index, CharacterUtilities.toLowerCase(buffer.charAt(index)));
	}

	/**
	 * Newer Verizon branded devices use 3G when on EVDO Rev. A
	 * 
	 * @return true if the Device is Verizon Branded and it is a newer device,
	 *         which uses this convention.
	 */
	private static boolean use3GforCDMA() {
		if (BB7) {
			return true;
		}
		if (Branding.getVendorId() == 105) {
			String deviceModel = DeviceInfo.getDeviceName();
			if (deviceModel != null && (!(deviceModel.equals("8330") || deviceModel.equals("9530") || deviceModel.equals("9630")))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * When displayed, start listening for events.
	 */
	protected void onDisplay() {
		UiApplication.getUiApplication().addRadioListener(RadioInfo.getSupportedWAFs(), this);
		CoverageInfo.addListener(this);
	}

	/**
	 * When this field is removed from the stack, remove the listeners.
	 */
	protected void onUndisplay() {
		UiApplication.getUiApplication().removeRadioListener(this);
		CoverageInfo.removeListener(this);
	}

	/**
	 * Ignored
	 */
	public void baseStationChange() {
	}

	/**
	 * Ignored
	 */
	public void mobilityManagementEvent(int eventCode, int cause) {
	}

	/**
	 * Ignored
	 */
	public void networkScanComplete(boolean success) {
	}

	/**
	 * Repaint on service changes.
	 * 
	 * @see RadioStatusListener
	 */
	public void networkServiceChange(int networkId, int service) {
		checkForRepaint();
	}

	/**
	 * Repaint when a network starts up.
	 */
	public void networkStarted(int networkId, int service) {
		checkForRepaint();
	}

	/**
	 * Ignored
	 */
	public void networkStateChange(int state) {
	}

	/**
	 * Repaint on pdp changes.
	 * 
	 * @see RadioStatusListener
	 */
	public void pdpStateChange(int apn, int state, int cause) {
		checkForRepaint();
	}

	/**
	 * Repaint if the radio is turned off
	 * 
	 * @see RadioStatusListener
	 */
	public void radioTurnedOff() {
		checkForRepaint();
	}

	/**
	 * Repaint on SignalLevel Changes
	 * 
	 * @see RadioStatusListener
	 */
	public void signalLevel(int level) {
		checkForRepaint();
	}

	/**
	 * Repaint when coverage status changes.
	 * 
	 * @see CoverageStatusListener
	 */
	public void coverageStatusChanged(int newCoverage) {
		checkForRepaint();
	}

	/**
	 * Check Display settings and current values to determine if a repaint is
	 * required.
	 */
	private void checkForRepaint() {
		boolean repaint = false;
		if (displaySetting == DISPLAY_DESCRIPTOR || displaySetting == DISPLAY_FULL) {
			String descriptor = getWirelessDescriptor();
			if (_currentText == null || !_currentText.equals(descriptor)) {
				_currentText = descriptor;
				repaint = true;
			}
		}
		if (displaySetting == DISPLAY_SIGNAL || displaySetting == DISPLAY_FULL) {
			int wirelessImage = getWirelessImageId();
			if (_currentWirelessImageId != wirelessImage) {
				_currentWirelessImageId = wirelessImage;
				repaint = true;
			}
		}
		if (displaySetting == DISPLAY_WIFI) {
			int wifiImage = getWifiImageId();
			if (_currentWiFiImageId != wifiImage) {
				_currentWiFiImageId = wifiImage;
				repaint = true;
			}
		}
		if (repaint) {
			if (_collapsible) {
				updateLayout();
			} else {
				invalidate();
			}
		}
	}
}
