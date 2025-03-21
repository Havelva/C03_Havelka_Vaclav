package model;

import transforms.*;

import java.util.ArrayList;

/**
 * Abstraktní třída Solid představuje základní strukturu pro 3D objekty.
 * Obsahuje buffery pro vrcholy, indexy a části objektu, a také informace
 * o modelové matici, středu objektu, a zda má objekt osu nebo texturu.
 */
public abstract class Solid {

    /**
     * Buffer pro vrcholy objektu.
     */
    protected final ArrayList<Vertex> vertexBuffer = new ArrayList<>();

    /**
     * Buffer pro indexy vrcholů objektu.
     */
    protected final ArrayList<Integer> indexBuffer = new ArrayList<>();

    /**
     * Buffer pro části objektu.
     */
    protected final ArrayList<Part> partBuffer = new ArrayList<>();

    /**
     * Určuje, zda má objekt osu.
     */
    protected boolean axis;

    /**
     * Určuje, zda má objekt texturu.
     */
    protected boolean texture;

    /**
     * Střed objektu.
     */
    private Vertex center;

    /**
     * Modelová matice objektu.
     */
    private Mat4 model = new Mat4Identity();

    /**
     * Vrací buffer vrcholů objektu.
     * 
     * @return ArrayList vrcholů objektu.
     */
    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    /**
     * Vrací buffer indexů vrcholů objektu.
     * 
     * @return ArrayList indexů vrcholů objektu.
     */
    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    /**
     * Vrací buffer částí objektu.
     * 
     * @return ArrayList částí objektu.
     */
    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    /**
     * Nastavuje modelovou matici objektu.
     * 
     * @param model Nová modelová matice.
     */
    public void setModel(Mat4 model) {
        this.model = model;
    }

    /**
     * Vrací modelovou matici objektu.
     * 
     * @return Modelová matice objektu.
     */
    public Mat4 getModel() {
        return model;
    }

    /**
     * Vrací, zda má objekt osu.
     * 
     * @return true, pokud má objekt osu, jinak false.
     */
    public boolean isAxis() {
        return axis;
    }

    /**
     * Vrací, zda má objekt texturu.
     * 
     * @return true, pokud má objekt texturu, jinak false.
     */
    public boolean withTexture() {
        return texture;
    }

    /**
     * Vrací střed objektu.
     * 
     * @return Střed objektu.
     */
    public Vertex getCenter() {
        return center;
    }

    /**
     * Inicializuje střed objektu na základě vrcholů v bufferu.
     * Vypočítá minimální a maximální souřadnice vrcholů a určí střed.
     */
    public void initCenter() {
        double maxX = vertexBuffer.get(0).getPos().getX();
        double maxY = vertexBuffer.get(0).getPos().getY();
        double maxZ = vertexBuffer.get(0).getPos().getZ();
        double minX = vertexBuffer.get(0).getPos().getX();
        double minY = vertexBuffer.get(0).getPos().getY();
        double minZ = vertexBuffer.get(0).getPos().getZ();

        for (Vertex v : vertexBuffer) {
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

    /**
     * Nastavuje střed objektu.
     * 
     * @param center Nový střed objektu.
     */
    public void setCenter(Vertex center) {
        this.center = center;
    }
}
