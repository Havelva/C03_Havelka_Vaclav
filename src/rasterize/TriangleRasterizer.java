package rasterize;

import raster.ZBuffer;

import shader.Shader;
import model.Vertex;
import transforms.Point3D;
import utils.Lerp;

/**
 * Třída TriangleRasterizer slouží k rasterizaci trojúhelníků pomocí Z-bufferu.
 * Rasterizace probíhá ve dvou fázích: horní a dolní část trojúhelníku.
 * 
 * @param zBuffer ZBuffer pro ukládání hloubky a barev pixelů.
 * @param shader Shader pro výpočet barvy pixelů při použití textur.
 */
public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;
    private Shader shader;

    /**
     * Konstruktor třídy TriangleRasterizer.
     * 
     * @param zBuffer ZBuffer pro ukládání hloubky a barev pixelů.
     * @param shader Shader pro výpočet barvy pixelů při použití textur.
     */
    public TriangleRasterizer(ZBuffer zBuffer, Shader shader) {
        this.zBuffer = zBuffer;
        this.shader = shader;
        this.lerp = new Lerp<>();
    }

    /**
     * Metoda pro rasterizaci trojúhelníku.
     * 
     * @param v1 První vrchol trojúhelníku.
     * @param v2 Druhý vrchol trojúhelníku.
     * @param v3 Třetí vrchol trojúhelníku.
     * @param withTexture Určuje, zda se má použít texturování.
     */
    public void rasterize(Vertex v1, Vertex v2, Vertex v3, boolean withTexture) {
        // Transformace vrcholů do okna
        v1 = new Vertex(new Point3D(v1.getPos().dehomog().get()), v1.getColor(), v1.getUv(), v1.getOne());
        v2 = new Vertex(new Point3D(v2.getPos().dehomog().get()), v2.getColor(), v2.getUv(), v2.getOne());
        v3 = new Vertex(new Point3D(v3.getPos().dehomog().get()), v3.getColor(), v3.getUv(), v3.getOne());

        Vertex a = transformToWindow(v1);
        Vertex b = transformToWindow(v2);
        Vertex c = transformToWindow(v3);

        // Seřazení vrcholů podle y souřadnice
        if (a.getPos().getY() > b.getPos().getY()) {
            Vertex temp = a; a = b; b = temp;
        }
        if (b.getPos().getY() > c.getPos().getY()) {
            Vertex temp = b; b = c; c = temp;
        }
        if (a.getPos().getY() > b.getPos().getY()) {
            Vertex temp = a; a = b; b = temp;
        }

        // Rasterizace horní části trojúhelníku
        for (int y = Math.max(a.getIntY(), 0); y <= Math.min(b.getIntY(), zBuffer.getHeight()-1); y++) {
            double tAB = (y-a.getIntY())/(double)(b.getIntY()-a.getIntY());
            Vertex AB = lerp.lerp(a, b, tAB);
            double tAC = (y-a.getIntY())/(double)(c.getIntY()-a.getIntY());
            Vertex AC = lerp.lerp(a, c, tAC);

            if (AB.getIntX() > AC.getIntX()) {
                Vertex temp = AB; AB = AC; AC = temp;
            }

            // Rasterizace po x ose
            for (int x = Math.max(AB.getIntX(), 0); x <= Math.min(AC.getIntX(), zBuffer.getWidth()-1); x++) {
                double t = (x-AB.getIntX())/(double)(AC.getIntX()-AB.getIntX());
                Vertex finalVertex = lerp.lerp(AB, AC, t);
                if(withTexture) zBuffer.setPixelWithZTest(x,y,finalVertex.getPos().getZ(), shader.getColor(finalVertex));
                else zBuffer.setPixelWithZTest(x,y,finalVertex.getPos().getZ(), finalVertex.getColor());
            }
        }

        // Rasterizace dolní části trojúhelníku
        for (int y = Math.max(b.getIntY(), 0); y <= Math.min(c.getIntY(), zBuffer.getHeight()-1); y++) {
            double tBC = (y-b.getIntY())/(double)(c.getIntY()-b.getIntY());
            Vertex BC = lerp.lerp(b, c, tBC);
            double tAC = (y-a.getIntY())/(double)(c.getIntY()-a.getIntY());
            Vertex AC = lerp.lerp(a, c, tAC);

            if (BC.getIntX() > AC.getIntX()) {
                Vertex temp = BC; BC = AC; AC = temp;
            }

            for (int x = Math.max(BC.getIntX(), 0); x < Math.min(AC.getIntX(), zBuffer.getWidth()-1); x++) {
                double t = (x-BC.getIntX())/(double)(AC.getIntX()-BC.getIntX());
                Vertex finalVertex = lerp.lerp(BC, AC, t);
                if(withTexture) zBuffer.setPixelWithZTest(x,y,finalVertex.getPos().getZ(), shader.getColor(finalVertex));
                else zBuffer.setPixelWithZTest(x,y,finalVertex.getPos().getZ(), finalVertex.getColor());
            }
        }
    }

    /**
     * Metoda pro transformaci vrcholu do okna.
     * 
     * @param v Vrchol k transformaci.
     * @return Transformovaný vrchol.
     */
    private Vertex transformToWindow(Vertex v){
        return v.mulPos(new Point3D(1, -1, 1))
                .addPos(new Point3D(1, 1, 0))
                .mulPos(new Point3D((double) (zBuffer.getWidth() - 1) /2, (double) (zBuffer.getHeight() - 1) /2, 1));
    }
}