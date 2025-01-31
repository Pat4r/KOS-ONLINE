package kos_online.ui;

import kos_online.models.Room;
import kos_online.models.Rental;
import kos_online.models.RentalView;
import kos_online.models.User;
import kos_online.services.RentalService;
import kos_online.services.RoomService;
import kos_online.services.UserService;
import kos_online.config.ApplicationConfig;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class PenyewaPanelPemilik extends BasePanel {
    private final RentalService rentalService;
    private final RoomService roomService;
    private final UserService userService;
    private JTable penyewaTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, viewButton;
    private Timer refreshTimer;
    private static final int REFRESH_INTERVAL = 5000; // 5 seconds
    
    public PenyewaPanelPemilik(RentalService rentalService, RoomService roomService) {
        this.rentalService = rentalService;
        this.roomService = roomService;
        this.userService = ApplicationConfig.getUserService();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initializeComponents();
        setupRefreshTimer();
        loadPenyewaData();
    }
    
    private void setupRefreshTimer() {
        refreshTimer = new Timer(REFRESH_INTERVAL, e -> loadPenyewaData());
        refreshTimer.start();
    }
    
    public void stopTimer() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }
    
    private void initializeComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Manajemen Penyewa");
        titleLabel.setFont(TITLE_FONT);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        addButton = createStyledButton("Tambah", new Color(40, 167, 69));
        editButton = createStyledButton("Edit", PRIMARY_COLOR);
        deleteButton = createStyledButton("Hapus", new Color(220, 53, 69));
        viewButton = createStyledButton("Lihat Bukti", new Color(108, 117, 125));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Initialize table
        String[] columns = {
            "No Kamar", "Nama Penyewa", "Tanggal Mulai", 
            "Durasi", "Total", "Status", "Tanggal Bayar", "Bukti"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        penyewaTable = new JTable(tableModel);
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(penyewaTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        addButton.addActionListener(e -> showPenyewaDialog(null));
        editButton.addActionListener(e -> editSelectedPenyewa());
        deleteButton.addActionListener(e -> deleteSelectedPenyewa());
        viewButton.addActionListener(e -> viewBuktiPembayaran());
    }

    private class UserComboItem {
        private final int id;
        private final String name;
        
        public UserComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    private void setupTable() {
        penyewaTable.setFont(REGULAR_FONT);
        penyewaTable.setRowHeight(25);
        penyewaTable.getTableHeader().setFont(REGULAR_FONT);
        penyewaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Setup status column renderer
        TableColumn statusColumn = penyewaTable.getColumnModel().getColumn(5);
        statusColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                switch (value.toString()) {
                    case "Dibayar" -> setForeground(new Color(40, 167, 69));
                    case "Menunggu" -> setForeground(new Color(255, 193, 7));
                    default -> setForeground(new Color(220, 53, 69));
                }
                
                return c;
            }
        });

        // Set preferred column widths
        penyewaTable.getColumnModel().getColumn(0).setPreferredWidth(80); // No Kamar
        penyewaTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama Penyewa
        penyewaTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Tanggal Mulai
        penyewaTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Durasi
        penyewaTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Total
        penyewaTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        penyewaTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Tanggal Bayar
        penyewaTable.getColumnModel().getColumn(7).setPreferredWidth(80); // Bukti
    }
    
    private void loadPenyewaData() {
        try {
            List<RentalView> rentals = rentalService.getAllRentalsView();
            tableModel.setRowCount(0);
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            for (RentalView rental : rentals) {
                String tanggalBayar = rental.getPaymentDate() != null ?
                    dateFormat.format(rental.getPaymentDate()) : "-";
                
                tableModel.addRow(new Object[]{
                    rental.getRoomNumber(),
                    rental.getTenantName(),
                    dateFormat.format(rental.getStartDate()),
                    rental.getDuration() + " bulan",
                    currencyFormat.format(rental.getTotalPayment()),
                    rental.getPaymentStatus(),
                    tanggalBayar,
                    rental.getPaymentProof() != null ? "Ada" : "Tidak Ada"
                });
            }
        } catch (Exception ex) {
            showError("Error loading data: " + ex.getMessage());
        }
    }
    
    private void showPenyewaDialog(RentalView rental) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            rental == null ? "Tambah Penyewa" : "Edit Penyewa", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Form components
        JComboBox<String> kamarCombo = new JComboBox<>();
        JComboBox<UserComboItem> penyewaCombo = new JComboBox<>();
        JSpinner durasiSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        JTextField totalField = new JTextField(20);
        totalField.setEditable(false);
        
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Menunggu", "Dibayar", "Dibatalkan"});
        JTextField tanggalBayarField = new JTextField(20);
        
        // Image upload components
        JTextField imagePathField = new JTextField(20);
        imagePathField.setEditable(false);
        JButton browseButton = createStyledButton("Browse", Color.GRAY);
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(imagePathField);
        imagePanel.add(browseButton);
        
        final String[] selectedImagePath = {null};
        
        try {
            // Load available rooms
            List<Room> availableRooms = roomService.getAllRooms();
            for (Room room : availableRooms) {
                if (rental == null && "Kosong".equals(room.getStatus())) {
                    kamarCombo.addItem(room.getNumber());
                } else if (rental != null) {
                    kamarCombo.addItem(room.getNumber());
                }
            }

            // Load tenants for combo box
            List<User> tenants = userService.getAllTenants();
            penyewaCombo.addItem(new UserComboItem(0, "Pilih Penyewa")); // Default item
            for (User tenant : tenants) {
                penyewaCombo.addItem(new UserComboItem(tenant.getId(), tenant.getUsername()));
            }
            
            // Calculate total when room or duration changes
            kamarCombo.addActionListener(e -> calculateTotal(kamarCombo, durasiSpinner, totalField));
            durasiSpinner.addChangeListener(e -> calculateTotal(kamarCombo, durasiSpinner, totalField));
            
            // Handle payment status changes
            statusCombo.addActionListener(e -> {
                boolean isPaid = "Dibayar".equals(statusCombo.getSelectedItem());
                tanggalBayarField.setEnabled(isPaid);
                imagePathField.setEnabled(isPaid);
                browseButton.setEnabled(isPaid);
                if (!isPaid) {
                    tanggalBayarField.setText("");
                    imagePathField.setText("");
                    selectedImagePath[0] = null;
                }
            });
            
            // Image upload
            browseButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter(
                    "Image Files", "jpg", "png", "gif", "jpeg"));
                
                if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imagePathField.setText(selectedFile.getPath());
                    selectedImagePath[0] = selectedFile.getPath();
                }
            });
            
            // Populate fields if editing
            if (rental != null) {
                kamarCombo.setSelectedItem(rental.getRoomNumber());
                durasiSpinner.setValue(rental.getDuration());
                statusCombo.setSelectedItem(rental.getPaymentStatus());
                if (rental.getPaymentDate() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    tanggalBayarField.setText(dateFormat.format(rental.getPaymentDate()));
                }
                if (rental.getPaymentProof() != null) {
                    imagePathField.setText(rental.getPaymentProof());
                    selectedImagePath[0] = rental.getPaymentProof();
                }

                // Select correct tenant in combo box
                String tenantName = rental.getTenantName();
                for (int i = 0; i < penyewaCombo.getItemCount(); i++) {
                    UserComboItem item = penyewaCombo.getItemAt(i);
                    if (item.toString().equals(tenantName)) {
                        penyewaCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // Add form components
            addFormRow(formPanel, gbc, "Nomor Kamar:", kamarCombo);
            addFormRow(formPanel, gbc, "Nama Penyewa:", penyewaCombo);
            addFormRow(formPanel, gbc, "Durasi Sewa (bulan):", durasiSpinner);
            addFormRow(formPanel, gbc, "Total Pembayaran:", totalField);
            addFormRow(formPanel, gbc, "Status Pembayaran:", statusCombo);
            addFormRow(formPanel, gbc, "Tanggal Pembayaran (dd/MM/yyyy):", tanggalBayarField);
            addFormRow(formPanel, gbc, "Bukti Pembayaran:", imagePanel);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton saveButton = createStyledButton("Simpan", new Color(40, 167, 69));
            JButton cancelButton = createStyledButton("Batal", new Color(220, 53, 69));
            
            saveButton.addActionListener(e -> {
                try {
                    // Validate penyewa selection
                    UserComboItem selectedUser = (UserComboItem) penyewaCombo.getSelectedItem();
                    if (selectedUser.getId() == 0) {
                        showWarning("Silakan pilih penyewa!");
                        return;
                    }
                    int userId = selectedUser.getId();
                    
                    if ("Dibayar".equals(statusCombo.getSelectedItem())) {
                        if (tanggalBayarField.getText().trim().isEmpty()) {
                            showWarning("Masukkan tanggal pembayaran");
                            return;
                        }
                        if (selectedImagePath[0] == null) {
                            showWarning("Pilih file bukti pembayaran");
                            return;
                        }
                    }
                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date startDate = new Date(); // Current date for new rentals
                    Date paymentDate = null;
                    if (!tanggalBayarField.getText().trim().isEmpty()) {
                        paymentDate = dateFormat.parse(tanggalBayarField.getText());
                    }
                    
                    double total = Double.parseDouble(totalField.getText()
                        .replace("Rp", "").replace(".", "")
                        .replace(",00", "").trim());
                    
                    if (rental == null) {
                        // Create new rental
                        rentalService.createRental(
                            kamarCombo.getSelectedItem().toString(),
                            userId,
                            (Integer) durasiSpinner.getValue(),
                            total,
                            (String) statusCombo.getSelectedItem(), // Pastikan nilai ini benar-benar diambil
                            startDate,
                            paymentDate,
                            selectedImagePath[0]
                        );
                    } else {
                        // Update existing rental
                        rentalService.updateRental(
                            rental.getRoomNumber(),
                            (Integer) durasiSpinner.getValue(),
                            total,
                            (String) statusCombo.getSelectedItem(),
                            paymentDate,
                            selectedImagePath[0]
                        );
                    }
                    
                    showInfo(rental == null ? "Data penyewa berhasil ditambahkan!" : 
                        "Data penyewa berhasil diupdate!");
                    loadPenyewaData();
                    KamarPanel.getInstance().notifyKamarStatusChanged();
                    dialog.dispose();
                    
                } catch (NumberFormatException ex) {
                    showError("Format angka tidak valid!");
                } catch (Exception ex) {
                    showError("Error saving data: " + ex.getMessage());
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            // Calculate initial total
            calculateTotal(kamarCombo, durasiSpinner, totalField);
            
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            showError("Error initializing form: " + ex.getMessage());
        }
    }
    
    private void calculateTotal(JComboBox<String> kamarCombo, JSpinner durasiSpinner, 
            JTextField totalField) {
        try {
            String selectedRoom = kamarCombo.getSelectedItem().toString();
            Room room = roomService.getRoomByNumber(selectedRoom);
            int duration = (Integer) durasiSpinner.getValue();
            double total = room.getPricePerMonth() * duration;
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            totalField.setText(currencyFormat.format(total));
        } catch (Exception ex) {
            showError("Error calculating total: " + ex.getMessage());
        }
    }
    
    private void editSelectedPenyewa() {
        int selectedRow = penyewaTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Pilih data penyewa yang akan diedit");
            return;
        }
        
        try {
            String roomNumber = tableModel.getValueAt(selectedRow, 0).toString();
            RentalView rental = rentalService.getRentalViewByRoomNumber(roomNumber);
            showPenyewaDialog(rental);
        } catch (Exception ex) {
            showError("Error loading rental data: " + ex.getMessage());
        }
    }
    
    private void deleteSelectedPenyewa() {
        int selectedRow = penyewaTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Pilih data penyewa yang akan dihapus");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data penyewa ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String roomNumber = tableModel.getValueAt(selectedRow, 0).toString();
                rentalService.cancelRental(roomNumber);
                showInfo("Data penyewa berhasil dihapus!");
                loadPenyewaData();
                KamarPanel.getInstance().notifyKamarStatusChanged();
            } catch (Exception ex) {
                showError("Error deleting data: " + ex.getMessage());
            }
        }
    }
    
    private void viewBuktiPembayaran() {
        int selectedRow = penyewaTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Pilih data penyewa terlebih dahulu");
            return;
        }
        
        if (!"Ada".equals(tableModel.getValueAt(selectedRow, 7))) {
            showWarning("Tidak ada bukti pembayaran untuk penyewa ini");
            return;
        }
        
        try {
            String roomNumber = tableModel.getValueAt(selectedRow, 0).toString();
            RentalView rental = rentalService.getRentalViewByRoomNumber(roomNumber);
            showImageDialog(rental.getPaymentProof());
        } catch (Exception ex) {
            showError("Error loading payment proof: " + ex.getMessage());
        }
    }
    
    private void showImageDialog(String imagePath) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this),
            "Bukti Pembayaran", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        
        // Initial scaled image
        int initialWidth = 400;
        Image scaledImage = originalImage.getScaledInstance(initialWidth, -1, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        
        // Zoom controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.WHITE);
        
        JButton zoomInButton = createStyledButton("+", PRIMARY_COLOR);
        JButton zoomOutButton = createStyledButton("-", PRIMARY_COLOR);
        JButton resetButton = createStyledButton("Reset", PRIMARY_COLOR);
        
        final int[] currentWidth = {initialWidth};
        
        zoomInButton.addActionListener(e -> {
            currentWidth[0] += 100;
            Image newImage = originalImage.getScaledInstance(currentWidth[0], -1, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(newImage));
            dialog.pack();
        });
        
        zoomOutButton.addActionListener(e -> {
            if (currentWidth[0] > 200) {
                currentWidth[0] -= 100;
                Image newImage = originalImage.getScaledInstance(currentWidth[0], -1, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(newImage));
                dialog.pack();
            }
        });
        
        resetButton.addActionListener(e -> {
            currentWidth[0] = initialWidth;
            Image newImage = originalImage.getScaledInstance(initialWidth, -1, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(newImage));
            dialog.pack();
        });
        
        controlPanel.add(zoomInButton);
        controlPanel.add(zoomOutButton);
        controlPanel.add(resetButton);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        
        JButton closeButton = createStyledButton("Tutup", PRIMARY_COLOR);
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(REGULAR_FONT);
        panel.add(labelComp, gbc);
        
        component.setFont(REGULAR_FONT);
        panel.add(component, gbc);
    }
    
    @Override
    public void removeNotify() {
        stopTimer();
        super.removeNotify();
    }
}