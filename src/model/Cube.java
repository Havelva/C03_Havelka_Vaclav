package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

public class Cube extends Solid {
    // Konstruktor třídy Cube
    public Cube() {
        // Přidání vrcholů do vertexBufferu
        vertexBuffer.add(new Vertex(new Point3D(0.1, 0, 0.1), new Col(Color.magenta.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.2, 0, 0.1), new Col(Color.blue.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.2, 0, 0.2), new Col(Color.red.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.1, 0, 0.2), new Col(Color.pink.getRGB()), new Vec2D(), 1));

        vertexBuffer.add(new Vertex(new Point3D(0.1, 0.1, 0.1), new Col(Color.yellow.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.2, 0.1, 0.1), new Col(Color.green.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.2, 0.1, 0.2), new Col(Color.cyan.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.1, 0.1, 0.2), new Col(Color.orange.getRGB()), new Vec2D(), 1));

        // Přidání indexů do indexBufferu pro definici trojúhelníků tvořících stěny krychle
        // základna dole
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(3);
        // základna nahoře
        indexBuffer.add(4);
        indexBuffer.add(5);
        indexBuffer.add(6);
        indexBuffer.add(4);
        indexBuffer.add(6);
        indexBuffer.add(7);
        // pravá stěna
        indexBuffer.add(1);
        indexBuffer.add(5);
        indexBuffer.add(6);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(6);
        // levá stěna
        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(7);
        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(7);
        // přední stěna
        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(5);
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(5);
        // zadní stěna
        indexBuffer.add(2);
        indexBuffer.add(6);
        indexBuffer.add(7);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(7);

        // Přidání části do partBufferu, která definuje topologii (trojúhelníky) a počet prvků
        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 12));
        // Inicializace středu krychle
        initCenter();
    }
}
