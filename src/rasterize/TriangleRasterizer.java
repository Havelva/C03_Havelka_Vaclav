package rasterize;

import model.Line;
import model.Vertex;
import raster.ZBuffer;
import shader.Shader;
import transforms.Col;
import transforms.Vec3D;
import utils.Lerp;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    // TODO: pouze pro debug
    private final LineRasterizer lineRasterizer;

    private Shader shader;

    public TriangleRasterizer(ZBuffer zBuffer, LineRasterizer lineRasterizer, Shader shader) {
        this.zBuffer = zBuffer;
        this.lineRasterizer = lineRasterizer;
        this.shader = shader;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c, Col color) {
        // TODO: pouze pro debug
        lineRasterizer.drawLine(new Line((int) a.getPosition().getX(), (int) a.getPosition().getY(), (int) b.getPosition().getX(), (int) b.getPosition().getY()));
        lineRasterizer.drawLine(new Line((int) a.getPosition().getX(), (int) a.getPosition().getY(), (int) c.getPosition().getX(), (int) c.getPosition().getY()));
        lineRasterizer.drawLine(new Line((int) b.getPosition().getX(), (int) b.getPosition().getY(), (int) c.getPosition().getX(), (int) c.getPosition().getY()));

        // TODO: transformace do okna obrazovky

        int xA = (int) Math.round(a.getPosition().getX());
        int yA = (int) Math.round(a.getPosition().getY());

        int xB = (int) Math.round(b.getPosition().getX());
        int yB = (int) Math.round(b.getPosition().getY());

        int xC = (int) Math.round(c.getPosition().getX());
        int yC = (int) Math.round(c.getPosition().getY());

        // TODO: seřadit vrcholy od ymin po ymax

        // První část trojúhelníku
        // TODO: ořezání
        Lerp<Vertex> lerp = new Lerp<>();
        for (int y = yA; y <= yB; y++) {
            double tAB = (y - yA) / (double) (yB - yA);
            //int xAB = (int) Math.round((1 - tAB) * xA + tAB * xB);
            // TODO: při interpolaci používat lerp, všude v projektu
            // TODO: instanci lerp nechceme tady
            Vertex ab = lerp.lerp(a, b, tAB);


            double tAC = (y - yA) / (double) (yC - yA);
            //int xAC = (int) Math.round((1 - tAC) * xA + tAC * xC);
            Vertex ac = lerp.lerp(a, c, tAC);

            // for cyklus od x do x
            // TODO: pozor xAC může být menší než xAB
            // TODO: ořezání
            for (int x = (int)ac.getPosition().getX(); x <= (int)ab.getPosition().getX(); x++) {
                double t = (x - ac.getPosition().getX()) / (ab.getPosition().getX() - ac.getPosition().getX());
                Vertex pixel = lerp.lerp(ac, ab, t);
                // TODo: dopočítat z
                zBuffer.setPixelWithZTest(x, y, pixel.getPosition().getZ(), shader.getColor(pixel));
            }

            // TODO: udělat druhu část trojúhelníku

        }
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
