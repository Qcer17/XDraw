import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BasicShape nowShape=BasicShape.LINE;
	Color nowColor=Color.BLACK,nowColor2=null;
	JButton colorButton,colorButton2;
	SaveButton saveButton;
	OpenOFFButton openOFFButton;
	PaintArea paintArea;
	boolean isFrameColor=true;
	private boolean shift=false;
	
	public MainWindow() {
		setTitle("XDraw");
		setSize(1200, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		
		initShapes();
		initColors();
		initPaintArea();
		initActions();
		
		setVisible(true);
		setFocusable(true);
	}
	
	private void initActions() {
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_SHIFT) {
					shift=false;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT) {
					shift=true;
				}
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					paintArea.delete();
				}
			}
		});
	}
	
	private void initShapes() {
		JPanel jPanelButtons = new JPanel(new GridLayout(12, 1, 5, 5));
		jPanelButtons.setBounds(6, 3, 80, 800);
		String[] iconNames= {"line","curve","rectangle","polygon","oval","cut"};
		for (int i = 0; i < iconNames.length; i++) {		
			ShapeButton shapeButton=new ShapeButton(this, iconNames[i]);
			jPanelButtons.add(shapeButton);
		}		
		saveButton=new SaveButton(this, "save",jPanelButtons);
		jPanelButtons.add(saveButton);
		openOFFButton=new OpenOFFButton("open",jPanelButtons);
		jPanelButtons.add(openOFFButton);
		add(jPanelButtons);
	}

	private void initColors() {
		Color[] colorArray = { null, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA,
				Color.PINK, Color.WHITE, Color.GRAY,Color.BLACK };
		
		JPanel jPanel = new JPanel(new GridLayout(1, colorArray.length, 3, 3));
		jPanel.setBounds(6,805, 540,40);
		ColorButton button0 = new ColorButton(this, null,30,30);
		jPanel.add(button0);
		for (int i = 1; i < colorArray.length; i++) {
			ColorButton button = new ColorButton(this, colorArray[i],30,30);
			jPanel.add(button);
		}
		
		add(jPanel);
		colorButton = new JButton();
		//colorButton.setEnabled(false);
		colorButton.setBackground(Color.BLACK);
		colorButton.setBounds(550, 800, 50, 50);
		colorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				isFrameColor=true;
			}
		});
		add(colorButton);
		colorButton2 = new JButton();
		//colorButton.setEnabled(false);
		//colorButton2.setBackground(Color.WHITE);
		colorButton2.setBackground(null);
		colorButton2.setBounds(605, 800, 50, 50);
		colorButton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				isFrameColor=false;
			}
		});
		add(colorButton2);
	}
	
	private void initPaintArea() {
		paintArea=new PaintArea(this, 1100, 790);
		add(paintArea);
	}
	
	public boolean shifted() {
		return shift;
	}
	
	public static void main(String[] args) {

		new MainWindow();
		
	}
	
	public void save(String path) {
		//System.out.println(path);
		paintArea.save(path+".jpg");
	}

}
