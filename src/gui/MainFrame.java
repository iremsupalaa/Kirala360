package gui;

import models.Arac;
import models.Kiralama;
import models.Musteri;
import services.AracService;
import services.KiralamaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField markaField;
    private JTextField modelField;
    private JTextField fiyatField;
    private JTextField musteriField;
    private JTextField gunField;
    private JButton ekleButton;
    private JButton kiralaButton;
    private JTable aracTable;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private AracService aracService;
    private KiralamaService kiralamaService;
    private JTextField kiralamaIdField;
    private JButton musaitButton;


    public MainFrame() {
        // Service
        aracService = new AracService();
        kiralamaService = new KiralamaService();

        // Frame
        setTitle("Araç Kiralama Sistemi");
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Başlık
        titleLabel = new JLabel("Araç Kiralama Sistemi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(260, 20, 400, 40);
        mainPanel.add(titleLabel);


        // ID
        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(50, 80, 100, 20);
        mainPanel.add(idLabel);
        idField = new JTextField();
        idField.setBounds(50, 100, 200, 30);
        mainPanel.add(idField);

        // Marka
        JLabel markaLabel = new JLabel("Marka:");
        markaLabel.setBounds(50, 140, 100, 20);
        mainPanel.add(markaLabel);
        markaField = new JTextField();
        markaField.setBounds(50, 160, 200, 30);
        mainPanel.add(markaField);


        // Model
        JLabel modelLabel = new JLabel("Model:");
        modelLabel.setBounds(50, 200, 100, 20);
        mainPanel.add(modelLabel);
        modelField = new JTextField();
        modelField.setBounds(50, 220, 200, 30);
        mainPanel.add(modelField);


        // Fiyat
        JLabel fiyatLabel = new JLabel("Fiyat:");
        fiyatLabel.setBounds(50, 260, 100, 20);
        mainPanel.add(fiyatLabel);
        fiyatField = new JTextField();
        fiyatField.setBounds(50, 280, 200, 30);
        mainPanel.add(fiyatField);


        // Araç Ekle Butonu
        ekleButton = new JButton("Araç Ekle");
        ekleButton.setBounds(50, 340, 200, 40);
        ekleButton.setFocusPainted(false);
        mainPanel.add(ekleButton);


        //Kiralama ID
        JLabel kiralamaIdLabel = new JLabel("Araç ID:");
        kiralamaIdLabel.setBounds(50, 410, 100, 20);
        kiralamaIdField = new JTextField();
        kiralamaIdField.setBounds(50, 430, 200, 30);
        mainPanel.add(kiralamaIdLabel);
        mainPanel.add(kiralamaIdField);


        // Müşteri
        JLabel musteriLabel = new JLabel("Müşteri:");
        musteriLabel.setBounds(50, 470, 100, 20);
        mainPanel.add(musteriLabel);
        musteriField = new JTextField();
        musteriField.setBounds(50, 490, 200, 30);
        mainPanel.add(musteriField);


        // Gün Sayısı
        JLabel gunLabel = new JLabel("Gün:");
        gunLabel.setBounds(50, 540, 100, 20);
        mainPanel.add(gunLabel);
        gunField = new JTextField();
        gunField.setBounds(50, 560, 200, 30);
        mainPanel.add(gunField);


        // Kirala Butonu
        kiralaButton = new JButton("Araç Kirala");
        kiralaButton.setBounds(50, 610, 200, 40);
        kiralaButton.setFocusPainted(false);
        mainPanel.add(kiralaButton);


        // Table
        String[] kolonlar = {"ID", "Marka", "Model", "Fiyat", "Müsaitlik"};
        tableModel = new DefaultTableModel(kolonlar, 0);
        aracTable = new JTable(tableModel);
        aracTable.setRowHeight(30);
        scrollPane = new JScrollPane(aracTable);
        scrollPane.setBounds(300, 100, 550, 450);
        mainPanel.add(scrollPane);

        //Müsait Araçlar Butonu
        musaitButton = new JButton("Müsait Araçlar");
        musaitButton.setBounds(300, 570, 180, 35);
        mainPanel.add(musaitButton);

        //Tüm Araçlar Butonu
        JButton tumButton = new JButton("Tüm Araçlar");
        tumButton.setBounds(500, 570, 150, 35);
        mainPanel.add(tumButton);
        tumButton.addActionListener(e -> tabloyuYenile());


        // Table doldur
        tabloyuDoldur();


        // Button Actions
        ekleButton.addActionListener(e -> aracEkle());
        kiralaButton.addActionListener(e -> aracKirala());
        musaitButton.addActionListener(e -> { tableModel.setRowCount(0);
            for (Arac arac : aracService.musaitAraclariGetir()) {
                tableModel.addRow(new Object[]{
                        arac.getId(),
                        arac.getMarka(),
                        arac.getModel(),
                        arac.getGunlukFiyat(),
                        arac.isMusaitMi() ? "Müsait" : "Kirada"
                });
            }
        });

        // Frame görünür
        setVisible(true);
    }


    // Araç Ekleme
    private void aracEkle() {
        try {
            if (idField.getText().isEmpty() ||
                    markaField.getText().isEmpty() ||
                    modelField.getText().isEmpty() ||
                    fiyatField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tüm alanları doldurun!"
                );
                return;
            }
            int id = Integer.parseInt(idField.getText());
            String marka = markaField.getText();
            String model = modelField.getText();
            double fiyat = Double.parseDouble(fiyatField.getText());
            Arac arac = new Arac(
                    id,
                    marka,
                    model,
                    fiyat,
                    true
            );
            aracService.aracEkle(arac);
            tableModel.addRow(new Object[]{
                    arac.getId(),
                    arac.getMarka(),
                    arac.getModel(),
                    arac.getGunlukFiyat(),
                    arac.isMusaitMi() ? "Müsait" : "Kirada"
            });
            alanlariTemizle();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "ID ve fiyat sayısal olmalıdır!"
            );
        }
    }


    // Araç Kiralama
    private void aracKirala() {
        try {
            if (kiralamaIdField.getText().isEmpty() ||
                    musteriField.getText().isEmpty() ||
                    gunField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tüm alanları doldurun!"
                );
                return;
            }
            int id = Integer.parseInt(kiralamaIdField.getText());
            String musteriAdi = musteriField.getText();
            int gunSayisi = Integer.parseInt(gunField.getText());

            Arac arac = aracService.aracGetir(id);

            if (arac == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Araç bulunamadı!"
                );
                return;
            }
            if (!arac.isMusaitMi()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Bu araç zaten kiralanmış!"
                );
                return;
            }
            Musteri musteri = new Musteri(
                    1,
                    musteriAdi,
                    "5555555555"
            );
            Kiralama kiralama = new Kiralama(
                    1,
                    arac,
                    musteri,
                    gunSayisi
            );
            kiralamaService.aracKirala(kiralama);
            tabloyuYenile();

            JOptionPane.showMessageDialog(
                    this,
                    "Toplam Ücret: "
                            + String.format("%.2f",
                            kiralama.getToplamUcret())
                            + " TL"
            );
            kiralamaAlanlariniTemizle();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Geçersiz giriş!"
            );
            kiralamaAlanlariniTemizle();
        }
    }

    // Form Temizleme
    private void alanlariTemizle() {
        idField.setText("");
        markaField.setText("");
        modelField.setText("");
        fiyatField.setText("");
    }
    private void kiralamaAlanlariniTemizle() {
        kiralamaIdField.setText("");
        musteriField.setText("");
        gunField.setText("");
    }

    // Table Doldurma
    private void tabloyuDoldur() {
        for (Arac arac : aracService.getAracListesi()) {
            tableModel.addRow(new Object[]{
                    arac.getId(),
                    arac.getMarka(),
                    arac.getModel(),
                    arac.getGunlukFiyat(),
                    arac.isMusaitMi() ? "Müsait" : "Kirada"
            });
        }
    }
    private void tabloyuYenile() {
        tableModel.setRowCount(0);
        tabloyuDoldur();
    }
}