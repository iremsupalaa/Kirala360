package gui.screens;

import gui.UIFactory;
import gui.components.ToastNotification;
import gui.theme.AppColors;
import gui.theme.AppFonts;
import services.DosyaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

public class KullaniciYonetimiDialog extends JDialog {

    private static final Color BG        = new Color(245, 246, 250);
    private static final Color CARD_BG   = Color.WHITE;
    private static final Color CARD_BDR  = new Color(218, 224, 238);
    private static final Color TITLE_FG  = AppColors.TITLE_FG;
    private static final Color LABEL_FG  = AppColors.LABEL_FG;
    private static final Color ACCENT    = AppColors.ACCENT;
    private static final Color GREEN     = new Color(22, 163, 74);
    private static final Color RED       = new Color(220, 38, 38);

    private final DosyaService dosyaService = new DosyaService();
    private Map<String, String> kullanicilar;

    private JTextField     kullaniciField;
    private JPasswordField sifreField;
    private JPasswordField sifreTekrarField;
    private JPanel         listePanel;
    private JLabel         sayacLbl;

    private String         duzenlemeModu = null;
    private JLabel         formBaslikLabel;
    private UIFactory.RoundButton kaydetBtn;
    private UIFactory.RoundButton iptalBtn;

    public KullaniciYonetimiDialog(JFrame parent) {
        super(parent, "Kullan\u0131c\u0131 Y\u00f6netimi", true);
        setSize(460, 660);
        setLocationRelativeTo(parent);
        setResizable(false);

        kullanicilar = dosyaService.kullanicilariYukle();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        add(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
    }

    // ── Başlık ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(CARD_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BDR),
                new EmptyBorder(16, 22, 16, 22)));

        // İkon — UIFactory.PersonIcon ile
        JPanel ikonWrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(219, 234, 254));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ikonWrap.setOpaque(false);
        ikonWrap.setPreferredSize(new Dimension(42, 42));
        JLabel ikonLbl = new JLabel(new UIFactory.PersonIcon(18, ACCENT), SwingConstants.CENTER);
        ikonLbl.setHorizontalAlignment(SwingConstants.CENTER);
        ikonWrap.add(ikonLbl, BorderLayout.CENTER);

        JPanel yazalar = new JPanel();
        yazalar.setLayout(new BoxLayout(yazalar, BoxLayout.Y_AXIS));
        yazalar.setBackground(CARD_BG);
        yazalar.setBorder(new EmptyBorder(0, 10, 0, 0));

        JLabel baslik = new JLabel("Kullan\u0131c\u0131 Y\u00f6netimi");
        baslik.setFont(AppFonts.SECTION);
        baslik.setForeground(TITLE_FG);

        JLabel alt = new JLabel("Ekle, d\u00fczenle veya sil");
        alt.setFont(AppFonts.SMALL);
        alt.setForeground(new Color(130, 140, 165));

        yazalar.add(baslik);
        yazalar.add(Box.createVerticalStrut(2));
        yazalar.add(alt);

        header.add(ikonWrap, BorderLayout.WEST);
        header.add(yazalar, BorderLayout.CENTER);
        return header;
    }

    // ── İçerik ───────────────────────────────────────────────────────────────
    private JScrollPane buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        content.add(buildFormKart());
        content.add(Box.createVerticalStrut(22));

        // Liste başlık satırı
        JPanel listeBaslikRow = new JPanel(new BorderLayout());
        listeBaslikRow.setOpaque(false);
        listeBaslikRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        listeBaslikRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel listeBaslik = new JLabel("Mevcut Kullan\u0131c\u0131lar");
        listeBaslik.setFont(AppFonts.BODY_MEDIUM);
        listeBaslik.setForeground(TITLE_FG);

        sayacLbl = new JLabel();
        sayacLbl.setFont(AppFonts.SMALL);
        sayacLbl.setForeground(new Color(130, 140, 165));

        listeBaslikRow.add(listeBaslik, BorderLayout.WEST);
        listeBaslikRow.add(sayacLbl, BorderLayout.EAST);
        content.add(listeBaslikRow);
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
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        return scroll;
    }

    // ── Form kartı ────────────────────────────────────────────────────────────
    private JPanel buildFormKart() {
        JPanel card = roundCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 18, 18));

        // Başlık satırı
        JPanel baslikRow = new JPanel(new BorderLayout());
        baslikRow.setOpaque(false);
        baslikRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        baslikRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        formBaslikLabel = new JLabel("Yeni Kullan\u0131c\u0131 Ekle");
        formBaslikLabel.setFont(AppFonts.BODY_MEDIUM);
        formBaslikLabel.setForeground(TITLE_FG);

        iptalBtn = new UIFactory.RoundButton("  \u0130ptal",
                new Color(255, 235, 235), RED, null, 8);
        iptalBtn.setPreferredSize(new Dimension(68, 26));
        iptalBtn.setFont(AppFonts.SMALL);
        iptalBtn.setVisible(false);
        iptalBtn.addActionListener(e -> iptalModu());

        baslikRow.add(formBaslikLabel, BorderLayout.WEST);
        baslikRow.add(iptalBtn, BorderLayout.EAST);
        card.add(baslikRow);
        card.add(Box.createVerticalStrut(14));

        card.add(formLabel("Kullan\u0131c\u0131 Ad\u0131"));
        card.add(Box.createVerticalStrut(5));
        kullaniciField = styledField("\u00f6rn. iremsu");
        card.add(kullaniciField);
        card.add(Box.createVerticalStrut(12));

        card.add(formLabel("\u015eifre"));
        card.add(Box.createVerticalStrut(5));
        sifreField = styledPassword();
        card.add(sifreField);
        card.add(Box.createVerticalStrut(12));

        card.add(formLabel("\u015eifre Tekrar"));
        card.add(Box.createVerticalStrut(5));
        sifreTekrarField = styledPassword();
        card.add(sifreTekrarField);
        card.add(Box.createVerticalStrut(16));

        kaydetBtn = new UIFactory.RoundButton(
                "  Kullan\u0131c\u0131 Ekle", GREEN, Color.WHITE, null, 10);
        kaydetBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        kaydetBtn.setFont(AppFonts.BUTTON);
        kaydetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        kaydetBtn.addActionListener(e -> kaydet());
        card.add(kaydetBtn);

        return card;
    }

    // ── Footer ───────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        footer.setBackground(CARD_BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BDR));
        UIFactory.RoundButton kapatBtn = new UIFactory.RoundButton(
                "  Kapat", new Color(230, 232, 242), AppColors.LABEL_FG, null, 10);
        kapatBtn.setPreferredSize(new Dimension(90, 36));
        kapatBtn.setFont(AppFonts.BODY_MEDIUM);
        kapatBtn.addActionListener(e -> dispose());
        footer.add(kapatBtn);
        return footer;
    }

    // ── Kullanıcı satırı ──────────────────────────────────────────────────────
    private JPanel kullaniciSatiri(String kullanici) {
        boolean isAdmin = "admin".equals(kullanici);
        Color avatarBg  = isAdmin ? new Color(219, 234, 254) : new Color(220, 252, 231);
        Color avatarFg  = isAdmin ? ACCENT : GREEN;

        JPanel row = roundCard();
        row.setLayout(new BorderLayout(10, 0));
        row.setBorder(new EmptyBorder(10, 14, 10, 12));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        // Avatar
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(avatarBg);
                g2.fillOval(0, 0, 36, 36);
                g2.setColor(avatarFg);
                g2.setFont(AppFonts.BODY_MEDIUM.deriveFont(15f));
                String harf = kullanici.substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(harf, (36 - fm.stringWidth(harf)) / 2,
                        (36 + fm.getAscent()) / 2 - 2);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(36, 36));

        // Orta
        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        info.setOpaque(false);

        JLabel isimLbl = new JLabel(kullanici);
        isimLbl.setFont(AppFonts.BODY_MEDIUM);
        isimLbl.setForeground(TITLE_FG);
        info.add(isimLbl);

        if (isAdmin) {
            JLabel badge = new JLabel("admin");
            badge.setFont(AppFonts.BADGE);
            badge.setForeground(ACCENT);
            badge.setOpaque(true);
            badge.setBackground(new Color(219, 234, 254));
            badge.setBorder(new EmptyBorder(2, 8, 2, 8));
            info.add(badge);
        }

        // Butonlar
        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnGroup.setOpaque(false);

        // Düzenle — kalem ikonu
        UIFactory.RoundButton duzenleBtn = new UIFactory.RoundButton(
                "\u270F", new Color(234, 242, 255), ACCENT, null, 8);
        duzenleBtn.setPreferredSize(new Dimension(34, 32));
        duzenleBtn.setFont(new Font("Dialog", Font.PLAIN, 15));
        duzenleBtn.setToolTipText("\u015eifreyi d\u00fczenle");
        duzenleBtn.addActionListener(e -> duzenleModu(kullanici));

        // Sil — çarpı
        UIFactory.RoundButton silBtn = new UIFactory.RoundButton(
                "\u00d7", new Color(255, 235, 235), RED, null, 8);
        silBtn.setPreferredSize(new Dimension(34, 32));
        silBtn.setFont(new Font("Dialog", Font.BOLD, 17));
        silBtn.setToolTipText("Sil");
        silBtn.addActionListener(e -> kullaniciSil(kullanici));

        if (isAdmin) {
            silBtn.setEnabled(false);
            duzenleBtn.setEnabled(false);
        }

        btnGroup.add(duzenleBtn);
        btnGroup.add(silBtn);

        row.add(avatar,   BorderLayout.WEST);
        row.add(info,     BorderLayout.CENTER);
        row.add(btnGroup, BorderLayout.EAST);
        return row;
    }

    // ── Modlar ───────────────────────────────────────────────────────────────
    private void duzenleModu(String kullanici) {
        duzenlemeModu = kullanici;
        kullaniciField.setText(kullanici);
        kullaniciField.setEnabled(false);
        sifreField.setText("");
        sifreTekrarField.setText("");

        formBaslikLabel.setText("\u270E  \"" + kullanici + "\" \u2014 \u015eifre G\u00fcncelle");
        formBaslikLabel.setForeground(ACCENT);
        kaydetBtn.setText("  G\u00fcncelle");
        iptalBtn.setVisible(true);
        sifreField.requestFocusInWindow();
    }

    private void iptalModu() {
        duzenlemeModu = null;
        kullaniciField.setText("");
        kullaniciField.setEnabled(true);
        sifreField.setText("");
        sifreTekrarField.setText("");
        formBaslikLabel.setText("Yeni Kullan\u0131c\u0131 Ekle");
        formBaslikLabel.setForeground(TITLE_FG);
        kaydetBtn.setText("  Kullan\u0131c\u0131 Ekle");
        iptalBtn.setVisible(false);
    }

    // ── Kaydet ───────────────────────────────────────────────────────────────
    private void kaydet() {
        String kullanici = kullaniciField.getText().trim();
        String sifre     = new String(sifreField.getPassword()).trim();
        String tekrar    = new String(sifreTekrarField.getPassword()).trim();
        JFrame owner     = (JFrame) getOwner();

        if (kullanici.isEmpty()) { toast(owner, "Kullan\u0131c\u0131 ad\u0131 bo\u015f olamaz.", ToastNotification.Type.ERROR); return; }
        if (kullanici.length() < 3) { toast(owner, "En az 3 karakter.", ToastNotification.Type.WARNING); return; }
        if (sifre.isEmpty()) { toast(owner, "\u015eifre bo\u015f olamaz.", ToastNotification.Type.ERROR); return; }
        if (sifre.length() < 4) { toast(owner, "\u015eifre en az 4 karakter.", ToastNotification.Type.WARNING); return; }
        if (!sifre.equals(tekrar)) { toast(owner, "\u015eifreler e\u015fle\u015fmiyor!", ToastNotification.Type.ERROR); return; }

        if (duzenlemeModu != null) {
            kullanicilar.put(duzenlemeModu, sifre);
            dosyaService.kullanicilariKaydet(kullanicilar);
            toast(owner, "\"" + duzenlemeModu + "\" g\u00fcncellendi.", ToastNotification.Type.SUCCESS);
            iptalModu();
        } else {
            if (kullanicilar.containsKey(kullanici)) { toast(owner, "\"" + kullanici + "\" zaten var.", ToastNotification.Type.WARNING); return; }
            kullanicilar.put(kullanici, sifre);
            dosyaService.kullanicilariKaydet(kullanicilar);
            kullaniciField.setText(""); sifreField.setText(""); sifreTekrarField.setText("");
            toast(owner, "\"" + kullanici + "\" eklendi.", ToastNotification.Type.SUCCESS);
        }
        listeGuncelle();
    }

    // ── Sil ──────────────────────────────────────────────────────────────────
    private void kullaniciSil(String kullanici) {
        if ("admin".equals(kullanici)) { toast((JFrame)getOwner(), "Admin silinemez.", ToastNotification.Type.WARNING); return; }
        if (kullanicilar.size() <= 1)  { toast((JFrame)getOwner(), "Son kullan\u0131c\u0131 silinemez.", ToastNotification.Type.WARNING); return; }
        int s = JOptionPane.showConfirmDialog(this,
                "<html>\"<b>" + kullanici + "</b>\" silinsin mi?</html>",
                "Sil", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (s == JOptionPane.YES_OPTION) {
            kullanicilar.remove(kullanici);
            dosyaService.kullanicilariKaydet(kullanicilar);
            if (kullanici.equals(duzenlemeModu)) iptalModu();
            listeGuncelle();
            toast((JFrame)getOwner(), "\"" + kullanici + "\" silindi.", ToastNotification.Type.WARNING);
        }
    }

    // ── Liste güncelle ────────────────────────────────────────────────────────
    private void listeGuncelle() {
        listePanel.removeAll();
        for (String ad : kullanicilar.keySet()) {
            listePanel.add(kullaniciSatiri(ad));
            listePanel.add(Box.createVerticalStrut(6));
        }
        if (sayacLbl != null) sayacLbl.setText(kullanicilar.size() + " kullan\u0131c\u0131");
        listePanel.revalidate();
        listePanel.repaint();
    }

    // ── Yardımcılar ───────────────────────────────────────────────────────────
    private void toast(JFrame owner, String msg, ToastNotification.Type type) {
        ToastNotification.show(owner, msg, type);
    }

    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppFonts.BODY_MEDIUM);
        l.setForeground(LABEL_FG);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.setFont(AppFonts.BODY);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        normalBorder(f); addFocus(f);
        return f;
    }

    private JPasswordField styledPassword() {
        JPasswordField f = new JPasswordField();
        f.setFont(AppFonts.BODY);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        normalBorder(f); addFocus(f);
        return f;
    }

    private void normalBorder(JTextField f) {
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 208, 228), 1),
                new EmptyBorder(5, 10, 5, 10)));
    }

    private void addFocus(JTextField f) {
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2),
                        new EmptyBorder(4, 9, 4, 9)));
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) { normalBorder(f); }
        });
    }

    private JPanel roundCard() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(60, 80, 150, 10));
                g2.fill(new RoundRectangle2D.Float(1, 2, getWidth()-2, getHeight()-2, 14, 14));
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-2, getHeight()-2, 14, 14));
                g2.setColor(CARD_BDR);
                g2.setStroke(new java.awt.BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-3, getHeight()-3, 14, 14));
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