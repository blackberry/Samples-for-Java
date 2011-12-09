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

package com.blackberry.toolkit.ui.images;

import java.io.InputStream;

import javax.microedition.io.Connector;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

/**
 * Wrapper for an image that is stored as a file on the filesystem.
 * 
 * @author twindsor
 * @version 1.0 (July 2010)
 */
public class BitmapFile {

	private Bitmap _bitmap;

	/**
	 * Load a Bitmap from the File.
	 * 
	 * @param filename
	 *            the fully qualified URL to load.
	 */
	public BitmapFile(String filename) {
		_bitmap = load(filename);
	}

	public Bitmap getBitmap() {
		return _bitmap;
	}

	/**
	 * Load the given file and parse it as an EncodedImage, then return the
	 * Bitmap.
	 * 
	 * @param filename
	 *            the fully qualified URL to load.
	 * @return the Bitmap loaded from the file.
	 */
	private Bitmap load(String filename) {
		try {
			InputStream input = Connector.openInputStream(filename);
			byte[] data = IOUtilities.streamToBytes(input);
			EncodedImage image = EncodedImage.createEncodedImage(data, 0, data.length);
			return image.getBitmap();
		} catch (Exception e) {
			// Just output errors to console
			System.out.print(e.toString());
		}
		return null;
	}

}
