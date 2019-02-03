import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Shape extends JPanel{
	private static final long serialVersionUID = 1L;
	public boolean selected=false;
	public boolean started=true;
	protected Color color,color2;
	ArrayList<PointInt> pointsOnFrame=new ArrayList<PointInt>();
	ArrayList<PointInt> pointsOnFrameLast=new ArrayList<PointInt>();
	public Shape() {
		setLayout(null);
		setBounds(0,0,1100,790);
		setBackground(null);
		setOpaque(false);
	}
	public boolean isWithin(int endX,int endY) {return false;}
	public int arrowType(int x, int y) {return 0;}
	public void delete(ListOfInt[][] board,int id) {
		pointsOnFrame.clear();
		updateBoard(board, id);
	}
	public void prepare() {}
	public void move(int x1, int y1, int x2, int y2) {}
	public void modify(int endX,int endY) {}
	public void scale(int endX,int endY) {}
	public void rotate() {}
	public void updateOthers() {}
	public void showSelectedStatus() {}
	public void unshowSelectedStatus() {}
	public void updateIcons() {}
	public void addIcons() {}
	public void cancelSelectedStatus() {
		selected=false;
		unshowSelectedStatus();
	}
	public void finished() {
		started=false;
	}
	public void updateBoard(ListOfInt[][] board,int id) {
		Integer target=id;
		for(PointInt point:pointsOnFrameLast) {
			int x=point.getX();
			//for(int x=Math.max(point.getX()-14, 0);x<=Math.min(point.getX()+14, board.length-1);x++)
				for(int y=Math.max(point.getY()-20, 0);y<=Math.min(point.getY()+20, board[0].length-1);y++) {
					board[x][y].remove(target);
				}
		}
		for(PointInt point:pointsOnFrame) {
			int x=point.getX();
			//for(int x=Math.max(point.getX()-14, 0);x<=Math.min(point.getX()+14, board.length-1);x++)
				for(int y=Math.max(point.getY()-20, 0);y<=Math.min(point.getY()+20, board[0].length-1);y++) {
					board[x][y].addLast(target);
				}
		}
		pointsOnFrameLast.clear();
		for(PointInt point:pointsOnFrame) {
			pointsOnFrameLast.add(new PointInt(point.getX(), point.getY()));
		}
	}
	
//	public void updateBoard(ListOfInt[][] board,int id) {
//		BufferedImage image=convertToImage();
//		Integer target=id;
//		for(int i=0;i<board.length;i++) {
//			for(int j=0;j<board[0].length;j++){
//				if(board[i][j].contains(target)&&image.getRGB(i, j)==0) {
//					for(int x=Math.max(i-14, 0);x<=Math.min(i+14, board.length-1);x++)
//						for(int y=Math.max(j-14, 0);y<=Math.min(j+14, board[0].length-1);y++) {
//							board[x][y].remove(target);
//						}
//				}
//			}
//		}
//		for(int i=0;i<board.length;i++) {
//			for(int j=0;j<board[0].length;j++){
//				if(image.getRGB(i, j)!=0) {
//					for(int x=Math.max(i-14, 0);x<=Math.min(i+14, board.length-1);x++)
//						for(int y=Math.max(j-14, 0);y<=Math.min(j+14, board[0].length-1);y++) {
//							board[x][y].addLast(target);
//						}
//				}
//			}
//		}
//	}
	protected BufferedImage convertToImage() {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);  
        Graphics2D g2d = img.createGraphics();  
        printAll(g2d);  
        g2d.dispose();  
        return img;  
	}

	public void setFrameColor(Color color) {
		this.color=color;
		repaint();
	}
	
	public void setFillingColor(Color color) {
		this.color2=color;
		repaint();
	}
}
