package gui;

import gui.theme.AppColors;
import models.Arac;
import models.Kiralama;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class IstatistikDialog extends JDialog {

    // ── Renk paleti ───────────────────────────────────────────────────────────
    private static final Color BG          = new Color(245, 246, 250);
    private static final Color CARD_BG     = Color.WHITE;
    private static final Color CARD_BDR    = new Color(218, 224, 238);
    private static final Color TITLE_FG    = new Color(18, 25, 50);
    private static final Color LABEL_FG    = new Color(80, 90, 115);
    private static final Color ACCENT      = new Color(41, 98, 255);
    private static final Color GREEN       = new Color(22, 163, 74);
    private static final Color RED         = new Color(220, 38, 38);
    private static final Color AMBER       = new Color(202, 138, 4);
    private static final Color MUSAIT_CLR  = new Color(34, 197, 94);
    private static final Color KIRADA_CLR  = new Color(239, 68, 68);
    private static final Color GREEN_BG    = new Color(220, 252, 231);
    private static final Color RED_BG      = new Color(254, 226, 226);
    private static final Color BLUE_BG     = new Color(219, 234, 254);
    private static final Color AMBER_BG    = new Color(254, 243, 199);
    private static final Color PURPLE_BG   = new Color(237, 233, 254);
    private static final Color PURPLE_FG   = new Color(109, 40, 217);

    public IstatistikDialog(JFrame parent, ArrayList<Arac> tumAraclar,
                            ArrayList<Kiralama> kiralamalar) {
        super(parent, "\u0130statistikler", true);
        setSize(600, 780);
        setLocationRelativeTo(parent);
        setResizable(true);
        setMinimumSize(new Dimension(520, 600));

        // ── Hesaplamalar ─────────────────────────────────────────────────────
        int    toplamArac     = tumAraclar.size();
        int    musaitSayisi   = (int) tumAraclar.stream().filter(Arac::isMusaitMi).count();
        int    kiradaSayisi   = toplamArac - musaitSayisi;
        int    toplamKiralama = kiralamalar.size();
        double toplamGelir    = kiralamalar.stream().mapToDouble(Kiralama::getToplamUcret).sum();
        double ortFiyat       = tumAraclar.isEmpty() ? 0
                : tumAraclar.stream().mapToDouble(Arac::getGunlukFiyat).average().orElse(0);
        Arac enPahali = tumAraclar.stream()
                .max(java.util.Comparator.comparingDouble(Arac::getGunlukFiyat)).orElse(null);
        Arac enUcuz   = tumAraclar.stream()
                .min(java.util.Comparator.comparingDouble(Arac::getGunlukFiyat)).orElse(null);
        int musaitPct = toplamArac > 0 ? musaitSayisi * 100 / toplamArac : 0;

        // ── Ana panel ────────────────────────────────────────────────────────
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);

        // ════════════════════════════════════════════════════════════════════
        // 1. BAŞLIK
        // ════════════════════════════════════════════════════════════════════
        JLabel baslik = new JLabel("Genel \u0130statistikler");
        baslik.setFont(new Font("SansSerif", Font.BOLD, 22));
        baslik.setForeground(TITLE_FG);
        baslik.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(baslik);
        panel.add(Box.createVerticalStrut(4));

        JLabel altYazi = new JLabel("Filo durumunuza genel bak\u0131\u015f");
        altYazi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        altYazi.setForeground(LABEL_FG);
        altYazi.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(altYazi);
        panel.add(Box.createVerticalStrut(20));

        // ════════════════════════════════════════════════════════════════════
        // 2. ÖZET KARTLARI — 3 sütun grid
        // ════════════════════════════════════════════════════════════════════
        JPanel cardGrid = new JPanel(new GridLayout(2, 3, 10, 10));
        cardGrid.setOpaque(false);
        cardGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        cardGrid.add(metrikKart("Toplam Ara\u00e7", String.valueOf(toplamArac),
                BLUE_BG, ACCENT, "ti-car"));
        cardGrid.add(metrikKart("M\u00fcsait", String.valueOf(musaitSayisi),
                GREEN_BG, GREEN, "ti-check"));
        cardGrid.add(metrikKart("Kirada", String.valueOf(kiradaSayisi),
                RED_BG, RED, "ti-key"));
        cardGrid.add(metrikKart("Toplam Kiralama", String.valueOf(toplamKiralama),
                PURPLE_BG, PURPLE_FG, "ti-clipboard-list"));
        cardGrid.add(metrikKart("Toplam Gelir", fmt(toplamGelir),
                AMBER_BG, AMBER, "ti-currency-lira"));
        cardGrid.add(metrikKart("Ort. Fiyat", fmt(ortFiyat),
                BLUE_BG, ACCENT, "ti-trending-up"));

        panel.add(cardGrid);
        panel.add(Box.createVerticalStrut(20));

        // ════════════════════════════════════════════════════════════════════
        // 3. EN PAHALI / EN UCUZ SATIR
        // ════════════════════════════════════════════════════════════════════
        if (enPahali != null && enUcuz != null) {
            JPanel extremeRow = new JPanel(new GridLayout(1, 2, 10, 0));
            extremeRow.setOpaque(false);
            extremeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            extremeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

            extremeRow.add(extremeKart("En Pahal\u0131",
                    enPahali.getMarka() + " " + enPahali.getModel(),
                    fmt(enPahali.getGunlukFiyat()), RED, RED_BG));
            extremeRow.add(extremeKart("En Ucuz",
                    enUcuz.getMarka() + " " + enUcuz.getModel(),
                    fmt(enUcuz.getGunlukFiyat()), GREEN, GREEN_BG));

            panel.add(extremeRow);
            panel.add(Box.createVerticalStrut(20));
        }

        // ════════════════════════════════════════════════════════════════════
        // 4. DURUM DAĞILIMI — yatay progress bar (iyileştirilmiş)
        // ════════════════════════════════════════════════════════════════════
        panel.add(bolumBaslik("Durum Da\u011f\u0131l\u0131m\u0131"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(progressBar(musaitSayisi, kiradaSayisi, toplamArac));
        panel.add(Box.createVerticalStrut(20));

        // ════════════════════════════════════════════════════════════════════
        // 5. PASTA + LEGEND yan yana (iyileştirilmiş)
        // ════════════════════════════════════════════════════════════════════
        panel.add(bolumBaslik("M\u00fcsait / Kirada Oran\u0131"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(donutPanel(musaitSayisi, kiradaSayisi, musaitPct));
        panel.add(Box.createVerticalStrut(20));

        // ════════════════════════════════════════════════════════════════════
        // 6. SÜTUN GRAFİK (iyileştirilmiş)
        // ════════════════════════════════════════════════════════════════════
        panel.add(bolumBaslik("Ara\u00e7 G\u00fcnl\u00fck Fiyat Da\u011f\u0131l\u0131m\u0131"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(sutunGrafik(tumAraclar));
        panel.add(Box.createVerticalStrut(20));

        // ════════════════════════════════════════════════════════════════════
        // 7. KAPAT BUTONU
        // ════════════════════════════════════════════════════════════════════
        UIFactory.RoundButton kapatBtn = new UIFactory.RoundButton(
                "  Kapat", ACCENT, Color.WHITE, null, 16);
        kapatBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        kapatBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        kapatBtn.addActionListener(e -> dispose());
        panel.add(kapatBtn);
    }

    // ════════════════════════════════════════════════════════════════════════
    // METRİK KART (özet kartı)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel metrikKart(String baslik, String deger,
                              Color bgColor, Color accentColor, String iconName) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 14, 14));
                g2.setColor(CARD_BDR);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-2, getHeight()-2, 14, 14));
                // Sol kenar accent şeridi
                g2.setColor(accentColor);
                g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 14, 14));
                g2.fillRect(0, 0, 4, getHeight());
                g2.setClip(null);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(0, 0));
        card.setBorder(new EmptyBorder(12, 14, 12, 12));

        // İkon dairesi
        JPanel iconCircle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(36, 36));
        iconCircle.setMinimumSize(new Dimension(36, 36));
        iconCircle.setMaximumSize(new Dimension(36, 36));
        iconCircle.setLayout(new BorderLayout());
        JLabel iconLbl = new JLabel("\u2022", SwingConstants.CENTER);
        iconLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        iconLbl.setForeground(accentColor);
        iconCircle.add(iconLbl);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(new EmptyBorder(0, 10, 0, 0));

        JLabel degerLbl = new JLabel(deger);
        degerLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        degerLbl.setForeground(accentColor);
        degerLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel baslikLbl = new JLabel(baslik);
        baslikLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        baslikLbl.setForeground(LABEL_FG);
        baslikLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(degerLbl);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(baslikLbl);

        card.add(iconCircle, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    // EXTREME KART (en pahalı / en ucuz)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel extremeKart(String etiket, String aracAdi,
                               String fiyat, Color accent, Color bg) {
        JPanel card = roundCard();
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel sol = new JPanel();
        sol.setOpaque(false);
        sol.setLayout(new BoxLayout(sol, BoxLayout.Y_AXIS));

        JLabel etiketLbl = new JLabel(etiket);
        etiketLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        etiketLbl.setForeground(LABEL_FG);
        etiketLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel aracLbl = new JLabel(aracAdi);
        aracLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        aracLbl.setForeground(TITLE_FG);
        aracLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        sol.add(etiketLbl);
        sol.add(Box.createVerticalStrut(2));
        sol.add(aracLbl);

        JLabel fiyatLbl = new JLabel(fiyat);
        fiyatLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        fiyatLbl.setForeground(accent);

        // Sol kenar badge
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setOpaque(false);
        badge.setLayout(new BorderLayout());
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));
        badge.setPreferredSize(new Dimension(90, 28));
        JLabel badgeLbl = new JLabel(fiyat, SwingConstants.CENTER);
        badgeLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        badgeLbl.setForeground(accent);
        badge.add(badgeLbl);

        card.add(sol, BorderLayout.CENTER);
        card.add(badge, BorderLayout.EAST);
        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    // PROGRESS BAR
    // ════════════════════════════════════════════════════════════════════════
    private JPanel progressBar(int musait, int kirada, int toplam) {
        JPanel wrapper = roundCard();
        wrapper.setLayout(new BorderLayout(0, 8));
        wrapper.setBorder(new EmptyBorder(14, 16, 14, 16));

        // Legend satırı
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        legend.setOpaque(false);
        legend.add(legendDot(MUSAIT_CLR, "M\u00fcsait: " + musait));
        legend.add(legendDot(KIRADA_CLR, "Kirada: " + kirada));

        // Bar
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), r = h / 2;

                // Arka plan
                g2.setColor(new Color(230, 232, 242));
                g2.fillRoundRect(0, 0, w, h, r, r);

                if (toplam > 0) {
                    // Müsait dilimi
                    int mw = (int)((double) musait / toplam * w);
                    g2.setColor(MUSAIT_CLR);
                    g2.fillRoundRect(0, 0, Math.max(mw, r * 2), h, r, r);

                    // Kirada dilimi (sağ taraf)
                    if (kirada > 0) {
                        int kw = (int)((double) kirada / toplam * w);
                        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, r, r));
                        g2.setColor(KIRADA_CLR);
                        g2.fillRect(w - kw, 0, kw, h);
                        g2.setClip(null);
                    }

                    // Yüzde yazıları
                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    if (musait > 0 && mw > 50) {
                        g2.setColor(Color.WHITE);
                        String t = "%" + (musait * 100 / toplam);
                        g2.drawString(t, 12, h / 2 + 4);
                    }
                    if (kirada > 0) {
                        int kw = (int)((double) kirada / toplam * w);
                        g2.setColor(Color.WHITE);
                        String t = "%" + (kirada * 100 / toplam);
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(t, w - kw + 6, h / 2 + 4);
                    }
                }
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(0, 32));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        bar.setOpaque(false);

        wrapper.add(legend, BorderLayout.NORTH);
        wrapper.add(bar, BorderLayout.CENTER);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        return wrapper;
    }

    // ════════════════════════════════════════════════════════════════════════
    // DONUT GRAFİK (pasta — iyileştirilmiş)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel donutPanel(int musait, int kirada, int musaitPct) {
        int toplam = musait + kirada;

        JPanel wrapper = roundCard();
        wrapper.setLayout(new BorderLayout(20, 0));
        wrapper.setBorder(new EmptyBorder(16, 20, 16, 20));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Donut
        JPanel donut = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (toplam == 0) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = Math.min(getWidth(), getHeight()) - 20;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                int stroke = size / 5;

                // Gri arka plan halkası
                g2.setColor(new Color(230, 232, 242));
                g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                g2.drawOval(x + stroke/2, y + stroke/2,
                        size - stroke, size - stroke);

                // Müsait yayı
                double musaitAci = 360.0 * musait / toplam;
                g2.setColor(MUSAIT_CLR);
                g2.setStroke(new BasicStroke(stroke - 3,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(x + stroke/2, y + stroke/2,
                        size - stroke, size - stroke, 90, -(int) musaitAci);

                // Kirada yayı
                if (kirada > 0) {
                    g2.setColor(KIRADA_CLR);
                    g2.setStroke(new BasicStroke(stroke - 3,
                            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawArc(x + stroke/2, y + stroke/2,
                            size - stroke, size - stroke,
                            (int)(90 - musaitAci), -(360 - (int) musaitAci));
                }

                // Ortada yüzde
                g2.setColor(TITLE_FG);
                g2.setFont(new Font("SansSerif", Font.BOLD, size / 5));
                String pct = "%" + musaitPct;
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(pct,
                        getWidth() / 2 - fm.stringWidth(pct) / 2,
                        getHeight() / 2 + fm.getAscent() / 2 - 2);
                g2.setFont(new Font("SansSerif", Font.PLAIN, size / 8));
                g2.setColor(LABEL_FG);
                String sub = "m\u00fcsait";
                FontMetrics fm2 = g2.getFontMetrics();
                g2.drawString(sub,
                        getWidth() / 2 - fm2.stringWidth(sub) / 2,
                        getHeight() / 2 + fm.getAscent() / 2 + fm2.getHeight() - 2);

                g2.dispose();
            }
        };
        donut.setOpaque(false);
        donut.setPreferredSize(new Dimension(160, 160));

        // Legend sağda
        JPanel legend = new JPanel();
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));
        legend.setOpaque(false);
        legend.add(Box.createVerticalGlue());
        legend.add(legendKart(MUSAIT_CLR, GREEN_BG, "M\u00fcsait",
                musait + " ara\u00e7",
                "%" + musaitPct));
        legend.add(Box.createVerticalStrut(10));
        legend.add(legendKart(KIRADA_CLR, RED_BG, "Kirada",
                kirada + " ara\u00e7",
                toplam > 0 ? "%" + (kirada * 100 / toplam) : "%0"));
        legend.add(Box.createVerticalGlue());

        wrapper.add(donut, BorderLayout.WEST);
        wrapper.add(legend, BorderLayout.CENTER);
        return wrapper;
    }

    // ════════════════════════════════════════════════════════════════════════
    // SÜTUN GRAFİK (tamamen yeniden çizildi)
    // ════════════════════════════════════════════════════════════════════════
    private JPanel sutunGrafik(ArrayList<Arac> araclar) {
        JPanel card = roundCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Legend satırı
        JPanel legendRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        legendRow.setOpaque(false);
        legendRow.add(legendDot(new Color(74, 222, 128), "M\u00fcsait"));
        legendRow.add(legendDot(new Color(248, 113, 113), "Kirada"));
        card.add(legendRow, BorderLayout.NORTH);

        JPanel grafik = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (araclar.isEmpty()) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int W = getWidth(), H = getHeight();
                int padL = 48, padB = 44, padT = 16, padR = 12;
                int grafikW = W - padL - padR;
                int grafikH = H - padB - padT;

                double maxFiyat = araclar.stream()
                        .mapToDouble(Arac::getGunlukFiyat).max().orElse(1);

                // Y ekseni ızgara + değerler
                int izgara = 4;
                for (int i = 0; i <= izgara; i++) {
                    int yPos = padT + grafikH - (grafikH * i / izgara);
                    // Izgara çizgisi
                    g2.setColor(new Color(235, 237, 245));
                    g2.setStroke(new BasicStroke(0.8f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1, new float[]{4, 3}, 0));
                    g2.drawLine(padL, yPos, padL + grafikW, yPos);

                    // Y etiketi
                    double deger = maxFiyat * i / izgara;
                    String etiket = deger >= 1000
                            ? String.format("%.0fK", deger / 1000)
                            : String.format("%.0f", deger);
                    g2.setStroke(new BasicStroke(1f));
                    g2.setColor(new Color(150, 160, 185));
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(etiket, padL - fm.stringWidth(etiket) - 5,
                            yPos + fm.getAscent() / 2 - 1);
                }

                // X ekseni
                g2.setColor(new Color(210, 215, 230));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(padL, padT + grafikH, padL + grafikW, padT + grafikH);

                // Sütunlar
                int n = araclar.size();
                int gap = Math.max(4, 40 / n);
                int barW = Math.max(12, (grafikW - gap * (n + 1)) / n);
                int cornerR = Math.min(6, barW / 3);

                for (int i = 0; i < n; i++) {
                    Arac a = araclar.get(i);
                    int barH = Math.max(4,
                            (int)(grafikH * a.getGunlukFiyat() / maxFiyat));
                    int xPos = padL + gap + i * (barW + gap);
                    int yPos = padT + grafikH - barH;

                    // Sütun rengi
                    Color barRenk = a.isMusaitMi()
                            ? new Color(74, 222, 128)
                            : new Color(248, 113, 113);
                    Color barDark = a.isMusaitMi()
                            ? new Color(34, 197, 94)
                            : new Color(239, 68, 68);

                    // Açık sütun (arka plan efekti)
                    g2.setColor(new Color(barRenk.getRed(), barRenk.getGreen(),
                            barRenk.getBlue(), 40));
                    g2.fillRoundRect(xPos, padT, barW, grafikH, cornerR, cornerR);

                    // Asıl sütun
                    g2.setColor(barRenk);
                    g2.fillRoundRect(xPos, yPos, barW, barH, cornerR, cornerR);

                    // Üst kenarlık çizgisi
                    g2.setColor(barDark);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawLine(xPos + cornerR, yPos, xPos + barW - cornerR, yPos);

                    // Değer etiketi
                    if (barW >= 20) {
                        g2.setFont(new Font("SansSerif", Font.BOLD, 9));
                        g2.setColor(TITLE_FG);
                        String valStr = a.getGunlukFiyat() >= 1000
                                ? String.format("%.0fK", a.getGunlukFiyat() / 1000)
                                : String.format("%.0f", a.getGunlukFiyat());
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(valStr,
                                xPos + (barW - fm.stringWidth(valStr)) / 2,
                                yPos - 4);
                    }

                    // Marka etiketi (eğik)
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    g2.setColor(LABEL_FG);
                    String isim = a.getMarka().length() > 5
                            ? a.getMarka().substring(0, 5) : a.getMarka();
                    AffineTransform old = g2.getTransform();
                    g2.translate(xPos + barW / 2, padT + grafikH + 6);
                    g2.rotate(Math.toRadians(-40));
                    g2.drawString(isim, 0, 0);
                    g2.setTransform(old);
                }
                g2.dispose();
            }
        };
        grafik.setOpaque(false);
        int yukseklik = Math.max(200, 160 + araclar.size() * 3);
        grafik.setPreferredSize(new Dimension(0, yukseklik));
        grafik.setMaximumSize(new Dimension(Integer.MAX_VALUE, yukseklik));
        card.add(grafik, BorderLayout.CENTER);

        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, yukseklik + 60));
        return card;
    }

    // ════════════════════════════════════════════════════════════════════════
    // YARDIMCI BİLEŞENLER
    // ════════════════════════════════════════════════════════════════════════

    private JLabel bolumBaslik(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(TITLE_FG);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel roundCard() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(
                        0, 0, getWidth()-1, getHeight()-1, 14, 14));
                g2.setColor(CARD_BDR);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(
                        0.5f, 0.5f, getWidth()-2, getHeight()-2, 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JPanel legendKart(Color accent, Color bg,
                              String baslik, String alt, String pct) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setLayout(new BorderLayout(8, 0));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        // Sol: renk çubuğu
        JPanel stripe = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        stripe.setOpaque(false);
        stripe.setPreferredSize(new Dimension(4, 0));

        // Metin
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel b = new JLabel(baslik);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setForeground(accent);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel a = new JLabel(alt);
        a.setFont(new Font("SansSerif", Font.PLAIN, 11));
        a.setForeground(LABEL_FG);
        a.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(b);
        text.add(a);

        // Yüzde badge
        JLabel pctLbl = new JLabel(pct, SwingConstants.RIGHT);
        pctLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        pctLbl.setForeground(accent);

        p.add(stripe, BorderLayout.WEST);
        p.add(text, BorderLayout.CENTER);
        p.add(pctLbl, BorderLayout.EAST);
        return p;
    }

    private JPanel legendDot(Color renk, String etiket) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(renk);
                g2.fillOval(0, (getHeight()-10)/2, 10, 10);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(10, 16));
        JLabel lbl = new JLabel(etiket);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(LABEL_FG);
        p.add(dot);
        p.add(lbl);
        return p;
    }

    private String fmt(double v) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("tr", "TR"));
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(2);
        return nf.format(v) + " \u20BA";
    }
}