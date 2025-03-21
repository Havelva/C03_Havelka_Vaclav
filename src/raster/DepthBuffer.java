package raster;

/**
 * Třída DepthBuffer implementuje rozhraní Raster pro práci s hloubkovým bufferem.
 * Hloubkový buffer je dvourozměrné pole hodnot typu double, které reprezentují
 * hloubku pixelů v rastrovém obrazu.
 */
public class DepthBuffer implements Raster<Double> {

    /**
     * Dvourozměrné pole pro ukládání hodnot hloubky.
     */
    private final double[][] buffer;

    /**
     * Šířka hloubkového bufferu.
     */
    private final int width;

    /**
     * Výška hloubkového bufferu.
     */
    private final int height;

    /**
     * Výchozí hodnota hloubky, která se použije při vymazání bufferu.
     */
    private final double defaultValue;

    /**
     * Konstruktor třídy DepthBuffer inicializuje šířku, výšku a buffer
     * s výchozí hodnotou hloubky 1.
     *
     * @param width  Šířka hloubkového bufferu.
     * @param height Výška hloubkového bufferu.
     */
    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new double[width][height];
        this.defaultValue = 1;
    }

    /**
     * Metoda clear vymaže hloubkový buffer a nastaví všechny hodnoty
     * na výchozí hodnotu.
     */
    @Override
    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buffer[i][j] = defaultValue;
            }
        }
    }

    /**
     * Metoda setDefaultValue je prázdná a není implementována.
     *
     * @param value Výchozí hodnota hloubky (nepoužito).
     */
    @Override
    public void setDefaultValue(Double value) {}

    /**
     * Metoda getWidth vrací šířku hloubkového bufferu.
     *
     * @return Šířka hloubkového bufferu.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Metoda getHeight vrací výšku hloubkového bufferu.
     *
     * @return Výška hloubkového bufferu.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Metoda getValue vrací hodnotu hloubky na zadaných souřadnicích.
     *
     * @param x Souřadnice x.
     * @param y Souřadnice y.
     * @return Hodnota hloubky na zadaných souřadnicích.
     */
    @Override
    public Double getValue(int x, int y) {
        return buffer[x][y];
    }

    /**
     * Metoda setValue nastavuje hodnotu hloubky na zadaných souřadnicích.
     *
     * @param x     Souřadnice x.
     * @param y     Souřadnice y.
     * @param color Hodnota hloubky, která má být nastavena.
     */
    @Override
    public void setValue(int x, int y, Double color) {
        buffer[x][y] = color;
    }
}
