import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

class Point3D{
	float x,y,z;
	public Point3D(float x,float y,float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public String toString() {
		return x+","+y+","+z;
	}
}

class Face{
	int n;
	LinkedList<Integer> points=new LinkedList<Integer>();
	public String toString() {
		String ret="";
		for(Integer i:points)ret=ret+i+" ";
		return ret;
	}
}

public class OFFLoader implements GLEventListener{

	int numVertices,numFaces,numEdges;
	float angle=0.0f;
	ArrayList<Point3D> points=new ArrayList<Point3D>();
	LinkedList<Face> faces=new LinkedList<Face>();
	
	public OFFLoader(String fileName) {
		final GLProfile proifile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(proifile);

        // 画布
        final GLCanvas glCanvas = new GLCanvas(capabilities);
        //LightingPolygon lightingPolygon = new LightingPolygon();
      //  OFFLoader offLoader=new OFFLoader();
        loadFile(fileName);
        glCanvas.addGLEventListener(this);
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
	
//	public static void main(String[] args) {
//		// 获取GL2的所有特性
//        final GLProfile proifile = GLProfile.get(GLProfile.GL2);
//        GLCapabilities capabilities = new GLCapabilities(proifile);
//
//        // 画布
//        final GLCanvas glCanvas = new GLCanvas(capabilities);
//        //LightingPolygon lightingPolygon = new LightingPolygon();
//        OFFLoader offLoader=new OFFLoader();
//        offLoader.loadFile(ShapeButton.class.getResource("/"+"bookshelf-p1056"+".off").getPath());
//        glCanvas.addGLEventListener(offLoader);
//        glCanvas.setSize(600, 600);
//
//        // 创建JFrame
//        final JFrame frame = new JFrame("3D");
//
//        frame.add(glCanvas);
//        frame.setSize(frame.getContentPane().getPreferredSize());
//        frame.setVisible(true);
//
//        // 设置动画
//        final FPSAnimator animator = new FPSAnimator(glCanvas, 300, true);
//        animator.start();
//        
//        
//
//	}

	public void loadFile(String fileName) {
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String[] tokens=null;
            String tempString = null;
            tempString = reader.readLine();
            if(!tempString.equals("OFF")) {
            	System.out.println("It's not a OFF file!");
            	return;
            }
            tempString=reader.readLine();
            tokens=tempString.split(" ");
            numVertices=Integer.parseInt(tokens[0]);
            numFaces=Integer.parseInt(tokens[1]);
            numEdges=Integer.parseInt(tokens[2]);
            int cnt=0;
            //System.out.println(numVertices+","+numFaces+","+numEdges);
            while ((tempString = reader.readLine()) != null) {
            	tokens=tempString.split(" ");
            	
            	if(cnt<numVertices) {
	            	points.add(new Point3D(Float.parseFloat(tokens[0]), 
	            			Float.parseFloat(tokens[1]), 
	            			Float.parseFloat(tokens[2])));
	            	cnt++;
	            	//System.out.println(points.get(points.size()-1));
            	}else {
            		Face tFace=new Face();
            		tFace.n=Integer.parseInt(tokens[0]);
            		for(int i=1;i<=tFace.n;i++) {
            			tFace.points.add(Integer.parseInt(tokens[i]));
            			//System.out.print(tokens[i]+" ");
            		}
            		
            		faces.add(tFace);
            		//System.out.println(tFace);
            	}
            	
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();


        gl.glColor3f(0.2f, 0.9f, 0.1f);

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glRotated(angle, 0.0f, 1.0f, 0.0f);

        // 光照
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);//为了在使用缩放的情况下仍然保证法向量为单位长度

        // 设置光源
        float[] ambientLight = { 1.0f, 1.0f, 1.0f, 0.0f };//环境光一般这么设置
        float[] diffuseLight = { 1.0f, 1.0f, 1.0f, 0.0f};//漫反射光一般设置成和环境光一致
        float[] specularLight={0.1f, 0.2f, 0.6f, 0.0f};
        float[] positionLight = { -0.9f, 0.9f, 0.9f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_SPECULAR, specularLight,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, positionLight, 0);

        // 设置光照模型
        float[] ambientModel = { 0.4f, 0.4f, 0.4f, 1.0f };
        float[] viewLmodel = { 0.0f, 0.0f, 0.0f, 1.0f };
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, ambientModel, 0);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, viewLmodel, 0);
        
//        gl.glBegin(GL2.GL_POLYGON);
//        gl.glNormal3f(0.0f,0.0f, 1.0f);//指定法向量
//        gl.glVertex3f(0f, 0.5f, 0f);
//        gl.glVertex3f(-0.5f, 0.2f, 0f);
//        gl.glVertex3f(-0.5f, -0.2f, 0f);
//        gl.glVertex3f(0f, -0.5f, 0f);
//        gl.glVertex3f(0f, 0.5f, 0f);
//        gl.glVertex3f(0.5f, 0.2f, 0f);
//        gl.glVertex3f(0.5f, -0.2f, 0f);
//        gl.glVertex3f(0.0f, -0.5f, 0.0f);
//        gl.glEnd();
        
        gl.glNormal3f(0.0f,0.0f, 1.0f);
        
        Point3D point3d;
        for(Face face:faces) {
        	gl.glBegin(GL2.GL_POLYGON);
        	for(Integer id:face.points) {
        		point3d=points.get(id);
        		gl.glVertex3f(point3d.x,point3d.y,point3d.z);
        	}
        	gl.glEnd();
        }
        
        
        gl.glFlush();
        angle+=1.0f;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
        // 设置背景颜色
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
    }

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

}
