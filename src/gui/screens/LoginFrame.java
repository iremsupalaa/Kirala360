package gui.screens;

import gui.UIFactory;
import services.DosyaService;
import models.Arac;
import models.Kiralama;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Map;

public class LoginFrame extends JFrame {

    private static final int MAX_DENEME    = 3;
    private static final int KILIT_SURE_SN = 30;

    private static final Color BG        = new Color(235, 238, 248);
    private static final Color CARD_BG   = Color.WHITE;
    private static final Color CARD_BDR  = new Color(210, 216, 232);
    private static final Color ACCENT    = new Color(41, 98, 255);
    private static final Color TITLE_FG  = new Color(18, 25, 50);
    private static final Color LABEL_FG  = new Color(70, 80, 110);
    private static final Color INPUT_BDR = new Color(200, 208, 228);
    private static final Color ERR_FG    = new Color(200, 30, 30);

    private JTextField     kullaniciField;
    private JPasswordField sifreField;
    private JLabel         hataMesaji;
    private UIFactory.RoundButton girisBtn;

    private int     yanlisSayisi = 0;
    private boolean kilitli      = false;

    private final Map<String, String> kullanicilar;
    public LoginFrame() {
        kullanicilar = new DosyaService().kullanicilariYukle();

        setTitle("Ara\u00e7 Kiralama Sistemi \u2014 Giri\u015f");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // ── Kök: gradient arka plan ───────────────────────────────────────────
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(220, 228, 255),
                        getWidth(), getHeight(), new Color(235, 240, 255));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setBackground(new Color(220, 228, 255));
        add(root);

        // ── Kart ─────────────────────────────────────────────────────────────
        LoginCard card = new LoginCard();

        // Logo — assets klasöründen yükle
        JPanel ikonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ikonPanel.setOpaque(false);
        ikonPanel.setAlignmentX(CENTER_ALIGNMENT);
        try {
            java.net.URL url = getClass().getClassLoader()
                    .getResource("assets/kirala360.png");
            if (url == null) {
                // Classpath'ta bulunamazsa dosya yolundan dene
                java.io.File f = new java.io.File("src/assets/kirala360.png");
                if (!f.exists()) f = new java.io.File("assets/kirala360.png");
                if (f.exists()) url = f.toURI().toURL();
            }
            if (url != null) {
                ImageIcon raw = new ImageIcon(url);
                Image scaled = raw.getImage().getScaledInstance(72, 72,
                        java.awt.Image.SCALE_SMOOTH);
                ikonPanel.add(new JLabel(new ImageIcon(scaled)));
            }
        } catch (Exception ignored) {}

        // Başlık
        JLabel baslik = new JLabel("Kirala360", SwingConstants.CENTER);
        baslik.setFont(new Font("SansSerif", Font.BOLD, 22));
        baslik.setForeground(TITLE_FG);
        baslik.setAlignmentX(CENTER_ALIGNMENT);

        JLabel altBaslik = new JLabel("Hesab\u0131n\u0131za giri\u015f yap\u0131n", SwingConstants.CENTER);
        altBaslik.setFont(new Font("SansSerif", Font.PLAIN, 13));
        altBaslik.setForeground(new Color(120, 132, 160));
        altBaslik.setAlignmentX(CENTER_ALIGNMENT);

        // Ayırıcı çizgi
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(230, 234, 245));

        // Kullanıcı adı
        kullaniciField = styledField("Kullan\u0131c\u0131 ad\u0131n\u0131z\u0131 girin");
        // Şifre
        sifreField = new JPasswordField();
        styleSifre(sifreField);

        // Hata
        hataMesaji = new JLabel(" ", SwingConstants.CENTER);
        hataMesaji.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hataMesaji.setForeground(ERR_FG);
        hataMesaji.setAlignmentX(CENTER_ALIGNMENT);

        // Giriş butonu
        girisBtn = new UIFactory.RoundButton(
                "Giri\u015f Yap", ACCENT, Color.WHITE, null, 22);
        girisBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        girisBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        girisBtn.setAlignmentX(CENTER_ALIGNMENT);

        // Alt not
        JLabel notLbl = new JLabel("admin / 1234 ile giri\u015f yap\u0131labilir", SwingConstants.CENTER);
        notLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        notLbl.setForeground(new Color(160, 170, 195));
        notLbl.setAlignmentX(CENTER_ALIGNMENT);

        // Karta ekle
        card.add(ikonPanel);
        card.add(Box.createVerticalStrut(12));
        card.add(baslik);
        card.add(Box.createVerticalStrut(4));
        card.add(altBaslik);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));
        card.add(formLabel("Kullan\u0131c\u0131 Ad\u0131"));
        card.add(Box.createVerticalStrut(6));
        card.add(kullaniciField);
        card.add(Box.createVerticalStrut(14));
        card.add(formLabel("\u015eifre"));
        card.add(Box.createVerticalStrut(6));
        card.add(sifreField);
        card.add(Box.createVerticalStrut(12));
        card.add(hataMesaji);
        card.add(Box.createVerticalStrut(8));
        card.add(girisBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        root.add(card, gbc);

        girisBtn.addActionListener(e -> girisYap());
        sifreField.addActionListener(e -> girisYap());
        kullaniciField.addActionListener(e -> sifreField.requestFocus());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── Giriş ────────────────────────────────────────────────────────────────
    private void girisYap() {
        if (kilitli) return;
        String kullanici = kullaniciField.getText().trim();
        String sifre     = new String(sifreField.getPassword()).trim();
        String kayitli   = kullanicilar.get(kullanici);

        if (kayitli != null && kayitli.equals(sifre)) {
            girisBasarili(kullanici);
        } else {
            yanlisSayisi++;
            sifreField.setText("");
            styleBorder(sifreField, true);
            styleBorder(kullaniciField, kayitli == null);
            int kalan = MAX_DENEME - yanlisSayisi;
            if (yanlisSayisi >= MAX_DENEME) hesabiKitle();
            else hataMesaji.setText("Hatal\u0131 giri\u015f! " + kalan + " hakk\u0131n\u0131z kald\u0131.");
        }
    }

    private void girisBasarili(String kullanici) {
        try {
            new java.io.File("data").mkdirs();
            DosyaService ds = new DosyaService();
            ArrayList<Arac> araclar = ds.araclariYukle();
            ArrayList<Kiralama> kiralamalar = ds.kiralamalariYukle(araclar);
            for (Kiralama k : kiralamalar) k.getArac().setMusaitMi(false);
            new MainFrame(araclar, kiralamalar, kullanici);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            hataMesaji.setText("Hata: " + ex.getMessage());
        }
    }

    private void hesabiKitle() {
        kilitli = true;
        girisBtn.setEnabled(false);
        final int[] kalan = {KILIT_SURE_SN};
        Timer t = new Timer(1000, null);
        t.addActionListener(e -> {
            kalan[0]--;
            if (kalan[0] <= 0) {
                t.stop(); kilitli = false; yanlisSayisi = 0;
                girisBtn.setEnabled(true);
                hataMesaji.setForeground(ERR_FG);
                hataMesaji.setText("Tekrar deneyebilirsiniz.");
            } else {
                hataMesaji.setText(kalan[0] + " saniye kilitli...");
            }
        });
        t.start();
        hataMesaji.setForeground(new Color(160, 60, 0));
        hataMesaji.setText("3 yanl\u0131\u015f! " + KILIT_SURE_SN + " saniye kilitlendi.");
    }

    // ── Yardımcılar ───────────────────────────────────────────────────────────
    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(LABEL_FG);
        l.setAlignmentX(CENTER_ALIGNMENT);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setAlignmentX(CENTER_ALIGNMENT);
        styleBorder(f, false);
        addFocusEffect(f);
        return f;
    }

    private void styleSifre(JPasswordField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        f.setAlignmentX(CENTER_ALIGNMENT);
        styleBorder(f, false);
        addFocusEffect(f);
    }

    private void addFocusEffect(JTextField f) {
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2),
                        new EmptyBorder(4, 10, 4, 10)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                styleBorder(f, false);
            }
        });
    }

    private void styleBorder(JTextField f, boolean hata) {
        Color bdr = hata ? new Color(220, 50, 50) : INPUT_BDR;
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bdr, hata ? 2 : 1),
                new EmptyBorder(5, 10, 5, 10)));
    }

    // ── Kart bileşeni ────────────────────────────────────────────────────────
    static class LoginCard extends JPanel {
        LoginCard() {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(32, 32, 32, 32));
            setPreferredSize(new Dimension(360, 480));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // Gölge katmanları
            for (int i = 3; i >= 1; i--) {
                g2.setColor(new Color(60, 80, 160, 8 * i));
                g2.fill(new RoundRectangle2D.Float(
                        i, i + 2, getWidth() - i * 2, getHeight() - i * 2, 20, 20));
            }
            // Kart
            g2.setColor(CARD_BG);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, 20, 20));
            // Kenarlık
            g2.setColor(new Color(220, 226, 242));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 5, getHeight() - 5, 20, 20));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}