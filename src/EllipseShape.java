import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class EllipseShape extends Shape{
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
	
	int rotateX, rotateY, centerX,centerY;
	int centerX2,centerY2,centerX3,centerY3;
	int weightCenterX,weightCenterY;
	int weightCenterX2,weightCenterY2;
	int virtualCenterX,virtualCenterY;
	int selectedPoint = -1;
	int[] dotX,dotY;
	FloatingIcon[] dots=new FloatingIcon[8];
	FloatingIcon rotateArrow,rotateCenter;
	//Color color,color2;
	GreyLine greyLine;
	double cosTheta=1,sinTheta=0;
	double cCosTheta=1,cSinTheta=0;
	double cosPhi=1,sinPhi=0;
	boolean beginMoveVirtualCenter=false;
	boolean isCircle=false;
	boolean firstPoint=false;
	int[] oldX=new int[] {-1,-1,-1,-1};
	int[] oldY=new int[4];
	boolean allowToRepaint=false;

	private void drawCircle(Graphics g) {
		pointsOnFrame.clear();
		int r=Math.abs(bx[1]-weightCenterX);
		int x=0,y=r,p=3-2*r;
		while(x<=y) {
			setCirclePixel(g,x,y);
			if(p>=0)y--;
			p+=(p>=0)?4*(x-y)+10:4*x+6;
			x++;
		}
	}
	
	public void repaintRequest() {
		allowToRepaint=true;
	}
	
	private void drawEllipse(Graphics g) {
		firstPoint=false;
		pointsOnFrame.clear();
		int rx=Math.abs((bx[1]-bx[0]))/2,ry=Math.abs((by[3]-by[0]))/2;
		int rx2=rx*rx,ry2=ry*ry;
	    double p = ry2 + rx2*(0.25 - ry);
	    int x = 0;
	    int y = ry;
	    translateLocationAndSetPixel(g,x,y);
	    while (ry2*(x + 1) < rx2*(y - 0.5))
	    {
	    	if(p>=0)y--;
	    	p+=((p<0)?ry2*(2 * x + 3):(ry2*(2 * x + 3) + rx2*((-2)*y + 2)));
	        x++;
	        translateLocationAndSetPixel(g,x,y);
	        firstPoint=true;
	    }
	    p = 2*(ry * (x + 0.5))+ 2*(rx * (y - 1)) - 2*(rx * ry);
	    while (y > 0)
	    {
	    	if(p<0)x++;
	    	p+=(p<0)?ry2 * (2 * x + 2) + rx2 * ((-2) * y + 3):rx2 * ((-2) * y + 3);
	        y--;
	        translateLocationAndSetPixel(g,x,y);
	    }
	    oldX[0]=-1;
	}

	
	
	private void setCirclePixel(Graphics g,int dx,int dy) {
		if(LineShape.isInArea(weightCenterX+dx, weightCenterY+dy))
			Pixel.setPixel(g, weightCenterX+dx, weightCenterY+dy);
		if(LineShape.isInArea(weightCenterX+dx, weightCenterY-dy))
			Pixel.setPixel(g, weightCenterX+dx, weightCenterY-dy);
		if(LineShape.isInArea(weightCenterX-dx, weightCenterY-dy))
			Pixel.setPixel(g, weightCenterX-dx, weightCenterY-dy);
		if(LineShape.isInArea(weightCenterX-dx, weightCenterY+dy))
			Pixel.setPixel(g, weightCenterX-dx, weightCenterY+dy);
		if(LineShape.isInArea(weightCenterX+dy, weightCenterY+dx))
			Pixel.setPixel(g, weightCenterX+dy, weightCenterY+dx);
		if(LineShape.isInArea(weightCenterX+dy, weightCenterY-dx))
			Pixel.setPixel(g, weightCenterX+dy, weightCenterY-dx);
		if(LineShape.isInArea(weightCenterX-dy, weightCenterY-dx))
			Pixel.setPixel(g, weightCenterX-dy, weightCenterY-dx);
		if(LineShape.isInArea(weightCenterX-dy, weightCenterY+dx))
			Pixel.setPixel(g, weightCenterX-dy, weightCenterY+dx);
		
//		Pixel.setPixel(g, weightCenterX+dx, weightCenterY-dy);
//		Pixel.setPixel(g, weightCenterX-dx, weightCenterY+dy);
//		Pixel.setPixel(g, weightCenterX-dx, weightCenterY-dy);
//		Pixel.setPixel(g, weightCenterX+dy, weightCenterY+dx);
//		Pixel.setPixel(g, weightCenterX+dy, weightCenterY-dx);
//		Pixel.setPixel(g, weightCenterX-dy, weightCenterY+dx);
//		Pixel.setPixel(g, weightCenterX-dy, weightCenterY-dx);
		pointsOnFrame.add(new PointInt(weightCenterX+dx, weightCenterY+dy));
		pointsOnFrame.add(new PointInt(weightCenterX+dx, weightCenterY-dy));
		pointsOnFrame.add(new PointInt(weightCenterX-dx, weightCenterY+dy));
		pointsOnFrame.add(new PointInt(weightCenterX-dx, weightCenterY-dy));
		pointsOnFrame.add(new PointInt(weightCenterX+dy, weightCenterY+dx));
		pointsOnFrame.add(new PointInt(weightCenterX+dy, weightCenterY-dx));
		pointsOnFrame.add(new PointInt(weightCenterX-dy, weightCenterY+dx));
		pointsOnFrame.add(new PointInt(weightCenterX-dy, weightCenterY-dx));
	}
	
	private void translateLocationAndSetPixel(Graphics g,int dx,int dy) {
		
		int cosdx=(int) (cosTheta*dx),sindx=(int) (sinTheta*dx),cosdy=(int) (cosTheta*dy),sindy=(int) (sinTheta*dy);
		//if(oldX[0]>=0) {
		if(firstPoint) {
			LineShape.drawLine(g, oldX[0], oldY[0], (int)(weightCenterX+cosdx-sindy), (int)(weightCenterY+sindx+cosdy),true,pointsOnFrame);
			LineShape.drawLine(g, oldX[1], oldY[1], (int)(weightCenterX+cosdx+sindy), (int)(weightCenterY+sindx-cosdy),true,pointsOnFrame);
			LineShape.drawLine(g, oldX[2], oldY[2], (int)(weightCenterX-cosdx-sindy), (int)(weightCenterY-sindx+cosdy),true,pointsOnFrame);
			LineShape.drawLine(g, oldX[3], oldY[3], (int)(weightCenterX-cosdx+sindy), (int)(weightCenterY-sindx-cosdy),true,pointsOnFrame);
		}
		oldX[0]=weightCenterX+cosdx-sindy;
		oldX[1]=weightCenterX+cosdx+sindy;
		oldX[2]=weightCenterX-cosdx-sindy;
		oldX[3]=weightCenterX-cosdx+sindy;
		oldY[0]=weightCenterY+sindx+cosdy;
		oldY[1]=weightCenterY+sindx-cosdy;
		oldY[2]=weightCenterY-sindx+cosdy;
		oldY[3]=weightCenterY-sindx-cosdy;
		
//		Pixel.setPixel(g, (int)(weightCenterX+cosdx-sindy), (int)(weightCenterY+sindx+cosdy));
//		Pixel.setPixel(g, (int)(weightCenterX+cosdx+sindy), (int)(weightCenterY+sindx-cosdy));
//		Pixel.setPixel(g, (int)(weightCenterX-cosdx-sindy), (int)(weightCenterY-sindx+cosdy));
//		Pixel.setPixel(g, (int)(weightCenterX-cosdx+sindy), (int)(weightCenterY-sindx-cosdy));
//		pointsOnFrame.add(new PointInt(weightCenterX+cosdx-sindy, weightCenterY+sindx+cosdy));
//		pointsOnFrame.add(new PointInt(weightCenterX+cosdx+sindy, weightCenterY+sindx-cosdy));
//		pointsOnFrame.add(new PointInt(weightCenterX-cosdx-sindy, weightCenterY-sindx+cosdy));
//		pointsOnFrame.add(new PointInt(weightCenterX-cosdx+sindy, weightCenterY-sindx-cosdy));
		
		//LineShape.drawLine(g, 0, weightCenterY+sindx+cosdy, 500, weightCenterY+sindx+cosdy, false, null);
	}
	
	private void fillEllipse(Graphics g) {
		if(pointsOnFrame.isEmpty())return;
		Collections.sort(pointsOnFrame, new Comparator<PointInt>() {

			@Override
			public int compare(PointInt o1, PointInt o2) {
				if(o1.getY()>o2.getY()) {
					return 1;
				}else if(o1.getY()==o2.getY()) {
					return o1.getX()-o2.getX();
				}else {
					return -1;
				}
			}
			
		});
		int startY=pointsOnFrame.get(0).getY();
		int endY=pointsOnFrame.get(pointsOnFrame.size()-1).getY();
		int startX=0,endX=0;
		int id=1;
		int midX=weightCenterX;
		int oldId=1;
		for(int y=startY+1;y<endY;y++) {
			startX=111110;
			endX=0;
			while(id<pointsOnFrame.size()&&pointsOnFrame.get(id).getY()<y)id++;
			oldId=id;
			PointInt tPointInt=pointsOnFrame.get(id);
//			id++;
//			while(id<pointsOnFrame.size()&&pointsOnFrame.get(id).getY()==y
//					&&(pointsOnFrame.get(id).getX()==pointsOnFrame.get(id-1).getX()+1
//					||pointsOnFrame.get(id).getX()==pointsOnFrame.get(id-1).getX())) {
//				//System.out.println(pointsOnFrame.get(id).getX());
//				id++;
//			}
//			startX=pointsOnFrame.get(id-1).getX();
//			endX=pointsOnFrame.get(id).getX();
//			System.out.println(startX+","+endX);
			
			while(id<pointsOnFrame.size()&&pointsOnFrame.get(id).getY()==y) {
				endX=Math.max(endX,pointsOnFrame.get(id).getX());
				startX=Math.min(startX,pointsOnFrame.get(id).getX());
				id++;
			}
			
//			midX=(pointsOnFrame.get(oldId).getX()+pointsOnFrame.get(id-1).getX())/2;
//			for(int i=oldId;i<id;i++) {
//				tPointInt=pointsOnFrame.get(i);
//				if(tPointInt.getX()<midX) {
//					startX=Math.max(startX, tPointInt.getX());
//				}else {
//					endX=Math.min(endX, tPointInt.getX());
//				}
//			}
			
//			midX=(startX+endX)/2;
//			startX=tPointInt.getX();
//			endX=pointsOnFrame.get(id-1).getX();
//			for(int i=oldId+1;i<id;i++) {
//				tPointInt=pointsOnFrame.get(i);
//				if(tPointInt.getX()<midX) {
//					startX=Math.max(startX, tPointInt.getX());
//				}else {
//					endX=Math.min(endX, tPointInt.getX());
//				}
//			}
			boolean flag=false;
			for(int x=startX+1;x<endX;x++) {
				flag=false;
				for(int i=oldId;i<id;i++) {
					if(pointsOnFrame.get(i).getX()==x)flag=true;
				}
				if(!flag&&LineShape.isInArea(x, y))
					Pixel.setPixel(g, x, y);
			}
		//	LineShape.drawLine(g, startX+1, y, endX-1, y, false, null);
		}
	}
	
	public EllipseShape(int x1,int y1, int x2,int y2,Color color, Color color2,PaintArea paintArea) {
		super();
		//selected=true;
		allowToRepaint=true;
		int tx1=Math.min(x1, x2),tx2=Math.max(x1, x2);
		int ty1=Math.min(y1, y2),ty2=Math.max(y1, y2);
		bx[0]=tx1;by[0]=ty1;
		bx[1]=tx2;by[1]=ty1;
		bx[2]=tx2;by[2]=ty2;
		bx[3]=tx1;by[3]=ty2;
		if(paintArea.window.shifted()) {
			isCircle=true;
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
		this.color = color;
		this.color2=color2;
		updateWeightCenter();
		updateVirtualCenter(weightCenterX, weightCenterY);
		for(int i=0;i<8;i++) {
			dots[i]=new FloatingIcon("dot.png");
		//	paintArea.add(dots[i]);
			dots[i].setVisible(false);
		}
		rotateArrow = new FloatingIcon("rotateArrow.png");
		rotateArrow.setVisible(false);
		rotateCenter=new FloatingIcon("center.png");
		rotateCenter.setVisible(true);
		greyLine=new GreyLine();
		greyLine.setVisible(false);
//		paintArea.add(rotateArrow,0);
//		paintArea.add(rotateCenter,0);
//		paintArea.add(greyLine,0);
		this.paintArea=paintArea;
		
		prepare();
	}
	
	@Override
	public void addIcons() {
		paintArea.add(rotateArrow,0);
		paintArea.add(rotateCenter,0);
		paintArea.add(greyLine,0);
		for(int i=0;i<8;i++) {
			paintArea.add(dots[i],0);
		}
	}
	
	@Override
	public void updateIcons() {
		paintArea.remove(rotateArrow);
		paintArea.remove(rotateCenter);
		paintArea.remove(greyLine);
		paintArea.add(rotateArrow,0);
		paintArea.add(rotateCenter,0);
		paintArea.add(greyLine,0);
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
//		System.out.println("paint");
//		if(!allowToRepaint)return;
//		allowToRepaint=false;
		updatePaintCoordinates();
		g.setColor(color);
		if(isCircle) {
			drawCircle(g);
			//drawEllipse(g);
			g.setColor(color2);
			if(color2!=null)
				fillEllipse(g);
		}
		else {
			drawEllipse(g);
			g.setColor(color2);
			if(color2!=null)
				fillEllipse(g);
		}
	}

	private void updatePaintCoordinates() {
		Point tPoint;
		for(int i=0;i<4;i++) {
			int dx=bx[i]-weightCenterX,dy=by[i]-weightCenterY;
			tPoint=getRotatedLocation(dx, dy,weightCenterX,weightCenterY,sinTheta,cosTheta);
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
		repaintRequest();
		repaint();
	}

	@Override
	public void updateOthers() {
	}

	@Override
	public void showSelectedStatus() {
		repaintRequest();
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
		rotateArrow.setBounds(rotateX-15, rotateY-15, 30, 30);
		rotateArrow.setVisible(true);
		
		rotateCenter.setBounds(virtualCenterX-15,virtualCenterY-15,30,30);
		rotateCenter.setVisible(true);
		greyLine.setLocations(rotateX,rotateY,virtualCenterX,virtualCenterY);
		greyLine.setVisible(true);
	}

	@Override
	public void unshowSelectedStatus() {
		for(FloatingIcon dot:dots)dot.setVisible(false);
		rotateArrow.setVisible(false);
		rotateCenter.setVisible(false);
		greyLine.setVisible(false);
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
		if (Math.abs(rotateX - x) <= 10 && Math.abs(rotateY - y) <= 10) {
			selectedPoint = 200;
			return 10086;
		}
		return Cursor.DEFAULT_CURSOR;
	}

	@Override
	public void modify(int endX, int endY) {
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
		case 200:
				double L=Math.sqrt((endX-virtualCenterX)*(endX-virtualCenterX)+(endY-virtualCenterY)*(endY-virtualCenterY));
				cosPhi=(virtualCenterY-endY)/L;
				sinPhi=(endX-virtualCenterX)/L;
				sinTheta=cSinTheta*cosPhi+cCosTheta*sinPhi;
				cosTheta=cCosTheta*cosPhi-cSinTheta*sinPhi;
				Point tPoint=getRotatedLocation(weightCenterX2-virtualCenterX, weightCenterY2-virtualCenterY,
							virtualCenterX, virtualCenterY, sinPhi, cosPhi);
				move(weightCenterX2, weightCenterY2, (int)tPoint.getX(), (int)tPoint.getY());

			beginMoveVirtualCenter=true;
			repaintRequest();
			repaint();
			showCenter();
			greyLine.setLocations(endX,endY,virtualCenterX,virtualCenterY);
			greyLine.setVisible(true);
			return;
		default:
			break;
		}
		isCircle=false;
		updateWeightCenter((x0+x1)/2, (y0+y1)/2);
		sinTheta=-sinTheta;
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
		repaintRequest();
		repaint();
	} 
	
	private Point getRotatedLocation(int dx,int dy) {
		Point ret=new Point();
		ret.setLocation(centerX3+cosTheta*dx-sinTheta*dy,centerY3+sinTheta*dx+cosTheta*dy);
		return ret;
	}
	
	private Point getRotatedLocation(int dx,int dy,int cx,int cy,double sinTheta,double cosTheta) {
		Point ret=new Point();
		ret.setLocation(cx+cosTheta*dx-sinTheta*dy,cy+sinTheta*dx+cosTheta*dy);
		return ret;
	}
	
	private int getRelativePos(int endX, int endY,int x,int y) {
		if(endX<=x&&endY<=y)return 0;
		if(endX>=x&&endY<=y)return 1;
		if(endX<=x&&endY>=y)return 2;
		if(endX>=x&&endY>=y)return 3;
		return -1;
	}
	
	@Override
	public void scale(int endX, int endY) {
		beginMoveVirtualCenter=true;
		int x0,y0,x1,y1;
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
		repaintRequest();
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
		cCosTheta=cosTheta;
		cSinTheta=sinTheta;
		centerX2=centerX;
		centerY2=centerY;
		weightCenterX2=weightCenterX;
		weightCenterY2=weightCenterY;
	}
	
	@Override
	public void delete(ListOfInt[][] board,int id) {
		super.delete(board, id);
		for(int i=0;i<8;i++) {
			paintArea.remove(dots[i]);
		}
		paintArea.remove(rotateArrow);
		paintArea.remove(rotateCenter);
		paintArea.remove(greyLine);
		paintArea.remove(this);
		paintArea.repaint();
	}
}
