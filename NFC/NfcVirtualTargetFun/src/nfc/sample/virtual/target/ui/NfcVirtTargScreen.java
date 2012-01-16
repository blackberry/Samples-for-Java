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
package nfc.sample.virtual.target.ui;

import net.rim.device.api.command.Command;
import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.emulation.VirtualISO14443Part4TypeATarget;
import net.rim.device.api.io.nfc.readerwriter.ReaderWriterManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.StringProvider;
import nfc.sample.virtual.target.Constants;
import nfc.sample.virtual.target.NfcVirtDetectionListener;
import nfc.sample.virtual.target.NfcVirtTargListener;
import nfc.sample.virtual.target.Utilities;
import nfc.sample.virtual.target.commands.ResponseTextCommand;
import nfc.sample.virtual.target.commands.StartCommand;
import nfc.sample.virtual.target.commands.StopCommand;

/**
 * A class extending the MainScreen class, which provides default standard behaviour for BlackBerry GUI applications.
 */
public final class NfcVirtTargScreen extends MainScreen {

    private NfcVirtTargScreen _screen;

    private HorizontalFieldManager icon_row = new HorizontalFieldManager();
    private Bitmap sc_icon = Bitmap.getBitmapResource("sc_icon.png");
    private Bitmap reader_icon = Bitmap.getBitmapResource("reader_icon.png");

    private BitmapField icon;

    private String response_text = "Send to other device";

    private SeparatorField separator1 = new SeparatorField(Field.USE_ALL_WIDTH);
    private SeparatorField separator2 = new SeparatorField(Field.USE_ALL_WIDTH);

    private RichTextField heading = new RichTextField();

    private Font heading_font;

    private AlternatingListField _log = new AlternatingListField(Color.WHITE,Color.LIGHTGRAY,Field.USE_ALL_WIDTH);

    private MenuItem mi_stop = new MenuItem(new StringProvider("Stop Emulation"), 110, 10);
    private MenuItem mi_start = new MenuItem(new StringProvider("Restart Emulation"), 110, 10);
    private MenuItem mi_response = new MenuItem(new StringProvider("Set Response Text"), 110, 10);

    private VirtualISO14443Part4TypeATarget _virtualISO14443ATarget;
    private NfcVirtDetectionListener _detectionListener;

    private int _emulation_type;

    /**
     * Creates a new NfcVirtTargScreen object
     */
    public NfcVirtTargScreen(int emulation_type) {

        super(MainScreen.HORIZONTAL_SCROLL);
        this._screen = this;
        this._detectionListener = new NfcVirtDetectionListener(_screen);
        _emulation_type = emulation_type;

        Utilities.log("XXXX NfcVirtTarg entered NfcVirtTargScreen constructor");
        setTitle("NfcVirtualTargetFun V" + Constants.MYAPP_VERSION);

        if(emulation_type == Constants.EMULATE_SC) {
            emulateSmartCard();
        } else {
            emulateReader();
        }
        mi_stop.setCommandContext(this);
        mi_stop.setCommand(new Command(new StopCommand(this)));
        addMenuItem(mi_stop);

        mi_start.setCommandContext(this);
        mi_start.setCommand(new Command(new StartCommand(this)));

        Font default_font = Font.getDefault();
        heading_font = default_font.derive(Font.BOLD, 24);

        heading.setText("Event Log (newest items first)");
        heading.setFont(heading_font);

        add(separator1);
        add(heading);
        add(separator2);
        add(_log);

        Utilities.log("XXXX NfcVirtTarg leaving NfcVirtTargScreen constructor");
    }

    private void emulateSmartCard() {
        icon = new BitmapField(sc_icon);
        icon_row.add(icon);
        icon_row.add(new LabelField(" - emulating smart card"));
        add(icon_row);
        mi_response.setCommandContext(this);
        mi_response.setCommand(new Command(new ResponseTextCommand()));
        addMenuItem(mi_response);
        startEmulationOfISO14443ATarget();

    }

    private void emulateReader() {
        icon = new BitmapField(reader_icon);
        icon_row.add(icon);
        icon_row.add(new LabelField(" - emulating reader"));
        add(icon_row);
        startDetectionListener(_detectionListener);
    }

    protected void stopDetectionListener(NfcVirtDetectionListener detectionListener) {

        ReaderWriterManager nfcManager;

        Utilities.log("XXXX NfcVirtTarg about to remove Detection Listener");

        try {
            nfcManager = ReaderWriterManager.getInstance();
            /*
             * This is the converse of the call to register our interest in tags of a certain type.
             */
            nfcManager.removeDetectionListener(detectionListener);

            Utilities.log("XXXX NfcVirtTarg remove Detection Listener success");
            _screen.logEvent("info:Detection Listener removed");

        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcVirtTarg NFCException on unregister");
            _screen.logEvent("info:Detection Listener remove failed");
        }
    }

    protected void startDetectionListener(NfcVirtDetectionListener detectionListener) {

        ReaderWriterManager nfcManager;

        Utilities.log("XXXX NfcVirtTarg about to add Detection Listener");

        try {
            nfcManager = ReaderWriterManager.getInstance();
            nfcManager.addDetectionListener(detectionListener);

            Utilities.log("XXXX NfcVirtTarg add Detection Listener success");
            _screen.logEvent("info:Detection Listener added");

        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcVirtTarg NFCException on register");
            _screen.logEvent("info:Detection Listener add failed");
        }

    }

    protected void stopEmulationOfISO14443ATarget() {
        Utilities.log("XXXX NfcVirtTarg About to stop ISO14443-A virtual emulation");
        try {
            _virtualISO14443ATarget.stopEmulation();
            Utilities.log("XXXX NfcVirtTarg ISO14443-A emulation stopped");
            _screen.logEvent("info:ISO14443-A emulation stopped");
        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcVirtTarg NFCException on stopping ISO14443-A emulation");
            _screen.logEvent("info:Stopping ISO 14443-A emulation failed");

        }
    }

    protected void startEmulationOfISO14443ATarget() {
        Utilities.log("XXXX NfcVirtTarg About to start ISO14443-A virtual emulation");
        try {
            // note that the ID parameter is not actually used by the API
            _virtualISO14443ATarget = new VirtualISO14443Part4TypeATarget(new NfcVirtTargListener(this),
                    Constants.MY_ISO_TARGET_ID, null);
            _virtualISO14443ATarget.startEmulation();
            Utilities.log("XXXX NfcVirtTarg ISO14443-A emulation started");
            _screen.logEvent("info:ISO14443-A emulation started");

        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcVirtTarg NFCException on starting ISO14443-A emulation");
            _screen.logEvent("info:Starting ISO 14443-A emulation failed");
        }
    }

    private void stopSmartcardEmulation() {
        Utilities.log("XXXX NfcVirtTarg handling Unregister To Send button event");

        logEvent("info:Stop sending data option selected");

        stopEmulationOfISO14443ATarget();
    }

    private void stopReaderEmulation() {
        Utilities.log("XXXX NfcVirtTarg handling Unregister To Receive button event");

        logEvent("info:Stop receiving data option selected");

        stopDetectionListener(_detectionListener);
    }

    public void start() {
        removeMenuItem(mi_start);
        addMenuItem(mi_stop);
        switch(_emulation_type) {
        case Constants.EMULATE_SC:
            startEmulationOfISO14443ATarget();
            break;
        case Constants.EMULATE_SR:
            startDetectionListener(_detectionListener);
            break;
        }
    }

    public void stop() {
        removeMenuItem(mi_stop);
        addMenuItem(mi_start);
        switch(_emulation_type) {
        case Constants.EMULATE_SC:
            stopSmartcardEmulation();
            break;
        case Constants.EMULATE_SR:
            stopReaderEmulation();
            break;
        }
    }

    public void logEvent(String event_message) {
        _log.insert(0, event_message);
        _log.setSelectedIndex(0);
    }

    public String getResponse_text() {
        return response_text;
    }

    public void setResponse_text(String response_text) {
        this.response_text = response_text;
    }

    public boolean onClose() {
        stop();
        return super.onClose();
    }

}
