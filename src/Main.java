import controller.Controller3D;
import view.Window;
import javax.swing.*;
/**
 * Hlavní třída programu.
 * <p>
 * Tato třída obsahuje hlavní metodu, která je vstupním bodem programu.
 * Vytváří a zobrazuje hlavní okno aplikace a inicializuje 3D ovladač.
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            new Controller3D(window.getPanel());
            window.setVisible(true);
        });
    }
}
