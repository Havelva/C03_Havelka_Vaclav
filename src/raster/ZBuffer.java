package raster;

import transforms.Col;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
        depthBuffer.clear();
    }

    public void setPixelWithZTest(int x, int y, double z, Col color){
        if(z < depthBuffer.getValue(x,y)) {
            imageBuffer.setValue(x,y,color);
            depthBuffer.setValue(x,y,z);
        }
    }

    public int getWidth() {return imageBuffer.getWidth();}
    public int getHeight() {return imageBuffer.getHeight();}

    public void clearDepth() {depthBuffer.clear();}
}
