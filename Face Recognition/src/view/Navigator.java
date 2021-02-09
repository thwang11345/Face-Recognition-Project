package view;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import data.Sample;
/**
 * Used to navigate the train and test images. Allows user to choose which person's list of samples they want and navigate through the list.
 * @author Thomas Hwang
 *
 */
public class Navigator {
	ArrayList<Sample> samples = null;
	Hashtable<String, Integer> table = new Hashtable<String, Integer>();
	ArrayList<String> fnames = null;
	int currentIndex = 0;
	Button next;
	Button prev;
	Combo combo;
	ImageFrame frame;
	ImageFrame faceFrame;
	Text faceDetScore;
	/**
	 * Sets the samples in the train or test, the buttons, combination box, and face and image frames.
	 * @param sps the samples in the train or test
	 * @param n next button
	 * @param p previous button
	 * @param c combination box
	 * @param f image frame
	 * @param f1 face Frame 
	 * @param faceDTScore the face detection score
	 */
	public Navigator(ArrayList<Sample> sps, Button n, Button p, Combo c, ImageFrame f, ImageFrame f1,
			Text faceDTScore) {
		samples = sps;
		next = n;
		prev = p;
		combo = c;
		frame = f;
		faceFrame = f1;
		faceDetScore = faceDTScore;
		Init();
	}
	/**
	 * Sets the name so program knows whose which list of samples to look at.
	 * @param name the name of the person for their samples
	 */
	public void SetName(String name) {
		currentIndex = (Integer) table.get(name);
		display();
	}
	/**
	 * Goes to next sample
	 */
	public void Next() {
		currentIndex++;
		currentIndex %= samples.size();
		display();
	}
	/**
	 * Goes to previous sample
	 */
	public void Previous() {

		currentIndex--;
		currentIndex += samples.size(); // prevent negative
		currentIndex %= samples.size();
		display();
	}
	/**
	 * Returns current sample
	 */
	public Sample getCurrentSample() {
		return samples.get(currentIndex);
	}
	/**
	 * Displays the current sample.
	 */
	private void display() {
		Sample s = samples.get(currentIndex);
		frame.ChangeFileName(s.getFileName());
		if (s.getFaceImage() != null)
			faceFrame.changeImage(s.getFaceImage());
		faceDetScore.setText(Double.toString(s.getFaceConf()));

		combo.select(currentIndex);
	}
	/**
	 * Adds all samples for the combinatoin box.
	 */
	private void Init() {
		combo.removeAll();
		int i = 0;
		for (Sample s : samples) {
			String name = s.getImageName();
			combo.add(name);
			table.put(name, (Integer) i);
			i++;
		}
		next.setEnabled(true);
		prev.setEnabled(true);
		combo.setEnabled(true);
		display();
	}
}
