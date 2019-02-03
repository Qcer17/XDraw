import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GreyLine extends JPanel{
	int x1,y1,x2,y2;
	
	public GreyLine() {
		setBackground(null);
		setOpaque(false);
		setBounds(0,0,1100,790);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(x1, y1+12, x2, y2);
	}
	
	public void setLocations(int x1,int y1,int x2,int y2) {
		this.x1=x1;
		this.x2=x2;
		this.y1=y1;
		this.y2=y2;
	}
}
