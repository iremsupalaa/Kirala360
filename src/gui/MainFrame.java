package gui;

import models.Arac;
import models.Kiralama;
import models.Musteri;
import services.AracService;
import services.DosyaService;
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

    // ── Renkler ──────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(243, 244, 248);
    private static final Color CARD_BG     = Color.WHITE;
    private static final Color CARD_BDR    = new Color(212, 218, 232);
    private static final Color TITLE_FG    = new Color(18, 25, 50);
    private static final Color ACCENT      = new Color(41, 98, 255);
    private static final Color BTN_EKLE    = new Color(41, 98, 255);
    private static final Color BTN_KIRALA  = new Color(34, 170, 90);
    private static final Color TH_BG       = new Color(232, 238, 250);
    private static final Color TH_FG       = new Color(40, 52, 82);
    private static final Color GRID        = new Color(220, 226, 238);
    private static final Color ROW_EVEN    = Color.WHITE;
    private static final Color ROW_ODD     = new Color(248, 250, 255);
    private static final Color MUSAIT_BG   = new Color(204, 244, 220);
    private static final Color MUSAIT_FG   = new Color(20, 125, 60);
    private static final Color KIRADA_BG   = new Color(255, 215, 215);
    private static final Color KIRADA_FG   = new Color(180, 30, 30);
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
    private JLabel tableTitleLabel;
    private AracService aracService;
    private KiralamaService kiralamaService;

    // ════════════════════════════════════════════════════════════════════════
    public MainFrame() {
        aracService     = new AracService();
        kiralamaService = new KiralamaService(aracService.getAracListesi());

        setTitle("Araç Kiralama Sistemi");
        setSize(1100, 800);
        setMinimumSize(new Dimension(1100, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(null);
        root.setBackground(BG);
        add(root);

        // ── BAŞLIK ────────────────────────────────────────────────────────
        JLabel title = new JLabel("Araç Kiralama Sistemi", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(TITLE_FG);
        title.setBounds(0, 14, 1100, 44);
        root.add(title);

        // ── SOL KART: ARAÇ EKLE  ─────────────────────────────────────────
        RoundCard ekleCard = new RoundCard(14);
        ekleCard.setBounds(22, 68, 298, 378);
        root.add(ekleCard);
        ekleCard.add(headerLabel("Araç Ekle", new CarIcon(15, ACCENT)));
        ekleCard.add(blueLine());
        ekleCard.add(vgap(10));
        idField    = inputRow(ekleCard, "ID:");
        markaField = inputRow(ekleCard, "Marka:");
        modelField = inputRow(ekleCard, "Model:");
        fiyatField = inputRow(ekleCard, "Fiyat (Günlük):");
        ekleCard.add(vgap(4));
        JButton ekleBtn = pillButton("  Araç Ekle", BTN_EKLE, Color.WHITE,
                new PlusIcon(13, Color.WHITE));
        ekleCard.add(ekleBtn);

        // ── SOL KART: ARAÇ KİRALA  ───────────────────────────────────────
        RoundCard kiralaCard = new RoundCard(14);
        kiralaCard.setBounds(22, 460, 298, 295);
        root.add(kiralaCard);
        kiralaCard.add(headerLabel("Araç Kirala", new PersonIcon(15, ACCENT)));
        kiralaCard.add(blueLine());
        kiralaCard.add(vgap(10));
        kiralamaIdField = inputRow(kiralaCard, "Araç ID:");
        musteriField    = inputRow(kiralaCard, "Müşteri:");
        gunField        = inputRow(kiralaCard, "Gün:");
        kiralaCard.add(vgap(4));
        JButton kiralaBtn = pillButton("  Araç Kirala", BTN_KIRALA, Color.WHITE,
                new SmallCarIcon(13, Color.WHITE));
        kiralaCard.add(kiralaBtn);

        // ── TABLO ─────────────────────────────────────────────────────────
        RoundCard tableCard = new RoundCard(14);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBounds(334, 68, 742, 656);
        root.add(tableCard);

        String[] cols = {"ID", "Marka", "Model", "Fiyat (Günlük)", "Müsaitlik"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        aracTable = new JTable(tableModel);
        aracTable.setRowHeight(38);
        aracTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        aracTable.setGridColor(GRID);
        aracTable.setShowGrid(true);
        aracTable.setIntercellSpacing(new Dimension(0, 0));
        aracTable.setSelectionBackground(new Color(200, 220, 255));
        aracTable.setSelectionForeground(TITLE_FG);
        aracTable.setBackground(CARD_BG);
        aracTable.setFocusable(false);
        aracTable.setFillsViewportHeight(true);

        int[] cw = {65, 155, 155, 155, 120};
        for (int i = 0; i < cw.length; i++)
            aracTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);

        JTableHeader th = aracTable.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 14));
        th.setBackground(TH_BG);
        th.setForeground(TH_FG);
        th.setPreferredSize(new Dimension(0, 40));
        th.setReorderingAllowed(false);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRID));

        DefaultTableCellRenderer hr = new DefaultTableCellRenderer() {
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
            aracTable.getColumnModel().getColumn(i).setHeaderRenderer(hr);

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
                if (col == 4 && v != null && t.getColumnCount() == 5) {
                    // Standart görünüm: Müsaitlik sütunu
                    setHorizontalAlignment(CENTER);
                    setFont(new Font("SansSerif", Font.BOLD, 13));
                    boolean m = "Müsait".equals(v.toString());
                    if (!sel) { setBackground(m ? MUSAIT_BG : KIRADA_BG);
                        setForeground(m ? MUSAIT_FG : KIRADA_FG); }
                }
                if (col == 5 && v != null && t.getColumnCount() == 6) {
                    // Kiradaki araçlar: Müsait Olacağı Tarih sütunu — sarı
                    setHorizontalAlignment(CENTER);
                    if (!sel) {
                        setBackground(new Color(255, 243, 180));
                        setForeground(new Color(150, 100, 0));
                    }
                }
                return this;
            }
        });

        // Tablo başlık paneli (NORTH)
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRID));
        tableTitleLabel = new JLabel("Tüm Araçlar");
        tableTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableTitleLabel.setForeground(TITLE_FG);
        titleBar.add(tableTitleLabel);
        tableCard.add(titleBar, BorderLayout.NORTH);

        // ── SAĞ TIKLA → Görünüme göre: Araç Sil veya Kiralama İptal ────────
        aracTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) return;
                int row = aracTable.rowAtPoint(e.getPoint());
                if (row < 0) return;
                aracTable.setRowSelectionInterval(row, row);

                boolean kiradaGorunumu = tableModel.getColumnCount() == 6;

                if (kiradaGorunumu) {
                    // ── Kiralama İptal ────────────────────────────────────
                    Object idObj  = tableModel.getValueAt(row, 0);
                    Object marka  = tableModel.getValueAt(row, 1);
                    Object model  = tableModel.getValueAt(row, 2);
                    Object musteri = tableModel.getValueAt(row, 3);

                    int secim = JOptionPane.showConfirmDialog(
                            MainFrame.this,
                            "<html><b>" + marka + " " + model + "</b> aracının<br>"
                                    + "<b>" + musteri + "</b> adlı müşteriye ait kiralamasını iptal etmek istiyor musunuz?</html>",
                            "Kiralama İptal",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (secim == JOptionPane.YES_OPTION) {
                        int aracId = Integer.parseInt(idObj.toString());
                        kiralamaService.kiralamaIptalEt(aracId);
                        // Araç müsaitlik durumu değişti — araç dosyasını güncelle
                        new DosyaService().araclariKaydet(aracService.getAracListesi());
                        kiradakiAraclariGoster();
                    }

                } else {
                    // ── Araç Sil ──────────────────────────────────────────
                    Object idObj = tableModel.getValueAt(row, 0);
                    Object marka = tableModel.getValueAt(row, 1);
                    Object model = tableModel.getValueAt(row, 2);

                    int secim = JOptionPane.showConfirmDialog(
                            MainFrame.this,
                            "<html><b>" + marka + " " + model + "</b> aracını silmek istiyor musunuz?</html>",
                            "Araç Sil",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (secim == JOptionPane.YES_OPTION) {
                        int id = Integer.parseInt(idObj.toString());
                        aracService.aracSil(id);
                        tabloyuYenile();
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(aracTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(CARD_BG);
        tableCard.add(scroll, BorderLayout.CENTER);

        // ── ALT BUTONLAR  ─────────────────────────────────────────────────
        // y = 68 + 656 + 10 = 734
        RoundButton musaitBtn = new RoundButton("  Müsait Araçlar",
                BOT_BLU_BG, BOT_BLU_FG, new SearchIcon(13, BOT_BLU_FG), 10);
        musaitBtn.setBounds(334, 736, 232, 42);

        RoundButton tumBtn = new RoundButton("  Tüm Araçlar",
                BOT_PRP_BG, BOT_PRP_FG, new ListIcon(13, BOT_PRP_FG), 10);
        tumBtn.setBounds(574, 736, 232, 42);

        RoundButton kiradaBtn = new RoundButton("  Kiradaki Araçlar",
                BOT_ORG_BG, BOT_ORG_FG, new SmallCarIcon(13, BOT_ORG_FG), 10);
        kiradaBtn.setBounds(814, 736, 262, 42);

        root.add(musaitBtn);
        root.add(tumBtn);
        root.add(kiradaBtn);

        // ── ACTION ────────────────────────────────────────────────────────
        ekleBtn.addActionListener(e -> aracEkle());
        kiralaBtn.addActionListener(e -> aracKirala());
        musaitBtn.addActionListener(e -> {
            tableTitleLabel.setText("Müsait Araçlar");
            setStandardColumns();
            tableModel.setRowCount(0);
            for (Arac a : aracService.musaitAraclariGetir())
                tableModel.addRow(tableRow(a, "Müsait"));
        });
        tumBtn.addActionListener(e -> {
            tableTitleLabel.setText("Tüm Araçlar");
            tabloyuYenile();
        });
        kiradaBtn.addActionListener(e -> {
            tableTitleLabel.setText("Kiradaki Araçlar");
            kiradakiAraclariGoster();
        });

        tabloyuYenile();
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // UI YARDIMCI
    // ════════════════════════════════════════════════════════════════════════

    /** Yuvarlak köşeli beyaz kart */
    static class RoundCard extends JPanel {
        private final int r;
        RoundCard(int r) {
            this.r = r;
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(16, 18, 16, 18));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // hafif gölge
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fill(new RoundRectangle2D.Float(2, 3, getWidth()-3, getHeight()-2, r+2, r+2));
            // kart
            g2.setColor(CARD_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-2, getHeight()-2, r, r));
            // kenarlık
            g2.setColor(CARD_BDR);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-3, getHeight()-3, r, r));
            g2.dispose();
        }
    }

    /**
     * Gerçek yuvarlak köşeli buton — Swing'in varsayılan L&F paint'ini tamamen
     * ezmek için paintComponent override edildi.
     */
    static class RoundButton extends JButton {
        private final Color bg, fg;
        private final int radius;
        private Color currentBg;

        RoundButton(String text, Color bg, Color fg, Icon icon, int radius) {
            super(text, icon);
            this.bg = bg; this.fg = fg; this.radius = radius;
            this.currentBg = bg;
            setForeground(fg);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setFocusPainted(false);
            setContentAreaFilled(false); // kendi çiziyoruz
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 16, 8, 16));

            Color dark = bg.darker();
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    currentBg = dark; repaint();
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    currentBg = bg; repaint();
                }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(currentBg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius*2, radius*2));
            // kenarlık
            g2.setColor(currentBg.darker());
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, radius*2, radius*2));
            g2.dispose();
            super.paintComponent(g); // metin + ikon
        }
    }

    /** Pill (hap) şeklinde tam yuvarlak ana buton */
    private JButton pillButton(String text, Color bg, Color fg, Icon icon) {
        RoundButton btn = new RoundButton(text, bg, fg, icon, 22);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
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

    // ════════════════════════════════════════════════════════════════════════
    // İŞ MANTIĞI
    // ════════════════════════════════════════════════════════════════════════

    private Object[] tableRow(Arac a, String durum) {
        return new Object[]{a.getId(), a.getMarka(), a.getModel(),
                formatFiyat(a.getGunlukFiyat()), durum};
    }

    private void aracEkle() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            double fiyat = Double.parseDouble(fiyatField.getText().trim().replace(",", "."));
            aracService.aracEkle(new Arac(id, markaField.getText().trim(),
                    modelField.getText().trim(), fiyat, true));
            tabloyuYenile(); temizle();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID ve Fiyat sayısal olmalıdır.",
                    "Hatalı Giriş", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aracKirala() {
        try {
            int id  = Integer.parseInt(kiralamaIdField.getText().trim());
            int gun = Integer.parseInt(gunField.getText().trim());
            Arac arac = aracService.aracGetir(id);
            if (arac == null || !arac.isMusaitMi()) {
                JOptionPane.showMessageDialog(this, "Araç mevcut değil veya müsait değil.",
                        "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }
            kiralamaService.aracKirala(new Kiralama(1, arac,
                    new Musteri(1, musteriField.getText().trim(), "555"), gun));
            tabloyuYenile(); temizle();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Araç ID ve Gün sayısal olmalıdır.",
                    "Hatalı Giriş", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Tüm / müsait araçlar için standart 5 sütunlu görünüm */
    private void setStandardColumns() {
        tableModel.setColumnCount(0);
        for (String col : new String[]{"ID", "Marka", "Model", "Fiyat (Günlük)", "Müsaitlik"})
            tableModel.addColumn(col);
        int[] cw = {65, 155, 155, 155, 120};
        for (int i = 0; i < cw.length; i++)
            aracTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);
    }

    /** Kiradaki araçlar için 6 sütunlu görünüm */
    private void setKiradaColumns() {
        tableModel.setColumnCount(0);
        for (String col : new String[]{"ID", "Marka", "Model", "Müşteri", "Kiralandığı Tarih", "Müsaitlik Tarihi"})
            tableModel.addColumn(col);
        int[] cw = {55, 130, 130, 120, 140, 155};
        for (int i = 0; i < cw.length; i++)
            aracTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);
    }

    private void tabloyuYenile() {
        setStandardColumns();
        tableModel.setRowCount(0);
        for (Arac a : aracService.getAracListesi())
            tableModel.addRow(tableRow(a, a.isMusaitMi() ? "Müsait" : "Kirada"));
    }

    private void kiradakiAraclariGoster() {
        setKiradaColumns();
        tableModel.setRowCount(0);
        for (Kiralama k : kiralamaService.getKiralamaListesi())
            tableModel.addRow(new Object[]{
                    k.getArac().getId(),
                    k.getArac().getMarka(),
                    k.getArac().getModel(),
                    k.getMusteri().getAdSoyad(),
                    k.getKiralamaTarihiStr(),
                    k.getMusaitOlacakTarihStr()
            });
    }

    private void temizle() {
        for (JTextField f : new JTextField[]{idField, markaField, modelField, fiyatField,
                kiralamaIdField, musteriField, gunField})
            f.setText("");
    }

    private String formatFiyat(double v) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("tr", "TR"));
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(2);
        return nf.format(v) + " \u20BA";
    }

    // ════════════════════════════════════════════════════════════════════════
    // CUSTOM IKONLAR  (Graphics2D — her sistemde çalışır)
    // ════════════════════════════════════════════════════════════════════════

    static class CarIcon implements Icon {
        final int sz; final Color c;
        CarIcon(int sz, Color c) { this.sz=sz; this.c=c; }
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
            g2.fillOval(x+sz/10,           y+sz*5/8, sz/4,sz/4);
            g2.fillOval(x+sz-sz/10-sz/4,   y+sz*5/8, sz/4,sz/4);
            g2.setColor(c); g2.setStroke(new BasicStroke(1f));
            g2.drawOval(x+sz/10,           y+sz*5/8, sz/4,sz/4);
            g2.drawOval(x+sz-sz/10-sz/4,   y+sz*5/8, sz/4,sz/4);
            g2.dispose();
        }
    }
    static class SmallCarIcon extends CarIcon {
        SmallCarIcon(int sz,Color c){super(sz,c);}
    }
    static class PersonIcon implements Icon {
        final int sz; final Color c;
        PersonIcon(int sz,Color c){this.sz=sz;this.c=c;}
        @Override public int getIconWidth()  {return sz;}
        @Override public int getIconHeight() {return sz;}
        @Override public void paintIcon(Component comp,Graphics g,int x,int y){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            int hw=sz*2/5;
            g2.fillOval(x+(sz-hw)/2,y,hw,hw);
            g2.fillArc(x,y+sz/2,sz,sz,0,180);
            g2.dispose();
        }
    }
    static class PlusIcon implements Icon {
        final int sz; final Color c;
        PlusIcon(int sz,Color c){this.sz=sz;this.c=c;}
        @Override public int getIconWidth()  {return sz;}
        @Override public int getIconHeight() {return sz;}
        @Override public void paintIcon(Component comp,Graphics g,int x,int y){
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
    static class SearchIcon implements Icon {
        final int sz; final Color c;
        SearchIcon(int sz,Color c){this.sz=sz;this.c=c;}
        @Override public int getIconWidth()  {return sz+2;}
        @Override public int getIconHeight() {return sz+2;}
        @Override public void paintIcon(Component comp,Graphics g,int x,int y){
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
    static class ListIcon implements Icon {
        final int sz; final Color c;
        ListIcon(int sz,Color c){this.sz=sz;this.c=c;}
        @Override public int getIconWidth()  {return sz+2;}
        @Override public int getIconHeight() {return sz;}
        @Override public void paintIcon(Component comp,Graphics g,int x,int y){
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