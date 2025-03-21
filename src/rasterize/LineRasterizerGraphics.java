package rasterize;

import model.Line;
import raster.Raster;
import raster.ImageBuffer;

import java.awt.*;

public class LineRasterizerGraphics extends LineRasterizer {

    public LineRasterizerGraphics(Raster raster) {
        super(raster);
    }

    public LineRasterizerGraphics(Raster raster, int color) {
        super(raster, color);
    }

    @Override
    public void drawLine(Line line) {
        Graphics g = ((ImageBuffer)raster).getGraphics();
        g.setColor(new Color(color));
        g.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }
}
