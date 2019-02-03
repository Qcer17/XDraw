import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.beans.Customizer;
import java.util.ArrayList;

import javax.management.IntrospectionException;

public class LineShape extends Shape {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PaintArea paintArea;
	int x1 = 100, y1 = 100, x2 = 200, y2 = 200;
	int cx1, cx2, cy1, cy2;
	int rotateX, rotateY, centerX, centerY;
	int[] xs = new int[2];
	int[] ys = new int[2];
	int[] types = new int[2];
	int selectedPoint = -1;
	FloatingIcon dot1, dot2, rotateArrow, rotateCenter;
	//Color color;
	GreyLine greyLine;

	private void drawLine(Graphics g) {
		pointsOnFrame.clear();
		drawLine(g, x1, y1, x2, y2, true, pointsOnFrame);
//		int dx = x2 - x1, dy = y2 - y1;
//		int ux = dx > 0 ? 1 : -1, uy = dy > 0 ? 1 : -1;
//		dx = Math.abs(dx);
//		dy = Math.abs(dy);
//		int x = x1, y = y1, eps = 0;
//		if (dx > dy) {
//			while(x!=x2) {
//				Pixel.setPixel(g, x, y);
//				eps += dy;
//				if (2*eps >= dx) {
//					y += uy;
//					eps -= dx;
//				}
//				x+=ux;
//			}
//		} else {
//			while(y!=y2) {
//				Pixel.setPixel(g, x, y);
//				eps += dx;
//				if (2*eps >= dy) {
//					x += ux;
//					eps -= dy;
//				}
//				y+=uy;
//			}
//		}
	}
	
	
	public static int X1=0,X2=0,Y1=0,Y2=0;
	public static void setAreaBound(int x1,int y1,int x2,int y2) {
		X1=Math.min(x1, x2);
		X2=Math.max(x1, x2);
		Y1=Math.min(y1, y2);
		Y2=Math.max(y1, y2);
	}
	public static int[] updatePoints(int x1,int y1,int x2,int y2) {
		int[] ret;
		byte c1=0,c2=0;
		if(y1>Y2)c1|=8;
		if(y1<Y1)c1|=4;
		if(x1>X2)c1|=2;
		if(x1<X1)c1|=1;
		if(y2>Y2)c2|=8;
		if(y2<Y1)c2|=4;
		if(x2>X2)c2|=2;
		if(x2<X1)c2|=1;
//		System.out.println(c1);
//		System.out.println(c2);
		if((c1|c2)==0) {
			ret=new int[] {x1,y1,x2,y2};
		}else if((c1&c2)!=0) {
			ret=null;
		}else {
			if(x2==x1) {
				int id=1;
				ret=new int[] {x1,y1,x2,y2};
				if(Y1>=Math.min(y1, y2)&&Y1<=Math.max(y1, y2)) {
					ret[id]=Y1;
					id+=2;
				}
				if(Y2>=Math.min(y1, y2)&&Y2<=Math.max(y1, y2)) {
					ret[id]=Y2;
					id+=2;
				}
				if(isInArea(x1, y1)&&id<4) {
					ret[id]=y1;
					id+=2;
				}
				if(isInArea(x2, y2)&&id<4) {
					ret[id]=y2;
					id+=2;
				}
				//if(id!=5)System.out.println("WWWW");
			}else if(y2==y1){
				int id=0;
				ret=new int[] {x1,y1,x2,y2};
				if(X1>=Math.min(x1, x2)&&X1<=Math.max(x1, x2)) {
					ret[id]=X1;
					id+=2;
				}
				if(X2>=Math.min(x1, x2)&&X2<=Math.max(x1, x2)) {
					ret[id]=X2;
					id+=2;
				}
				if(isInArea(x1, y1)&&id<3) {
					ret[id]=x1;
					id+=2;
				}
				if(isInArea(x2, y2)&&id<3) {
					ret[id]=x2;
					id+=2;
				}
				//if(id!=4)System.out.println("WWWW");
			}else {
				ret=new int[] {-1,-1,-1,-1,-1,-1};
				double m=((double)y2-y1)/(x2-x1);
				int px1=x1,py1=y1;
				PointInt a=null;
				PointInt b=null;
				int tx1=Math.min(x1, x2),tx2=Math.max(x1, x2);
				int ty1=Math.min(y1, y2),ty2=Math.max(y1, y2);
				int id=0;
				if(X1>=tx1&&X1<=tx2&&isInArea(X1, (int)(y1+(X1-x1)*m))) {
					ret[id++]=X1;
					ret[id++]=(int)(y1+(X1-x1)*m);
				}
				if(X2>=tx1&&X2<=tx2&&isInArea(X2, (int)(y1+(X2-x1)*m))) {
					ret[id++]=X2;
					ret[id++]=(int)(y1+(X2-x1)*m);
				}
				if(Y1>=ty1&&Y1<=ty2&&isInArea((int)(x1+(Y1-y1)/m), Y1)) {
					ret[id++]=(int)(x1+(Y1-y1)/m);
					ret[id++]=Y1;
				}
				if(Y2>=ty1&&Y2<=ty2&&isInArea((int)(x1+(Y2-y1)/m), Y2)) {
					ret[id++]=(int)(x1+(Y2-y1)/m);
					ret[id++]=Y2;
				}
				if(id==2) {
					ret[id++]=ret[0];
					ret[id++]=ret[1];
				}
//				System.out.println(ret[0]+","+ret[1]);
//				System.out.println(ret[2]+","+ret[3]);
//				System.out.println("crossnum="+id/2);
				if(x1!=X1&&x1!=X2&&isInArea(x1, y1)) {
					ret[id++]=x1;
					ret[id++]=y1;
				}
				if(x2!=X1&&x2!=X2&&isInArea(x2, y2)) {
					ret[id++]=x2;
					ret[id++]=y2;
				}
//				System.out.println(id);
				if((ret[2]==ret[0]||ret[3]==ret[1])&&ret[4]!=-1) {
					ret[2]=ret[4];
					ret[3]=ret[5];
				}
//				
//				px1=X1;
//				py1=(int)(y1+(px1-x1)*m);
//				if(py1>=Y1&&py1<=Y2) {
//					a=new PointInt(px1, py1);
//				}
//				px1=X2;
//				py1=(int)(y1+(px1-x1)*m);
//				if(py1>=Y1&&py1<=Y2) {
//					if(a==null)a=new PointInt(px1, py1);
//					else b=new PointInt(px1, py1);
//				}
//				py1=Y1;
//				px1=(int)(x1+(py1-y1)/m);
//				if(px1>=X1&&px1<=X2) {
//					if(a==null)a=new PointInt(px1, py1);
//					else b=new PointInt(px1, py1);
//				}
//				py1=Y2;
//				px1=(int)(x1+(py1-y1)/m);
//				if(px1>=X1&&px1<=X2) {
//					if(a==null)a=new PointInt(px1, py1);
//					else b=new PointInt(px1, py1);
//				}
//				if(a==null&&b==null)return null;
//				if(b==null) {
//					System.out.println("*********");
//					b=a;
//				}
//				int id=0;
//				if(a!=null&&a.getX()>=Math.min(x1,x2)&&a.getX()<=Math.max(x1,x2)) {
//					ret[2*id]=a.getX();
//					ret[2*id+1]=a.getY();
//					id++;
//				}
//				if(b!=null&&b.getX()>=Math.min(x1,x2)&&b.getX()<=Math.max(x1,x2)) {
//					ret[2*id]=b.getX();
//					ret[2*id+1]=b.getY();
//					id++;
//				}
//				if(isInArea(x1, y1)&&id<2) {
//					ret[2*id]=x1;
//					ret[2*id+1]=y1;
//					id++;
//				}
//				if(isInArea(x2, y2)&&id<2) {
//					ret[2*id]=x2;
//					ret[2*id+1]=y2;
//					id++;
//				}
//				a=null;
//				b=null;
//				if(id!=2)System.out.println("SDS");
			}
		}
		return ret;
	}
	public static boolean isInArea(int x,int y) {
		return x>=X1&&x<=X2&&y>=Y1&&y<=Y2;
	}
	public static void drawLine(Graphics g, int x1,int y1,int x2,int y2, boolean storePoints,ArrayList<PointInt> pointInts) {
		if(x1>x2) {
			int temp=x1;
			x1=x2;
			x2=temp;
			temp=y1;
			y1=y2;
			y2=temp;
		}
		if(x1!=x2&&x1<=X1&&x2>=X1) {
			int x=X1,y=(int)((double)y1+(X1-x1)*(y1-y2)/(x1-x2));
		//	if(isInArea(x, y)) {
	//			int step=y1<y?1:-1;
//				int yy=step==1?Math.max(Y1, y1):Math.min(Y2, y1);
//				for(;yy!=y;yy+=step) {
//					pointInts.add(new PointInt(X1, yy));
//				}
		//	}
			int step=y1>y?-1:1;
			for(int yy=y1;yy!=y;yy+=step) {
				if(storePoints&&yy>=Y1&&yy<=Y2)
					pointInts.add(new PointInt(X1, yy));
			}
			if(storePoints&&y>=Y1&&y<=Y2)
				pointInts.add(new PointInt(X1, y));
		}
		if(x1!=x2&&x1<=X2&&x2>=X2) {
			int x=X2,y=(int)((double)y1+(X2-x1)*(y1-y2)/(x1-x2));
		//	if(isInArea(x, y)) {
//				int step=y2<y?1:-1;
//				int yy=step==1?Math.max(Y1, y2):Math.min(Y2, y2);
//				
//				for(;yy!=y;yy+=step) {
//					pointInts.add(new PointInt(X2, yy));
//				}
		//	}
			int step=y2>y?-1:1;
			for(int yy=y2;yy!=y;yy+=step) {
				if(storePoints&&yy>=Y1&&yy<=Y2)
					pointInts.add(new PointInt(X2, yy));
			}
			if(storePoints&&y>=Y1&&y<=Y2)
				pointInts.add(new PointInt(X2, y));
		}
		if(x2<=X1) {
			int yy;
			for(yy=Math.min(y1, y2);yy!=Math.max(y1, y2);yy++) {
				if(storePoints&&yy>=Y1&&yy<=Y2)
					pointInts.add(new PointInt(X1, yy));
			}
			if(storePoints&&yy>=Y1&&yy<=Y2)
				pointInts.add(new PointInt(X1, yy));
		}
		if(x1>=X2) {
			int yy;
			for(yy=Math.min(y1, y2);yy!=Math.max(y1, y2);yy++) {
				if(storePoints&&yy>=Y1&&yy<=Y2)
					pointInts.add(new PointInt(X2, yy));
			}
			if(storePoints&&yy>=Y1&&yy<=Y2)
				pointInts.add(new PointInt(X2, yy));
		}
		
//		for(PointInt pointInt:pointInts) {
//			if(pointInt.getY()==0) {
//				System.out.println("SSSSSSSSSSSSSSSSSSSSSSSs");
//			}
//		}
		
		int[] r=updatePoints(x1,y1,x2,y2);
		if(r==null)return;
		x1=r[0];
		y1=r[1];
		x2=r[2];
		y2=r[3];
//		System.out.println(x1+","+y1);
//		System.out.println(x2+","+y2);
		
		int dx = x2 - x1, dy = y2 - y1;
		int ux = dx > 0 ? 1 : -1, uy = dy > 0 ? 1 : -1;
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		int x = x1, y = y1, eps = 0;
		if (dx > dy) {
			while(x!=x2) {
				Pixel.setPixel(g, x, y);
				if(storePoints)
					pointInts.add(new PointInt(x,y));
				eps += dy;
				if (2*eps >= dx) {
					y += uy;
					eps -= dx;
				}
				x+=ux;
			}
		} else {
			while(y!=y2) {
				Pixel.setPixel(g, x, y);
				if(storePoints)
					pointInts.add(new PointInt(x,y));
				eps += dx;
				if (2*eps >= dy) {
					x += ux;
					eps -= dy;
				}
				y+=uy;
			}
		}
		Pixel.setPixel(g, x2, y2);
		if(storePoints)
			pointInts.add(new PointInt(x2,y2));
		
//		for(PointInt pointInt:pointInts) {
//			if(pointInt.getY()==0) {
//				System.out.println("pppppppppppppppppppp");
//			}
//		}
	}

	public static void drawDottedLine(Graphics g, int x1,int y1,int x2,int y2, boolean storePoints,ArrayList<PointInt> pointInts) {
		
		int dx = x2 - x1, dy = y2 - y1;
		int ux = dx > 0 ? 1 : -1, uy = dy > 0 ? 1 : -1;
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		int x = x1, y = y1, eps = 0;
		int cnt=0;
		if (dx > dy) {
			while(x!=x2) {
				if(cnt%15<12) {
					if(storePoints) {
						pointInts.add(new PointInt(x, y));
					}
					Pixel.setPixel(g, x, y);
				}
				cnt++;
				eps += dy;
				if (2*eps >= dx) {
					y += uy;
					eps -= dx;
				}
				x+=ux;
			}
		} else {
			while(y!=y2) {
				if(cnt%15<12) {
					if(storePoints) {
						pointInts.add(new PointInt(x, y));
					}
					Pixel.setPixel(g, x, y);
				}
				cnt++;
				eps += dx;
				if (2*eps >= dy) {
					x += ux;
					eps -= dy;
				}
				y+=uy;
			}
		}
		Pixel.setPixel(g, x2, y2);
	}
	
	public LineShape(int x1, int y1, int x2, int y2, Color color, PaintArea paintArea) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
		updateCenter();
		dot1 = new FloatingIcon("dot.png");
		dot2 = new FloatingIcon("dot.png");
		rotateArrow = new FloatingIcon("rotateArrow.png");
		rotateCenter=new FloatingIcon("center.png");
		greyLine=new GreyLine();
		this.paintArea=paintArea;
//		paintArea.add(dot1);
//		paintArea.add(dot2);
//		paintArea.add(rotateArrow);
//		paintArea.add(rotateCenter);
//		paintArea.add(greyLine);
		dot1.setVisible(false);
		dot2.setVisible(false);
		rotateArrow.setVisible(false);
		rotateCenter.setVisible(false);
		greyLine.setVisible(false);
		prepare();
	}
	
	@Override
	public void addIcons() {
		paintArea.add(dot1,0);
		paintArea.add(dot2,0);
		paintArea.add(rotateArrow,0);
		paintArea.add(rotateCenter,0);
		paintArea.add(greyLine,0);
	}
	
	@Override
	public void updateIcons() {
		paintArea.remove(rotateArrow);
		paintArea.remove(rotateCenter);
		paintArea.remove(greyLine);
		paintArea.remove(dot1);
		paintArea.remove(dot2);
		paintArea.add(rotateArrow,0);
		paintArea.add(rotateCenter,0);
		paintArea.add(greyLine,0);
		paintArea.add(dot1,0);
		paintArea.add(dot2,0);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(color);
		if(started&&paintArea.window.shifted()) {
			float k=((float)y1-y2)/(x1-x2);
			if(k<0.577333&&k>-0.577333)y2=y1;
			else if(k>1.732||k<-1.732)x2=x1;
			else {
				if(k>0)y2=x2-x1+y1;
				else y2=x1-x2+y1;
			}
			updateCenter();
		}
		drawLine(g);
	}
	
	@Override
	public void move(int x1, int y1, int x2, int y2) {
		this.x1 = cx1 + (x2 - x1);
		this.x2 = cx2 + (x2 - x1);
		this.y1 = cy1 + (y2 - y1);
		this.y2 = cy2 + (y2 - y1);
		repaint();
		updateCenter();
	}

	@Override
	public void updateOthers() {
	}

	@Override
	public void showSelectedStatus() {
		selected = true;
		dot1.setBounds(x1 - 15, y1 - 15, 30, 30);
		dot2.setBounds(x2 - 15, y2 - 15, 30, 30);
		rotateCenter.setBounds(centerX-15,centerY-15, 30, 30);
		double k;
		int deltaX = 0, deltaY = 50;
		if (y1 != y2) {
			if (x1 != x2)
				k = -1 / ((double) (y1 - y2) / (x1 - x2));
			else
				k = 0;
			if (Math.abs(k) < 10) {
				deltaX = (int) (50 / (Math.sqrt(1 + k * k)));
				deltaY = (int) (Math.abs(k * deltaX));
			}
		}
		rotateX=centerX;
		rotateY=centerY-70;
		rotateArrow.setBounds(rotateX-15, rotateY-15, 30, 30);

		dot1.setVisible(true);
		dot2.setVisible(true);
		rotateArrow.setVisible(true);
		rotateCenter.setVisible(true);
		greyLine.setLocations(rotateX, rotateY,centerX,centerY);
		greyLine.setVisible(true);
	}

	@Override
	public void unshowSelectedStatus() {
		dot1.setVisible(false);
		dot2.setVisible(false);
		rotateArrow.setVisible(false);
		rotateCenter.setVisible(false);
		greyLine.setVisible(false);
	}

	@Override
	public int arrowType(int x, int y) {
		xs[0] = x1;
		xs[1] = x2;
		ys[0] = y1;
		ys[1] = y2;
		if (xs[0] <= xs[1] && ys[0] <= ys[1])
			for (int i = 0; i < xs.length; i++)
				types[i] = Cursor.NW_RESIZE_CURSOR;
		else
			for (int i = 0; i < xs.length; i++)
				types[i] = Cursor.NE_RESIZE_CURSOR;
		for (int i = 0; i < xs.length; i++) {
			if (Math.abs(xs[i] - x) <= 10 && Math.abs(ys[i] - y) <= 10) {
				selectedPoint = i;
				return types[i];
			}
		}
		if (Math.abs(centerX - x) <= 10 && Math.abs(centerY - y) <= 10) {
			selectedPoint = 100;
			return Cursor.HAND_CURSOR;
		}
		if (Math.abs(rotateX - x) <= 10 && Math.abs(rotateY - y) <= 10) {
			selectedPoint = 200;
			return 10086;
		}
		return Cursor.DEFAULT_CURSOR;
	}

	private void updateCenter() {
		centerX=(x1+x2)/2;
		centerY=(y1+y2)/2;
	}
	
	private void updateCenter(int x,int y) {
		centerX=x;
		centerY=y;
	}
	
	private Point getRotatedLocation(int dx,int dy,double cosTheta,double sinTheta) {
		Point ret=new Point();
		ret.setLocation(centerX+cosTheta*dx-sinTheta*dy,centerY+sinTheta*dx+cosTheta*dy);
		return ret;
	}
	
	@Override
	public void modify(int endX, int endY) {
		if (selectedPoint == 0) {
			x1 = endX;
			y1 = endY;
			repaint();
			updateCenter();
		} else if(selectedPoint==1){
			x2 = endX;
			y2 = endY;
			repaint();
			updateCenter();
		}else if(selectedPoint==100) {
			updateCenter(endX, endY);
			showCenter();
		}else if(selectedPoint==200) {
			double L=Math.sqrt((endX-centerX)*(endX-centerX)+(endY-centerY)*(endY-centerY));
			double cosTheta=(centerY-endY)/L,sinTheta=(endX-centerX)/L;
			int dx1=cx1-centerX,dx2=cx2-centerX,dy1=cy1-centerY,dy2=cy2-centerY;
			Point tPoint=getRotatedLocation(dx1, dy1, cosTheta, sinTheta);
			x1=(int)tPoint.getX();
			y1=(int)tPoint.getY();
			tPoint=getRotatedLocation(dx2, dy2, cosTheta, sinTheta);
			x2=(int)tPoint.getX();
			y2=(int)tPoint.getY();
			repaint();
		}
	}
	
	private void showCenter() {
		rotateCenter.setBounds(centerX-15, centerY-15, 30, 30);
		rotateCenter.setVisible(true);
	}

	@Override
	public void scale(int endX, int endY) {
//		if (selectedPoint == 0) {
//			if (cx1 != cx2) {
//				x1 = endX;
//				y1 = cy1 + (cy1 - cy2) * (endX - cx1) / (cx1 - cx2);
//			} else
//				y1 = endY;
//		} else {
//			if (cx1 != cx2) {
//				x2 = endX;
//				y2 = cy2 + (cy2 - cy1) * (endX - cx2) / (cx2 - cx1);
//			} else {
//				y2 = endY;
//			}
//		}
//		repaint();
//		updateCenter();
		double scaleFactor=1;
		int virtualCenterX=centerX;
		int virtualCenterY=centerY;
		switch(selectedPoint) {
		case 0:
			scaleFactor=((double)endX-virtualCenterX)/(cx1-virtualCenterX);
			break;
		case 1:
			scaleFactor=((double)endX-virtualCenterX)/(cx2-virtualCenterX);
			break;
		default:
			return;
		}
		x1=(int) ((cx1-virtualCenterX)*scaleFactor+virtualCenterX);
		x2=(int) ((cx2-virtualCenterX)*scaleFactor+virtualCenterX);
		y1=(int) ((cy1-virtualCenterY)*scaleFactor+virtualCenterY);
		y2=(int) ((cy2-virtualCenterY)*scaleFactor+virtualCenterY);
		//System.out.println(x1);
		//updateWeightCenter((bx[0]+bx[2])/2, (by[0]+by[2])/2);
		repaint();
		showCenter();
	}

	@Override
	public void prepare() {
		cx1 = x1;
		cx2 = x2;
		cy1 = y1;
		cy2 = y2;
	}
	
	@Override
	public void delete(ListOfInt[][] board,int id) {
		super.delete(board, id);
		paintArea.remove(dot1);
		paintArea.remove(dot2);
		paintArea.remove(rotateArrow);
		paintArea.remove(rotateCenter);
		paintArea.remove(greyLine);
		paintArea.remove(this);
		paintArea.repaint();
	}
}
