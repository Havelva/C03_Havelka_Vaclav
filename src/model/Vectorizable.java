package model;

/**
 * Rozhraní Vectorizable představuje vektorové operace, které lze provádět na objektech typu T.
 *
 * @param <T> Typ objektu, na kterém budou prováděny vektorové operace.
 */
public interface Vectorizable<T> {
    T mul(double k);
    T add(T v);
}
