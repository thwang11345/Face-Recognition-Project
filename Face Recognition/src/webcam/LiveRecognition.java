package webcam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.swt.widgets.Text;
import org.openimaj.image.processing.face.detection.DetectedFace;

import com.github.sarxos.webcam.Webcam;

import algorithms.Classifier;
import algorithms.PredictResult;
import data.Sample;
import process.FaceDetection;
import process.TheDetectedFace;
import view.ImageFrame;

/**
 * Allows for program to recognize face images in real time. Writes recorded
 * face images to test directory and updates frame with face images.
 * 
 * @author Thomas Hwang
 *
 */
public class LiveRecognition implements Runnable {

	Webcam webcam;
	String path;
	ImageFrame faceFrame;
	Text textPerson = null;
	Text textScore = null;
	int count = 0;
	Classifier classifier = null;
	final int maxImage = 100;

	/**
	 * Sets the webcam being used, the directory to save the test images, the
	 * faceframe to display the fame images, and the classifier used to predict.
	 * 
	 * @param webcam     the web cam used
	 * @param path       the directory where the test images will be saved
	 * @param faceFrame  the frame that displays the face image
	 * @param classifier the classifier used
	 * @param textPerson not currently used
	 * @param textScore  not currently used
	 */
	public LiveRecognition(Webcam webcam, String path, ImageFrame faceFrame, Classifier classifier, Text textPerson,
			Text textScore) {
		this.webcam = webcam;
		this.path = path;
		this.faceFrame = faceFrame;
		this.textPerson = textPerson;
		this.textScore = textScore;
		this.classifier = classifier;
	}

	/**
	 * Classifies faces in real time using PCA and LDA. Captures a face image every
	 * 200 mili seconds and the maximum images is 100. Displays whether not a face
	 * has been detected. Saves face data into test directory and displays fame
	 * image in face frame.
	 */
	public void run() {

		try {
			while (count < maxImage) {
				Thread.sleep(200);
				BufferedImage img = webcam.getImage();
				DetectedFace face = FaceDetection.detectFace(img);
				TheDetectedFace theFace = new TheDetectedFace(img, face);
				if (theFace.isValid() && classifier != null) {
					String fileName = path + Integer.toString(count) + ".jpg";
					File outputfile = new File(fileName);
					ImageIO.write(theFace.getFaceImage(), "jpg", outputfile);

					Sample s = new Sample(fileName, true);

					PredictResult res = classifier.Predict(s);
					String person = res.getLabel();
					double confidence = res.getConfidence();
					if (confidence > Classifier.THRESHOLD)
						faceFrame.changeImageName(theFace.getFaceImage(),
								person + " : " + String.format("%.2f", confidence * 100) + "%");
					else
						faceFrame.changeImageName(theFace.getFaceImage(),
								"Unknow" + " : " + String.format("%.2f", confidence * 100) + "%");
					// textScore.setText(Double.toString(confidence));
					// textPerson.setText(person);;

					count++;
				} else {
					faceFrame.changeTitle("Face not detected!");
				}
			}
		} catch (InterruptedException e) {

		} catch (IOException ioe) {

		}

		faceFrame.changeTitle("Done!!");
	}

}
