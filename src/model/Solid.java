package model;

import transforms.*;

import java.util.ArrayList;

public abstract class Solid {
    protected final ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    protected final ArrayList<Integer> indexBuffer = new ArrayList<>();
    protected final ArrayList<Part> partBuffer = new ArrayList<>();
    protected boolean axis;
    protected boolean texture;
    private Vertex center;
    private Mat4 model = new Mat4Identity();

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }
    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }
    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }
    public void setModel(Mat4 model) {
        this.model = model;
    }
    public Mat4 getModel() {
        return model;
    }
    public boolean isAxis() { return axis; }
    public boolean withTexture() { return texture; }
    public Vertex getCenter() {
        return center;
    }
    public void initCenter() {
        double maxX = vertexBuffer.get(0).getPos().getX();
        double maxY = vertexBuffer.get(0).getPos().getY();
        double maxZ = vertexBuffer.get(0).getPos().getZ();
        double minX = vertexBuffer.get(0).getPos().getX();
        double minY = vertexBuffer.get(0).getPos().getY();
        double minZ = vertexBuffer.get(0).getPos().getZ();

        for (Vertex v: vertexBuffer) {
            if (v.getPos().getX() < minX) {
                minX = v.getPos().getX();
            } else if (v.getPos().getX() > maxX) {
                maxX = v.getPos().getX();
            }
            if (v.getPos().getY() < minY) {
                minY = v.getPos().getY();
            } else if (v.getPos().getY() > maxY) {
                maxY = v.getPos().getY();
            }
            if (v.getPos().getZ() < minZ) {
                minZ = v.getPos().getZ();
            } else if (v.getPos().getZ() > maxZ) {
                maxZ = v.getPos().getZ();
            }
        }
        this.center = new Vertex(new Point3D(
                (maxX + minX) / 2,
                (maxY + minY) / 2,
                (maxZ + minZ) / 2),
                new Col(), new Vec2D(), 1
        );
    }

    public void setCenter(Vertex center){
        this.center = center;
    }
}
