import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ColorButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Color color=Color.BLACK;
	public ColorButton(MainWindow window, Color color,int w,int h) {
		this.color=color;
		setBackground(color);
		setPreferredSize(new Dimension(w, h));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(window.isFrameColor) {
					if(color!=null) {
						window.nowColor=color;
						window.colorButton.setBackground(color);
					}
				}else {
					window.nowColor2=color;
					window.colorButton2.setBackground(color);
				}
				if(window.paintArea.curSelectShape!=null) {
					if(window.isFrameColor) {
						window.paintArea.curSelectShape.setFrameColor(color);
					}else {
						window.paintArea.curSelectShape.setFillingColor(color);
					}
				}
				window.requestFocusInWindow();
			}
		});
	}
}
