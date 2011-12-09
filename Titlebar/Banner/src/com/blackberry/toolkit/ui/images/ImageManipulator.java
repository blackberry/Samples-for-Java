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

import javax.microedition.lcdui.Image;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.math.VecMath;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYDimension;

/**
 * Class for Rotating, Mirroring, and Scaling Bitmaps
 * 
 * Supports:
 * <ul>
 * <li>Transformations by angle around 0,0 and mirroring in X and/or Y axis.</li>
 * <li>Transformation by arbitrary transformation matrix.</li>
 * <li>Scaling applied independently of any transformation.</li>
 * <li>Scaling separately in X and Y axis</li>
 * <li>Results can be painted to a new Bitmap, or directly on any Graphics
 * context</li>
 * </ul>
 * 
 * @version 1.6 (December 2009)
 */
public class ImageManipulator {

	/**
	 * This bitmap scaling option marks that scaling should proceed without
	 * preserving aspect ratio. The source bitmap is fit to the dimensions of
	 * the destination bitmap. Copied from 5.0 Bitmap API.
	 * 
	 * <pre>
	 * http://www.blackberry.com/developers/docs/5.0.0api/net/rim/device/api/system/Bitmap.html
	 * </pre>
	 * 
	 * @since 1.1
	 */
	public static final int SCALE_STRETCH = 0;
	/**
	 * This bitmap scaling option marks that scaling is done with preserving the
	 * aspect ratio. The source bitmap fills the destination bitmap completely
	 * and outstanding parts of the source bitmap are not copied to the
	 * destination bitmap. Copied from 5.0 Bitmap API.
	 * 
	 * <pre>
	 * http://www.blackberry.com/developers/docs/5.0.0api/net/rim/device/api/system/Bitmap.html
	 * </pre>
	 * 
	 * @since 1.1
	 */
	public static final int SCALE_TO_FILL = 1;
	/**
	 * This bitmap scaling option marks that scaling is done with preserving the
	 * aspect ratio. The source bitmap is fit to the dimensions of the
	 * destination bitmap and a part of destination bitmap remains unchanged.
	 * Copied from 5.0 Bitmap API.
	 * 
	 * <pre>
	 * http://www.blackberry.com/developers/docs/5.0.0api/net/rim/device/api/system/Bitmap.html
	 * </pre>
	 * 
	 * @since 1.1
	 */
	public static final int SCALE_TO_FIT = 2;
	/**
	 * Used for compatibility with 5.0. Has no effect, but the values are
	 * checked.
	 * 
	 * @since 1.1
	 */
	public static final int FILTER_LANCZOS = 0;
	/**
	 * Used for compatibility with 5.0. Has no effect, but the values are
	 * checked.
	 * 
	 * @since 1.1
	 */
	public static final int FILTER_BOX = 1;
	/**
	 * Used for compatibility with 5.0. Has no effect, but the values are
	 * checked.
	 * 
	 * @since 1.1
	 */
	public static final int FILTER_BILINEAR = 2;

	// The image to alter
	private Bitmap bitmap;

	// The graphics context to use
	// if one is not provided, a bitmap will be

	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int UX = 0;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int UY = 3;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int UZ = 6;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int VX = 1;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int VY = 4;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int VZ = 7;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int WX = 2;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int WY = 5;
	/**
	 * Constants for Matrix locations within a single dimensional array
	 * 
	 * <pre>
	 * | UX  UY  UZ |
	 * | VX  VY  VZ |
	 * | WX  WY  WZ |
	 * </pre>
	 * 
	 * @since 1.3
	 */
	public static final int WZ = 8;

	/*
	 * the transform matrix, initialized to identity
	 */
	private int[] transformMatrix = VecMath.IDENTITY_3X3;
	// set scale in X axis
	private int scaleX = Fixed32.ONE;
	// set scale in Y axis
	private int scaleY = Fixed32.ONE;

	// track separate texture origin for non-orthogonal rotations
	private int textureX;
	private int textureY;

	// track whether the rotation a multiple of 90 degrees
	private boolean orthogonal;

	// actual scale factor when applied to pixels
	private int resultantScaleX = Fixed32.ONE;
	private int resultantScaleY = Fixed32.ONE;

	// Initialize the X and Y point arrays
	// These define the resultant image points
	private int[] bitmapXPts = { 0, 0, 0, 0 };
	private int[] bitmapYPts = { 0, 0, 0, 0 };

	// Store the resulting dimensions of the transformation
	private XYDimension transformedRegion = new XYDimension();

	// Flag whether the transformation has been applied
	private boolean transformationApplied;

	// Constant for drawing in "15.16 fixed point format"
	private static final int SHIFT16 = 1 << 16;

	// Set the background color default white
	private int backgroundColor = Color.WHITE;

	// Set default alpha to transparent
	private int backgroundAlpha = 0;

	// static object for use in the static methods
	private static ImageManipulator imageManipulator;

	/**
	 * Scale the provided bitmap
	 * 
	 * @param bitmap
	 *            the bitmap to scale
	 * @param scale
	 *            the new scale factor in Fixed32
	 * @return the scaled bitmap
	 * @since 1.1
	 */
	public static Bitmap scale(Bitmap bitmap, int scale) {
		if (imageManipulator == null) {
			imageManipulator = new ImageManipulator(bitmap);
		} else {
			imageManipulator.setBitmap(bitmap);
		}
		imageManipulator.resetTransform();
		imageManipulator.setScale(scale);
		return imageManipulator.transformAndPaintBitmap();
	}

	/**
	 * Rotate the provided Bitmap
	 * 
	 * @param bitmap
	 *            the bitmap to rotate
	 * @param angle
	 *            the angle in degrees to rotate, counterclockwise around the
	 *            top left corner.
	 * @return the rotated bitmap
	 * @since 1.1
	 */
	public static Bitmap rotate(Bitmap bitmap, int angle) {
		if (imageManipulator == null) {
			imageManipulator = new ImageManipulator(bitmap);
		} else {
			imageManipulator.setBitmap(bitmap);
		}
		imageManipulator.resetTransform();
		imageManipulator.transformByAngle(angle, false, false);
		return imageManipulator.transformAndPaintBitmap();
	}

	public ImageManipulator(Bitmap image) {
		bitmap = image;
	}

	/**
	 * Reset the transformation to the identity: No rotation and Scale is 1.0
	 * 
	 * @since 1.1
	 */
	public void resetTransform() {
		transformByMatrix(Fixed32.ONE, 0, 0, Fixed32.ONE);
		orthogonal = true;
		setScale(Fixed32.ONE);
		transformationApplied = false;
	}

	/**
	 * Setup the given transformation matrix, given the cosine and sine values,
	 * and mirroring options
	 * 
	 * @param matrix
	 *            the transform matrix, must be of size 4
	 * @param cos
	 *            cosine of the angle in radians
	 * @param sin
	 *            sine of the angle in radians
	 * @param mirrorX
	 *            to mirror in the X axis
	 * @param mirrorY
	 *            to mirror in the Y axis
	 */
	private void setTransform(int[] matrix, int cos, int sin, boolean mirrorX, boolean mirrorY) {
		// default, non mirrored transformation
		matrix[UX] = cos;
		matrix[UY] = -sin;
		matrix[VX] = sin;
		matrix[VY] = cos;
		if (mirrorY) {
			matrix[UX] = -cos;
			matrix[UY] = sin;
		}
		if (mirrorX) {
			matrix[VX] = -sin;
			matrix[VY] = -cos;
		}
	}

	/**
	 * Rotate the image about an angle and/or mirror about an axis
	 * 
	 * @param angle
	 *            the angle in degrees, postive rotates counterclockwise
	 * @param mirrorX
	 *            true to mirror in the X axis
	 * @param mirrorY
	 *            true to mirror in the Y axis
	 */
	public void transformByAngle(int angle, boolean mirrorX, boolean mirrorY) {
		if (angle % 90 == 0) {
			orthogonal = true;
		} else {
			orthogonal = false;
		}
		angle = Fixed32.toFP(angle);
		int cos = Fixed32.cosd(angle);
		int sin = Fixed32.sind(angle);
		setTransform(transformMatrix, cos, sin, mirrorX, mirrorY);
		transformationApplied = false;
	}

	/**
	 * Rotate the image using a custom transformation matrix. The matrix takes
	 * the form:
	 * 
	 * <pre>
	 * | ux  uy 0 |
	 * | vx  vy 0 |
	 * | 0   0  1 |
	 * </pre>
	 * 
	 * Where, the identity (matrix that does no rotation) is:
	 * 
	 * <pre>
	 * | 1  0  0 |
	 * | 0  1  0 |
	 * | 0  0  1 |
	 * </pre>
	 * 
	 * @param ux
	 *            in Fixed32 format (15.16)
	 * @param uy
	 *            in Fixed32 format (15.16)
	 * @param vx
	 *            in Fixed32 format (15.16)
	 * @param vy
	 *            in Fixed32 format (15.16)
	 */
	public void transformByMatrix(int ux, int uy, int vx, int vy) {
		transformMatrix[UX] = ux;
		transformMatrix[UY] = uy;
		transformMatrix[VX] = vx;
		transformMatrix[VY] = vy;
		orthogonal = false; // not completely accurate
		transformationApplied = false;
	}

	/**
	 * Applies the transformation matrix and scaling to the region defined by
	 * the bitmap. Uses the parameters given in the setter methods and applies
	 * to the internal region.
	 * 
	 * Called by the transformAndPaintBitmap methods before painting.
	 */
	public void applyTransformation() {

		// Check if this is necessary
		if (transformationApplied) {
			return;
		}

		int bitmapRight = Fixed32.toFP(bitmap.getWidth());
		int bitmapBottom = Fixed32.toFP(bitmap.getHeight());

		// scale the image size as requested
		bitmapRight = Fixed32.mul(bitmapRight, scaleX);
		bitmapBottom = Fixed32.mul(bitmapBottom, scaleY);

		// init matrix
		bitmapXPts[0] = 0;
		bitmapYPts[0] = 0;

		bitmapXPts[1] = 0;
		bitmapYPts[1] = bitmapBottom;

		bitmapXPts[2] = bitmapRight;
		bitmapYPts[2] = bitmapBottom;

		bitmapXPts[3] = bitmapRight;
		bitmapYPts[3] = 0;

		long rotation;
		for (int i = 0; i < 4; i++) {
			rotation = VecMath.multiplyPoint(transformMatrix, 0, bitmapXPts[i], bitmapYPts[i]);
			bitmapXPts[i] = (int) rotation;
			bitmapYPts[i] = (int) (rotation >>> 32);
		}

		// calculate the new boundaries (as rotation can require a
		// larger bitmap to hold the image)
		int minX = 0;
		int minY = 0;
		int maxX = 0;
		int maxY = 0;

		for (int i = 1; i < 4; i++) {
			if (bitmapXPts[i] < minX) {
				minX = bitmapXPts[i];
			} else if (bitmapXPts[i] > maxX) {
				maxX = bitmapXPts[i];
			}

			if (bitmapYPts[i] < minY) {
				minY = bitmapYPts[i];
			} else if (bitmapYPts[i] > maxY) {
				maxY = bitmapYPts[i];
			}
		}

		// shift the new boundaries to be anchored in the top left at
		// 0,0 and restore to int coordinates.
		for (int i = 0; i < 4; i++) {
			bitmapXPts[i] = Fixed32.toInt(bitmapXPts[i] - minX);
			bitmapYPts[i] = Fixed32.toInt(bitmapYPts[i] - minY);
		}

		/**
		 * When creating a non-orthogonal rotation, the resultant path may not
		 * be properly rectangular after rotation. This avoids misalignment of
		 * the texture and path by slightly spreading the texture over the
		 * space, making it a bit larger.
		 */
		if (!orthogonal) {
			// New texture origin outside the original region
			textureX = Fixed32.toFP(-2);
			textureY = Fixed32.toFP(-2);

			// rotate the texture origin just as the region was rotated
			rotation = VecMath.multiplyPoint(transformMatrix, 0, textureX, textureY);
			textureX = (int) rotation;
			textureY = (int) (rotation >>> 32);

			// adjust the texture origin to the same point space as the region
			textureX = Fixed32.toInt(textureX - minX);
			textureY = Fixed32.toInt(textureY - minY);

			// scale the texture so that it is 2 pixels larger than the region
			// on all sides
			resultantScaleX = Fixed32.div((bitmapRight + Fixed32.toFP(4)), Fixed32.toFP(bitmap.getWidth()));
			resultantScaleY = Fixed32.div((bitmapBottom + Fixed32.toFP(4)), Fixed32.toFP(bitmap.getHeight()));

		} else {
			// orthogonal roations can use exact coordinates since the region is
			// perfectly formed
			textureX = bitmapXPts[0];
			textureY = bitmapYPts[0];
			resultantScaleX = scaleX;
			resultantScaleY = scaleY;
		}
		// set the resulting dimensions of a bitmap containing this rotation
		transformedRegion.set(Fixed32.toRoundedInt(maxX - minX) + 1, Fixed32.toRoundedInt(maxY - minY) + 1);
		transformationApplied = true;
	}

	/**
	 * Get the background color.
	 * 
	 * @return integer in the format 0xAARRGGBB.
	 * @since 1.5
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set the background color. Takes an integer in the format 0xAARRGGBB,
	 * where AA is the alpha value.
	 * 
	 * @param backgroundColor
	 * @since 1.5
	 */
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Set the background alpha value.
	 * 
	 * @param backgroundAlpha
	 *            0-255, 0 is transparent.
	 */
	public void setBackgroundAlpha(int backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
	}

	/**
	 * Get the background alpha
	 * 
	 * @return alpha setting of the background (0-255).
	 */
	public int getBackgroundAlpha() {
		return backgroundAlpha;
	}

	/**
	 * Paint the transformed Bitmap using the given Graphics context. Paints the
	 * area transparent before painting the bitmap.
	 * 
	 * @param g
	 *            the {@link Graphics} context to use - from a screen or bitmap,
	 *            etc.
	 */
	private void paintTransformedBitmap(Graphics g) {
		paintTransformedBitmap(g, textureX, textureY);
	}

	/**
	 * Paint the transformed Bitmap using the given Graphics context. Paints the
	 * area transparent before painting the bitmap.
	 * 
	 * @param g
	 *            the {@link Graphics} context to use - from a screen or bitmap,
	 *            etc.
	 * @param textureOriginX
	 *            x value in the original bitmap to start drawing from
	 * @param textureOriginY
	 *            y value in the original bitmap to start drawing from
	 * @since 1.1
	 */
	private void paintTransformedBitmap(Graphics g, int textureOriginX, int textureOriginY) {
		// Make the drawing space transparent first before painting
		g.setGlobalAlpha(getBackgroundAlpha());
		g.setBackgroundColor(getBackgroundColor());
		g.clear();
		g.setGlobalAlpha(255);
		/**
		 * Keep the precision of our transformation and Scale the drawing as
		 * well. Scale is applied as though a matrix of the form
		 * 
		 * <pre>
		 * | ScaleX  0     0|
		 * | 0    ScaleY   0|
		 * | 0       0     1|
		 * </pre>
		 * 
		 * is multiplied by the Transformation matrix
		 **/

		int dux = Fixed32.div(transformMatrix[UX], resultantScaleX);
		int dvx = Fixed32.div(transformMatrix[VX], resultantScaleY);
		int duy = Fixed32.div(transformMatrix[UY], resultantScaleX);
		int dvy = Fixed32.div(transformMatrix[VY], resultantScaleY);

		g.drawTexturedPath(bitmapXPts, bitmapYPts, null, null, textureOriginX, textureOriginY, dux, dvx, duy, dvy, bitmap);
	}

	/**
	 * Apply the transformation and paint on the given Graphics context
	 * 
	 * @param g
	 *            the {@link Graphics} context to use - from a screen or bitmap,
	 *            etc.
	 */
	public void transformAndPaintBitmap(Graphics g) {
		applyTransformation();
		paintTransformedBitmap(g);
	}

	/**
	 * Apply the transformation, paint to a new {@link Bitmap} and return it
	 * 
	 * @return The transformed {@link Bitmap} of the required size to display
	 *         the entire transformed (rotated, scaled) image, with an alpha
	 *         channel so that the space around the bitmap is transparent.
	 */
	public Bitmap transformAndPaintBitmap() {

		applyTransformation();

		// create the new bitmap
		Bitmap transformedBitmap = new Bitmap(bitmap.getType(), transformedRegion.width, transformedRegion.height);
		transformedBitmap.createAlpha(Bitmap.ALPHA_BITDEPTH_8BPP);
		Graphics graphics = new Graphics(transformedBitmap);
		paintTransformedBitmap(graphics);
		return transformedBitmap;
	}

	/**
	 * Scale the internal bitmap into the provided Bitmap. Alternate
	 * implementation of method from 5.0 Bitmap API.
	 * 
	 * <pre>
	 * {@link http://www.blackberry.com/developers/docs/5.0.0api/net/rim/device/api/system/Bitmap.html}
	 * </pre>
	 * 
	 * @param dst
	 *            the {@link Bitmap} to paint on, scale comes from this.
	 * @param filterType
	 *            has no real effect, but must be one of:
	 *            <ul>
	 *            <li>{@link #FILTER_LANCZOS}</li>
	 *            <li>{@link #FILTER_BOX}</li>
	 *            <li>{@link #FILTER_BILINEAR}</li>
	 *            </ul>
	 * 
	 * @since 1.1
	 */
	public void scaleInto(Bitmap dst, int filterType) {
		scaleInto(0, 0, bitmap.getWidth(), bitmap.getHeight(), dst, 0, 0, dst.getWidth(), dst.getHeight(), filterType);
	}

	/**
	 * Scale the internal bitmap into the provided Bitmap, using the provided
	 * Aspect Ratio rules. Alternate implementation of method from 5.0 Bitmap
	 * API.
	 * 
	 * <pre>
	 * {@link http://www.blackberry.com/developers/docs/5.0.0api/net/rim/device/api/system/Bitmap.html}
	 * </pre>
	 * 
	 * @param dst
	 *            the {@link Bitmap} to paint on, scale comes from this.
	 * @param filterType
	 *            has no real effect, but must be one of:
	 *            <ul>
	 *            <li>{@link #FILTER_LANCZOS}</li>
	 *            <li>{@link #FILTER_BOX}</li>
	 *            <li>{@link #FILTER_BILINEAR}</li>
	 *            </ul>
	 * @param iAspectRatioOption
	 *            one of:
	 *            <ul>
	 *            <li>{@link #SCALE_STRETCH}</li>
	 *            <li>{@link #SCALE_TO_FILL}</li>
	 *            <li>{@link #SCALE_TO_FIT}</li>
	 *            </ul>
	 * @since 1.1
	 */
	public void scaleInto(Bitmap dst, int filterType, int iAspectRatioOption) {

		// Maintain same interface as 5.0 API
		if (iAspectRatioOption < 0 || iAspectRatioOption > 2) {
			throw new IllegalArgumentException("Invalid aspect ratio parameter");
		}

		int dstWidth = Fixed32.toFP(dst.getWidth());
		int srcWidth = Fixed32.toFP(bitmap.getWidth());
		int dstHeight = Fixed32.toFP(dst.getHeight());
		int srcHeight = Fixed32.toFP(bitmap.getHeight());

		// Find the scale values in each axis to compare
		int scaleHoriz = Fixed32.div(dstWidth, srcWidth);
		int scaleVert = Fixed32.div(dstHeight, srcHeight);

		switch (iAspectRatioOption) {
		case SCALE_STRETCH:
			scaleInto(0, 0, bitmap.getWidth(), bitmap.getHeight(), dst, 0, 0, dst.getWidth(), dst.getHeight(), filterType);
			break;
		case SCALE_TO_FILL:
			/*
			 * Destination should be completely filled by source, with some
			 * source not being painted if necessary. Also centers the painting.
			 */
			int srcShiftX;
			int srcShiftY;

			if (scaleVert > scaleHoriz) {
				// source height fills the destination
				int srcRegionWidth = Fixed32.div(dstWidth, scaleVert);
				srcShiftX = Fixed32.toRoundedInt(Fixed32.mul((srcWidth - srcRegionWidth) >> 1, scaleVert));
				scaleInto(srcShiftX, 0, Fixed32.toRoundedInt(srcRegionWidth), bitmap.getHeight(), dst, 0, 0, dst.getWidth(), dst.getHeight(),
						filterType);
			} else if (scaleHoriz > scaleVert) {
				// source width fills destination
				int srcRegionHeight = Fixed32.div(dstHeight, scaleHoriz);
				srcShiftY = Fixed32.toRoundedInt(Fixed32.mul((srcHeight - srcRegionHeight) >> 1, scaleVert));
				scaleInto(0, srcShiftY, bitmap.getWidth(), Fixed32.toRoundedInt(srcRegionHeight), dst, 0, 0, dst.getWidth(), dst.getHeight(),
						filterType);
			} else {
				// Both bitmaps have the same aspect ratio
				scaleInto(0, 0, bitmap.getWidth(), bitmap.getHeight(), dst, 0, 0, dst.getWidth(), dst.getHeight(), filterType);
			}
			break;
		case SCALE_TO_FIT:
			/*
			 * Source should be completely contained by destination, with some
			 * destination not being painted if necessary. Also centers the
			 * painting.
			 */
			int dstShiftX;
			int dstShiftY;

			if (scaleVert < scaleHoriz) {
				// source is fitted vertically with blank sides
				int scaledWidth = Fixed32.mul(scaleVert, srcWidth);
				dstShiftX = Fixed32.toInt((dstWidth - scaledWidth) >> 1);
				scaleInto(0, 0, bitmap.getWidth(), bitmap.getHeight(), dst, dstShiftX, 0, Fixed32.toRoundedInt(scaledWidth), dst.getHeight(),
						filterType);
			} else if (scaleHoriz < scaleVert) {
				// source fits horizontally with blank top and bottom
				int scaledHeight = Fixed32.mul(scaleHoriz, srcHeight);
				dstShiftY = Fixed32.toInt((dstHeight - scaledHeight) >> 1);
				scaleInto(0, 0, bitmap.getWidth(), bitmap.getHeight(), dst, 0, dstShiftY, dst.getWidth(), Fixed32.toRoundedInt(scaledHeight),
						filterType);
			} else {
				// Both bitmaps have the same aspect ratio
				scaleInto(0, 0, bitmap.getWidth(), bitmap.getHeight(), dst, 0, 0, dst.getWidth(), dst.getHeight(), filterType);
			}
			break;

		}

	}

	/**
	 * Scale the internal bitmap into the provided Bitmap. Alternate
	 * implementation of method from 5.0 Bitmap API.
	 * 
	 * <pre>
	 * {@link http://www.blackberry.com/developers/docs/5.0.0api/net/rim/device/api/system/Bitmap.html}
	 * </pre>
	 * 
	 * 
	 * @param srcLeft
	 *            X coordinate of the top left corner of the area to be copied
	 *            from the source bitmap.
	 * @param srcTop
	 *            Y coordinate of the top left corner of the area to be copied
	 *            from the source bitmap.
	 * @param srcWidth
	 *            Width of the area to be copied from the source bitmap.
	 * @param srcHeight
	 *            Height of the area to be copied from the source bitmap.
	 * @param dst
	 *            the {@link Bitmap} to paint on, scale comes from this.
	 * @param dstLeft
	 *            X coordinate of the top left corner of the area to be copied
	 *            to the destination bitmap.
	 * @param dstTop
	 *            Y coordinate of the top left corner of the area to be copied
	 *            to the destination bitmap.
	 * @param dstWidth
	 *            Width of the area to be copied from the source bitmap.
	 * @param dstHeight
	 *            Height of the area to be copied to the destination bitmap.
	 * @param filterType
	 *            has no real effect, but must be one of:
	 *            <ul>
	 *            <li>{@link #FILTER_LANCZOS}</li>
	 *            <li>{@link #FILTER_BOX}</li>
	 *            <li>{@link #FILTER_BILINEAR}</li>
	 *            </ul>
	 * @throws NullPointerException
	 *             Thrown if 'dst' is null.
	 * @throws IllegalArgumentException
	 *             Thrown if the destination bitmap is read-only.
	 * @throws IllegalArgumentException
	 *             Thrown if illegal filter type is specified.
	 * @since 1.1
	 */
	public void scaleInto(int srcLeft, int srcTop, int srcWidth, int srcHeight, Bitmap dst, int dstLeft, int dstTop, int dstWidth, int dstHeight,
			int filterType) {

		// Maintain same interface as 5.0 API
		if (dst == null) {
			throw new NullPointerException("Destination bitmap can not be set to NULL");
		}
		if (!dst.isWritable()) {
			throw new IllegalArgumentException("Destination bitmap should not be read-only");
		}
		if (filterType < 0 || filterType > 2) {
			throw new IllegalArgumentException("Invalid filter type");
		}

		// make sure we're starting with no rotation.
		resetTransform();

		// set the region to be drawn on
		bitmapXPts = new int[] { dstLeft, dstLeft, dstLeft + dstWidth, dstLeft + dstWidth };
		bitmapYPts = new int[] { dstTop, dstTop + dstHeight, dstTop + dstHeight, dstTop };

		// Calculate the new scale based on the region sizes
		resultantScaleX = Fixed32.div(Fixed32.toFP(dstWidth), Fixed32.toFP(srcWidth));
		resultantScaleY = Fixed32.div(Fixed32.toFP(dstHeight), Fixed32.toFP(srcHeight));

		Graphics graphics = new Graphics(dst);
		paintTransformedBitmap(graphics, dstLeft - srcLeft, dstTop - srcTop);
	}

	/**
	 * Round the value to the nearest int
	 * 
	 * @param value
	 * @return rounded value
	 * @since 1.1
	 */
	public static int round(double value) {
		if (value < 0) {
			int roundedDown = (int) value;
			if (value != (roundedDown - 0.5f)) {
				return (int) (value - 0.5f);
			} // Special case to match java.lang.Math round functionality. Same
			// rounding as positive float when value is negative exact half
			// (eg. -x.5)

		}
		return (int) (value + 0.5f);
	}

	/**
	 * Convert a double value to a Fixed32 integer
	 * 
	 * @param value
	 * @return the value as an integer in Fixed32 format (16.16);
	 * @since 1.3
	 */
	public static int toFP(double value) {
		return round(value * SHIFT16);
	}

	/**
	 * Utility to convert a RIM API Bitmap into a J2ME LCDUI Image. This assists
	 * in using this library within a MIDP application, where the
	 * drawTexturedPath method is not available.
	 * 
	 * @param bitmap
	 *            the source Bitmap
	 * @return the Image object based on the Bitmap data
	 * @since 1.2
	 */
	public static Image convert(Bitmap bitmap) {
		int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getARGB(data, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		return Image.createRGBImage(data, bitmap.getWidth(), bitmap.getHeight(), true);
	}

	/**
	 * Return the unmodified bitmap
	 * 
	 * @return the original, unmodified bitmap provided in the constructor or
	 *         through setBitmap(Bitmap)
	 */
	public Bitmap getOriginalBitmap() {
		return bitmap;
	}

	/**
	 * Set a new bitmap to be transformed
	 * 
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		transformationApplied = false;
	}

	/**
	 * The Scale factor in the X dimension
	 * 
	 * @return the scale as a Fixed32 integer (16.16)
	 */
	public double getScaleX() {
		return scaleX;
	}

	/**
	 * Set the X dimension scale independently
	 * 
	 * @param scaleX
	 *            as a Fixed32 integer (16.16)
	 */
	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
		transformationApplied = false;
	}

	/**
	 * The Scale factor in the Y dimension
	 * 
	 * @return the scale as a Fixed32 integer (16.16)
	 */
	public int getScaleY() {
		return scaleY;
	}

	/**
	 * Set the Y dimension scale independently
	 * 
	 * @param scaleY
	 *            as a Fixed32 integer (16.16)
	 */
	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
		transformationApplied = false;
	}

	/**
	 * Set the X and Y scales together
	 * 
	 * @param scale
	 *            as a Fixed32 integer (16.16)
	 */
	public void setScale(int scale) {
		this.setScaleX(scale);
		this.setScaleY(scale);
	}
}
