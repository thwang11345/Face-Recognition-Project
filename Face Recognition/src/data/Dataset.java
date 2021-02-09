package data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import algorithms.Classifier;
import algorithms.LDA;
import algorithms.LDAMap;
import algorithms.Matrix;
import algorithms.PCAMap;
import algorithms.PredictResult;

/**
 * Data set for a face data. Contains samples, the people for those sample. 
 * 
 * @author Thomas Hwang
 *
 */
public class Dataset {

	ArrayList<Sample> allSamples = new ArrayList<Sample>();

	Hashtable<String, ArrayList<Sample>> table = new Hashtable<String, ArrayList<Sample>>();
	ArrayList<String> persons = new ArrayList<String>();
	Classifier classifier = null;
	/**
	 * Constructs a data set with all samples for each person in the given folder.
	 * @param folder the folder to retrieve the people and their samples
	 * @param faceOnly true if this folder contains images with only face data, false otherwise
	 */
	public Dataset(File folder, boolean faceOnly) {

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				for (final File fileEntry1 : fileEntry.listFiles()) {
					String path = fileEntry1.getAbsolutePath();
					allSamples.add(new Sample(path, faceOnly));
				}
			}
		}

		for (Sample s : allSamples) {
			ArrayList<Sample> sps = null;

			if (table.get(s.getPersonName()) == null) {
				persons.add(s.getPersonName());
				sps = new ArrayList<Sample>();
				sps.add(s);
				table.put(s.getPersonName(), sps);
			} else {
				sps = (ArrayList<Sample>) table.get(s.getPersonName());
				sps.add(s);
			}

		}
	}
	/**
	 * Returns all people in the data set.
	 * @return a list of all people in the data set
	 */
	public ArrayList<String> getPersons() {
		return persons;
	}
	/**
	 * Returns all samples for a person.
	 * @param person the person to retrieve samples for
	 * @return a list of samples for a person
	 */
	public ArrayList<Sample> getSampleFromPerson(String person) {
		ArrayList<Sample> sps = null;
		if (table.get(person) != null) {
			sps = (ArrayList<Sample>) table.get(person);
		}
		return sps;
	}
	/**
	 * Returns number of samples in the data set.
	 * @return the number of samples in the data set
	 */
	public int sampleSize() {
		if (allSamples == null)
			return 0;
		return allSamples.size();
	}
	/**
	 * Returns the data of all the samples. The data from all the samples is appended. 
	 * @return the data from all the samples
	 */
	public double[] getData() {
		double[] retval = null;
		if (allSamples == null)
			return retval;

		retval = new double[sampleSize() * Sample.IMAGE_SIZE];
		int i = 0;
		for (Sample s : allSamples) {
			double[] data = s.getData();
			for (int j = 0; j < Sample.IMAGE_SIZE; j++, i++)
				retval[i] = data[j];

		}

		return retval;
	}
	/**
	 * Returns the id's of all the samples.
	 * @return the id's of all the samples.
	 */
	public int[] getIDs() {
		int size = sampleSize();
		int[] retval = new int[size];
		for (int i = 0; i < size; i++)
			retval[i] = allSamples.get(i).getID();
		return retval;
	}
	/**
	 * Returns the classifier. 
	 * @return the classifier
	 */
	public Classifier getClassifier() {
		return classifier;
	}
	/**
	 * Sets the classifier.
	 * @param other the classifier to be set
	 */
	public void setClassifier(Classifier other) {
		classifier = other;
	}

	// train using the sample
	public void Train() {
		double[] data = getData();
		int[] labels = getIDs();
		int xdim = Sample.IMAGE_SIZE;
		int noData = sampleSize();

		PCAMap pcaMap = LDA.PCA(data, sampleSize(), xdim);

		int m = pcaMap.getM();
		double[] Yt = pcaMap.getYt();
		double[] Het = pcaMap.getHt();
		LDAMap ldaMap = LDA.Learn(Yt, labels, m);

		int K = ldaMap.getNoClasses();
		int C = K - 1;
		double[] Hdt = ldaMap.getMatrix();

		// Ht (xdim, C) = Het *Hdt
		double[] Ht = Matrix.d1Mult(Het, xdim, m, Hdt, m, C);

		// Z (noData * C)
		double[] Z = Matrix.d1Mult(Yt, noData, m, Hdt, m, C);

		double[][] features = Matrix.d1ToD2(Z, noData, C);
		classifier = new Classifier(pcaMap.getMean(), Ht, C, features, noData);

		String[] allP = new String[sampleSize()];
		for (int i = 0; i < sampleSize(); i++) {
			allP[i] = allSamples.get(i).getPersonName();
		}
		classifier.setLabels(allP);
	}

	// Given a sample, find the NN
	public PredictResult Predict(Sample other) {
		PredictResult res = classifier.PredictNN(other.getData());
		if (res.getIndex() != -1) {
			Sample s = allSamples.get(res.getIndex());
			if (s != null) {
				res.setSample(s);
				res.setLabel(s.getPersonName());
			}
		}

		return res;

	}

	public PredictResult Predict(BufferedImage img, int startX, int startY, int endX, int endY) {
		double[] data = Sample.getData(img, startX, startY, endX, endY);
		PredictResult res = classifier.PredictNN(data);
		if (res.getIndex() != -1) {
			Sample s = allSamples.get(res.getIndex());
			if (s != null) {
				res.setSample(s);
				res.setLabel(s.getPersonName());
			}
		}

		return res;
	}
}
