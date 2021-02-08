package algorithms;
import data.Sample;

public class PredictResult {

	int index;
	double confidence;
	Sample sample = null;
	String label = null;
	public PredictResult(int idx, double conf) {
		index = idx;
		confidence = conf;
	}

	public int getIndex()
	{
		return index;
	}
	
	public double getConfidence()
	{
		return confidence;
	}
	
	public void setSample(Sample s)
	{
		sample = s;
	}
	
	public void setLabel(String l)
	{
		label = l;
	}
	
	public String getLabel() {
		return label;
	}
	
	public Sample getSample()
	{
		return sample;
	}
}
