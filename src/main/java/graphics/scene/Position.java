package graphics.scene;

import graphics.object.Vector;

public class Position extends Vector {

    private float a;

    public Position() {

    }

    public Position(float x, float y, float z, float a) {
        super(x, y, z);
        this.a = a;
    }

    public float[] getFloatArray(boolean getAll) {
        if (getAll) {
            return new float[]{getX(), getY(), getZ(), getA()};
        } else {
            return new float[]{getX(), getY(), getZ(), getA()};
        }
    }

    public void setByArray(float[] array) {
        setX(array[0]);
        setY(array[1]);
        setZ(array[2]);
        if (array.length == 4)
            setA(array[3]);
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }
}
