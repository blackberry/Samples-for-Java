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

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.GlobalEventListener;
import net.rim.device.api.system.RealtimeClockListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.util.DateTimeUtilities;

/**
 * Field to display the time as shown in the banner.
 * 
 * @author twindsor
 * @since 1.1
 * @version 1.2 (Aug 2010)
 */
public class TimeDisplay extends LabelField implements RealtimeClockListener, GlobalEventListener {

	private SimpleDateFormat formatter = new SimpleDateFormat(DateFormat.TIME_MEDIUM);

	/**
	 * a Create the field
	 */
	public TimeDisplay() {
		setText(getTime());
		setFont(BannerFont.getFont());
	}

	protected void layout(int width, int height) {
		super.layout(width, height);
	}

	protected void paint(Graphics graphics) {
		graphics.setColor(BannerFont.getFontColor()); // Banner font color
		super.paint(graphics);
	}

	public String getTime() {
		return formatter.formatLocal(System.currentTimeMillis());
	}

	/**
	 * When displayed, start listening for events.
	 */
	protected void onDisplay() {
		UiApplication.getUiApplication().addRealtimeClockListener(this);
		UiApplication.getUiApplication().addGlobalEventListener(this);
	}

	/**
	 * When this field is removed from the stack, remove the listener.
	 */
	protected void onUndisplay() {
		UiApplication.getUiApplication().removeRealtimeClockListener(this);
		UiApplication.getUiApplication().removeGlobalEventListener(this);
	}

	public void clockUpdated() {
		setText(getTime());
		invalidate();
	}

	/**
	 * GlobalEventListener Listen for Date and Timezone changes
	 */
	public void eventOccurred(long guid, int data0, int data1, Object object0, Object object1) {
		if (guid == DateTimeUtilities.GUID_DATE_CHANGED || guid == DateTimeUtilities.GUID_TIMEZONE_CHANGED) {
			clockUpdated();
		}
	}

}
