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

public class LiveRecognition implements Runnable {
	
	Webcam webcam;
	String path;
	ImageFrame faceFrame;
	Text textPerson = null;
	Text textScore = null;
	int count = 0;
	Classifier classifier = null;
	final int maxImage = 100;
	public LiveRecognition(Webcam webcam, String path, ImageFrame faceFrame, Classifier classifier, Text textPerson, Text textScore)
	{
		this.webcam = webcam;	
		this.path = path;
		this.faceFrame = faceFrame;
		this.textPerson = textPerson;
		this.textScore = textScore;
		this.classifier = classifier;
	}
    public void run() {
        
        try {
        	while ( count < maxImage) {
	        	Thread.sleep(200);
	        	BufferedImage img = webcam.getImage();
	        	DetectedFace face = FaceDetection.DetectFace(img);
				TheDetectedFace theFace = new TheDetectedFace(img,face);
	            if ( theFace.isValid() && classifier != null) {
	            	String fileName = path + Integer.toString(count) + ".jpg";
	            	File outputfile = new File(fileName);
	            	ImageIO.write(theFace.getFaceImage(), "jpg", outputfile);
	            	
	            	
	            	Sample s = new Sample(fileName,true);
	            	
	    			PredictResult res = classifier.Predict(s);
	    			String person = res.getLabel();
	    			double confidence = res.getConfidence();
	    			if (confidence > Classifier.THRESHOLD)
	    				faceFrame.changeImageName(theFace.getFaceImage(), person + " : "    + String.format("%.2f", confidence*100) + "%");
	    			else
	    				faceFrame.changeImageName(theFace.getFaceImage(), "Unknow" + " : "  + String.format("%.2f", confidence*100) + "%");
	    			//textScore.setText(Double.toString(confidence));
	    			//textPerson.setText(person);;
	    			
	            	count ++;
	            }
	            else
	            {
	            	faceFrame.changeTitle("Face not detected!");
	            }
        	}
        } catch (InterruptedException e) {
           
        } catch (IOException ioe) {
        	
        }
        
        faceFrame.changeTitle("Done!!");
    }

}

