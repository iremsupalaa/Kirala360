package gui.theme;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;


public class Shadows {

    private Shadows() {}

    public static void drawShadow(
            Graphics2D g2,
            int width,
            int height,
            int radius
    ) {

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Outer soft shadows
        for (int i = 0; i < 10; i++) {
            int alpha = 14 - i;
            g2.setColor(new Color(0, 0, 0, Math.max(alpha, 2)));
            g2.draw(new RoundRectangle2D.Float(
                    i,
                    i + 1,
                    width - (i * 2) - 1,
                    height - (i * 2) - 1,
                    radius,
                    radius
            ));
        }
    }
}