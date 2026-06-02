import gui.screens.LoginFrame;
import gui.theme.AppFonts;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        UIManager.put("Label.font", AppFonts.BODY);
        UIManager.put("Button.font", AppFonts.BUTTON);
        UIManager.put("TextField.font", AppFonts.BODY);
        UIManager.put("ComboBox.font", AppFonts.BODY);
        UIManager.put("Table.font", AppFonts.TABLE_CELL);
        UIManager.put("TableHeader.font", AppFonts.TABLE_HEADER);

        // Swing EDT üzerinde başlat
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}