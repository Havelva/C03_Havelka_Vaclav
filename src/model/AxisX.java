package model;

// Importuje potřebné třídy z balíčku transforms a java.awt
import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

// Definice třídy AxisX, která dědí od třídy Solid
public class AxisX extends Solid {
    // Konstruktor třídy AxisX
    public AxisX() {
        // Nastaví příznak osy na true
        axis = true;
        
        // Přidává vrcholy do vertexBufferu
        vertexBuffer.add(new Vertex(new Point3D(-0.1, 0, 0), new Col(Color.red.getRGB()), new Vec2D(), 1)); // Vrchol na pozici (-0.1, 0, 0) s červenou barvou
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0, 0), new Col(Color.red.getRGB()), new Vec2D(), 1));  // Vrchol na pozici (0.5, 0, 0) s červenou barvou
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0.05, 0), new Col(Color.red.getRGB()), new Vec2D(), 1)); // Vrchol na pozici (0.5, 0.05, 0) s červenou barvou
        vertexBuffer.add(new Vertex(new Point3D(0.5, -0.05, 0), new Col(Color.red.getRGB()), new Vec2D(), 1)); // Vrchol na pozici (0.5, -0.05, 0) s červenou barvou
        vertexBuffer.add(new Vertex(new Point3D(0.6, 0, 0), new Col(Color.red.getRGB()), new Vec2D(), 1)); // Vrchol na pozici (0.6, 0, 0) s červenou barvou
        
        // Přidává indexy do indexBufferu
        indexBuffer.add(0); // Index prvního vrcholu
        indexBuffer.add(1); // Index druhého vrcholu
        indexBuffer.add(2); // Index třetího vrcholu
        indexBuffer.add(3); // Index čtvrtého vrcholu
        indexBuffer.add(4); // Index pátého vrcholu
        
        // Přidává části do partBufferu
        partBuffer.add(new Part(TopologyType.LINES, 0, 1)); // Přidává část typu LINES od indexu 0 s délkou 1
        partBuffer.add(new Part(TopologyType.TRIANGLE, 2, 1)); // Přidává část typu TRIANGLE od indexu 2 s délkou 1
        
        // Inicializuje střed objektu
        initCenter();
    }
}
