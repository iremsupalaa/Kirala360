package gui.screens;

import gui.UIFactory;
import gui.screens.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Kullanıcı giriş ekranı.
 * Doğru şifre girildiğinde MainFrame açılır.
 * Kullanıcı adı: admin  |  Şifre: 1234
 */
public class LoginFrame extends JFrame {

    private static final String KULLANICI_ADI = "admin";
    private static final String SIFRE         = "1234";

    private static final Color BG        = new Color(243, 244, 248);
    private static final Color CARD_BG   = Color.WHITE;
    private static final Color CARD_BDR  = new Color(212, 218, 232);
    private static final Color ACCENT    = new Color(41, 98, 255);
    private static final Color TITLE_FG  = new Color(18, 25, 50);
    private static final Color LABEL_FG  = new Color(52, 62, 88);
    private static final Color INPUT_BDR = new Color(192, 200, 220);
    private static final Color ERR_FG    = new Color(200, 30, 30);

    private JTextField kullaniciField;
    private JPasswordField sifreField;
    private JLabel hataMesaji;

    public LoginFrame() {
        setTitle("Araç Kiralama Sistemi — Giriş");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(null);
        root.setBackground(BG);
        add(root);

        // Kart
        LoginCard card = new LoginCard();
        card.setBounds(40, 30, 340, 290);
        root.add(card);

        // Logo / başlık
        JLabel logo = new JLabel("\uD83D\uDE97", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.PLAIN, 36));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel baslik = new JLabel("Araç Kiralama Sistemi", SwingConstants.CENTER);
        baslik.setFont(new Font("SansSerif", Font.BOLD, 17));
        baslik.setForeground(TITLE_FG);
        baslik.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel altBaslik = new JLabel("Lütfen giriş yapın", SwingConstants.CENTER);
        altBaslik.setFont(new Font("SansSerif", Font.PLAIN, 13));
        altBaslik.setForeground(new Color(120, 130, 155));
        altBaslik.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Kullanıcı adı
        JLabel kullaniciLbl = new JLabel("Kullanıcı Adı");
        kullaniciLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        kullaniciLbl.setForeground(LABEL_FG);
        kullaniciLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        kullaniciField = new JTextField();
        kullaniciField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        kullaniciField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        kullaniciField.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleBorder(kullaniciField, false);
        focusEffect(kullaniciField);

        // Şifre
        JLabel sifreLbl = new JLabel("Şifre");
        sifreLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sifreLbl.setForeground(LABEL_FG);
        sifreLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        sifreField = new JPasswordField();
        sifreField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sifreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        sifreField.setAlignmentX(Component.LEFT_ALIGNMENT);
        styleBorder(sifreField, false);
        focusEffect(sifreField);

        // Hata mesajı
        hataMesaji = new JLabel(" ", SwingConstants.CENTER);
        hataMesaji.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hataMesaji.setForeground(ERR_FG);
        hataMesaji.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Giriş butonu
        UIFactory.RoundButton girisBtn = new UIFactory.RoundButton(
                "  Giriş Yap", ACCENT, Color.WHITE, null, 20);
        girisBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        girisBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Karta bileşenleri ekle
        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(baslik);
        card.add(Box.createVerticalStrut(2));
        card.add(altBaslik);
        card.add(Box.createVerticalStrut(16));
        card.add(kullaniciLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(kullaniciField);
        card.add(Box.createVerticalStrut(10));
        card.add(sifreLbl);
        card.add(Box.createVerticalStrut(3));
        card.add(sifreField);
        card.add(Box.createVerticalStrut(6));
        card.add(hataMesaji);
        card.add(Box.createVerticalStrut(4));
        card.add(girisBtn);

        // Aksiyon
        girisBtn.addActionListener(e -> girisYap());
        sifreField.addActionListener(e -> girisYap());
        kullaniciField.addActionListener(e -> sifreField.requestFocus());

        setVisible(true);
    }

    private void girisYap() {
        String kullanici = kullaniciField.getText().trim();
        String sifre     = new String(sifreField.getPassword()).trim();

        if (kullanici.equals(KULLANICI_ADI) && sifre.equals(SIFRE)) {
            dispose();
            new MainFrame();
        } else {
            hataMesaji.setText("Kullanıcı adı veya şifre hatalı!");
            sifreField.setText("");
            styleBorder(kullaniciField, !kullanici.equals(KULLANICI_ADI));
            styleBorder(sifreField, true);
        }
    }

    private void styleBorder(JTextField f, boolean hata) {
        Color bdr = hata ? new Color(200, 60, 60) : INPUT_BDR;
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bdr, hata ? 2 : 1),
                new EmptyBorder(3, 8, 3, 8)));
    }

    private void focusEffect(JTextField f) {
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

    // Yuvarlak kart
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