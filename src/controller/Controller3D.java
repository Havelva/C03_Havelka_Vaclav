package controller;

import model.TopologyType;
import model.Vertex;
import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.TriangleRasterizer;
import render.Renderer;
import shader.Shader;
import shader.ShaderConstant;
import shader.ShaderInterpolated;
import solid.Arrow;
import transforms.Col;
import transforms.Point3D;
import view.Panel;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller3D {
    private final Panel panel;
    private final ZBuffer zBuffer;
    private final TriangleRasterizer triangleRasterizer;
    private final LineRasterizer lineRasterizer;
    private final Renderer renderer;

    private final BufferedImage texture;


    public Controller3D(Panel panel) {

        try {
            texture = ImageIO.read(new File("./res/texture.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        // TODO: pozor, posílá se tam raster místo zbufferu
        this.lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer, lineRasterizer, new ShaderInterpolated());
        this.renderer = new Renderer(lineRasterizer, triangleRasterizer);

        initListeners();

        redraw();
    }

    private void initListeners() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Shader shader = new Shader() {
                    @Override
                    public Col getColor(Vertex pixel) {
                        int x = (int) Math.round(pixel.getUv().getX() * texture.getWidth());
                        int y = (int) Math.round(texture.getHeight() -  pixel.getUv().getY() * texture.getHeight());
                        return new Col(texture.getRGB(x, y));
                    }
                };

                triangleRasterizer.setShader(shader);
                redraw();
            }
        });
    }

    private void redraw() {
        panel.clear();

        renderer.renderSolid(new Arrow());

//        triangleRasterizer.rasterize(
//                new Vertex(new Point3D(400, 0, 0.5)),
//                new Vertex(new Point3D(0, 300, 0.5)),
//                new Vertex(new Point3D(799, 300, 0.5)),
//                new Col(0x00ff00)
//        );
//
//        triangleRasterizer.rasterize(
//                new Vertex(new Point3D(400, 0, 0.3)),
//                new Vertex(new Point3D(0, 300, 0.6)),
//                new Vertex(new Point3D(799, 300, 0.6)),
//                new Col(0xff0000)
//        );

        panel.repaint();
    }

}
