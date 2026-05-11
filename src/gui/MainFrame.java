package gui;

import models.Arac;
import services.AracService;
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
    private JButton ekleButton;
    private JTable aracTable;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private AracService aracService;
    public MainFrame() {
        // Service
        aracService = new AracService();
        // Frame
        setTitle("Araç Kiralama Sistemi");
        setSize(900, 600);
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

        // Buton
        ekleButton = new JButton("Araç Ekle");
        ekleButton.setBounds(50, 340, 200, 40);
        ekleButton.setFocusPainted(false);
        mainPanel.add(ekleButton);

        // Table
        String[] kolonlar = {"ID", "Marka", "Model", "Fiyat"};
        tableModel = new DefaultTableModel(kolonlar, 0);
        aracTable = new JTable(tableModel);
        aracTable.setRowHeight(30);
        scrollPane = new JScrollPane(aracTable);
        scrollPane.setBounds(300, 100, 550, 350);
        mainPanel.add(scrollPane);

        // Tabloyu doldur
        tabloyuDoldur();

        // Button action
        ekleButton.addActionListener(e -> aracEkle());

        // Frame görünür
        setVisible(true);
    }

    // Araç ekleme
    private void aracEkle() {
        try {
            int id = Integer.parseInt(idField.getText());
            String marka = markaField.getText();
            String model = modelField.getText();
            double fiyat = Double.parseDouble(fiyatField.getText());
            Arac arac = new Arac(id, marka, model, fiyat, true);
            aracService.aracEkle(arac);
            tableModel.addRow(new Object[]{
                    arac.getId(),
                    arac.getMarka(),
                    arac.getModel(),
                    arac.getGunlukFiyat()
            });
            alanlariTemizle();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "ID ve fiyat sayısal olmalıdır!"
            );
        }
    }

    // Form temizleme
    private void alanlariTemizle() {
        idField.setText("");
        markaField.setText("");
        modelField.setText("");
        fiyatField.setText("");
    }

    // Table doldurma
    private void tabloyuDoldur() {
        for (Arac arac : aracService.getAracListesi()) {
            tableModel.addRow(new Object[]{
                    arac.getId(),
                    arac.getMarka(),
                    arac.getModel(),
                    arac.getGunlukFiyat()
            });
        }
    }
}