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

import net.rim.device.api.gps.BlackBerryLocation;

/**
 * Implement this interface to listen for events triggered by {@link SimpleLocationProvider} for a tracking session. Implementation of this interface
 * should be registered with {@link SimpleLocationProvider} by calling {@link SimpleLocationProvider#addSimpleLocationListener(SimpleLocationListener, int)}.
 * Otherwise, no tracking session will be started and no location fixes will be delivered to this listener interface. Only one {@link SimpleLocationListener}
 * implementation can be registered with a {@link SimpleLocationProvider}.
 * <p>
 * <pre>
 * // The following call registers simpleLocationListenerImpl (a {@link SimpleLocationListener} implementation) to receive location updates every 5 seconds.
 * simpleLocationProvider.addSimpleLocationListener(simpleLocationListenerImpl, 5);
 * 
 * // The following call removes simpleLocationListenerImpl (a {@link SimpleLocationListener} implementation) and stops any further location updates.
 * simpleLocationProvider.removeSimpleLocationListener();
 *  
 * </pre>
 * @author Shadid Haque 
 *
 */
public interface SimpleLocationListener {
	/**
	 * Triggered when the SimpleLocationProvider starts/restarts to acquire a location fix.
	 * <p>
	 * eventData is an {@link Integer} representing the attempt #. To learn more about attempt please see {@link SimpleLocationProvider#setRetryFactor(int)}
	 */
	public static final int EVENT_ACQUIRING_LOCATION = 0;
	/**
	 * Triggered when a location is obtained via GPS.
	 * <p>
	 * eventData is a {@link BlackBerryLocation} object.
	 */
	public static final int EVENT_GPS_LOCATION = 1;
	/**
	 * Triggered when a location is obtained via cell tower based geolocation.
	 * <p>
	 * eventData is a {@link BlackBerryLocation} object.
	 */
	public static final int EVENT_CELL_GEOLOCATION = 2;
	/**
	 * Triggered when a location is obtained via wlan based geolocation.
	 * <p>
	 * eventData is a {@link BlackBerryLocation} object.
	 */
	public static final int EVENT_WLAN_GEOLOCATION = 3;
	/**
	 * Triggered when a valid location is obtained but the mode is unknown.
	 * <p>
	 * eventData is a {@link BlackBerryLocation} object.
	 */
	public static final int EVENT_UNKNOWN_MODE = 4;
	/**
	 * Triggered when obtaining a location fix fails within the specified timeout -  
	 * (See {@link SimpleLocationProvider#setGeolocationTimeout(int)} and {@link SimpleLocationProvider#setGPSTimeout(int)}). 
	 * The SimpleLocationProvider will retry after a certain time has passed. For more information on retry mechanisms please 
	 * see {@link SimpleLocationProvider#setRetryFactor(int)}.
	 * <p> 
	 * eventData is a {@link Long} representing the timestamp of the next scheduled retry.
	 */
	public static final int EVENT_LOCATION_FAILED = 5;
	
	
	/**
	 * Triggers one of the EVENT_* events defined in this interface.
	 * @param event	an event code. One of EVENT_*
	 * @param eventData	an Object that contains contextual information of the event. See descriptions of each EVENT_*.
	 */
	public void locationEvent(int event, Object eventData);
	
	/**
	 * Called when a debug message is available. Applications can use this log to debug this API.
	 * @param msg	A debug message.
	 */
	public void debugLog(String msg);
}
