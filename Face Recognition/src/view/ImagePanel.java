package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
/**
 * Not currently used.
 * @author Thomas Hwang
 *
 */
public class ImagePanel extends JPanel {

	private Image img;
	final static long serialVersionUID = 2211113;

	public ImagePanel(String img) {

		this(new ImageIcon(img).getImage());

	}

	public ImagePanel(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void setImage(Image img) {
		this.img = img;
		this.revalidate();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(this.img, 0, 0, null);
	}

}