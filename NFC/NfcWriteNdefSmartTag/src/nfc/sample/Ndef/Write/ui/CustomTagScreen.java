package nfc.sample.Ndef.Write.ui;
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

import java.util.Hashtable;

import net.rim.device.api.command.Command;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.StringProvider;
import nfc.sample.Ndef.Write.Constants;
import nfc.sample.Ndef.Write.DetectionListenerManager;
import nfc.sample.Ndef.Write.NfcWriteNdefSmartTagListener;
import nfc.sample.Ndef.Write.Utilities;
import nfc.sample.Ndef.Write.commands.RegisterCommand;
import nfc.sample.Ndef.Write.commands.UnregisterCommand;

public class CustomTagScreen extends MainScreen implements TagCreatorScreen {

    private CustomTagScreen _screen;
    private NfcWriteNdefSmartTagListener _smartTagListener;
    DetectionListenerManager _listener_mgr = DetectionListenerManager.getInstance();

    private LabelField _instuctions = new LabelField("Custom tag: Bring a tag into NFC field to write");
    private SeparatorField separator0 = new SeparatorField(Field.USE_ALL_WIDTH);

    private String NFC_EXTERNAL_DOMAIN = "my.rim.com";
    private String NFC_EXTERNAL_TYPE = "myrecordtype";

    private EditField _domainField = new EditField("Domain: ", NFC_EXTERNAL_DOMAIN);
    private EditField _typeField = new EditField("Type: ", NFC_EXTERNAL_TYPE);
    private EditField _contentField = new EditField("Content: ", "my content");

    private Font heading_font;

    private AlternatingListField _log = new AlternatingListField(Color.WHITE, Color.LIGHTGRAY, Field.USE_ALL_WIDTH);

    private SeparatorField separator1 = new SeparatorField(Field.USE_ALL_WIDTH);
    private SeparatorField separator2 = new SeparatorField(Field.USE_ALL_WIDTH);

    private RichTextField heading = new RichTextField();

    private Hashtable _tag_attrs = new Hashtable();

    /*
     * Set up the screen with the buttons and data fields and make sure we can scroll
     */
    public CustomTagScreen() {

        super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);

        _screen = this;
        _smartTagListener = new NfcWriteNdefSmartTagListener(_screen, Constants.TYPE_CUSTOM);

        Utilities.log("XXXX NfcWriteNdefSmartTag entered NfcWriteNdefSmartTagScreen constructor");

        setTitle("NFC Write NDEF SmartTag v" + Constants.MYAPP_VERSION);

        add(_instuctions);
        add(separator0);

        add(_domainField);
        add(_typeField);
        add(_contentField);

        Font default_font = Font.getDefault();
        heading_font = default_font.derive(Font.BOLD, 24);

        heading.setText("Event Log (newest items first)");
        heading.setFont(heading_font);

        add(separator1);
        add(heading);
        add(separator2);

        add(_log);

        _listener_mgr.set_log_screen(this);
        _listener_mgr.registerListener(_smartTagListener);

        Utilities.log("XXXX NfcWriteNdefSmartTag leaving NfcWriteNdefSmartTagScreen constructor");
    }

    /*
     * Get rid of the annoying on-save prompt :-)
     */
    public boolean onSavePrompt() {
        return true;
    }

    public String getDomainField() {
        return _domainField.getText();
    }

    public String getTypeField() {
        return _typeField.getText();
    }

    public String getContentField() {
        return _contentField.getText();
    }

    public void logEvent(String event_message) {
        synchronized(UiApplication.getEventLock()) {
            _log.insert(0, event_message);
            _log.setSelectedIndex(0);
        }
    }

    public String getTagDetails() {
        return "Domain=" + getDomainField() + ",Type=" + getTypeField()+",Content="+getContentField();
    }

    public Hashtable getTagAttributes() {
        _tag_attrs.put(Constants.TAG_ATTRIBUTE_DOMAIN, getDomainField());
        _tag_attrs.put(Constants.TAG_ATTRIBUTE_TYPE, getTypeField());
        _tag_attrs.put(Constants.TAG_ATTRIBUTE_CONTENT, getContentField());
        return _tag_attrs;
    }

}
