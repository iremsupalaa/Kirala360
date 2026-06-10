package gui.icons;

import javax.swing.*;
import java.awt.*;

public class TrashIcon implements Icon {

    private final int size;
    private final Color color;

    public TrashIcon(int size, Color color) {
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

        g2.drawRect(
                x + 3,
                y + 5,
                size - 6,
                size - 7
        );

        g2.drawLine(
                x + 1,
                y + 5,
                x + size - 1,
                y + 5
        );

        g2.drawLine(
                x + size / 3,
                y + 8,
                x + size / 3,
                y + size - 4
        );

        g2.drawLine(
                x + (size / 3) * 2,
                y + 8,
                x + (size / 3) * 2,
                y + size - 4
        );

        g2.dispose();
    }
}