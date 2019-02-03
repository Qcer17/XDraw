import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ShapeButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShapeButton(MainWindow window,String name) {
		super(new ImageIcon(ShapeButton.class.getResource("/"+name+".png")));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (name) {
				case "line":window.nowShape=BasicShape.LINE;break;
				case "oval":window.nowShape=BasicShape.OVAL;break;
				case "rectangle":window.nowShape=BasicShape.RECTANGLE;break;
				case "polygon":window.nowShape=BasicShape.POLYGON;break;
				case "curve":window.nowShape=BasicShape.CURVE;break;
				case "cut":window.nowShape=BasicShape.CUT;break;
				default:break;
				}
				if(window.paintArea.cut!=null) {
					window.paintArea.cut.delete(window.paintArea.board, window.paintArea.cutId);
					window.paintArea.cut=null;
					window.paintArea.cutId=-1;
				}
				window.requestFocusInWindow();
			}
		});
	}
}
