package graphics.object;

public class Face {

    private int[] vertexIndex;
    private int[] normalIndex;

    public Face(int a, int b, int c, int na, int nb, int nc) {
        vertexIndex = new int[]{--a, --b, --c};
        normalIndex = new int[]{--na, --nb, --nc};
    }

    public int[] getVertexIndex() {
        return vertexIndex;
    }

    public int[] getNormalIndex() {
        return normalIndex;
    }
}
