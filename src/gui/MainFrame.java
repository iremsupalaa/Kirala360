package gui;

import models.Arac;
import models.Kiralama;
import models.Musteri;
import services.AracService;
import services.DosyaService;
import services.KiralamaService;
import services.SiralamaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainFrame extends JFrame {

    // ── Servisler ─────────────────────────────────────────────────────────────
    private final AracService     aracService;
    private final KiralamaService kiralamaService;
    private final SiralamaService siralamaService;

    // ── Form alanları ─────────────────────────────────────────────────────────
    private JTextField idField, markaField, modelField, fiyatField;
    private JTextField kiralamaIdField, musteriField, gunField;

    // ── Tablo ─────────────────────────────────────────────────────────────────
    private JTable            aracTable;
    private DefaultTableModel tableModel;
    private JLabel            tableTitleLabel;

    // ── Filtre ────────────────────────────────────────────────────────────────
    private JTextField aramaField, minFiyatField, maxFiyatField;
    private JPanel     filterBar;
    private String     aktifGorunum = "tum"; // "tum" | "musait" | "kirada"

    // ════════════════════════════════════════════════════════════════════════
    public MainFrame() {
        aracService     = new AracService();
        kiralamaService = new KiralamaService(aracService.getAracListesi());
        siralamaService = new SiralamaService();

        setTitle("Araç Kiralama Sistemi");
        setSize(1100, 840);
        setMinimumSize(new Dimension(1100, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(null);
        root.setBackground(AppColors.BG);
        add(root);

        buildTitle(root);
        buildSolPanel(root);
        buildTabloPanel(root);
        buildAltButonlar(root);

        tabloyuYenile();
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // UI KURULUM
    // ════════════════════════════════════════════════════════════════════════

    private void buildTitle(JPanel root) {
        JLabel title = new JLabel("Araç Kiralama Sistemi", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(AppColors.TITLE_FG);
        title.setBounds(0, 14, 1100, 44);
        root.add(title);
    }

    private void buildSolPanel(JPanel root) {
        // ── Araç Ekle kartı ──────────────────────────────────────────────────
        UIFactory.RoundCard ekleCard = new UIFactory.RoundCard(14);
        ekleCard.setBounds(22, 68, 298, 378);
        root.add(ekleCard);

        ekleCard.add(UIFactory.headerLabel("Araç Ekle", new UIFactory.CarIcon(15, AppColors.ACCENT)));
        ekleCard.add(UIFactory.blueLine());
        ekleCard.add(UIFactory.vgap(10));
        idField    = UIFactory.inputRow(ekleCard, "ID:");
        markaField = UIFactory.inputRow(ekleCard, "Marka:");
        modelField = UIFactory.inputRow(ekleCard, "Model:");
        fiyatField = UIFactory.inputRow(ekleCard, "Fiyat (Günlük):");
        ekleCard.add(UIFactory.vgap(4));
        JButton ekleBtn = UIFactory.pillButton("  Araç Ekle", AppColors.BTN_EKLE,
                Color.WHITE, new UIFactory.PlusIcon(13, Color.WHITE));
        ekleCard.add(ekleBtn);
        ekleBtn.addActionListener(e -> aracEkle());

        // ── Araç Kirala kartı ────────────────────────────────────────────────
        UIFactory.RoundCard kiralaCard = new UIFactory.RoundCard(14);
        kiralaCard.setBounds(22, 460, 298, 295);
        root.add(kiralaCard);

        kiralaCard.add(UIFactory.headerLabel("Araç Kirala", new UIFactory.PersonIcon(15, AppColors.ACCENT)));
        kiralaCard.add(UIFactory.blueLine());
        kiralaCard.add(UIFactory.vgap(10));
        kiralamaIdField = UIFactory.inputRow(kiralaCard, "Araç ID:");
        musteriField    = UIFactory.inputRow(kiralaCard, "Müşteri:");
        gunField        = UIFactory.inputRow(kiralaCard, "Gün:");
        kiralaCard.add(UIFactory.vgap(4));
        JButton kiralaBtn = UIFactory.pillButton("  Araç Kirala", AppColors.BTN_KIRALA,
                Color.WHITE, new UIFactory.SmallCarIcon(13, Color.WHITE));
        kiralaCard.add(kiralaBtn);
        kiralaBtn.addActionListener(e -> aracKirala());
    }

    private void buildTabloPanel(JPanel root) {
        UIFactory.RoundCard tableCard = new UIFactory.RoundCard(14);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBounds(334, 68, 742, 656);
        root.add(tableCard);

        // Tablo modeli
        String[] cols = {"ID", "Marka", "Model", "Fiyat (Günlük)", "Müsaitlik"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        aracTable = new JTable(tableModel);
        aracTable.setRowHeight(38);
        aracTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        aracTable.setGridColor(AppColors.GRID);
        aracTable.setShowGrid(true);
        aracTable.setIntercellSpacing(new Dimension(0, 0));
        aracTable.setSelectionBackground(new Color(200, 220, 255));
        aracTable.setSelectionForeground(AppColors.TITLE_FG);
        aracTable.setBackground(AppColors.CARD_BG);
        aracTable.setFocusable(false);
        aracTable.setFillsViewportHeight(true);

        int[] cw = {65, 155, 155, 155, 120};
        for (int i = 0; i < cw.length; i++)
            aracTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);

        // Header
        JTableHeader th = aracTable.getTableHeader();
        th.setFont(new Font("SansSerif", Font.BOLD, 14));
        th.setBackground(AppColors.TH_BG);
        th.setForeground(AppColors.TH_FG);
        th.setPreferredSize(new Dimension(0, 40));
        th.setReorderingAllowed(false);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.GRID));

        DefaultTableCellRenderer headerRend = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(AppColors.TH_BG); setForeground(AppColors.TH_FG);
                setFont(new Font("SansSerif", Font.BOLD, 14));
                setBorder(new EmptyBorder(0, 14, 0, 8));
                return this;
            }
        };
        for (int i = 0; i < cols.length; i++)
            aracTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRend);

        // Hücre renderer
        aracTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 14, 0, 8));
                if (!sel) {
                    setBackground(row % 2 == 0 ? AppColors.ROW_EVEN : AppColors.ROW_ODD);
                    setForeground(AppColors.TITLE_FG);
                }
                setFont(new Font("SansSerif", Font.PLAIN, 14));
                setHorizontalAlignment(col == 0 ? CENTER : LEFT);
                // Müsaitlik sütunu (standart 5 sütun)
                if (col == 4 && v != null && t.getColumnCount() == 5) {
                    setHorizontalAlignment(CENTER);
                    setFont(new Font("SansSerif", Font.BOLD, 13));
                    boolean m = "Müsait".equals(v.toString());
                    if (!sel) { setBackground(m ? AppColors.MUSAIT_BG : AppColors.KIRADA_BG);
                        setForeground(m ? AppColors.MUSAIT_FG : AppColors.KIRADA_FG); }
                }
                // Müsait olacağı tarih (kirada 6 sütun)
                if (col == 5 && v != null && t.getColumnCount() == 6) {
                    setHorizontalAlignment(CENTER);
                    if (!sel) { setBackground(new Color(255, 243, 180));
                        setForeground(new Color(150, 100, 0)); }
                }
                return this;
            }
        });

        // Kuzey panel: başlık + filtre barı
        tableCard.add(buildNorthPanel(), BorderLayout.NORTH);

        // Sağ tıklama
        aracTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) return;
                int row = aracTable.rowAtPoint(e.getPoint());
                if (row < 0) return;
                aracTable.setRowSelectionInterval(row, row);
                if (tableModel.getColumnCount() == 6) onKiralamaIptal(row);
                else                                  onAracSil(row);
            }
        });

        JScrollPane scroll = new JScrollPane(aracTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(AppColors.CARD_BG);
        tableCard.add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildNorthPanel() {
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.setOpaque(false);

        // Başlık satırı
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.GRID));
        tableTitleLabel = new JLabel("Tüm Araçlar");
        tableTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableTitleLabel.setForeground(AppColors.TITLE_FG);
        titleBar.add(tableTitleLabel);
        north.add(titleBar);

        // Filtre barı
        filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterBar.setOpaque(false);
        filterBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.GRID));

        aramaField    = filterTextField(160);
        minFiyatField = filterTextField(80);
        maxFiyatField = filterTextField(80);

        UIFactory.RoundButton filtreBtn  = new UIFactory.RoundButton("Ara", AppColors.ACCENT,
                Color.WHITE, new UIFactory.SearchIcon(12, Color.WHITE), 8);
        filtreBtn.setPreferredSize(new Dimension(100, 28));

        UIFactory.RoundButton temizleBtn = new UIFactory.RoundButton("  Temizle",
                new Color(230, 232, 240), AppColors.LABEL_FG, null, 8);
        temizleBtn.setPreferredSize(new Dimension(95, 28));

        filterBar.add(filterLabel("Marka / Model:"));
        filterBar.add(aramaField);
        filterBar.add(filterLabel("Min ₺:"));
        filterBar.add(minFiyatField);
        filterBar.add(filterLabel("Max ₺:"));
        filterBar.add(maxFiyatField);
        filterBar.add(filtreBtn);
        filterBar.add(temizleBtn);
        north.add(filterBar);

        filtreBtn.addActionListener(e -> filtreUygula());
        temizleBtn.addActionListener(e -> filtreSifirla());
        aramaField.addActionListener(e -> filtreUygula());
        minFiyatField.addActionListener(e -> filtreUygula());
        maxFiyatField.addActionListener(e -> filtreUygula());

        return north;
    }

    private void buildAltButonlar(JPanel root) {
        // 1. satır: görünüm butonları
        UIFactory.RoundButton musaitBtn = new UIFactory.RoundButton("  Müsait Araçlar",
                AppColors.BOT_BLU_BG, AppColors.BOT_BLU_FG,
                new UIFactory.SearchIcon(13, AppColors.BOT_BLU_FG), 10);
        musaitBtn.setBounds(334, 736, 232, 42);

        UIFactory.RoundButton tumBtn = new UIFactory.RoundButton("  Tüm Araçlar",
                AppColors.BOT_PRP_BG, AppColors.BOT_PRP_FG,
                new UIFactory.ListIcon(13, AppColors.BOT_PRP_FG), 10);
        tumBtn.setBounds(574, 736, 232, 42);

        UIFactory.RoundButton kiradaBtn = new UIFactory.RoundButton("  Kiradaki Araçlar",
                AppColors.BOT_ORG_BG, AppColors.BOT_ORG_FG,
                new UIFactory.SmallCarIcon(13, AppColors.BOT_ORG_FG), 10);
        kiradaBtn.setBounds(814, 736, 262, 42);

        // 2. satır: sıralama + istatistik
        UIFactory.RoundButton siralaArtanBtn = new UIFactory.RoundButton(
                "  Fiyata Göre Sırala ↑", AppColors.SORT_BG, AppColors.SORT_FG, null, 10);
        siralaArtanBtn.setBounds(334, 786, 230, 38);

        UIFactory.RoundButton siralaAzalanBtn = new UIFactory.RoundButton(
                "  Fiyata Göre Sırala ↓", AppColors.SORT_BG, AppColors.SORT_FG, null, 10);
        siralaAzalanBtn.setBounds(572, 786, 230, 38);

        UIFactory.RoundButton istatistikBtn = new UIFactory.RoundButton(
                "  İstatistikler", AppColors.STAT_BG, AppColors.STAT_FG, null, 10);
        istatistikBtn.setBounds(810, 786, 266, 38);

        root.add(musaitBtn);
        root.add(tumBtn);
        root.add(kiradaBtn);
        root.add(siralaArtanBtn);
        root.add(siralaAzalanBtn);
        root.add(istatistikBtn);

        musaitBtn.addActionListener(e -> musaitAraclariGoster());
        tumBtn.addActionListener(e -> { tableTitleLabel.setText("Tüm Araçlar"); tabloyuYenile(); });
        kiradaBtn.addActionListener(e -> { tableTitleLabel.setText("Kiradaki Araçlar"); kiradakiAraclariGoster(); });
        siralaArtanBtn.addActionListener(e -> sirala(true));
        siralaAzalanBtn.addActionListener(e -> sirala(false));
        istatistikBtn.addActionListener(e ->
                new IstatistikDialog(this, aracService.getAracListesi(),
                        kiralamaService.getKiralamaListesi()).setVisible(true));
    }

    // ════════════════════════════════════════════════════════════════════════
    // SAĞ TIKLA AKSIYONLAR
    // ════════════════════════════════════════════════════════════════════════

    private void onKiralamaIptal(int row) {
        Object id     = tableModel.getValueAt(row, 0);
        Object marka  = tableModel.getValueAt(row, 1);
        Object model  = tableModel.getValueAt(row, 2);
        Object musteri = tableModel.getValueAt(row, 3);
        int secim = JOptionPane.showConfirmDialog(this,
                "<html><b>" + marka + " " + model + "</b> aracının<br>"
                        + "<b>" + musteri + "</b> adlı müşteriye ait kiralamasını iptal etmek istiyor musunuz?</html>",
                "Kiralama İptal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (secim == JOptionPane.YES_OPTION) {
            kiralamaService.kiralamaIptalEt(Integer.parseInt(id.toString()));
            new DosyaService().araclariKaydet(aracService.getAracListesi());
            kiradakiAraclariGoster();
        }
    }

    private void onAracSil(int row) {
        Object id    = tableModel.getValueAt(row, 0);
        Object marka = tableModel.getValueAt(row, 1);
        Object model = tableModel.getValueAt(row, 2);
        int secim = JOptionPane.showConfirmDialog(this,
                "<html><b>" + marka + " " + model + "</b> aracını silmek istiyor musunuz?</html>",
                "Araç Sil", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (secim == JOptionPane.YES_OPTION) {
            aracService.aracSil(Integer.parseInt(id.toString()));
            tabloyuYenile();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // İŞ MANTIĞI
    // ════════════════════════════════════════════════════════════════════════

    private void aracEkle() {
        try {
            int    id    = Integer.parseInt(idField.getText().trim());
            double fiyat = Double.parseDouble(fiyatField.getText().trim().replace(",", "."));
            aracService.aracEkle(new Arac(id, markaField.getText().trim(),
                    modelField.getText().trim(), fiyat, true));
            tabloyuYenile();
            temizle();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID ve Fiyat sayısal olmalıdır.", "Hatalı Giriş", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aracKirala() {
        try {
            int  id  = Integer.parseInt(kiralamaIdField.getText().trim());
            int  gun = Integer.parseInt(gunField.getText().trim());
            Arac arac = aracService.aracGetir(id);
            if (arac == null || !arac.isMusaitMi()) {
                JOptionPane.showMessageDialog(this, "Araç mevcut değil veya müsait değil.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }
            kiralamaService.aracKirala(new Kiralama(1, arac,
                    new Musteri(1, musteriField.getText().trim(), "555"), gun));
            tabloyuYenile();
            temizle();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Araç ID ve Gün sayısal olmalıdır.", "Hatalı Giriş", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // TABLO GÖRÜNÜM METODLARI
    // ════════════════════════════════════════════════════════════════════════

    private void setStandardColumns() {
        tableModel.setColumnCount(0);
        for (String col : new String[]{"ID", "Marka", "Model", "Fiyat (Günlük)", "Müsaitlik"})
            tableModel.addColumn(col);
        int[] cw = {65, 155, 155, 155, 120};
        for (int i = 0; i < cw.length; i++)
            aracTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);
    }

    private void setKiradaColumns() {
        tableModel.setColumnCount(0);
        for (String col : new String[]{"ID", "Marka", "Model", "Müşteri", "Kiralandığı Tarih", "Müsaitlik Tarihi"})
            tableModel.addColumn(col);
        int[] cw = {55, 130, 130, 120, 140, 155};
        for (int i = 0; i < cw.length; i++)
            aracTable.getColumnModel().getColumn(i).setPreferredWidth(cw[i]);
    }

    private void tabloyuYenile() {
        aktifGorunum = "tum";
        setStandardColumns();
        filterBar.setVisible(true);
        tableModel.setRowCount(0);
        for (Arac a : aracService.getAracListesi())
            tableModel.addRow(tableRow(a, a.isMusaitMi() ? "Müsait" : "Kirada"));
    }

    private void musaitAraclariGoster() {
        aktifGorunum = "musait";
        tableTitleLabel.setText("Müsait Araçlar");
        setStandardColumns();
        filterBar.setVisible(true);
        aramaField.setText(""); minFiyatField.setText(""); maxFiyatField.setText("");
        tableModel.setRowCount(0);
        for (Arac a : aracService.musaitAraclariGetir())
            tableModel.addRow(tableRow(a, "Müsait"));
    }

    private void kiradakiAraclariGoster() {
        aktifGorunum = "kirada";
        setKiradaColumns();
        filterBar.setVisible(false);
        tableModel.setRowCount(0);
        for (Kiralama k : kiralamaService.getKiralamaListesi())
            tableModel.addRow(new Object[]{
                    k.getArac().getId(), k.getArac().getMarka(), k.getArac().getModel(),
                    k.getMusteri().getAdSoyad(), k.getKiralamaTarihiStr(), k.getMusaitOlacakTarihStr()
            });
    }

    private void sirala(boolean artan) {
        if (aktifGorunum.equals("kirada")) return;
        ArrayList<Arac> kaynak = aktifGorunum.equals("musait")
                ? aracService.musaitAraclariGetir() : aracService.getAracListesi();
        ArrayList<Arac> sirali = siralamaService.fiyataGoreBubbleSort(kaynak, artan);
        setStandardColumns();
        filterBar.setVisible(true);
        tableModel.setRowCount(0);
        for (Arac a : sirali)
            tableModel.addRow(tableRow(a, a.isMusaitMi() ? "Müsait" : "Kirada"));
        tableTitleLabel.setText("Fiyata Göre Sıralı (" + (artan ? "Artan" : "Azalan") + ")");
    }

    private void filtreUygula() {
        String aranan  = aramaField.getText().trim().toLowerCase();
        double minF    = parseDouble(minFiyatField.getText(), 0);
        double maxF    = parseDouble(maxFiyatField.getText(), Double.MAX_VALUE);
        ArrayList<Arac> kaynak = aktifGorunum.equals("musait")
                ? aracService.musaitAraclariGetir() : aracService.getAracListesi();
        setStandardColumns();
        tableModel.setRowCount(0);
        for (Arac a : kaynak) {
            boolean ad  = aranan.isEmpty()
                    || a.getMarka().toLowerCase().contains(aranan)
                    || a.getModel().toLowerCase().contains(aranan);
            boolean fiy = a.getGunlukFiyat() >= minF && a.getGunlukFiyat() <= maxF;
            if (ad && fiy)
                tableModel.addRow(tableRow(a, a.isMusaitMi() ? "Müsait" : "Kirada"));
        }
        tableTitleLabel.setText("Arama Sonuçları (" + tableModel.getRowCount() + " araç)");
    }

    private void filtreSifirla() {
        aramaField.setText(""); minFiyatField.setText(""); maxFiyatField.setText("");
        tableTitleLabel.setText("Tüm Araçlar");
        tabloyuYenile();
    }

    // ════════════════════════════════════════════════════════════════════════
    // YARDIMCI METODLAR
    // ════════════════════════════════════════════════════════════════════════

    private Object[] tableRow(Arac a, String durum) {
        return new Object[]{a.getId(), a.getMarka(), a.getModel(), fmt(a.getGunlukFiyat()), durum};
    }

    private void temizle() {
        for (JTextField f : new JTextField[]{idField, markaField, modelField, fiyatField,
                kiralamaIdField, musteriField, gunField})
            f.setText("");
    }

    private String fmt(double v) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("tr", "TR"));
        nf.setMinimumFractionDigits(1); nf.setMaximumFractionDigits(2);
        return nf.format(v) + " ₺";
    }

    private double parseDouble(String s, double varsayilan) {
        try { return s.trim().isEmpty() ? varsayilan : Double.parseDouble(s.trim().replace(",", ".")); }
        catch (NumberFormatException e) { return varsayilan; }
    }

    private JLabel filterLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(AppColors.LABEL_FG);
        return l;
    }

    private JTextField filterTextField(int width) {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(width, 28));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.INPUT_BDR, 1),
                new EmptyBorder(2, 7, 2, 7)));
        return f;
    }
}