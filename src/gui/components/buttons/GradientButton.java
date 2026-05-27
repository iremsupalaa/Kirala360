package gui.components.buttons;

import gui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GradientButton extends JButton {

    private final Color startColor;
    private final Color endColor;

    private boolean hovered = false;

    private int radius = 18;

    public GradientButton(
            String text,
            Color startColor,
            Color endColor
    ) {

        super(text);

        this.startColor = startColor;
        this.endColor = endColor;

        setFont(AppFonts.BUTTON);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
        ));
        setBorder(new EmptyBorder(
                10,
                18,
                10,
                18
        ));
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        int yOffset = hovered ? -2 : 0;
        // Shadow
        g2.setColor(new Color(0,0,0,25));
        g2.fillRoundRect(
                0,
                4 + yOffset,
                getWidth(),
                getHeight() - 2,
                radius,
                radius
        );
        // Gradient
        GradientPaint gp = new GradientPaint(
                0,
                0,
                hovered
                        ? startColor.brighter()
                        : startColor,
                0,
                getHeight(),
                hovered
                        ? endColor.brighter()
                        : endColor
        );
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Float(
                0,
                yOffset,
                getWidth(),
                getHeight() - 4,
                radius,
                radius
        ));
        g2.dispose();
        super.paintComponent(g);
    }
}