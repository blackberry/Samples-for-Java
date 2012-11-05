package nfc.sample.nfctransaction;

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

import net.rim.device.api.io.nfc.se.TransactionListener;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.ApplicationManagerException;
import net.rim.device.api.system.Backlight;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.ByteArrayUtilities;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.nfc.CardEmulation;
import nfc.sample.nfctransaction.ui.NfcTransScreen;

public class NfcTransHandlerApp extends UiApplication implements TransactionListener {

    private static TransactionListener _tl;
    private static boolean transactionLaunch = false;
    private static int pid;

    public static final long PASSED_TO_UI_DATA_ID = 0xa2051c199827fc7eL;
    private static NfcTransScreen screen;
    private boolean running_in_background = true;

    public static void main(String[] args) {
        NfcTransHandlerApp app = new NfcTransHandlerApp();
        Utilities.initLogging();
        Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " NfcTransactionHandler V" + Constants.VERSION + " has registered with EventLogger");
        pid = ApplicationManager.getApplicationManager().getProcessId(ApplicationDescriptor.currentApplicationDescriptor());
        Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " Launching with PID = " + pid);

        Settings settings = Settings.getInstance();
        Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " Initial settings:" + settings.toString());

        if(args.length > 0) {
            dumpArgs(args);
            if(args[0].equals("auto")) {
                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " auto starting NfcTransactionHandler");

                Utilities.setUiNotRunningIndication();

                // switch on the backlight purely to ensure that the application permissions check/prompt associated with this API
                // occurs during automatic start up rather than the first time an NFC transaction is handled as this would disrupt the
                // user experience.
                
                Backlight.enable(true, 10);

                // Essential that the SIM is unlocked (PIN) and initialised before we can proceed to use JSR177
                waitForSimInitialisation();

                CardEmulation.registerTransactionListener(app);

                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " TransactionListener is exiting and will be restarted when a transaction occurs");

            } else {
                if(args[0].equals("seTransactionListener")) {
                    Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " launched by system to handle NFC transaction");
                    transactionLaunch = true;
                    app.enterEventDispatcher();
                }
            }
        } else {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " launched with no arguments");
            app.setRunning_in_background(false);
            NfcTransScreen screen = NfcTransScreen.getInstance();
            app.pushScreen(screen);
            screen.startRtsMonitorThread();
            screen.init();
            UiApplication.getApplication().requestForeground();
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " entering event dispatcher");
            app.enterEventDispatcher();
        }
    }

    private static void waitForSimInitialisation() {
        
        int mcc = GPRSInfo.getHomeMCC();
        int mnc = GPRSInfo.getHomeMNC();
        Utilities.log("XXXX MCC="+mcc+",MNC="+mnc);
        while (mcc == -1) {
            try {
                Thread.sleep(3000);
            } catch(InterruptedException e) {
            }
            mcc = GPRSInfo.getHomeMCC();
            mnc = GPRSInfo.getHomeMNC();
            Utilities.log("XXXX MCC="+mcc+",MNC="+mnc);
        }
        Utilities.log("XXXX SIM initialised.... proceeding");
    }

    private static void dumpArgs(String[] args) {
        for(int i = 0; i < args.length; i++) {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " args[" + i + "]=" + args[i]);
        }
    }

    private NfcTransHandlerApp() {
        _tl = this;
    }

    public static TransactionListener getTransactionListener() {
        return _tl;
    }

    public void onTransactionDetected(byte[][] aids) {
        Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " onTransactionDetected received call");
        int num_aids = aids.length;
        String aids_as_string = "";
        for(int i = 0; i < num_aids; i++) {
            String aid = ByteArrayUtilities.byteArrayToHex(aids[i]);
            aids_as_string = aids_as_string + "," + aid;
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " aid=" + aid);
        }
        aids_as_string = aids_as_string.substring(1, aids_as_string.length());

        showDetailsScreen(aids_as_string);
        // exit because we'll be launched afresh when the next transaction occurs so no need to linger in the background
        if(transactionLaunch) {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " onTransactionDetected: application is exiting");
            transactionLaunch = false;
            System.exit(0);
        }
    }

    private void showDetailsScreen(final String aids) {
        Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " showDetailsScreen...");
        Backlight.enable(true, 10);
        RuntimeStore store = RuntimeStore.getRuntimeStore();
        try {
            if(!Utilities.isUiRunning()) {
                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " UI not running");
                int pid = ApplicationManager.getApplicationManager().launchApplication("NfcTransactionHandler");
                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " PID = " + pid);
            } else {
                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " UI already running");
            }
        } catch(final ApplicationManagerException e) {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " Error:" + e.getMessage());
        }
        store.put(PASSED_TO_UI_DATA_ID, aids);
    }

    protected boolean acceptsForeground() {
        return !running_in_background;
    }

    public boolean isRunning_in_background() {
        return running_in_background;
    }

    public void setRunning_in_background(boolean running_in_background) {
        this.running_in_background = running_in_background;
    }
}
