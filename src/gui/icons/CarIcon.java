package gui.icons;

import javax.swing.*;
import java.awt.*;

public class CarIcon implements Icon {
    private final int size;
    private final Color color;
    public CarIcon(int size, Color color) {
        this.size = size;
        this.color = color;
    }
    @Override
    public int getIconWidth() {
        return size;
    }
    @Override
    public int getIconHeight() {
        return size;
    }
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(color);
        g2.fillRoundRect(
                x + 2,
                y + size / 3,
                size - 4,
                size / 3,
                6,
                6
        );

        g2.fillRoundRect(
                x + size / 4,
                y + size / 6,
                size / 2,
                size / 3,
                6,
                6
        );

        g2.setColor(Color.WHITE);

        g2.fillOval(x + 4, y + size / 2, 5, 5);
        g2.fillOval(x + size - 9, y + size / 2, 5, 5);

        g2.dispose();
    }
}