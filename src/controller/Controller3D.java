package controller;

import rasterize.LineRasterizer;
import raster.Raster;
import rasterize.TriangleRasterizer;
import raster.ZBuffer;
import render.Renderer;
import model.*;
import transforms.*;
import view.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Třída Controller3D implementuje rozhraní Controller a slouží k ovládání 3D scény.
 */
public class Controller3D implements Controller {
    private final Panel panel; // Panel pro vykreslování
    private ZBuffer zBuffer; // Z-buffer pro správu hloubky
    private TriangleRasterizer triangleRasterizer; // Rasterizátor trojúhelníků

    private LineRasterizer lineRasterizer; // Rasterizátor čar
    private Renderer renderer; // Renderer pro vykreslování
    private BufferedImage texture; // Textura pro vykreslování
    private Camera camera; // Kamera pro pohled na scénu
    private ArrayList<Vertex> centers; // Seznam středů objektů
    private ArrayList<Solid> solids; // Seznam objektů ve scéně
    private int selectedSolidIndex; // Index vybraného objektu
    private int mouseDraggedStartX, mouseDraggedStartY; // Počáteční pozice myši při tažení
    private Mat4 proj; // Projekční matice

    /**
     * Konstruktor třídy Controller3D.
     * @param panel Panel pro vykreslování
     */
    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster()); // Inicializace objektů
        initListeners(); // Inicializace posluchačů událostí
        redraw(); // Překreslení scény
    }

    /**
     * Inicializace objektů ve scéně.
     * @param raster Raster pro vykreslování
     */
    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(Color.black.getRGB())); // Nastavení výchozí barvy rasteru
        zBuffer = new ZBuffer(raster); // Inicializace Z-bufferu
        try {
            texture = ImageIO.read(new File("./res/texture.jpg")); // Načtení textury
        } catch (IOException e) {
            throw new RuntimeException(e); // Ošetření výjimky při načítání textury
        }
        triangleRasterizer = new TriangleRasterizer(zBuffer, v -> {
            Vec2D vec = v.getUv().mul(1 / v.getOne()); // Výpočet UV souřadnic
            int x = (int) (vec.getX() * texture.getWidth()); // Výpočet X souřadnice textury
            int y = (int) (vec.getY() * texture.getHeight()); // Výpočet Y souřadnice textury
            return new Col(texture.getRGB(x, y)); // Vrácení barvy z textury
        });

        lineRasterizer = new LineRasterizer(zBuffer); // Inicializace rasterizátoru čar
        renderer = new Renderer(triangleRasterizer, lineRasterizer, panel); // Inicializace rendereru

        Vec3D pos = new Vec3D(0, 0, 0); // Počáteční pozice kamery
        camera = new Camera(pos,
                Math.toRadians(90),
                Math.toRadians(-100),
                1, false
        );
        proj = new Mat4PerspRH(
                Math.PI/4,
                (double) raster.getHeight() / raster.getWidth(),
                0.2, 20
        );
        solids = new ArrayList<>(); // Inicializace seznamu objektů
        Cube cube = new Cube(); // Vytvoření krychle
        Triangl triangl = new Triangl(); // Vytvoření trojúhelníku
        CubicTriangl cubicTriangl = new CubicTriangl(); // Vytvoření kubického trojúhelníku
        AxisX axisX = new AxisX(); // Vytvoření osy X
        AxisY axisY = new AxisY(); // Vytvoření osy Y
        AxisZ axisZ = new AxisZ(); // Vytvoření osy Z
        solids.add(cube); // Přidání krychle do seznamu objektů
        solids.add(triangl); // Přidání trojúhelníku do seznamu objektů
        solids.add(cubicTriangl); // Přidání kubického trojúhelníku do seznamu objektů
        solids.add(axisX); // Přidání osy X do seznamu objektů
        solids.add(axisY); // Přidání osy Y do seznamu objektů
        solids.add(axisZ); // Přidání osy Z do seznamu objektů
    }

    /**
     * Inicializace posluchačů událostí.
     */
    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize(); // Změna velikosti panelu
                initObjects(panel.getRaster()); // Inicializace objektů při změně velikosti
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_A){
                    // Pohyb kamery doleva
                    camera = camera.left(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_D) {
                    // Pohyb kamery doprava
                    camera = camera.right(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_W) {
                    // Pohyb kamery nahoru
                    camera = camera.up(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    // Pohyb kamery dolů
                    camera = camera.down(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    // Přiblížení kamery
                    camera = camera.forward(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_E) {
                    // Oddálení kamery
                    camera = camera.backward(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    // Rotace kamery nahoru
                    camera = camera.addZenith(Math.toRadians(5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // Rotace kamery doleva
                    camera = camera.addAzimuth(Math.toRadians(5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    // Rotace kamery dolů
                    camera = camera.addZenith(Math.toRadians(-5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // Rotace kamery doprava
                    camera = camera.addAzimuth(Math.toRadians(-5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_I) {
                    // Pohyb objektu nahoru
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, 0.05, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_J) {
                    // Pohyb objektu doleva
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(-0.05, 0, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_L) {
                    // Pohyb objektu doprava
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0.05, 0, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_K) {
                    // Pohyb objektu dolů
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, -0.05, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_U) {
                    // Pohyb objektu dopředu
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, 0, 0.05)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_O) {
                    // Pohyb objektu dozadu
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, 0, -0.05)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_1) {
                    // Rotace objektu kolem osy Y doleva
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotY(Math.toRadians(-5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_2) {
                    // Rotace objektu kolem osy Y doprava
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotY(Math.toRadians(5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_3) {
                    // Rotace objektu kolem osy X dozadu
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotX(Math.toRadians(-5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_4) {
                    // Rotace objektu kolem osy X dopředu
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotX(Math.toRadians(5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_5) {
                    // Rotace objektu kolem osy Z doleva
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotZ(Math.toRadians(5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_6) {
                    // Rotace objektu kolem osy Z doprava
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotZ(Math.toRadians(-5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_7) {
                    // Přiblížení objektu
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Scale(1.1)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_8) {
                    // Oddálení objektu
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Scale(0.9)));
                }
                redraw(); // Překreslení scény
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDraggedStartX = e.getX(); // Uložení počáteční X pozice myši
                mouseDraggedStartY = e.getY(); // Uložení počáteční Y pozice myši
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                double dX = e.getX() - mouseDraggedStartX; // Výpočet rozdílu X pozice myši
                double dY = e.getY() - mouseDraggedStartY; // Výpočet rozdílu Y pozice myši
                camera = camera.addAzimuth(Math.PI*(dX / panel.getWidth())/3); // Rotace kamery podle X
                camera = camera.addZenith(Math.PI*(dY / panel.getHeight())/3); // Rotace kamery podle Y
                redraw(); // Překreslení scény
                mouseDraggedStartX = e.getX(); // Aktualizace počáteční X pozice myši
                mouseDraggedStartY = e.getY(); // Aktualizace počáteční Y pozice myši
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                double dist = Integer.MAX_VALUE; // Inicializace nejmenší vzdálenosti
                int index = 0; // Inicializace indexu nejbližšího objektu
                for(int i = 0; i < centers.size(); i++){
                    double dx = e.getX()-centers.get(i).getPos().getX(); // Výpočet rozdílu X pozice
                    double dy = e.getY()-centers.get(i).getPos().getY(); // Výpočet rozdílu Y pozice
                    if((dx + dy) < dist){
                        index = i; // Aktualizace indexu nejbližšího objektu
                        dist = dx + dy; // Aktualizace nejmenší vzdálenosti
                    }
                }
                selectedSolidIndex = index; // Nastavení indexu vybraného objektu
            }
        };
        panel.addMouseListener(mouseAdapter); // Přidání posluchače myši
        panel.addMouseMotionListener(mouseAdapter); // Přidání posluchače pohybu myši
    }

    /**
     * Překreslení scény.
     */
    private void redraw() {
        panel.clear(); // Vyčištění panelu
        zBuffer.clearDepth(); // Vyčištění Z-bufferu
        renderer.setView(camera.getViewMatrix()); // Nastavení pohledu kamery
        System.out.println("Azimuth: "+Math.toDegrees(camera.getAzimuth())); // Výpis azimutu kamery
        System.out.println("Zenith: "+Math.toDegrees(camera.getZenith())); // Výpis zenitu kamery
        renderer.setProj(proj); // Nastavení projekční matice
        renderer.setCenters(solids); // Nastavení středů objektů
        centers = renderer.getCenters(solids); // Získání středů objektů
        renderer.render(solids); // Vykreslení objektů
        panel.repaint(); // Překreslení panelu
    }
}
