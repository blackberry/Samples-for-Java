package nfc.sample.nfctransaction.ui;

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
import net.rim.device.api.command.Command;
import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.NFCManager;
import net.rim.device.api.io.nfc.se.SecureElement;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.AbsoluteFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.util.StringProvider;
import nfc.sample.nfctransaction.Constants;
import nfc.sample.nfctransaction.NfcTransHandlerApp;
import nfc.sample.nfctransaction.Settings;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.commands.ClearCommand;
import nfc.sample.nfctransaction.commands.CloseCommand;
import nfc.sample.nfctransaction.commands.KillCommand;
import nfc.sample.nfctransaction.commands.Iso7816Command;
import nfc.sample.nfctransaction.commands.SettingsCommand;
import nfc.sample.nfctransaction.commands.ToggleCeCommand;
import nfc.sample.nfctransaction.nfc.CardEmulation;
import nfc.sample.nfctransaction.nfc.MyNfcStatusListener;
import nfc.sample.nfctransaction.nfc.NfcService;
import nfc.sample.nfctransaction.ui.buttons.MsbConfig;
import nfc.sample.nfctransaction.ui.buttons.MsbState;
import nfc.sample.nfctransaction.ui.buttons.MultiStateButtonField;

public final class NfcTransScreen extends MainScreen implements Runnable {

    private static NfcTransScreen screen;
    private static MyNfcStatusListener nfc_status_listener = new MyNfcStatusListener();
    private Settings settings = Settings.getInstance();

    private NfcService nfc_service = NfcService.getInstance();
    private CardEmulation ce = CardEmulation.getInstance();

    private AbsoluteFieldManager mgr = new AbsoluteFieldManager();

    private Bitmap ce_on_focused = Bitmap.getBitmapResource("ce_on_focused.png");
    private Bitmap ce_on_unfocused = Bitmap.getBitmapResource("ce_on_unfocused.png");
    private Bitmap ce_on_clicked = Bitmap.getBitmapResource("ce_on_clicked.png");
    private Bitmap ce_on_unclicked = Bitmap.getBitmapResource("ce_on_focused.png");

    private Bitmap ce_off_focused = Bitmap.getBitmapResource("ce_off_focused.png");
    private Bitmap ce_off_unfocused = Bitmap.getBitmapResource("ce_off_unfocused.png");
    private Bitmap ce_off_clicked = Bitmap.getBitmapResource("ce_off_clicked.png");
    private Bitmap ce_off_unclicked = Bitmap.getBitmapResource("ce_off_focused.png");

    private Bitmap clear_focused = Bitmap.getBitmapResource("clear_focused.png");
    private Bitmap clear_unfocused = Bitmap.getBitmapResource("clear_unfocused.png");
    private Bitmap clear_clicked = Bitmap.getBitmapResource("clear_clicked.png");
    private Bitmap clear_unclicked = Bitmap.getBitmapResource("clear_focused.png");

    private Bitmap select_on_focused = Bitmap.getBitmapResource("select_focused.png");
    private Bitmap select_on_unfocused = Bitmap.getBitmapResource("select_unfocused.png");
    private Bitmap select_on_clicked = Bitmap.getBitmapResource("select_clicked.png");
    private Bitmap select_on_unclicked = Bitmap.getBitmapResource("select_focused.png");

    private Bitmap select_off_focused = Bitmap.getBitmapResource("select_disabled_focused.png");
    private Bitmap select_off_unfocused = Bitmap.getBitmapResource("select_disabled.png");
    private Bitmap select_off_clicked = Bitmap.getBitmapResource("select_disabled.png");
    private Bitmap select_off_unclicked = Bitmap.getBitmapResource("select_disabled.png");

    private Bitmap close_focused = Bitmap.getBitmapResource("close_focused.png");
    private Bitmap close_unfocused = Bitmap.getBitmapResource("close_unfocused.png");
    private Bitmap close_clicked = Bitmap.getBitmapResource("close_clicked.png");
    private Bitmap close_unclicked = Bitmap.getBitmapResource("close_focused.png");

    private Bitmap bmp_nfc_led_on = Bitmap.getBitmapResource("nfc_led_on.png");
    private Bitmap bmp_nfc_led_off = Bitmap.getBitmapResource("nfc_led_off.png");
    private Bitmap bmp_nfc_led_err = Bitmap.getBitmapResource("nfc_led_err.png");

    private Bitmap bmp_ce_led_on = Bitmap.getBitmapResource("ce_led_on.png");
    private Bitmap bmp_ce_led_off = Bitmap.getBitmapResource("ce_led_off.png");
    private Bitmap bmp_ce_led_err = Bitmap.getBitmapResource("ce_led_err.png");

    private BitmapField nfc_led = new BitmapField(bmp_nfc_led_off);
    private BitmapField cem_led = new BitmapField(bmp_ce_led_off);

    private LabelFieldColoured last_aids = new LabelFieldColoured("", Color.RED, Field.FIELD_HCENTER);
    private int last_aids_y;

    private TimedLabelField user_message = new TimedLabelField("", Field.FIELD_HCENTER);
    private int user_message_x;
    private int user_message_y;

    private Background focus_bg = BackgroundFactory.createSolidBackground(Color.LIGHTBLUE);

    private MenuItem mi_kill = new MenuItem(new StringProvider("Kill"), 110, 10);

    private MenuItem mi_settings = new MenuItem(new StringProvider("Settings"), 110, 10);

    private MultiStateButtonField msbf_ce_on;
    private MultiStateButtonField msbf_clear;
    private MultiStateButtonField msbf_select;
    private MultiStateButtonField msbf_close;

    private int focused_button = 0;

    boolean ce_enabled = false;
    boolean cem_led_err = false;
    boolean nfc_led_err = false;
    
    private FocusChangeListener focus_listener = new FocusChangeListener() {

        public void focusChanged(Field field, int eventType) {
            if(field == msbf_ce_on) {
                focused_button = 0;
            }
            if(field == msbf_clear) {
                focused_button = 1;
            }
            if(field == msbf_select) {
                focused_button = 2;
            }
            if(field == msbf_close) {
                focused_button = 3;
            }
        }
    };
    
    private FieldChangeListener centre_text_listener = new FieldChangeListener() {
        public void fieldChanged(Field field, int context) {
            if (field == user_message || field == last_aids) {
                LabelField label = (LabelField) field;
                String content = label.getText();
                int msg_width = Utilities.getTextWidth(content);
                int x = (Display.getWidth() - msg_width) / 2;
                int y=0;
                if (field == user_message) {
                    y = user_message_y;
                } else {
                    y = last_aids_y;
                }
                Utilities.log("XXXX " + Thread.currentThread().getName() + " repositioning user message:" + x+","+user_message_y);
                mgr.setPosChild(field, x, y);
            }
        }
    };

    public static synchronized NfcTransScreen getInstance() {
        if(screen == null) {
            screen = new NfcTransScreen();
        }
        return screen;
    }

    private NfcTransScreen() {
        super(USE_ALL_HEIGHT | USE_ALL_WIDTH | FIELD_HCENTER | NO_VERTICAL_SCROLL);
        Utilities.log("XXXX " + Thread.currentThread().getName() + " constructing NfcTransScreen");
        setTitle("NFCTransactionHandler V" + Constants.VERSION);

        clearLastAIDs();
        
        user_message.setChangeListener(centre_text_listener);
        last_aids.setChangeListener(centre_text_listener);

        mi_kill.setCommandContext(this);
        mi_kill.setCommand(new Command(new KillCommand()));
        addMenuItem(mi_kill);

        mi_settings.setCommandContext(this);
        mi_settings.setCommand(new Command(new SettingsCommand()));
        addMenuItem(mi_settings);

        try {
            if(nfc_service.isNfcEnabled()) {
                nfc_led = new BitmapField(bmp_nfc_led_on);
            } else {
                nfc_led = new BitmapField(bmp_nfc_led_off);
            }
        } catch(NFCException e2) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " from isNfcEnabled()");
            nfc_led = new BitmapField(bmp_nfc_led_err);
        }
        nfc_led.setPadding(7, 0, 0, 0);

        try {
            ce.initCe(ce.getCurrentSecureElement());
            if(!ce.isSe_obtained()) {
                Utilities.log("XXXX " + Thread.currentThread().getName() + " SecureElement(SIM) not available");
                setUserMessage("Error: could not acquire SE");
                cem_led_err = true;
            } else {
                try {
                    ce_enabled = ce.isCeEnabled(SecureElement.BATTERY_ON_MODE);
                    if(ce_enabled) {
                        cem_led = new BitmapField(bmp_ce_led_on);
                    } else {
                        cem_led = new BitmapField(bmp_ce_led_off);
                    }
                } catch(Exception e) {
                    Utilities.log("XXXX " + Thread.currentThread().getName() + " exception when enabling CE: " + e.getClass().getName() + ":" + e.getMessage());
                    cem_led = new BitmapField(bmp_ce_led_err);
                }
            }
        } catch(NFCException e1) {
            Utilities.log("XXXX " + Thread.currentThread().getName() + " exception when getting SE: " + e1.getClass().getName() + ":" + e1.getMessage());
            setUserMessage("Error: could not acquire SE");
            nfc_led_err = true;
            cem_led_err = true;
        }
        cem_led.setPadding(7, 0, 0, 0);

        // bitmaps are 120 x 120
        int hgap = (int) ((Display.getWidth() - 240) / 3);
        int vgap = (int) (Display.getHeight() - 240) / 6;
        int x1 = hgap;
        int x2 = x1 + 120 + hgap;
        int y1 = bmp_nfc_led_on.getHeight() + 4;
        int y2 = y1 + 120 + (vgap / 4);
        int y3 = y2 + 120 + 10;
        last_aids_y = y3;
        user_message_y = y3 + vgap;
        user_message_x = (Display.getWidth() - Utilities.getTextWidth(Constants.READY)) / 2;
        int x_centre_1 = x1 + 60;
        int x_centre_2 = x2 + 60;
        int last_aids_x = (Display.getWidth() - Utilities.getTextWidth(Constants.AID_SPACE)) / 2;

        // centre LEDs over the button columns
        mgr.add(nfc_led, x_centre_1 - (bmp_nfc_led_on.getWidth() / 2), 0);
        mgr.add(cem_led, x_centre_2 - (bmp_ce_led_on.getWidth() / 2), 0);
        
        MsbConfig config1 = new MsbConfig();
        MsbState ce_off = new MsbState(Constants.BTN_CE_OFF, "Switch card emulation OFF", "CE OFF");
        ce_off.setbmp_focused(ce_off_focused);
        ce_off.setbmp_unfocused(ce_off_unfocused);
        ce_off.setbmp_clicked(ce_off_clicked);
        ce_off.setbmp_unclicked(ce_off_unclicked);
        config1.addState(ce_off);
        MsbState ce_on = new MsbState(Constants.BTN_CE_ON, "Switch card emulation ON", "CE ON");
        ce_on.setbmp_focused(ce_on_focused);
        ce_on.setbmp_unfocused(ce_on_unfocused);
        ce_on.setbmp_clicked(ce_on_clicked);
        ce_on.setbmp_unclicked(ce_on_unclicked);
        config1.addState(ce_on);
        msbf_ce_on = new MultiStateButtonField(config1, new ToggleCeCommand(), 0, Field.FIELD_HCENTER);
        mgr.add(msbf_ce_on, x1, y1);

        MsbConfig config2 = new MsbConfig();
        MsbState clear = new MsbState(Constants.BTN_DEFAULT, "Clear last AID received field", "Clear");
        clear.setbmp_focused(clear_focused);
        clear.setbmp_unfocused(clear_unfocused);
        clear.setbmp_clicked(clear_clicked);
        clear.setbmp_unclicked(clear_unclicked);
        config2.addState(clear);
        msbf_clear = new MultiStateButtonField(config2, new ClearCommand(), 0, Field.FIELD_HCENTER);
        mgr.add(msbf_clear, x2, y1);

        MsbConfig config3 = new MsbConfig();
        MsbState select_on = new MsbState(Constants.BTN_DEFAULT, "Select applet", "Select");
        select_on.setbmp_focused(select_on_focused);
        select_on.setbmp_unfocused(select_on_unfocused);
        select_on.setbmp_clicked(select_on_clicked);
        select_on.setbmp_unclicked(select_on_unclicked);
        config3.addState(select_on);
        MsbState select_off = new MsbState(Constants.BTN_SELECT_OFF, "Select applet", "Select");
        select_off.setbmp_focused(select_off_focused);
        select_off.setbmp_unfocused(select_off_unfocused);
        select_off.setbmp_clicked(select_off_clicked);
        select_off.setbmp_unclicked(select_off_unclicked);
        config3.addState(select_off);
        Iso7816Command select_command = new Iso7816Command();
        msbf_select = new MultiStateButtonField(config3, select_command, 0, Field.FIELD_HCENTER);
        mgr.add(msbf_select, x1, y2);

        MsbConfig config4 = new MsbConfig();
        MsbState close = new MsbState(0, "Send to background", "Close");
        close.setbmp_focused(close_focused);
        close.setbmp_unfocused(close_unfocused);
        close.setbmp_clicked(close_clicked);
        close.setbmp_unclicked(close_unclicked);
        config4.addState(close);
        msbf_close = new MultiStateButtonField(config4, new CloseCommand(), 0, Field.FIELD_HCENTER);
        mgr.add(msbf_close, x2, y2);
        
        mgr.add(last_aids, last_aids_x, last_aids_y);
        mgr.add(user_message, user_message_x, user_message_y);

        add(mgr);

        msbf_ce_on.setFocusListener(focus_listener);
        msbf_clear.setFocusListener(focus_listener);
        msbf_select.setFocusListener(focus_listener);
        msbf_close.setFocusListener(focus_listener);

        screen = this;
        setUiRunningIndication();
    }

    public void init() {
        setCeButtonState(ce_enabled);
        try {
            NFCManager.addNFCStatusListener(nfc_status_listener);
        } catch(NFCException e) {
            Utilities.log("XXXX exception when adding NFCStatusListener: " + e.getClass().getName() + ":" + e.getMessage());
        }
        focused_button = 0;
        msbf_ce_on.setFocus();
        msbf_ce_on.setImage();

    }

    private void setUiRunningIndication() {
        Object ui_running_token = new Object();
        RuntimeStore rts = RuntimeStore.getRuntimeStore();
        rts.replace(Constants.UI_IS_RUNNING, ui_running_token);
    }

    public void startRtsMonitorThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public boolean onClose() {
        closeScreen();
        return false;
    }

    public void setAidsReceived(String aids) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " updating UI with AID:" + aids);
        synchronized(UiApplication.getUiApplication().getEventLock()) {
            last_aids.setText(aids);
        }
    }

    public void run() {
        boolean running = true;
        Utilities.log("XXXX " + Thread.currentThread().getName() + " Starting RuntimeStore monitoring for screen");
        while(running) {
            try {
                RuntimeStore rts = RuntimeStore.getRuntimeStore();
                try {
                    Utilities.log("XXXX " + Thread.currentThread().getName() + " waiting for object from RTS");
                    Object obj = rts.waitFor(NfcTransHandlerApp.PASSED_TO_UI_DATA_ID);
                    Utilities.log("XXXX " + Thread.currentThread().getName() + " got object from RTS=" + obj);
                    if(obj != null) {
                        String aids_received = (String) rts.remove(NfcTransHandlerApp.PASSED_TO_UI_DATA_ID);
                        Utilities.log("XXXX " + Thread.currentThread().getName() + " AIDs received are:" + aids_received);
                        setAidsReceived(Utilities.hexPresentation(aids_received));
                        UiApplication.getApplication().requestForeground();

                    }
                } catch(RuntimeException e) {
                    Utilities.log("XXXX " + Thread.currentThread().getName() + " RunTimeException - probably timed out waiting for object in RTS");
                }

            } catch(Exception e) {
                Utilities.log("XXXX " + Thread.currentThread().getName() + " Exception in RTS loop: " + e.getClass().getName() + ":" + e.getMessage());
                System.out.println("XXXX " + Thread.currentThread().getName() + " Exception in RTS loop: " + e.getClass().getName() + ":" + e.getMessage());
            }
        }
    }

    public void clearLastAIDs() {
        last_aids.setText(Constants.AID_SPACE);
        Utilities.log("XXXX " + Thread.currentThread().getName() + " AID field cleared");
    }

    public void closeScreen() {
        // don't actually close otherwise our thread which is receiving the NFC transactions will exit as the app will be fully
        // closed
        UiApplication.getUiApplication().requestBackground();
    }

    public void setNfcLed(int state) {
        synchronized(UiApplication.getEventLock()) {
            switch(state) {
            case Constants.LED_OFF:
                nfc_led.setBitmap(bmp_nfc_led_off);
                break;
            case Constants.LED_ON:
                nfc_led.setBitmap(bmp_nfc_led_on);
                break;
            case Constants.LED_ERR:
                Utilities.log("XXXX " + Thread.currentThread().getName() + " NFC LED set to ERR state");
                nfc_led.setBitmap(bmp_nfc_led_err);
                break;
            }
        }
    }

    public void setCemLed(final int state) {
        UiApplication.getUiApplication().invokeAndWait(new Runnable() {
            public void run() {
                switch(state) {
                case Constants.LED_OFF:
                    cem_led.setBitmap(bmp_ce_led_off);
                    break;
                case Constants.LED_ON:
                    cem_led.setBitmap(bmp_ce_led_on);
                    break;
                case Constants.LED_ERR:
                    Utilities.log("XXXX " + Thread.currentThread().getName() + " CEM LED set to ERR state");
                    cem_led.setBitmap(bmp_ce_led_err);
                    break;
                }
            }
        });
    }

    public void nfcStateChanged(boolean nfc_on, boolean ce_on) {
        if(nfc_on) {
            setNfcLed(Constants.LED_ON);
            if(ce_on) {
                setCemLed(Constants.LED_ON);
                setCeButtonState(true);
                setSelectButtonState(true);
            }
        } else {
            setNfcLed(Constants.LED_OFF);
            setCemLed(Constants.LED_OFF);
            setCeButtonState(false);
            setSelectButtonState(false);
            try {
                ce.setRoutingOff();
            } catch(NFCException e) {
            }
        }
    }

    public void setUserMessage(final String message) {
        UiApplication.getUiApplication().invokeAndWait(new Runnable() {
            public void run() {
                user_message.setLabelText(message);
            }
        });
    }

    public void setCeButtonState(final boolean on) {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " setting CE button state to on=" + on);
        UiApplication.getUiApplication().invokeAndWait(new Runnable() {
            public void run() {
                if(on) {
                    msbf_ce_on.setMsbState(Constants.BTN_CE_OFF);
                } else {
                    msbf_ce_on.setMsbState(Constants.BTN_CE_ON);
                }
            }
        });
    }

    public void setSelectButtonState(final boolean on) {
        UiApplication.getUiApplication().invokeAndWait(new Runnable() {
            public void run() {
                if(on) {
                    msbf_select.setMsbState(Constants.BTN_SELECT_ON);
                } else {
                    msbf_select.setMsbState(Constants.BTN_SELECT_OFF);
                }
            }
        });
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time) {

        int x_dir = 0;
        int y_dir = 0;

        if(dx > 0) {
            x_dir = 1;
        }
        if(dx < 0) {
            x_dir = -1;
        }

        if(dy > 0) {
            y_dir = 1;
        }
        if(dy < 0) {
            y_dir = -1;
        }
        setFocusedButton(x_dir, y_dir);
        return true;
    }

    private void setFocusedButton(int x_dir, int y_dir) {
        switch(focused_button) {
        case 0:
            if(x_dir == 1 && y_dir == 0) {
                focused_button = 1;
                msbf_clear.setFocus();
                return;
            }
            if(x_dir == 0 && y_dir == 1) {
                focused_button = 2;
                msbf_select.setFocus();
                return;
            }
            if(x_dir == 1 && y_dir == 1) {
                focused_button = 3;
                msbf_close.setFocus();
                return;
            }
            break;
        case 1:
            if(x_dir == -1 && y_dir == 0) {
                focused_button = 0;
                msbf_ce_on.setFocus();
                return;
            }
            if(x_dir == 0 && y_dir == 1) {
                focused_button = 3;
                msbf_close.setFocus();
                return;
            }
            if(x_dir == -1 && y_dir == 1) {
                focused_button = 2;
                msbf_select.setFocus();
                return;
            }
            break;
        case 2:
            if(x_dir == 1 && y_dir == 0) {
                focused_button = 3;
                msbf_close.setFocus();
                return;
            }
            if(x_dir == 0 && y_dir == -1) {
                focused_button = 0;
                msbf_ce_on.setFocus();
                return;
            }
            if(x_dir == 1 && y_dir == -1) {
                focused_button = 1;
                msbf_clear.setFocus();
                return;
            }
            break;
        case 3:
            if(x_dir == -1 && y_dir == 0) {
                focused_button = 2;
                msbf_select.setFocus();
                return;
            }
            if(x_dir == 0 && y_dir == -1) {
                focused_button = 1;
                msbf_clear.setFocus();
                return;
            }
            if(x_dir == -1 && y_dir == -1) {
                focused_button = 0;
                msbf_ce_on.setFocus();
                return;
            }
            break;
        }
    }

}
