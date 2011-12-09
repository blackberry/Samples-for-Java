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


package com.blackberry.toolkit.device.util;

//#ifdef BlackBerrySDK4.2.1
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;

//#else
import net.rim.device.api.system.DeviceInfo;

//#endif
import net.rim.device.api.util.StringUtilities;

/**
 * Utility Class for working with Device Software Versions.
 * 
 * @author twindsor
 * @version 1.2 (Aug 2011)
 */
public class SoftwareVersion {
	public static String VER_400 = "4.0.0";
	public static String VER_402 = "4.0.2";
	public static String VER_410 = "4.1.0";
	public static String VER_420 = "4.2.0";
	public static String VER_421 = "4.2.1";
	public static String VER_430 = "4.3.0";
	public static String VER_450 = "4.5.0";
	public static String VER_460 = "4.6.0";
	public static String VER_461 = "4.6.1";
	public static String VER_470 = "4.7.0";
	public static String VER_471 = "4.7.1";
	public static String VER_500 = "5.0.0";
	public static String VER_600 = "6.0.0";
	public static String VER_700 = "7.0.0";

	private static String MODULE_VER_CHECK = "net_rim_bb_ribbon_app";

	/**
	 * Given some number of software version 'buckets', find where the current
	 * device fits. It is assumed that the buckets given represent inclusive
	 * boundaries and that the array of buckets is in sorted order.
	 * 
	 * for example, passing in { "4.0.0", "4.5.0", "5.0.0" } should return 0 on
	 * 4.0.0 ... 4.3.0, 1 on 4.5.0 ... 4.7.1, and 2 on 5.0.0 ...
	 * 
	 * @param buckets
	 *            String array containing sorted software versions
	 * @return the index of the bucket where the current software fits. -1 if no
	 *         fit is found.
	 */
	public static int whichBucket(String[] buckets) {
		String version = getSoftwareVersion();
		for (int i = buckets.length - 1; i >= 0; i--) {
			if (StringUtilities.compareToIgnoreCase(version, buckets[i]) >= 0) {
				// Found the index to use
				return i;
			}
		}
		// Version did not fit in a bucket
		return -1;

	}

	/**
	 * Check if the current software version is part of the given Major Version.
	 * 
	 * @param majorVersion
	 *            should be one of the constants in this class, or basically any
	 *            String that follows the Version pattern.
	 * @return true if the software version starts with the given version
	 *         string, based on String.startsWith()
	 * @since 1.1
	 */
	public static boolean startsWith(String majorVersion) {
		String version = getSoftwareVersion();
		return version.startsWith(majorVersion);
	}

	/**
	 * Determine if the current software version is in the range given, from the
	 * low value (inclusive) to the high value (exclusive).
	 * 
	 * @param low
	 *            lower bound, inclusive
	 * @param high
	 *            upper bound, exclusive
	 * @return true if the software version is in the given range.
	 * @since 1.1
	 */
	public static boolean isInRange(String low, String high) {
		int bucket = whichBucket(new String[] { low, high });
		return (bucket == 1);
	}

	/**
	 * Get the major version (first 3 groups) of the given version. Will return
	 * as much as is given up to the first 3 groups, if less than 3 version
	 * groups are provided.
	 * 
	 * @param version
	 *            software version, ie: "5.0.0.607"
	 * @return Major version of the software, ie: "5.0.0"
	 * @since 1.1
	 */
	public static String getMajorVersion(String version) {
		if (version != null) {
			int index = 0;
			int group = 0;
			while (index < version.length()) {
				index = version.indexOf('.', index);
				++group;
				if (group > 2) {
					break;
				}
			}
			return version.substring(0, index);
		}
		return "0.0.0";
	}

	/**
	 * Get the major version (first 3 groups) of the device software.
	 * 
	 * @return Major version of the current software, ie: "5.0.0"
	 */
	public static String getMajorVersion() {
		return getMajorVersion(getSoftwareVersion());
	}

//#ifdef BlackBerrySDK4.2.1

	/**
	 * Get the device software version using the Ribbon Application. Will
	 * typically have 4 levels of precision (ie: 4.5.0.77)
	 * 
	 * @return the software version of the device as a String, based on the
	 *         version of the Ribbon application.
	 */
	public static String getSoftwareVersion() {
		// Loop through the running apps to get one that we can check the
		// version of
		ApplicationDescriptor[] descriptors = ApplicationManager.getApplicationManager().getVisibleApplications();
		int size = descriptors.length;

		for (int i = size - 1; i >= 0; --i) {
			if ((descriptors[i].getModuleName()).equals(MODULE_VER_CHECK)) {
				return descriptors[i].getVersion();
			}
		}
		// Something unexpected happened in determining the version.
		return "0.0.0.0";
	}

//#else
	
	/**
	 * Get the device software version using DeviceInfo API. Will typically have
	 * 4 levels of precision (ie: 4.5.0.77)
	 * 
	 * @return the software version of the device as a String.
	 * @since 1.1
	 */
	public static String getSoftwareVersion() {
		return DeviceInfo.getSoftwareVersion();
	}
	
//#endif

}
