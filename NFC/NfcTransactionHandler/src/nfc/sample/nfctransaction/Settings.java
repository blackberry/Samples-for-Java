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
 * A singleton which holds application settings
 * 
 */

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.ByteArrayUtilities;
import net.rim.device.api.util.Persistable;

public class Settings implements Persistable {
    
    private static final long SETTINGS_ID = 0x8a75c361cc97f9d8L;

    private static Settings _settings;

    private int _secure_element; // 0=SIM, 1=eSE
    private boolean _ISO14443A;
    private boolean _ISO14443B;
    private boolean _ISO14443B_PRIME;
    private String _registered_aid="";
    private String _apdu;
    
    private Settings() {
    }
    
    public static synchronized Settings getInstance() {
        if (_settings == null) {
            _settings = new Settings();
            loadSettings();
        }
        return _settings;
    }
    
    public String toString() {
        return 
            "SE="+Constants.SE_NAMES[_secure_element]+
            ",ISO14443A="+_ISO14443A+
            ",ISO14443B="+_ISO14443B+
            ",ISO14443B_PRIME="+_ISO14443B_PRIME+
            ",AID="+_registered_aid;
        
    }
    
    private static void loadSettings() {
        synchronized(PersistentStore.getSynchObject()) {
            PersistentObject po = PersistentStore.getPersistentObject(SETTINGS_ID);
            Object obj = po.getContents();
            if (obj != null) {
                _settings = (Settings) obj;
            } else {
                _settings.setISO14443A(false);
                _settings.setISO14443B(true);
                _settings.setISO14443B_PRIME(false);
                _settings.setSecure_element(Constants.SE_SIM);
                _settings.setRegisteredAID(Constants.DEFAULT_AID);
                _settings.setAPDU(Constants.DEFAULT_APDU);
            }
        }
    }
    
    public void saveSettings() {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " Saving settings");
        synchronized(PersistentStore.getSynchObject()) {
            PersistentObject po = PersistentStore.getPersistentObject(SETTINGS_ID);
            po.setContents(_settings);
            po.commit();
            Utilities.log("XXXX " + Thread.currentThread().getName() + " Saved settings:" + _settings.toString());
        }
    }

    public int getSecure_element() {
        return _secure_element;
    }
    
    public void setSecure_element(int secure_element) {
        _secure_element = secure_element;
    }
    
    public void setSeSIM() {
        _secure_element = Constants.SE_SIM;
    }

    public void setSeEmbedded() {
        _secure_element = Constants.SE_EMB;
    }
    
    public boolean isSimSeSelected() {
        return (_secure_element == Constants.SE_SIM);
    }

    public boolean isEmbeddedSeSelected() {
        return (_secure_element == Constants.SE_EMB);
    }

    public boolean isISO14443A() {
        return _ISO14443A;
    }
    
    public void setISO14443A(boolean iSO14443A) {
        _ISO14443A = iSO14443A;
    }
    
    public boolean isISO14443B() {
        return _ISO14443B;
    }
    
    public void setISO14443B(boolean iSO14443B) {
        _ISO14443B = iSO14443B;
    }
    
    public boolean isISO14443B_PRIME() {
        return _ISO14443B_PRIME;
    }
    
    public void setISO14443B_PRIME(boolean iSO14443B_PRIME) {
        _ISO14443B_PRIME = iSO14443B_PRIME;
    }
    
    public String getRegisteredAIDAsString() {
        return _registered_aid;
    }

    public byte [] getRegisteredAID() {
        return ByteArrayUtilities.hexToByteArray(_registered_aid);
    }

    public void setRegisteredAID(String registered_aid) {
        _registered_aid = registered_aid;
    }
    
    public void setRegisteredAID(byte [] registered_aid) {
        _registered_aid = ByteArrayUtilities.byteArrayToHex(registered_aid);
    }
    
    public byte [] getAPDU() {
        return ByteArrayUtilities.hexToByteArray(_apdu);
    }
    
    public String getAPDUAsString() {
        return _apdu;
    }
    
    public void setAPDU(String apdu) {
        _apdu = apdu;
    }
    
    public void setAPDU(byte [] apdu) {
        _apdu = ByteArrayUtilities.byteArrayToHex(apdu);
    }
}
