package gui.screens;

import gui.UIFactory;
import services.DosyaService;
import services.KiralamaService;
import services.AracService;
import models.Arac;
import models.Kiralama;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import gui.theme.AppFonts;

/**
 * Kullanıcı giriş ekranı.
 * Kullanıcılar data/kullanicilar.txt dosyasından yüklenir.
 * Dosya yoksa varsayılan: admin / 1234
 * 3 yanlış girişte giriş butonu 30 saniye kilitlenir.
 */
public class LoginFrame extends JFrame {

    private static final int MAX_DENEME    = 3;
    private static final int KILIT_SURE_SN = 30;

    private static final Color BG        = new Color(243, 244, 248);
    private static final Color CARD_BG   = Color.WHITE;
    private static final Color CARD_BDR  = new Color(212, 218, 232);
    private static final Color ACCENT    = new Color(41, 98, 255);
    private static final Color TITLE_FG  = new Color(18, 25, 50);
    private static final Color LABEL_FG  = new Color(52, 62, 88);
    private static final Color INPUT_BDR = new Color(192, 200, 220);
    private static final Color ERR_FG    = new Color(200, 30, 30);

    private JTextField     kullaniciField;
    private JPasswordField sifreField;
    private JLabel         hataMesaji;
    private UIFactory.RoundButton girisBtn;

    private int yanlisSayisi = 0;
    private boolean kilitli  = false;

    // Kullanıcı adı → şifre
    private final Map<String, String> kullanicilar;

    // ── Veri servisleri (giriş başarılı olunca MainFrame'e geç) ──────────────
    private final DosyaService dosyaService = new DosyaService();

    public LoginFrame() {
        kullanicilar = dosyaService.kullanicilariYukle();

        setTitle("Araç Kiralama Sistemi \u2014 Giri\u015f");
        Image appIcon = Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource("/assets/kirala360.png"));
        setIconImage(appIcon);
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(null);
        root.setBackground(BG);
        add(root);

        LoginCard card = new LoginCard();
        card.setBounds(40, 30, 400, 540);
        root.add(card);

        // Başlık
        ImageIcon logoIcon = new ImageIcon(
                getClass().getResource("/assets/kirala360.png"));
        Image scaledLogo = logoIcon.getImage()
                .getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledLogo));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel baslik = new JLabel("Kirala360", SwingConstants.CENTER);
        baslik.setFont(AppFonts.TITLE);
        baslik.setForeground(TITLE_FG);
        baslik.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel altBaslik = new JLabel("L\u00fctfen Giri\u015f Yap\u0131n", SwingConstants.CENTER);
        altBaslik.setFont(AppFonts.BODY);
        altBaslik.setForeground(new Color(120, 130, 155));
        altBaslik.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Kullanıcı adı
        JLabel kullaniciLbl = kullaniciLabel("Kullan\u0131c\u0131 Ad\u0131");
        kullaniciField = new JTextField();
        styleField(kullaniciField, false);

        // Şifre
        JLabel sifreLbl = kullaniciLabel("\u015fifre");
        sifreField = new JPasswordField();
        styleField(sifreField, false);

        // Hata
        hataMesaji = new JLabel(" ", SwingConstants.CENTER);
        hataMesaji.setFont(AppFonts.BODY_MEDIUM);
        hataMesaji.setForeground(ERR_FG);
        hataMesaji.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Giriş butonu
        girisBtn = new UIFactory.RoundButton("  Giri\u015f Yap", ACCENT, Color.WHITE, null, 20);
        girisBtn.setPreferredSize(new Dimension(220, 45));
        girisBtn.setMaximumSize(new Dimension(220, 45));
        girisBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(baslik);
        card.add(Box.createVerticalStrut(2));
        card.add(altBaslik);
        card.add(Box.createVerticalStrut(18));
        card.add(kullaniciLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(kullaniciField);
        card.add(Box.createVerticalStrut(10));
        card.add(sifreLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(sifreField);
        card.add(Box.createVerticalStrut(8));
        card.add(hataMesaji);
        card.add(Box.createVerticalStrut(4));
        card.add(girisBtn);

        girisBtn.addActionListener(e -> girisYap());
        sifreField.addActionListener(e -> girisYap());
        kullaniciField.addActionListener(e -> sifreField.requestFocus());

        setVisible(true);
    }

    // ── Giriş kontrolü ───────────────────────────────────────────────────────
    private void girisYap() {
        if (kilitli) return;

        String kullanici = kullaniciField.getText().trim();
        String sifre     = new String(sifreField.getPassword()).trim();

        String kayitliSifre = kullanicilar.get(kullanici);

        if (kayitliSifre != null && kayitliSifre.equals(sifre)) {
            // ── Giriş başarılı: verileri yükle, MainFrame aç ─────────────────
            girisBasarili(kullanici);
        } else {
            yanlisSayisi++;
            sifreField.setText("");
            styleBorder(sifreField, true);
            styleBorder(kullaniciField, kayitliSifre == null);

            int kalan = MAX_DENEME - yanlisSayisi;
            if (yanlisSayisi >= MAX_DENEME) {
                hesabiKitle();
            } else {
                hataMesaji.setText("Hatal\u0131 giri\u015f! " + kalan + " hakk\u0131n\u0131z kald\u0131.");
            }
        }
    }

    private void girisBasarili(String kullanici) {
        // Verileri dosyadan yükle
        DosyaService ds        = new DosyaService();
        ArrayList<Arac> araclar = ds.araclariYukle();
        ArrayList<Kiralama> kiralamalar = ds.kiralamalariYukle(araclar);

        // Kiralanan araçları müsait=false olarak işaretle
        for (Kiralama k : kiralamalar) {
            k.getArac().setMusaitMi(false);
        }

        dispose();
        new MainFrame(araclar, kiralamalar, kullanici);
    }

    private void hesabiKitle() {
        kilitli = true;
        girisBtn.setEnabled(false);
        hataMesaji.setForeground(new Color(160, 60, 0));

        // Geri sayım
        final int[] kalan = {KILIT_SURE_SN};
        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            kalan[0]--;
            if (kalan[0] <= 0) {
                timer.stop();
                kilitli      = false;
                yanlisSayisi = 0;
                girisBtn.setEnabled(true);
                hataMesaji.setForeground(ERR_FG);
                hataMesaji.setText("Tekrar deneyebilirsiniz.");
            } else {
                hataMesaji.setText("Hesap kilitlendi. " + kalan[0] + " saniye bekleyin.");
            }
        });
        timer.start();
        hataMesaji.setText("3 yanl\u0131\u015f giri\u015f! " + KILIT_SURE_SN + " saniye kilitlendi.");
    }

    // ── Yardımcı ─────────────────────────────────────────────────────────────
    private JLabel kullaniciLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppFonts.BODY_MEDIUM);
        l.setForeground(LABEL_FG);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    private void styleField(JTextField f, boolean hata) {
        f.setFont(AppFonts.BODY_MEDIUM);
        f.setMaximumSize(new Dimension(280, 38));
        f.setPreferredSize(new Dimension(280, 38));
        f.setMaximumSize(new Dimension(280, 38));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleBorder(f, hata);
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2),
                        new EmptyBorder(2, 7, 2, 7)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                styleBorder(f, false);
            }
        });
    }

    private void styleBorder(JTextField f, boolean hata) {
        Color bdr = hata ? new Color(200, 60, 60) : INPUT_BDR;
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bdr, hata ? 2 : 1),
                new EmptyBorder(3, 8, 3, 8)));
    }

    // ── Yuvarlak kart ─────────────────────────────────────────────────────────
    static class LoginCard extends JPanel {
        LoginCard() {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(22, 24, 22, 24));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fill(new RoundRectangle2D.Float(2, 3, getWidth()-3, getHeight()-2, 16, 16));
            g2.setColor(CARD_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-2, getHeight()-2, 14, 14));
            g2.setColor(CARD_BDR);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-3, getHeight()-3, 14, 14));
            g2.dispose();
        }
    }
}