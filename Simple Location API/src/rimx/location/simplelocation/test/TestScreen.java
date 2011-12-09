//#preprocess

/*
* Copyright (c) 2011 Research In Motion Limited.
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

package rimx.location.simplelocation.test;

import java.util.Enumeration;

import javax.microedition.location.LocationException;

import net.rim.device.api.gps.BlackBerryLocation;
import net.rim.device.api.gps.SatelliteInfo;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NumericChoiceField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import rimx.location.simplelocation.SimpleLocationListener;
import rimx.location.simplelocation.SimpleLocationProvider;

public class TestScreen extends MainScreen implements FieldChangeListener, SimpleLocationListener{
	/** Self reference. */
	TestScreen testScreen;
	
	/** Test Setup */
	private String[] modeStrings;
	private ObjectChoiceField modeField;
	private NumericChoiceField trackingIntervalField;
	private NumericChoiceField retryFactorField;
	private NumericChoiceField gpsTimeoutField;
	private NumericChoiceField geoTimeoutField;
	private NumericChoiceField maxRetryDelayField;
	
	/** Test Actions */
	private ButtonField getSingleLocationField, getLastLocationField, startTrackingField, stopTrackingField, restartField, satellitesField;;
	
	/** Test Results */
	private EditField locationField, locationTypeField, fixCountField, gpsFixCountField, geoFixCountField, statusField, logField;
	
	private int fixCount=0, gpsFixCount=0, geoFixCount=0;
	
	private SimpleLocationProvider simpleProvider;
	private BlackBerryLocation location;
	
	public TestScreen(){
		setTitle("SimpleLocation API Demo");	
		this.testScreen = this;
		//#ifdef BlackBerrySDK6.0.0
		modeStrings = new String[] {"OPTIMAL", "GPS", "GEOLOCATION", "GEOLOCATION CELL", "GEOLOCATION WLAN"};
		//#endif
		//#ifdef BlackBerrySDK5.0.0
		modeStrings = new String[] {"OPTIMAL", "GPS", "GEOLOCATION"};
		//#endif
		modeField = new ObjectChoiceField("Mode: ", modeStrings, 0);
		modeField.setChangeListener(this);		
		
		trackingIntervalField = new NumericChoiceField("Tracking Interval: ", 1, 300, 1, 4);
		trackingIntervalField.setChangeListener(this);
		
		retryFactorField = new NumericChoiceField("Retry Factor: ", 1, 50, 1, 0);
		retryFactorField.setChangeListener(this);
		
		gpsTimeoutField = new NumericChoiceField("GPS Timeout: ", 10, 300, 1, 20);
		gpsTimeoutField.setChangeListener(this);		
		
		geoTimeoutField = new NumericChoiceField("Geolocation Timeout: ", 3, 100, 1, 12);
		geoTimeoutField.setChangeListener(this);		
		
		maxRetryDelayField = new NumericChoiceField("Maximum Retry Delay: ", 300, 43200, 300, 23);
		maxRetryDelayField.setChangeListener(this);		
		
		getSingleLocationField = new ButtonField("Get Single Location");
		getSingleLocationField.setChangeListener(this);
		getLastLocationField = new ButtonField("Get Last Location");
		getLastLocationField.setChangeListener(this);
		startTrackingField = new ButtonField("Start Tracking", Field.FIELD_RIGHT);
		startTrackingField.setChangeListener(this);
		stopTrackingField =  new ButtonField("Stop Tracking", Field.FIELD_RIGHT);
		stopTrackingField.setChangeListener(this);
		restartField = new ButtonField("Restart Tracking", Field.FIELD_RIGHT);
		restartField.setChangeListener(this);
		satellitesField = new ButtonField("Satellites");
		satellitesField.setChangeListener(this);
		
		locationField = new EditField("Location: ", "-");		
		locationTypeField = new EditField("Location Type: ", "-");		
		fixCountField = new EditField("Fix Count: ", "-");		
		gpsFixCountField = new EditField("\tGPS: ", "-");		
		geoFixCountField = new EditField("\tGeolocation: ", "-");		
		statusField = new EditField("Status: ", "-");		
		logField = new EditField("Log: ", "-");
		
		add(modeField);		
		add(new SeparatorField());
		add(retryFactorField);
		add(new SeparatorField());
		add(new LabelField("[All values in seconds]", LabelField.HCENTER));		
		add(new SeparatorField());
		add(trackingIntervalField);		
		add(new SeparatorField());
		add(gpsTimeoutField);
		add(new SeparatorField());
		add(geoTimeoutField);	
		add(new SeparatorField());
		add(maxRetryDelayField);
		add(new SeparatorField());
		HorizontalFieldManager hfm = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		VerticalFieldManager vfmLeft = new VerticalFieldManager();
		VerticalFieldManager vfmRight = new VerticalFieldManager(DrawStyle.RIGHT|Field.FIELD_RIGHT);
		vfmLeft.add(startTrackingField);
		vfmLeft.add(stopTrackingField);
		vfmLeft.add(restartField);
		vfmRight.add(getSingleLocationField);
		vfmRight.add(getLastLocationField);
		vfmRight.add(satellitesField);
		hfm.add(vfmLeft);
		hfm.add(vfmRight);
		add(hfm);		
		add(new SeparatorField());
		add(statusField);
		add(new SeparatorField());
		add(locationField);
		add(new SeparatorField());
		add(locationTypeField);
		add(new SeparatorField());
		add(fixCountField);
		add(new SeparatorField());
		add(gpsFixCountField);
		add(new SeparatorField());
		add(geoFixCountField);
		add(new SeparatorField());
		add(logField);	
		
		
	}

	


	public void fieldChanged(Field field, int context) {
		if(simpleProvider==null){
			try{
				simpleProvider = new SimpleLocationProvider(getMode(modeField.getSelectedIndex()));
			} catch(final Exception le){
				UiApplication.getUiApplication().invokeLater(new Runnable(){
					public void run(){
						Dialog.alert(le.getMessage());
					}
				});			
			} 
			if(simpleProvider!=null){
				simpleProvider.setGeolocationTimeout(geoTimeoutField.getSelectedValue());
				simpleProvider.setGPSTimeout(gpsTimeoutField.getSelectedValue());
				simpleProvider.setRetryFactor(retryFactorField.getSelectedValue());
				simpleProvider.setTrackignInterval(trackingIntervalField.getSelectedValue());
				simpleProvider.setMaxRetryDelay(maxRetryDelayField.getSelectedValue());
			}
		}
		
		if(field==startTrackingField){
			if(simpleProvider!=null){				
				simpleProvider.addSimpleLocationListener(this, trackingIntervalField.getSelectedValue());
			}
		} else if(field==stopTrackingField){
			if(simpleProvider!=null){
				simpleProvider.removeSimpleLocationListener();
			}
		} else if(field==restartField){
			if(simpleProvider!=null){
				try{
					simpleProvider.restart();
				} catch(IllegalStateException ise){
					UiApplication.getUiApplication().invokeLater(new Runnable(){
						public void run(){
							Dialog.alert("Tracking session must be in progress. Please select Start Tracking.");
						}
					});					
				}
			}
		} else if(field==modeField){
			if(simpleProvider!=null){
				try{
					simpleProvider.setMode(getMode(modeField.getSelectedIndex()));
				} catch(final LocationException le){
					UiApplication.getUiApplication().invokeLater(new Runnable(){
						public void run(){
							Dialog.alert(le.toString());
						}
					});							
				}
			}
		} else if(field==trackingIntervalField){
			if(simpleProvider!=null){
				simpleProvider.setTrackignInterval(trackingIntervalField.getSelectedValue());
			}
		} else if(field==gpsTimeoutField){
			if(simpleProvider!=null){
				simpleProvider.setGPSTimeout(gpsTimeoutField.getSelectedValue());
			}
		} else if(field==geoTimeoutField){
			if(simpleProvider!=null){
				simpleProvider.setGPSTimeout(geoTimeoutField.getSelectedValue());
			}
		} else if(field==retryFactorField){
			if(simpleProvider!=null){				
				simpleProvider.setRetryFactor(retryFactorField.getSelectedValue());
			}
		} else if(field==maxRetryDelayField){
			if(simpleProvider!=null){
				simpleProvider.setMaxRetryDelay(maxRetryDelayField.getSelectedValue());
			}
		}else if(field==getSingleLocationField){
			new Thread(){
				public void run(){					
					location = simpleProvider.getLocation(30);
					
					if(location!=null && location.isValid()){
						locationField.setText(location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
						statusField.setText("Obtained single location.");							
					} else{
						statusField.setText("Failed to obtain location!");
					}
				}
			}.start();			
		} else if(field==getLastLocationField){
			if(simpleProvider!=null){
				location = simpleProvider.getLastKnownLocation();
				if(location!=null && location.isValid()){
					locationField.setText(location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
					statusField.setText("Obtained last known location.");
				} else{
					statusField.setText("Last known location not available! Please acquire a single location or start a tracking session first.");
				}
			} else{
				statusField.setText("Last known location not available! Please acquire a single location or a tracking session first.");
			}
		} else if(field==satellitesField){
			if(location!=null){
				final PopupScreen satPopup = new PopupScreen(new VerticalFieldManager()){

					protected boolean keyDown(int keycode, int time) {
						int key = Keypad.key(keycode);
						if(key==Keypad.KEY_ESCAPE){
							Screen screen = getScreen();																			
							UiApplication.getUiApplication().popScreen(screen);							
						}
						return true;
					}					
				};
				EditField satField = new EditField("", "");
				satPopup.add(satField);
				StringBuffer buffer = new StringBuffer();
				Enumeration satEnum = location.getSatelliteInfo();
				if(satEnum!=null && satEnum.hasMoreElements()){
					buffer.append("ID\tAzimuth\tElevation\tSignal\n");
					while(satEnum.hasMoreElements()){
						SatelliteInfo satInfo = (SatelliteInfo)satEnum.nextElement();		
						if(satInfo!=null && satInfo.isValid()){
							buffer.append(satInfo.getId() + "\t" + satInfo.getAzimuth() + "\t" + satInfo.getElevation() + "\t" + satInfo.getSignalQuality() + "\n");
						}
					}
					satField.setText(buffer.toString());
				} else{
					buffer.append("No satellite information available for current location fix");
					satField.setText(buffer.toString());
				}
				UiApplication.getUiApplication().invokeLater(new Runnable(){
					public void run(){
						UiApplication.getUiApplication().pushScreen(satPopup);
					}
				});	
								
			} else{
				UiApplication.getUiApplication().invokeLater(new Runnable(){
					public void run(){
						Dialog.inform("No location fix available..");
					}
				});	
				
			}
		}
	}
	
	public int getMode(int selectedIndex){
		switch(selectedIndex){
			case 0:
				return SimpleLocationProvider.MODE_OPTIMAL;				
			case 1:
				return SimpleLocationProvider.MODE_GPS;
			case 2:
				return SimpleLocationProvider.MODE_GEOLOCATION;
			//#ifdef BlackBerrySDK6.0.0
			case 3:
				return SimpleLocationProvider.MODE_GEOLOCATION_CELL;
			case 4:
				return SimpleLocationProvider.MODE_GEOLOCATION_WLAN;
			//#endif
			default:
				return SimpleLocationProvider.MODE_OPTIMAL;
		}		
	}


	public void locationEvent(int event, Object eventData) {		
		synchronized(UiApplication.getEventLock()){
			if(event == SimpleLocationListener.EVENT_GPS_LOCATION){
				location = (BlackBerryLocation)eventData;
				locationField.setText(location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
				locationTypeField.setText("GPS");
				fixCount++;
				gpsFixCount++;
				fixCountField.setText(Integer.toString(fixCount));
				gpsFixCountField.setText(Integer.toString(gpsFixCount));			
			} else if(event == SimpleLocationListener.EVENT_CELL_GEOLOCATION){
				location = (BlackBerryLocation)eventData;
				locationField.setText(location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
				locationTypeField.setText("Cell Tower Geolocation");
				fixCount++;
				geoFixCount++;
				fixCountField.setText(Integer.toString(fixCount));
				geoFixCountField.setText(Integer.toString(geoFixCount));			
			} else if(event == SimpleLocationListener.EVENT_WLAN_GEOLOCATION){
				location = (BlackBerryLocation)eventData;
				locationField.setText(location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
				locationTypeField.setText("WLAN Geolocation");
				fixCount++;
				geoFixCount++;
				fixCountField.setText(Integer.toString(fixCount));
				geoFixCountField.setText(Integer.toString(geoFixCount));			
			} else if(event == SimpleLocationListener.EVENT_UNKNOWN_MODE){
				location = (BlackBerryLocation)eventData;
				locationField.setText(location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
				locationTypeField.setText("Unknown");
				fixCount++;			
				fixCountField.setText(Integer.toString(fixCount));						
			} else if(event == SimpleLocationListener.EVENT_ACQUIRING_LOCATION){
				statusField.setText("EVENT_ACQUIRING_LOCATION - attempt = " + eventData);	
			} else if(event == SimpleLocationListener.EVENT_LOCATION_FAILED){
				statusField.setText("EVENT_LOCATION_FAILED - attempt = " + eventData);	
			}
		}
	}


	
	protected boolean onSave() { 
		return true;
	}
	
	protected boolean onSavePrompt() { 
		return true;
	}


	public boolean onClose() {
		if(simpleProvider!=null){
			simpleProvider.removeSimpleLocationListener();
		}
		return super.onClose();
	}


	public void debugLog(String msg) {
		synchronized(UiApplication.getEventLock()){
			if(logField.getText().length()>3000){
				logField.setText("");
			}		
			logField.setText(logField.getText()+"\n"+msg);
			statusField.setText(msg);
		}
	}
	
	
}
