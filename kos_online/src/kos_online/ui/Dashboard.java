package kos_online.ui;

import kos_online.models.User;
import kos_online.services.RoomService;
import kos_online.services.RentalService;
import kos_online.config.ApplicationConfig;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {
    private JPanel sidePanel;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private User currentUser;
    private BerandaPanel berandaPanel;
    private MenyewaPanel menyewaPanel;
    private PembayaranPanel pembayaranPanel;
    private JButton activeButton;
    
    private final RoomService roomService;
    private final RentalService rentalService;
    private final int MIN_WIDTH = 1280;
    private final int MIN_HEIGHT = 720;
    
    public Dashboard(User user, RoomService roomService, RentalService rentalService) {
        this.currentUser = user;
        this.roomService = roomService;
        this.rentalService = rentalService;
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleClosing();
            }
        });
    }
    
    private void initComponents() {
        berandaPanel = new BerandaPanel(roomService, currentUser);
        menyewaPanel = new MenyewaPanel(currentUser, roomService, rentalService);
        pembayaranPanel = new PembayaranPanel(rentalService, currentUser.getId(), null, 0);
        
        setTitle("Piilp Kost");
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setPreferredSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Use BorderLayout for the main frame
        setLayout(new BorderLayout());
        
        getContentPane().setBackground(new Color(248, 249, 250));
        
        createSidePanel();
        createMainPanel();
        
        showPanel("beranda");
        
        // Pack the frame to ensure proper initial layout
        pack();
    }
    
    private void createSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setPreferredSize(new Dimension(220, getHeight()));
        sidePanel.setMinimumSize(new Dimension(220, getHeight()));
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setMaximumSize(new Dimension(220, 60));
        logoPanel.setPreferredSize(new Dimension(220, 60));

        // Ubah text label
        JLabel titleLabel = new JLabel("Halooo " + currentUser.getUsername());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 5, 0, 0));
        logoPanel.add(titleLabel);
        
        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Menu buttons
        JButton berandaBtn = createMenuButton("Beranda", "beranda", true);
        JButton menyewaBtn = createMenuButton("Menyewa kost", "menyewa", false);
        JButton pembayaranBtn = createMenuButton("Pembayaran", "pembayaran", false);
        JButton logoutBtn = createMenuButton("Logout", "logout", false);
        
        menuPanel.add(berandaBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(menyewaBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(pembayaranBtn);
        
        // Logout panel with glue for spacing
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
        logoutPanel.setBackground(Color.WHITE);
        logoutPanel.add(Box.createVerticalGlue());
        logoutPanel.add(logoutBtn);
        logoutPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        sidePanel.add(logoPanel);
        sidePanel.add(menuPanel);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(logoutPanel);
        
        add(sidePanel, BorderLayout.WEST);
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
        
        // Create a scroll pane for the content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Wrap content panel in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JButton createMenuButton(String text, String command, boolean isDefault) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(60, 60, 60));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 40));
        button.setPreferredSize(new Dimension(220, 40));
        
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        if (isDefault) {
            activeButton = button;
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setForeground(Color.BLACK);
        }
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setContentAreaFilled(true);
                    button.setBackground(new Color(245, 245, 245));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setContentAreaFilled(false);
                }
            }
        });
        
        button.addActionListener(e -> {
            if (activeButton != null) {
                activeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                activeButton.setForeground(new Color(60, 60, 60));
                activeButton.setContentAreaFilled(false);
            }
            activeButton = button;
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setForeground(Color.BLACK);
            handleMenuClick(command);
        });
        
        return button;
    }
    
    // ... [Rest of the methods remain the same: handleMenuClick, refreshPanels, showPanel, handleLogout, handleClosing]
    private void handleMenuClick(String command) {
        switch (command) {
            case "beranda" -> showPanel("beranda");
            case "menyewa" -> showPanel("menyewa");
            case "pembayaran" -> showPanel("pembayaran");
            case "logout" -> handleLogout();
        }
    }
    
    public void refreshPanels() {
        berandaPanel = new BerandaPanel(roomService, currentUser);
        menyewaPanel = new MenyewaPanel(currentUser, roomService, rentalService);
        pembayaranPanel = new PembayaranPanel(rentalService, currentUser.getId(), null, 0);
    }
    
    public void showPanel(String panelName) {
        contentPanel.removeAll();
        
        switch (panelName) {
            case "beranda" -> contentPanel.add(berandaPanel);
            case "menyewa" -> contentPanel.add(menyewaPanel);
            case "pembayaran" -> contentPanel.add(pembayaranPanel);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (berandaPanel != null) berandaPanel.stopTimer();
            if (menyewaPanel != null) menyewaPanel.stopTimer();
            if (pembayaranPanel != null) pembayaranPanel.stopTimer();
            
            ApplicationConfig.cleanup();
            
            new SignIn(null).setVisible(true);
            this.dispose();
        }
    }
    
    private void handleClosing() {
        if (berandaPanel != null) berandaPanel.stopTimer();
        if (menyewaPanel != null) menyewaPanel.stopTimer();
        if (pembayaranPanel != null) pembayaranPanel.stopTimer();
        ApplicationConfig.cleanup();
        System.exit(0);
    }
}