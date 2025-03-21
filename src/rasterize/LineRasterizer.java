package rasterize;


import model.Vertex;
import raster.ZBuffer;
import shader.Shader;
import transforms.Point3D;
import transforms.Vec3D;
import utils.Lerp;
import java.awt.*;


public class LineRasterizer{
    ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;

    public LineRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.lerp = new Lerp<>();
    }


    public void rasterize(Vertex v1, Vertex v2) {
        v1 = new Vertex(new Point3D(v1.getPos().dehomog().get()), v1.getColor(), v1.getUv(), v1.getOne());
        v2 = new Vertex(new Point3D(v2.getPos().dehomog().get()), v2.getColor(), v2.getUv(), v2.getOne());
        Vertex a = transformToWindow(v1);
        Vertex b = transformToWindow(v2);
        if (a.getPos().getY() > b.getPos().getY()) {
            Vertex temp = a; a = b; b = temp;
        }
        if (a.getIntX() != b.getIntX()){
            double k = (double) (b.getIntY() - a.getIntY()) /(b.getIntX()-a.getIntX());
            if (k > 1 || k < -1) interpolByY(a, b);
            else interpolByX(a, b);
        }
        else interpolByY(a, b);


    }

    private void interpolByX(Vertex a, Vertex b){
        if (a.getIntX() > b.getIntX()) {
            Vertex temp = a; a = b; b = temp;
        }
        for (int x = Math.max(a.getIntX(), 0); x <= Math.min(b.getIntX(), zBuffer.getWidth()-1); x++) {
            double tAB = (x-a.getIntX())/(double)(b.getIntX()-a.getIntX());
            Vertex AB = lerp.lerp(a, b, tAB);
            for(int y = Math.max(AB.getIntY(),0); y <= Math.min(AB.getIntY(),zBuffer.getHeight()-1); y++){
                zBuffer.setPixelWithZTest(x,y,AB.getPos().getZ(),AB.getColor());
            }
        }
    }
    private void interpolByY(Vertex a, Vertex b){
        if (a.getIntY() > b.getIntY()) {
            Vertex temp = a; a = b; b = temp;
        }
        for (int y = Math.max(a.getIntY(), 0); y <= Math.min(b.getIntY(), zBuffer.getHeight()-1); y++) {
            double tAB = (y-a.getIntY())/(double)(b.getIntY()-a.getIntY());
            Vertex AB = lerp.lerp(a, b, tAB);
            for(int x = Math.max(AB.getIntX(),0); x<= Math.min(AB.getIntX(),zBuffer.getWidth()-1);x++){
                zBuffer.setPixelWithZTest(x,y,AB.getPos().getZ(),AB.getColor());
            }
        }
    }
    private Vertex transformToWindow(Vertex v){
        return v.mulPos(new Point3D(1, -1, 1))
                .addPos(new Point3D(1, 1, 0))
                .mulPos(new Point3D((double) (zBuffer.getWidth() - 1) /2, (double) (zBuffer.getHeight() - 1) /2, 1));
    }


}
