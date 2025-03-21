package shader;

import model.Vertex;
import transforms.Col;

/**
 * Rozhraní Shader představuje stínovač, který určuje barvu vrcholu.
 */
public interface Shader {

    /**
     * Metoda getColor vrací barvu pro daný vrchol.
     *
     * @param v Vrchol, pro který se má určit barva.
     * @return Barva vrcholu.
     */
    Col getColor(Vertex v);
}
