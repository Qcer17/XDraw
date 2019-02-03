import java.awt.Graphics;

public class Pixel {
	public static void setPixel(Graphics g, int x, int y) {
		g.drawLine(x, y, x, y);
		//g.fillRect(x, y, 1, 1);
		//g.drawOval(x, y, 1, 1);
	}
}
