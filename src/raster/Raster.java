package raster;

/**
 * Rozhraní Raster představuje obecný rastr, který může obsahovat hodnoty libovolného typu.
 *
 * @param <T> Typ hodnoty, kterou rastr obsahuje.
 */
public interface Raster<T> {

    /**
     * Vymaže všechny hodnoty v rastru.
     */
    void clear();

    /**
     * Nastaví výchozí hodnotu pro rastr.
     *
     * @param value Výchozí hodnota.
     */
    void setDefaultValue(T value);

    /**
     * Vrátí šířku rastru.
     *
     * @return Šířka rastru.
     */
    int getWidth();

    /**
     * Vrátí výšku rastru.
     *
     * @return Výška rastru.
     */
    int getHeight();

    /**
     * Vrátí hodnotu na dané pozici v rastru.
     *
     * @param x Horizontální souřadnice.
     * @param y Vertikální souřadnice.
     * @return Hodnota na dané pozici.
     */
    T getValue(int x, int y);

    /**
     * Nastaví hodnotu na dané pozici v rastru.
     *
     * @param x Horizontální souřadnice.
     * @param y Vertikální souřadnice.
     * @param color Hodnota, která má být nastavena.
     */
    void setValue(int x, int y, T color);

    /**
     * Zkontroluje, zda jsou dané souřadnice uvnitř rastru.
     *
     * @param x Horizontální souřadnice.
     * @param y Vertikální souřadnice.
     * @return true, pokud jsou souřadnice uvnitř rastru, jinak false.
     */
    default boolean isInRaster(int x, int y){
        return x < getWidth() && y < getHeight();
    }

}
