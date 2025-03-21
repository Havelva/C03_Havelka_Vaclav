package render;

import model.Part;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.Solid;
import transforms.Col;

import java.util.List;

public class Renderer {
    private LineRasterizer lineRasterizer;
    private TriangleRasterizer triangleRasterizer;

    // TODO: view a proj matice

    public Renderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
    }

    public void renderSolid(Solid solid) {
        // TODO: MVP matice
        // TODO: transformovat vrcholy

        for (Part part : solid.getPartBuffer()) {
            switch (part.getType()) {
                case LINES:
                    // TODO: lines
                    break;
                case TRIANGLES:
                    // TODO: triangles
                    int start = part.getStart();
                    for(int i = 0; i < part.getCount(); i++){
                        int indexA = start;
                        int indexB = start + 1;
                        int indexC = start + 2;
                        start += 3;

                        Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexA));
                        Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexB));
                        Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexC));

                        //clipTriangle(a, b, c);

                        triangleRasterizer.rasterize(a, b, c, new Col(0xff0000));
                    }


                    break;
                // TODO: další primitiva
                default:
                    break;
            }
        }
    }


    // TODo: vymyslet, bude něco vracet nebo rasterizace uvnitř této metody?
    private void clipTriangle(Vertex a, Vertex b, Vertex c) {
        // TODO: fast clip

        // TODO: ořezání podle z
        float zMin = 0;
        // 1. seřadit vrcholy pod z od max po min. A = max
        if(a.getPosition().getZ() < zMin)
            return;

        if(b.getPosition().getZ() < zMin) {
            // TODO: interpolací spočítáme nový trojúhelník
            //triangleRasterizer.rasterize(a, ab, ac);
            return;
        }

        if(c.getPosition().getZ() < zMin) {
            // TODO: interpolací spočítáme 2 nové trojúhelníky a rasterizujeme
            return;
        }

        // TODO: Nic z předchozího neplatí, rasterizujeme původné trojúhelník

    }
}
