package shader;

import model.Vertex;
import transforms.Col;

/**
 * Třída ShaderInterpolated implementuje rozhraní Shader a poskytuje metodu pro získání barvy z vrcholu.
 * 
 * <p>
 * Tato třída je určena k interpolaci barev mezi vrcholy. Metoda getColor vrací barvu, která je přímo
 * uložena ve vrcholu.
 * </p>
 * 
 * @author Vaclav Havelka
 */
public class ShaderInterpolated implements Shader{
    @Override
    public Col getColor(Vertex v) {
        return v.getColor();
    }
}
