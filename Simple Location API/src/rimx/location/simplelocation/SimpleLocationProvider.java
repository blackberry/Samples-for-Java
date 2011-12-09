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

package rimx.location.simplelocation;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.gps.BlackBerryCriteria;
import net.rim.device.api.gps.BlackBerryLocation;
import net.rim.device.api.gps.BlackBerryLocationProvider;
import net.rim.device.api.gps.GPSInfo;
//#ifdef BlackBerrySDK6.0.0
import net.rim.device.api.gps.LocationInfo;
//#endif
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.component.Dialog;


/**
 * This is the starting point for applications using the Simple Location API. An instance of this class must
 * be created in order to get a single location fix or to start a tracking session.
 * 
 * <p><b>Simple Location API features:</b></p>
 * <ul>
 * 		<li> Simplified and worry-free location API that leverages on-device GPS and RIM's Geolocation services.
 *    	<li> Dynamically detects availability of GPS and Geolocation on the device before trying them.
 *     	<li> Chooses the best location mode based on the modes available on the device. (See {@link SimpleLocationProvider#MODE_OPTIMAL}).
 *      <li> Built-in retry mechanism with dynamic delay (to save battery) based on a retry factor set by the API user. (See {@link SimpleLocationProvider#setRetryFactor(int)}).
 *     	<li> Performs both single or tracking location fixes.
 *     	<li> Simplified events via {@link SimpleLocationListener} interface.
 *     	<li> Capable of starting, stopping and restarting tracking session in a reliable thread-safe way.
 *     	<li> Designed to eliminate/reduce misuse of location API
 * </ul>
 * 
 * <p><b>Single location fix in default mode</b></p>
 * <pre>
 * try{
 * 	simpleProvider = new SimpleLocationProvider();
 * } catch(LocationException le){ // thrown if the default mode {@link #MODE_OPTIMAL} is not available.
 * 	...
 * }
 * BlackBerryLocation location = simpleProvider.getLocation(120);	// 120 seconds timeout
 * </pre>
 *
 * 
 * <p><b>Single location fix in a specified mode:</b></p>
 * <pre>
 * try{
 * 	simpleProvider = new SimpleLocationProvider(SimpleLocationProvider.MODE_GPS);
 * } catch(LocationException le){ // thrown if the selected mode (in this case {@link #MODE_GPS}) is not available.
 * 	...
 * }
 * BlackBerryLocation location = simpleProvider.getLocation(120);	// 120 seconds timeout
 * </pre>
 * 
 * <p><b>Tracking session in default mode</b></p>
 * <pre>
 * try{
 * 	simpleProvider = new SimpleLocationProvider(); 	 
 * } catch(LocationException le){ // thrown if the default mode {@link #MODE_OPTIMAL} is not available.
 * 	...
 * } 
 * // Location fixes will be delivered to simpleLocationListenerImpl (an implementation of {@link SimpleLocationListener}) every 6 seconds.
 * simpleProvider.addSimpleLocationListener(simpleLocationListenerImpl, 6);
 * </pre>
 * 
 * <p><b>Tracking session in a specific mode</b></p>
 * <pre>
 * try{
 * 	simpleProvider = new SimpleLocationProvider(SimpleLocationProvider.MODE_GPS); 	 
 * } catch(LocationException le){ // thrown if the selected mode (in this case {@link #MODE_GPS}) is not available.
 * 	...
 * } 
 * // Location fixes will be delivered to simpleLocationListenerImpl (an implementation of {@link SimpleLocationListener}) every 6 seconds.
 * simpleProvider.addSimpleLocationListener(simpleLocationListenerImpl, 6);
 * </pre>
 * 
 * @author Shadid Haque
 *
 */
public class SimpleLocationProvider implements LocationListener{	
	/** 
	 * Operates in both Geolocation and GPS mode based on availability. First fix is computed in Geolocation mode, subsequent fixes in 
	 * Standalone mode. However if Standalone mode fails, falls back to Geolocation mode temporarily until a retry is attempted in 
	 * Standalone mode after a certain waiting period has passed. See {@link #setRetryFactor(int)}.
	 * <p>
	 * For single fix calls to {@link #getLocation(int)}, Geolocation mode is used first with a fallback to 
	 * Standalone mode if Geolocation mode fails.
	 * 
	 **/
	public static final int MODE_OPTIMAL = 0;
	/**
	 * Operates strictly in GPS (aka Standalone/Autonomous) mode. 
	 */
	public static final int MODE_GPS = 1;
	/**
	 * Operates strictly in Geolocation mode. One of WLAN and CELL based is attempted based on availability.
	 */
	public static final int MODE_GEOLOCATION = 2;
	
	//#ifdef BlackBerrySDK6.0.0
	/**
	 * Operates strictly in cell based Geolocation mode. <i>BlackBerry 6.0 and above only</i>.
	 */
	public static final int MODE_GEOLOCATION_CELL = 3;
	/**
	 * Operates strictly in WLAN based Geolocation mode. <i>BlackBerry 6.0 and above only</i>.
	 */
	public static final int MODE_GEOLOCATION_WLAN = 4;
	//#endif
	
	/********************** PRIVATE VARIABLES *********************************/
	/** Reference to the last location fix */
	private BlackBerryLocation location = null;
	/** A SimpleLocationListener where events should be sent for a tracking session */
	private SimpleLocationListener simpleLocationListener = null;
	/** A reference to the current SimpleLocationThread */
	private SimpleLocationThread locationThread;
	/** A reference to the BlackBerryLocationProvider of the current locationThread:SimpleLocationThread. Updated everytime startTracking is called */
	BlackBerryLocationProvider locationProviderReference;
	
	/** Interval between fixes for multiple fix session. Default is 5 seconds */
	private int trackingInterval = 5;
	/** Retry factor that determines the delay between retries. Default is 1. See setRetryFactor() */
	private int retryFactor = 1;
	/** Current mode. Set to MODE_OPTIMAL by default. */
	private int mode = MODE_OPTIMAL;	
	/** Reference to the current BlackBerryCriteria object */
	private BlackBerryCriteria criteria;
	/** Flag to indicate whether the current optimal mode is geolocation. */ 
	private boolean currentOptimalModeIsGeolocation = true;
	/** Flag to indicate that optimal mode should switch to GPS next */
	private boolean switchToGPS = false;	
	
		
	/** Flag to indicate whether a tracking session is in progress */
	private boolean trackingInProgress = false;	
	
	/** Time of the last valid fix */
	private long lastValidFixTime = System.currentTimeMillis();
	/** Interval after which a GPS fix request should be considered a failure. After this interval the current tracking session is stopped and a retry is considered. */
	private int gpsTimeout = 30;
	/** Interval after which a Geolocation fix request should be considered a failure. After this interval the current tracking session is stopped and a retry is considered. */
	private int geolocationTimeout = 20;
	/** Maximum length of time this SimpleLocationProvider is allowed to wait (in seconds) before a retry is attempted. Default is 2 hours. */
	private int maxRetryDelay = 7200;
	/** Represents the number of retries since the last valid fix. Used in combination with retryFactor */
	private int retryAttempt = 0;		
	/** A TimerTask that is used to restart/retry tracking */
	private Timer restartTimer = null; 
	private TimerTask restartTask = null;
	
	
	private Dialog askUserPermission;
	private boolean userPermissionResult;
	/************************************************************************/
	
	/**
	 * Initializes the default SimpleLocationProvider in MODE_OPTIMAL. 
	 *   
	 * @throws	LocationException	If the default mode is not available.
	 * @throws	IllegalStateException	If Location Services is OFF in device options. <i>BlackBerry 6.0 and above only</i>.
	 */
	public SimpleLocationProvider() throws LocationException, IllegalStateException{
		EventLogger.register(0xf9768e659ebd0dfcL, "SimpleLocation", EventLogger.VIEWER_STRING);
		log("Initializing default SimpleLocationProvider..");	
		//#ifdef BlackBerrySDK6.0.0
		if(!LocationInfo.isLocationOn()){
			throw new IllegalStateException("Location Services is OFF. Please turn on Location Services in device options.");
		}
		//#endif
		
		if(!isModeAvailable(mode)){
			throw new LocationException(getModeString(mode) + " is not currently available on this device.");
		}		
	}
	
	/**
	 * Initializes a SimpleLocationProvider in the specified mode. 	 
	 * @param mode	One of {@link #MODE_OPTIMAL}, {@link #MODE_GPS}, {@link #MODE_GEOLOCATION}, {@link #MODE_GEOLOCATION_CELL}, {@link #MODE_GEOLOCATION_WLAN}. 
	 * @throws	IllegalArgumentException	If the specified mode is invalid.
	 * @throws	LocationException	If the specified mode is not available.
	 * @throws IllegalStateException	If Location Services is OFF in device options. <i>BlackBerry 6.0 and above only</i>.
	 */
	public SimpleLocationProvider(int mode) throws IllegalArgumentException, LocationException, IllegalStateException{
		EventLogger.register(0xf9768e659ebd0dfcL, "SimpleLocation", EventLogger.VIEWER_STRING);
		log("Initializing SimpleLocationProvider in " + getModeString(mode));
		//#ifdef BlackBerrySDK6.0.0
		if(!LocationInfo.isLocationOn()){
			throw new IllegalStateException("Location Services is OFF. Please turn on Location Services in device options.");
		}
		//#endif
		
		if(!isModeValid(mode)){
			throw new IllegalArgumentException("Mode: " + mode + " is invalid. Please use one of SimpleLocationProvider.MODE_*.");
		}
		
		if(!isModeAvailable(mode)){
			throw new LocationException(getModeString(mode) + " is not currently available on this device.");
		}
		setMode(mode);				
	}
	
	/**
	 * Adds a {@link SimpleLocationListener} implementation to this {@link SimpleLocationProvider} and starts a tracking session. 
	 * Once added, the implementation can expect to get location fixes at the specified interval via 
	 * {@link SimpleLocationListener#locationEvent(int, Object)}. NOTE: The first fix may take longer than the interval.
	 *  
	 * @param listener	A {@link SimpleLocationListener} implementation.
	 * @param interval	An interval in seconds - describing how often the implementation requires a fix. -1 represents the default interval value of 5.
	 * @throws	IllegalArgumentException	If listener is null.
	 */
	public void addSimpleLocationListener(SimpleLocationListener listener, int interval) throws IllegalArgumentException{
		if(listener == null){
			throw new IllegalArgumentException("null SimpleLocationListener");
		}
		stopTracking();
		retryAttempt = 0;
		lastValidFixTime = System.currentTimeMillis();
		currentOptimalModeIsGeolocation = true;
		switchToGPS = false;
		this.simpleLocationListener= listener;
		if(interval > 0){
			this.trackingInterval = interval;
		} 
		log("SimpleLocationListener added.");
		startTracking();		
	}

	/**
	 * Stops tracking and removes the current {@link SimpleLocationListener} implementation from this.
	 */
	public void removeSimpleLocationListener(){
		stopTracking();
		retryAttempt = 0;
		lastValidFixTime = System.currentTimeMillis();
		currentOptimalModeIsGeolocation = true;
		switchToGPS = false;		
		simpleLocationListener = null;
		log("SimpleLocationListener removed.");
	}

	/**
	 * Triggers an immediate retry by restarting the current tracking session. Note that {@link SimpleLocationProvider} implements an optimal logic
	 * to decide when to retry for location fixes if location fixes fail or are not available at a given time (see {@link #setRetryFactor(int)}). 
	 * However, if an application chooses to trigger an immediate retry at will, it can do so by simply calling this method. 
	 * @throws IllegalStateException If no tracking session exists. Essentially you cannot restart a tracking session that was never started. See {@link #addSimpleLocationListener(SimpleLocationListener, int)}
	 * 
	 */
	public void restart() throws IllegalStateException{	
		if(simpleLocationListener!=null){
			log("Restarting SimpleLocationProvider..");
			retryAttempt = 0;
			lastValidFixTime = System.currentTimeMillis();
			currentOptimalModeIsGeolocation = true;		
			switchToGPS = false;
			startTracking();
		} else{
			throw new IllegalStateException("No existing tracking session found.");
		}
	}

	/**
	 * Gets a single location fix and stops. Note that if this method is called in the middle of a tracking session, this method 
	 * will stop the tracking session before attempting a single location fix. This is a blocking call. 
	 * @param	timeout	Maximum time this method is allowed to spend to get a location. If a location could not be obtained in that time, returns a null Location object. Using -1 will force the default timeout value 120.
	 * @return	A Location object or null if it was unable to obtain a location within timeout many seconds. 
	 */
	public BlackBerryLocation getLocation(int timeout) {	
		stopTracking();
		retryAttempt = 0;
		lastValidFixTime = System.currentTimeMillis();
		currentOptimalModeIsGeolocation = true;
		switchToGPS = false;
		log("Acquiring single location fix.. timeout="+timeout);
		return getSingleFix(timeout);
	}
	
	/**
	 * Returns the last known location.
	 * @return	the last known location of this SimpleLocationProvider or null if no location is computed so far.
	 */
	public BlackBerryLocation getLastKnownLocation(){
		log("Acquiring last known location..");		
		if(location==null){
			log("No location available. Returning null.");
		} else if(location.isValid() && (location.getQualifiedCoordinates().getLatitude()!=0 && location.getQualifiedCoordinates().getLongitude()!=0)){
			log("Returning " + location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
		} else{
			log("No valid location available. Returning invalid location");
		}
		return location;		
	}
	
	/**
	 * Sets/changes the current mode of this SimpleLocationProvider. This method can be used to dynamically change the current 
	 * mode in the middle of a tracking session. All subsequent fixes after this call will be computed in the new mode.
	 * @param mode	One of {@link #MODE_OPTIMAL}, {@link #MODE_GPS}, {@link #MODE_GEOLOCATION}, {@link #MODE_GEOLOCATION_CELL}, {@link #MODE_GEOLOCATION_WLAN}.
	 * @throws IllegalArgumentException	If the specified mode is invalid. 
	 * @throws	LocationException	If the specified mode is not available.
	 */
	public void setMode(int mode) throws IllegalArgumentException, LocationException{		
		retryAttempt = 0;
		
		if(!isModeValid(mode)){
			throw new IllegalArgumentException("Mode: " + mode + " is invalid. Please use one of SimpleLocationProvider.MODE_*.");
		}
		if(!isModeAvailable(mode)){
			throw new LocationException(getModeString(mode) + " is not currently available on this device.");
		}
		lastValidFixTime = System.currentTimeMillis();
		currentOptimalModeIsGeolocation = true;		
		switchToGPS = false;
		if(trackingInProgress){		
			stopTracking();
			this.mode = mode;	
			log("Mode set to " + getModeString(mode));			
			startTracking();
		} else {
			this.mode = mode;
			log("Mode set to " + getModeString(mode));			
		}		
	}
	
	/**
	 * Returns the current mode of this SimpleLocationProvider
	 * @return	One of {@link #MODE_OPTIMAL}, {@link #MODE_GPS}, {@link #MODE_GEOLOCATION}, {@link #MODE_GEOLOCATION_CELL}, {@link #MODE_GEOLOCATION_WLAN}.
	 */
	public int getMode(){
		return mode;
	}
	
	/**
	 * Sets the retry factor to the specified value. Obtaining location fixes can fail for many reasons (e.g. out of gps coverage, network error etc.). In case of such 
	 * failures we cannot afford to continuously retry until we get a fix since that will rapidly drain the battery. 
	 * Instead a retryFactor is used that controls the delay (in seconds) between retries according to the following expression - 
	 * (((5 << attempt) - 5) * retryFactor) seconds. Value of attempt is reset to 0 when a valid location fix is obtained. However, if the selected 
	 * mode is {@link #MODE_OPTIMAL}, value of attempt is reset to 0 only when a valid GPS location fix is obtained.
	 * <p>
	 * Setting a negative/zero value as the retry factor has no effect.
	 * 
	 * @param retryFactor	A retry factor. Default is 1. 
	 * 
	 */
	public void setRetryFactor(int factor){
		if(factor<=0){	
			log("Invalid retryFactor. Must be greater than 0");
			return;
		}		
		this.retryFactor = factor;
		log("retryFactor set to " + retryFactor);
	}
	
	/**
	 * Gets the current retry factor. 
	 * @return	Value of current retry factor. See {@link #setRetryFactor(int)}
	 */
	public int getRetryFactor(){
		return retryFactor;
	}
	
	/**
	 * Sets the maxRetryDelay value that dictates the maximum time this provider can wait until a
	 * retry is performed. This essentially overrides and sets a cap to the value computed by the 
	 * expression documented in {@link #setRetryFactor(int)}. Default value is 7200 seconds.
	 * <p>
	 * Setting a negative/zero value as the retry factor has no effect. 
	 * @param maxRetryDelay	A value in seconds.
	 */
	public void setMaxRetryDelay(int delay){
		if(delay<=0){	
			log("Invalid maxRetryDelay. Must be greater than 0");
			return;
		}
		this.maxRetryDelay = delay;
		log("maxRetryDelay set to " + maxRetryDelay);
	}
	
	/**
	 * Gets the current value of maxRetryDelay.
	 * @return	the current value of maxRetryDelay.
	 */
	public int getMaxRetryDelay(){
		return maxRetryDelay;
	}
	
	/**
	 * Sets the interval used between fixes in a tracking session. This method can be used to dynamically change the current 
	 * interval in the middle of a tracking session. All subsequent fixes after this call will be delivered at the new interval.
	 * <p>
	 * Setting a negative/zero value as the tracking interval has no effect.
	 * @param interval	Interval in seconds. 
	 */
	public void setTrackignInterval(int interval) {
		if(interval<=0){
			log("Invalid trackingInterval. Must be greater than 0");
			return;
		}		
		this.trackingInterval = interval;
		log("trackingInterval set to " + trackingInterval);
		retryAttempt = 0;
		lastValidFixTime = System.currentTimeMillis();
		currentOptimalModeIsGeolocation = true;		
		switchToGPS = false;
		if(trackingInProgress){	
			stopTracking();
			startTracking();
		}
	}
	
	/**
	 * Gets the current tracking interval.
	 * @return	Current value of tracking interval.
	 */
	public int getTrackingInterval(){
		return this.trackingInterval;
	}
	
	/**
	 * Sets the Geolocation timeout. This is how long SimpleLocationProvider can spend in trying to get a Geolocation fix in a tracking session. 
	 * After this timeout, {@link SimpleLocationListener#EVENT_LOCATION_FAILED} is triggered and a retry is scheduled. Default value is 20.
	 * <p>
	 * Setting this timeout to a negative/zero value has no effect.
	 *    
	 * @param timeout	A value for Geolocation timeout in seconds.
	 */
	public void setGeolocationTimeout(int timeout){
		if(timeout<=0){
			log("Invalid geolocationTimeout. Must be greater than 0");
			return;
		}
		this.geolocationTimeout = timeout;
		log("geolocationTimeout set to " + geolocationTimeout);
	}
	/**
	 * Gets the current value of Geolocation timeout.
	 * @return	Current value of Geolocation timeout.
	 */
	public int getGeolocationTimeout(){
		return geolocationTimeout;
	}
	
	/**
	 * Sets the GPS timeout. This is how long SimpleLocationProvider can spend in trying to get a GPS fix in a tracking session. 
	 * After this timeout, {@link SimpleLocationListener#EVENT_LOCATION_FAILED} is triggered and a retry is scheduled. Default value is 30.
	 * <p>
	 * Setting this timeout to a negative/zero value has no effect.
	 * 
	 * @param timeout	A value for GPS timeout in seconds.
	 */
	public void setGPSTimeout(int timeout){
		if(timeout<=0){
			log("Invalid gpsTimeout. Must be greater than 0");
			return;
		}
		this.gpsTimeout = timeout;
		log("gpsTimeout set to " + gpsTimeout);
	}
	
	/**
	 * Gets the current value of GPS timeout.
	 * @return	Current value of GPS timeout.
	 */
	public int getGPSTimeout(){
		return gpsTimeout;
	} 
	
	/**
	 * Logs a message to standard output as well as to the device eventlog.
	 * @param msg	Message to log.
	 */
	public void log(String msg){
		System.out.println(msg);
		EventLogger.logEvent(0xf9768e659ebd0dfcL, msg.getBytes(), EventLogger.ALWAYS_LOG);
		if(simpleLocationListener!=null){
			simpleLocationListener.debugLog(msg);
		}
	}

	/************** PRIVATE METHODS *************************/
	
	/**
	 * Starts a tracking session in a LocationThread.
	 */
	private void startTracking(){
		log("Starting tracking session..");
		if(trackingInProgress){
			log("Tracking session exists. Stopping..");
			stopTracking();			
		}		
		if(switchToGPS || !isModeAvailable(MODE_GEOLOCATION)){
			currentOptimalModeIsGeolocation = false;
		}		
		setupCriteria();
		trackingInProgress = true;		
		locationThread = new SimpleLocationThread(criteria, trackingInterval, this, this);
		locationThread.start();		
		log("New tracking session started..");
		simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_ACQUIRING_LOCATION, new Long(retryAttempt));	
	}
	
	/**
	 * Stops the current tracking session.
	 */
	private void stopTracking(){	
		if(trackingInProgress){
			locationProviderReference = null; //null location provider reference to stop unnecessary updated to LocationListener
			log("Stopping tracking session..");		
			if(restartTimer!=null){
				restartTimer.cancel();
				restartTimer = null;
				log("TimerTask canceled.");
			}				
			new Thread(){	// Doing this in a separate thread. If the provider is currently acquiring a location a reset may block.
				public void run(){
					if(locationThread!=null){
						locationThread.resetProvider();			
					}
				}
			}.start();
		}
		
		trackingInProgress = false;
		log("Tracking session stopped");
	}
	
	/**
	 * Gets a single location fix. Will stop any tracking session that is currently active for this SimpleLocationProvider. 
	 * This is a blocking call.
	 * @return
	 */
	private BlackBerryLocation getSingleFix(int timeout){		
		if(timeout<0){
			timeout = 120;
		}
		if(trackingInProgress){
			log("Tracking session exists. Stopping..");
			stopTracking();
		}
		
		if( (mode!=MODE_OPTIMAL) || (mode==MODE_OPTIMAL && isModeAvailable(MODE_GEOLOCATION)) ){
			currentOptimalModeIsGeolocation = true;			
			setupCriteria();		
			locationThread = new SimpleLocationThread(criteria, trackingInterval, this, this);		
			location = locationThread.getSingleFix(timeout);		
		} else{
			log("Geolocation mode is not available, hence, not attempted.");
		}
	
				
		if(mode==MODE_OPTIMAL && isModeAvailable(MODE_GPS) && ((location==null) || (location!=null && !location.isValid()) || (location!=null && location.getQualifiedCoordinates().getLatitude()==0 && location.getQualifiedCoordinates().getLongitude()==0))){
			log("Single location fix failed/skipped in Geolocation mode!");			
			log("Attempting GPS mode..");
			currentOptimalModeIsGeolocation = false;
			setupCriteria();			
			locationThread = new SimpleLocationThread(criteria, trackingInterval, this, this);			
			location = locationThread.getSingleFix(timeout);						
			if(location==null || (location!=null && !location.isValid()) || (location!=null && location.getQualifiedCoordinates().getLatitude()==0 && location.getQualifiedCoordinates().getLongitude()==0)){
				log("Single location fix failed in GPS mode! Returning invalid or null location..");				
			}  else if(location.isValid()){
				log("Got single fix in GPS mode");
				log("Returning " + location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
			}			
		} else if(location.isValid()){
			log("Got single fix in Geolocation mode");
			log("Returning " + location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
		}
		return location; 
	}

	/**
	 * Initializes an appropriate Criteria object based on the selected mode.  
	 * @param mode	One of MODE_OPTIMAL, MODE_GPS, MODE_GEOLOCATION, MODE_GEOLOCATION_CELL, MODE_GEOLOCATION_WLAN
	 */
	private void setupCriteria(){		
		if(mode==SimpleLocationProvider.MODE_GPS){			
			criteria = new BlackBerryCriteria(GPSInfo.GPS_MODE_AUTONOMOUS);
			criteria.setSatelliteInfoRequired(true, false);
			log("Criteria set for GPS_MODE_AUTONOMOUS");
		} 
		
		//#ifdef BlackBerrySDK5.0.0
		else if(mode==MODE_GEOLOCATION){
			criteria = new BlackBerryCriteria(GPSInfo.GPS_MODE_CELLSITE);
			log("Criteria set for GPS_MODE_CELLSITE");
		} else if(mode==MODE_OPTIMAL && currentOptimalModeIsGeolocation){
			criteria = new BlackBerryCriteria(GPSInfo.GPS_MODE_CELLSITE);
			log("Criteria set for GPS_MODE_CELLSITE");
		} else if(mode==MODE_OPTIMAL && !currentOptimalModeIsGeolocation){
			criteria = new BlackBerryCriteria(GPSInfo.GPS_MODE_AUTONOMOUS);
			log("Criteria set for GPS_MODE_AUTONOMOUS");
			criteria.setSatelliteInfoRequired(true, false);
		}
		//#endif
		
		//#ifdef BlackBerrySDK6.0.0
		else if(mode==MODE_GEOLOCATION){
			criteria = new BlackBerryCriteria(LocationInfo.GEOLOCATION_MODE);
			log("Criteria set for GEOLOCATION_MODE");
		} else if(mode==MODE_GEOLOCATION_CELL){
			criteria = new BlackBerryCriteria(LocationInfo.GEOLOCATION_MODE_CELL);
			log("Criteria set for GEOLOCATION_MODE_CELL");
		} else if(mode==MODE_GEOLOCATION_WLAN){
			criteria = new BlackBerryCriteria(LocationInfo.GEOLOCATION_MODE_WLAN);
			log("Criteria set for GEOLOCATION_MODE_WLAN");
		} else if(mode==MODE_OPTIMAL && currentOptimalModeIsGeolocation){
			criteria = new BlackBerryCriteria(LocationInfo.GEOLOCATION_MODE);
			log("Criteria set for GEOLOCATION_MODE");
		} else if(mode==MODE_OPTIMAL && !currentOptimalModeIsGeolocation){
			criteria = new BlackBerryCriteria(GPSInfo.GPS_MODE_AUTONOMOUS);
			log("Criteria set for GPS_MODE_AUTONOMOUS");
			criteria.setSatelliteInfoRequired(true, false);
		}		
		//#endif
	}
	
	void updateLocationProviderReference(BlackBerryLocationProvider provider){
		this.locationProviderReference = provider;
	}
		
	/**
	 * Checks the validity of a given mode value. Also checks whether the selected mode is actually available on the device.
	 * @param mode	Mode to validate.
	 * @return	True if valid. False otherwise.
	 */
	private boolean isModeValid(int mode){	
		//#ifdef BlackBerrySDK6.0.0
		if(mode>=0 && mode<=4){
			return true;		
		} else {
			return false;
		}		
		//#endif
		//#ifdef BlackBerrySDK5.0.0
		if(mode>=0 && mode<=2){
			return true;		
		} else {
			return false;
		}		
		//#endif	
		
	}
	
	private boolean isModeAvailable(int mode){ 
		if(mode==MODE_GPS && !GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_AUTONOMOUS)){				
			log("MODE_GPS is not available!");
			return false;				
		} 
		
		//#ifdef BlackBerrySDK5.0.0
		else if(mode==MODE_GEOLOCATION && !GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CELLSITE)){				
			log("MODE_GEOLOCATION is not available!");
			return false;				
		} else if(mode==MODE_OPTIMAL && !GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_CELLSITE) && !GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_AUTONOMOUS)){				
			log("MODE_OPTIMAL is not available!");
			return false;				
		} 
		//#endif
		
		//#ifdef BlackBerrySDK6.0.0
		else if(mode==MODE_GEOLOCATION && !LocationInfo.isModeAvailable(LocationInfo.GEOLOCATION_MODE)){				
			log("MODE_GEOLOCATION is not available!");
			return false;				
		} else if(mode==MODE_GEOLOCATION_CELL && !LocationInfo.isModeAvailable(LocationInfo.GEOLOCATION_MODE_CELL)){				
			log("MODE_GEOLOCATION_CELL is not available!");
			return false;				
		} else if(mode==MODE_GEOLOCATION_WLAN && !LocationInfo.isModeAvailable(LocationInfo.GEOLOCATION_MODE_WLAN)){				
			log("MODE_GEOLOCATION_WLAN is not available!");
			return false;				
		} else if(mode==MODE_OPTIMAL && !LocationInfo.isModeAvailable(LocationInfo.GEOLOCATION_MODE) && !GPSInfo.isGPSModeAvailable(GPSInfo.GPS_MODE_AUTONOMOUS)){				
			log("MODE_OPTIMAL is not available!");
			return false;				
		}		
		//#endif
		return true;
	}
	
	private String getModeString(int mode){
		switch(mode){
			case MODE_OPTIMAL:
				return "MODE_OPTIMAL";
			case MODE_GPS:
				return "MODE_GPS";
			case MODE_GEOLOCATION:
				return "MODE_GEOLOCATION";
			//#ifdef BlackBerrySDK6.0.0
			case MODE_GEOLOCATION_CELL:
				return "MODE_GEOLOCATION_CELL";
			case MODE_GEOLOCATION_WLAN:
				return "MODE_GEOLOCATION_WLAN";
			//#endif
			default:
				return "UNKNOWN";	
		}
	}
	/********************* END OF PRIVATE METHODS **********************/


	/******************************** LocationListener IMPLEMENTATION ***************************************/
	
	/**
	 * This is used by this API internally and must NOT be called. 
	 */
	public void locationUpdated(LocationProvider provider, Location location) {
		if(provider != this.locationProviderReference){ // if this is an update from an old location provider that is in the process of being stopped, ignore the update.
			return;
		}
		
		BlackBerryLocation bbLocation = (BlackBerryLocation) location;
		
		
		if(bbLocation!=null && bbLocation.isValid() && (bbLocation.getQualifiedCoordinates().getLatitude()!=0 && bbLocation.getQualifiedCoordinates().getLongitude()!=0)){	
			log("Tracking: Acquired valid location - " + location.getQualifiedCoordinates().getLatitude() + ", " + location.getQualifiedCoordinates().getLongitude());
			this.location = (BlackBerryLocation) location;				
			/**
			 * retryAttempt should not be reset in MODE_OPTIMAL when currentOptimalModeIsGeolocation
			 * because last GPS fix might have failed and this is only a fall back to Geolocation that has passed.
			 * We still need to preserve the retryAttempt until a GPS fix works in MODE_OPTIMAL. Otherwise
			 * we will simply waste battery by trying GPS fixes too frequently when it is not available. 
			 */
			if( mode!=MODE_OPTIMAL || (mode==MODE_OPTIMAL && !currentOptimalModeIsGeolocation) ){
				retryAttempt = 0;
				log("Tracking: retryAttempt reset to 0");
			}
			
			/** Update lastValidFixTime */				
			lastValidFixTime = System.currentTimeMillis();
			log("Tracking: lastValidFixTime updated.");
			
			/** Trigger events via SimpleLocationListener */
			if(bbLocation.getGPSMode()==GPSInfo.GPS_MODE_AUTONOMOUS){
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_GPS_LOCATION, bbLocation);
				log("Tracking: Location delivered to SimpleLocationListener as EVENT_GPS_LOCATION");
			}				
			//#ifdef BlackBerrySDK5.0.0
			else if(bbLocation.getGPSMode()==GPSInfo.GPS_MODE_CELLSITE){
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_CELL_GEOLOCATION, bbLocation);
				log("Tracking: Location delivered to SimpleLocationListener as EVENT_CELL_GEOLOCATION");
			}
			//#endif	
			
			//#ifdef BlackBerrySDK6.0.0
			else if(bbLocation.getGPSMode()==LocationInfo.GEOLOCATION_MODE_CELL){
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_CELL_GEOLOCATION, bbLocation);
				log("Tracking: Location delivered to SimpleLocationListener as EVENT_CELL_GEOLOCATION");
			} else if(bbLocation.getGPSMode()==LocationInfo.GEOLOCATION_MODE_WLAN){
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_WLAN_GEOLOCATION, bbLocation);
				log("Tracking: Location delivered to SimpleLocationListener as EVENT_WLAN_GEOLOCATION");
			}												
			//#endif
			else{ // This should not occur
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_UNKNOWN_MODE, bbLocation);
				log("Tracking: Location delivered to SimpleLocationListener as EVENT_UNKNOWN_MODE");
			}
			
			/** Create a TimerTask to switch to MODE_GPS when appropriate */
			if(mode == MODE_OPTIMAL && currentOptimalModeIsGeolocation && isModeAvailable(MODE_GPS)){
				if(restartTimer == null){						
					log("Tracking: Scheduling switch to GPS");
					switchToGPS = true;																					
					restartTimer = new Timer();						
					restartTask = new RestartTask();
					long nextRetry = getNextRetryDelay();	
					lastValidFixTime = System.currentTimeMillis() + nextRetry;
					restartTimer.schedule(restartTask, nextRetry);
					log("Tracking: GPS will be attempted after " + (nextRetry/1000) + " seconds.");
				}
			}
		} else{				
			log("Tracking: Invalid fix.");				
			/** If Geolocation fails in MODE_OPTIMAL, try GPS (if available) after waiting an appropriate interval */
			if( mode == MODE_OPTIMAL && currentOptimalModeIsGeolocation && (((System.currentTimeMillis() - lastValidFixTime) / 1000) > geolocationTimeout) ) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();
				if(!isModeAvailable(MODE_GPS)){
					currentOptimalModeIsGeolocation = true;
					log("Cannot switch to GPS because MODE_GPS is not available.");
				} else{
					currentOptimalModeIsGeolocation = false;
					log("Switching to GPS");
				}
				long nextRetry = getNextRetryDelay();
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry);										
				log("Tracking: Next attempt after " + (nextRetry/1000) + " seconds.");
			} 
			/** If GPS fails in MODE_OPTIMAL, try Geolocation (if available) immediately */
			else if( mode == MODE_OPTIMAL && !currentOptimalModeIsGeolocation && (((System.currentTimeMillis() - lastValidFixTime) / 1000) > gpsTimeout) ) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();
				long nextRetry = 0;
				if(!isModeAvailable(MODE_GEOLOCATION)){
					currentOptimalModeIsGeolocation = false;
					nextRetry = getNextRetryDelay();
					log("Cannot switch to Geolocation because MODE_GEOLOCATION is not available.");
				} else{
					currentOptimalModeIsGeolocation = true;
					nextRetry = 0;
					log("Switching to Geolocation.");
				}				
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();		
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry); 								
				log("Tracking: Next attempt after " + (nextRetry/1000) + " seconds.");
			} 
			/** If Geolocation fails, retry after waiting an appropriate interval */
			else if( (mode == MODE_GEOLOCATION
					//#ifdef BlackBerrySDK6.0.0
					|| mode == MODE_GEOLOCATION_CELL || mode == MODE_GEOLOCATION_WLAN
					//#endif
					) && (((System.currentTimeMillis() - lastValidFixTime) / 1000) > geolocationTimeout) ) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();				
				long nextRetry = getNextRetryDelay();
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();	
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry);										
				log("Tracking: Geolocation will be attempted after " + (nextRetry/1000) + " seconds.");
			}
			/** If GPS fails, retry after waiting an appropriate interval */
			else if( mode == MODE_GPS && (((System.currentTimeMillis() - lastValidFixTime) / 1000) > gpsTimeout) ) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();				
				long nextRetry = getNextRetryDelay();
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();		
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry);										
				log("Tracking: GPS will be attempted after " + (nextRetry/1000) + " seconds.");
			}		
		}		
	}	
		

	/**
	 * This is used by this API internally and must NOT be called. 
	 */
	public void providerStateChanged(LocationProvider provider, int newState) {
		if(newState == LocationProvider.TEMPORARILY_UNAVAILABLE){
			log("Tracking: Location temporarily unavailable");
			/** If Geolocation fails in MODE_OPTIMAL, try GPS after waiting an appropriate interval */
			if( mode == MODE_OPTIMAL && currentOptimalModeIsGeolocation ) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();
				if(!isModeAvailable(MODE_GPS)){
					currentOptimalModeIsGeolocation = true;
					log("Cannot switch to GPS because MODE_GPS is not available.");
				} else{
					currentOptimalModeIsGeolocation = false;
					log("Switching to GPS");
				}
				long nextRetry = getNextRetryDelay();
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();		
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry);										
				log("Tracking: Next attempt after " + (nextRetry/1000) + " seconds.");
			} 
			/** If GPS fails in MODE_OPTIMAL, try Geolocation immediately */
			else if( mode == MODE_OPTIMAL && !currentOptimalModeIsGeolocation ) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();
				long nextRetry = 0;
				if(!isModeAvailable(MODE_GEOLOCATION)){
					currentOptimalModeIsGeolocation = false;
					nextRetry = getNextRetryDelay();
					log("Cannot switch to Geolocation because MODE_GEOLOCATION is not available.");
				} else{
					currentOptimalModeIsGeolocation = true;
					nextRetry = 0;
					log("Switching to Geolocation.");
				}				
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry);										
				log("Tracking: Next attempt after " + (nextRetry/1000) + " seconds.");
			} 
			/** If Geolocation fails, retry after waiting an appropriate interval */
			else if( mode == MODE_GEOLOCATION
					//#ifdef BlackBerrySDK6.0.0
					|| mode == MODE_GEOLOCATION_CELL || mode == MODE_GEOLOCATION_WLAN
					//#endif
					) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();				
				long nextRetry = getNextRetryDelay();
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();	
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry);										
				log("Tracking: Geolocation will be attempted after " + (nextRetry/1000) + " seconds.");
			} 
			/** If GPS fails, retry after waiting an appropriate interval */
			else if( mode == MODE_GPS ) {
				switchToGPS = false;
				retryAttempt++;
				stopTracking();				
				long nextRetry = getNextRetryDelay();
				simpleLocationListener.locationEvent(SimpleLocationListener.EVENT_LOCATION_FAILED, new Long(nextRetry));
				if(restartTimer!=null){
					restartTimer.cancel();
					restartTimer = null;
				}																						
				restartTimer = new Timer();
				restartTask = new RestartTask();	
				lastValidFixTime = System.currentTimeMillis() + nextRetry;
				restartTimer.schedule(restartTask, nextRetry);										
				log("Tracking: GPS will be attempted after " + (nextRetry/1000) + " seconds.");
			}		
		}
	}
	/******************************** END OF LocationListener IMPLEMENTATION. ***************************************/
	
	private long getNextRetryDelay(){
		long nextRetry = ((5000 << retryAttempt) - 5000) * retryFactor;		
		if(nextRetry > maxRetryDelay*1000){
			nextRetry = maxRetryDelay;
		}		
		return nextRetry;
	}
	
	private class RestartTask extends TimerTask{
		public void run() {	
			if(restartTimer != null){
				restartTimer.cancel();
				restartTimer=null;
			}
			startTracking();			
		}					
	}
	
}
