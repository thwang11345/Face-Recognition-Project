package view;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import data.Sample;

public class Navigator {
	ArrayList<Sample> samples = null;
	Hashtable<String, Integer> table = new Hashtable<String,Integer>();
	ArrayList<String> fnames = null;
	int currentIndex = 0;
	Button next;
	Button prev;
	Combo  combo;
	ImageFrame frame;
	ImageFrame faceFrame;
	Text faceDetScore;
	public Navigator(ArrayList<Sample> sps, Button n, Button p, Combo c, ImageFrame f, ImageFrame f1, Text faceDTScore)
	{
		samples = sps;
		next = n;
		prev = p;
		combo = c;
		frame = f;
		faceFrame = f1;
		faceDetScore = faceDTScore;
		Init();
	}
	
	public void SetName(String name)
	{
		currentIndex = (Integer)table.get(name);
		display();
	}
	
	public void Next()
	{
		currentIndex++;
		currentIndex %= samples.size();
		display();
	}
	
	public void Previous()
	{
		
		currentIndex--;
		currentIndex += samples.size(); // prevent negative
		currentIndex %= samples.size();
		display();
	}
	
	public Sample getCurrentSample()
	{
		return samples.get(currentIndex);
	}
	
	private void display()
	{
		Sample s = samples.get(currentIndex);
		frame.ChangeFileName(s.getFileName());
		if ( s.getFaceImage() != null)
			faceFrame.changeImage( s.getFaceImage());
		faceDetScore.setText(Double.toString(s.getFaceConf()));
		
		combo.select(currentIndex);
	}
	
	private void Init()
	{
		combo.removeAll();
		int i = 0;
		for(Sample s : samples) {
			String name = s.getImageName();
			combo.add(name);
			table.put(name,(Integer) i);
			i++;
		}
		next.setEnabled(true);
		prev.setEnabled(true);
		combo.setEnabled(true);	
		display();
	}
}
