package model;

/**
 * Výčtový typ TopologyType představuje různé typy topologie grafických prvků.
 * 
 * <ul>
 *   <li>POINTS - jednotlivé body</li>
 *   <li>LINES - jednotlivé čáry</li>
 *   <li>LINE_STRIP - souvislá čára složená z více bodů</li>
 *   <li>TRIANGLE - jednotlivé trojúhelníky</li>
 *   <li>TRIANGLE_STRIP - souvislý pás trojúhelníků</li>
 * </ul>
 */
public enum TopologyType {
    POINTS, LINES, LINE_STRIP, TRIANGLE, TRIANGLE_STRIP
}
