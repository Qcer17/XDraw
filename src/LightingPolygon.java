
import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class LightingPolygon implements GLEventListener {
    private float angle = 0.0f;

    @Override
    public void display(GLAutoDrawable drawable) {
        // 绘制代码
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

        //设置物体材质
//      float[] ambientMat={1.0f,1.0f,1.0f,0.0f};
//      float[] diffuseMat={1.0f,1.0f,1.0f,0.0f};
//      float[] specularMat={1.0f,1.0f,1.0f,1.0f};
//      float[] emissionMat={0.3f,0.2f,0.2f,0.0f};
//      float shininess=100.0f;//镜面反射系数
//        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_AMBIENT,ambientMat,0);
//        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_DIFFUSE,diffuseMat,0);
//        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_SPECULAR,specularMat,0);
//        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_EMISSION,emissionMat,0);
//        gl.glMaterialf(GL2.GL_FRONT,GL2.GL_SHININESS,shininess);

        gl.glBegin(GL2.GL_POLYGON);
   //     gl.glNormal3f(0.0f,0.0f, 1.0f);//指定法向量
        gl.glVertex3f(0f, 0.5f, 0f);
        gl.glVertex3f(-0.5f, 0.2f, 0f);
        gl.glVertex3f(-0.5f, -0.2f, 0f);
        gl.glVertex3f(0f, -0.5f, 0f);
        gl.glVertex3f(0f, 0.5f, 0f);
        gl.glVertex3f(0.5f, 0.2f, 0f);
        gl.glVertex3f(0.5f, -0.2f, 0f);
        gl.glVertex3f(0.0f, -0.5f, 0.0f);
        gl.glEnd();
        
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(0.0f, 0.3f, 0.3f);
        gl.glVertex3f(-0.3f, 0.3f, 0f);
        gl.glEnd();
        gl.glFlush();
        
        //gl.glBegin (GL2.GL_LINES);
        //drawing the base
//        gl.glBegin (GL2.GL_LINES);
//        gl.glVertex3f(-0.50f, -0.50f, 0);
//        gl.glVertex3f(0.50f, -0.50f, 0);
//        gl.glEnd();
//        //drawing the right edge
//        gl.glBegin (GL2.GL_LINES);
//        gl.glVertex3f(0f, 0.50f, 0);
//        gl.glVertex3f(-0.50f, -0.50f, 0);
//        gl.glEnd();
//        //drawing the lft edge
//        gl.glBegin (GL2.GL_LINES);
//        gl.glVertex3f(0f, 0.50f, 0);
//        gl.glVertex3f(0.50f, -0.50f, 0);
//        gl.glEnd();
//        gl.glFlush();
        
        angle += 3.0f;

    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        //
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // 设置背景颜色
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
    }

    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // 获取GL2的所有特性
        final GLProfile proifile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(proifile);

        // 画布
        final GLCanvas glCanvas = new GLCanvas(capabilities);
        LightingPolygon lightingPolygon = new LightingPolygon();
        glCanvas.addGLEventListener(lightingPolygon);
        glCanvas.setSize(600, 600);

        // 创建JFrame
        final JFrame frame = new JFrame("带光照效果的旋转多边形");

        frame.getContentPane().add(glCanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        // 设置动画
        final FPSAnimator animator = new FPSAnimator(glCanvas, 300, true);
        animator.start();
    }

}
