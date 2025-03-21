package model;

// Importuje potřebné třídy z balíčku transforms a java.awt
import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

// Definice třídy AxisZ, která dědí z třídy Solid
public class AxisZ extends Solid {
    // Konstruktor třídy AxisZ
    public AxisZ() {
        // Nastaví příznak, že se jedná o osu
        axis = true;
        
        // Přidává vrcholy do vertexBufferu
        // Každý vrchol je definován pomocí třídy Vertex, která obsahuje bod v 3D prostoru, barvu, 2D vektor a váhu
        vertexBuffer.add(new Vertex(new Point3D(0, 0, -0.1), new Col(Color.blue.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0.5), new Col(Color.blue.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.05, 0, 0.5), new Col(Color.blue.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(-0.05, 0, 0.5), new Col(Color.blue.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0.6), new Col(Color.blue.getRGB()), new Vec2D(), 1));
        
        // Přidává indexy do indexBufferu, které určují pořadí vrcholů pro vykreslování
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);
        
        // Přidává části do partBufferu, které určují topologii a rozsah indexů pro vykreslování
        partBuffer.add(new Part(TopologyType.LINES, 0, 1)); // První část je čára mezi vrcholy 0 a 1
        partBuffer.add(new Part(TopologyType.TRIANGLE, 2, 1)); // Druhá část je trojúhelník mezi vrcholy 2, 3 a 4
    }
}
