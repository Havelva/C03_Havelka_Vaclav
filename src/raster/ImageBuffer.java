package raster;

import transforms.Col;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Třída ImageBuffer implementuje rozhraní Raster<Col> a poskytuje metody pro práci s rastrovým obrazem.
 * Umožňuje vytvářet, upravovat a vykreslovat obraz pomocí BufferedImage.
 */
public class ImageBuffer implements Raster<Col> {
    private final BufferedImage img; // BufferedImage pro ukládání obrazových dat
    private Col color; // Výchozí barva pro operace s obrazem

    /**
     * Konstruktor vytváří nový ImageBuffer s danou šířkou a výškou.
     *
     * @param width  šířka obrazu
     * @param height výška obrazu
     */
    public ImageBuffer(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Metoda repaint vykreslí aktuální obraz na danou grafiku.
     *
     * @param graphics grafický kontext, na který se má obraz vykreslit
     */
    public void repaint(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    /**
     * Metoda draw vykreslí obsah jiného ImageBuffer na tento ImageBuffer.
     *
     * @param raster ImageBuffer, který se má vykreslit
     */
    public void draw(ImageBuffer raster) {
        Graphics graphics = img.getGraphics();
        graphics.setColor(new Color(color.getRGB()));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(raster.img, 0, 0, null);
    }

    /**
     * Metoda getValue vrátí barvu pixelu na dané pozici.
     *
     * @param x horizontální souřadnice pixelu
     * @param y vertikální souřadnice pixelu
     * @return barva pixelu na dané pozici
     */
    @Override
    public Col getValue(int x, int y) {
        return new Col(img.getRGB(x, y));
    }

    /**
     * Metoda setValue nastaví barvu pixelu na dané pozici.
     *
     * @param x     horizontální souřadnice pixelu
     * @param y     vertikální souřadnice pixelu
     * @param color nová barva pixelu
     */
    @Override
    public void setValue(int x, int y, Col color) {
        img.setRGB(x, y, color.getRGB());
    }

    /**
     * Metoda clear vymaže obraz a nastaví ho na výchozí barvu.
     */
    @Override
    public void clear() {
        Graphics g = img.getGraphics();
        g.setColor(new Color(color.getRGB()));
        g.clearRect(0, 0, img.getWidth(), img.getHeight());
    }

    /**
     * Metoda setDefaultValue nastaví výchozí barvu pro operace s obrazem.
     *
     * @param color nová výchozí barva
     */
    @Override
    public void setDefaultValue(Col color) {
        this.color = color;
    }

    /**
     * Metoda getWidth vrátí šířku obrazu.
     *
     * @return šířka obrazu
     */
    @Override
    public int getWidth() {
        return img.getWidth();
    }

    /**
     * Metoda getHeight vrátí výšku obrazu.
     *
     * @return výška obrazu
     */
    @Override
    public int getHeight() {
        return img.getHeight();
    }
}

