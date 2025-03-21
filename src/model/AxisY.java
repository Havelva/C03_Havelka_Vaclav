package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

/**
 * Třída AxisY reprezentuje osovou čáru v ose Y.
 * Dědí od třídy Solid.
 */
public class AxisY extends Solid {
    /**
     * Konstruktor třídy AxisY.
     * Inicializuje vertexBuffer a indexBuffer pro vykreslení osy Y.
     */
    public AxisY() {
        // Nastavení příznaku, že se jedná o osu
        axis = true;

        // Přidání vrcholů do vertexBuffer
        vertexBuffer.add(new Vertex(new Point3D(0, -0.1, 0), new Col(Color.green.getRGB()), new Vec2D(), 1)); // Spodní bod osy
        vertexBuffer.add(new Vertex(new Point3D(0, 0.5, 0), new Col(Color.green.getRGB()), new Vec2D(), 1));  // Horní bod osy
        vertexBuffer.add(new Vertex(new Point3D(0.05, 0.5, 0), new Col(Color.green.getRGB()), new Vec2D(), 1)); // Pravý bod šipky
        vertexBuffer.add(new Vertex(new Point3D(-0.05, 0.5, 0), new Col(Color.green.getRGB()), new Vec2D(), 1)); // Levý bod šipky
        vertexBuffer.add(new Vertex(new Point3D(0, 0.6, 0), new Col(Color.green.getRGB()), new Vec2D(), 1));  // Špička šipky

        // Přidání indexů do indexBuffer
        indexBuffer.add(0); // Index spodního bodu
        indexBuffer.add(1); // Index horního bodu
        indexBuffer.add(2); // Index pravého bodu šipky
        indexBuffer.add(3); // Index levého bodu šipky
        indexBuffer.add(4); // Index špičky šipky

        // Přidání částí do partBuffer
        partBuffer.add(new Part(TopologyType.LINES, 0, 1)); // Čára mezi spodním a horním bodem
        partBuffer.add(new Part(TopologyType.TRIANGLE, 2, 1)); // Trojúhelník tvořící šipku

        // Inicializace středu objektu
        initCenter();
    }
}
