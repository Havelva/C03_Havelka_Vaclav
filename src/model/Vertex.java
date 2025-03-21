package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Col color;
    // TODO: další atributy
    private final Vec2D uv;
    private final double one = 1;

    public Vertex(Point3D position) {
        this.position = position;
        this.color = new Col(0xffffff);
        this.uv = new Vec2D(0);
    }

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
        this.uv = new Vec2D(0);
    }

    public Vertex(Point3D position, Col color, Vec2D uv) {
        this.position = position;
        this.color = color;
        this.uv = uv;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    @Override
    public Vertex mul(double k) {
        return new Vertex(position.mul(k), color.mul(k), uv.mul(k));
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(
                position.add(v.getPosition()),
                color.add(v.getColor()),
                uv.add(v.getUv())
        );
    }

    public Vec2D getUv() {
        return uv;
    }

    public double getOne() {
        return one;
    }
}
