package raster;

import transforms.Col;

/**
 * Třída ZBuffer implementuje Z-buffer algoritmus pro správu hloubky pixelů v rastrovém obrazu.
 * Používá dva buffery: jeden pro barvy pixelů a druhý pro hloubku pixelů.
 */
public class ZBuffer {

    /**
     * Rastrový buffer pro ukládání barev pixelů.
     */
    private final Raster<Col> imageBuffer;

    /**
     * Rastrový buffer pro ukládání hloubky pixelů.
     */
    private final Raster<Double> depthBuffer;

    /**
     * Konstruktor třídy ZBuffer.
     * Inicializuje imageBuffer a depthBuffer na základě rozměrů imageBuffer.
     * @param imageBuffer Rastrový buffer pro ukládání barev pixelů.
     */
    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
        depthBuffer.clear();
    }

    /**
     * Nastaví pixel na dané souřadnici (x, y) s danou hloubkou z a barvou color,
     * pokud je hloubka z menší než aktuální hodnota v depthBuffer na této souřadnici.
     * @param x Souřadnice x pixelu.
     * @param y Souřadnice y pixelu.
     * @param z Hloubka pixelu.
     * @param color Barva pixelu.
     */
    public void setPixelWithZTest(int x, int y, double z, Col color) {
        if (z < depthBuffer.getValue(x, y)) {
            imageBuffer.setValue(x, y, color);
            depthBuffer.setValue(x, y, z);
        }
    }

    /**
     * Vrací šířku imageBuffer.
     * @return Šířka imageBuffer.
     */
    public int getWidth() {
        return imageBuffer.getWidth();
    }

    /**
     * Vrací výšku imageBuffer.
     * @return Výška imageBuffer.
     */
    public int getHeight() {
        return imageBuffer.getHeight();
    }

    /**
     * Vymaže depthBuffer, nastaví všechny hodnoty hloubky na výchozí hodnotu.
     */
    public void clearDepth() {
        depthBuffer.clear();
    }
}
