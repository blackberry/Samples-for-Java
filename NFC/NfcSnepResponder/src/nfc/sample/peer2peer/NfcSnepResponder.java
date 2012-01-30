package nfc.sample.peer2peer;

import net.rim.device.api.ui.UiApplication;
import nfc.sample.peer2peer.ui.NfcSnepResponderScreen;
import nfc.sample.peer2peer.ui.RoleSelectionScreen;

/**
 * This class extends the UiApplication class, providing a graphical user
 * interface.
 * 
 * It's purpose is to demonstrate the emulation and reading of virtual tags.
 * 
 * The application is liberally instrumented with EventLogger Informational
 * messages using the label "NfcSnepResponder" that can be examined in the
 * event log of a device. Use Alt-LGLG to examine the event log
 * 
 * 
 * Authors: John Murray and Martin Woolley
 * 
 */
public class NfcSnepResponder extends UiApplication {

	private static RoleSelectionScreen _screen;

	/**
	 * Standard entry point for a BlackBerry java application
	 */
	public static void main(String[] args) {
		System.out.println("XXXX starting NfcSnepResponder: #args=" + args.length);

		NfcSnepResponder myApp = new NfcSnepResponder();

		/*
		 * Register with the event logger
		 */
		Utilities.initLogging();


		Utilities.log("XXXX NfcSnepResponder has registered with EventLogger");
		Utilities.log("XXXX NfcSnepResponder: has #args=" + args.length);

		/*
		 * This is the screen we'll use
		 */
	    _screen = new RoleSelectionScreen();

		Utilities.log("XXXX NfcSnepResponder about to push main screen");

		/*
		 * Push the screen onto the stack
		 */
		myApp.pushScreen(_screen);

		Utilities.log("XXXX NfcSnepResponder about to enter event dispatcher");

		/*
		 * Enter the event dispatcher to receive events and handle them
		 */
		myApp.enterEventDispatcher();
	}
}
