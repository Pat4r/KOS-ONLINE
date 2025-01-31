package kos_online.ui;

import kos_online.config.ApplicationConfig;
import kos_online.services.AuthenticationService;
import kos_online.models.User;
import javax.swing.*;
import java.awt.*;

public class SignIn extends JFrame {
    private final AuthenticationService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final int FORM_WIDTH = 400;
    private final int FORM_HEIGHT = 450;
    
    public SignIn(AuthenticationService authService) {
        ApplicationConfig.initialize();
        this.authService = authService != null ? authService : 
            ApplicationConfig.getAuthenticationService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Piilp Kost");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true); // Allow window to be resized
        
        // Main container with GridBagLayout for center alignment
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(Color.WHITE);
        
        // Center panel with login form
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setPreferredSize(new Dimension(FORM_WIDTH, FORM_HEIGHT));
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Title
        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(160, 20, 100, 30);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Logo panel
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.LIGHT_GRAY);
        imagePanel.setBounds(150, 70, 100, 100);
        
        // Username fields
        JLabel usernameLabel = new JLabel("Username :");
        usernameLabel.setBounds(50, 200, 300, 25);
        
        usernameField = new JTextField();
        usernameField.setBounds(50, 230, 300, 30);
        
        // Password fields
        JLabel passwordLabel = new JLabel("Password :");
        passwordLabel.setBounds(50, 270, 300, 25);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 300, 300, 30);
        
        // Login button
        JButton loginButton = new JButton("Go In");
        loginButton.setBounds(165, 350, 70, 25);
        loginButton.setBackground(new Color(240, 240, 240));
        loginButton.addActionListener(e -> handleLogin());
        
        // Bottom panel with sign up link
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        bottomPanel.setBounds(50, 390, 300, 30);
        bottomPanel.setOpaque(false);
        
        JLabel noAccountLabel = new JLabel("Not have account?");
        bottomPanel.add(noAccountLabel);
        
        JButton signupLink = new JButton("Sign up");
        signupLink.setBorderPainted(false);
        signupLink.setContentAreaFilled(false);
        signupLink.setForeground(Color.BLUE);
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addActionListener(e -> openSignUp());
        bottomPanel.add(signupLink);
        
        // Add components to center panel
        centerPanel.add(titleLabel);
        centerPanel.add(imagePanel);
        centerPanel.add(usernameLabel);
        centerPanel.add(usernameField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(loginButton);
        centerPanel.add(bottomPanel);
        
        // Add centerPanel to mainContainer with GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainContainer.add(centerPanel, gbc);
        
        // Add mainContainer to frame
        add(mainContainer);
    }
    
    private void handleLogin() {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            User user = authService.authenticate(username, password);
            
            if (user != null) {
                if (user.getUserType().equals("Pemilik Kos")) {
                    new AdminDashboard(user, 
                        ApplicationConfig.getRoomService(),
                        ApplicationConfig.getRentalService()
                    ).setVisible(true);
                } else {
                    new Dashboard(user, 
                        ApplicationConfig.getRoomService(),
                        ApplicationConfig.getRentalService()
                    ).setVisible(true);
                }
                this.dispose();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void openSignUp() {
        new SignUp(ApplicationConfig.getAuthenticationService()).setVisible(true);
        this.dispose();
    }
}