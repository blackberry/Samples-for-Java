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
import java.io.IOException;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.NFCManager;
import net.rim.device.api.io.nfc.emulation.TechnologyType;
import net.rim.device.api.io.nfc.se.SecureElement;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.ByteArrayUtilities;
import nfc.sample.nfctransaction.Constants;
import nfc.sample.nfctransaction.NfcTransHandlerApp;
import nfc.sample.nfctransaction.Settings;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.nfc.CardEmulation;
import nfc.sample.nfctransaction.nfc.NfcService;

public class NfcSettingsScreen extends MainScreen {
    
    private Settings settings = Settings.getInstance();

    private CardEmulation ce = CardEmulation.getInstance();
    private NfcService nfc = NfcService.getInstance();

    private LabelFieldColoured lbl_se_choices = new LabelFieldColoured("Secure Element", Color.GREEN, Field.FIELD_LEFT);
    private RadioButtonGroup se_choices = new RadioButtonGroup();
    private RadioButtonField se_sim = new RadioButtonField("SIM", se_choices, true);
    private RadioButtonField se_embedded = new RadioButtonField("EMBEDDED", se_choices, false);

    private LabelFieldColoured lbl_pr_choices = new LabelFieldColoured("Protocols", Color.GREEN, Field.FIELD_LEFT);
    private CheckboxField cbx_iso1443a = new CheckboxField("ISO14443A", false);
    private CheckboxField cbx_iso1443b = new CheckboxField("ISO14443B", true);
    private CheckboxField cbx_iso1443b_prime = new CheckboxField("ISO14443B PRIME", false);

    private LabelFieldColoured lbl_aid = new LabelFieldColoured("AID", Color.GREEN, Field.FIELD_LEFT);
    private EditField edt_aid = new EditField("","");
    
    private ButtonField btn_save_settings = new ButtonField("Apply", ButtonField.CONSUME_CLICK);
    private SeparatorField separator = new SeparatorField();
    private LabelFieldColoured lbl_nfc_service_details = new LabelFieldColoured("NFC Service Details", Color.GREEN,
            Field.FIELD_LEFT);
    private RichTextField rtf_nfc_service_details = new RichTextField();

    private FieldChangeListener listener = new FieldChangeListener() {

        public void fieldChanged(Field field, int context) {
            if(field == btn_save_settings) {
                save();
            }
        }
    };

    private int current_se;
    private int current_tt;
    private boolean aid_changed=false;

    public NfcSettingsScreen() {
        setTitle("NFCTransactionHandler V" + Constants.VERSION + "-Settings");
        add(lbl_se_choices);
        add(se_sim);
        add(se_embedded);
        add(lbl_pr_choices);
        add(cbx_iso1443a);
        add(cbx_iso1443b);
        add(cbx_iso1443b_prime);
        add(lbl_aid);
        edt_aid.setText(settings.getRegistered_aid());
        add(edt_aid);
        add(btn_save_settings);
        btn_save_settings.setChangeListener(listener);
        add(separator);
        add(lbl_nfc_service_details);
        String nfc_details = "";
        try {
            nfc_details = Utilities.getNfcStatusDescription(NFCManager.getInstance().getEnabledNFCServices());
        } catch(NFCException e) {
            nfc_details = e.getClass().getName() + ":" + e.getMessage();
        }
        rtf_nfc_service_details.setText(nfc_details);
        add(rtf_nfc_service_details);
        syncUiWithNfcSettings();
    }

    private void syncUiWithNfcSettings() {
        current_se = ce.getCurrentSecureElement();
        current_tt = ce.getCurrentTechnologyTypes();
        if(current_se == SecureElement.SIM) {
            se_sim.setSelected(true);
            se_embedded.setSelected(false);
        } else {
            se_sim.setSelected(false);
            se_embedded.setSelected(true);
        }
        try {
            int tech_types = ce.getTechnologyTypes(SecureElement.BATTERY_ON_MODE);
            if((tech_types & TechnologyType.ISO14443A) == TechnologyType.ISO14443A) {
                cbx_iso1443a.setChecked(true);
            } else {
                cbx_iso1443a.setChecked(false);
            }
            if((tech_types & TechnologyType.ISO14443B) == TechnologyType.ISO14443B) {
                cbx_iso1443b.setChecked(true);
            } else {
                cbx_iso1443b.setChecked(false);
            }
            if((tech_types & TechnologyType.ISO14443B_PRIME) == TechnologyType.ISO14443B_PRIME) {
                cbx_iso1443b_prime.setChecked(true);
            } else {
                cbx_iso1443b_prime.setChecked(false);
            }
        } catch(NFCException e) {
        }
    }

    public void save() {
        applySettings();
        settings.saveSettings();
        try {
            super.save();
        } catch(IOException e) {
        }
    }

    private void applySettings() {
        try {
            if((current_se == SecureElement.SIM) && (se_choices.getSelectedIndex() == 1)) {
                ce.initCe(SecureElement.EMBEDDED);
                settings.setSeEmbedded();
            } else {
                if((current_se == SecureElement.EMBEDDED) && (se_choices.getSelectedIndex() == 0)) {
                    ce.initCe(SecureElement.SIM);
                    settings.setSeSIM();
                }
            }
        } catch(NFCException e) {
            Utilities.popupAlert("Error: could not change SE selection");
            return;
        }
        int tech_types = TechnologyType.NONE;
        if(cbx_iso1443a.getChecked() == true) {
            tech_types = tech_types | TechnologyType.ISO14443A;
        }
        if(cbx_iso1443b.getChecked() == true) {
            tech_types = tech_types | TechnologyType.ISO14443B;
        }
        if(cbx_iso1443b_prime.getChecked() == true) {
            tech_types = tech_types | TechnologyType.ISO14443B_PRIME;
        }
        settings.setISO14443A(cbx_iso1443a.getChecked());
        settings.setISO14443B(cbx_iso1443b.getChecked());
        settings.setISO14443B_PRIME(cbx_iso1443b_prime.getChecked());
        if (!settings.getRegistered_aid().equals(edt_aid.getText())) {
            settings.setRegistered_aid(edt_aid.getText());
            CardEmulation ce = CardEmulation.getInstance();
            ce.removeTransactionListener();
            CardEmulation.registerTransactionListener(NfcTransHandlerApp.getTransactionListener());
        }

        Utilities.log("XXXX " + Thread.currentThread().getName() + " Selected tech types="
                + Utilities.getTechnologyTypesNames(tech_types));

        if(current_tt != tech_types) {
            try {
                ce.setRoutingOn(tech_types);
            } catch(NFCException e) {
                Utilities.popupAlert("Error: could not change CE technology types");
                return;
            } catch(Exception e) {
                Utilities.log("XXXX " + Thread.currentThread().getName() + " " + e.getClass().getName() + ":" + e.getMessage());
                Utilities.popupAlert(e.getClass().getName() + ": could not change CE technology types");
                return;
            }
        }
        Utilities.log("XXXX " + Thread.currentThread().getName() + " applySettings 11");
        setDirty(false);
        Utilities.log("XXXX " + Thread.currentThread().getName() + " applySettings 12");
        Utilities.popupMessage("New settings applied");

    }
}
