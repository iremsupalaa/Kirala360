package gui;

import models.Arac;
import models.Kiralama;
import models.Musteri;
import services.AracService;
import services.KiralamaService;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

public class MainFrame extends JFrame {

    // ── Renkler ────────────────────────────────────────────────────────────────
    private static final Color BG           = new Color(243, 244, 248);
    private static final Color CARD_BG      = Color.WHITE;
    private static final Color CARD_BORDER  = new Color(215, 220, 232);
    private static final Color TITLE_FG     = new Color(18, 25, 50);
    private static final Color ACCENT       = new Color(41, 98, 255);
    private static final Color BTN_EKLE     = new Color(41, 98, 255);
    private static final Color BTN_KIRALA   = new Color(34, 170, 90);
    private static final Color TH_BG        = new Color(232, 238, 250);
    private static final Color TH_FG        = new Color(40, 52, 82);
    private static final Color GRID_COLOR   = new Color(220, 226, 238);
    private static final Color ROW_EVEN     = Color.WHITE;
    private static final Color ROW_ODD      = new Color(248, 250, 255);
    private static final Color MUSAIT_BG    = new Color(204, 244, 220);
    private static final Color MUSAIT_FG    = new Color(20, 125, 60);
    private static final Color KIRADA_BG    = new Color(255, 215, 215);
    private static final Color KIRADA_FG    = new Color(180, 30, 30);
    private static final Color BOT_BLU_BG  = new Color(210, 228, 255);
    private static final Color BOT_BLU_FG  = new Color(28, 76, 200);
    private static final Color BOT_PRP_BG  = new Color(228, 212, 255);
    private static final Color BOT_PRP_FG  = new Color(88, 28, 185);
    private static final Color BOT_ORG_BG  = new Color(255, 222, 180);
    private static final Color BOT_ORG_FG  = new Color(182, 88, 12);
    private static final Color LABEL_FG    = new Color(52, 62, 88);
    private static final Color INPUT_BDR   = new Color(192, 200, 220);

    private JTextField idField, markaField, modelField, fiyatField;
    private JTextField kiralamaIdField, musteriField, gunField;
    private JTable aracTable;
    private DefaultTableModel tableModel;
    private AracService aracService;
    private KiralamaService kiralamaService;

    private static final int RADIUS = 12;

    // ══════════════════════════════════════════════════════════════════════════
    public MainFrame() {
        aracService     = new AracService();
        kiralamaService = new KiralamaService();

        setTitle("Araç Kiralama Sistemi");
        setSize(1100, 900);
        setMinimumSize(new Dimension(1100, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(null);
        root.setBackground(BG);
        add(root);

        // ── BAŞLIK ──────────────────────────────────────────────────────────
        JLabel title = new JLabel("Araç Kiralama Sistemi", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(TITLE_FG);
        title.setBounds(0, 14, 1100, 44);
        root.add(title);

        // ── SOL KART: ARAÇ EKLE  (y=70, h=370) ────────────────────────────
        //   başlık(38) + sep(14) + 4×input(label+field+gap ≈ 56) + buton(44) + padding = ~368
        RoundedPanel ekleCard = new RoundedPanel(RADIUS);
        ekleCard.setBounds(22, 68, 282, 370);
        root.add(ekleCard);

        ekleCard.add(headerLabel("Araç Ekle", new CarIcon(16, ACCENT)));
        ekleCard.add(blueLine());
        ekleCard.add(vgap(10));
        idField    = inputRow(ekleCard, "ID:");
        markaField = inputRow(ekleCard, "Marka:");
        modelField = inputRow(ekleCard, "Model:");
        fiyatField = inputRow(ekleCard, "Fiyat (Günlük):");
        ekleCard.add(vgap(4));
        JButton ekleBtn = mainButton("Araç Ekle", BTN_EKLE, new PlusIcon(13, Color.WHITE));
        ekleCard.add(ekleBtn);

        // ── SOL KART: ARAÇ KİRALA  (y=455, h=288) ─────────────────────────
        //   başlık(38) + sep(14) + 3×input(56) + buton(44) + padding = ~286
        RoundedPanel kiralaCard = new RoundedPanel(RADIUS);
        kiralaCard.setBounds(22, 452, 282, 288);
        root.add(kiralaCard);

        kiralaCard.add(headerLabel("Araç Kirala", new PersonIcon(16, ACCENT)));
        kiralaCard.add(blueLine());
        kiralaCard.add(vgap(10));
        kiralamaIdField = inputRow(kiralaCard, "Araç ID:");
        musteriField    = inputRow(kiralaCard, "Müşteri:");
        gunField        = inputRow(kiralaCard, "Gün:");
        kiralaCard.add(vgap(4));
        JButton kiralaBtn = mainButton("Araç Kirala", BTN_KIRALA, new SmallCarIcon(13, Color.WHITE));
        kiralaCard.add(kiralaBtn);

        // ── TABLO PANELİ  ───────────────────────────────────────────────────
        // Tablo + header: y=68, h=650 (800 - 68 - 12 - 58 - 12)
        RoundedPanel tableCard = new RoundedPanel(RADIUS);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBounds(320, 68, 758, 648);
        root.add(tableCard);

        String[] cols = {"ID", "Marka", "Model", "Fiyat (Günlük)", "Müsaitlik"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        aracTable = new JTable(tableModel);
        aracTable.setRowHeight(38);
        aracTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        aracTable.setGridColor(GRID_COLOR);
        aracTable.setShowGrid(true);
        aracTable.setIntercellSpacing(new Dimension(0, 0));
        aracTable.setSelectionBackground(new Color(200, 220, 255));
        aracTable.setSelectionForeground(TITLE_FG);
        aracTable.setBackground(CARD_BG);
        aracTable.setFocusable(false);
        aracTable.setFillsViewportHeight(true);

        int[] cw = {60, 155, 155, 145, 115};
        for (int i = 0; i < cw.length; i++)
            aracTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);

        JTableHeader th = aracTable.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 14));
        th.setBackground(TH_BG);
        th.setForeground(TH_FG);
        th.setPreferredSize(new Dimension(0, 40));
        th.setReorderingAllowed(false);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRID_COLOR));

        DefaultTableCellRenderer headerRend = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(TH_BG); setForeground(TH_FG);
                setFont(new Font("SansSerif", Font.BOLD, 14));
                setBorder(new EmptyBorder(0, 14, 0, 8));
                return this;
            }
        };
        for (int i = 0; i < cols.length; i++)
            aracTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRend);

        aracTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 8));
                if (!sel) {
                    setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                    setForeground(TITLE_FG);
                }
                setFont(new Font("SansSerif", Font.PLAIN, 14));
                setHorizontalAlignment(col == 0 ? CENTER : LEFT);
                if (col == 4 && v != null) {
                    setHorizontalAlignment(CENTER);
                    setFont(new Font("SansSerif", Font.BOLD, 13));
                    boolean musait = "Müsait".equals(v.toString());
                    if (!sel) {
                        setBackground(musait ? MUSAIT_BG : KIRADA_BG);
                        setForeground(musait ? MUSAIT_FG : KIRADA_FG);
                    }
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(aracTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(CARD_BG);
        tableCard.add(scroll, BorderLayout.CENTER);

        // ── ALT BUTONLAR  ───────────────────────────────────────────────────
        // y = 68 + 648 + 12 = 728; h = 44
        JButton musaitBtn = bottomButton("Müsait Araçlar", BOT_BLU_BG, BOT_BLU_FG, new SearchIcon(13, BOT_BLU_FG));
        musaitBtn.setBounds(320, 728, 238, 44);

        JButton tumBtn = bottomButton("Tüm Araçlar", BOT_PRP_BG, BOT_PRP_FG, new ListIcon(13, BOT_PRP_FG));
        tumBtn.setBounds(568, 728, 238, 44);

        JButton kiradaBtn = bottomButton("Kiradaki Araçlar", BOT_ORG_BG, BOT_ORG_FG, new SmallCarIcon(13, BOT_ORG_FG));
        kiradaBtn.setBounds(816, 728, 262, 44);

        root.add(musaitBtn);
        root.add(tumBtn);
        root.add(kiradaBtn);

        // ── ACTION LİSTENER ─────────────────────────────────────────────────
        ekleBtn.addActionListener(e -> aracEkle());
        kiralaBtn.addActionListener(e -> aracKirala());
        musaitBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            for (Arac a : aracService.musaitAraclariGetir())
                tableModel.addRow(row(a, "Müsait"));
        });
        tumBtn.addActionListener(e -> tabloyuYenile());
        kiradaBtn.addActionListener(e -> kiradakiAraclariGoster());

        tabloyuYenile();
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // UI YARDIMCI METODLAR
    // ══════════════════════════════════════════════════════════════════════════

    /** Yuvarlak köşeli kart paneli */
    static class RoundedPanel extends JPanel {
        private final int r;
        RoundedPanel(int r) {
            this.r = r;
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(16, 18, 16, 18));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Gölge
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fill(new RoundRectangle2D.Float(2, 3, getWidth() - 3, getHeight() - 2, r + 2, r + 2));
            // Kart
            g2.setColor(CARD_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 2, getHeight() - 2, r, r));
            // Kenarlık
            g2.setColor(CARD_BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 3, getHeight() - 3, r, r));
            g2.dispose();
        }
    }

    private JLabel headerLabel(String text, Icon icon) {
        JLabel lbl = new JLabel("  " + text, icon, SwingConstants.LEFT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbl.setForeground(ACCENT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        return lbl;
    }

    private JSeparator blueLine() {
        JSeparator s = new JSeparator();
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        s.setForeground(ACCENT);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        return s;
    }

    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    private JTextField inputRow(JPanel panel, String lbl) {
        JLabel label = new JLabel(lbl);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(LABEL_FG);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BDR, 1),
                new EmptyBorder(3, 8, 3, 8)));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2),
                        new EmptyBorder(2, 7, 2, 7)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(INPUT_BDR, 1),
                        new EmptyBorder(3, 8, 3, 8)));
            }
        });
        panel.add(label);
        panel.add(Box.createVerticalStrut(2));
        panel.add(field);
        panel.add(Box.createVerticalStrut(9));
        return field;
    }

    private JButton mainButton(String text, Color bg, Icon icon) {
        JButton btn = new JButton("  " + text, icon);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        Color dark = bg.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(dark); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JButton bottomButton(String text, Color bg, Color fg, Icon icon) {
        JButton btn = new JButton("  " + text, icon);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, bg.darker()),
                new EmptyBorder(8, 16, 8, 16)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        Color dark = bg.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(dark); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    /** Yuvarlak kenarlık (alt butonlar için) */
    static class RoundedBorder extends AbstractBorder {
        private final int r; private final Color color;
        RoundedBorder(int r, Color color) { this.r = r; this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, w - 1, h - 1, r, r));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(r/2, r/2, r/2, r/2); }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // İŞ MANTIĞI
    // ══════════════════════════════════════════════════════════════════════════

    private Object[] row(Arac a, String durum) {
        return new Object[]{a.getId(), a.getMarka(), a.getModel(), formatFiyat(a.getGunlukFiyat()), durum};
    }

    private void aracEkle() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            double fiyat = Double.parseDouble(fiyatField.getText().trim().replace(",", "."));
            aracService.aracEkle(new Arac(id, markaField.getText().trim(), modelField.getText().trim(), fiyat, true));
            tabloyuYenile(); temizle();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID ve Fiyat sayısal olmalıdır.", "Hatalı Giriş", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aracKirala() {
        try {
            int id = Integer.parseInt(kiralamaIdField.getText().trim());
            int gun = Integer.parseInt(gunField.getText().trim());
            Arac arac = aracService.aracGetir(id);
            if (arac == null || !arac.isMusaitMi()) {
                JOptionPane.showMessageDialog(this, "Araç mevcut değil veya müsait değil.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }
            kiralamaService.aracKirala(new Kiralama(1, arac, new Musteri(1, musteriField.getText().trim(), "555"), gun));
            tabloyuYenile(); temizle();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Araç ID ve Gün sayısal olmalıdır.", "Hatalı Giriş", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tabloyuYenile() {
        tableModel.setRowCount(0);
        for (Arac a : aracService.getAracListesi())
            tableModel.addRow(row(a, a.isMusaitMi() ? "Müsait" : "Kirada"));
    }

    private void kiradakiAraclariGoster() {
        tableModel.setRowCount(0);
        for (Kiralama k : kiralamaService.getKiralamaListesi())
            tableModel.addRow(new Object[]{k.getArac().getId(), k.getArac().getMarka(),
                    k.getArac().getModel(), k.getMusteri().getAdSoyad(),
                    k.getKiralamaTarihi(), k.getGunSayisi(), k.getKalanGun()});
    }

    private void temizle() {
        for (JTextField f : new JTextField[]{idField, markaField, modelField, fiyatField, kiralamaIdField, musteriField, gunField})
            f.setText("");
    }

    private String formatFiyat(double v) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("tr", "TR"));
        nf.setMinimumFractionDigits(1); nf.setMaximumFractionDigits(2);
        return nf.format(v) + " \u20BA";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CUSTOM IKONLAR
    // ══════════════════════════════════════════════════════════════════════════

    static class CarIcon implements Icon {
        final int sz; final Color c;
        CarIcon(int sz, Color c) { this.sz = sz; this.c = c; }
        @Override public int getIconWidth()  { return sz + 2; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            int s = sz;
            g2.fillRoundRect(x, y + s * 3 / 8, s, s / 2, 3, 3);
            g2.fillRoundRect(x + s / 6, y + s / 8, s * 2 / 3, s * 3 / 8, 4, 4);
            Color bg = comp != null ? comp.getBackground() : Color.WHITE;
            g2.setColor(bg);
            g2.fillOval(x + s / 10,           y + s * 5 / 8, s / 4, s / 4);
            g2.fillOval(x + s - s / 10 - s/4, y + s * 5 / 8, s / 4, s / 4);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(1f));
            g2.drawOval(x + s / 10,           y + s * 5 / 8, s / 4, s / 4);
            g2.drawOval(x + s - s / 10 - s/4, y + s * 5 / 8, s / 4, s / 4);
            g2.dispose();
        }
    }
    static class SmallCarIcon extends CarIcon {
        SmallCarIcon(int sz, Color c) { super(sz, c); }
    }

    static class PersonIcon implements Icon {
        final int sz; final Color c;
        PersonIcon(int sz, Color c) { this.sz = sz; this.c = c; }
        @Override public int getIconWidth()  { return sz; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            int hw = sz * 2 / 5;
            g2.fillOval(x + (sz - hw) / 2, y, hw, hw);
            g2.fillArc(x, y + sz / 2, sz, sz, 0, 180);
            g2.dispose();
        }
    }

    static class PlusIcon implements Icon {
        final int sz; final Color c;
        PlusIcon(int sz, Color c) { this.sz = sz; this.c = c; }
        @Override public int getIconWidth()  { return sz; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int m = sz / 2;
            g2.drawLine(x + m, y + 1, x + m, y + sz - 1);
            g2.drawLine(x + 1, y + m, x + sz - 1, y + m);
            g2.dispose();
        }
    }

    static class SearchIcon implements Icon {
        final int sz; final Color c;
        SearchIcon(int sz, Color c) { this.sz = sz; this.c = c; }
        @Override public int getIconWidth()  { return sz + 2; }
        @Override public int getIconHeight() { return sz + 2; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int r = (int)(sz * 0.6);
            g2.drawOval(x, y, r, r);
            int lx = x + (int)(r * 0.7), ly = y + (int)(r * 0.7);
            g2.drawLine(lx, ly, x + sz, y + sz);
            g2.dispose();
        }
    }

    static class ListIcon implements Icon {
        final int sz; final Color c;
        ListIcon(int sz, Color c) { this.sz = sz; this.c = c; }
        @Override public int getIconWidth()  { return sz + 2; }
        @Override public int getIconHeight() { return sz; }
        @Override public void paintIcon(Component comp, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int gap = sz / 4;
            for (int i = 0; i < 3; i++)
                g2.drawLine(x + 2, y + gap + i * gap, x + sz, y + gap + i * gap);
            g2.dispose();
        }
    }
}