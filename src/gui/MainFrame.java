package gui;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField markaField;
    private JTextField modelField;
    private JTextField fiyatField;
    private JButton ekleButton;
    private JTable aracTable;
    private JScrollPane scrollPane;

    public MainFrame() {
        // Frame ayarları
        setTitle("Araç Kiralama Sistemi");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        add(mainPanel);

        // Başlık
        titleLabel = new JLabel("Araç Kiralama Sistemi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(280, 20, 400, 40);
        mainPanel.add(titleLabel);

        // Label'lar
        JLabel markaLabel = new JLabel("Marka:");
        JLabel modelLabel = new JLabel("Model:");
        JLabel fiyatLabel = new JLabel("Fiyat:");

        markaLabel.setBounds(50, 80, 100, 20);
        modelLabel.setBounds(50, 130, 100, 20);
        fiyatLabel.setBounds(50, 180, 100, 20);

        mainPanel.add(markaLabel);
        mainPanel.add(modelLabel);
        mainPanel.add(fiyatLabel);

        // TextField'lar
        markaField = new JTextField();
        modelField = new JTextField();
        fiyatField = new JTextField();

        markaField.setBounds(50, 100, 200, 30);
        modelField.setBounds(50, 150, 200, 30);
        fiyatField.setBounds(50, 200, 200, 30);

        mainPanel.add(markaField);
        mainPanel.add(modelField);
        mainPanel.add(fiyatField);

        // Buton
        ekleButton = new JButton("Araç Ekle");
        ekleButton.setBounds(50, 260, 200, 40);
        mainPanel.add(ekleButton);

        // Table
        String[] kolonlar = {"ID", "Marka", "Model", "Fiyat"};
        Object[][] veriler = {};
        aracTable = new JTable(veriler, kolonlar);
        scrollPane = new JScrollPane(aracTable);
        scrollPane.setBounds(300, 100, 500, 300);
        mainPanel.add(scrollPane);

        // Frame'in görünür hale gelmesi
        setVisible(true);
    }
}