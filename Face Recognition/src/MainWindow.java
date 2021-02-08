import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.openimaj.image.processing.face.detection.DetectedFace;

import algorithms.Classifier;
import algorithms.PredictResult;
import data.AllData;
import data.Sample;
import process.FaceDetection;
import process.TheDetectedFace;
import view.ImageFrame;
import view.Navigator;
import webcam.LiveRecognition;
import webcam.WebCamCapture;
import webcam.WebCamLive;
import webcam.WebCamLiveCapture;

public class MainWindow {

	// GUI variables
	protected Shell shell;
	
	protected Combo comboTrain;
	protected Combo comboTest;
	protected Button btnLoad;
	protected Button btnTrain;
	protected Button btnTrainNext;
	protected Button btnTrainPrev;
	protected Button btnTestNext;
	protected Button btnTestPrev;
	protected Combo comboTrainImage;
	protected Combo comboTestImage;
	
	protected ImageFrame frameTrain;
	protected ImageFrame frameTest;
	protected ImageFrame frameTrainFace;
	protected ImageFrame frameTestFace;
	protected ImageFrame frameWebCam;
	
	
	//String defaultPth = "C:\\Users\\weyhwang\\Documents\\Images\\";
	String defaultPth = "C:\\Users\\Wey\\Documents\\Images\\";
	String defaultClassifier = defaultPth + "PCALDA.dat";
	
	AllData allData = null;
	AllData allDataFace = null;
	Navigator navTrain = null;
	Navigator navTest = null;
	Classifier classifier = null;
	
	private Button btnPredict;
	private Text textScore;
	private Text textFaceDetTrainScore;
	private Text textFaceDetTestScore;
	
	
	WebCamCapture webCapture = null;
	private Button buttonCapture;
	private Button buttonRecord;
	private Button buttonStop;
	private Text textPersonName;
	private Label labelPersonName;
	
	Thread thread = null;
	private Button buttonTrainFace;
	private Button buttonRecogFace;
	private Button buttonLiveRec;
	private Button buttonSave;
	private Button buttonLoad;


	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();			       		
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		
		/*
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {
		    public void run() {
		    	createContents();
				shell.open();
				shell.layout();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
		    }
		} );
        */

	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		shell = new Shell();
		shell.setSize(613, 436);
		shell.setLocation(0, 0);
		shell.setText("Face Recognition System");

		/*
		shell.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                if((e.keyCode == 32))
                {
                    int debug = 1;
                }
            }
            public void keyPressed(KeyEvent e) {
                if((e.keyCode == 32))
                {
                    int debug = 1;
                }
            }
        });
*/
		
			
		btnLoad = new Button(shell, SWT.NONE);
		btnLoad.setBounds(28, 24, 83, 23);
		btnLoad.setText("Load");
		
		Label lblTraining = new Label(shell, SWT.NONE);
		lblTraining.setBounds(28, 113, 55, 15);
		lblTraining.setText("Training");
		
		comboTrain = new Combo(shell, SWT.NONE);
		comboTrain.setEnabled(false);
		comboTrain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String person = comboTrain.getText();		
				if ( allData != null)
				{
					ArrayList<Sample> sps = allData.getTrain().getSampleFromPerson(person);
					navTrain = new Navigator(sps, btnTrainNext, btnTrainPrev,comboTrainImage, frameTrain, frameTrainFace, textFaceDetTrainScore);
				}
				//MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WORKING);
                //messageBox.setText("My Face");
                //messageBox.setMessage( trainCombo.getText());
                //messageBox.open();
			}
		});
		comboTrain.setBounds(89, 110, 91, 23);
		
		btnTrainNext = new Button(shell, SWT.NONE);
		btnTrainNext.setEnabled(false);
		btnTrainNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				navTrain.Next();
			}
		});
		btnTrainNext.setBounds(304, 108, 75, 25);
		btnTrainNext.setText("Next");
		
		btnTrainPrev = new Button(shell, SWT.NONE);
		btnTrainPrev.setEnabled(false);
		btnTrainPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				navTrain.Previous();
			}
		});
		btnTrainPrev.setText("Previous");
		btnTrainPrev.setBounds(401, 108, 75, 25);
		
		Label lbtTesting = new Label(shell, SWT.NONE);
		lbtTesting.setText("Testing");
		lbtTesting.setBounds(28, 177, 55, 15);
		
	    comboTest = new Combo(shell, SWT.NONE);
		comboTest.setEnabled(false);
		comboTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String person = comboTest.getText();		
				if ( allData != null)
				{
					ArrayList<Sample> sps = allData.getTest().getSampleFromPerson(person);
					navTest = new Navigator(sps, btnTestNext, btnTestPrev,comboTestImage, frameTest, frameTestFace, textFaceDetTestScore);
				}
			}
		});
		comboTest.setBounds(89, 174, 91, 23);
		
		btnTestNext = new Button(shell, SWT.NONE);
		btnTestNext.setEnabled(false);
		btnTestNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				navTest.Next();
			}
		});
		btnTestNext.setText("Next");
		btnTestNext.setBounds(304, 172, 75, 25);
		
		btnTestPrev = new Button(shell, SWT.NONE);
		btnTestPrev.setEnabled(false);
		btnTestPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				navTest.Previous();
			}
		});
		btnTestPrev.setText("Previous");
		btnTestPrev.setBounds(401, 172, 75, 25);
		
		comboTrainImage = new Combo(shell, SWT.NONE);
		comboTrainImage.setEnabled(false);
		comboTrainImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String imageName = comboTrainImage.getText();
				if (navTrain != null )
					navTrain.SetName(imageName);
				
			}
		});
		comboTrainImage.setBounds(197, 110, 91, 23);
		
	    comboTestImage = new Combo(shell, SWT.NONE);
		comboTestImage.setEnabled(false);
		comboTestImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String imageName = comboTestImage.getText();
				if (navTest != null )
					navTest.SetName(imageName);
				
			}
		});
		comboTestImage.setBounds(197, 174, 91, 23);
		
		btnTrain = new Button(shell, SWT.NONE);
		btnTrain.setEnabled(false);
		btnTrain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				allData.getTrain().Train();				
				classifier = allData.getTrain().getClassifier();
				allData.getTest().setClassifier(classifier);
				btnPredict.setEnabled(true);
			}
		});
		btnTrain.setText("Train");
		btnTrain.setBounds(134, 24, 83, 23);
		
		btnPredict = new Button(shell, SWT.NONE);
		btnPredict.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Sample s = navTest.getCurrentSample();
				PredictResult res = classifier.Predict(s);
				String person = res.getLabel();
				double confidence = res.getConfidence();
				textScore.setText(Double.toString(confidence));
				comboTrain.setText(person);
			}
		});
		btnPredict.setText("Predict");
		btnPredict.setEnabled(false);
		btnPredict.setBounds(244, 24, 83, 23);
		
		Label lblScore = new Label(shell, SWT.NONE);
		lblScore.setBounds(373, 5, 64, 15);
		lblScore.setText("Score");
		
		textScore = new Text(shell, SWT.BORDER);
		textScore.setText("0");
		textScore.setEditable(false);
		textScore.setBounds(360, 25, 76, 21);
		
		textFaceDetTrainScore = new Text(shell, SWT.BORDER);
		textFaceDetTrainScore.setText("0");
		textFaceDetTrainScore.setEditable(false);
		textFaceDetTrainScore.setBounds(498, 110, 76, 21);
		
		textFaceDetTestScore = new Text(shell, SWT.BORDER);
		textFaceDetTestScore.setText("0");
		textFaceDetTestScore.setEditable(false);
		textFaceDetTestScore.setBounds(498, 172, 76, 21);
		
		buttonCapture = new Button(shell, SWT.NONE);
		buttonCapture.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				DetectFace();
			}
		});
		buttonCapture.setText("Capture Face");
		buttonCapture.setBounds(28, 247, 83, 23);
		
		buttonRecord = new Button(shell, SWT.NONE);
		buttonRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startRecord();
			}
		});
		buttonRecord.setText("Record");
		buttonRecord.setBounds(28, 287, 83, 23);
		
		buttonStop = new Button(shell, SWT.NONE);
		buttonStop.setEnabled(false);
		buttonStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stopRecord();
			}
	
		});
		buttonStop.setText("Stop");
		buttonStop.setBounds(138, 287, 83, 23);
		
		textPersonName = new Text(shell, SWT.BORDER);
		textPersonName.setBounds(317, 288, 151, 21);
		
		labelPersonName = new Label(shell, SWT.NONE);
		labelPersonName.setText("PersonName");
		labelPersonName.setBounds(239, 287, 84, 23);
		
		buttonTrainFace = new Button(shell, SWT.NONE);
		buttonTrainFace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TrainFace();
			}
		});
		buttonTrainFace.setText("Train Face");
		buttonTrainFace.setBounds(138, 247, 83, 23);
		
		buttonRecogFace = new Button(shell, SWT.NONE);
		buttonRecogFace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Recognize();
			}
		});
		buttonRecogFace.setEnabled(false);
		buttonRecogFace.setText("Recognize");
		buttonRecogFace.setBounds(28, 328, 83, 23);
		
		buttonLiveRec = new Button(shell, SWT.NONE);
		buttonLiveRec.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LiveRecognize();
			}
		});
		buttonLiveRec.setText("Live Recog");
		buttonLiveRec.setBounds(138, 328, 83, 23);
		
		buttonSave = new Button(shell, SWT.NONE);
		buttonSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ( classifier != null)
					classifier.write(defaultClassifier);
			}
		});
		buttonSave.setText("Save");
		buttonSave.setBounds(260, 247, 83, 23);
		
		buttonLoad = new Button(shell, SWT.NONE);
		buttonLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				classifier = new Classifier(defaultClassifier);
				buttonRecogFace.setEnabled(true);
			}
		});
		buttonLoad.setText("Load");
		buttonLoad.setBounds(373, 247, 83, 23);

	    frameTrain = new ImageFrame("Train Image");
	    frameTrain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frameTrain.setVisible(true);
	    frameTrain.setLocation(0, shell.getSize().y +20);
        
	    frameTest = new ImageFrame("Test Image");
	    frameTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frameTest.setVisible(true);
	    frameTest.setLocation(500, shell.getSize().y +20);
	    
	    frameTrainFace = new ImageFrame("Train face");
	    frameTrainFace.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frameTrainFace.setVisible(true);
	    frameTrainFace.setLocation(0, frameTrain.getLocation().y + frameTrain.getSize().height +20);
        
	    frameTestFace = new ImageFrame("Test Face");
	    frameTestFace.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frameTestFace.setVisible(true);
	    frameTestFace.setLocation(500, frameTest.getLocation().y + frameTest.getSize().height +20);
	    
	    frameWebCam = new ImageFrame("Detected Face", 300, 360, 30);
	    frameWebCam.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frameWebCam.setVisible(true);
	    frameWebCam.setLocation(shell.getLocation().x+shell.getSize().x+20 , shell.getLocation().y);
	    
	    webCapture = new WebCamCapture();
	    
	    SwingUtilities.invokeLater(new WebCamLive(webCapture.getWebCam()));
	    
		btnLoad.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes());
				chooser.setFileFilter(filter);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				File selectedFile = null;
				int returnVal = chooser.showOpenDialog(chooser);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				   System.out.println("You chose to open this file: " +
				        chooser.getSelectedFile().getName());
				   selectedFile = chooser.getSelectedFile();
				   
				   setSelected(selectedFile);
				}

			}
		});
		

	}

	private void setSelected(File selectedFile)
	{
	    allData = new AllData(selectedFile, false);
	    comboTrain.removeAll();
	    comboTest.removeAll();
		
		for(String s:allData.getTrain().getPersons())
			comboTrain.add(s);
		comboTrain.setEnabled(true);
		
		for(String s:allData.getTest().getPersons())
			comboTest.add(s);
		comboTest.setEnabled(true);
		
		btnTrain.setEnabled(true);
	}
	
	private void startRecord()
	{
		if ( textPersonName.getText().length() > 0) {
			String personName = textPersonName.getText();
			String path = defaultPth + "faceTrain";
			File dir = new File(path);
			if (!dir.exists()) dir.mkdir();
			int maxID = 0;
			for (final File fileEntry : dir.listFiles()) { 
	            if (fileEntry.isDirectory() )
	            {
	                 String subfolder = fileEntry.getName().toLowerCase();
	                 int idx = subfolder.indexOf(personName.toLowerCase());
	                 if ( idx >= 0 ) {
	                	 idx = subfolder.indexOf("_");
	                	 if ( idx >= 0 ) {
		                	 String sub = subfolder.substring(idx+1);
		                	 int id = Integer.parseInt(sub);
		                	 if ( id > maxID)
		                		 maxID = id;
	                	 }
	                 }
	
	            }
		    }
			maxID++;
			String newPath = path +"\\" + personName + "_" + Integer.toString(maxID);
			File newDir = new File(newPath);
			newDir.mkdir();
			WebCamLiveCapture webCamLiveCapture = new WebCamLiveCapture( webCapture.getWebCam(), newPath, frameWebCam);
			thread = new Thread(webCamLiveCapture);
			thread.start();
			buttonStop.setEnabled(true);
			buttonRecord.setEnabled(false);
			
		}	
	}
	
	private void stopRecord()
	{
		if (thread != null) {
			if ( thread.isAlive() ) {
				try {
					thread.interrupt();
					thread.join();
					Thread.sleep(1000);
					thread = null;
					buttonStop.setEnabled(false);
					buttonRecord.setEnabled(true);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}
	
	private void TrainFace()
	{
		File root = new File(defaultPth);
		allDataFace = new AllData(root, true);
		allDataFace.getTrain().Train();
		classifier = allDataFace.getTrain().getClassifier();
		buttonRecogFace.setEnabled(true);
	}
	
	private TheDetectedFace DetectFace()
	{
		BufferedImage img = webCapture.getImage();
		DetectedFace face = FaceDetection.DetectFace(img);
		TheDetectedFace theFace = new TheDetectedFace(img,face);
        if ( theFace.isValid() ) {
        		frameWebCam.changeImage(theFace.getFaceImage());
        }	
        return theFace;
	}
	
	private void LiveRecognize()
	{
		if ( classifier != null ) {
			String path = CheckTestPath();
			
			LiveRecognition liveRec = new LiveRecognition( webCapture.getWebCam(), path, frameWebCam,  classifier, textPersonName, textScore);
			thread = new Thread(liveRec);
			thread.start();
			buttonRecord.setEnabled(false);		
			buttonStop.setEnabled(true);
		}		
	}
	
	private String CheckTestPath()
	{
		String path = defaultPth + "faceTest";
		File dir = new File(path);
		if (!dir.exists()) dir.mkdir();
		path = path + "\\Unknown_0\\";
		dir = new File(path);
		if (!dir.exists()) dir.mkdir();
		
		return path;
	}
	
	private void Recognize()
	{
		String path = CheckTestPath();
		TheDetectedFace theFace = DetectFace();
        if ( theFace.isValid() && classifier != null)
        {
        	String fileName = path  +  "test.jpg";
        	File outputfile = new File(fileName);
        	try {
				ImageIO.write(theFace.getFaceImage(), "jpg", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	Sample s = new Sample(fileName,true);
        	
			PredictResult res = classifier.Predict(s);
			String person = res.getLabel();
			double confidence = res.getConfidence();
			textScore.setText(Double.toString(confidence));
			textPersonName.setText(person);;
        }
		
	}
}
