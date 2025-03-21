package rasterize;


import model.Vertex;
import raster.ZBuffer;
import transforms.Point3D;
import utils.Lerp;


/**
 * Třída LineRasterizer slouží k rasterizaci čar pomocí Z-bufferu.
 * Používá interpolaci pro vykreslení čar mezi dvěma vrcholy.
 */
public class LineRasterizer {

    /**
     * Z-buffer pro ukládání hloubky a barvy pixelů.
     */
    ZBuffer zBuffer;

    /**
     * Lineární interpolátor pro vrcholy.
     */
    private final Lerp<Vertex> lerp;

    /**
     * Konstruktor třídy LineRasterizer.
     * 
     * @param zBuffer Z-buffer pro ukládání hloubky a barvy pixelů.
     */
    public LineRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.lerp = new Lerp<>();
    }

    /**
     * Rasterizuje čáru mezi dvěma vrcholy.
     * 
     * @param v1 První vrchol.
     * @param v2 Druhý vrchol.
     */
    public void rasterize(Vertex v1, Vertex v2) {
        // Transformace vrcholů do okna
        v1 = new Vertex(new Point3D(v1.getPos().dehomog().get()), v1.getColor(), v1.getUv(), v1.getOne());
        v2 = new Vertex(new Point3D(v2.getPos().dehomog().get()), v2.getColor(), v2.getUv(), v2.getOne());
        Vertex a = transformToWindow(v1);
        Vertex b = transformToWindow(v2);

        // Ujistěte se, že vrchol 'a' je níže než vrchol 'b'
        if (a.getPos().getY() > b.getPos().getY()) {
            Vertex temp = a; a = b; b = temp;
        }

        // Rozhodnutí, zda interpolovat podle osy X nebo Y
        if (a.getIntX() != b.getIntX()) {
            double k = (double) (b.getIntY() - a.getIntY()) / (b.getIntX() - a.getIntX());
            if (k > 1 || k < -1) interpolByY(a, b);
            else interpolByX(a, b);
        } else {
            interpolByY(a, b);
        }
    }

    /**
     * Interpoluje a rasterizuje čáru podle osy X.
     * 
     * @param a První vrchol.
     * @param b Druhý vrchol.
     */
    private void interpolByX(Vertex a, Vertex b) {
        if (a.getIntX() > b.getIntX()) {
            Vertex temp = a; a = b; b = temp;
        }
        for (int x = Math.max(a.getIntX(), 0); x <= Math.min(b.getIntX(), zBuffer.getWidth() - 1); x++) {
            double tAB = (x - a.getIntX()) / (double) (b.getIntX() - a.getIntX());
            Vertex AB = lerp.lerp(a, b, tAB);
            for (int y = Math.max(AB.getIntY(), 0); y <= Math.min(AB.getIntY(), zBuffer.getHeight() - 1); y++) {
                zBuffer.setPixelWithZTest(x, y, AB.getPos().getZ(), AB.getColor());
            }
        }
    }

    /**
     * Interpoluje a rasterizuje čáru podle osy Y.
     * 
     * @param a První vrchol.
     * @param b Druhý vrchol.
     */
    private void interpolByY(Vertex a, Vertex b) {
        if (a.getIntY() > b.getIntY()) {
            Vertex temp = a; a = b; b = temp;
        }
        for (int y = Math.max(a.getIntY(), 0); y <= Math.min(b.getIntY(), zBuffer.getHeight() - 1); y++) {
            double tAB = (y - a.getIntY()) / (double) (b.getIntY() - a.getIntY());
            Vertex AB = lerp.lerp(a, b, tAB);
            for (int x = Math.max(AB.getIntX(), 0); x <= Math.min(AB.getIntX(), zBuffer.getWidth() - 1); x++) {
                zBuffer.setPixelWithZTest(x, y, AB.getPos().getZ(), AB.getColor());
            }
        }
    }

    /**
     * Transformuje vrchol do okna.
     * 
     * @param v Vrchol k transformaci.
     * @return Transformovaný vrchol.
     */
    private Vertex transformToWindow(Vertex v) {
        return v.mulPos(new Point3D(1, -1, 1))
                .addPos(new Point3D(1, 1, 0))
                .mulPos(new Point3D((double) (zBuffer.getWidth() - 1) / 2, (double) (zBuffer.getHeight() - 1) / 2, 1));
    }
}
