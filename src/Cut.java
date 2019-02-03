import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;

public class Cut extends Shape{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PaintArea paintArea;
	
	int[] x=new int[4]; // paint
	int[] y=new int[4];
	int[] px=new int[4]; // paint copy
	int[] py=new int[4];
	int[] bx=new int[4]; // standard
	int[] by=new int[4];
	int[] cx=new int[4]; // standard copy
	int[] cy=new int[4];
	
	int rotateX, rotateY;
	int weightCenterX,weightCenterY;
	int weightCenterX2,weightCenterY2;
	int virtualCenterX,virtualCenterY;
	int selectedPoint = -1;
	int[] dotX,dotY;
	FloatingIcon[] dots=new FloatingIcon[8];
//	FloatingIcon rotateArrow,
	FloatingIcon rotateCenter;
	//Color color,color2=null;
//	GreyLine greyLine;
//	double cosTheta=1,sinTheta=0;
//	double cCosTheta=1,cSinTheta=0;
//	double cosPhi=1,sinPhi=0;
	boolean beginMoveVirtualCenter=false;

	private void drawCutFrame(Graphics g) {
		pointsOnFrame.clear();
		for(int i=0;i<4;i++) {
			LineShape.drawDottedLine(g, x[i%4], y[i%4], x[(i+1)%4], y[(i+1)%4],true,pointsOnFrame);
		}
	}
	

	public Cut(int x1,int y1, int x2,int y2, PaintArea paintArea) {
		super();
		int tx1=Math.min(x1, x2),tx2=Math.max(x1, x2);
		int ty1=Math.min(y1, y2),ty2=Math.max(y1, y2);
		bx[0]=tx1;by[0]=ty1;
		bx[1]=tx2;by[1]=ty1;
		bx[2]=tx2;by[2]=ty2;
		bx[3]=tx1;by[3]=ty2;
		if(paintArea.window.shifted()) {
			if(y2>y1) {
				by[2]=by[0]+bx[1]-bx[0];
				by[3]=by[2];
			}else {
				by[0]=by[2]+bx[3]-bx[2];
				by[1]=by[0];
			}
		}
		for(int i=0;i<4;i++) {
			x[i]=bx[i];
			y[i]=by[i];
		}
		
		updateWeightCenter();
		updateVirtualCenter(weightCenterX, weightCenterY);
		for(int i=0;i<8;i++) {
			dots[i]=new FloatingIcon("dot.png");
	//		paintArea.add(dots[i]);
			dots[i].setVisible(false);
		}
//		rotateArrow = new FloatingIcon("rotateArrow.png");
//		rotateArrow.setVisible(false);
		rotateCenter=new FloatingIcon("center.png");
		rotateCenter.setVisible(true);
//		greyLine=new GreyLine();
//		greyLine.setVisible(false);
//		paintArea.add(rotateArrow);
//		paintArea.add(rotateCenter);
//		paintArea.add(greyLine);
		this.paintArea=paintArea;
		
		prepare();
	}

	@Override
	public void addIcons() {
//		paintArea.add(rotateArrow,0);
		paintArea.add(rotateCenter,0);
//		paintArea.add(greyLine,0);
		for(int i=0;i<8;i++) {
			paintArea.add(dots[i],0);
		}
	}
	
	@Override
	public void updateIcons() {
//		paintArea.remove(rotateArrow);
		paintArea.remove(rotateCenter);
//		paintArea.remove(greyLine);
//		paintArea.add(rotateArrow,0);
		paintArea.add(rotateCenter,0);
//		paintArea.add(greyLine,0);
		for(int i=0;i<8;i++) {
			paintArea.remove(dots[i]);
		}
		for(int i=0;i<8;i++) {
			paintArea.add(dots[i],0);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		updatePaintCoordinates();
		drawCutFrame(g);
		LineShape.setAreaBound(Math.min(Math.min(x[0], x[1]),x[2]), 
				Math.min(Math.min(y[0], y[1]),y[2]),
				Math.max(Math.max(x[0], x[1]),x[2]), 
				Math.max(Math.max(y[0], y[1]),y[2]));
	}

	private void updatePaintCoordinates() {
		Point tPoint;
		for(int i=0;i<4;i++) {
			int dx=bx[i]-weightCenterX,dy=by[i]-weightCenterY;
			tPoint=getRotatedLocation(dx, dy,weightCenterX,weightCenterY,0,1);
			x[i]=(int) tPoint.getX();
			y[i]=(int) tPoint.getY();
		}
	}
	
	@Override
	public void move(int x1, int y1, int x2, int y2) {
		beginMoveVirtualCenter=false;
		int dx=x2-x1,dy=y2-y1;
		for(int i=0;i<4;i++) {
			bx[i]=cx[i]+dx;
			by[i]=cy[i]+dy;
		}
		updatePaintCoordinates();
		updateWeightCenter();
		repaint();
	}

	@Override
	public void updateOthers() {
	}

	@Override
	public void showSelectedStatus() {
		selected = true;
		dotX=new int[] {x[0],(x[0]+x[1])/2,x[1],(x[0]+x[3])/2,(x[1]+x[2])/2,x[3],(x[2]+x[3])/2,x[2]};
		dotY=new int[] {y[0],(y[0]+y[1])/2,y[1],(y[0]+y[3])/2,(y[1]+y[2])/2,y[3],(y[2]+y[3])/2,y[2]};
		for(int i=0;i<dots.length;i++) {
			dots[i].setBounds(dotX[i]-15, dotY[i]-15, 30, 30);
			dots[i].setVisible(true);
		}
		if(!beginMoveVirtualCenter) {
			updateVirtualCenter(weightCenterX, weightCenterY);
		}
		rotateX = virtualCenterX;
		rotateY = virtualCenterY-70;
//		rotateArrow.setBounds(rotateX-15, rotateY-15, 30, 30);
//		rotateArrow.setVisible(true);
//		
		rotateCenter.setBounds(virtualCenterX-15,virtualCenterY-15,30,30);
		rotateCenter.setVisible(true);
//		greyLine.setLocations(rotateX,rotateY,virtualCenterX,virtualCenterY);
//		greyLine.setVisible(true);
	}

	@Override
	public void unshowSelectedStatus() {
		for(FloatingIcon dot:dots)dot.setVisible(false);
//		rotateArrow.setVisible(false);
		rotateCenter.setVisible(false);
//		greyLine.setVisible(false);
	}
	
	private void updateWeightCenter() {
		weightCenterX=(bx[0]+bx[2])/2;
		weightCenterY=(by[0]+by[2])/2;
	}
	
	private void updateWeightCenter(int x,int y) {
		weightCenterX=x;
		weightCenterY=y;
	}
	
	private void updateVirtualCenter(int x,int y) {
		virtualCenterX=x;
		virtualCenterY=y;
	}
	
	private void showCenter() {
		rotateCenter.setBounds(virtualCenterX-15, virtualCenterY-15, 30, 30);
		rotateCenter.setVisible(true);
	}

	@Override
	public int arrowType(int x, int y) {
		for(int i=0;i<dotX.length;i++) {
			if(Math.abs(x-dotX[i])<=10&&Math.abs(y-dotY[i])<=10) {
				selectedPoint=i;
				switch (selectedPoint) {
				case 0:
				case 7:
					return Cursor.NW_RESIZE_CURSOR;
				case 1:
				case 6:
					return Cursor.S_RESIZE_CURSOR;
				case 2:
				case 5:
					return Cursor.NE_RESIZE_CURSOR;
				case 3:
				case 4:
					return Cursor.W_RESIZE_CURSOR;
				default:
					break;
				}
			}
		}
		if (Math.abs(virtualCenterX - x) <= 10 && Math.abs(virtualCenterY - y) <= 10) {
			selectedPoint = 100;
			return Cursor.HAND_CURSOR;
		}
//		if (Math.abs(rotateX - x) <= 10 && Math.abs(rotateY - y) <= 10) {
//			selectedPoint = 200;
//			return 10086;
//		}
		return Cursor.DEFAULT_CURSOR;
	}

	@Override
	public void modify(int endX, int endY) {
		//color2=null;
		beginMoveVirtualCenter=false;
		int x0=0,y0=0,x1=endX,y1=endY,dx=0,dy=0;
		switch (selectedPoint) {
		case 0:
			x0=px[2];
			y0=py[2];
			break;
		case 2:
			x0=px[3];
			y0=py[3];
			break;
		case 5:
			x0=px[1];
			y0=py[1];
			break;
		case 7:
			x0=px[0];
			y0=py[0];
			break;
		case 1:
			x0=px[2];
			y0=py[2];
			if(px[2]!=px[1]) {
				dx=endX-(px[0]+px[1])/2;
				dy=(int) ((double)dx*(py[2]-py[1])/(px[2]-px[1]));
			}else {
				dx=0;
				dy=endY-(py[0]+py[1])/2;
			}
			x1=px[0]+dx;
			y1=py[0]+dy;
			break;
		case 3:
			x0=px[1];
			y0=py[1];
			if(px[0]!=px[1]) {
				dx=endX-(px[0]+px[3])/2;
				dy=(int) ((double)dx*(py[0]-py[1])/(px[0]-px[1]));
			}else {
				dx=0;
				dy=endY-(py[0]+py[3])/2;
			}
			x1=px[3]+dx;
			y1=py[3]+dy;
			break;
		case 4:
			x0=px[3];
			y0=py[3];
			if(px[0]!=px[1]) {
				dx=endX-(px[2]+px[1])/2;
				dy=(int) (dx*(py[0]-py[1])/(px[0]-px[1]));
			}else {
				dx=0;
				dy=endY-(py[2]+py[1])/2;
			}
			x1=px[1]+dx;
			y1=py[1]+dy;
			break;
		case 6:
			x0=px[0];
			y0=py[0];
			if(px[2]!=px[1]) {
				dx=endX-(px[2]+px[3])/2;
				dy=(int) (dx*(py[2]-py[1])/(px[2]-px[1]));
			}else {
				dx=0;
				dy=endY-(py[2]+py[3])/2;
			}
			x1=px[2]+dx;
			y1=py[2]+dy;
			break;
		case 100:
			beginMoveVirtualCenter=true;
			updateVirtualCenter(endX, endY);
			showCenter();
			return;
//		case 200:
//			double L=Math.sqrt((endX-virtualCenterX)*(endX-virtualCenterX)+(endY-virtualCenterY)*(endY-virtualCenterY));
//			cosPhi=(virtualCenterY-endY)/L;
//			sinPhi=(endX-virtualCenterX)/L;
//			sinTheta=cSinTheta*cosPhi+cCosTheta*sinPhi;
//			cosTheta=cCosTheta*cosPhi-cSinTheta*sinPhi;
//			Point tPoint=getRotatedLocation(weightCenterX2-virtualCenterX, weightCenterY2-virtualCenterY,
//						virtualCenterX, virtualCenterY, sinPhi, cosPhi);
//			move(weightCenterX2, weightCenterY2, (int)tPoint.getX(), (int)tPoint.getY());
//			beginMoveVirtualCenter=true;
//			repaint();
//			showCenter();
//			greyLine.setLocations(endX,endY,virtualCenterX,virtualCenterY);
//			greyLine.repaint();
//			greyLine.setVisible(true);
//			return;
		default:
			break;
		}
		updateWeightCenter((x0+x1)/2, (y0+y1)/2);
		double sinTheta=0;
		double cosTheta=1;
		Point tPoint1=getRotatedLocation(x0-weightCenterX, y0-weightCenterY,weightCenterX,weightCenterY,sinTheta,cosTheta);
		Point tPoint2=getRotatedLocation(x1-weightCenterX, y1-weightCenterY,weightCenterX,weightCenterY,sinTheta,cosTheta);
		sinTheta=-sinTheta;
		int tx1=(int) Math.min(tPoint1.getX(), tPoint2.getX());
		int tx2=(int) Math.max(tPoint1.getX(), tPoint2.getX());
		int ty1=(int) Math.min(tPoint1.getY(), tPoint2.getY());
		int ty2=(int) Math.max(tPoint1.getY(), tPoint2.getY());
		bx[0]=tx1;by[0]=ty1;
		bx[1]=tx2;by[1]=ty1;
		bx[2]=tx2;by[2]=ty2;
		bx[3]=tx1;by[3]=ty2;
		repaint();
	} 
	
	private Point getRotatedLocation(int dx,int dy,int cx,int cy,double sinTheta,double cosTheta) {
		Point ret=new Point();
		ret.setLocation(cx+cosTheta*dx-sinTheta*dy,cy+sinTheta*dx+cosTheta*dy);
		return ret;
	}
	
	@Override
	public void scale(int endX, int endY) {
		//color2=null;
		beginMoveVirtualCenter=true;
		double scaleFactor=1;
		switch(selectedPoint) {
		case 0:
			scaleFactor=((double)endX-virtualCenterX)/(px[0]-virtualCenterX);
			break;
		case 2:
			scaleFactor=((double)endX-virtualCenterX)/(px[1]-virtualCenterX);
			break;
		case 5:
			scaleFactor=((double)endX-virtualCenterX)/(px[3]-virtualCenterX);
			break;
		case 7:
			scaleFactor=((double)endX-virtualCenterX)/(px[2]-virtualCenterX);
			break;
		default:
			return;
		}
		for(int i=0;i<4;i++) {
			bx[i]=(int) ((cx[i]-virtualCenterX)*scaleFactor+virtualCenterX);
			by[i]=(int)((cy[i]-virtualCenterY)*scaleFactor+virtualCenterY);
		}
		updateWeightCenter((bx[0]+bx[2])/2, (by[0]+by[2])/2);
		repaint();
		showCenter();
	}

	public void prepare() {
		for(int i=0;i<4;i++) {
			cx[i]=bx[i];
			cy[i]=by[i];
			px[i]=x[i];
			py[i]=y[i];
		}
//		cCosTheta=cosTheta;
//		cSinTheta=sinTheta;
		weightCenterX2=weightCenterX;
		weightCenterY2=weightCenterY;
	}
	
	@Override
	public boolean isWithin(int endX,int endY) {
		int ptNum = 4;
        int j = ptNum - 1;
        int zeroState = 0;
        for(int k=0;k<4;k++) {
        	if(((y[k]>endY)!=(y[j]>endY))&&(endX < (x[j] - x[k]) * (endY - y[k]) / (y[j] - y[k]) + x[k])) {
        		if (y[k] > y[j]) {
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
		LineShape.setAreaBound(0, 0, 1100, 790);
		for(int i=0;i<8;i++) {
			paintArea.remove(dots[i]);
		}
		paintArea.remove(rotateCenter);
		paintArea.remove(this);
		paintArea.repaint();
	}
}
