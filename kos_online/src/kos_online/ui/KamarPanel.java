package kos_online.ui;

import kos_online.models.Room;
import kos_online.services.RoomService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import javax.swing.Timer;

public class KamarPanel extends BasePanel {
    private final RoomService roomService;
    private JTable kamarTable;
    private DefaultTableModel tableModel;
    private JButton editButton, deleteButton;
    private Timer refreshTimer;
    private Map<String, String> lastKnownStatus = new HashMap<>();
    private static final int REFRESH_INTERVAL = 100; // 0.1 second for faster updates
    
    private static KamarPanel instance;
    
    public static KamarPanel getInstance() {
        return instance;
    }
    
    public void forceRefresh() {
        loadKamarData();
    }
    
    public KamarPanel(RoomService roomService) {
        this.roomService = roomService;
        instance = this;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initializeComponents();
        setupRefreshTimer();
        loadKamarData();
    }
    
    private void setupRefreshTimer() {
        refreshTimer = new Timer(REFRESH_INTERVAL, e -> checkForUpdates());
        refreshTimer.start();
    }
    
    public void stopTimer() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    public void notifyKamarStatusChanged() {
        SwingUtilities.invokeLater(this::loadKamarData);
    }
    
    private void initializeComponents() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Data Kamar");
        titleLabel.setFont(TITLE_FONT);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        editButton = createStyledButton("Edit", PRIMARY_COLOR);
        deleteButton = createStyledButton("Hapus", new Color(220, 53, 69));
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Table
        String[] columns = {"No Kamar", "Status", "Harga/Bulan", "Fasilitas"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        kamarTable = new JTable(tableModel);
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(kamarTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        editButton.addActionListener(e -> editSelectedKamar());
        deleteButton.addActionListener(e -> deleteSelectedKamar());
    }
    
    private void setupTable() {
        kamarTable.setFont(REGULAR_FONT);
        kamarTable.setRowHeight(25);
        kamarTable.getTableHeader().setFont(REGULAR_FONT);
        kamarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom renderer for status column
        TableColumn statusColumn = kamarTable.getColumnModel().getColumn(1);
        statusColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                if ("Terisi".equals(value)) {
                    setForeground(new Color(220, 53, 69));
                } else {
                    setForeground(new Color(40, 167, 69));
                }
                
                return c;
            }
        });

        kamarTable.setShowGrid(true);
        kamarTable.setGridColor(new Color(240, 240, 240));
        
        // Set column widths
        kamarTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        kamarTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        kamarTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        kamarTable.getColumnModel().getColumn(3).setPreferredWidth(300);
    }
    
    private void checkForUpdates() {
        try {
            List<Room> currentRooms = roomService.getAllRooms();
            boolean needsRefresh = false;
            
            for (Room room : currentRooms) {
                String previousStatus = lastKnownStatus.get(room.getNumber());
                if (previousStatus == null || !previousStatus.equals(room.getStatus())) {
                    needsRefresh = true;
                    break;
                }
            }
            
            if (needsRefresh) {
                SwingUtilities.invokeLater(this::loadKamarData);
            }
            
        } catch (Exception ex) {
            System.err.println("Error checking for updates: " + ex.getMessage());
        }
    }
    
    private void loadKamarData() {
        tableModel.setRowCount(0);
        lastKnownStatus.clear();
        
        try {
            List<Room> rooms = roomService.getAllRooms();
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            
            for (Room room : rooms) {
                lastKnownStatus.put(room.getNumber(), room.getStatus());
                
                tableModel.addRow(new Object[]{
                    room.getNumber(),
                    room.getStatus(),
                    currencyFormat.format(room.getPricePerMonth()),
                    room.getFacilities()
                });
            }
        } catch (Exception ex) {
            showError("Error loading data: " + ex.getMessage());
        }
    }
    
    private void editSelectedKamar() {
        int selectedRow = kamarTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Pilih kamar yang akan diedit");
            return;
        }
        
        String status = tableModel.getValueAt(selectedRow, 1).toString();
        if ("Terisi".equals(status)) {
            showWarning("Tidak dapat mengedit kamar yang sedang terisi!");
            return;
        }
        
        Room room = new Room();
        room.setNumber(tableModel.getValueAt(selectedRow, 0).toString());
        room.setStatus(status);
        String priceStr = tableModel.getValueAt(selectedRow, 2).toString()
            .replace("Rp", "").replace(".", "").replace(",00", "").trim();
        room.setPricePerMonth(Double.parseDouble(priceStr));
        room.setFacilities(tableModel.getValueAt(selectedRow, 3).toString());
        
        showEditDialog(room);
    }
    
    private void showEditDialog(Room room) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            "Edit Kamar", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JLabel nomorLabel = new JLabel("Nomor Kamar:");
        nomorLabel.setFont(REGULAR_FONT);
        JTextField nomorField = new JTextField(20);
        nomorField.setText(room.getNumber());
        nomorField.setEditable(false);
        nomorField.setBackground(new Color(240, 240, 240));
        
        JLabel hargaLabel = new JLabel("Harga per Bulan:");
        hargaLabel.setFont(REGULAR_FONT);
        JTextField hargaField = new JTextField(20);
        hargaField.setText(String.valueOf(room.getPricePerMonth()));
        
        JLabel fasilitasLabel = new JLabel("Fasilitas:");
        fasilitasLabel.setFont(REGULAR_FONT);
        JTextArea fasilitasArea = new JTextArea(4, 20);
        fasilitasArea.setFont(REGULAR_FONT);
        fasilitasArea.setLineWrap(true);
        fasilitasArea.setWrapStyleWord(true);
        fasilitasArea.setText(room.getFacilities());
        
        JScrollPane fasilitasScroll = new JScrollPane(fasilitasArea);
        fasilitasScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nomorLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nomorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(hargaLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(hargaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(fasilitasLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(fasilitasScroll, gbc);
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = createStyledButton("Simpan", new Color(40, 167, 69));
        JButton cancelButton = createStyledButton("Batal", new Color(220, 53, 69));
        
        saveButton.addActionListener(e -> {
            try {
                if (hargaField.getText().trim().isEmpty() || 
                    fasilitasArea.getText().trim().isEmpty()) {
                    showWarning("Semua field harus diisi!");
                    return;
                }
                
                double harga = Double.parseDouble(hargaField.getText().trim());
                room.setPricePerMonth(harga);
                room.setFacilities(fasilitasArea.getText().trim());
                
                roomService.updateRoom(room);
                showInfo("Kamar berhasil diupdate!");
                loadKamarData();
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                showWarning("Harga harus berupa angka!");
            } catch (Exception ex) {
                showError("Error updating room: " + ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedKamar() {
        int selectedRow = kamarTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Pilih kamar yang akan dihapus");
            return;
        }
        
        String status = tableModel.getValueAt(selectedRow, 1).toString();
        if ("Terisi".equals(status)) {
            showWarning("Tidak dapat menghapus kamar yang sedang terisi!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus kamar ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String roomNumber = tableModel.getValueAt(selectedRow, 0).toString();
                roomService.deleteRoom(roomNumber);
                showInfo("Kamar berhasil dihapus!");
                loadKamarData();
            } catch (Exception ex) {
                showError("Error deleting room: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public void removeNotify() {
        stopTimer();
        super.removeNotify();
    }
}