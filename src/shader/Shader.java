package shader;
import transforms.Col;
import model.Vertex;

@FunctionalInterface
public interface Shader {
    Col getColor(Vertex pixel);
}
