import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class JPGFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".jpg"))return true;
		return false;
	}
	
}

public class SaveButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFileChooser fileChooser=new JFileChooser();
	MainWindow window;
	
	public SaveButton(MainWindow window,String name,JPanel parent) {
		super(new ImageIcon(ShapeButton.class.getResource("/"+name+".png")));
		this.window=window;
		fileChooser.setFileFilter(new FileNameExtensionFilter("image(jpg)", "jpg"));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int value=fileChooser.showSaveDialog(parent);
				if(value==JFileChooser.APPROVE_OPTION) {
					window.save(fileChooser.getSelectedFile().getPath());
				}
			}
		});
	}
}
