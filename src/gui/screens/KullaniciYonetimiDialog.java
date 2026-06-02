package gui.screens;

import gui.UIFactory;
import gui.components.ToastNotification;
import gui.theme.AppColors;
import services.DosyaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

/**
 * Kullanıcı yönetim ekranı.
 * Mevcut kullanıcıları listeler, yeni kullanıcı eklemeye ve silmeye izin verir.
 */
public class KullaniciYonetimiDialog extends JDialog {

    private static final Color BG       = new Color(245, 246, 250);
    private static final Color CARD_BG  = Color.WHITE;
    private static final Color CARD_BDR = new Color(218, 224, 238);
    private static final Color TITLE_FG = new Color(18, 25, 50);
    private static final Color LABEL_FG = new Color(70, 80, 110);
    private static final Color ACCENT   = new Color(41, 98, 255);
    private static final Color GREEN    = new Color(22, 163, 74);
    private static final Color RED      = new Color(220, 38, 38);

    private final DosyaService dosyaService = new DosyaService();
    private Map<String, String> kullanicilar;

    private JTextField yeniKullaniciField;
    private JPasswordField yeniSifreField;
    private JPasswordField sifreTekrarField;
    private JPanel listePanel;

    public KullaniciYonetimiDialog(JFrame parent) {
        super(parent, "Kullanıcı Yönetimi", true);
        setSize(480, 620);
        setLocationRelativeTo(parent);
        setResizable(false);

        kullanicilar = dosyaService.kullanicilariYukle();

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);
        add(root);

        // ── Başlık ───────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BDR),
                new EmptyBorder(16, 22, 16, 22)));

        JLabel baslik = new JLabel("\uD83D\uDC64  Kullan\u0131c\u0131 Y\u00f6netimi");
        baslik.setFont(new Font("SansSerif", Font.BOLD, 18));
        baslik.setForeground(TITLE_FG);
        header.add(baslik, BorderLayout.WEST);

        JLabel altBaslik = new JLabel("Kullan\u0131c\u0131 ekle veya sil");
        altBaslik.setFont(new Font("SansSerif", Font.PLAIN, 12));
        altBaslik.setForeground(new Color(130, 140, 165));
        altBaslik.setBorder(new EmptyBorder(2, 0, 0, 0));

        JPanel baslikPanel = new JPanel();
        baslikPanel.setLayout(new BoxLayout(baslikPanel, BoxLayout.Y_AXIS));
        baslikPanel.setBackground(CARD_BG);
        baslikPanel.add(baslik);
        baslikPanel.add(altBaslik);
        header.add(baslikPanel, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // ── İçerik ───────────────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Yeni kullanıcı formu
        content.add(bolumBaslik("Yeni Kullan\u0131c\u0131 Ekle"));
        content.add(Box.createVerticalStrut(10));
        content.add(buildEkleForm());
        content.add(Box.createVerticalStrut(24));

        // Mevcut kullanıcılar listesi
        content.add(bolumBaslik("Mevcut Kullan\u0131c\u0131lar"));
        content.add(Box.createVerticalStrut(10));

        listePanel = new JPanel();
        listePanel.setLayout(new BoxLayout(listePanel, BoxLayout.Y_AXIS));
        listePanel.setBackground(BG);
        listePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(listePanel);

        listeGuncelle();

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        root.add(scroll, BorderLayout.CENTER);

        // ── Alt buton ─────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setBackground(CARD_BG);
        footer.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, CARD_BDR));
        UIFactory.RoundButton kapatBtn = new UIFactory.RoundButton(
                "  Kapat", new Color(230, 232, 242),
                AppColors.LABEL_FG, null, 10);
        kapatBtn.setPreferredSize(new Dimension(100, 36));
        kapatBtn.addActionListener(e -> dispose());
        footer.add(kapatBtn);
        root.add(footer, BorderLayout.SOUTH);
    }

    // ── Yeni kullanıcı formu ─────────────────────────────────────────────────
    private JPanel buildEkleForm() {
        JPanel card = roundCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Kullanıcı adı
        card.add(formLabel("Kullan\u0131c\u0131 Ad\u0131"));
        card.add(Box.createVerticalStrut(4));
        yeniKullaniciField = styledField("örn. iremsu");
        card.add(yeniKullaniciField);
        card.add(Box.createVerticalStrut(12));

        // Şifre
        card.add(formLabel("\u015eifre"));
        card.add(Box.createVerticalStrut(4));
        yeniSifreField = new JPasswordField();
        styleSifre(yeniSifreField);
        card.add(yeniSifreField);
        card.add(Box.createVerticalStrut(12));

        // Şifre tekrar
        card.add(formLabel("\u015eifre Tekrar"));
        card.add(Box.createVerticalStrut(4));
        sifreTekrarField = new JPasswordField();
        styleSifre(sifreTekrarField);
        card.add(sifreTekrarField);
        card.add(Box.createVerticalStrut(16));

        // Ekle butonu
        UIFactory.RoundButton ekleBtn = new UIFactory.RoundButton(
                "  Kullan\u0131c\u0131 Ekle", GREEN, Color.WHITE, null, 10);
        ekleBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ekleBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        ekleBtn.addActionListener(e -> kullaniciEkle());
        card.add(ekleBtn);

        return card;
    }

    // ── Kullanıcı ekle ────────────────────────────────────────────────────────
    private void kullaniciEkle() {
        String kullanici = yeniKullaniciField.getText().trim();
        String sifre     = new String(yeniSifreField.getPassword()).trim();
        String tekrar    = new String(sifreTekrarField.getPassword()).trim();

        if (kullanici.isEmpty()) {
            ToastNotification.show((JFrame) getOwner(),
                    "Kullan\u0131c\u0131 ad\u0131 bo\u015f b\u0131rak\u0131lamaz.",
                    ToastNotification.Type.ERROR); return;
        }
        if (kullanici.length() < 3) {
            ToastNotification.show((JFrame) getOwner(),
                    "Kullan\u0131c\u0131 ad\u0131 en az 3 karakter olmal\u0131.",
                    ToastNotification.Type.WARNING); return;
        }
        if (sifre.isEmpty()) {
            ToastNotification.show((JFrame) getOwner(),
                    "\u015eifre bo\u015f b\u0131rak\u0131lamaz.",
                    ToastNotification.Type.ERROR); return;
        }
        if (sifre.length() < 4) {
            ToastNotification.show((JFrame) getOwner(),
                    "\u015eifre en az 4 karakter olmal\u0131.",
                    ToastNotification.Type.WARNING); return;
        }
        if (!sifre.equals(tekrar)) {
            ToastNotification.show((JFrame) getOwner(),
                    "\u015eifreler e\u015fle\u015fmiyor!",
                    ToastNotification.Type.ERROR); return;
        }
        if (kullanicilar.containsKey(kullanici)) {
            ToastNotification.show((JFrame) getOwner(),
                    "\"" + kullanici + "\" zaten mevcut.",
                    ToastNotification.Type.WARNING); return;
        }

        kullanicilar.put(kullanici, sifre);
        dosyaService.kullanicilariKaydet(kullanicilar);

        yeniKullaniciField.setText("");
        yeniSifreField.setText("");
        sifreTekrarField.setText("");

        listeGuncelle();
        ToastNotification.show((JFrame) getOwner(),
                "\"" + kullanici + "\" eklendi.",
                ToastNotification.Type.SUCCESS);
    }

    // ── Kullanıcı sil ─────────────────────────────────────────────────────────
    private void kullaniciSil(String kullanici) {
        if ("admin".equals(kullanici)) {
            ToastNotification.show((JFrame) getOwner(),
                    "Admin kullan\u0131c\u0131s\u0131 silinemez.",
                    ToastNotification.Type.WARNING); return;
        }
        if (kullanicilar.size() <= 1) {
            ToastNotification.show((JFrame) getOwner(),
                    "Son kullan\u0131c\u0131 silinemez.",
                    ToastNotification.Type.WARNING); return;
        }
        int secim = JOptionPane.showConfirmDialog(this,
                "<html>\"<b>" + kullanici + "</b>\" kullan\u0131c\u0131s\u0131n\u0131 silmek istiyor musunuz?</html>",
                "Kullan\u0131c\u0131 Sil", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (secim == JOptionPane.YES_OPTION) {
            kullanicilar.remove(kullanici);
            dosyaService.kullanicilariKaydet(kullanicilar);
            listeGuncelle();
            ToastNotification.show((JFrame) getOwner(),
                    "\"" + kullanici + "\" silindi.",
                    ToastNotification.Type.WARNING);
        }
    }

    // ── Liste güncelle ────────────────────────────────────────────────────────
    private void listeGuncelle() {
        listePanel.removeAll();
        for (String ad : kullanicilar.keySet()) {
            listePanel.add(kullaniciSatiri(ad));
            listePanel.add(Box.createVerticalStrut(6));
        }
        listePanel.revalidate();
        listePanel.repaint();
    }

    private JPanel kullaniciSatiri(String kullanici) {
        JPanel row = roundCard();
        row.setLayout(new BorderLayout(10, 0));
        row.setBorder(new EmptyBorder(10, 14, 10, 14));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        // Avatar dairesi
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor("admin".equals(kullanici)
                        ? new Color(219, 234, 254) : new Color(220, 252, 231));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor("admin".equals(kullanici) ? ACCENT : GREEN);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                String harf = kullanici.substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(harf,
                        (getWidth() - fm.stringWidth(harf)) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(34, 34));

        // İsim + badge
        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        info.setOpaque(false);
        JLabel isimLbl = new JLabel(kullanici);
        isimLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        isimLbl.setForeground(TITLE_FG);
        info.add(isimLbl);
        if ("admin".equals(kullanici)) {
            JLabel badge = new JLabel("admin");
            badge.setFont(new Font("SansSerif", Font.BOLD, 10));
            badge.setForeground(ACCENT);
            badge.setOpaque(true);
            badge.setBackground(new Color(219, 234, 254));
            badge.setBorder(new EmptyBorder(2, 6, 2, 6));
            info.add(badge);
        }

        // Sil butonu
        UIFactory.RoundButton silBtn = new UIFactory.RoundButton(
                "\u2715", new Color(255, 235, 235), RED, null, 8);
        silBtn.setPreferredSize(new Dimension(32, 32));
        silBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        silBtn.addActionListener(e -> kullaniciSil(kullanici));
        if ("admin".equals(kullanici)) {
            silBtn.setEnabled(false);
        }

        row.add(avatar, BorderLayout.WEST);
        row.add(info,   BorderLayout.CENTER);
        row.add(silBtn, BorderLayout.EAST);
        return row;
    }

    // ── Yardımcılar ───────────────────────────────────────────────────────────
    private JLabel bolumBaslik(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(TITLE_FG);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(LABEL_FG);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 208, 228), 1),
                new EmptyBorder(4, 10, 4, 10)));
        addFocus(f);
        return f;
    }

    private void styleSifre(JPasswordField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 208, 228), 1),
                new EmptyBorder(4, 10, 4, 10)));
        addFocus(f);
    }

    private void addFocus(JTextField f) {
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2),
                        new EmptyBorder(3, 9, 3, 9)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 208, 228), 1),
                        new EmptyBorder(4, 10, 4, 10)));
            }
        });
    }

    private JPanel roundCard() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(
                        0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.setColor(CARD_BDR);
                g2.setStroke(new java.awt.BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(
                        0.5f, 0.5f, getWidth() - 2, getHeight() - 2, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return p;
    }
}