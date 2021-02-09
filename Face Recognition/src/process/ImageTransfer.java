package process;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
/**
 * Used to transform images (scaling and transforming to numerical data). Transformations and scaling are used to pre-process the images
 * for training. 
 * @author Thomas Hwang
 *
 */
public class ImageTransfer {
	/**
	 * Constructs an Image transformation. 
	 */
    public ImageTransfer() {
        
    }
    /**
     * Scales an image the desired width and height.
     * @param inputImage the image to be processed
     * @param dWidth the desired width
     * @param dHeight the desired height
     * @return an image scaled to dWidth and dHeight
     */
    public static BufferedImage scale(BufferedImage inputImage, int dWidth, int dHeight) {
        BufferedImage outputImage = null;
        if(inputImage != null) {
            outputImage = new BufferedImage(dWidth, dHeight, inputImage.getType());    
            Graphics2D g = outputImage.createGraphics();
            g.drawImage(inputImage, 0, 0, dWidth, dHeight, null);
            g.dispose();
        }
        return outputImage;
    }
    /**
     * Scales an image with boundaries startX, startY, endX, endY, to dWidth and dHeight.
     * @param inputImage the input to be processed
     * @param startX lower x bound
     * @param startY lower y bound
     * @param endX upper x bound
     * @param endY upper y bound
     * @param dWidth the desired width
     * @param dHeight the desired height
     * @return an image with boundaries startX, startY, endX, endY, scaled to dWidth and dHeight
     */
    public static BufferedImage scale(BufferedImage inputImage, int startX, int startY, int endX, int endY, int dWidth, int dHeight) {
        BufferedImage outputImage = null;
        if(inputImage != null) {
            outputImage = new BufferedImage(dWidth, dHeight, inputImage.getType());    
            Graphics2D g = outputImage.createGraphics();
            g.drawImage(inputImage, 0, 0, dWidth, dHeight, startX, startY, endX, endY, null);
            g.dispose();
        }
        return outputImage;
    }
    
    /**
     * Given a BufferedImage, return the double array of image data
     * Note that the image data is the average of RGB
     * @param image BufferedImage image
     * @return image data in double array
     */
    public static double [] getImageData(BufferedImage image) {
        int imWidth = image.getWidth();
        int imHeight = image.getHeight();
        int bands    = 3;  // RGB
        double[] imArr = new double[imWidth* imHeight];
        for (int y=0, nb=0 ; y < imHeight ; y++)
        {
            for (int x=0 ; x < imWidth ; x++, nb++) {
                double avg = 0;
                for(int b = 0; b<bands; b++) {
                    avg += image.getRaster().getSampleDouble(x, y, b) ;
                }
                
                imArr[nb] = avg/3.0;
            }
        }     
        return imArr;   
    }
}
