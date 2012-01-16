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
package nfc.sample.virtual.target;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.virtual.target.ui.RoleSelectionScreen;

/**
 * The purpose of this app is to demonstrate the emulation and reading of NFC virtual tags.
 * 
 * The application is liberally instrumented with EventLogger Informational
 * messages using the label "NfcVirtTargFun" that can be examined in the
 * event log of a device. Use Alt-LGLG to set the log level to Information
 * and to examine the event log or use javaloader -u eventlog>eventlog.txt to 
 * extract the log to your PC over USB.
 * 
 *  Authors: John Murray and Martin Woolley, Research In Motion
 * 
 */
public class NfcVirtTarg extends UiApplication {

	private static MainScreen _screen;

	/**
	 * Standard entry point for a BlackBerry java application
	 */
	public static void main(String[] args) {
		System.out.println("XXXX starting NfcVirtTarg: #args=" + args.length);

		NfcVirtTarg myApp = new NfcVirtTarg();

		/*
		 * Register with the event logger
		 */
		Utilities.initLogging();


		Utilities.log("XXXX NfcVirtTarg has registered with EventLogger");
		Utilities.log("XXXX NfcVirtTarg: has #args=" + args.length);

		_screen = new RoleSelectionScreen();

		Utilities.log("XXXX NfcVirtTarg about to push main screen");

		/*
		 * Push the screen onto the stack
		 */
		myApp.pushScreen(_screen);

		Utilities.log("XXXX NfcVirtTarg about to enter event dispatcher");

		/*
		 * Enter the event dispatcher to receive events and handle them
		 */
		myApp.enterEventDispatcher();
	}
}
