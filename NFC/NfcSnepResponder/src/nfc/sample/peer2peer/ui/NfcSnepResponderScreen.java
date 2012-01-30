package nfc.sample.peer2peer.ui;
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
import net.rim.device.api.io.nfc.push.NDEFPushManager;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.StringProvider;
import nfc.sample.peer2peer.Constants;
import nfc.sample.peer2peer.NfcSnepResponderCallBack;
import nfc.sample.peer2peer.NfcSnepResponderMsgBuilder;
import nfc.sample.peer2peer.Utilities;
import nfc.sample.peer2peer.commands.StartCommand;
import nfc.sample.peer2peer.commands.StopCommand;


/**
 * A class extending the MainScreen class, which provides default standard behaviour for BlackBerry GUI applications.
 */
public final class NfcSnepResponderScreen extends MainScreen {

    private NfcSnepResponderScreen _screen;
    private NDEFPushManager _ndefPushManager = null;
    private int _ndefPushId;
    private boolean _registeredWithPush = false;
    private NfcSnepResponderMsgBuilder _myMsgBuilder;
    private NfcSnepResponderCallBack _myStatusCallBack;
    private int _roleType;

    private SeparatorField _separator1 = new SeparatorField(Field.USE_ALL_WIDTH);
    private SeparatorField _separator2 = new SeparatorField(Field.USE_ALL_WIDTH);

    private RichTextField _heading = new RichTextField();

    private Font _headingFont;
    
    private AlternatingListField _log = new AlternatingListField(Color.WHITE,Color.LIGHTGRAY,Field.USE_ALL_WIDTH);
    
    private MenuItem _miStop = new MenuItem(new StringProvider("Stop vCard Emulation"), 110, 10);
    private MenuItem _miStart = new MenuItem(new StringProvider("Restart vCard Emulation"), 110, 10);

    /**
     * Creates a new NfcSnepResponderScreen object
     */
    public NfcSnepResponderScreen(int roleType) {
        super(MainScreen.HORIZONTAL_SCROLL);
        this._screen = this;
        _roleType = roleType;

        Utilities.log("XXXX NfcSnepResponder entered NfcSnepResponderScreen constructor");
        setTitle("NfcSnepResponder V" + Constants.MYAPP_VERSION);

        try {
            _ndefPushManager = NDEFPushManager.getInstance();
        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcSnepResponder Exception on obtaining NDEFPushManager: " + e.getMessage());
        }
        
        _miStop.setCommandContext(this);
        _miStop.setCommand(new Command(new StopCommand(this)));

        _miStart.setCommandContext(this);
        _miStart.setCommand(new Command(new StartCommand(this)));

        Font default_font = Font.getDefault();
        _headingFont = default_font.derive(Font.BOLD, 24);

        _heading.setText("Event Log (newest items first)");
        _heading.setFont(_headingFont);

        add(_separator1);
        add(_heading);
        add(_separator2);
        add(_log);

        start();
        
        Utilities.log("XXXX NfcSnepResponder leaving NfcSnepResponderScreen constructor");
    }

    protected void unregisterForNdefPush() {
        Utilities.log("XXXX NfcSnepResponder About to unregister from NDEF Push");
        
        if ((_ndefPushManager != null) && _registeredWithPush) {
            _ndefPushManager.cancelNDEFPush(_ndefPushId);
            _registeredWithPush = false;
            _myMsgBuilder = null;
            _myStatusCallBack = null;
            Utilities.log("XXXX NfcSnepResponder unregistered for NDEF Push");
            _screen.logEvent("info:Unregistered for NDEF Push");
        } else {
            Utilities.log("XXXX NfcSnepResponder Not in correct state to unregister");
            _screen.logEvent("warn:Not in correct state to unregister");
        }
    }

    protected void registerForNdefPush() {
        Utilities.log("XXXX NfcSnepResponder About to register for NDEF Push");
        
        _myMsgBuilder = new NfcSnepResponderMsgBuilder(_screen);
        _myStatusCallBack = new NfcSnepResponderCallBack(_screen);
        
        if ((_ndefPushManager != null) && !_registeredWithPush) {
            _ndefPushId = _ndefPushManager.pushNDEF(_myMsgBuilder, _myStatusCallBack);
            _registeredWithPush = true;
            Utilities.log("XXXX NfcSnepResponder registered for NDEF Push");
            _screen.logEvent("info:Registered for NDEF Push");
        } else {
            Utilities.log("XXXX NfcSnepResponder Not in correct state to register");
            _screen.logEvent("warn:Not in correct state to register");
        }
    }

    public void start() {
        
        removeMenuItem(_miStart);
        addMenuItem(_miStop);
        
        switch(_roleType) {
        
        case Constants.VCARD_RESPONDER:
            Utilities.log("XXXX NfcSnepResponder Registering vCard responder");
            _screen.logEvent("info:Registering vCard responder");
            registerForNdefPush();
            break;
            
        default:
            Utilities.log("XXXX NfcSnepResponder Unknown Role State on start");
            _screen.logEvent("info:Unknown Role State on start");
            break;
        }
    }

    public void stop() {
        
        removeMenuItem(_miStop);
        addMenuItem(_miStart);
        
        switch(_roleType) {
        
        case Constants.VCARD_RESPONDER:
            Utilities.log("XXXX NfcSnepResponder unregistering vCard responder");
            _screen.logEvent("info:Unregistering vCard responder");
            unregisterForNdefPush();
            break;
            
        default:
            Utilities.log("XXXX NfcSnepResponder Unknown Role State on stop");
            _screen.logEvent("warn:Unknown Role State on stop");
            break;
        }
    }

    public void logEvent(String event_message) {
        _log.insert(0, event_message);
        _log.setSelectedIndex(0);
    }

    /*
     * Get rid of the annoying on-save prompt :-)
     */
    public boolean onSavePrompt() {
        return true;
    }

    public boolean onClose() {
        stop();
        return super.onClose();
    }
}
