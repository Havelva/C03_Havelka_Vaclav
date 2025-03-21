package raster;

import transforms.Col;

import java.awt.*;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        // TODO: implementovat

        //if(depthBuffer.getValue(x, y).isPresent())
        //double zDepthBuffer = depthBuffer.getValue(x, y).get();

        // 1. Načteme z hodnotu z depth bufferu
        // 2. Podmínka, jestli nové z < než z v depthbufferu
        // 3. Pokud ano
            // Přepíšeme hodnotuv  depth bufferu
            // Obarvíme pixel
        // 4. Pokud ne
            // Nic neděláme

        imageBuffer.setValue(x, y, color);
    }
}
