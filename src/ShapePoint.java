
public class ShapePoint implements Comparable<Object>{
	public int x,y;
	public ShapePoint(int x,int y) {
		this.x=x;
		this.y=y;
	}
	public int compareTo(Object o) {
		ShapePoint shapePoint=(ShapePoint)o;
		if(shapePoint.x==x&&shapePoint.y==y)return 0;
		if(shapePoint.x<x||(shapePoint.x==x&&shapePoint.y<y))return 1;
		return -1;
	}
}
