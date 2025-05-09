package graphics.object;

import com.jogamp.opengl.GL2;

public class Material {

    private String name;
    private float specularExponent;
    private float[] ambient;
    private float[] diffuse;
    private float[] specular;

    public Material(String name) {
        this.name = name;
    }

    private Material(float r, float g, float b) {
        name = "simple";
        ambient = new float[]{0.2f * r, 0.2f * g, 0.2f * b, 1};
        diffuse = new float[]{r, g, b, 1};
        specular = new float[]{0.8f * r, 0.8f * g, 0.8f * b, 1};
        specularExponent = 0;
    }

    public void setSpecularExponent(float specularExponent) {
        this.specularExponent = specularExponent;
    }

    public void setDiffuseAndAmbient(float[] diffuse) {
        this.diffuse = diffuse;
        ambient = new float[4];
        for (int i = 0; i < diffuse.length; i++) {
            if (i < 3)
                ambient[i] = diffuse[i] * 0.2f;
            else
                ambient[i] = diffuse[i];
        }
    }

    public void setSpecular(float[] specular) {
        this.specular = specular;
    }

    public String getName() {
        return name;
    }

    public void applyMaterial(GL2 gl) {
        gl.glColor3f(diffuse[0], diffuse[1], diffuse[2]);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, specularExponent);
    }

    public static Material create(float r, float g, float b) {
        return new Material(r, g, b);
    }
}
