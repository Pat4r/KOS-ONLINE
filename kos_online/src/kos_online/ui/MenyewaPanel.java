package kos_online.ui;

import kos_online.models.Room;
import kos_online.models.User;
import kos_online.services.RoomService;
import kos_online.services.RentalService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import javax.swing.border.EmptyBorder;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import kos_online.models.Rental;

public class MenyewaPanel extends BasePanel {
    private final RoomService roomService;
    private final RentalService rentalService;
    private final User currentUser;
    
    private JComboBox<String> kostCombo;
    private JComboBox<String> durationCombo;
    private JSpinner dateSpinner;
    private JCheckBox peraturanCheck;
    private List<Room> roomList;

    public MenyewaPanel(User user, RoomService roomService, RentalService rentalService) {
        this.currentUser = user;
        this.roomService = roomService;
        this.rentalService = rentalService;
        
        setLayout(null);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        try {
            loadData();
            initializeComponents();
            createKamarGrid();
            createFormSection();
        } catch (Exception ex) {
            showError("Error initializing panel: " + ex.getMessage());
        }
    }
    
    private void loadData() throws Exception {
        roomList = roomService.getAllRooms();
    }
    
    private void initializeComponents() {
        JLabel infoLabel = new JLabel("Info Kamar Kostan");
        infoLabel.setFont(TITLE_FONT);
        infoLabel.setForeground(TEXT_COLOR);
        infoLabel.setBounds(0, 0, 200, 25);
        add(infoLabel);
        
        String[] durationOptions = {"1 bulan", "3 bulan", "6 bulan", "12 bulan"};
        durationCombo = new JComboBox<>(durationOptions);
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateModel.setStart(new java.util.Date());
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        dateSpinner.setValue(new java.util.Date());
        
        peraturanCheck = new JCheckBox("Setuju dengan peraturan dan ketentuan");
        peraturanCheck.setFont(REGULAR_FONT);
        peraturanCheck.setBackground(Color.WHITE);
    }
    
    private void createKamarGrid() {
        int x = 0;
        int y = 40;
        
        for (int i = 0; i < roomList.size(); i++) {
            if (i > 0 && i % 4 == 0) {
                x = 0;
                y += 70;
            }
            
            Room room = roomList.get(i);
            RoomPanel kamarPanel = new RoomPanel(room);
            kamarPanel.setBounds(x, y, 200, 60);
            add(kamarPanel);
            
            x += 210;
        }
    }

    private void createFormSection() {
        int startY = 200;
        
        JLabel kostLabel = new JLabel("Pilih kost:");
        kostLabel.setFont(REGULAR_FONT);
        kostLabel.setBounds(0, startY, 100, 25);
        add(kostLabel);
        
        ArrayList<String> availableRooms = new ArrayList<>();
        availableRooms.add("Pilih Kamar");
        for (Room room : roomList) {
            if ("Kosong".equals(room.getStatus())) {
                availableRooms.add("Kamar " + room.getNumber());
            }
        }
        
        kostCombo = new JComboBox<>(availableRooms.toArray(new String[0]));
        kostCombo.setFont(REGULAR_FONT);
        kostCombo.setBounds(0, startY + 30, 300, 25);
        add(kostCombo);
        
        addFormComponent("Pilih lama tinggal:", durationCombo, startY + 70);
        addFormComponent("Waktu mulai tinggal:", dateSpinner, startY + 140);
        
        peraturanCheck.setBounds(0, startY + 210, 300, 25);
        add(peraturanCheck);
        
        JButton submitBtn = createStyledButton("Submit", PRIMARY_COLOR);
        submitBtn.setBounds(230, startY + 250, 80, 35);
        submitBtn.addActionListener(e -> handleSubmit());
        add(submitBtn);
    }
    
    private boolean validateForm() {
        if (kostCombo.getSelectedIndex() == 0) {
            showError("Silakan pilih kamar");
            return false;
        }
        if (!peraturanCheck.isSelected()) {
            showError("Silakan setuju dengan peraturan dan ketentuan");
            return false;
        }
        return true;
    }
    
    private void handleSubmit() {
        if (!validateForm()) {
            return;
        }

        try {
            String selectedRoom = kostCombo.getSelectedItem().toString();
            String roomNumber = selectedRoom.substring(selectedRoom.indexOf("Kamar ") + 6);
            int months = Integer.parseInt(durationCombo.getSelectedItem().toString().split(" ")[0]);
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

            // Ambil informasi kamar
            Room room = roomService.getRoomByNumber(roomNumber);
            double total = room.getPricePerMonth() * months;

            // Buat objek Rental baru
            Rental rental = new Rental(
                currentUser.getId(),
                room.getId(), 
                sqlDate,
                months,
                total
            );

            // Panggil createRental dengan parameter yang benar
            rentalService.createRental(rental, roomNumber);

            showPembayaranPanel(roomNumber, total);

        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    private void showPembayaranPanel(String nomorKamar, double total) {
        Container parent = getParent();
        parent.removeAll();
        PembayaranPanel pembayaranPanel = new PembayaranPanel(
            rentalService,
            currentUser.getId(),
            nomorKamar,
            total
        );
        parent.add(pembayaranPanel);
        parent.revalidate();
        parent.repaint();
    }
    
    private void addFormComponent(String label, JComponent component, int y) {
        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(REGULAR_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(0, y, 150, 25);
        add(titleLabel);
        
        component.setBounds(0, y + 30, 300, 30);
        add(component);
    }
    
    private class RoomPanel extends JPanel {
        private boolean isHovered = false;
        private final Room room;
        
        public RoomPanel(Room room) {
            this.room = room;
            setLayout(null);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            
            setupMouseListeners();
        }
        
        private void setupMouseListeners() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showFasilitasDialog();
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(isHovered ? HOVER_COLOR : Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            
            g2.setColor(isHovered ? PRIMARY_COLOR : Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            
            g2.setFont(TITLE_FONT);
            g2.setColor(TEXT_COLOR);
            String roomText = "Kamar " + room.getNumber();
            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(roomText)) / 2;
            g2.drawString(roomText, textX, 25);
            
            g2.setFont(REGULAR_FONT);
            g2.setColor(room.getStatus().equals("Terisi") ? 
                new Color(255, 59, 48) : new Color(52, 199, 89));
            textX = (getWidth() - fm.stringWidth(room.getStatus())) / 2;
            g2.drawString(room.getStatus(), textX, 45);
            
            g2.dispose();
        }
        
        private void showFasilitasDialog() {
            JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
                "Fasilitas Kamar " + room.getNumber(), true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(350, room.getStatus().equals("Kosong") ? 400 : 350);
            dialog.setLocationRelativeTo(this);
            
            JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(Color.WHITE);
            
            // Header with room details
            JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            headerPanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Fasilitas Kamar " + room.getNumber());
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setForeground(PRIMARY_COLOR);
            
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            JLabel priceLabel = new JLabel(formatter.format(room.getPricePerMonth()) + " / bulan");
            priceLabel.setFont(REGULAR_FONT);
            
            headerPanel.add(titleLabel);
            headerPanel.add(priceLabel);
            contentPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Facilities description
            JTextArea fasilitasArea = new JTextArea(room.getFacilities());
            fasilitasArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fasilitasArea.setEditable(false);
            fasilitasArea.setBackground(Color.WHITE);
            fasilitasArea.setMargin(new Insets(10, 0, 10, 10));
            contentPanel.add(fasilitasArea, BorderLayout.CENTER);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            
            if (room.getStatus().equals("Kosong")) {
                JButton pilihButton = createStyledButton("Pilih Kamar", PRIMARY_COLOR);
                pilihButton.setPreferredSize(new Dimension(100, 35));
                pilihButton.addActionListener(e -> {
                    kostCombo.setSelectedItem("Kamar " + room.getNumber());
                    dialog.dispose();
                });
                buttonPanel.add(pilihButton);
            }
            
            JButton closeButton = createStyledButton("Tutup", 
                room.getStatus().equals("Kosong") ? Color.GRAY : PRIMARY_COLOR);
            closeButton.setPreferredSize(new Dimension(100, 35));
            closeButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeButton);
            
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
            dialog.add(contentPanel);
            dialog.setVisible(true);
        }
    }

    public void stopTimer() {
    }
}