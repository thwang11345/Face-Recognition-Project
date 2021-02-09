package process;

import java.util.Iterator;
import java.util.List;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import java.awt.image.BufferedImage;
/**
 * Used to detect the face in an image. Uses the HaarCascadeDetector from OpenIMAJ. 
 * http://openimaj.org/index.html 
 * @author Thomas Hwang
 *
 */
public class FaceDetection {

	static HaarCascadeDetector detector = new HaarCascadeDetector();
	/**
	 * Constructs a FaceDetection
	 */
	public FaceDetection() {

	}
	/**
	 * Returns the detected face from the image. If there are multiple faces in the image, chooses
	 * the face with the highest confidence score. If there are no faces, returns null. 
	 * @param img the image to process
	 * @return the detected face with the highest confidence score. If no face exists, returns null. 
	 */
	static public DetectedFace detectFace(BufferedImage img) {
		DetectedFace bestFace = null;
		List<DetectedFace> faces = detector.detectFaces(ImageUtilities.createFImage(img));
		Iterator<DetectedFace> dfi = faces.iterator();
		if (dfi == null)
			return bestFace;
		double max = -1;
		while (dfi.hasNext()) {
			DetectedFace face = dfi.next();
			if (face.getConfidence() > max) {
				max = face.getConfidence();
				bestFace = face;
			}

		}
		return bestFace;

	}

}
