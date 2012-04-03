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
 * 
 * 
 * This sample application illustrates various aspects of NFC card emulation.
 * 
 * Authors: John Murray and Martin Woolley
 * 
 */

package nfc.sample.midlet;

/**
 * Shows PushRegistry being used by a MIDlet for NFC transactions.
 * 
 *  Register with the PushRegistry so that the MIDlet is launched automatically when not running
 *  in startApp add a transaction listener so that it can receive callbacks when already running (push registry will not launch already running applications)
 *  in destroyApp remove the transaction listener
 *  
 *  In the jad file, ensure the MIDlet is automatically launched on device reset so that the TransactionListener can be registered
 *  RIM-MIDlet-Flags-1: 1
 *  
 *  Updated from original (unreleased) sample app "NfcMidlet" to exploit improvements made in Apollo build 7.0 bundle 2414) such that there is no need to
 *  
 *  -   To unregister from the push registry on your MIDlet startup as you can now be registered in the PushRegistry and as a TransactionListener
 *  -   To background/foreground your MIDlet at startup
 *  
 *  If you experience issues whereby the system reports the AID is already registered when attempting to add your TransactionListener then you need to
 *  upgrade your device. See NFC Developer FAQ item 4:
 *  
 *  http://supportforums.blackberry.com/t5/Java-Development/NFC-Developer-FAQ/ta-p/1634793
 *  
 */

import java.io.IOException;
import java.util.Date;

import javax.microedition.io.PushRegistry;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.*;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.emulation.TechnologyType;
import net.rim.device.api.io.nfc.se.SecureElement;
import net.rim.device.api.io.nfc.se.SecureElementManager;
import net.rim.device.api.io.nfc.se.TransactionListener;
import net.rim.device.api.system.Backlight;
import net.rim.device.api.system.RuntimeStore;

public class NfcMidlet2 extends MIDlet implements CommandListener {

    public static final long TRANSACTION_LISTENER = 0xf435b57918ee015eL;
    private static final long RUN_PREVIOUSLY_TOKEN = 0xc8c9f2bf4c371254L;
    private static final String CONNECTION_STRING = "apdu:0;target=6E.66.63.74.65.73.74.30.31";
    private Command exitCommand;
    private static TextBox tb;
    String connections[];

    MyTransactionListener myListener;

    private RuntimeStore runtimeStore = RuntimeStore.getRuntimeStore();

    public static byte[][] MY_AID = { { 0x6E, 0x66, 0x63, 0x74, 0x65, 0x73, 0x74, 0x30, 0x31 } };

    private static javax.microedition.lcdui.Display display;

    private static MIDlet midlet;

    private static int midlet_state;
    private static final int MIDLET_PAUSED = 0;
    private static final int MIDLET_ACTIVE = 1;
    private static final int MIDLET_DESTROYED = 2;

    private static String alert_message = "";
    private static boolean alert_pending = false;
    private static boolean terminate = false;

    public NfcMidlet2() {
        Utilities.initLogging();

        Utilities.log("XXXX " + Thread.currentThread().getName() + " MIDlet constructed");

        display = javax.microedition.lcdui.Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 1);
        tb = new TextBox("NfcMidlet2 V1.14", "MIDlet awaiting NFC transaction", 50, 0);

        addToPushRegistryIfNecessary();

        // register as a TransactionListener too so we can receive NFC events when in foreground.
        addTransactionListener();

        midlet = this;
        midlet_state = MIDLET_PAUSED;
        // reset statics
        alert_message = "";
        alert_pending = false;
    }

    public void addToPushRegistryIfNecessary() {
        String registered_midlet = PushRegistry.getMIDlet(CONNECTION_STRING);
        Utilities.log("XXXX " + Thread.currentThread().getName() + " MIDlet registered for " + CONNECTION_STRING + "=" + registered_midlet);
        if(registered_midlet == null) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " first time so registering with push registry");
            addToPushRegistry();
        }
    }

    public void startApp() {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " startApp");

        if(hasRunPreviously()) {
            String[] availableConnections = PushRegistry.listConnections(true);
            if(availableConnections.length > 0) {
                getTransactionFromPushRegistry(availableConnections);
            }

            midlet_state = MIDLET_ACTIVE;

            Backlight.enable(true);

            tb.addCommand(exitCommand);
            tb.setCommandListener(this);
            if(!alert_pending) {
                display.setCurrent(tb);
            } else {
                doAlert(alert_message);
                alert_pending = false;
            }
        } else {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " MIDlet running for first time so assuming we're auto-running");
            setRunPreviously();
            destroyApp(true);
            notifyDestroyed();
        }
    }

    public void pauseApp() {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " pauseApp");
        midlet_state = MIDLET_PAUSED;
    }

    public void destroyApp(boolean ignore) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " destroyApp");

        // reset statics
        alert_message = "";
        alert_pending = false;

        // transaction listener can now be removed
        removeTransactionListener();

        midlet_state = MIDLET_DESTROYED;
    }

    public void addTransactionListener() {
        SecureElementManager sem = SecureElementManager.getInstance();
        if(sem == null) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:SecureElementManager instance is null - exiting");
            return;
        }

        Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:getting SecureElement instance of type SIM");
        SecureElement se = null;
        try {
            se = sem.getSecureElement(SecureElement.SIM);
        } catch(Exception e1) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:exception when getting SE: " + e1.getClass().getName() + ":" + e1.getMessage());
        }

        if(se == null) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:SecureElement(SIM) is null - exiting");
            return;
        }

        myListener = getTransactionListener();

        try {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:adding transaction listener");

            try {
                se.addTransactionListener(myListener, MY_AID);
            } catch(NFCException e) {
                if(e.getMessage().startsWith("TransactionListener has already been added") || e.getMessage().startsWith("AID has already been registered")) {
                    Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:already registered - ignoring");
                }
            }
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:added transaction listener OK");
        } catch(Exception e) {
            if(!e.getMessage().startsWith("TransactionListener has already been added") && !e.getMessage().startsWith("AID has already been registered")) {
                Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:exception when adding transaction listener: " + e.getClass().getName() + ":" + e.getMessage());
                return;
            } else {
                Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:exception when adding transaction listener: " + e.getClass().getName() + ":" + e.getMessage());
                return;
            }
        }

        try {
            se.setTechnologyTypes(SecureElement.BATTERY_ON_MODE, TechnologyType.ISO14443B);
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:NFC routing established OK");
        } catch(NFCException e) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addTransactionListener:exception when setting technology types: " + e.getClass().getName() + ":" + e.getMessage());
        }

        try {
            runtimeStore.replace(TRANSACTION_LISTENER, myListener);
        } catch(IllegalArgumentException e) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " RuntimeStore.replace exception: " + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    private boolean hasRunPreviously() {
        Object prev_run_token = runtimeStore.get(RUN_PREVIOUSLY_TOKEN);
        return(prev_run_token != null);
    }

    private void setRunPreviously() {
        Object prev_run_token = new Object();
        runtimeStore.replace(RUN_PREVIOUSLY_TOKEN, prev_run_token);
    }

    public MyTransactionListener getTransactionListener() {
        MyTransactionListener tl = (MyTransactionListener) runtimeStore.get(TRANSACTION_LISTENER);
        if(tl == null) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " allocating new TransactionListener");
            tl = new MyTransactionListener();
            Utilities.log("XXXX " + Thread.currentThread().getName() + " new TransactionListener=" + tl);
            runtimeStore.replace(TRANSACTION_LISTENER, tl);
        }
        return tl;
    }

    public void removeTransactionListener() {
        SecureElementManager sem = SecureElementManager.getInstance();
        if(sem == null) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " removeTransactionListener instance is null - exiting");
            return;
        }

        Utilities.log("XXXX " + Thread.currentThread().getName() + " removeTransactionListener SecureElement instance of type SIM");
        SecureElement se = null;
        try {
            se = sem.getSecureElement(SecureElement.SIM);
        } catch(Exception e1) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " removeTransactionListener when getting SE: " + e1.getClass().getName() + ":" + e1.getMessage());
        }

        if(se == null) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " removeTransactionListener(SIM) is null - exiting");
            return;
        }
        try {
            myListener = getTransactionListener();
            Utilities.log("XXXX " + Thread.currentThread().getName() + " TransactionListener=" + myListener);
            if(myListener != null) {
                se.removeTransactionListener(myListener);
                Utilities.log("XXXX " + Thread.currentThread().getName() + " removeTransactionListener listener OK");
            }
        } catch(Exception e) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " removeTransactionListener when removing TransactionListener:" + e.getClass().getName() + ":" + e.getMessage());
        }

        Utilities.log("XXXX " + Thread.currentThread().getName() + " removed TransactionListener from RuntimeStore");
        runtimeStore.remove(TRANSACTION_LISTENER);

    }

    private static void foreground() {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " switching screen to foreground");
        midlet.resumeRequest();
    }

    private static void queueAlert(String message) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " queueing alert for when MIDlet is next active");
        alert_message = message;
        alert_pending = true;
    }

    public static void alertTransaction(String message) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " displaying alert");
        Alert alert = new Alert(message);
        display.setCurrent(alert, tb);
    }

    public static void doAlert(String message) {
        switch(midlet_state) {
        case MIDLET_ACTIVE:
            alertTransaction(message);
            break;
        case MIDLET_PAUSED:
            queueAlert(message);
            foreground();
            break;
        }
    }

    private void getTransactionFromPushRegistry(String[] availableConnections) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " checking for waiting message from PushRegistry");
        if(availableConnections != null && availableConnections.length > 0) {
            // I'm assuming there's only ever one I care about and all I need is the AID just like in a TransactionListener call
            // back
            // apdu:0;target=4e.46.43.54.65.73.74.65.72.20.31.2e.30
            int index = availableConnections[0].indexOf("target=");
            if(index > -1) {
                Utilities.log("XXXX " + Thread.currentThread().getName() + " got message from PushRegistry:" + availableConnections[0]);
                String aid = availableConnections[0].substring(index + 7);
                String message = new Date() + " - transaction from AID " + aid;
                doAlert(message);
            }
        }
    }

    public void commandAction(Command cmd, Displayable arg1) {
        if(cmd == exitCommand) {
            exitApp();
        }
    }

    public void exitApp() {
        destroyApp(false);
        notifyDestroyed();
    }

    public void addToPushRegistry() {
        try {
            PushRegistry.registerConnection(CONNECTION_STRING, this.getClass().getName(), "*");
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addToPushRegistry:registered ok");
        } catch(ClassNotFoundException e) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addToPushRegistry:exception registering connection: " + e.getClass().getName() + ":" + e.getMessage());
        } catch(IOException e) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " addToPushRegistry:exception registering connection: " + e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public void listConnectionStrings(String[] conns) {
        if(conns != null) {
            for(int i = 0; i < conns.length; i++) {
                Utilities.log("XXXX " + Thread.currentThread().getName() + " connection:" + conns[i]);
            }
        } else {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " no connections");
        }
    }

    private static class MyTransactionListener implements TransactionListener {

        public void onTransactionDetected(byte[][] aids) {
            String aid = new String(aids[0]);
            Utilities.log("XXXX " + Thread.currentThread().getName() + " onTransactionDetected:" + aid);

            // switch on the backlight. If device is locked it will be automatically unlocked.
            Backlight.enable(true);

            String message = new Date() + " - transaction from AID " + aid;
            doAlert(message);
        }
    }
}
