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

public class Sample {
    public static final int IMAGE_X = 128;
    public static final int IMAGE_Y = 128;
    public static final int IMAGE_SIZE = IMAGE_X * IMAGE_Y;
        
    static Integer Count = 0;
    String fileName = null;
    String personName = null;
    String imageName = null;
    int    id = -1;
    boolean isTraining = false;
    static Hashtable<String, Integer> table = new Hashtable<String,Integer>(); 
    double [] data = null;
    BufferedImage faceImage = null;
    double faceConf = 0;
    boolean faceOnly = false;
    
    public Sample(String fName, boolean faceOnly) {
    	this.faceOnly = faceOnly;
        ParseFileName(fName);
        if ( id != -1) {
            ReadImage();
        }
        
    }
    
    public static double [] getData(BufferedImage img, int startX, int startY, int endX, int endY)
    {
    	BufferedImage outputImage = ImageTransfer.scale(img, startX, startY, endX, endY, IMAGE_X,IMAGE_Y);
        
        double [] x = ImageTransfer.GetImageData(outputImage);
        double [] retval  = LDA.zeroMeanNorm(x);
    	return retval;
    }
    
    public static double [] getData(BufferedImage img)
    {
    	BufferedImage outputImage = ImageTransfer.scale(img, IMAGE_X,IMAGE_Y);
        
        double [] x = ImageTransfer.GetImageData(outputImage);
        double [] retval  = LDA.zeroMeanNorm(x);
    	return retval;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public int getID() {
        return id;
    }
    
    public String getImageName() {
    	return imageName;
    }
    
    public double [] getData() {
    	return data;
    }
    
    public BufferedImage getFaceImage() {
    	return faceImage;
    }
    
    public double getFaceConf() {
    	return faceConf;
    }
    
    private void ParseFileName(String fName)  {
    	if ( faceOnly ) {
    		ParseFileNameFaceOly(fName);
    	} else {
    		ParseFileNameRegular(fName);
    	}
   	
    }
    
    private void ParseFileNameFaceOly(String fName) {
        if (fName == null)
            return;
        int idx = fName.toLowerCase().indexOf("facetrain");
        if ( idx >= 0 ) // trainig
        {
            isTraining = true;
            String substr = fName.substring(idx+10); // "faceTrain/"
            idx = substr.indexOf("\\");
            int idxP = substr.indexOf("_");
            if ( idx > 0 ) {
                personName = substr.substring(0,idxP); 
                imageName = substr.substring(idx+1);
            }
        }
        else {
            idx = fName.toLowerCase().indexOf("facetest");
            if ( idx >= 0 ) // test
            {
                isTraining = false;
                String substr = fName.substring(idx+9); // "faceTest/"
                idx = substr.indexOf("\\");
                int idxP = substr.indexOf("_");
                if ( idx > 0 ) {
                    personName = substr.substring(0,idxP); 
                    imageName = substr.substring(idx+1);
                }
            }           
        }
        
        if ( personName != null) {
            if ( table.get(personName) == null ) // new person
            {
                table.put(personName, ++Count);
            }         
            id = (Integer) table.get(personName);
            fileName = fName;
        }       
    }
    
    private void ParseFileNameRegular(String fName) {
        if (fName == null)
            return;
        int idx = fName.toLowerCase().indexOf("train");
        if ( idx >= 0 ) // trainig
        {
            isTraining = true;
            String substr = fName.substring(idx+6); // "train/"
            idx = substr.indexOf("\\");
            if ( idx > 0 ) {
                personName = substr.substring(0,idx); 
                imageName = substr.substring(idx+1);
            }
        }
        else {
            idx = fName.toLowerCase().indexOf("test");
            if ( idx >= 0 ) // test
            {
                isTraining = false;
                String substr = fName.substring(idx+5); // "test/"
                idx = substr.indexOf("\\");
                if ( idx > 0 ) {
                    personName = substr.substring(0,idx); 
                    imageName = substr.substring(idx+1);
                }
            }           
        }
        
        if ( personName != null) {
            if ( table.get(personName) == null ) // new person
            {
                table.put(personName, ++Count);
            }         
            id = (Integer) table.get(personName);
            fileName = fName;
        }       
    }
    
    
    
    
    private void ReadImage() {
        BufferedImage img = null;
        try {
        	if ( faceOnly)
        	{
        		img = ImageIO.read(new File(fileName));	 
        		faceImage = img;
        	
        	} else {
	            img = ImageIO.read(new File(fileName));	             
	            // perform face detection here
	            DetectedFace face = FaceDetection.DetectFace(img);
	            if ( face != null ) {
	            	Rectangle rect = face.getBounds();
	            	faceImage = img.getSubimage((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
	            	faceConf = face.getConfidence();
	            }
        	}    
        	
        	data = getData(img);
            
            
            // test
            /*
            String outputImagePath = "C:\\Users\\Wey\\Documents\\Images\\t1.jpg";
            // extracts extension of output file
            String formatName = outputImagePath.substring(outputImagePath
                            .lastIndexOf(".") + 1);
             
            // writes to output file
            ImageIO.write(outputImage, formatName, new File(outputImagePath));
*/
            
            
            
        } catch (IOException e) {
        }

    }
}
