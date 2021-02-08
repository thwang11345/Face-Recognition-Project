package webcam;

import java.awt.image.BufferedImage;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

public class WebCamCapture { // extends JFrame {
	
	Webcam webcam;
	BufferedImage  img;
	
	public WebCamCapture()
	{
		webcam = Webcam.getDefault();
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
        }
	  
       webcam.setViewSize(WebcamResolution.VGA.getSize());

       webcam.open(true);

       img = webcam.getImage();

	}
	
	public BufferedImage getImage()
	{
		img = webcam.getImage();
		return img;
	}
	
	public Webcam getWebCam()
	{
		return webcam;
	}
	
	public void Close()
	{
		webcam.close();
	}

}
