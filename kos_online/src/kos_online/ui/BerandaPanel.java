package kos_online.ui;

import kos_online.models.Room;
import kos_online.models.User;
import kos_online.services.RoomService;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BerandaPanel extends BasePanel {
    private final RoomService roomService;
    private List<Room> roomList;
    private int totalEmptyRooms = 0;
    private int totalOccupiedRooms = 0;
    private final User currentUser;
    private JPanel contentPanel;
    private static final int CONTENT_WIDTH = 850;
    private static final int STATS_CARD_WIDTH = 200;
    private static final int STATS_CARD_HEIGHT = 100;
    private static final int ROOM_CARD_WIDTH = 200;
    private static final int ROOM_CARD_HEIGHT = 60;
    private static final int HORIZONTAL_GAP = 15;
    private static final int VERTICAL_GAP = 20;

    public BerandaPanel(RoomService roomService, User user) {
        this.roomService = roomService;
        this.currentUser = user;
        setLayout(null);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        try {
            loadData();
            initializeComponents();
        } catch (Exception ex) {
            showError("Error initializing panel: " + ex.getMessage());
        }
    }
    
    private void loadData() throws Exception {
        roomList = roomService.getAllRooms();
        countRooms();
    }

    private void countRooms() {
        totalEmptyRooms = 0;
        totalOccupiedRooms = 0;
        for (Room room : roomList) {
            if ("Kosong".equals(room.getStatus())) {
                totalEmptyRooms++;
            } else {
                totalOccupiedRooms++;
            }
        }
    }
    
    private void initializeComponents() {
        // Welcome section at top right
        JLabel welcomeLabel = new JLabel("Halo, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setBounds(getWidth() - 200, 20, 180, 30);
        add(welcomeLabel);

        // Main content panel
        contentPanel = new JPanel(null);
        contentPanel.setBackground(Color.WHITE);
        
        // Stats cards
        JPanel statsCard1 = createStatsCard("Kamar Kosong", totalEmptyRooms, new Color(52, 199, 89));
        JPanel statsCard2 = createStatsCard("Jumlah Penghuni", totalOccupiedRooms, PRIMARY_COLOR);
        
        statsCard1.setBounds(0, 0, STATS_CARD_WIDTH, STATS_CARD_HEIGHT);
        statsCard2.setBounds(STATS_CARD_WIDTH + HORIZONTAL_GAP, 0, STATS_CARD_WIDTH, STATS_CARD_HEIGHT);
        
        contentPanel.add(statsCard1);
        contentPanel.add(statsCard2);

        // Info Kamar Kostan label
        JLabel infoLabel = new JLabel("Info Kamar Kostan");
        infoLabel.setFont(TITLE_FONT);
        infoLabel.setForeground(TEXT_COLOR);
        infoLabel.setBounds(0, STATS_CARD_HEIGHT + VERTICAL_GAP, CONTENT_WIDTH, 30);
        contentPanel.add(infoLabel);

        // Room grid
        createRoomGrid(STATS_CARD_HEIGHT + VERTICAL_GAP + 40);

        // Info buttons
        addInfoButtons(STATS_CARD_HEIGHT + VERTICAL_GAP + 250);

        // Set content panel bounds and add to main panel
        contentPanel.setBounds(20, 20, CONTENT_WIDTH, 600);
        add(contentPanel);
    }

    private void createRoomGrid(int startY) {
        int x = 0;
        int y = startY;
        int roomsPerRow = 4;
        
        for (int i = 0; i < roomList.size(); i++) {
            if (i > 0 && i % roomsPerRow == 0) {
                x = 0;
                y += ROOM_CARD_HEIGHT + VERTICAL_GAP;
            }
            
            Room room = roomList.get(i);
            RoomPanel roomPanel = new RoomPanel(room);
            roomPanel.setBounds(x, y, ROOM_CARD_WIDTH, ROOM_CARD_HEIGHT);
            contentPanel.add(roomPanel);
            
            x += ROOM_CARD_WIDTH + HORIZONTAL_GAP;
        }
    }

    private void addInfoButtons(int startY) {
        addInfoButton("Lokasi kostan", """
            Nama Kosan: Kosan Berkah
            Alamat: Jl. Kenangan No. 123
            Kota: Malang
            Kode Pos: 65145
            
            Landmark: 
            - 500m dari LA
            - Dekat Alfamart dan Indomaret
            - 5 menit ke halte bus
            
            Area sekitar:
            - Lingkungan aman dan tenang
            - Dekat pusat kuliner
            - Akses 24 jam
            """, startY);
            
        addInfoButton("Informasi ibu kos", """
            Nama: Ibu Sarah Wijaya
            No. HP: 0812-3456-7890
            WhatsApp: 0812-3456-7890
            Email: sarah.wijaya@email.com
            
            Jam bertemu:
            - Senin-Jumat: 08.00 - 20.00
            - Sabtu-Minggu: 09.00 - 18.00
            
            Catatan:
            - Hubungi terlebih dahulu sebelum berkunjung
            - Tersedia tour kosan dengan perjanjian
            """, startY + 40);
    }
    
    private JPanel createStatsCard(String title, int value, Color accentColor) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2.setColor(new Color(245, 247, 250));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));

                // Accent line
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(15, 15, 15, getHeight() - 15);

                g2.dispose();
            }
        };
        card.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(30, 15, 150, 20);
        card.add(titleLabel);

        // Value
        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        valueLabel.setBounds(30, 40, 100, 30);
        card.add(valueLabel);

        return card;
    }
    
    private void addInfoButton(String title, String content, int y) {
        JButton infoButton = new JButton();
        infoButton.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoButton.setBackground(Color.WHITE);
        infoButton.setBorderPainted(false);
        infoButton.setFocusPainted(false);
        
        JLabel iconLabel = createInfoIcon();
        JLabel textLabel = new JLabel(title);
        textLabel.setFont(REGULAR_FONT);
        textLabel.setForeground(TEXT_COLOR);
        
        infoButton.add(iconLabel);
        infoButton.add(textLabel);
        
        infoButton.setBounds(0, y, 440, 25);
        infoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        infoButton.addActionListener(e -> showInfoDialog(title, content));
        
        contentPanel.add(infoButton);
    }
    
    private JLabel createInfoIcon() {
        return new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(66, 133, 244)); // Blue color for info icon
                g2.fillOval(0, 0, 16, 16);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString("i", 6, 12);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(16, 16);
            }
        };
    }

    private class RoomPanel extends JPanel {
        private boolean isHovered = false;
        private final Room room;
        
        public RoomPanel(Room room) {
            this.room = room;
            setLayout(null);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            
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
            
            // Background
            g2.setColor(isHovered ? new Color(245, 247, 250) : Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
            
            // Border
            g2.setColor(Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke(1.0f));
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
            
            // Room number
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.setColor(Color.BLACK);
            String roomText = "Kamar " + room.getNumber();
            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(roomText)) / 2;
            g2.drawString(roomText, textX, 25);
            
            // Status
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.setColor(room.getStatus().equals("Terisi") ? 
                new Color(255, 59, 48) : new Color(52, 199, 89));
            String status = room.getStatus();
            textX = (getWidth() - fm.stringWidth(status)) / 2;
            g2.drawString(status, textX, 45);
            
            g2.dispose();
        }
        
        private void showFasilitasDialog() {
            JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
                "Fasilitas Kamar " + room.getNumber(), true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(this);
            
            JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(Color.WHITE);
            
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
            
            JTextArea fasilitasArea = new JTextArea(room.getFacilities());
            fasilitasArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fasilitasArea.setEditable(false);
            fasilitasArea.setBackground(Color.WHITE);
            fasilitasArea.setMargin(new Insets(10, 0, 10, 10));
            
            JScrollPane scrollPane = new JScrollPane(fasilitasArea);
            scrollPane.setBorder(null);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton closeButton = createStyledButton("Tutup", PRIMARY_COLOR);
            closeButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeButton);
            
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
            dialog.add(contentPanel);
            dialog.setVisible(true);
        }
    }
    
    private void showInfoDialog(String title, String content) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Dialog Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(PRIMARY_COLOR);
                g2.fillOval(0, 0, 24, 24);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2.drawString("i", 10, 17);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(24, 24);
            }
        };
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Dialog Content
        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setMargin(new Insets(10, 0, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Dialog Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = createStyledButton("Tutup", PRIMARY_COLOR);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    protected JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(80, 30));
        
        return button;
    }

    public void stopTimer() {
        // Implementation for timer if needed
    }
}