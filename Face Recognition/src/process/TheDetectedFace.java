package process;


import java.awt.image.BufferedImage;

import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.math.geometry.shape.Rectangle;

// wrapping the DetectedFace
public class TheDetectedFace {
	
	public static double THRESHOLD = 10.0;
	public static int MIN_WIDTH = 50;
	public static int MAX_WIDTH = 300;
	public static int MIN_HEIGHT = 50;
	public static int MAX_HEIGHT = 300;
	
	double faceConf = 0;
	BufferedImage faceImage = null;
	BufferedImage image = null;
	final int extendX = 0;
	final int extendY = 10;
	
	int startX = 0;
	int startY = 0;
	int endX = 0;
	int endY = 0;
	int width=0;
	int height = 0;
	
	public TheDetectedFace(BufferedImage img, DetectedFace face)
	{
		if ( face != null && img != null ) {
			image = img;
        	Rectangle rect = face.getBounds();

        	width = (int) rect.width+extendX;
            height = (int)rect.height+extendY;
        	if ( width >= img.getWidth())
        		width = img.getWidth() -1;
        	if ( height >= img.getHeight())
        		height = img.getHeight() -1;
        	
        	startX = (int)rect.x;
        	startY = (int)rect.y;
        	endX  = startX + width-1;
        	endY  = startY + height-1;
        	
        	
        	faceImage = img.getSubimage(startX, startY, width, height);
            faceConf  = face.getConfidence();
        }
	}
	
	public double getFaceConf()
	{
		return faceConf;
	}
	
	public BufferedImage getFaceImage() {
		return faceImage;
	}
	
	public int getStartX() {
		return startX;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public int getEndX() {
		return endX;
	}
	
	public int getEndY() {
		return endY;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public boolean isValid()
	{
		if ( faceImage == null || faceConf < THRESHOLD)
			return false;
		
		if ( width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT)
			return false;
		return true;
	}

}
