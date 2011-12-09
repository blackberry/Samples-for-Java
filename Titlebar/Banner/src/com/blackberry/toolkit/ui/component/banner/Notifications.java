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

import com.blackberry.toolkit.device.util.SoftwareVersion;

import net.rim.blackberry.api.mail.ServiceConfiguration;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Store;
import net.rim.blackberry.api.mail.event.FolderEvent;
import net.rim.blackberry.api.mail.event.FolderListener;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

/**
 * Field to display Unread Mail as seen in the Banner
 * 
 * @author twindsor
 * @since 1.1 of Banner
 * @version 1.4 (Aug 2011)
 */
public class Notifications extends Field implements FolderListener {

	private static Store[] mailStores;
	private static String CMIME = "CMIME";

	private static int UNREAD_IMAGE_WIDTH;
	private static int UNREAD_IMAGE_HEIGHT;
	private static Bitmap UNREAD_IMAGE;

	private static String[] buckets = { SoftwareVersion.VER_421, SoftwareVersion.VER_460, SoftwareVersion.VER_600, SoftwareVersion.VER_700 };

	static {
		String device = DeviceInfo.getDeviceName();
		int bucket = SoftwareVersion.whichBucket(buckets);
		String unreadImageName;
		switch (bucket) {
		case 0:
			UNREAD_IMAGE_WIDTH = 14;
			UNREAD_IMAGE_HEIGHT = 14;
			unreadImageName = "unread.png";
			break;
		case 1:
			if (Display.getWidth() < 360) {
				// Smaller images for 8520 and 8530
				UNREAD_IMAGE_WIDTH = 14;
				UNREAD_IMAGE_HEIGHT = 14;
				unreadImageName = "unread46small.png";
			} else {
				UNREAD_IMAGE_WIDTH = 21;
				UNREAD_IMAGE_HEIGHT = 21;
				unreadImageName = "unread46.png";
			}
			break;
		case 2:
			if (device.startsWith("97") || device.startsWith("96")) {
				UNREAD_IMAGE_WIDTH = 25;
				UNREAD_IMAGE_HEIGHT = 22;
				unreadImageName = "unread60wide.png";
			} else {
				UNREAD_IMAGE_WIDTH = 19;
				UNREAD_IMAGE_HEIGHT = 19;
				unreadImageName = "unread60.png";
			}
			break;
		case 3:
		default:
			if (device.startsWith("98")) {
				UNREAD_IMAGE_WIDTH = 25;
				UNREAD_IMAGE_HEIGHT = 25;
				unreadImageName = "unread70thin.png";
			} else if (device.startsWith("99")) {
				UNREAD_IMAGE_WIDTH = 33;
				UNREAD_IMAGE_HEIGHT = 29;
				unreadImageName = "unread70wide.png";
			} else {
				UNREAD_IMAGE_WIDTH = 25;
				UNREAD_IMAGE_HEIGHT = 22;
				unreadImageName = "unread70small.png";
			}
		}
		UNREAD_IMAGE = EncodedImage.getEncodedImageResource(unreadImageName).getBitmap();
	}

	/**
	 * Create the field
	 */
	public Notifications() {
		try {
			ServiceBook sb = ServiceBook.getSB();
			ServiceRecord[] records = sb.findRecordsByCid(CMIME);
			mailStores = new Store[records.length];
			for (int i = 0; i < records.length; i++) {
				ServiceConfiguration sc = new ServiceConfiguration(records[i]);
				Session session = Session.getInstance(sc);
				mailStores[i] = session.getStore();
			}
		} catch (Exception e) {
			// carry on
		}
		setFont(BannerFont.getFont());
	}

	public int getPreferredHeight() {
		return BannerFont.getFontHeight();
	}

	public int getPreferredWidth() {
		int width = 0;
		int unread = getUnreadMessages();
		if (unread > 0) {
			String text = Integer.toString(unread);
			width += BannerFont.getWidth(BannerFont.getFont(), text) + 1;
			width += UNREAD_IMAGE_WIDTH;
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
		graphics.setColor(BannerFont.getFontColor()); // Banner font color
		int unread = getUnreadMessages();
		if (unread > 0) {
			String text = Integer.toString(unread);
			graphics.drawText(text, 0, UNREAD_IMAGE_HEIGHT, DrawStyle.BOTTOM);
			graphics.drawBitmap(BannerFont.getWidth(BannerFont.getFont(), text) + 1, 0, UNREAD_IMAGE_WIDTH, UNREAD_IMAGE_HEIGHT, UNREAD_IMAGE, 0, 0);
		}
	}

	/**
	 * Get the total unread messages counter
	 * 
	 * @return number of messages in all Stores
	 */
	protected int getUnreadMessages() {
		int messages = 0;
		for (int i = 0; i < mailStores.length; i++) {
			if (mailStores[i] != null) {
				messages += mailStores[i].getUnreadMessageCount();
			}
		}
		return messages;
	}

	/**
	 * When displayed, start listening for events.
	 */
	protected void onDisplay() {
		for (int i = 0; i < mailStores.length; i++) {
			if (mailStores[i] != null) {
				mailStores[i].addFolderListener(this);
			}
		}
	}

	/**
	 * When this field is removed from the stack, remove the listener.
	 */
	protected void onUndisplay() {
		for (int i = 0; i < mailStores.length; i++) {
			mailStores[i].removeFolderListener(this);
		}
	}

	/**
	 * Update display when new messages arrive
	 */
	public void messagesAdded(FolderEvent event) {
		if (event.getMessage().isInbound()) {
			invalidate();
		}
	}

	public void messagesRemoved(FolderEvent e) {
		// ignore
	}

}
