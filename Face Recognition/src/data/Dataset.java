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


public class Dataset {

    ArrayList<Sample> AllSamples = new ArrayList<Sample> ();
    
    Hashtable<String, ArrayList<Sample>> table = new Hashtable<String, ArrayList<Sample>>(); 
    ArrayList<String> Persons = new ArrayList<String>  ();
    Classifier classifier = null;
     
    public Dataset( File folder, boolean faceOnly) {

        for (final File fileEntry : folder.listFiles()) { 
            if (fileEntry.isDirectory() )
            {
                for (final File fileEntry1 : fileEntry.listFiles()) { 
                    String path = fileEntry1.getAbsolutePath();
                    AllSamples.add( new Sample(path, faceOnly) );
                }
            }
        }
        
        for(Sample s:AllSamples)
        {
        	ArrayList<Sample> sps = null;
      	
        	if ( table.get(s.getPersonName()) == null )
        	{    		
        		Persons.add(s.getPersonName());
        		sps = new ArrayList<Sample>();
        		sps.add(s);
        		table.put(s.getPersonName(), sps);
        	}
        	else
        	{
        		sps =(ArrayList<Sample>) table.get(s.getPersonName());
        		sps.add(s);
        	}
        	
        	
        }
    }
    
    public ArrayList<String> getPersons()
    {
    	return Persons;
    }
    
    public ArrayList<Sample> getSampleFromPerson(String person)
    {
    	ArrayList<Sample> sps = null;
    	if ( table.get(person) != null )
    	{
    		sps =(ArrayList<Sample>) table.get(person);  	
    	}
    	return sps;
    }
    
    public int sampleSize()
    {
    	if (AllSamples == null )
    		return 0;
    	return AllSamples.size();
    }
    
 
    public double [] getData()
    {
    	double [] retval = null;
    	if (AllSamples == null )
    		return retval;
    	
    	retval = new double[sampleSize()*Sample.IMAGE_SIZE];
    	int i = 0;
    	for(Sample s: AllSamples)
    	{
    		double [] data = s.getData();
    		for(int j=0;j<Sample.IMAGE_SIZE;j++,i++)
    			retval[i] = data[j];
    			
    	}
    	
    	return retval;
    }
    
    public int [] getIDs() {
        int size = sampleSize();
        int [] retval = new int[size];
        for(int i = 0; i<size; i++)
            retval[i] = AllSamples.get(i).getID();
        return retval;
    }
    
    public Classifier getClassifier() {
    	return classifier;
    }
    
    public void setClassifier(Classifier other)
    {
    	classifier = other;
    }
    
    
    // train using the sample
    public void Train()
    {
    	double [] data = getData();
        int [] labels = getIDs();
        int xdim = Sample.IMAGE_SIZE;
        int noData = sampleSize();
    	
        PCAMap pcaMap = LDA.PCA(data,sampleSize(),xdim);
        
        int m = pcaMap.getM();
        double [] Yt = pcaMap.getYt();
        double [] Het = pcaMap.getHt();
        LDAMap ldaMap = LDA.Learn(Yt, labels, m);
        
        int K = ldaMap.getNoClasses();
        int C  = K-1;
        double [] Hdt = ldaMap.getMatrix();
        
        // Ht (xdim, C) = Het *Hdt
        double [] Ht = Matrix.d1Mult(Het, xdim, m , Hdt, m, C );
        
        // Z (noData * C)
        double [] Z = Matrix.d1Mult(Yt, noData, m , Hdt, m, C );
        
        double [][] features = Matrix.d1ToD2(Z, noData,C);
        classifier = new Classifier(pcaMap.getMean(), Ht, C, features, noData);
        
        String [] allP = new String[sampleSize()];
        for(int i = 0; i<sampleSize(); i++){
        	allP[i] = AllSamples.get(i).getPersonName();
        }
        classifier.setLabels(allP);
    }
    
    // Given a sample, find the NN
    public PredictResult Predict(Sample other)
    {
    	PredictResult res = classifier.PredictNN(other.getData());
    	if ( res.getIndex() != -1 )
    	{
    		Sample s = AllSamples.get( res.getIndex());
    		if ( s!= null) {
    			res.setSample(s);
    			res.setLabel(s.getPersonName());
    		}
    	}
    	
    	return res;
    	
    }
    
    public PredictResult Predict(BufferedImage img, int startX, int startY, int endX, int endY )
    {
    	double [] data = Sample.getData(img, startX, startY, endX, endY);
    	PredictResult res = classifier.PredictNN(data);
    	if ( res.getIndex() != -1 )
    	{
    		Sample s = AllSamples.get( res.getIndex());
    		if ( s!= null) {
    			res.setSample(s);
    			res.setLabel(s.getPersonName());
    		}
    	}
    	
    	return res;
    }
}
