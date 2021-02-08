package algorithms;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import data.Sample;

public class Classifier {
	
	static public double THRESHOLD = 0.95;
	
	double [] mean;
	int dimX;                // dimension of original features
	double [] projection;
	int dimF;                // dimension of new projected features
	double [][] features = null;
	int noTrain;
	String [] labels = null;
	
	public Classifier(double [] m, double [] proj, int c, double [][] feats, int noTr) {
		mean = m;
		dimX = mean.length;
		projection = proj;
		dimF = c;
		features = feats;
		noTrain = noTr;
	}
	
	public Classifier(String fileName) {
		read(fileName);
	}
	
	// given the x in the original feature space, find the index of the nearest neighbor in the projected featreu space
	public PredictResult PredictNN(double [] x) {
		int retval = -1;
		double [] y = Project(x);
		
		double min = Double.MAX_VALUE;
		
		for(int i=0; i<noTrain; i++)
		{
			double dist = 0;
			for(int j = 0; j<dimF; j++)
			{
				double diff = y[j] - features[i][j];
				dist += diff*diff;
			}
			dist = Math.sqrt(dist);
			if ( dist < min )
			{
				min = dist;
				retval = i;
			}
		}
		
		min = Math.exp(-min);
		
		PredictResult res = new PredictResult(retval, min);
		
		return res;
	}
	
	
	// Given x in the original feature space, project to the new feature space
	public double [] Project(double [] x)
	{
		double [] a = new double[dimX];
		for(int i = 0; i<dimX; i++)
			a[i] = x[i] - mean[i];
		double [] retval = Matrix.d1Mult(a, 1, dimX, projection, dimX, dimF);
		return retval;
	}
	
	public double [] getMean() {
		return mean;
	}
	
	public int getDimX() {
		return dimX;
	}
	
	public double [] getProj() {
		return projection;
	}
	
	public int getDimF() {
		return dimF;
	}
	
	public void setLabels(String [] lbs){
		labels = lbs;
	}
	
	public String [] getLabels() {
		return labels;
	}
	
    // Given a sample, find the NN
    public PredictResult Predict(Sample other)
    {
    	PredictResult res = PredictNN(other.getData());
    	if ( res.getIndex() != -1 )
    	{
    		res.setLabel(labels[res.getIndex()]);
    	}
    	
    	return res;
    	
    }
    
    public PredictResult Predict(BufferedImage img, int startX, int startY, int endX, int endY )
    {
    	double [] data = Sample.getData(img, startX, startY, endX, endY);
    	PredictResult res = PredictNN(data);
    	if ( res.getIndex() != -1 )
    	{
    		res.setLabel(labels[res.getIndex()]);
    	}
    	
    	return res;    	
    }
	
	public void write(String fileName) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
			// write out 
			// 1. noTrain
			// 2. dimX
			// 3. dimF
			// 4. mean  (dimx)
			// 5. projection (dimx*dimF)
			// 6. features (noTrain * dimF)
			// 7. labels
			
			out.writeInt(noTrain);
			out.writeInt(dimX);
			out.writeInt(dimF);
			for(int i =0; i<dimX; i++)
				out.writeDouble(mean[i]);
			int size = dimX*dimF;
			for(int i =0; i<size; i++)
				out.writeDouble(projection[i]);
			for(int i = 0; i<noTrain; i++)
			{
				for(int j = 0; j<dimF; j++)
					out.writeDouble(features[i][j]);
			}
			
			for(int i = 0; i<noTrain; i++)
			{
				writeString(out, labels[i]);
			}
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void read(String fileName) {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(fileName));
			// read in
			// 1. noTrain
			// 2. dimX
			// 3. dimF
			// 4. mean  (dimx)
			// 5. projection (dimx*dimF)
			// 6. features (noTrain * dimF)
			// 7. labels
			
			noTrain = in.readInt();
			dimX    = in.readInt();
			dimF    = in.readInt();
			
			mean = new double[dimX];
			for(int i =0; i<dimX; i++)
				mean[i] = in.readDouble();
			
			int size = dimX*dimF;
			projection = new double[size];
			for(int i =0; i<size; i++)
				projection[i] = in.readDouble();
			
			features = new double[noTrain][dimF];
			for(int i = 0; i<noTrain; i++)
			{
				for(int j = 0; j<dimF; j++)
					features[i][j] = in.readDouble();
			}
			
			labels = new String[noTrain];
			for(int i = 0; i<noTrain; i++)
			{
				labels[i] = readString(in);
			}
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeString(DataOutputStream out, String s)
	{
		int len = s.length();
		try {
			out.writeInt(len);
			out.writeBytes(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private String readString(DataInputStream in)
	{
		String retval = null;
		
		try {
			int len = in.readInt();
			byte[] array = new byte[len];
			in.read(array);
			retval = new String(array);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retval;
	}

}
