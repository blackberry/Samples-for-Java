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

package com.blackberry.toolkit.ui.component.banner.test;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.Radio;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.blackberry.toolkit.ui.component.banner.Banner;
import com.blackberry.toolkit.ui.component.banner.BatteryStatus;
import com.blackberry.toolkit.ui.component.banner.Notifications;
import com.blackberry.toolkit.ui.component.banner.TimeDisplay;
import com.blackberry.toolkit.ui.component.banner.WirelessStatus;

/**
 * TestHarness for testing the implementation and visual display of the Banner elements.
 * 
 * @author twindsor
 * @version 1.3 (Aug 2010)
 */
public class TestHarness extends UiApplication {

	public TestHarness() {
		HarnessScreen screen = new HarnessScreen();
		pushScreen(screen);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestHarness app = new TestHarness();
		app.enterEventDispatcher();
	}

	private class HarnessScreen extends MainScreen {

		private final Banner banner = new Banner();
		private int batteryVisibility = Banner.PROPERTY_VALUE_DEFAULT;
		private boolean hasSignal = false;
		private boolean hasNotifications = false;
		private boolean hasClock = false;
		private boolean hasIcon = false;

		private final EncodedImage img = EncodedImage.getEncodedImageResource("tree.jpg");

		private final BasicEditField log = new BasicEditField("Log: ", "");
		private final VerticalFieldManager vfm = new VerticalFieldManager() {
			protected void paint(Graphics graphics) {
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
				super.paint(graphics);
			};
		};

		public HarnessScreen() {
			BatteryStatus batteryStatus = new BatteryStatus();
			batteryStatus.setDisplaySetting(BatteryStatus.BATTERY_VISIBLE_ALWAYS);
			vfm.add(batteryStatus);
			vfm.add(new WirelessStatus());
			vfm.add(new WirelessStatus(WirelessStatus.DISPLAY_DESCRIPTOR, false));
			vfm.add(new WirelessStatus(WirelessStatus.DISPLAY_SIGNAL, true));
			vfm.add(new WirelessStatus(WirelessStatus.DISPLAY_WIFI, true));
			vfm.add(new Notifications());
			vfm.add(new TimeDisplay());
			add(vfm);
			banner.addTitle("Banner Test");

			setTitle(banner);

			add(log);
			addMenuItem(new ShowDialog());
			addMenuItem(new ToggleRadio());
			addMenuItem(new ToggleBattery());
			addMenuItem(new ToggleSignal());
			addMenuItem(new ToggleNotifications());
			addMenuItem(new ToggleClock());
			addMenuItem(new ToggleIcon());
			addMenuItem(new ToggleWifi());
			addMenuItem(new ToggleCell());
		}

		public void log(String message) {
			if (message != null) {
				synchronized (UiApplication.getEventLock()) {
					System.out.println(message);
					log.setText(log.getText() + "\n" + message);
				}
			}
		}

		/**
		 * Displays a Dialog window with basic banner elements to compare to the Homescreen.
		 * 
		 * @author twindsor
		 * 
		 */
		private class ShowDialog extends MenuItem {
			public ShowDialog() {
				super("Show Dialog", 0, 100);
			}

			public void run() {
				try {
					Dialog dialog = new Dialog(Dialog.D_OK, "Banner Test", 0, null, 0);
					BatteryStatus battery = new BatteryStatus();
					battery.setDisplaySetting(BatteryStatus.BATTERY_VISIBLE_ALWAYS);
					dialog.add(battery);
					dialog.add(new WirelessStatus());
					dialog.add(new WirelessStatus(WirelessStatus.DISPLAY_DESCRIPTOR, false));
					dialog.add(new WirelessStatus(WirelessStatus.DISPLAY_SIGNAL, true));
					dialog.add(new WirelessStatus(WirelessStatus.DISPLAY_WIFI, true));
					dialog.add(new Notifications());
					dialog.add(new TimeDisplay());
					UiApplication.getUiApplication().pushGlobalScreen(dialog, UiApplication.GLOBAL_SHOW_LOWER, 0);
					Application.getApplication().requestBackground();
				} catch (Exception e) {
					log(e.toString());
				}

			}
		}

		/**
		 * Turn the Radio on/off to see the elements respond.
		 * 
		 * @author twindsor
		 * 
		 */
		private class ToggleRadio extends MenuItem {
			public ToggleRadio() {
				super("Toggle Radio", 0, 100);
			}

			public void run() {
				try {
					if (RadioInfo.getActiveWAFs() == 0) {
						Radio.activateWAFs(RadioInfo.getSupportedWAFs());
					} else {
						Radio.deactivateWAFs(RadioInfo.getSupportedWAFs());
					}
				} catch (Exception e) {
					log(e.toString());
				}

			}
		}

		private class ToggleBattery extends MenuItem {
			public ToggleBattery() {
				super("Toggle Battery", 0, 100);
			}

			public void run() {
				try {
					switch (batteryVisibility) {
					case Banner.BATTERY_VISIBLE_ALWAYS:
						batteryVisibility = Banner.BATTERY_VISIBLE_LOW_OR_CHARGING;
						break;
					case Banner.BATTERY_VISIBLE_LOW_OR_CHARGING:
					default:
						batteryVisibility = Banner.BATTERY_VISIBLE_ALWAYS;
						break;
					}
					banner.setPropertyValue(Banner.PROPERTY_BATTERY_VISIBILITY, batteryVisibility);
				} catch (Throwable e) {
					log(e.toString());
				}
			}
		}

		private class ToggleSignal extends MenuItem {
			public ToggleSignal() {
				super("Toggle Signal", 0, 100);
			}

			public void run() {
				try {
					if (hasSignal) {
						banner.removeSignalIndicator();
					} else {
						banner.addSignalIndicator();
					}
					hasSignal = !hasSignal;
				} catch (Throwable e) {
					log(e.toString());
				}
			}
		}

		private class ToggleNotifications extends MenuItem {
			public ToggleNotifications() {
				super("Toggle Notifications", 0, 100);
			}

			public void run() {
				try {
					if (hasNotifications) {
						banner.removeNotifications();
					} else {
						banner.addNotifications();
					}
					hasNotifications = !hasNotifications;
				} catch (Throwable e) {
					log(e.toString());
				}
			}
		}

		private class ToggleWifi extends MenuItem {
			public ToggleWifi() {
				super("Toggle Wifi", 0, 100);
			}

			public void run() {
				try {
					if (banner.getPropertyValue(Banner.PROPERTY_WIFI_VISIBILITY) == Banner.PROPERTY_VALUE_ON
							|| banner.getPropertyValue(Banner.PROPERTY_WIFI_VISIBILITY) == Banner.PROPERTY_VALUE_DEFAULT) {
						banner.setPropertyValue(Banner.PROPERTY_WIFI_VISIBILITY, Banner.PROPERTY_VALUE_OFF);
					} else {
						banner.setPropertyValue(Banner.PROPERTY_WIFI_VISIBILITY, Banner.PROPERTY_VALUE_ON);
					}
				} catch (Throwable e) {
					log(e.toString());
				}
			}
		}

		private class ToggleCell extends MenuItem {
			public ToggleCell() {
				super("Toggle Cell", 0, 100);
			}

			public void run() {
				try {
					if (banner.getPropertyValue(Banner.PROPERTY_CELLULAR_VISIBILITY) == Banner.PROPERTY_VALUE_ON
							|| banner.getPropertyValue(Banner.PROPERTY_CELLULAR_VISIBILITY) == Banner.PROPERTY_VALUE_DEFAULT) {
						banner.setPropertyValue(Banner.PROPERTY_CELLULAR_VISIBILITY, Banner.PROPERTY_VALUE_OFF);
					} else {
						banner.setPropertyValue(Banner.PROPERTY_CELLULAR_VISIBILITY, Banner.PROPERTY_VALUE_ON);
					}
				} catch (Throwable e) {
					log(e.toString());
				}
			}
		}

		private class ToggleClock extends MenuItem {
			public ToggleClock() {
				super("Toggle Clock", 0, 100);
			}

			public void run() {
				try {
					if (hasClock) {
						banner.removeClock();
					} else {
						banner.addClock();
					}
					hasClock = !hasClock;
				} catch (Throwable e) {
					log(e.toString());
				}
			}
		}

		private class ToggleIcon extends MenuItem {
			public ToggleIcon() {
				super("Toggle Icon", 0, 100);
			}

			public void run() {
				try {
					if (hasIcon) {
						banner.removeIcon();
					} else {
						banner.addIcon(img.getBitmap());
					}
					hasIcon = !hasIcon;
				} catch (Throwable e) {
					log(e.toString());
				}
			}
		}

	}

}
