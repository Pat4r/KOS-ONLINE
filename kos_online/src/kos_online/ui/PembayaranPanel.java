package kos_online.ui;

import kos_online.services.RentalService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PembayaranPanel extends BasePanel {
    private final RentalService rentalService;
    private final int userId;
    private final String selectedKamar;
    private final double totalAmount;
    
    private JTextField totalField;
    private JButton uploadButton;
    private JLabel noFileLabel;
    private JButton submitButton;
    private File selectedFile;
    
    public PembayaranPanel(RentalService rentalService, int userId, String selectedKamar, double totalAmount) {
        this.rentalService = rentalService;
        this.userId = userId;
        this.selectedKamar = selectedKamar;
        this.totalAmount = totalAmount;
        
        setLayout(null);
        initializeComponents();
    }
    
    private void initializeComponents() {
        int panelWidth = 600;
        int panelHeight = 450;
        int x = (1280 - panelWidth) / 2;
        int y = (720 - panelHeight) / 2;
        
        JPanel centerPanel = new JPanel(null);
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        centerPanel.setBounds(x - 190, y - 80, panelWidth, panelHeight);
        add(centerPanel);
        
        // Title
        JLabel titleLabel = new JLabel("Pembayaran Aplikasi DANA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBounds(0, 20, panelWidth, 30);
        centerPanel.add(titleLabel);

        // Add QR Code image
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/qr-code.jpg"));
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds((panelWidth - 150) / 2, 60, 150, 150);
        centerPanel.add(imageLabel);
        
        // Total amount
        JLabel amountLabel = new JLabel("Total Yang Perlu Dibayar:");
        amountLabel.setFont(REGULAR_FONT);
        amountLabel.setBounds(30, 220, 200, 25);
        centerPanel.add(amountLabel);
        
        totalField = new JTextField();
        totalField.setFont(REGULAR_FONT);
        totalField.setEditable(false);
        totalField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        totalField.setBounds(30, 250, panelWidth - 60, 35);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        totalField.setText(formatter.format(totalAmount));
        centerPanel.add(totalField);
        
        // Payment proof section
        JLabel proofLabel = new JLabel("Bukti Pembayaran:");
        proofLabel.setFont(REGULAR_FONT);
        proofLabel.setBounds(30, 295, 200, 25);
        centerPanel.add(proofLabel);
        
        // Upload panel
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.setBounds(30, 325, panelWidth - 60, 35);
        
        uploadButton = createStyledButton("Upload Bukti", new Color(76, 175, 80));
        uploadButton.setPreferredSize(new Dimension(120, 30));
        uploadButton.addActionListener(e -> handleUpload());
        
        noFileLabel = new JLabel("No file chosen");
        noFileLabel.setFont(REGULAR_FONT);
        
        uploadPanel.add(uploadButton);
        uploadPanel.add(noFileLabel);
        centerPanel.add(uploadPanel);
        
        // Submit button
        submitButton = createStyledButton("Submit", new Color(76, 175, 80));
        submitButton.setBounds(panelWidth - 130, 375, 100, 30);
        submitButton.addActionListener(e -> handleSubmit());
        centerPanel.add(submitButton);
    }
    
    private void handleUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            noFileLabel.setText(selectedFile.getName());
        }
    }
    
    private void handleSubmit() {
        if (selectedFile == null) {
            showError("Mohon upload bukti pembayaran terlebih dahulu");
            return;
        }
        
        try {
            rentalService.updatePayment(selectedKamar, selectedFile.getAbsolutePath());
            
            showInfo("Pembayaran berhasil disubmit!");
            
            Container parent = getParent();
            while (!(parent instanceof Dashboard)) {
                parent = parent.getParent();
            }
            Dashboard dashboard = (Dashboard) parent;
            dashboard.refreshPanels();
            dashboard.showPanel("beranda");
            
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    public void stopTimer() {
    }
}