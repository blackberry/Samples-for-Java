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

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;

import com.blackberry.toolkit.device.util.SoftwareVersion;

/**
 * Static utility class for finding the appropriate Font. Used by Title,
 * WirelessStatus, and TimeDisplay
 * 
 * @author twindsor
 * @since 1.1
 * @version 1.3 (Aug 2011)
 */
public class BannerFont {

	private static String[] buckets = { SoftwareVersion.VER_421, SoftwareVersion.VER_460, SoftwareVersion.VER_471, SoftwareVersion.VER_600,
			SoftwareVersion.VER_700 };

	private static Font TITLE_FONT = Font.getDefault();
	private static int TITLE_FONT_HEIGHT;
	private static int TITLE_FONT_COLOR;
	private static final int ANTIALIAS_STANDARD = 2;

	static {
		FontFamily[] fonts = FontFamily.getFontFamilies();
		String device = DeviceInfo.getDeviceName();
		int bucket = SoftwareVersion.whichBucket(buckets);
		switch (bucket) {
		case 0:
			TITLE_FONT_HEIGHT = 12;
			for (int i = 0; i < fonts.length; i++) {
				if (fonts[i].getName().equals("BBClarity")) {
					TITLE_FONT = fonts[i].getFont(Font.BOLD, TITLE_FONT_HEIGHT, Ui.UNITS_px);
					TITLE_FONT = TITLE_FONT.derive(Font.BOLD, TITLE_FONT_HEIGHT, Ui.UNITS_px, ANTIALIAS_STANDARD, 0);
					TITLE_FONT_COLOR = 0x000059DE;
					break;
				}
			}
			break;
		case 1:
		case 2:
		case 3:
			if (bucket == 3 && (device.startsWith("97") || device.startsWith("96"))) {
				TITLE_FONT_HEIGHT = 24;
			} else if (Display.getWidth() < 360) {
				TITLE_FONT_HEIGHT = 15;
			} else {
				TITLE_FONT_HEIGHT = 20;
			}
			String fontName = "BBAlpha Sans Condensed";
			if (bucket == 1) {
				// Condensed is not available until 4.7.1
				fontName = "BBAlpha Sans";
			}
			for (int i = 0; i < fonts.length; i++) {
				if (fonts[i].getName().equals(fontName)) {
					TITLE_FONT = fonts[i].getFont(Font.PLAIN, TITLE_FONT_HEIGHT, Ui.UNITS_px);
					break;
				}
			}
			TITLE_FONT_COLOR = 0x00F7F7F7;
			break;
		case 4:
		default:
			if (device.startsWith("98")) {
				TITLE_FONT_HEIGHT = 27;
			} else if (device.startsWith("99")) {
				TITLE_FONT_HEIGHT = 32;
			} else {
				TITLE_FONT_HEIGHT = 24;
			}
			for (int i = 0; i < fonts.length; i++) {
				if (fonts[i].getName().equals("BBAlpha Sans Condensed")) {
					TITLE_FONT = fonts[i].getFont(Font.PLAIN, TITLE_FONT_HEIGHT, Ui.UNITS_px);
					break;
				}
			}
			TITLE_FONT_COLOR = 0x00F7F7F7;
		}
	}

	public static int getFontHeight() {
		return TITLE_FONT.getHeight();
	}

	public static Font getFont() {
		return TITLE_FONT;
	}

	public static int getFontColor() {
		return TITLE_FONT_COLOR;
	}

	/**
	 * Given the font and the array of text, find the maximum width used to
	 * display these strings.
	 * 
	 * @param font
	 *            font to calculate with
	 * @param text
	 *            strings to test
	 * @return the maximum width using the Font's getAdvance() method.
	 */
	public static int getMaxWidth(Font font, String[] text) {
		int max = 0;
		int advance;
		for (int i = 0; i < text.length; i++) {
			advance = font.getAdvance(text[i]);
			if (advance > max) {
				max = advance;
			}
		}
		return max;
	}

	/**
	 * Get width given the font and text
	 * 
	 * @param font
	 * @param text
	 * @return width of the text
	 */
	public static int getWidth(Font font, String text) {
		return font.getAdvance(text);
	}
}
