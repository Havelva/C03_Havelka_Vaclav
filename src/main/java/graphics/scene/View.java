package graphics.scene;

import com.jogamp.opengl.math.Matrix4;

public class View {

    private Matrix4 matrix;
    private Position position;
    private float zoom;

    public View() {
        setDefaultPosition();
    }

    public void setDefaultPosition() {
        matrix = new Matrix4();
        matrix.loadIdentity();
        zoom = 4.0f;
        position = new Position(zoom, 0, 0, 1);
    }

    public Matrix4 getMatrix() {
        return matrix;
    }

    public void calculatePosition() {
        float[] output = new float[4];
        matrix.multVec(new float[]{zoom, 0, 0, 1}, output);
        position.setByArray(output);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float i) {
        zoom += i;
    }

    public Position getPosition() {
        return position;
    }
}
