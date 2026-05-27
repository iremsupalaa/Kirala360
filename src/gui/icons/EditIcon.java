package gui.icons;

import javax.swing.*;
import java.awt.*;

public class EditIcon implements Icon {

    private final int size;
    private final Color color;

    public EditIcon(int size, Color color) {
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

        g2.drawLine(
                x + 3,
                y + size - 4,
                x + size - 4,
                y + 3
        );

        g2.drawLine(
                x + size - 6,
                y + 3,
                x + size - 3,
                y + 6
        );

        g2.dispose();
    }
}