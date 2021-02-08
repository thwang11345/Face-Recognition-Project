package process;
import java.util.Iterator;
import java.util.List;


import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;


import java.awt.image.BufferedImage;


public class FaceDetection {
	
	static HaarCascadeDetector detector = new HaarCascadeDetector();

	public FaceDetection()
	{
		
	}
	
	static public DetectedFace DetectFace(BufferedImage  img)
	{
		DetectedFace bestFace = null;
		List < DetectedFace > faces = detector.detectFaces(ImageUtilities.createFImage(img));
		Iterator < DetectedFace > dfi = faces.iterator();
		if ( dfi == null )
			return bestFace;
		double max = -1;
	    while (dfi.hasNext()) {
	    	DetectedFace face = dfi.next();   
	    	if ( face.getConfidence() > max )
	    	{
	    		max = face.getConfidence() ;
	    		bestFace = face;
	    	}
		    
	   }
	   return bestFace;
		
	}

}
