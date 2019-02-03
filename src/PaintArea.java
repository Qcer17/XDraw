import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.*;

class ListOfInt extends LinkedList<Integer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	}

public class PaintArea extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PaintArea cThis=this;
	short cnt=0;
	MainWindow window;
	int startX,startY;
	boolean beginPaint=false,selected=false;
	Shape preShape=null;
	ListOfInt[][] board;
	ArrayList<Shape> shapes=new ArrayList<Shape>();
	Shape curSelectShape;
	int curSelectShapeId;
	int curCursorType=Cursor.DEFAULT_CURSOR;
	FloatingIcon rotateIcon;
	Cursor nullCursor;
	
	PolygonShape polygonShape=null;
	CurveShape curveShape=null;
	Cut cut=null;
	int cutId=-1;
	
	public PaintArea(MainWindow window,int w,int h) {
		
		LineShape.setAreaBound(0, 0, 1100, 790);
		
		setLayout(null);
		this.window=window;
		rotateIcon=new FloatingIcon("twoArrows.png");
		add(rotateIcon);
		rotateIcon.setSize(32,32);
		setPreferredSize(new Dimension(w,h));
		board=new ListOfInt[w][h];
		for(int i=0;i<w;i++)
			for(int j=0;j<h;j++)
				board[i][j]=new ListOfInt();
		setBackground(Color.WHITE);
		setBounds(90, 3, 1100, 790);
		initActions();
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(0, 0, new int[0], 0, 0));
		nullCursor=Toolkit.getDefaultToolkit().createCustomCursor(image,new Point(0, 0), null);
		
	}
	
	private void initActions() {
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				rotateIcon.setVisible(false);
				for(Shape shape:shapes)
					if(shape.selected) {
				//		shape.setFillingColor(window.nowColor2);
						shape.showSelectedStatus();
					//	System.out.println("sele");
				//		shape.setFillingColor(window.nowColor2);
					}
				if(beginPaint) {
					if(window.nowShape!=BasicShape.POLYGON&&window.nowShape!=BasicShape.CURVE) {
						beginPaint=false;
						curSelectShape=preShape;
						curSelectShapeId=cnt;
						selected=true;
						if(preShape!=null) {
							preShape.updateBoard(board,cnt++);
							preShape.showSelectedStatus();
							//System.out.println("SS");
							preShape.finished();
							shapes.add(preShape);
							updateIcons();
							if(preShape instanceof Cut) {
								cut=(Cut) preShape;
								cutId=cnt-1;
							}
							//System.out.println("SS");
						}
						preShape=null;
					}
					else if(window.nowShape==BasicShape.POLYGON&&polygonShape!=null) {
						polygonShape.unshowAssistDot();
						if(!polygonShape.firstLineDrawed) {
							polygonShape.updateFirstLine();
							polygonShape.addNumPoints();
							polygonShape.addNumPoints();
						}else {
							polygonShape.addNumPoints();
						}
					}else if(window.nowShape==BasicShape.CURVE&&curveShape!=null) {
						curveShape.unshowAssistDot();
						if(!curveShape.firstLineDrawed) {
							curveShape.updateFirstLine();
							curveShape.addNumPoints();
							curveShape.addNumPoints();
						}else {
							curveShape.addNumPoints();
						}
					}
				}else if(selected&&curSelectShape!=null) {
					curSelectShape.updateBoard(board,curSelectShapeId);
					curSelectShape.updateOthers();
//					if(curSelectShape.color2==null) {
//						curSelectShape.setFillingColor(window.nowColor2);
//					}
				}
			//	System.out.println("relea");
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(curSelectShape!=null)curSelectShape.prepare();
				startX=e.getX();
				startY=e.getY();
				if(curSelectShape!=null && curSelectShape.arrowType(startX, startY)!=Cursor.DEFAULT_CURSOR) {
					curCursorType=curSelectShape.arrowType(startX, startY);
				}else if(!board[startX][startY].isEmpty()) {
					curCursorType=Cursor.MOVE_CURSOR;
				}else {
					curCursorType=Cursor.DEFAULT_CURSOR;
				}
				if(((curSelectShape!=null && curSelectShape.arrowType(startX, startY)==Cursor.DEFAULT_CURSOR)
						||curSelectShape==null)&&board[startX][startY].isEmpty()) {
					for(Shape shape:shapes) {
						if(shape.selected)
							shape.cancelSelectedStatus();
					}
					beginPaint=true;
					curSelectShapeId=-1;
					curSelectShape=null;
					selected=false;
//					for(Shape shape:shapes) {
//						if()
//						shape.cancelSelectedStatus();
//					}
//					if(curSelectShape!=null)
//						curSelectShape.cancelSelectedStatus();
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("click");
				int startX=e.getX();
				int startY=e.getY();
				if(!board[startX][startY].isEmpty()){	
					selected=true;
					beginPaint=false;
					curSelectShapeId=board[startX][startY].getLast();
					curSelectShape=shapes.get(curSelectShapeId);
					for(Shape shape:shapes)
						if(shape!=curSelectShape) {
							if(shape.selected)
								shape.cancelSelectedStatus();
						}else {
							curSelectShape.showSelectedStatus();
						}
				}else { 
					//System.out.println("SS");
					if(polygonShape!=null){
						beginPaint=false;
						polygonShape.unshowLastPoint();
						polygonShape.showSelectedStatus();
						selected=true;
						curSelectShape=polygonShape;
						curSelectShapeId=shapes.size();
						for(Shape shape:shapes)
							if(shape!=curSelectShape) {
								if(shape.selected)
									shape.cancelSelectedStatus();
							}else {
								curSelectShape.showSelectedStatus();
							}
						shapes.add(polygonShape);
						updateIcons();
						//System.out.println("B");
						polygonShape.updateBoard(board,cnt++);
						//System.out.println("C");
						polygonShape.finished();
						polygonShape=null;
					}else if(curveShape!=null){
						beginPaint=false;
						curveShape.unshowLastPoint();
						curveShape.showSelectedStatus();
						selected=true;
						curSelectShape=curveShape;
						curSelectShapeId=shapes.size();
						for(Shape shape:shapes)
							if(shape!=curSelectShape) {
								if(shape.selected)
									shape.cancelSelectedStatus();
							}else {
								curSelectShape.showSelectedStatus();
							}
						//System.out.println("A");
						shapes.add(curveShape);
						updateIcons();
						//System.out.println("B");
						curveShape.updateBoard(board,cnt++);
						//System.out.println("C");
						curveShape.finished();
						curveShape=null;
					}
				}
			//	System.out.println("click finish");
			}
			
		});
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
			//	System.out.println("MOVED");
				int x=e.getX();
				int y=e.getY();
				int tCursor=curSelectShape!=null?curSelectShape.arrowType(x, y):0;
				if(curSelectShape!=null && tCursor!=Cursor.DEFAULT_CURSOR) {
					if(tCursor!=10086) {
						setCursor(Cursor.getPredefinedCursor(tCursor));
						rotateIcon.setVisible(false);
					}
					else{
						setCursor(nullCursor);
						rotateIcon.setLocation(x-16, y-16);
						rotateIcon.setVisible(true);
					}
				}else if(!board[x][y].isEmpty()) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					rotateIcon.setVisible(false);
				}else {
					setCursor(Cursor.getDefaultCursor());
					rotateIcon.setVisible(false);
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				//System.out.println("DRAGGED");
				if(curveShape==null&&polygonShape==null) {
					for(Shape shape:shapes) {
						if(shape.selected)
							shape.unshowSelectedStatus();
					}
				}
				int endX=e.getX(),endY=e.getY();
				if(beginPaint) {
					Shape newShape=null;
					switch(window.nowShape) {
					case LINE:
						newShape=new LineShape(startX,startY,endX,endY,window.nowColor,cThis);
						break;
					case CURVE:
						if(curveShape==null||!curveShape.firstLineDrawed) {					
							if(curveShape!=null) {
								curveShape.unshowLastPoint();
								remove(curveShape);
							}
							curveShape=new CurveShape(startX, startY, endX, endY, window.nowColor,window.nowColor2, cThis);
							add(curveShape,0);
						}else {
							if(curveShape.points.size()>curveShape.confirmedNumPoints)curveShape.removeLastPoint();
							int id=curveShape.getNearPointId(endX, endY);
							if(id>=0) {
								curveShape.showAssistDot(id);
								curveShape.addPoint((int)curveShape.points.get(id).getX(), 
										(int)curveShape.points.get(id).getY());
							}else {
								curveShape.unshowAssistDot();
								curveShape.addPoint(endX, endY);
							}
						}
						curveShape.showLastPoint();
						break;
					case OVAL:
						newShape=new EllipseShape(startX, startY, endX, endY, window.nowColor,window.nowColor2, cThis);
						break;
					case RECTANGLE:
						newShape=new RectangleShape(startX, startY, endX, endY, window.nowColor,window.nowColor2, cThis);
						break;
					case CUT:
						newShape=new Cut(startX, startY, endX, endY, cThis);
						break;
					case POLYGON:
						if(polygonShape==null||!polygonShape.firstLineDrawed) {					
							if(polygonShape!=null) {
								polygonShape.unshowLastPoint();
								remove(polygonShape);
							}
							polygonShape=new PolygonShape(startX, startY, endX, endY, window.nowColor,window.nowColor2, cThis);
							add(polygonShape,0);
						}else {
							if(polygonShape.points.size()>polygonShape.confirmedNumPoints)polygonShape.removeLastPoint();
							int id=polygonShape.getNearPointId(endX, endY);
							if(id>=0) {
								polygonShape.showAssistDot(id);
								polygonShape.addPoint((int)polygonShape.points.get(id).getX(), 
										(int)polygonShape.points.get(id).getY());
							}else {
								polygonShape.unshowAssistDot();
								polygonShape.addPoint(endX, endY);
							}
						}
						polygonShape.showLastPoint();
						break;
					default:break;
					}
					if(window.nowShape!=BasicShape.POLYGON&&window.nowShape!=BasicShape.CURVE) {
						if(beginPaint&&preShape!=null) {
							remove(preShape);
						}
						add(newShape,0);
						newShape.addIcons();
						preShape=newShape;
					}
					
					repaint();
					//System.out.println("REPAINT");
				}else if(selected) {
					if(curCursorType==Cursor.MOVE_CURSOR&&board[startX][startY].getLast()==curSelectShapeId)
						curSelectShape.move(startX, startY, endX, endY);
					else if(curCursorType!=Cursor.DEFAULT_CURSOR&&curSelectShape!=null) {
						if(!window.shifted()) {
							if(curCursorType==10086) {
								rotateIcon.setLocation(endX-16,endY-16);
								rotateIcon.setVisible(true);
							}
							curSelectShape.modify(endX, endY);
						}
						else {
							curSelectShape.scale(endX, endY);
						}
					}
				}
				//System.out.println("SSSSSSSSSSSSSSSSSs");
			}
		});
		
	}
	
	public void save(String pathname) {
		for(Shape shape:shapes) {
			if(shape.selected)
				shape.unshowSelectedStatus();
		}
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);  
        Graphics2D g2d = img.createGraphics();  
        printAll(g2d);  
        g2d.dispose();  
        try {
			ImageIO.write(img, "jpg", new File(pathname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(curSelectShape!=null)curSelectShape.showSelectedStatus();
	}
	
	private void updateIcons() {
		for(Shape shape:shapes) {
			shape.updateIcons();
		}
		remove(rotateIcon);
		add(rotateIcon,0);
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
	
	public void delete() {
		//System.out.println("DELE");
		if(curSelectShape!=null) {
			curSelectShape.delete(board, curSelectShapeId);
			curSelectShape=null;
			curSelectShapeId=-1;
		}
	}

}
