import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class OpenOFFButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFileChooser fileChooser=new JFileChooser();
	MainWindow window;
	OFFLoader offLoader;
	
	public OpenOFFButton(String name,JPanel parent) {
		super(new ImageIcon(ShapeButton.class.getResource("/"+name+".png")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("3D(off)", "off"));
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int value=fileChooser.showOpenDialog(parent);
				if(value==JFileChooser.APPROVE_OPTION) {
					offLoader=new OFFLoader(fileChooser.getSelectedFile().getPath());
				}
			}
		});
	}
	
	private void myMain() {
		final GLProfile proifile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(proifile);

        // 画布
        final GLCanvas glCanvas = new GLCanvas(capabilities);
        //LightingPolygon lightingPolygon = new LightingPolygon();
        //OFFLoader offLoader=new OFFLoader();
        //offLoader.loadFile(ShapeButton.class.getResource("/"+"bookshelf-p1056"+".off").getPath());
        glCanvas.addGLEventListener(offLoader);
        glCanvas.setSize(600, 600);

        // 创建JFrame
        final JFrame frame = new JFrame("3D");

        frame.add(glCanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        // 设置动画
        final FPSAnimator animator = new FPSAnimator(glCanvas, 300, true);
        animator.start();
        
	}
}
