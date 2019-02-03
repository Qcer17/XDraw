import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.IntPredicate;

public class CurveShape extends Shape{
	ArrayList<Point> points=new ArrayList<Point>();  // paint
	ArrayList<Point> ppoints=new ArrayList<Point>(); // paint copy
	ArrayList<Point> bpoints=new ArrayList<Point>();  // standard
	ArrayList<Point> cpoints=new ArrayList<Point>(); // standard copy
	private static final long serialVersionUID = 1L;
	PaintArea paintArea;
	int rotateX, rotateY;
	int weightCenterX,weightCenterY;
	int weightCenterX2,weightCenterY2;
	int virtualCenterX,virtualCenterY;
	int selectedPoint = -1;
	int confirmedNumPoints=0;
	int[] dotsX=new int[8];
	int[] dotsY=new int[8];
	int[] cdotsX=new int[8];
	int[] cdotsY=new int[8];
	int[] bdotsX=new int[8];
	int[] bdotsY=new int[8];
	int[] pdotsX=new int[8];
	int[] pdotsY=new int[8];
	int n=3;
	double cosTheta=1,sinTheta=0;
	double cCosTheta=1,cSinTheta=0;
	double cosPhi=1,sinPhi=0;
	FloatingIcon[] dots=new FloatingIcon[8];
	FloatingIcon rotateArrow,rotateCenter;
	FloatingIcon assistDot,lastDot;
	ArrayList<FloatingIcon> pointIcons=new ArrayList<FloatingIcon>();
	//Color color,color2;
	GreyLine greyLine;
	boolean firstLineDrawed=false;
	boolean beginMoveVirtualCenter=false;

	@Override
	public void finished() {
	//	points.add(new Point(points.get(0)));
	//	System.out.println(points.size());
		started=false;
	}
	
	private void drawCurve(Graphics g) {
		pointsOnFrame.clear();
		if(points.size()<n+1)return;
		double x=-1,y=-1;
		double prex=-1,prey=-1;
		for(int i=0;i<points.size()-n;i++) {
			for(double t=0;t<=1;t+=0.005) {
				x=0;
				y=0;
				for(int k=0;k<=n;k++) {
					x+=points.get(i+k).getX()*baseFunction(k, t);
					y+=points.get(i+k).getY()*baseFunction(k, t);
				}
				if(prex>=0&&prey>=0) {
					LineShape.drawLine(g, (int)prex, (int)prey, (int)x, (int)y, true, pointsOnFrame);
				}
				prex=x;
				prey=y;
//				if(LineShape.isInArea((int)x,(int)y))
//					Pixel.setPixel(g, (int)x, (int)y);
//				pointsOnFrame.add(new PointInt((int)x, (int)y));
			}
		}
	}

	private double baseFunction(int k,double t) {
		if(n==3) {
			switch (k) {
			case 0:
				return (-t*t*t+3*t*t-3*t+1)/6;
			case 1:
				return (3*t*t*t-6*t*t+4)/6;
			case 2:
				return (-3*t*t*t+3*t*t+3*t+1)/6;
			case 3:
				return t*t*t/6;
			default:
				break;
			}
		}
		return 0;
	}
	
	public void addNumPoints() {
		if(confirmedNumPoints>=points.size())return;
		confirmedNumPoints++;
		FloatingIcon tFloatingIcon=new FloatingIcon("square.png");
		paintArea.add(tFloatingIcon,0);
		tFloatingIcon.setBounds((int)points.get(confirmedNumPoints-1).getX()-15,
				(int)points.get(confirmedNumPoints-1).getY()-15, 
				30, 30);
		tFloatingIcon.setVisible(true);
		pointIcons.add(tFloatingIcon);
		repaint();
	}
	
	public void updateFirstLine() {
		firstLineDrawed=true;
	}
	
	public void removeLastPoint() {
		bpoints.remove(bpoints.size()-1);
		cpoints.remove(cpoints.size()-1);
		points.remove(points.size()-1);
		ppoints.remove(ppoints.size()-1);
		
		repaint();
	}
	
	public void addPoint(int x,int y) {
		bpoints.add(new Point(x, y));
		cpoints.add(new Point(x,y));
		points.add(new Point(x,y));
		ppoints.add(new Point(points.get(0)));
		repaint();
	}
	
	public int getNearPointId(int x,int y) {
		for(int i=0;i<points.size()-1;i++) {
			if(Math.abs(x-points.get(i).getX())<10&&Math.abs(y-points.get(i).getY())<10) {
				return i;
			}
		}
		return -1;
	}
	
	public void showAssistDot(int id) {
		assistDot.setBounds((int)points.get(id).getX()-15, (int)points.get(id).getY()-15, 30, 30);
		assistDot.setVisible(true);
	}
	
	public void unshowAssistDot() {
		assistDot.setVisible(false);
	}
	
	public void showLastPoint() {
		lastDot.setBounds((int)bpoints.get(bpoints.size()-1).getX()-15, 
				(int)bpoints.get(bpoints.size()-1).getY()-15, 30, 30);
		lastDot.setVisible(true);
	}
	
	public void unshowLastPoint() {
		lastDot.setVisible(false);
	}
	
	public CurveShape(int x1,int y1,int x2,int y2,Color color,Color color2, PaintArea paintArea) {
		super();
		bdotsX[0]=-1;
		addPoint(x1, y1);
		addPoint(x2, y2);
		this.color=color;
		this.color2=color2;
		this.paintArea=paintArea;
		for(int i=0;i<8;i++) {
			dots[i]=new FloatingIcon("dot.png");
			paintArea.add(dots[i]);
			dots[i].setVisible(false);
		}
		rotateArrow = new FloatingIcon("rotateArrow.png");
		rotateArrow.setVisible(false);
		paintArea.add(rotateArrow);
		rotateCenter=new FloatingIcon("center.png");
		rotateCenter.setVisible(true);
		paintArea.add(rotateCenter);
		assistDot = new FloatingIcon("dot.png");
		paintArea.add(assistDot);
		assistDot.setVisible(false);
		lastDot=new FloatingIcon("dot.png");
		paintArea.add(lastDot);
		lastDot.setVisible(false);
		greyLine=new GreyLine();
		greyLine.setVisible(false);
		paintArea.add(greyLine);
		prepare();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(color);
		updatePaintCoordinates();
		drawCurve(g);
		if(started||selected) {
			for(int i=0;i<confirmedNumPoints-1;i++) {
				g.setColor(Color.LIGHT_GRAY);
				LineShape.drawLine(g, (int)points.get(i).getX(), 
						(int)points.get(i).getY(), 
						(int)points.get(i+1).getX(), 
						(int)points.get(i+1).getY(), false, null);
			}
		}
	}
	
	private void updatePaintCoordinates() {
		for(int i=0;i<bpoints.size();i++) {
			Point aPoint=bpoints.get(i);
			int dx=(int)aPoint.getX()-weightCenterX,dy=(int)aPoint.getY()-weightCenterY;
			points.get(i).setLocation(getRotatedLocation(dx, dy,weightCenterX,weightCenterY,sinTheta,cosTheta));
		}
		for(int i=0;i<bdotsX.length;i++) {
			int dx=bdotsX[i]-weightCenterX,dy=bdotsY[i]-weightCenterY;
			Point tPoint=getRotatedLocation(dx, dy, weightCenterX, weightCenterY,sinTheta,cosTheta);
			dotsX[i]=(int)tPoint.getX();
			dotsY[i]=(int)tPoint.getY();
		}
	}

	private Point getRotatedLocation(int dx,int dy,int cx,int cy,double sinTheta,double cosTheta) {
		Point ret=new Point();
		ret.setLocation(cx+cosTheta*dx-sinTheta*dy,cy+sinTheta*dx+cosTheta*dy);
		return ret;
	}
	
	@Override
	public void move(int x1, int y1, int x2, int y2) {
		beginMoveVirtualCenter=false;
		int dx=x2-x1,dy=y2-y1;
		for(int i=0;i<bpoints.size();i++) {
			bpoints.get(i).setLocation(cpoints.get(i));
			bpoints.get(i).translate(dx, dy);
		}
		for(int i=0;i<8;i++) {
			bdotsX[i]=cdotsX[i]+dx;
			bdotsY[i]=cdotsY[i]+dy;
		}
		weightCenterX=weightCenterX2+dx;
		weightCenterY=weightCenterY2+dy;
		
		repaint();
	}

	@Override
	public void updateOthers() {
	}

	@Override
	public void showSelectedStatus() {
		selected = true;
		if(bdotsX[0]<0) {
			updateDots();
			weightCenterX=(bdotsX[0]+bdotsX[2])/2;
			weightCenterY=(bdotsY[0]+bdotsY[5])/2;
			weightCenterX2=(bdotsX[0]+bdotsX[2])/2;
			weightCenterY2=(bdotsY[0]+bdotsY[5])/2;
			virtualCenterX=weightCenterX;
			virtualCenterY=weightCenterY;
			updatePaintCoordinates();
		}
		if(!beginMoveVirtualCenter) {
			updateVirtualCenter((dotsX[0]+dotsX[7])/2, (dotsY[0]+dotsY[7])/2);
		}
		for(int i=0;i<dots.length;i++) {
			dots[i].setBounds(dotsX[i]-15, dotsY[i]-15, 30, 30);
			dots[i].setVisible(true);
		}
		
		rotateX = virtualCenterX;
		rotateY = virtualCenterY-70;
		rotateArrow.setBounds(rotateX-15, rotateY-15, 30, 30);
		rotateArrow.setVisible(true);
		
		rotateCenter.setBounds(virtualCenterX-15,virtualCenterY-15,30,30);
		rotateCenter.setVisible(true);
		greyLine.setLocations(rotateX,rotateY,virtualCenterX,virtualCenterY);
		greyLine.setVisible(true);
		
		for(int i=0;i<points.size();i++) {
			pointIcons.get(i).setBounds((int)points.get(i).getX()-15, (int)points.get(i).getY()-15, 30, 30);
			pointIcons.get(i).setVisible(true);
		}
	}

	@Override
	public void unshowSelectedStatus() {
		
		for(FloatingIcon dot:dots)dot.setVisible(false);
		for(FloatingIcon pointIcon:pointIcons)pointIcon.setVisible(false);
		rotateArrow.setVisible(false);
		rotateCenter.setVisible(false);
		greyLine.setVisible(false);
	}

	@Override
	public int arrowType(int x, int y) {
		selectedPoint=-1;
		for(int i=0;i<8;i++) {
			if((Math.abs(dotsX[i]-x)<=10&&Math.abs(dotsY[i]-y)<=10)){
				selectedPoint=i;
				switch (selectedPoint) {
					case 0:
					case 7:
						return Cursor.SE_RESIZE_CURSOR;
					case 1:
					case 6:
						return Cursor.S_RESIZE_CURSOR;
					case 2:
					case 5:
						return Cursor.NE_RESIZE_CURSOR;
					case 3:
					case 4:
						return Cursor.E_RESIZE_CURSOR;
					default:
						break;
				}			
			}
		}
		
		for(int i=0;i<points.size();i++) {
			if(Math.abs(points.get(i).getX()-x)<10&&Math.abs(points.get(i).getY()-y)<10) {
				selectedPoint=9+i;
				return Cursor.HAND_CURSOR;
			}
		}
		
		if (Math.abs(virtualCenterX - x) <= 10 && Math.abs(virtualCenterY - y) <= 10) {
			selectedPoint = 100;
			return Cursor.HAND_CURSOR;
		}
		if (Math.abs(rotateX - x) <= 10 && Math.abs(rotateY - y) <= 10) {
			selectedPoint = 200;
			return 10086;
		}
		
		return Cursor.DEFAULT_CURSOR;
	}

	@Override
	public void modify(int endX, int endY) {
		beginMoveVirtualCenter=false;
		int newdx,newdy,dx=cdotsX[2]-cdotsX[0],dy=cdotsY[5]-cdotsY[0],baseX,baseY;
		boolean middleX=false,middleY=false;
		if(selectedPoint>=0&&selectedPoint<=7)dotsX[0]=-1;
		switch (selectedPoint) {
		case 0:
			break;
		case 1:
			middleX=true;
			break;
		case 2:
			break;
		case 3:
			middleY=true;
			break;
		case 4:
			middleY=true;
			break;
		case 5:
			break;
		case 6:
			middleX=true;
			break;
		case 7:
			break;
		case 100:
			beginMoveVirtualCenter=true;
			updateVirtualCenter(endX, endY);
			showCenter();
			return;
		case 200:
			double L=Math.sqrt((endX-virtualCenterX)*(endX-virtualCenterX)+(endY-virtualCenterY)*(endY-virtualCenterY));
			double cosPhi=(virtualCenterY-endY)/L;
			double sinPhi=(endX-virtualCenterX)/L;
			sinTheta=cSinTheta*cosPhi+cCosTheta*sinPhi;
			cosTheta=cCosTheta*cosPhi-cSinTheta*sinPhi;
			Point tPoint=getRotatedLocation(weightCenterX2-virtualCenterX, weightCenterY2-virtualCenterY,
					virtualCenterX, virtualCenterY, sinPhi, cosPhi);
			move(weightCenterX2, weightCenterY2, (int)tPoint.getX(), (int)tPoint.getY());
			beginMoveVirtualCenter=true;
			repaint();
			showCenter();
			greyLine.setLocations(endX,endY,virtualCenterX,virtualCenterY);
			greyLine.repaint();
			greyLine.setVisible(true);
			return;
		default:
			Point aPoint=getRotatedLocation(endX-weightCenterX2, endY-weightCenterY2, weightCenterX2, weightCenterY2,-sinTheta, cosTheta);
			Point ttPoint=bpoints.get(selectedPoint-9);
			for(int i=selectedPoint-8;i<bpoints.size();i++) {
				if(bpoints.get(i).equals(ttPoint)) {
					bpoints.get(i).setLocation(aPoint);
				}
			}
			ttPoint.setLocation(aPoint);
			updateDots();
			repaint();
			return;
		}
		beginMoveVirtualCenter=false;
		baseX=cdotsX[7-selectedPoint];
		baseY=cdotsY[7-selectedPoint];
		Point tPoint=getRotatedLocation(endX-weightCenterX2, endY-weightCenterY2, weightCenterX2, weightCenterY2, -sinTheta, cosTheta);
		newdx=(int) (tPoint.getX()-baseX);
		newdy=(int) (tPoint.getY()-baseY);
		for(int i=0;i<bpoints.size();i++) {
			bpoints.get(i).setLocation(baseX+(middleX?cpoints.get(i).getX()-baseX:(float)newdx/dx*Math.abs(cpoints.get(i).getX()-baseX)),
					baseY+(middleY?cpoints.get(i).getY()-baseY:(float)newdy/dy*Math.abs(cpoints.get(i).getY()-baseY)));
		}
		updateDots();
		repaint();
	} 
	
	@Override
	public void scale(int endX, int endY) {
		beginMoveVirtualCenter=true;
		double scaleFactor=1;
		switch(selectedPoint) {
		case 0:
		case 2:
		case 5:
		case 7:
			scaleFactor=((double)endX-virtualCenterX)/(pdotsX[selectedPoint]-virtualCenterX);
			break;
		default:
			return;
		}
		for(int i=0;i<bpoints.size();i++) {
			bpoints.get(i).setLocation((cpoints.get(i).getX()-virtualCenterX)*scaleFactor+virtualCenterX,
					(cpoints.get(i).getY()-virtualCenterY)*scaleFactor+virtualCenterY);
		}
		weightCenterX=(int) ((weightCenterX2-virtualCenterX)*scaleFactor+virtualCenterX);
		weightCenterY=(int) ((weightCenterY2-virtualCenterY)*scaleFactor+virtualCenterY);
		updateDots();
		repaint();
		showCenter();
	}

	public void prepare() {
		for(int i=0;i<bpoints.size();i++) {
			cpoints.get(i).setLocation(bpoints.get(i));
			ppoints.get(i).setLocation(points.get(i));
		}
		for(int i=0;i<8;i++) {
			cdotsX[i]=bdotsX[i];
			cdotsY[i]=bdotsY[i];
			pdotsX[i]=dotsX[i];
			pdotsY[i]=dotsY[i];
		}
		cCosTheta=cosTheta;
		cSinTheta=sinTheta;
		weightCenterX2=weightCenterX;
		weightCenterY2=weightCenterY;
	}
	
	private void updateVirtualCenter(int x,int y) {
		virtualCenterX=x;
		virtualCenterY=y;
	}
	
	private void showCenter() {
		rotateCenter.setBounds(virtualCenterX-15, virtualCenterY-15, 30, 30);
		rotateCenter.setVisible(true);
	}
	
	private Point getRotatedLocation(int dx,int dy,int cx,int cy) {
		Point ret=new Point();
		ret.setLocation(cx+cosTheta*dx-sinTheta*dy,cy+sinTheta*dx+cosTheta*dy);
		return ret;
	}
	
	private void updateWeightCenter(int x,int y) {
		weightCenterX=x;
		weightCenterY=y;
	}
	
	private void updateWeightCenter() {
		weightCenterX=(bdotsX[0]+bdotsX[7])/2;
		weightCenterY=(bdotsY[0]+bdotsY[7])/2;
	}
	
	private void updateDots() {
		int tx1=9999999,tx2=-1,ty1=9999999,ty2=-1;
		for(int i=0;i<bpoints.size();i++) {
			tx1=Math.min((int)bpoints.get(i).getX(), tx1);
			ty1=Math.min((int)bpoints.get(i).getY(), ty1);
			tx2=Math.max((int)bpoints.get(i).getX(), tx2);
			ty2=Math.max((int)bpoints.get(i).getY(), ty2);
		}
		bdotsX=new int[] {tx1,(tx1+tx2)/2,tx2,tx1,tx2,tx1,(tx1+tx2)/2,tx2};
		bdotsY=new int[] {ty1,ty1,ty1,(ty1+ty2)/2,(ty1+ty2)/2,ty2,ty2,ty2};
	}
	
	@Override
	public boolean isWithin(int endX,int endY) {
		int ptNum = points.size();
        int j = ptNum - 1;
        int zeroState = 0;
        for(int k=0;k<ptNum;k++) {
        	if(((points.get(k).getY()>endY)!=(points.get(j).getY()>endY))
        			&&(endX < (points.get(j).getX() - points.get(k).getX()) * (endY - points.get(k).getY()) 
        					/ (points.get(j).getY() - points.get(k).getY()) + points.get(k).getX())) {
        		if (points.get(k).getY() > points.get(j).getY()) {
                    zeroState++;
                }
                else {
                    zeroState--;
                }
        	}
        	j=k;
        }
        return zeroState!=0;
    }
	
	@Override
	public void delete(ListOfInt[][] board,int id) {
		super.delete(board, id);
		for(int i=0;i<8;i++) {
			paintArea.remove(dots[i]);
		}
		for(FloatingIcon icon:pointIcons) {
			paintArea.remove(icon);
		}
		paintArea.remove(assistDot);
		paintArea.remove(lastDot);
		paintArea.remove(rotateArrow);
		paintArea.remove(rotateCenter);
		paintArea.remove(greyLine);
		paintArea.remove(this);
		paintArea.repaint();
	}
}
