package view;

import raster.ImageBuffer;
import transforms.Col;

import javax.swing.*;
import java.awt.*;

/**
 * Třída Panel rozšiřuje JPanel a slouží k vykreslování grafiky pomocí ImageBuffer.
 * Panel má pevně danou šířku a výšku a obsahuje metody pro změnu velikosti, vyčištění a získání rasteru.
 */
public class Panel extends JPanel {
    /**
     * Konstantní šířka panelu.
     */
    public static final int WIDTH = 800;

    /**
     * Konstantní výška panelu.
     */
    public static final int HEIGHT = 600;

    /**
     * Objekt ImageBuffer, který slouží jako raster pro vykreslování.
     */
    private ImageBuffer raster;

    /**
     * Konstruktor třídy Panel, který nastaví preferovanou velikost panelu
     * a inicializuje raster s pevně danou šířkou a výškou.
     */
    public Panel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        raster = new ImageBuffer(WIDTH, HEIGHT);
    }

    /**
     * Překreslí komponentu panelu a vykreslí obsah rasteru.
     * 
     * @param g Grafický kontext, který se používá pro vykreslování.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
    }

    /**
     * Změní velikost rasteru podle aktuální velikosti panelu.
     * Pokud je šířka nebo výška panelu menší než 1, metoda se ukončí.
     */
    public void resize() {
        if (this.getWidth() < 1 || this.getHeight() < 1)
            return;

        ImageBuffer newRaster = new ImageBuffer(this.getWidth(), this.getHeight());
        newRaster.setDefaultValue(new Col(Color.black.getRGB()));
        newRaster.draw(raster);
        raster = newRaster;
    }

    /**
     * Vrátí aktuální raster.
     * 
     * @return Aktuální raster.
     */
    public ImageBuffer getRaster() {
        return raster;
    }

    /**
     * Vyčistí obsah rasteru.
     */
    public void clear() {
        raster.clear();
    }

    /**
     * Vrátí šířku panelu.
     * 
     * @return Šířka panelu.
     */
    public int getWidth(){
        return WIDTH;
    }

    /**
     * Vrátí výšku panelu.
     * 
     * @return Výška panelu.
     */
    public int getHeight(){
        return HEIGHT;
    }
}
