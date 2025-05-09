package graphics.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Matrix4;

public class Lighting {

    private Matrix4 matrix;
    private Position position;

    public Lighting(GLAutoDrawable glAutoDrawable) {
        position = new Position();
        matrix = new Matrix4();
        init(glAutoDrawable);
    }

    public Matrix4 getMatrix() {
        return matrix;
    }

    private void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        float[] light_amb = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] light_dif = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] light_spec = {0.6f, 0.6f, 0.6f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_amb, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_dif, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_spec, 0);

        setDefaultPosition();
    }

    public void setDefaultPosition() {
        matrix.loadIdentity();
        matrix.rotate((float) Math.toRadians(45), 0, 0, 1);
    }

    public void calculatePosition(GL2 gl) {
        float[] output = new float[4];
        matrix.multVec(new float[]{5, 0, 1, 1.0f}, output);
        position.setByArray(output);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position.getFloatArray(true), 0);
    }
}
