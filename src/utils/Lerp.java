package utils;

import model.Vectorizable;

/**
 * Třída Lerp poskytuje metodu pro lineární interpolaci mezi dvěma vektory.
 *
 * @param <T> Typ, který implementuje rozhraní Vectorizable<T>.
 */
public class Lerp<T extends Vectorizable<T>> {

    /**
     * Provádí lineární interpolaci mezi dvěma vektory v1 a v2 na základě parametru t.
     *
     * @param v1 První vektor.
     * @param v2 Druhý vektor.
     * @param t  Parametr interpolace, který určuje váhu druhého vektoru. Hodnota by měla být v rozmezí 0.0 až 1.0.
     * @return Interpolovaný vektor, který je kombinací v1 a v2 na základě parametru t.
     */
    public T lerp(T v1, T v2, double t) {
        return v1.mul(1 - t).add(v2.mul(t));
    }
}
       
