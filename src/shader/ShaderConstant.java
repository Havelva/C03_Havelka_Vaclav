package shader;

import model.Vertex;
import transforms.Col;

import java.awt.*;

public class ShaderConstant implements Shader{
    @Override
    public Col getColor(Vertex v) {
        return new Col(Color.red.getRGB());
    }
}
