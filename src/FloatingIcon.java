import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class FloatingIcon extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	Image img;
	public FloatingIcon(String fileName) {
		icon=new ImageIcon(getClass().getResource("/"+fileName));
		img=icon.getImage();
		setVisible(false);
		setBackground(null);
		setOpaque(false);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0,this.getWidth(), this.getHeight(), this);
	}
}
