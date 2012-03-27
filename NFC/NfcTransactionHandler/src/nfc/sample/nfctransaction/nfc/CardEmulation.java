package nfc.sample.nfctransaction.nfc;
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
import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.emulation.TechnologyType;
import net.rim.device.api.io.nfc.se.SecureElement;
import net.rim.device.api.io.nfc.se.SecureElementManager;
import net.rim.device.api.io.nfc.se.TransactionListener;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import nfc.sample.nfctransaction.Settings;
import nfc.sample.nfctransaction.Utilities;
import nfc.sample.nfctransaction.ui.TextDetailsProvider;

// This class contains various Card Emulation related methods

public class CardEmulation implements TextDetailsProvider {

	private static CardEmulation ce;
	
	private static TransactionListener _tl;

	private SecureElementManager sem = SecureElementManager.getInstance();
	private SecureElement se = null;

	private int tech_type=TechnologyType.ISO14443B;
	
	private boolean se_obtained = false;
	
	private int current_se=-1;
	private int current_tt=-1;

	public static synchronized CardEmulation getInstance() {
		if (ce == null) {
			ce = new CardEmulation();
		}
		return ce;
	}

	public void initCe(int se_choice) throws NFCException {
		se = sem.getSecureElement(se_choice);
		if (se == null) {
			Utilities.log("XXXX " + Thread.currentThread().getName() + " SecureElement(SIM) is null");
			se_obtained = false;
		} else {
			se_obtained = true;
			current_se = se_choice;
		}
	}
	
	public static void registerTransactionListener(TransactionListener tl) {
	    
        int pid = ApplicationManager.getApplicationManager().getProcessId(ApplicationDescriptor.currentApplicationDescriptor());
        Settings settings = Settings.getInstance();

        SecureElementManager sem = SecureElementManager.getInstance();
        if(sem == null) {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " SecureElementManager instance is null - exiting");
            System.exit(0);
        }

        SecureElement[] sec_elements = null;
        try {
            sec_elements = sem.getSecureElements();
        } catch(NFCException e2) {
            e2.printStackTrace();
        }
        if(sec_elements == null) {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " SecureElement[] is null - exiting");
            System.exit(0);
        }

        Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " device has " + sec_elements.length + " SEs");

        Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " getting SecureElement instance");
        SecureElement se = null;
        try {
            if(settings.isSimSeSelected()) {
                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " Obtaining SecureElement in SIM");
                se = sem.getSecureElement(SecureElement.SIM);
            } else {
                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " Obtaining embedded SecureElement");
                se = sem.getSecureElement(SecureElement.EMBEDDED);
            }
        } catch(NFCException e1) {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " exception when getting SE: " + e1.getClass().getName() + ":" + e1.getMessage());
        }

        if(se == null) {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " SecureElement is null - exiting");
            System.exit(0);
        }

        // register our app as a transaction listener
        try {
            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " adding transaction listener for AID:"+settings.getRegistered_aid());
            se.addTransactionListener(tl, settings.getRegistered_aid_arrays());
            _tl = tl;
        } catch(NFCException e) {
            if(!e.getMessage().startsWith("TransactionListener has already been added")) {
                Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " exception when adding transaction listener: " + e.getClass().getName() + ":" + e.getMessage());
                System.exit(0);
            }
        }
	}

	public void removeTransactionListener() {
	    if (se != null && _tl != null) {
	        try {
	            int pid = ApplicationManager.getApplicationManager().getProcessId(ApplicationDescriptor.currentApplicationDescriptor());
	            Utilities.log("XXXX " + pid + ":" + Thread.currentThread().getName() + " removing transaction listener");
                se.removeTransactionListener(_tl);
            } catch(NFCException e) {
                Utilities.log("XXXX " + ":" + Thread.currentThread().getName() + " exception when removing transaction listener: " + e.getClass().getName() + ":" + e.getMessage());
            }
	    }
	}
	
	public boolean isCeEnabled(int battery_mode) throws NFCException {
		int types = getTechnologyTypes(battery_mode);
		Utilities.log("XXXX " + Thread.currentThread().getName() + " " + Utilities.getTechnologyTypesNames(types));
		boolean ce_enabled = (types & TechnologyType.ISO14443B) == TechnologyType.ISO14443B;
		Utilities.log("XXXX " + Thread.currentThread().getName() + " ce_enabled=" + ce_enabled);
		return ce_enabled;
	}

	public int getTechnologyTypes(int battery_mode) throws NFCException {
		if (!se_obtained) {
			return TechnologyType.NONE;
		}
		return se.getTechnologyTypes(battery_mode);
	}

	public int getTechnologyTypes() throws NFCException {
		return getTechnologyTypes(SecureElement.BATTERY_ON_MODE);
	}

	public String getTextDetails() {
		return Utilities.getCeStatusSummary();
	}

	public boolean isSe_obtained() {
		return se_obtained;
	}

	public void setSe_obtained(boolean se_obtained) {
		this.se_obtained = se_obtained;
	}

	public boolean setRoutingOn(int technology_types) throws NFCException {
		if (se_obtained) {
			se.setTechnologyTypes(SecureElement.BATTERY_ON_MODE, technology_types);
			Utilities.log("XXXX " + Thread.currentThread().getName() + " NFC routing established OK");
			int types = se.getTechnologyTypes(SecureElement.BATTERY_ON_MODE);
			Utilities.log("XXXX " + Thread.currentThread().getName() + " " + Utilities.getTechnologyTypesNames(types));
			current_tt = technology_types;
			return true;
		} else {
			Utilities.log("XXXX " + Thread.currentThread().getName() + " No SE selected");
			return false;
		}
	}

	public boolean setRoutingOff() throws NFCException {
		if (se_obtained) {
			se.setTechnologyTypes(SecureElement.BATTERY_ON_MODE, TechnologyType.NONE);
			return false;
		} else {
			Utilities.log("XXXX " + Thread.currentThread().getName() + " No SE selected");
			return true;
		}
	}

	public int getCurrentSecureElement() {
		if (current_se != -1) {
			return current_se;
		} else {
			// default is to use the SIM SE
			return SecureElement.SIM;
		}
	}

	public int getCurrentTechnologyTypes() {
		if (current_tt != -1) {
			return current_tt;
		} else {
			// default is to use the ISO14443B
			return TechnologyType.ISO14443B;
		}
	}

}
