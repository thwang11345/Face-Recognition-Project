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

public class WebCamLiveCapture implements Runnable {
	
	Webcam webcam;
	String path;
	ImageFrame faceFrame;
	int count = 0;
	final int maxImage = 100;
	public WebCamLiveCapture(Webcam webcam, String path, ImageFrame faceFrame)
	{
		this.webcam = webcam;	
		this.path = path;
		this.faceFrame = faceFrame;
	}
    public void run() {
        
        try {
        	while ( count < maxImage) {
	        	Thread.sleep(500);
	        	BufferedImage img = webcam.getImage();
	        	DetectedFace face = FaceDetection.DetectFace(img);
				TheDetectedFace theFace = new TheDetectedFace(img,face);
	            if ( theFace.isValid() ) {
	            	String fileName = path + "\\" + Integer.toString(count) + ".jpg";
	            	File outputfile = new File(fileName);
	            	ImageIO.write(theFace.getFaceImage(), "jpg", outputfile);
	            	faceFrame.changeImage(theFace.getFaceImage());
	            	count ++;
	            }
        	}
        } catch (InterruptedException e) {
           
        } catch (IOException ioe) {
        	
        }
    }

}
