package view;

import javax.swing.*;
import java.awt.*;

/**
 * Třída Window rozšiřuje JFrame a představuje hlavní okno aplikace.
 * 
 * <p>
 * Tato třída nastavuje základní vlastnosti okna, jako je název, 
 * výchozí operace při zavření a umístění. Obsahuje také instanci 
 * třídy Panel, která je přidána do středu okna.
 * </p>
 * 
 * <p>
 * Metoda {@link #getPanel()} vrací instanci třídy Panel.
 * </p>
 * 
 * @author Vaclav
 */
public class Window extends JFrame {

    private final Panel panel;

    public Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("UHK FIM PGRF : " + this.getClass().getName());

        panel = new Panel();

        add(panel, BorderLayout.CENTER);
        setVisible(true);
        pack();

        setLocationRelativeTo(null);

        panel.setFocusable(true);
        panel.grabFocus();
    }

    public Panel getPanel() {
        return panel;
    }

}
