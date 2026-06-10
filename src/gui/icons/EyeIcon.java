package gui.icons;

import javax.swing.*;
import java.awt.*;

public class EyeIcon implements Icon {
    private final int size;
    private final Color color;
    public EyeIcon(int size, Color color) {
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
        g2.setStroke(new BasicStroke(1.8f));

        g2.drawOval(
                x + 1,
                y + size / 4,
                size - 2,
                size / 2
        );

        g2.fillOval(
                x + size / 2 - 2,
                y + size / 2 - 2,
                4,
                4
        );

        g2.dispose();
    }
}