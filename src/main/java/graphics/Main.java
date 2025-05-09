package graphics;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import graphics.scene.AlarmClockRenderer;
import graphics.scene.IRenderer;
import graphics.utils.ResourceType;
import graphics.utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private final int FPS = 60;

    public static void main(String[] args) {
        boolean fullscreen;
        if (args.length == 1)
            fullscreen = true;
        else
            fullscreen = false;

        SwingUtilities.invokeLater(() -> new Main().start(fullscreen));
    }

    public void start(boolean fullscreen) {
        try {
            Frame frame = new Frame("Alarm Clock");
            frame.setIconImages(loadIconSet());

            if (fullscreen) {
                frame.setUndecorated(true);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            }

            GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities capabilities = new GLCapabilities(profile);
            capabilities.setRedBits(8);
            capabilities.setBlueBits(8);
            capabilities.setGreenBits(8);
            capabilities.setAlphaBits(8);
            capabilities.setDepthBits(24);

            capabilities.setDoubleBuffered(true);
            capabilities.setSampleBuffers(true);
            capabilities.setNumSamples(8);

            GLCanvas canvas = new GLCanvas(capabilities);
            IRenderer renderer = new AlarmClockRenderer();
            canvas.addGLEventListener(renderer);
            canvas.addMouseListener(renderer);
            canvas.addMouseMotionListener(renderer);
            canvas.addKeyListener(renderer);
            canvas.setSize(880, 660);

            frame.add(canvas);

            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    new Thread(() -> {
                        if (animator.isStarted())
                            animator.stop();
                        System.exit(0);
                    }).start();
                }
            });
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            canvas.requestFocus();
            animator.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Image> loadIconSet() {
        List<Image> iconSet = new ArrayList<>();

        try {
            for (int i = 32; i < 512; i *= 2) {
                InputStream inputStream = Resources.loadFile(String.format("icon%d", i), ResourceType.Icon);
                Image icon = ImageIO.read(inputStream);
                iconSet.add(icon);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iconSet;
    }
}
