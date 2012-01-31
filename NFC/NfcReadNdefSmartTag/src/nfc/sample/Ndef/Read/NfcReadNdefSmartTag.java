package nfc.sample.Ndef.Read;
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
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import nfc.sample.Ndef.Read.ui.NfcReadNdefSmartTagScreen;
import nfc.sample.Ndef.Read.ui.ListenerControlScreen;

/**
 * This class extends the UiApplication class, providing a graphical user interface.
 * 
 * It's purpose is to demonstrate the reading of a Smart Tag (NDEF tag) using the NFC java APIs available from version 7.0 of the
 * BlackBerry software.
 * 
 * The application is liberally instrumented with EventLogger Informational messages using the label "NfcReadNdefSmartTag" that
 * can be examined in the event log of a device. Use Alt-LGLG to examine the event log
 * 
 */
public class NfcReadNdefSmartTag extends UiApplication  {

    private static MainScreen _screen;
    
    private static boolean launched_from_gui=false;;

    /**
     * Standard entry point for a BlackBerry java application
     */
    public static void main(String[] args) {
        System.out.println("XXXX starting NfcReadNdefSmartTag: #args=" + args.length);

        NfcReadNdefSmartTag myApp = new NfcReadNdefSmartTag();

        /*
         * Register with the event logger
         */
        Utilities.initLogging();

        Utilities.log("XXXX NfcReadNdefSmartTag has registered with EventLogger");
        Utilities.log("XXXX NfcReadNdefSmartTag: has #args=" + args.length);
        
        if (args.length > 0) {
            Utilities.log("XXXX args[0]=" + args[0]);
            if (args[0].equals("gui")) {
                launched_from_gui = true;
            } else {
                launched_from_gui = false;
            }
        } else {
            launched_from_gui = false;
        }

        if (launched_from_gui) {
            Utilities.log("XXXX NfcReadNdefSmartTag about to push listener registration screen");
            _screen = ListenerControlScreen.getInstance();
            myApp.pushScreen(_screen);
        } else {
            // we're being auto launched by the system because an NDEF message we're interested in has been read and this app was not running
            Utilities.log("XXXX NfcReadNdefSmartTag about to push event log screen");
            _screen = NfcReadNdefSmartTagScreen.getInstance();
            myApp.pushScreen(_screen);
        }


        Utilities.log("XXXX NfcReadNdefSmartTag about to enter event dispatcher");

        /*
         * Enter the event dispatcher to receive events and handle them
         */
        
        NdefListenerManager listener_mgr = NdefListenerManager.getInstance();
        NfcReadNdefSmartTagListener listener = NfcReadNdefSmartTagListener.getInstance();

        if (listener_mgr.is_listening()) {
            // listener needs to be re-registered so it can receive any imminent callback i.e. we could be registered but not actually listening
            listener_mgr.registerListener(listener);
        }
        
        myApp.enterEventDispatcher();
    }
}
