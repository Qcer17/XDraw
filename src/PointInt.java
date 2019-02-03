import java.awt.Point;

public class PointInt {
	private int x,y;
	public PointInt(int x,int y) {
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void translate(int dx,int dy) {
		x+=dx;
		y+=dy;
	}
	public boolean equals(PointInt o) {
		return o.getX()==x&&o.getY()==y;
	}
	public void mult(double a) {
		x*=a;
		y*=a;
	}
	public void add(PointInt a) {
		x+=a.getX();
		y+=a.getY();
	}
}
