package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

// Třída CubicTriangl dědí z třídy Solid
public class CubicTriangl extends Solid {
    // Konstruktor třídy CubicTriangl
    public CubicTriangl() {
        // Nastavení textury na true
        texture = true;

        // Přidání vrcholů do vertexBufferu
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0, 0.1), new Col(Color.yellow.getRGB()), new Vec2D(0, 0), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.6, 0, 0.1), new Col(Color.blue.getRGB()), new Vec2D(0.5, 0), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0, 0.2), new Col(Color.red.getRGB()), new Vec2D(0, 0.5), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.6, 0, 0.2), new Col(Color.magenta.getRGB()), new Vec2D(0.5, 0.5), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.55, 0.1, 0.15), new Col(Color.pink.getRGB()), new Vec2D(0.2, 0.2), 1));

        // Přidání indexů do indexBufferu pro definování trojúhelníků
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(4);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(4);
        indexBuffer.add(1);
        indexBuffer.add(3);
        indexBuffer.add(4);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);

        // Přidání části do partBufferu s topologií typu trojúhelník
        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 6));

        // Inicializace středu objektu
        initCenter();
    }
}
