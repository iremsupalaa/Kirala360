package gui;

import models.Arac;
import models.Kiralama;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * İstatistik ve grafik ekranı — modal dialog, kaydırılabilir içerik.
 */
public class IstatistikDialog extends JDialog {

    public IstatistikDialog(JFrame parent, ArrayList<Arac> tumAraclar,
                            ArrayList<Kiralama> kiralamalar) {
        super(parent, "İstatistikler", true);
        setSize(520, 720);
        setLocationRelativeTo(parent);
        setResizable(true);

        // ── Hesaplamalar ─────────────────────────────────────────────────────
        int    toplamArac     = tumAraclar.size();
        int    musaitSayisi   = (int) tumAraclar.stream().filter(Arac::isMusaitMi).count();
        int    kiradaSayisi   = toplamArac - musaitSayisi;
        int    toplamKiralama = kiralamalar.size();
        double toplamGelir    = kiralamalar.stream().mapToDouble(Kiralama::getToplamUcret).sum();
        double ortalamaFiyat  = tumAraclar.isEmpty() ? 0
                : tumAraclar.stream().mapToDouble(Arac::getGunlukFiyat).average().orElse(0);
        Arac enPahali = tumAraclar.stream()
                .max(java.util.Comparator.comparingDouble(Arac::getGunlukFiyat)).orElse(null);
        Arac enUcuz = tumAraclar.stream()
                .min(java.util.Comparator.comparingDouble(Arac::getGunlukFiyat)).orElse(null);

        // ── İçerik paneli (BoxLayout, kaydırılabilir) ─────────────────────────
        JPanel panel = new JPanel();
        panel.setBackground(AppColors.BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll);

        // ── Başlık ────────────────────────────────────────────────────────────
        JLabel baslik = new JLabel("Genel İstatistikler");
        baslik.setFont(new Font("SansSerif", Font.BOLD, 22));
        baslik.setForeground(AppColors.TITLE_FG);
        baslik.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(baslik);
        panel.add(Box.createVerticalStrut(16));

        // ── Stat satırları ────────────────────────────────────────────────────
        panel.add(satir("Toplam Araç Sayısı",   String.valueOf(toplamArac),     AppColors.ACCENT));
        panel.add(Box.createVerticalStrut(7));
        panel.add(satir("Müsait Araçlar",        String.valueOf(musaitSayisi),   AppColors.MUSAIT_FG));
        panel.add(Box.createVerticalStrut(7));
        panel.add(satir("Kiradaki Araçlar",      String.valueOf(kiradaSayisi),   AppColors.KIRADA_FG));
        panel.add(Box.createVerticalStrut(7));
        panel.add(satir("Toplam Kiralama",       String.valueOf(toplamKiralama), AppColors.BOT_PRP_FG));
        panel.add(Box.createVerticalStrut(7));
        panel.add(satir("Toplam Gelir",          fmt(toplamGelir),               AppColors.STAT_FG));
        panel.add(Box.createVerticalStrut(7));
        panel.add(satir("Ortalama Günlük Fiyat", fmt(ortalamaFiyat),             AppColors.ACCENT));
        panel.add(Box.createVerticalStrut(7));
        panel.add(satir("En Pahalı Araç",
                enPahali != null ? enPahali.getMarka() + " " + enPahali.getModel()
                        + " — " + fmt(enPahali.getGunlukFiyat()) : "-", AppColors.KIRADA_FG));
        panel.add(Box.createVerticalStrut(7));
        panel.add(satir("En Ucuz Araç",
                enUcuz != null ? enUcuz.getMarka() + " " + enUcuz.getModel()
                        + " — " + fmt(enUcuz.getGunlukFiyat()) : "-", AppColors.MUSAIT_FG));

        panel.add(Box.createVerticalStrut(24));

        // ── Küçük yatay bar (mevcut) ──────────────────────────────────────────
        panel.add(bolumBaslik("Araç Durum Dağılımı"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(yatayBar(musaitSayisi, kiradaSayisi, toplamArac));

        panel.add(Box.createVerticalStrut(24));

        // ── PASTA GRAFİK ──────────────────────────────────────────────────────
        panel.add(bolumBaslik("Müsait / Kirada Oranı"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(pastaGrafik(musaitSayisi, kiradaSayisi));

        panel.add(Box.createVerticalStrut(24));

        // ── SÜTUN GRAFİK (araçların günlük fiyatları) ─────────────────────────
        panel.add(bolumBaslik("Araç Günlük Fiyat Dağılımı"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(sutunGrafik(tumAraclar));

        panel.add(Box.createVerticalStrut(20));

        // ── Kapat butonu ──────────────────────────────────────────────────────
        UIFactory.RoundButton kapatBtn = new UIFactory.RoundButton(
                "  Kapat", AppColors.ACCENT, Color.WHITE, null, 16);
        kapatBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        kapatBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        kapatBtn.addActionListener(e -> dispose());
        panel.add(kapatBtn);
    }

    // ════════════════════════════════════════════════════════════════════════
    // BÖLÜM BAŞLIĞI
    // ════════════════════════════════════════════════════════════════════════

    private JLabel bolumBaslik(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(AppColors.TITLE_FG);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ════════════════════════════════════════════════════════════════════════
    // İSTATİSTİK SATIRI
    // ════════════════════════════════════════════════════════════════════════

    private JPanel satir(String etiket, String deger, Color renk) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 238), 1),
                new EmptyBorder(8, 14, 8, 14)));
        JLabel lbl = new JLabel(etiket);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(AppColors.LABEL_FG);
        JLabel val = new JLabel(deger);
        val.setFont(new Font("SansSerif", Font.BOLD, 13));
        val.setForeground(renk);
        val.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(lbl, BorderLayout.WEST);
        p.add(val, BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════
    // YATAY BAR (küçük özet)
    // ════════════════════════════════════════════════════════════════════════

    private JPanel yatayBar(int musait, int kirada, int toplam) {
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth() - 4, barH = 28, y = (getHeight() - barH) / 2;
                g2.setColor(new Color(230, 232, 240));
                g2.fillRoundRect(2, y, w, barH, 14, 14);
                if (toplam > 0) {
                    int mw = (int)((double) musait / toplam * w);
                    g2.setColor(AppColors.MUSAIT_BG);
                    g2.fillRoundRect(2, y, Math.max(mw, 14), barH, 14, 14);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                    if (musait > 0) {
                        g2.setColor(AppColors.MUSAIT_FG);
                        g2.drawString("Müsait: " + musait, 14, y + 19);
                    }
                    if (kirada > 0) {
                        String t = "Kirada: " + kirada;
                        g2.setColor(AppColors.KIRADA_FG);
                        g2.drawString(t, w - g2.getFontMetrics().stringWidth(t) - 8, y + 19);
                    }
                }
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(0, 44));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        bar.setOpaque(false);
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        return bar;
    }

    // ════════════════════════════════════════════════════════════════════════
    // PASTA GRAFİK
    // ════════════════════════════════════════════════════════════════════════

    private JPanel pastaGrafik(int musait, int kirada) {
        int toplam = musait + kirada;

        JPanel wrapper = new JPanel(new BorderLayout(20, 0));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Pasta
        JPanel pasta = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (toplam == 0) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int pad = 16;
                int size = Math.min(getWidth(), getHeight()) - pad * 2;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                // Gölge
                g2.setColor(new Color(0, 0, 0, 18));
                g2.fillOval(x + 3, y + 4, size, size);

                // Müsait dilimi
                double musaitAci = 360.0 * musait / toplam;
                g2.setColor(new Color(100, 210, 150));
                g2.fillArc(x, y, size, size, 90, -(int) musaitAci);

                // Kirada dilimi
                g2.setColor(new Color(240, 100, 100));
                g2.fillArc(x, y, size, size, (int)(90 - musaitAci), -(360 - (int) musaitAci));

                // Ortada beyaz daire (donut efekti)
                int innerPad = size / 4;
                g2.setColor(AppColors.BG);
                g2.fillOval(x + innerPad, y + innerPad, size - innerPad * 2, size - innerPad * 2);

                // Ortada yüzde yazısı
                g2.setColor(AppColors.TITLE_FG);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                String pct = toplam > 0 ? String.format("%%%d", musait * 100 / toplam) : "";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(pct, getWidth() / 2 - fm.stringWidth(pct) / 2,
                        getHeight() / 2 + fm.getAscent() / 2 - 2);

                // Dış kenarlık
                g2.setColor(new Color(220, 225, 235));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(x, y, size, size);

                g2.dispose();
            }
        };
        pasta.setPreferredSize(new Dimension(180, 180));
        pasta.setOpaque(false);

        // Legand
        JPanel legand = new JPanel();
        legand.setLayout(new BoxLayout(legand, BoxLayout.Y_AXIS));
        legand.setOpaque(false);
        legand.setAlignmentY(Component.CENTER_ALIGNMENT);

        legand.add(Box.createVerticalGlue());
        legand.add(legandSatir(new Color(100, 210, 150), "Müsait",
                toplam > 0 ? String.format("%d  (%%%d)", musait, musait * 100 / toplam) : "0"));
        legand.add(Box.createVerticalStrut(14));
        legand.add(legandSatir(new Color(240, 100, 100), "Kirada",
                toplam > 0 ? String.format("%d  (%%%d)", kirada, kirada * 100 / toplam) : "0"));
        legand.add(Box.createVerticalGlue());

        wrapper.add(pasta, BorderLayout.WEST);
        wrapper.add(legand, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel legandSatir(Color renk, String etiket, String deger) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Renkli kare
        JPanel kare = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(renk);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
            }
        };
        kare.setPreferredSize(new Dimension(14, 14));
        kare.setOpaque(false);

        JLabel lbl = new JLabel(etiket + ": " + deger);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(AppColors.LABEL_FG);

        p.add(kare);
        p.add(lbl);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SÜTUN GRAFİK (her aracın günlük fiyatı)
    // ════════════════════════════════════════════════════════════════════════

    private JPanel sutunGrafik(ArrayList<Arac> araclar) {
        JPanel grafik = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (araclar.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int W      = getWidth();
                int H      = getHeight();
                int padL   = 52;  // sol: Y ekseni için
                int padB   = 50;  // alt: etiketler için
                int padT   = 16;
                int padR   = 12;

                int grafikW = W - padL - padR;
                int grafikH = H - padB - padT;

                double maxFiyat = araclar.stream()
                        .mapToDouble(Arac::getGunlukFiyat).max().orElse(1);

                // Izgara çizgileri + Y ekseni değerleri
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                int izgara = 5;
                for (int i = 0; i <= izgara; i++) {
                    int yPos = padT + grafikH - (grafikH * i / izgara);
                    g2.setColor(new Color(220, 225, 235));
                    g2.drawLine(padL, yPos, padL + grafikW, yPos);
                    double deger = maxFiyat * i / izgara;
                    String etiket = deger >= 1000
                            ? String.format("%.0fK", deger / 1000)
                            : String.format("%.0f", deger);
                    g2.setColor(new Color(140, 150, 170));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(etiket, padL - fm.stringWidth(etiket) - 4,
                            yPos + fm.getAscent() / 2 - 1);
                }

                // X ekseni
                g2.setColor(new Color(200, 205, 218));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawLine(padL, padT + grafikH, padL + grafikW, padT + grafikH);

                // Sütunlar
                int n       = araclar.size();
                int gap     = 6;
                int barW    = Math.max(8, (grafikW - gap * (n + 1)) / n);

                for (int i = 0; i < n; i++) {
                    Arac a = araclar.get(i);
                    int barH  = (int)(grafikH * a.getGunlukFiyat() / maxFiyat);
                    int xPos  = padL + gap + i * (barW + gap);
                    int yPos  = padT + grafikH - barH;

                    // Gölge
                    g2.setColor(new Color(0, 0, 0, 18));
                    g2.fillRoundRect(xPos + 2, yPos + 3, barW, barH, 6, 6);

                    // Sütun rengi: müsait → yeşil, kirada → kırmızı
                    Color barRenk = a.isMusaitMi()
                            ? new Color(80, 190, 130)
                            : new Color(230, 90, 90);
                    g2.setColor(barRenk);
                    g2.fillRoundRect(xPos, yPos, barW, barH, 6, 6);

                    // Üst değer etiketi (dar değilse)
                    if (barW >= 28) {
                        g2.setFont(new Font("SansSerif", Font.BOLD, 9));
                        g2.setColor(AppColors.TITLE_FG);
                        String valStr = a.getGunlukFiyat() >= 1000
                                ? String.format("%.0fK", a.getGunlukFiyat() / 1000)
                                : String.format("%.0f", a.getGunlukFiyat());
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(valStr, xPos + (barW - fm.stringWidth(valStr)) / 2, yPos - 3);
                    }

                    // Alt marka etiketi (döndürülmüş)
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    g2.setColor(new Color(100, 110, 130));
                    String isim = a.getMarka().length() > 6
                            ? a.getMarka().substring(0, 6) : a.getMarka();
                    int xCenter = xPos + barW / 2;
                    java.awt.geom.AffineTransform old = g2.getTransform();
                    g2.translate(xCenter, padT + grafikH + 4);
                    g2.rotate(Math.toRadians(-35));
                    g2.drawString(isim, 0, 0);
                    g2.setTransform(old);
                }

                // Legand
                int legX = padL + grafikW - 130;
                int legY = padT + 6;
                g2.setColor(new Color(245, 247, 252));
                g2.fillRoundRect(legX, legY, 128, 44, 8, 8);
                g2.setColor(new Color(210, 215, 228));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(legX, legY, 128, 44, 8, 8);

                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.setColor(new Color(80, 190, 130));
                g2.fillRoundRect(legX + 8, legY + 8, 12, 12, 4, 4);
                g2.setColor(AppColors.LABEL_FG);
                g2.drawString("Müsait", legX + 24, legY + 19);

                g2.setColor(new Color(230, 90, 90));
                g2.fillRoundRect(legX + 8, legY + 26, 12, 12, 4, 4);
                g2.setColor(AppColors.LABEL_FG);
                g2.drawString("Kirada", legX + 24, legY + 37);

                g2.dispose();
            }
        };
        grafik.setBackground(Color.WHITE);
        grafik.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 238), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        int yukseklik = Math.max(220, 160 + araclar.size() * 4);
        grafik.setPreferredSize(new Dimension(0, yukseklik));
        grafik.setMaximumSize(new Dimension(Integer.MAX_VALUE, yukseklik));
        grafik.setAlignmentX(Component.LEFT_ALIGNMENT);
        return grafik;
    }

    // ════════════════════════════════════════════════════════════════════════
    // YARDIMCI
    // ════════════════════════════════════════════════════════════════════════

    private String fmt(double v) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("tr", "TR"));
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(2);
        return nf.format(v) + " ₺";
    }
}