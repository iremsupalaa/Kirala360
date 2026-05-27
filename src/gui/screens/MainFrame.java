package gui.screens;

import gui.IstatistikDialog;
import gui.UIFactory;
import gui.components.ToastNotification;
import gui.theme.AppColors;
import models.Arac;
import models.Kiralama;
import models.Musteri;
import services.AracService;
import services.DosyaService;
import services.KiralamaService;
import services.SiralamaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import gui.components.buttons.GradientButton;
import gui.components.inputs.ModernTextField;
import gui.components.table.ModernScrollBarUI;
import gui.components.table.ModernTable;
import gui.components.card.StatsCard;
import gui.components.table.ActionCellRenderer;
import gui.components.table.ActionCellEditor;
import gui.components.table.TableStyler;
import gui.components.inputs.FormGroup;

public class MainFrame extends JFrame {

    // ── Servisler ─────────────────────────────────────────────────────────────
    private final AracService     aracService;
    private final KiralamaService kiralamaService;
    private final SiralamaService siralamaService;

    // ── Form alanları ─────────────────────────────────────────────────────────
    private JTextField idField, markaField, modelField, fiyatField;
    private JTextField kiralamaIdField, musteriField, gunField;

    // ── Tablo ─────────────────────────────────────────────────────────────────
    private JTable         aracTable;
    private DefaultTableModel tableModel;
    private JLabel         tableTitleLabel;

    // ── Stats kartları (animasyon için referans) ───────────────────────────────
    private StatsCard toplamCard;
    private StatsCard musaitCard;
    private StatsCard fiyatCard;

    // ── Filtre ────────────────────────────────────────────────────────────────
    private JTextField aramaField, minFiyatField, maxFiyatField;
    private JPanel     filterBar;
    private String     aktifGorunum = "tum";

    // ── Boş tablo mesajı ──────────────────────────────────────────────────────
    private JLabel emptyLabel;

    // ═════════════════════════════════════════════════════════════════════════
    public MainFrame() {
        aracService     = new AracService();
        kiralamaService = new KiralamaService(aracService.getAracListesi());
        siralamaService = new SiralamaService();

        setTitle("Araç Kiralama Sistemi");
        setMinimumSize(new Dimension(1100, 820));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ── Kök panel: BorderLayout ───────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(AppColors.BG);
        add(root);

        // Kuzey: başlık + stats
        root.add(buildNorthBar(), BorderLayout.NORTH);

        // Merkez: sol form + sağ tablo
        root.add(buildCenterPanel(), BorderLayout.CENTER);

        // Güney: butonlar
        root.add(buildSouthBar(), BorderLayout.SOUTH);

        tabloyuYenile();

        pack();
        setSize(1200, 860);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // KUZEY: başlık + stats kartları
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildNorthBar() {
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(AppColors.BG);
        north.setBorder(BorderFactory.createEmptyBorder(36, 24, 8, 24));

        // Başlık (sol)
        JLabel title = new JLabel("Araç Kiralama Sistemi");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(AppColors.TITLE_FG);
        north.add(title, BorderLayout.WEST);

        // Stats kartları (sağ)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        statsPanel.setBackground(AppColors.BG);

        toplamCard = new StatsCard("Toplam Araç", "0",
                new Color(235, 243, 255), new Color(45, 120, 255));
        musaitCard = new StatsCard("Müsait", "0",
                new Color(230, 250, 235), new Color(22, 163, 74));
        fiyatCard  = new StatsCard("Toplam Değer", "0 ₺",
                new Color(255, 245, 225), new Color(210, 120, 20));

        statsPanel.add(toplamCard);
        statsPanel.add(musaitCard);
        statsPanel.add(fiyatCard);

        north.add(statsPanel, BorderLayout.EAST);
        return north;
    }

    // ════════════════════════════════════════════════════════════════════════
    // MERKEZ: sol panel + sağ tablo
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(14, 0));
        center.setBackground(AppColors.BG);
        center.setBorder(BorderFactory.createEmptyBorder(4, 24, 8, 24));

        // Sol: kaydırılabilir form alanı
        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setBackground(AppColors.BG);
        leftWrapper.setPreferredSize(new Dimension(300, 0));

        JPanel leftScroll = buildSolPanel();
        JScrollPane leftSP = new JScrollPane(leftScroll,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftSP.setBorder(BorderFactory.createEmptyBorder());
        leftSP.getViewport().setBackground(AppColors.BG);
        leftSP.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        leftWrapper.add(leftSP, BorderLayout.CENTER);

        center.add(leftWrapper, BorderLayout.WEST);
        center.add(buildTabloPanel(), BorderLayout.CENTER);

        return center;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SOL PANEL: Araç Ekle + Araç Kirala kartları
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildSolPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AppColors.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // ── Araç Ekle kartı ──────────────────────────────────────────────────
        UIFactory.RoundCard ekleCard = new UIFactory.RoundCard(14);
        ekleCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        ekleCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        ekleCard.add(UIFactory.headerLabel("Araç Ekle",
                new UIFactory.CarIcon(15, AppColors.ACCENT)));
        ekleCard.add(UIFactory.blueLine());
        ekleCard.add(UIFactory.vgap(10));

        FormGroup idGroup    = new FormGroup("ID",     "Araç ID");
        FormGroup markaGroup = new FormGroup("Marka",  "BMW");
        FormGroup modelGroup = new FormGroup("Model",  "320i");
        FormGroup fiyatGroup = new FormGroup("Fiyat",  "1500");

        idField    = idGroup.getField();
        markaField = markaGroup.getField();
        modelField = modelGroup.getField();
        fiyatField = fiyatGroup.getField();

        for (FormGroup fg : new FormGroup[]{idGroup, markaGroup, modelGroup, fiyatGroup}) {
            ekleCard.add(fg);
            ekleCard.add(UIFactory.vgap(10));
        }

        GradientButton ekleBtn = new GradientButton("Araç Ekle",
                new Color(45, 120, 255), new Color(20, 90, 220));
        ekleBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        ekleCard.add(ekleBtn);
        ekleBtn.addActionListener(e -> aracEkle());

        // ── Araç Kirala kartı ────────────────────────────────────────────────
        UIFactory.RoundCard kiralaCard = new UIFactory.RoundCard(14);
        kiralaCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        kiralaCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        kiralaCard.add(UIFactory.headerLabel("Araç Kirala",
                new UIFactory.PersonIcon(15, AppColors.ACCENT)));
        kiralaCard.add(UIFactory.blueLine());
        kiralaCard.add(UIFactory.vgap(10));

        FormGroup aracIdGroup  = new FormGroup("Araç ID",  "1");
        FormGroup musteriGroup = new FormGroup("Müşteri",  "İsim");
        FormGroup gunGroup     = new FormGroup("Gün",      "7");

        kiralamaIdField = aracIdGroup.getField();
        musteriField    = musteriGroup.getField();
        gunField        = gunGroup.getField();

        for (FormGroup fg : new FormGroup[]{aracIdGroup, musteriGroup, gunGroup}) {
            kiralaCard.add(fg);
            kiralaCard.add(UIFactory.vgap(10));
        }

        GradientButton kiralaBtn = new GradientButton("Araç Kirala",
                new Color(34, 197, 94), new Color(22, 163, 74));
        kiralaBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        kiralaCard.add(kiralaBtn);
        kiralaBtn.addActionListener(e -> aracKirala());

        panel.add(ekleCard);
        panel.add(Box.createVerticalStrut(14));
        panel.add(kiralaCard);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TABLO PANELİ
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildTabloPanel() {
        UIFactory.RoundCard tableCard = new UIFactory.RoundCard(14);
        tableCard.setLayout(new BorderLayout());

        String[] cols = {"ID", "Marka", "Model", "Fiyat (Günlük)", "Müsaitlik", "İşlemler"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                return c == getColumnCount() - 1;
            }
        };

        aracTable = new ModernTable();
        aracTable.setModel(tableModel);
        aracTable.getColumnModel().getColumn(5).setCellRenderer(new ActionCellRenderer());
        aracTable.getColumnModel().getColumn(5).setCellEditor(new ActionCellEditor());

        tableCard.add(buildTableNorthPanel(), BorderLayout.NORTH);

        // Boş mesaj katmanı
        JLayeredPane layered = new JLayeredPane();
        layered.setLayout(new OverlayLayout(layered));

        JScrollPane scroll = new JScrollPane(aracTable);
        scroll.getViewport().setBackground(Color.WHITE);
        aracTable.setRowHeight(48);
        aracTable.setFillsViewportHeight(true);
        TableStyler.setColumnWidths(aracTable, 65, 145, 145, 145, 120, 120);
        TableStyler.styleTable(aracTable, scroll);

        emptyLabel = new JLabel("Araç bulunamadı", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emptyLabel.setForeground(new Color(160, 168, 190));
        emptyLabel.setVisible(false);

        layered.add(scroll,     JLayeredPane.DEFAULT_LAYER);
        layered.add(emptyLabel, JLayeredPane.PALETTE_LAYER);
        tableCard.add(layered, BorderLayout.CENTER);

        // Sağ tıklama
        aracTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) return;
                int row = aracTable.rowAtPoint(e.getPoint());
                if (row < 0) return;
                aracTable.setRowSelectionInterval(row, row);
                if (aktifGorunum.equals("kirada")) onKiralamaIptal(row);
                else                               onAracSil(row);
            }
        });

        return tableCard;
    }

    private JPanel buildTableNorthPanel() {
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.setOpaque(false);

        // Başlık satırı
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, AppColors.GRID));
        tableTitleLabel = new JLabel("Tüm Araçlar");
        tableTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableTitleLabel.setForeground(AppColors.TITLE_FG);
        titleBar.add(tableTitleLabel);
        north.add(titleBar);

        // Filtre barı
        filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterBar.setOpaque(false);
        filterBar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, AppColors.GRID));

        aramaField    = new ModernTextField("Marka veya model ara");
        minFiyatField = new ModernTextField("Min");
        maxFiyatField = new ModernTextField("Max");
        aramaField.setPreferredSize(new Dimension(160, 36));
        minFiyatField.setPreferredSize(new Dimension(80, 36));
        maxFiyatField.setPreferredSize(new Dimension(80, 36));

        UIFactory.RoundButton filtreBtn = new UIFactory.RoundButton("Ara",
                AppColors.ACCENT, Color.WHITE,
                new UIFactory.SearchIcon(12, Color.WHITE), 8);
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

    // ════════════════════════════════════════════════════════════════════════
    // GÜNEY: eylem butonları
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildSouthBar() {
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(AppColors.BG);
        south.setBorder(BorderFactory.createEmptyBorder(0, 24, 16, 24));

        // 1. satır: görünüm butonları
        JPanel row1 = new JPanel(new GridLayout(1, 3, 10, 0));
        row1.setBackground(AppColors.BG);
        row1.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        UIFactory.RoundButton musaitBtn = new UIFactory.RoundButton("  Müsait Araçlar",
                AppColors.BOT_BLU_BG, AppColors.BOT_BLU_FG,
                new UIFactory.SearchIcon(13, AppColors.BOT_BLU_FG), 10);
        UIFactory.RoundButton tumBtn = new UIFactory.RoundButton("  Tüm Araçlar",
                AppColors.BOT_PRP_BG, AppColors.BOT_PRP_FG,
                new UIFactory.ListIcon(13, AppColors.BOT_PRP_FG), 10);
        UIFactory.RoundButton kiradaBtn = new UIFactory.RoundButton("  Kiradaki Araçlar",
                AppColors.BOT_ORG_BG, AppColors.BOT_ORG_FG,
                new UIFactory.SmallCarIcon(13, AppColors.BOT_ORG_FG), 10);

        row1.add(musaitBtn);
        row1.add(tumBtn);
        row1.add(kiradaBtn);

        // 2. satır: sıralama + istatistik
        JPanel row2 = new JPanel(new GridLayout(1, 3, 10, 0));
        row2.setBackground(AppColors.BG);

        UIFactory.RoundButton siralaArtanBtn = new UIFactory.RoundButton(
                "  Fiyata Göre Sırala ↑", AppColors.SORT_BG, AppColors.SORT_FG, null, 10);
        UIFactory.RoundButton siralaAzalanBtn = new UIFactory.RoundButton(
                "  Fiyata Göre Sırala ↓", AppColors.SORT_BG, AppColors.SORT_FG, null, 10);
        UIFactory.RoundButton istatistikBtn = new UIFactory.RoundButton(
                "  İstatistikler", AppColors.STAT_BG, AppColors.STAT_FG, null, 10);

        row2.add(siralaArtanBtn);
        row2.add(siralaAzalanBtn);
        row2.add(istatistikBtn);

        JPanel rows = new JPanel(new BorderLayout(0, 0));
        rows.setBackground(AppColors.BG);

        // Sol boşluk: sol panel genişliğiyle hizala
        JPanel placeholder = new JPanel();
        placeholder.setBackground(AppColors.BG);
        placeholder.setPreferredSize(new Dimension(314, 0)); // 300 + 14 gap

        JPanel btnArea = new JPanel(new BorderLayout(0, 8));
        btnArea.setBackground(AppColors.BG);
        btnArea.add(row1, BorderLayout.NORTH);
        btnArea.add(row2, BorderLayout.CENTER);

        rows.add(placeholder, BorderLayout.WEST);
        rows.add(btnArea, BorderLayout.CENTER);

        south.add(rows, BorderLayout.CENTER);

        // Aksiyonlar
        musaitBtn.addActionListener(e -> musaitAraclariGoster());
        tumBtn.addActionListener(e -> {
            tableTitleLabel.setText("Tüm Araçlar");
            tabloyuYenile();
        });
        kiradaBtn.addActionListener(e -> {
            tableTitleLabel.setText("Kiradaki Araçlar");
            kiradakiAraclariGoster();
        });
        siralaArtanBtn.addActionListener(e -> sirala(true));
        siralaAzalanBtn.addActionListener(e -> sirala(false));
        istatistikBtn.addActionListener(e -> new IstatistikDialog(this,
                aracService.getAracListesi(),
                kiralamaService.getKiralamaListesi()).setVisible(true));

        return south;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SAĞ TIKLAMA AKSİYONLARI
    // ════════════════════════════════════════════════════════════════════════


    // ════════════════════════════════════════════════════════════════════════
    // İKON AKSİYONLARI
    // ════════════════════════════════════════════════════════════════════════

    private void onAracDetay(int row) {
        Object id    = tableModel.getValueAt(row, 0);
        Object marka = tableModel.getValueAt(row, 1);
        Object model = tableModel.getValueAt(row, 2);
        Object fiyat = tableModel.getValueAt(row, 3);
        Object durum = tableModel.getValueAt(row, 4);
        JOptionPane.showMessageDialog(this,
                "<html><b>" + marka + " " + model + "</b><br>"
                        + "ID: " + id + "<br>"
                        + "Günlük Fiyat: " + fiyat + "<br>"
                        + "Durum: " + durum + "</html>",
                "Araç Detayı", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onAracDuzenle(int row) {
        Object id    = tableModel.getValueAt(row, 0);
        Object marka = tableModel.getValueAt(row, 1);
        Object model = tableModel.getValueAt(row, 2);
        Object fiyat = tableModel.getValueAt(row, 3);
        String fiyatStr = fiyat.toString()
                .replace(" ₺", "").replace(".", "").replace(",", ".");
        idField   .setText(id   .toString());
        markaField.setText(marka.toString());
        modelField.setText(model.toString());
        fiyatField.setText(fiyatStr);
        idField.requestFocusInWindow();
        ToastNotification.show(this, "Formu düzenleyip Araç Ekle'ye tıklayın.", ToastNotification.Type.INFO);
    }

    private void onKiralamaDetay(int row) {
        Object id      = tableModel.getValueAt(row, 0);
        Object marka   = tableModel.getValueAt(row, 1);
        Object model   = tableModel.getValueAt(row, 2);
        Object musteri = tableModel.getValueAt(row, 3);
        Object basTar  = tableModel.getValueAt(row, 4);
        Object bitTar  = tableModel.getValueAt(row, 5);
        JOptionPane.showMessageDialog(this,
                "<html><b>" + marka + " " + model + "</b> (ID: " + id + ")<br>"
                        + "Müşteri: <b>" + musteri + "</b><br>"
                        + "Kiralama: " + basTar + "<br>"
                        + "Bitiş: " + bitTar + "</html>",
                "Kiralama Detayı", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onKiralamaIptal(int row) {
        Object id      = tableModel.getValueAt(row, 0);
        Object marka   = tableModel.getValueAt(row, 1);
        Object model   = tableModel.getValueAt(row, 2);
        Object musteri = tableModel.getValueAt(row, 3);
        int secim = JOptionPane.showConfirmDialog(this,
                "<html><b>" + marka + " " + model + "</b> aracının<br>"
                        + "<b>" + musteri + "</b> adlı müşteriye ait kiralamasını iptal etmek istiyor musunuz?</html>",
                "Kiralama İptal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (secim == JOptionPane.YES_OPTION) {
            kiralamaService.kiralamaIptalEt(Integer.parseInt(id.toString()));
            new DosyaService().araclariKaydet(aracService.getAracListesi());
            kiradakiAraclariGoster();
            ToastNotification.show(this, "Kiralama iptal edildi.", ToastNotification.Type.WARNING);
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
            ToastNotification.show(this, "Araç silindi.", ToastNotification.Type.WARNING);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // İŞ MANTIĞI
    // ════════════════════════════════════════════════════════════════════════

    private void aracEkle() {
        try {
            int    id    = Integer.parseInt(idField.getText().trim());
            double fiyat = Double.parseDouble(
                    fiyatField.getText().trim().replace(",", "."));
            aracService.aracEkle(new Arac(id, markaField.getText().trim(),
                    modelField.getText().trim(), fiyat, true));
            tabloyuYenile();
            temizle();
            ToastNotification.show(this, "Araç başarıyla eklendi.", ToastNotification.Type.SUCCESS);
        } catch (NumberFormatException ex) {
            ToastNotification.show(this, "ID ve Fiyat sayısal olmalıdır.", ToastNotification.Type.ERROR);
        } catch (Exception ex) {
            ToastNotification.show(this, "Hata: " + ex.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void aracKirala() {
        try {
            int  id  = Integer.parseInt(kiralamaIdField.getText().trim());
            int  gun = Integer.parseInt(gunField.getText().trim());
            Arac arac = aracService.aracGetir(id);
            if (arac == null || !arac.isMusaitMi()) {
                ToastNotification.show(this,
                        "Araç mevcut değil veya müsait değil.",
                        ToastNotification.Type.WARNING);
                return;
            }
            kiralamaService.aracKirala(new Kiralama(1, arac,
                    new Musteri(1, musteriField.getText().trim(), "555"), gun));
            tabloyuYenile();
            temizle();
            ToastNotification.show(this, "Araç başarıyla kiralandı.", ToastNotification.Type.SUCCESS);
        } catch (NumberFormatException ex) {
            ToastNotification.show(this, "Araç ID ve Gün sayısal olmalıdır.", ToastNotification.Type.ERROR);
        } catch (Exception ex) {
            ToastNotification.show(this, "Hata: " + ex.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // TABLO GÖRÜNÜM METODLARI
    // ════════════════════════════════════════════════════════════════════════

    private void setStandardColumns() {
        tableModel.setColumnCount(0);
        for (String col : new String[]{
                "ID", "Marka", "Model", "Fiyat (Gunluk)", "Musaitlik", "Islemler"})
            tableModel.addColumn(col);
        // geri ekle
        tableModel.setColumnCount(0);
        for (String col : new String[]{
                "ID", "Marka", "Model", "Fiyat (G\u00fcnl\u00fck)", "M\u00fcsaitlik", "\u0130\u015flemler"})
            tableModel.addColumn(col);
        TableStyler.setColumnWidths(aracTable, 65, 145, 145, 145, 120, 120);

        ActionCellEditor editor = new ActionCellEditor();
        editor.setOnView  (row -> onAracDetay(row));
        editor.setOnEdit  (row -> onAracDuzenle(row));
        editor.setOnDelete(row -> onAracSil(row));

        aracTable.getColumnModel().getColumn(5).setCellRenderer(new ActionCellRenderer());
        aracTable.getColumnModel().getColumn(5).setCellEditor(editor);
    }

    private void setKiradaColumns() {
        tableModel.setColumnCount(0);
        for (String col : new String[]{
                "ID", "Marka", "Model", "M\u00fc\u015fteri",
                "Kiraland\u0131\u011f\u0131 Tarih", "M\u00fcsaitlik Tarihi", "\u0130\u015flemler"})
            tableModel.addColumn(col);
        TableStyler.setColumnWidths(aracTable, 55, 120, 120, 120, 140, 140, 100);

        ActionCellEditor editor = new ActionCellEditor();
        editor.setOnView  (row -> onKiralamaDetay(row));
        editor.setOnDelete(row -> onKiralamaIptal(row));

        aracTable.getColumnModel().getColumn(6).setCellRenderer(new ActionCellRenderer());
        aracTable.getColumnModel().getColumn(6).setCellEditor(editor);
    }

    private void tabloyuYenile() {
        aktifGorunum = "tum";
        setStandardColumns();
        filterBar.setVisible(true);
        tableModel.setRowCount(0);
        for (Arac a : aracService.getAracListesi())
            tableModel.addRow(tableRow(a, a.isMusaitMi() ? "Müsait" : "Kirada"));
        updateStats();
        updateEmptyLabel();
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
        updateEmptyLabel();
    }

    private void kiradakiAraclariGoster() {
        aktifGorunum = "kirada";
        setKiradaColumns();
        filterBar.setVisible(false);
        tableModel.setRowCount(0);
        for (Kiralama k : kiralamaService.getKiralamaListesi())
            tableModel.addRow(new Object[]{
                    k.getArac().getId(),
                    k.getArac().getMarka(),
                    k.getArac().getModel(),
                    k.getMusteri().getAdSoyad(),
                    k.getKiralamaTarihiStr(),
                    k.getMusaitOlacakTarihStr(),
                    ""
            });
        updateEmptyLabel();
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
        tableTitleLabel.setText("Fiyata Göre Sıralı ("
                + (artan ? "Artan" : "Azalan") + ")");
        updateEmptyLabel();
    }

    private void filtreUygula() {
        String aranan = aramaField.getText().trim().toLowerCase();
        double minF   = parseDouble(minFiyatField.getText(), 0);
        double maxF   = parseDouble(maxFiyatField.getText(), Double.MAX_VALUE);
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
        tableTitleLabel.setText("Arama Sonuçları ("
                + tableModel.getRowCount() + " araç)");
        updateEmptyLabel();
    }

    private void filtreSifirla() {
        aramaField.setText(""); minFiyatField.setText(""); maxFiyatField.setText("");
        tableTitleLabel.setText("Tüm Araçlar");
        tabloyuYenile();
    }

    private void updateStats() {
        ArrayList<Arac> liste = aracService.getAracListesi();
        toplamCard.setValue(liste.size(), "");
        long musait = liste.stream().filter(Arac::isMusaitMi).count();
        musaitCard.setValue(musait, "");
        double toplam = liste.stream().mapToDouble(Arac::getGunlukFiyat).sum();
        fiyatCard.setValue(toplam, " ₺");
    }

    /** Tablo boşsa "Araç bulunamadı" yazar */
    private void updateEmptyLabel() {
        if (emptyLabel != null)
            emptyLabel.setVisible(tableModel.getRowCount() == 0);
    }

    // ════════════════════════════════════════════════════════════════════════
    // YARDIMCI METODLAR
    // ════════════════════════════════════════════════════════════════════════

    private Object[] tableRow(Arac a, String durum) {
        return new Object[]{a.getId(), a.getMarka(), a.getModel(),
                fmt(a.getGunlukFiyat()), durum, ""};
    }

    private void temizle() {
        for (JTextField f : new JTextField[]{
                idField, markaField, modelField, fiyatField,
                kiralamaIdField, musteriField, gunField})
            f.setText("");
        idField.requestFocusInWindow(); // ← odağı ilk alana döndür
    }

    private String fmt(double v) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("tr", "TR"));
        nf.setMinimumFractionDigits(1); nf.setMaximumFractionDigits(2);
        return nf.format(v) + " ₺";
    }

    private double parseDouble(String s, double varsayilan) {
        try { return s.trim().isEmpty() ? varsayilan
                : Double.parseDouble(s.trim().replace(",", ".")); }
        catch (NumberFormatException e) { return varsayilan; }
    }

    private JLabel filterLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        l.setForeground(AppColors.LABEL_FG);
        return l;
    }
}