package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

/**
 * Třída Triangl reprezentuje trojúhelník jako pevné těleso.
 * 
 * Konstruktor třídy Triangl inicializuje vrcholový buffer (vertexBuffer) a indexový buffer (indexBuffer)
 * pro definici trojúhelníku s vrcholy v různých barvách.
 * 
 * Vrcholy jsou přidány do vrcholového bufferu s následujícími souřadnicemi a barvami:
 * - Vrchol 1: (0.3, 0, 0.1) - žlutá barva
 * - Vrchol 2: (0.4, 0, 0.1) - modrá barva
 * - Vrchol 3: (0.35, 0, 0.2) - červená barva
 * - Vrchol 4: (0.35, 0.1, 0.15) - růžová barva
 * 
 * Indexový buffer definuje trojúhelníky pomocí indexů vrcholů:
 * - Trojúhelník 1: vrcholy 0, 1, 2
 * - Trojúhelník 2: vrcholy 1, 2, 3
 * - Trojúhelník 3: vrcholy 0, 2, 3
 * - Trojúhelník 4: vrcholy 0, 1, 3
 * 
 * Part buffer obsahuje část s topologií typu TRIANGLE, která začíná na indexu 0 a obsahuje 4 trojúhelníky.
 * 
 * Metoda initCenter() je volána pro inicializaci středu trojúhelníku.
 */
public class Triangl extends Solid{
    public Triangl(){
        vertexBuffer.add(new Vertex(new Point3D(0.3, 0, 0.1), new Col(Color.yellow.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.4, 0, 0.1), new Col(Color.blue.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.35, 0, 0.2), new Col(Color.red.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.35, 0.1, 0.15), new Col(Color.pink.getRGB()), new Vec2D(), 1));
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(3);
        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 4));
        initCenter();
    }
}
