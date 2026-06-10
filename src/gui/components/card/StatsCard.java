package gui.components.card;

import gui.theme.AppColors;
import gui.theme.AppFonts;
import gui.theme.Shadows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StatsCard extends JPanel {

    private final Color bgColor;
    private final Color accentColor;
    private final JLabel valueLabel;
    private final JLabel titleLabel;
    // Animasyon için
    private double   currentValue  = 0;
    private double   targetValue   = 0;
    private boolean  isNumeric     = false;
    private String   suffix        = "";
    private Timer    animTimer;

    // ── Constructor ───────────────────────────────────────────────────────────
    public StatsCard(String title, String value, Color bgColor, Color accentColor) {
        this.bgColor     = bgColor;
        this.accentColor = accentColor;

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(14, 18, 18, 18));   // alt 18 → şerit için boşluk
        setPreferredSize(new Dimension(140, 78));
        setMaximumSize (new Dimension(140, 78));
        setMinimumSize (new Dimension(140, 78));

        // VALUE
        valueLabel = new JLabel(value);
        valueLabel.setAlignmentX(LEFT_ALIGNMENT);
        valueLabel.setFont(AppFonts.SUBTITLE);
        valueLabel.setForeground(accentColor);

        // TITLE
        titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        titleLabel.setFont(AppFonts.BODY);
        titleLabel.setForeground(AppColors.LABEL_FG);

        add(valueLabel);
        add(Box.createVerticalStrut(6));
        add(titleLabel);
    }

    // ── Değer güncelleme: sadece string ───────────────────────────────────────
    public void setValue(String value) {
        valueLabel.setText(value);
    }

    /**
     * Sayısal değeri animasyonlu günceller.
     * Örnek: setValue(8, "") veya setValue(3505.0, " ₺")
     */
    public void setValue(double target, String sfx) {
        this.suffix   = sfx;
        this.isNumeric = true;
        this.targetValue = target;

        if (animTimer != null && animTimer.isRunning()) animTimer.stop();

        animTimer = new Timer(16, null);
        animTimer.addActionListener(e -> {
            double diff = targetValue - currentValue;
            if (Math.abs(diff) < 0.5) {
                currentValue = targetValue;
                animTimer.stop();
            } else {
                currentValue += diff * 0.15;   // ease-out
            }
            // Tam sayı ise virgülsüz göster
            if (sfx.contains("₺")) {
                valueLabel.setText(String.format("%.1f%s", currentValue, sfx));
            } else {
                valueLabel.setText(String.valueOf((int) Math.round(currentValue)) + sfx);
            }
        });
        animTimer.start();
    }

    // ── Boyama ────────────────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w  = getWidth()  - 6;
        int h  = getHeight() - 6;
        int r  = 18;

        // Gölge
        Shadows.drawShadow(g2, getWidth(), getHeight(), r);

        // Arka plan
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, w, h, r, r));

        // Alt renk şeridi (accent)
        int stripH = 4;
        g2.setColor(accentColor);
        g2.setClip(new RoundRectangle2D.Double(0, 0, w, h, r, r));
        g2.fillRect(0, h - stripH, w, stripH);
        g2.setClip(null);

        // İnce kenarlık
        g2.setColor(new Color(255, 255, 255, 120));
        g2.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, r, r));

        g2.dispose();
        super.paintComponent(g);
    }
}
