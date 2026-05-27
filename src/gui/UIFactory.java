package gui;

import gui.theme.AppColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import gui.theme.Shadows;
import gui.components.inputs.ModernTextField;

/**
 * Tekrar kullanılabilir UI bileşenleri: RoundCard, RoundButton, ikonlar, yardımcı factory metodları.
 */
public class UIFactory {

    private UIFactory() {}

    // ════════════════════════════════════════════════════════════════════════
    // YUVARLAK KÖŞELİ KART
    // ════════════════════════════════════════════════════════════════════════

    public static class RoundCard extends JPanel {
        private final int r;
        public RoundCard(int r) {
            this.r = r;
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(16, 18, 16, 18));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shadows.drawShadow(g2, getWidth(), getHeight(), r);
            g2.setColor(AppColors.CARD_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-2, getHeight()-2, r, r));
            g2.setColor(AppColors.CARD_BDR);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-3, getHeight()-3, r, r));
            g2.dispose();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // YUVARLAK KÖŞELİ BUTON
    // ════════════════════════════════════════════════════════════════════════

    public static class RoundButton extends JButton {
        private final Color bg;
        private final int radius;
        private Color currentBg;

        public RoundButton(String text, Color bg, Color fg, Icon icon, int radius) {
            super(text, icon);
            this.bg = bg;
            this.radius = radius;
            this.currentBg = bg;
            setForeground(fg);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 16, 8, 16));

            Color dark = bg.darker();
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { currentBg = dark; repaint(); }
                @Override public void mouseExited (java.awt.event.MouseEvent e) { currentBg = bg;   repaint(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(currentBg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius*2, radius*2));
            g2.setColor(currentBg.darker());
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, radius*2, radius*2));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // FACTORY METODLAR
    // ════════════════════════════════════════════════════════════════════════

    /** Hap şeklinde (pill) tam yuvarlak büyük buton */
    public static RoundButton pillButton(String text, Color bg, Color fg, Icon icon) {
        RoundButton btn = new RoundButton(text, bg, fg, icon, 22);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    /** Kart başlık etiketi: ikon + yazı */
    public static JLabel headerLabel(String text, Icon icon) {
        JLabel lbl = new JLabel("  " + text, icon, SwingConstants.LEFT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbl.setForeground(AppColors.ACCENT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        return lbl;
    }

    /** Mavi yatay ince çizgi */
    public static JSeparator blueLine() {
        JSeparator s = new JSeparator();
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        s.setForeground(AppColors.ACCENT);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        return s;
    }

    /** Dikey boşluk */
    public static Component vgap(int h) { return Box.createVerticalStrut(h); }

    /** Etiket + text field satırı, panele ekler ve field'ı döner */
    public static JTextField inputRow(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(AppColors.LABEL_FG);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        ModernTextField field =
                new ModernTextField(labelText);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.INPUT_BDR, 1),
                new EmptyBorder(3, 8, 3, 8)));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppColors.ACCENT, 2),
                        new EmptyBorder(2, 7, 2, 7)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppColors.INPUT_BDR, 1),
                        new EmptyBorder(3, 8, 3, 8)));
            }
        });
        panel.add(label);
        panel.add(Box.createVerticalStrut(2));
        panel.add(field);
        panel.add(Box.createVerticalStrut(9));
        return field;
    }

    // ════════════════════════════════════════════════════════════════════════
    // CUSTOM IKONLAR
    // ════════════════════════════════════════════════════════════════════════

    public static class CarIcon implements Icon {
        final int sz; final Color c;
        public CarIcon(int sz, Color c) { this.sz=sz; this.c=c; }
        @Override public int getIconWidth()  { return sz+2; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(x, y+sz*3/8, sz, sz/2, 3,3);
            g2.fillRoundRect(x+sz/6, y+sz/8, sz*2/3, sz*3/8, 4,4);
            Color bg=comp!=null?comp.getBackground():Color.WHITE;
            g2.setColor(bg);
            g2.fillOval(x+sz/10,         y+sz*5/8, sz/4,sz/4);
            g2.fillOval(x+sz-sz/10-sz/4, y+sz*5/8, sz/4,sz/4);
            g2.setColor(c); g2.setStroke(new BasicStroke(1f));
            g2.drawOval(x+sz/10,         y+sz*5/8, sz/4,sz/4);
            g2.drawOval(x+sz-sz/10-sz/4, y+sz*5/8, sz/4,sz/4);
            g2.dispose();
        }
    }
    public static class SmallCarIcon extends CarIcon {
        public SmallCarIcon(int sz, Color c) { super(sz,c); }
    }
    public static class PersonIcon implements Icon {
        final int sz; final Color c;
        public PersonIcon(int sz, Color c) { this.sz=sz; this.c=c; }
        @Override public int getIconWidth()  { return sz; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            int hw=sz*2/5;
            g2.fillOval(x+(sz-hw)/2,y,hw,hw);
            g2.fillArc(x,y+sz/2,sz,sz,0,180);
            g2.dispose();
        }
    }
    public static class PlusIcon implements Icon {
        final int sz; final Color c;
        public PlusIcon(int sz, Color c) { this.sz=sz; this.c=c; }
        @Override public int getIconWidth()  { return sz; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(2.2f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            int m=sz/2;
            g2.drawLine(x+m,y+1,x+m,y+sz-1);
            g2.drawLine(x+1,y+m,x+sz-1,y+m);
            g2.dispose();
        }
    }
    public static class SearchIcon implements Icon {
        final int sz; final Color c;
        public SearchIcon(int sz, Color c) { this.sz=sz; this.c=c; }
        @Override public int getIconWidth()  { return sz+2; }
        @Override public int getIconHeight() { return sz+2; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(2f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            int r=(int)(sz*0.6);
            g2.drawOval(x,y,r,r);
            int lx=x+(int)(r*0.7),ly=y+(int)(r*0.7);
            g2.drawLine(lx,ly,x+sz,y+sz);
            g2.dispose();
        }
    }
    public static class ListIcon implements Icon {
        final int sz; final Color c;
        public ListIcon(int sz, Color c) { this.sz=sz; this.c=c; }
        @Override public int getIconWidth()  { return sz+2; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(2f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            int gap=sz/4;
            for(int i=0;i<3;i++)
                g2.drawLine(x+2,y+gap+i*gap,x+sz,y+gap+i*gap);
            g2.dispose();
        }
    }
}