package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

/**
 * Třída Vertex reprezentuje vrchol v 3D prostoru s barvou, texturovými souřadnicemi a dalšími atributy.
 * Implementuje rozhraní Vectorizable, což umožňuje operace jako násobení a sčítání vrcholů.
 */
public class Vertex implements Vectorizable<Vertex> {
    private Point3D pos; // Pozice vrcholu v 3D prostoru
    private final Col color; // Barva vrcholu
    private final Vec2D uv; // Texturové souřadnice vrcholu
    private final double one; // Další atribut, který může být použit pro různé účely

    /**
     * Konstruktor třídy Vertex.
     * @param pos Pozice vrcholu v 3D prostoru.
     * @param color Barva vrcholu.
     * @param uv Texturové souřadnice vrcholu.
     * @param one Další atribut.
     */
    public Vertex(Point3D pos, Col color, Vec2D uv, double one) {
        this.pos = pos;
        this.color = color;
        this.uv = uv;
        this.one = one;
    }

    /**
     * Násobí všechny atributy vrcholu daným koeficientem.
     * @param k Koeficient násobení.
     * @return Nový vrchol s násobenými atributy.
     */
    @Override
    public Vertex mul(double k) {
        return new Vertex(pos.mul(k), color.mul(k), uv.mul(k), one * k);
    }

    /**
     * Násobí pozici vrcholu daným bodem.
     * @param point Bod, kterým se násobí pozice vrcholu.
     * @return Nový vrchol s násobenou pozicí.
     */
    public Vertex mulPos(Point3D point) {
        return new Vertex(new Point3D(pos.getX() * point.getX(), pos.getY() * point.getY(), pos.getZ() * point.getZ()), color, uv, one);
    }

    /**
     * Sčítá atributy dvou vrcholů.
     * @param v Vrchol, který se přičítá.
     * @return Nový vrchol se sečtenými atributy.
     */
    @Override
    public Vertex add(Vertex v) {
        return new Vertex(pos.add(v.getPos()), color.add(v.getColor()), uv.add(v.getUv()), one + v.getOne());
    }

    /**
     * Přičítá daný bod k pozici vrcholu.
     * @param point Bod, který se přičítá k pozici vrcholu.
     * @return Nový vrchol s přičtenou pozicí.
     */
    public Vertex addPos(Point3D point) {
        return new Vertex(pos.add(point), color, uv, one);
    }

    /**
     * Vrací pozici vrcholu.
     * @return Pozice vrcholu.
     */
    public Point3D getPos() {
        return pos;
    }

    /**
     * Nastavuje pozici vrcholu.
     * @param pos Nová pozice vrcholu.
     */
    public void setPos(Point3D pos) {
        this.pos = pos;
    }

    /**
     * Vrací zaokrouhlenou X souřadnici pozice vrcholu.
     * @return Zaokrouhlená X souřadnice.
     */
    public int getIntX() {
        return (int) Math.round(pos.getX());
    }

    /**
     * Vrací zaokrouhlenou Y souřadnici pozice vrcholu.
     * @return Zaokrouhlená Y souřadnice.
     */
    public int getIntY() {
        return (int) Math.round(pos.getY());
    }

    /**
     * Vrací W souřadnici pozice vrcholu.
     * @return W souřadnice.
     */
    public double getW() {
        return pos.getW();
    }

    /**
     * Vrací barvu vrcholu.
     * @return Barva vrcholu.
     */
    public Col getColor() {
        return color;
    }

    /**
     * Vrací texturové souřadnice vrcholu.
     * @return Texturové souřadnice.
     */
    public Vec2D getUv() {
        return uv;
    }

    /**
     * Vrací hodnotu atributu one.
     * @return Hodnota atributu one.
     */
    public double getOne() {
        return one;
    }
}
