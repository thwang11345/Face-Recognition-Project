package view;



//import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class ImageFrame extends JFrame{

	ImageComponent component;
	int padding = 30;
	final String ti;
	final static long serialVersionUID = 2211112;
    public ImageFrame(String title){
    	ti= new String() + title + " ";
        setTitle(title);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        component = new ImageComponent();
        //setSize(component.getWidth()+100, component.getHeight()+100);
        add(component);

    }
    
    public ImageFrame(String title, int width, int height, int padding){
    	ti= new String() + title + " ";
    	this.padding = padding;
        setTitle(title);
        setSize(width, height);

        component = new ImageComponent(width, height,padding);
        //setSize(component.getWidth()+100, component.getHeight()+100);
        add(component);

    }
    
    public void ChangeFileName(String fileName)
    {
    	component.ChangeFileName(fileName);
    }
    
    public void changeImage(Image image1)
    {
    	component.changeImage(image1);
    }
    
    public void changeImageName(Image image1, String name)
    {
    	changeTitle(name);
    	component.changeImage(image1);
    }
    
    public void changeTitle(String name)
    {
    	String s = name; // ti + "-> " + name;
    	setTitle(s);   	
    }

    public static final int DEFAULT_WIDTH = 380;
    public static final int DEFAULT_HEIGHT = 300;
}


class ImageComponent extends JComponent{
    /**
     * 
     */
	 public static final int DEFAULT_WIDTH = 320;
	 public static final int DEFAULT_HEIGHT = 240;
	 public static final int DEFAULT_PADDING = 30;
	    
    private static final long serialVersionUID = 1L;
    private Image image;
    private int width;
    private int height;
    private int padding;
    public ImageComponent(){
    	width = DEFAULT_WIDTH;
    	height = DEFAULT_HEIGHT;
    	padding = DEFAULT_PADDING;
    	this.setSize(width, height-padding);
    	
    }
    
    public ImageComponent(int width, int height, int padding){
        this.width = width;
        this.height = height;
        this.padding = padding;
        this.setSize(width, height-padding);
    }
    
    public void ChangeFileName(String fileName){
        try{
            File image2 = new File(fileName);
            Image image1 = ImageIO.read(image2);
            changeImage(image1);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public void changeImage(Image image1)
    {
    	image = image1.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        this.setSize(image.getWidth(this), image.getHeight(this)-padding);
    }
    
    public void paintComponent (Graphics g){
        if(image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        g.drawImage(image, 0, 0, this);

        for (int i = 0; i*imageWidth <= getWidth(); i++)
            for(int j = 0; j*imageHeight <= getHeight();j++)
                if(i+j>0) g.copyArea(0, 0, imageWidth, imageHeight, i*imageWidth, j*imageHeight);
    }

}
