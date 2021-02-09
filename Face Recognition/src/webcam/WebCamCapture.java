package webcam;

import java.awt.image.BufferedImage;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

/**
 * Wrapper for the web camera used for live recognition and live capture.
 * 
 * @author Thomas Hwang
 *
 */
public class WebCamCapture {

	Webcam webcam;
	BufferedImage img;

	/**
	 * Constructs a WebCamCapture that detects the usb webcam.
	 */
	public WebCamCapture() {
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

	/**
	 * Returns an image from the webcam.
	 * 
	 * @return an image
	 */
	public BufferedImage getImage() {
		img = webcam.getImage();
		return img;
	}

	/**
	 * Returns the webcam that is being used.
	 * 
	 * @return the webcam
	 */
	public Webcam getWebCam() {
		return webcam;
	}

	/**
	 * Closes the webcam.
	 */
	public void Close() {
		webcam.close();
	}

}
