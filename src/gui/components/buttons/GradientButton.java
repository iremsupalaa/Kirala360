package gui.components.buttons;

import gui.theme.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GradientButton extends JButton {
    private final Color startColor;
    private final Color endColor;
    private final int   radius;

    private boolean hovered = false;
    private boolean pressed = false;
    // ── Tam parametreli constructor ───────────────────────────────────────────
    public GradientButton(String text, Color startColor, Color endColor, int radius) {
        super(text);
        this.startColor = startColor;
        this.endColor   = endColor;
        this.radius     = radius;
        init();
    }
    // ── Geriye dönük uyumluluk: eski kod radius vermeden çağırabilir ──────────
    public GradientButton(String text, Color startColor, Color endColor) {
        this(text, startColor, endColor, 18);
    }

    // ── Ortak kurulum ─────────────────────────────────────────────────────────
    private void init() {
        setFont(AppFonts.BUTTON);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 18, 10, 18));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!isEnabled()) return;
                hovered = true; repaint();
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false; pressed = false; repaint();
            }
            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                if (!isEnabled()) return;
                pressed = true; repaint();
            }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) {
                pressed = false; repaint();
            }
        });
    }

    // ── Devre dışı bırakıldığında cursor'ı değiştir ───────────────────────────
    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setCursor(Cursor.getPredefinedCursor(
                b ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        repaint();
    }

    // ── Boyama ────────────────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Pressed → aşağı it, hover → yukarı kaldır
        int yOffset = pressed ? 1 : (hovered ? -2 : 0);

        if (!isEnabled()) {
            // Devre dışı: gri gradyan
            g2.setPaint(new GradientPaint(
                    0, 0, new Color(190, 195, 210),
                    0, getHeight(), new Color(170, 175, 190)));
            g2.fill(new RoundRectangle2D.Float(0, yOffset,
                    getWidth(), getHeight() - 4, radius, radius));
            g2.dispose();
            super.paintComponent(g);
            return;
        }

        // Gölge (pressed'da daha küçük)
        int shadowAlpha  = pressed ? 12 : 25;
        int shadowOffset = pressed ? 2  : 4;
        g2.setColor(new Color(0, 0, 0, shadowAlpha));
        g2.fillRoundRect(0, shadowOffset + yOffset,
                getWidth(), getHeight() - 2, radius, radius);

        // Gradyan
        Color s = hovered ? startColor.brighter() : startColor;
        Color e = hovered ? endColor.brighter()   : endColor;
        if (pressed) { s = startColor.darker(); e = endColor.darker(); }

        g2.setPaint(new GradientPaint(0, 0, s, 0, getHeight(), e));
        g2.fill(new RoundRectangle2D.Float(
                0, yOffset, getWidth(), getHeight() - 4, radius, radius));

        // Üst kenar parlaklık çizgisi (glass efekti)
        g2.setColor(new Color(255, 255, 255, pressed ? 20 : 60));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(
                1, yOffset + 1, getWidth() - 2, getHeight() - 6, radius, radius));

        g2.dispose();
        super.paintComponent(g);
    }
}
