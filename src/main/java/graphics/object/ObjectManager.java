package graphics.object;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import graphics.utils.ResourceType;
import graphics.utils.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ObjectManager {

    private List<Vector> vertices;
    private List<Vector> normals;
    private List<Face> faces;
    private int faceIndex;

    private List<Object> objects;
    private List<Material> materials;

    private AlarmClockAnimator animator;

    public ObjectManager() {
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        faces = new ArrayList<>();
        objects = new ArrayList<>();
        materials = new ArrayList<>();
        faceIndex = 0;
        animator = new AlarmClockAnimator();
    }

    public AlarmClockAnimator getAnimator() {
        return animator;
    }

    public void load(GLAutoDrawable glAutoDrawable, String fileName) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        loadMaterials(fileName);
        loadObjects(fileName);
        prepareGraphicsLists(gl);

        vertices.clear();
        normals.clear();
        faces.clear();
    }

    private void loadMaterials(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Resources.loadFile(
                        fileName, ResourceType.Materials), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                parseMaterial(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseMaterial(String line) {
        String[] array = line.split(" ");
        if (array[0].equals("newmtl")) {
            materials.add(new Material(array[1]));
        } else if (array[0].equals("Ns")) {
            materials.get(materials.size() - 1).setSpecularExponent(Float.parseFloat(array[1]));
        } else if (array[0].equals("Ka")) {

        } else if (array[0].equals("Kd")) {
            materials.get(materials.size() - 1).setDiffuseAndAmbient(new float[]{
                    Float.parseFloat(array[1]),
                    Float.parseFloat(array[2]),
                    Float.parseFloat(array[3]),
                    1});
        } else if (array[0].equals("Ks")) {
            materials.get(materials.size() - 1).setSpecular(new float[]{
                    Float.parseFloat(array[1]),
                    Float.parseFloat(array[2]),
                    Float.parseFloat(array[3]),
                    1});
        }
    }

    private void loadObjects(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Resources.loadFile(
                        fileName, ResourceType.Objects), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                parseObject(line);
            }
            if (objects.size() > 0)
                objects.get(objects.size() - 1).setFaceCount(faceIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseObject(String line) {
        String[] array = line.split(" ");
        if (array[0].equals("o")) {
            if (objects.size() > 0)
                objects.get(objects.size() - 1).setFaceCount(faceIndex);
            objects.add(new Object(array[1]));
        } else if (array[0].equals("v")) {
            vertices.add(new Vector(Float.parseFloat(array[1]), Float.parseFloat(array[2]), Float.parseFloat(array[3])));
        } else if (array[0].equals("vn")) {
            normals.add(new Vector(Float.parseFloat(array[1]), Float.parseFloat(array[2]), Float.parseFloat(array[3])));
        } else if (array[0].equals("f")) {
            int a = Integer.parseInt((array[1].split("//"))[0]);
            int b = Integer.parseInt((array[2].split("//"))[0]);
            int c = Integer.parseInt((array[3].split("//"))[0]);
            int na = Integer.parseInt((array[1].split("//"))[1]);
            int nb = Integer.parseInt((array[2].split("//"))[1]);
            int nc = Integer.parseInt((array[3].split("//"))[1]);
            faces.add(new Face(a, b, c, na, nb, nc));
            faceIndex++;
        } else if (array[0].equals("usemtl")) {
            Material objectMaterial = Material.create(0.8f, 0, 0.8f);
            for (Material material : materials) {
                if (material.getName().equals(array[1]))
                    objectMaterial = material;
            }
            objects.get(objects.size() - 1).setMaterial(objectMaterial);
        }
    }

    private void prepareGraphicsLists(GL2 gl) {
        int index = 0;
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).setIndex(gl.glGenLists(1));
            gl.glNewList(objects.get(i).getIndex(), GL2.GL_COMPILE);
            gl.glBegin(GL2.GL_TRIANGLES);
            for (int j = index; j < objects.get(i).getFaceCount(); j++) {
                int[] vertexIndex = faces.get(j).getVertexIndex();
                int[] normalIndex = faces.get(j).getNormalIndex();
                Vector vertexA = vertices.get(vertexIndex[0]);
                Vector normalA = normals.get(normalIndex[0]);
                Vector vertexB = vertices.get(vertexIndex[1]);
                Vector normalB = normals.get(normalIndex[1]);
                Vector vertexC = vertices.get(vertexIndex[2]);
                Vector normalC = normals.get(normalIndex[2]);

                gl.glNormal3f(normalA.getX(), normalA.getY(), normalA.getZ());
                gl.glVertex3f(vertexA.getX(), vertexA.getY(), vertexA.getZ());

                gl.glNormal3f(normalB.getX(), normalB.getY(), normalB.getZ());
                gl.glVertex3f(vertexB.getX(), vertexB.getY(), vertexB.getZ());

                gl.glNormal3f(normalC.getX(), normalC.getY(), normalC.getZ());
                gl.glVertex3f(vertexC.getX(), vertexC.getY(), vertexC.getZ());
            }
            gl.glEnd();
            gl.glEndList();
            index = objects.get(i).getFaceCount();
        }
    }

    public void renderAlarmClock(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        if (animator.isActualTime()) {
            animator.getNow();
        } else {
            if (animator.isSlowMotion()) {
                animator.advanceTime(AlarmClockAnimator.SLOW_MOTION_SECONDS_PER_FRAME); // Use constant from animator
            } else {
                animator.advanceTime(AlarmClockAnimator.FAST_FORWARD_SECONDS_PER_FRAME); // Use constant from animator
            }
        }

        for (Object object : objects) {
            animator.render(gl, object);
        }
    }
}
