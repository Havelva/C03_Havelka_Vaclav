package rasterize;

import model.Line;
import raster.Raster;


public abstract class LineRasterizer {
    // TODO: pozor musíme používat zBuffer
    protected final Raster raster;
    protected int color;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
        this.color = 0xff0000;
    }

    public LineRasterizer(Raster raster, int color) {
        this.raster = raster;
        this.color = color;
    }

    public void drawLine(Line line) {
        System.out.println("Draw line");
    }

    public void setColor(int color) {
        this.color = color;
    }
}
