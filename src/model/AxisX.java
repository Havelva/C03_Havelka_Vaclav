package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

public class AxisX extends Solid{
    public AxisX(){
        axis = true;
        vertexBuffer.add(new Vertex(new Point3D(-0.1, 0, 0), new Col(Color.red.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0, 0), new Col(Color.red.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0.05, 0), new Col(Color.red.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.5, -0.05, 0), new Col(Color.red.getRGB()), new Vec2D(), 1));
        vertexBuffer.add(new Vertex(new Point3D(0.6, 0, 0), new Col(Color.red.getRGB()), new Vec2D(), 1));
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);
        partBuffer.add(new Part(TopologyType.LINES, 0, 1));
        partBuffer.add(new Part(TopologyType.TRIANGLE, 2, 1));
        initCenter();
    }
}
