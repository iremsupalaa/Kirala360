package gui.components;

import gui.theme.AppColors;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Ekranın sağ altında beliren, 2.5 saniye sonra kaybolan bildirim balonu.
 * JOptionPane'in yerini alır — kullanıcıyı bloklamaz.
 *
 * Kullanım:
 *   ToastNotification.show(parentFrame, "Araç eklendi.", ToastNotification.Type.SUCCESS);
 */
public class ToastNotification extends JWindow {

    public enum Type { SUCCESS, WARNING, ERROR, INFO }
    private static final int  WIDTH   = 300;
    private static final int  HEIGHT  = 54;
    private static final int  MARGIN  = 20;   // ekran kenarından boşluk
    private static final int  SHOW_MS = 2500; // görünür kalma süresi (ms)
    private static final int  FADE_STEPS = 25;

    private float alpha = 0f;
    private ToastNotification(Frame parent, String message, Type type) {
        super(parent);
        setSize(WIDTH, HEIGHT);

        // Ekranın sağ altına yerleştir
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width - WIDTH - MARGIN,
                screen.height - HEIGHT - MARGIN - 60);

        setAlwaysOnTop(true);

        // Saydam pencere desteği
        try { setBackground(new Color(0, 0, 0, 0)); }
        catch (Exception ignored) {}

        Color[] colors = colorsFor(type);
        String  icon   = iconFor(type);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, alpha));

                // Gölge
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 6, 14, 14);

                // Arka plan
                g2.setColor(colors[0]);
                g2.fill(new RoundRectangle2D.Float(
                        0, 0, getWidth() - 4, getHeight() - 4, 14, 14));

                // Sol kenar şeridi
                g2.setColor(colors[1]);
                g2.setClip(new RoundRectangle2D.Float(
                        0, 0, getWidth() - 4, getHeight() - 4, 14, 14));
                g2.fillRect(0, 0, 5, getHeight());
                g2.setClip(null);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(0, 0));

        // İkon
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        iconLbl.setForeground(colors[1]);
        iconLbl.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 10));

        // Mesaj
        JLabel msgLbl = new JLabel(message);
        msgLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msgLbl.setForeground(AppColors.TITLE_FG);

        panel.add(iconLbl, BorderLayout.WEST);
        panel.add(msgLbl,  BorderLayout.CENTER);
        add(panel);
    }

    // ── Renk ve ikon seçimi ───────────────────────────────────────────────────
    private Color[] colorsFor(Type type) {
        if (type == Type.SUCCESS) return new Color[]{new Color(235, 252, 240), new Color(22, 163, 74)};
        if (type == Type.WARNING) return new Color[]{new Color(255, 248, 225), new Color(202, 138, 4)};
        if (type == Type.ERROR)   return new Color[]{new Color(255, 235, 235), new Color(185, 28, 28)};
        return new Color[]{new Color(235, 244, 255), new Color(37, 99, 235)}; // INFO
    }

    private String iconFor(Type type) {
        if (type == Type.SUCCESS) return "\u2713"; // ✓
        if (type == Type.WARNING) return "\u26A0"; // ⚠
        if (type == Type.ERROR)   return "\u2715"; // ✕
        return "\u2139";                            // ℹ INFO
    }

    // ── Göster ───────────────────────────────────────────────────────────────
    public static void show(Frame parent, String message, Type type) {
        ToastNotification toast = new ToastNotification(parent, message, type);
        toast.setVisible(true);

        // Fade-in
        Timer fadeIn = new Timer(16, null);
        fadeIn.addActionListener(e -> {
            toast.alpha = Math.min(1f, toast.alpha + 1f / FADE_STEPS);
            toast.repaint();
            if (toast.alpha >= 1f) ((Timer) e.getSource()).stop();
        });
        fadeIn.start();

        // Görünür kal, sonra fade-out
        Timer hold = new Timer(SHOW_MS, e -> {
            Timer fadeOut = new Timer(16, null);
            fadeOut.addActionListener(ev -> {
                toast.alpha = Math.max(0f, toast.alpha - 1f / FADE_STEPS);
                toast.repaint();
                if (toast.alpha <= 0f) {
                    ((Timer) ev.getSource()).stop();
                    toast.dispose();
                }
            });
            fadeOut.start();
        });
        hold.setRepeats(false);
        hold.start();
    }
}