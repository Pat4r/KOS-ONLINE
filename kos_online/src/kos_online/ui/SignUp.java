package kos_online.ui;

import kos_online.services.AuthenticationService;
import kos_online.models.User;
import javax.swing.*;
import java.awt.*;

public class SignUp extends JFrame {
    private final AuthenticationService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField phoneField;
    private JCheckBox termsCheckBox;
    private final int FORM_WIDTH = 400;
    private final int FORM_HEIGHT = 450;
    
    public SignUp(AuthenticationService authService) {
        this.authService = authService;
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
        
        // Center panel with signup form
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setPreferredSize(new Dimension(FORM_WIDTH, FORM_HEIGHT));
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Title
        JLabel titleLabel = new JLabel("CREATE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(160, 30, 150, 30);
        
        // Form fields
        JLabel usernameLabel = new JLabel("Username :");
        usernameLabel.setBounds(50, 80, 100, 25);
        
        usernameField = new JTextField();
        usernameField.setBounds(50, 110, 300, 30);
        
        JLabel passwordLabel = new JLabel("Password :");
        passwordLabel.setBounds(50, 150, 100, 25);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 180, 300, 30);
        
        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setBounds(50, 220, 100, 25);
        
        emailField = new JTextField();
        emailField.setBounds(50, 250, 300, 30);
        
        JLabel phoneLabel = new JLabel("Nomer Handphone :");
        phoneLabel.setBounds(50, 290, 150, 25);
        
        phoneField = new JTextField();
        phoneField.setBounds(50, 320, 300, 30);
        
        termsCheckBox = new JCheckBox("Already read term and accept it");
        termsCheckBox.setBounds(50, 360, 300, 25);
        
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(160, 400, 80, 30);
        submitButton.addActionListener(e -> handleSignUp());
        
        JButton signinLink = new JButton("Sign in");
        signinLink.setBounds(240, 400, 80, 25);
        signinLink.setBorderPainted(false);
        signinLink.setContentAreaFilled(false);
        signinLink.setForeground(Color.BLUE);
        signinLink.addActionListener(e -> openSignIn());
        
        // Add components to center panel
        centerPanel.add(titleLabel);
        centerPanel.add(usernameLabel);
        centerPanel.add(usernameField);
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(emailLabel);
        centerPanel.add(emailField);
        centerPanel.add(phoneLabel);
        centerPanel.add(phoneField);
        centerPanel.add(termsCheckBox);
        centerPanel.add(submitButton);
        centerPanel.add(signinLink);
        
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
    
    private void handleSignUp() {
        try {
            if (!termsCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please accept the terms and conditions!");
                return;
            }
            
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            User newUser = new User(username, password, email, phone, "Pencari Kos");
            authService.register(newUser);
            
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            openSignIn();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void openSignIn() {
        new SignIn(authService).setVisible(true);
        this.dispose();
    }
}