import gui.screens.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Swing EDT üzerinde başlat
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}