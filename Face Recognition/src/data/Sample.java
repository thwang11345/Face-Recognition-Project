package data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.math.geometry.shape.Rectangle;

import algorithms.LDA;
import process.FaceDetection;
import process.ImageTransfer;

/**
 * Sample that contains person, image data for a training or test data set
 * 
 * @author Thomas Hwang
 *
 */
public class Sample {
	public static final int IMAGE_X = 128;
	public static final int IMAGE_Y = 128;
	public static final int IMAGE_SIZE = IMAGE_X * IMAGE_Y;

	static Integer Count = 0;
	String fileName = null;
	String personName = null;
	String imageName = null;
	int id = -1;
	boolean isTraining = false;
	static Hashtable<String, Integer> table = new Hashtable<String, Integer>();
	double[] data = null;
	BufferedImage faceImage = null;
	double faceConf = 0;
	boolean faceOnly = false;

	/**
	 * Constructs a sample from file.
	 * 
	 * @param fName
	 * @param faceOnly
	 */
	public Sample(String fName, boolean faceOnly) {
		this.faceOnly = faceOnly;
		parseFileName(fName);
		if (id != -1) {
			readImage();
		}
	}

	/**
	 * Returns the numerical representation of an image that is scaled 128 X 128,
	 * scaled using z-score normalization..
	 * 
	 * @param img    an image to read
	 * @param startX lower x bound
	 * @param startY lower y bound
	 * @param endX   upper x bound
	 * @param endY   upper y bound
	 * @return the numerical representation of an image that is scaled 128 X 128,
	 *         scaled using z-score normalization
	 */
	public static double[] getData(BufferedImage img, int startX, int startY, int endX, int endY) {
		BufferedImage outputImage = ImageTransfer.scale(img, startX, startY, endX, endY, IMAGE_X, IMAGE_Y);

		double[] x = ImageTransfer.getImageData(outputImage);
		double[] retval = LDA.zeroMeanNorm(x);
		return retval;
	}

	/**
	 * Returns the numerical representation of an image, scaled using z-score normalization.
	 * 
	 * @param img an image to read
	 * @return the numerical representation of an image, scaled using z-score normalization
	 */
	public static double[] getData(BufferedImage img) {
		BufferedImage outputImage = ImageTransfer.scale(img, IMAGE_X, IMAGE_Y);

		double[] x = ImageTransfer.getImageData(outputImage);
		double[] retval = LDA.zeroMeanNorm(x);
		return retval;
	}

	/**
	 * Returns the file name for this sample.
	 * 
	 * @return the file name for this sample
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns the person name of the sample.
	 * 
	 * @return the person name of the sample
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * Returns the id of the sample.
	 * 
	 * @return the id of the sample
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the name of the sample.
	 * 
	 * @return the name of the sample
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Returns the numerical data for this sample.
	 * 
	 * @return the numerical data for this sample
	 */
	public double[] getData() {
		return data;
	}

	/**
	 * Returns the image for this sample.
	 * 
	 * @return the image for this sample
	 */
	public BufferedImage getFaceImage() {
		return faceImage;
	}

	/**
	 * Returns the confidence score for this sample.
	 * 
	 * @return the confidence score for this sample
	 */
	public double getFaceConf() {
		return faceConf;
	}

	/**
	 * Parses file name
	 */
	private void parseFileName(String fName) {
		if (faceOnly) {
			parseFileNameFaceOnly(fName);
		} else {
			parseFileNameRegular(fName);
		}
	}

	/**
	 * Retrieves person and image name, and whether this sample is training/test
	 * from file path that contains an image with only the face.
	 */
	private void parseFileNameFaceOnly(String fName) {
		if (fName == null)
			return;
		int idx = fName.toLowerCase().indexOf("facetrain");
		// train
		if (idx >= 0) {
			isTraining = true;
			// "faceTrain/"
			String substr = fName.substring(idx + 10);
			idx = substr.indexOf("\\");
			int idxP = substr.indexOf("_");
			if (idx > 0) {
				personName = substr.substring(0, idxP);
				imageName = substr.substring(idx + 1);
			}
		} else {
			idx = fName.toLowerCase().indexOf("facetest");
			// test
			if (idx >= 0) {
				isTraining = false;
				// "faceTest/"
				String substr = fName.substring(idx + 9);
				idx = substr.indexOf("\\");
				int idxP = substr.indexOf("_");
				if (idx > 0) {
					personName = substr.substring(0, idxP);
					imageName = substr.substring(idx + 1);
				}
			}
		}

		if (personName != null) {
			if (table.get(personName) == null) // new person
			{
				table.put(personName, ++Count);
			}
			id = (Integer) table.get(personName);
			fileName = fName;
		}
	}

	/**
	 * Retrieves person and image name, and whether this sample is training/test
	 * from file path that contains a regular image.
	 */
	private void parseFileNameRegular(String fName) {
		if (fName == null)
			return;
		int idx = fName.toLowerCase().indexOf("train");
		// training
		if (idx >= 0) {
			isTraining = true;
			// "train/"
			String substr = fName.substring(idx + 6);
			idx = substr.indexOf("\\");
			if (idx > 0) {
				personName = substr.substring(0, idx);
				imageName = substr.substring(idx + 1);
			}
		} else {
			idx = fName.toLowerCase().indexOf("test");
			// test
			if (idx >= 0) {
				isTraining = false;
				// "test/"
				String substr = fName.substring(idx + 5);
				idx = substr.indexOf("\\");
				if (idx > 0) {
					personName = substr.substring(0, idx);
					imageName = substr.substring(idx + 1);
				}
			}
		}

		if (personName != null) {
			// new person
			if (table.get(personName) == null) {
				table.put(personName, ++Count);
			}
			id = (Integer) table.get(personName);
			fileName = fName;
		}
	}

	/**
	 * Reads in face image. If an image contains objects other than the face,
	 * resizes image to only the face.
	 */
	private void readImage() {
		BufferedImage img = null;
		try {
			if (faceOnly) {
				img = ImageIO.read(new File(fileName));
				faceImage = img;

			} else {
				img = ImageIO.read(new File(fileName));
				// perform face detection here
				DetectedFace face = FaceDetection.detectFace(img);
				if (face != null) {
					Rectangle rect = face.getBounds();
					faceImage = img.getSubimage((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
					faceConf = face.getConfidence();
				}
			}

			data = getData(img);
		} catch (IOException e) {
		}

	}
}
