package webcam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import process.FaceDetection;
import process.TheDetectedFace;
import view.ImageFrame;

import org.openimaj.image.processing.face.detection.DetectedFace;

/**
 * Uses the web camera for capturing images in real time. Captures 2 images
 * every second with a max of 100 images and updates the image frame with face
 * images.
 * 
 * @author Thomas Hwang
 *
 */
public class WebCamLiveCapture implements Runnable {

	Webcam webcam;
	String path;
	ImageFrame faceFrame;
	int count = 0;
	final int maxImage = 100;

	/**
	 * Sets the webcam and inits the directory to save the captured images. Also
	 * sets the image frame used to display the images that are captured.
	 * 
	 * @param webcam    the webcam used for capture
	 * @param path      the directory to save the captured images
	 * @param faceFrame the image frame used to display the images that are captured
	 */
	public WebCamLiveCapture(Webcam webcam, String path, ImageFrame faceFrame) {
		this.webcam = webcam;
		this.path = path;
		this.faceFrame = faceFrame;
	}

	/**
	 * Captures 2 images every second and writes it as a face image to the set
	 * directory. Displays the face images through the image frame
	 */
	public void run() {

		try {
			while (count < maxImage) {
				Thread.sleep(500);
				BufferedImage img = webcam.getImage();
				DetectedFace face = FaceDetection.detectFace(img);
				TheDetectedFace theFace = new TheDetectedFace(img, face);
				if (theFace.isValid()) {
					String fileName = path + "\\" + Integer.toString(count) + ".jpg";
					File outputfile = new File(fileName);
					ImageIO.write(theFace.getFaceImage(), "jpg", outputfile);
					faceFrame.changeImage(theFace.getFaceImage());
					count++;
				}
			}
		} catch (InterruptedException e) {

		} catch (IOException ioe) {

		}
	}

}
