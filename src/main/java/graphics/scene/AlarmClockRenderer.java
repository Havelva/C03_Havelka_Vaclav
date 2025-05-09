package graphics.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import graphics.object.ObjectManager;
import graphics.utils.TextHUD;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class AlarmClockRenderer implements IRenderer {

    private final GLU glu;
    private final GLUT glut;

    private final TextHUD textHUD;
    private final View view;
    private Lighting lighting;
    private ObjectManager objectManager;

    private int width;
    private int height;
    private int ax, ay;

    private boolean perspective;
    private boolean actualTime;
    private boolean wireframe;
    private boolean info;
    private boolean ctrl;

    public AlarmClockRenderer() {
        glu = new GLU();
        glut = new GLUT();
        textHUD = new TextHUD("UbuntuMono-B");
        view = new View();
        objectManager = new ObjectManager();

        perspective = false;
        actualTime = true;
        wireframe = false;
        info = true;
        ctrl = false;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        glAutoDrawable.getAnimator().setUpdateFPSFrames(5, null);
        lighting = new Lighting(glAutoDrawable);
        objectManager.load(glAutoDrawable, "AlarmClock");
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        float fps = glAutoDrawable.getAnimator().getLastFPS();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (perspective)
            glu.gluPerspective(90, width / (float) height, 0.1f, 20.0f);
        else
            gl.glOrtho(-view.getZoom() * width / (float) height,
                    view.getZoom() * width / (float) height, -view.getZoom(), view.getZoom(), 0.1f, 20.0f);

        view.calculatePosition();
        glu.gluLookAt(view.getPosition().getX(), view.getPosition().getY(), view.getPosition().getZ(),
                0, 0, 0, 0, 0, 1);

        gl.glShadeModel(GL2.GL_SMOOTH);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        if (wireframe) {
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
            gl.glDisable(GL2.GL_LIGHTING);
            gl.glDisable(GL2.GL_LIGHT0);
            gl.glLineWidth(1.4f);
        } else {
            gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
            gl.glEnable(GL2.GL_LIGHTING);
            gl.glEnable(GL2.GL_LIGHT0);
            lighting.calculatePosition(gl);
        }
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
        gl.glFrontFace(GL2.GL_CCW);

        objectManager.renderAlarmClock(glAutoDrawable);

        if (info) {
            textHUD.display(glAutoDrawable, 4, height - 20, String.format("FPS: %.0f", fps));
            textHUD.display(glAutoDrawable, width - 170, height - 20, objectManager.getAnimator().getTime());
            textHUD.display(glAutoDrawable, 4, 8, (perspective) ? "Perspective" : "Orthographic");
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        glAutoDrawable.getGL().getGL2().glViewport(0, 0, width, height);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
                perspective = !perspective;
                break;
            case KeyEvent.VK_2:
                actualTime = !actualTime;
                objectManager.getAnimator().setActualTime(actualTime);
                break;
            case KeyEvent.VK_3:
                wireframe = !wireframe;
                break;
            case KeyEvent.VK_4:
                info = !info;
                break;
            case KeyEvent.VK_R:
                view.setDefaultPosition();
                lighting.setDefaultPosition();
                break;
            case KeyEvent.VK_CONTROL:
                if (!ctrl)
                    ctrl = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                ctrl = false;
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        ax = e.getX();
        ay = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - ax;
        int dy = e.getY() - ay;
        ax = e.getX();
        ay = e.getY();

        if (ctrl) {
            view.setZoom(dy / 100.0f);
        } else {
            if (SwingUtilities.isLeftMouseButton(e))
                view.getMatrix().rotate((float) Math.toRadians(dx / 2.0), 0, 0, 1);
            else
                lighting.getMatrix().rotate((float) Math.toRadians(dx / 2.0), 0, 0, 1);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
