package algorithms;

import data.Sample;

/**
 * Contains the predicted result. Includes information about the label, and the confidence. Works with the classifier 
 * to obtain labels and confidence. Works with the DataSet by storing index information for where it is stored in the 
 * list of samples.
 * 
 * @author Thomas Hwang
 *
 */
public class PredictResult {

	int index;
	double confidence;
	Sample sample = null;
	String label = null;
	/**
	 * Sets the confidence and the index of the result. The index is used to track where the sample is in the list in DataSet.
	 * @param idx the index used to track where teh sample is in the list for the DataSet
	 * @param conf the confidence of the prediction
	 */
	public PredictResult(int idx, double conf) {
		index = idx;
		confidence = conf;
	}
	/**
	 * Returns the location of this 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setSample(Sample s) {
		sample = s;
	}

	public void setLabel(String l) {
		label = l;
	}

	public String getLabel() {
		return label;
	}

	public Sample getSample() {
		return sample;
	}
}
