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
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import nfc.sample.nfctransaction.Constants;

public class TimedLabelField extends LabelField implements Runnable {

	private String new_value="";
	
	public TimedLabelField(String value,long style) {
		super(value,style);
	}
	
	public void run() {
		synchronized (UiApplication.getEventLock()) {
			setText(new_value);
		}
		
		try {
			Thread.sleep(3000);
			synchronized (UiApplication.getEventLock()) {
				setText(Constants.READY);
			}
		} catch (InterruptedException e) {
			synchronized (UiApplication.getEventLock()) {
				setText(e.getClass().getName()+":"+e.getMessage());
			}
		}
	}

	public void setLabelText(String text) {
		new_value = text;
		Thread t = new Thread(this);
		t.start();
	}
}


