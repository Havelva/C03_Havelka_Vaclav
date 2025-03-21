package solid;

import model.Part;
import model.Vertex;

import java.util.ArrayList;
import java.util.List;

public abstract class Solid {
    protected List<Vertex> vertexBuffer = new ArrayList<Vertex>();
    protected List<Integer> indexBuffer = new ArrayList<Integer>();
    protected List<Part> partBuffer = new ArrayList<Part>();

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Part> getPartBuffer() {
        return partBuffer;
    }
}
