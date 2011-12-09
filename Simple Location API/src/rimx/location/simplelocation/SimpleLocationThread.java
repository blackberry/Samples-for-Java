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

import javax.microedition.location.LocationListener;

import net.rim.device.api.gps.BlackBerryCriteria;
import net.rim.device.api.gps.BlackBerryLocation;
import net.rim.device.api.gps.BlackBerryLocationProvider;


/**
 * Handles all tasks to setup a tracking session.
 * @author shaque
 *
 */
class SimpleLocationThread extends Thread{	
	private BlackBerryCriteria criteria;
	private BlackBerryLocationProvider locationProvider = null;	
	private LocationListener listener = null;
	private SimpleLocationProvider simpleProvider;
		
	private int trackingInterval = 5;
	
	
	public SimpleLocationThread(BlackBerryCriteria criteria, int interval, LocationListener listener, SimpleLocationProvider simpleProvider){
		this.criteria = criteria;
		this.trackingInterval = interval;		
		this.listener = listener;		
		this.simpleProvider = simpleProvider;
	}
	
	public void run(){		
		startTracking();			
	}
			
	private void startTracking(){
		try{
			locationProvider = (BlackBerryLocationProvider) BlackBerryLocationProvider.getInstance(criteria);
			simpleProvider.updateLocationProviderReference(locationProvider);
			locationProvider.setLocationListener(listener, trackingInterval, -1, -1);			
		} catch (Throwable t){
			
		}
	}
	
	public BlackBerryLocation getSingleFix(int timeout){
		try{
			locationProvider = (BlackBerryLocationProvider) BlackBerryLocationProvider.getInstance(criteria);
			simpleProvider.updateLocationProviderReference(locationProvider);
			return (BlackBerryLocation)locationProvider.getLocation(timeout);	
		} catch (Throwable t){
			return null;
		}		
	}
	
	public void resetProvider(){		
		if(locationProvider!=null){			
			locationProvider.setLocationListener(null, 0, 0, 0);
			locationProvider.reset();
			locationProvider = null;			
		}
	}	
}