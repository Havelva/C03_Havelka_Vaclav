package model;

/**
 * Třída Part reprezentuje část topologie s definovaným typem, počátečním indexem a počtem prvků.
 */
public class Part {

    /**
     * Typ topologie této části.
     */
    private final TopologyType type;

    /**
     * Počáteční index této části v rámci topologie.
     */
    private final int start;

    /**
     * Počet prvků v této části topologie.
     */
    private final int count;

    /**
     * Vytváří novou instanci třídy Part s daným typem topologie, počátečním indexem a počtem prvků.
     *
     * @param type Typ topologie této části.
     * @param start Počáteční index této části v rámci topologie.
     * @param count Počet prvků v této části topologie.
     */
    public Part(TopologyType type, int start, int count) {
        this.type = type;
        this.start = start;
        this.count = count;
    }

    /**
     * Vrací typ topologie této části.
     *
     * @return Typ topologie této části.
     */
    public TopologyType getType() {
        return type;
    }

    /**
     * Vrací počáteční index této části v rámci topologie.
     *
     * @return Počáteční index této části.
     */
    public int getStart() {
        return start;
    }

    /**
     * Vrací počet prvků v této části topologie.
     *
     * @return Počet prvků v této části topologie.
     */
    public int getCount() {
        return count;
    }
}
