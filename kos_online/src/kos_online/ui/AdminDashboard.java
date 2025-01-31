package kos_online.ui;

import kos_online.models.User;
import kos_online.services.RoomService;
import kos_online.services.RentalService;
import kos_online.config.ApplicationConfig;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(64, 123, 255);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    private JPanel sidePanel;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private User currentUser;
    private KamarPanel kamarPanel;
    private PenyewaPanelPemilik penyewaPanel;
    private JButton activeButton;
    
    private final RoomService roomService;
    private final RentalService rentalService;
    
    public AdminDashboard(User user, RoomService roomService, RentalService rentalService) {
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
        kamarPanel = new KamarPanel(roomService);
        penyewaPanel = new PenyewaPanelPemilik(rentalService, roomService);
        
        setTitle("Admin Panel - Piilp Kost");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        // Removed setResizable(false) to allow maximization
        
        getContentPane().setBackground(new Color(248, 249, 250));
        getContentPane().setLayout(new BorderLayout());
        
        createSidePanel();
        createMainPanel();
        
        showPanel("kamar");
    }
    
    private void createSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setPreferredSize(new Dimension(220, getHeight()));
        sidePanel.setMinimumSize(new Dimension(220, getHeight())); // Added minimum size
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));
        
        // Logo Panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Modified for resizing
        logoPanel.setPreferredSize(new Dimension(220, 60));
        
        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        logoPanel.add(titleLabel);
        
        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Menu Buttons
        JButton kamarBtn = createMenuButton("Manajemen Kamar", "kamar", true);
        JButton penyewaBtn = createMenuButton("Data Penyewa", "penyewa", false);
        JButton logoutBtn = createMenuButton("Logout", "logout", false);
        
        menuPanel.add(kamarBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        menuPanel.add(penyewaBtn);
        
        // Logout Panel
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
        
        // Wrap sidePanel in a container to maintain its width
        JPanel sidePanelContainer = new JPanel(new BorderLayout());
        sidePanelContainer.add(sidePanel, BorderLayout.CENTER);
        add(sidePanelContainer, BorderLayout.WEST);
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
        
        // Use JScrollPane to handle content when window is smaller than content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JButton createMenuButton(String text, String command, boolean isDefault) {
        JButton button = new JButton(text);
        button.setFont(MENU_FONT);
        button.setForeground(new Color(60, 60, 60));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Modified for resizing
        button.setPreferredSize(new Dimension(220, 40));
        
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        if (isDefault) {
            activeButton = button;
            button.setFont(MENU_FONT.deriveFont(Font.BOLD));
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
                activeButton.setFont(MENU_FONT);
                activeButton.setForeground(new Color(60, 60, 60));
                activeButton.setContentAreaFilled(false);
            }
            activeButton = button;
            button.setFont(MENU_FONT.deriveFont(Font.BOLD));
            button.setForeground(Color.BLACK);
            handleMenuClick(command);
        });
        
        return button;
    }
    
    // Rest of the methods remain the same
    private void handleMenuClick(String command) {
        switch (command) {
            case "kamar" -> showPanel("kamar");
            case "penyewa" -> showPanel("penyewa");
            case "logout" -> handleLogout();
        }
    }
    
    private void showPanel(String panelName) {
        contentPanel.removeAll();
        
        switch (panelName) {
            case "kamar" -> contentPanel.add(kamarPanel);
            case "penyewa" -> contentPanel.add(penyewaPanel);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void refreshPanels() {
        kamarPanel = new KamarPanel(roomService);
        penyewaPanel = new PenyewaPanelPemilik(rentalService, roomService);
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Cleanup resources
            if (kamarPanel != null) kamarPanel.stopTimer();
            if (penyewaPanel != null) penyewaPanel.stopTimer();
            ApplicationConfig.cleanup();
            
            // Show login form
            new SignIn(null).setVisible(true);
            this.dispose();
        }
    }

    private void handleClosing() {
        // Cleanup resources when window is closed
        if (kamarPanel != null) kamarPanel.stopTimer();
        if (penyewaPanel != null) penyewaPanel.stopTimer();
        ApplicationConfig.cleanup();
        System.exit(0);
    }
}