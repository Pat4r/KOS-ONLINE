package kos_online.config;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;

public class UIConfig {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(64, 123, 255);
    public static final Color HOVER_COLOR = new Color(245, 247, 255);
    public static final Color TEXT_COLOR = new Color(51, 51, 51);
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    public static final Color ERROR_COLOR = new Color(220, 53, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    public static void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Error setting UI Look and Feel: " + ex.getMessage());
        }
    }
}