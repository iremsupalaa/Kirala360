package gui.icons;

import javax.swing.*;
import java.awt.*;

public class SearchIcon implements Icon {

    private final int size;
    private final Color color;

    public SearchIcon(int size, Color color) {
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
        g2.setStroke(new BasicStroke(2f));

        int r = size - 6;

        g2.drawOval(x + 1, y + 1, r, r);

        g2.drawLine(
                x + r - 1,
                y + r - 1,
                x + size - 1,
                y + size - 1
        );

        g2.dispose();
    }
}