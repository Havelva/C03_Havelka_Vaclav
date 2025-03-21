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

public class Controller3D implements Controller {
    private final Panel panel;
    private ZBuffer zBuffer;
    private TriangleRasterizer triangleRasterizer;

    private LineRasterizer lineRasterizer;
    private Renderer renderer;
    private BufferedImage texture;
    private Camera camera;
    private ArrayList<Vertex> centers;
    private ArrayList<Solid> solids;
    private int selectedSolidIndex;
    private int mouseDraggedStartX, mouseDraggedStartY;
    private Mat4 proj;


    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(Color.black.getRGB()));
        zBuffer = new ZBuffer(raster);
        try {
            texture = ImageIO.read(new File("./res/texture.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        triangleRasterizer = new TriangleRasterizer(zBuffer, v -> {
            Vec2D vec = v.getUv().mul(1 / v.getOne());
            int x = (int) (vec.getX() * texture.getWidth());
            int y = (int) (vec.getY() * texture.getHeight());
            return new Col(texture.getRGB(x, y));
        });


        lineRasterizer = new LineRasterizer(zBuffer);
        renderer = new Renderer(triangleRasterizer, lineRasterizer, panel);


        Vec3D pos = new Vec3D(0, 0, 0);
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
        solids = new ArrayList<>();
        Cube cube = new Cube();
        Triangl triangl = new Triangl();
        CubicTriangl cubicTriangl = new CubicTriangl();
        AxisX axisX = new AxisX();
        AxisY axisY = new AxisY();
        AxisZ axisZ = new AxisZ();
        solids.add(cube);
        solids.add(triangl);
        solids.add(cubicTriangl);
        solids.add(axisX);
        solids.add(axisY);
        solids.add(axisZ);
    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_A){
                    //camera move left
                    camera = camera.left(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_D) {
                    //camera move right
                    camera = camera.right(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_W) {
                    //camera move up
                    camera = camera.up(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    //camera move down
                    camera = camera.down(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    //camera zoom in
                    camera = camera.forward(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_E) {
                    //camera zoom out
                    camera = camera.backward(0.05);
                }
                else if (e.getKeyCode() == KeyEvent.VK_T) {
                    //camera rotate over
                    camera = camera.addZenith(Math.toRadians(5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_F) {
                    //camera rotate left
                    camera = camera.addAzimuth(Math.toRadians(5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_G) {
                    //camera rotate under
                    camera = camera.addZenith(Math.toRadians(-5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_H) {
                    //camera rotate right
                    camera = camera.addAzimuth(Math.toRadians(-5));
                }
                else if (e.getKeyCode() == KeyEvent.VK_I) {
                    //solid move up
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, 0.05, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_J) {
                    //solid move left
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(-0.05, 0, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_L) {
                    //solid move right
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0.05, 0, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_K) {
                    //solid move down
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, -0.05, 0)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_U) {
                    //solid move forward
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, 0, 0.05)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_O) {
                    //solid move backward
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Transl(0, 0, -0.05)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    //rotation Y left
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotY(Math.toRadians(-5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    //rotation Y right
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotY(Math.toRadians(5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_HOME) {
                    //rotation X backward
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotX(Math.toRadians(-5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_END) {
                    //rotation X forward
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotX(Math.toRadians(5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                    //rotation Z left
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotZ(Math.toRadians(5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    //rotation Z right
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4RotZ(Math.toRadians(-5))));
                }
                else if (e.getKeyCode() == KeyEvent.VK_M) {
                    //zoom in
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Scale(1.1)));
                }
                else if (e.getKeyCode() == KeyEvent.VK_N) {
                    //zoom out
                    solids.get(selectedSolidIndex).setModel(solids.get(selectedSolidIndex).getModel().mul(new Mat4Scale(0.9)));
                }
                redraw();
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDraggedStartX = e.getX();
                mouseDraggedStartY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                double dX = e.getX() - mouseDraggedStartX;
                double dY = e.getY() - mouseDraggedStartY;
                camera = camera.addAzimuth(Math.PI*(dX / panel.getWidth())/3);
                camera = camera.addZenith(Math.PI*(dY / panel.getHeight())/3);
                redraw();
                mouseDraggedStartX = e.getX();
                mouseDraggedStartY = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                double dist = Integer.MAX_VALUE;
                int index = 0;
                for(int i = 0; i < centers.size(); i++){
                    double dx = e.getX()-centers.get(i).getPos().getX();
                    double dy = e.getY()-centers.get(i).getPos().getY();
                    if((dx + dy) < dist){
                        index = i;
                        dist = dx + dy;
                    }
                }
                selectedSolidIndex = index;
            }
        };
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
    }

    private void redraw() {
        panel.clear();
        zBuffer.clearDepth();
        renderer.setView(camera.getViewMatrix());
        System.out.println("Azimuth: "+Math.toDegrees(camera.getAzimuth()));
        System.out.println("Zenith: "+Math.toDegrees(camera.getZenith()));
        renderer.setProj(proj);
        renderer.setCenters(solids);
        centers = renderer.getCenters(solids);
        renderer.render(solids);
        panel.repaint();
    }
}
