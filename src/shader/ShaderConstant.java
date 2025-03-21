package shader;

import model.Vertex;
import transforms.Col;

import java.awt.*;

/**
 * Třída ShaderConstant implementuje rozhraní Shader a poskytuje konstantní barvu pro daný vrchol.
 * 
 * <p>
 * Tato třída vrací vždy stejnou barvu, konkrétně červenou, bez ohledu na vstupní vrchol.
 * </p>
 * 
 * @author Vaclav Havelka
 */
public class ShaderConstant implements Shader{
    @Override
    public Col getColor(Vertex v) {
        return new Col(Color.red.getRGB());
    }
}
