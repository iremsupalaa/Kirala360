package gui.components.badges;

import gui.theme.AppColors;
import gui.theme.AppFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StatusBadge extends JPanel {
    private final String text;
    private final Color bg;
    private final Color fg;
    public StatusBadge(
            String text,
            Color bg,
            Color fg
    ) {
        this.text = text;
        this.bg = bg;
        this.fg = fg;
        setOpaque(false);
        setPreferredSize(new Dimension(110, 28));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        // background
        g2.setColor(bg);
        g2.fill(new RoundRectangle2D.Float(
                0,
                0,
                getWidth(),
                getHeight(),
                20,
                20
        ));

        // text
        g2.setFont(AppFonts.BADGE);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g2.setColor(fg);
        g2.drawString(text, x, y);
        g2.dispose();
    }

    public static StatusBadge musait() {
        return new StatusBadge(
                "✓ Müsait",
                AppColors.MUSAIT_BG,
                AppColors.MUSAIT_FG
        );
    }

    public static StatusBadge kirada() {
        return new StatusBadge(
                "✕ Kirada",
                AppColors.KIRADA_BG,
                AppColors.KIRADA_FG
        );
    }
}