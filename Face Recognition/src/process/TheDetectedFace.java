package process;

import java.awt.image.BufferedImage;

import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.math.geometry.shape.Rectangle;

// wrapping the DetectedFace
/**
 * Wraps the DetectedFace class with height, width, x and y start and end
 * coordinates.
 * 
 * @author person12
 *
 */
public class TheDetectedFace {

	public static double THRESHOLD = 10.0;
	public static int MIN_WIDTH = 50;
	public static int MAX_WIDTH = 300;
	public static int MIN_HEIGHT = 50;
	public static int MAX_HEIGHT = 300;

	double faceConf = 0;
	BufferedImage faceImage = null;
	BufferedImage image = null;
	final int extendX = 0;
	final int extendY = 10;

	int startX = 0;
	int startY = 0;
	int endX = 0;
	int endY = 0;
	int width = 0;
	int height = 0;

	/**
	 * Constructs a DetectedFace from an image and retrieves the coordinates of the
	 * rectangle that surrounds the Detected face.
	 * 
	 * @param img  the image containing the face
	 * @param face the face extracted from the image
	 */
	public TheDetectedFace(BufferedImage img, DetectedFace face) {
		if (face != null && img != null) {
			image = img;
			Rectangle rect = face.getBounds();

			width = (int) rect.width + extendX;
			height = (int) rect.height + extendY;
			if (width >= img.getWidth())
				width = img.getWidth() - 1;
			if (height >= img.getHeight())
				height = img.getHeight() - 1;

			startX = (int) rect.x;
			startY = (int) rect.y;
			endX = startX + width - 1;
			endY = startY + height - 1;

			faceImage = img.getSubimage(startX, startY, width, height);
			faceConf = face.getConfidence();
		}
	}

	/**
	 * Returns the confidence of the face for the image that the face is in.
	 * 
	 * @return the confidence of the face
	 */
	public double getFaceConf() {
		return faceConf;
	}

	/**
	 * Returns the face image.
	 * 
	 * @return the face image.
	 */
	public BufferedImage getFaceImage() {
		return faceImage;
	}

	/**
	 * Returns the lower x bound of the face image in respect to the original image.
	 * 
	 * @return the lower x bound of the face image
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * Returns the lower y bound of the face image in respect to the original image.
	 * 
	 * @return the lower y bound of the face image
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * Returns the upper x bound of the face image in respect to the original image.
	 * 
	 * @return the upper x bound of the face image
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Returns the upper y bound of the face image in respect to the original image.
	 * 
	 * @return the upper y bound of the face image
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * Returns the image containing the face.
	 * 
	 * @return the image containing the face
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Determines whether the face image is valid from the original image.
	 * Determines validity if the faceConf is high enough, or if the face image is
	 * within the bounds of the image original image.
	 * 
	 * @return true if face image if valid, false otherwise
	 */
	public boolean isValid() {
		if (faceImage == null || faceConf < THRESHOLD)
			return false;

		if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT)
			return false;
		return true;
	}

}
