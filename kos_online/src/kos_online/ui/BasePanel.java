package kos_online.ui;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected static final Color PRIMARY_COLOR = new Color(64, 123, 255);
    protected static final Color HOVER_COLOR = new Color(245, 247, 255);
    protected static final Color TEXT_COLOR = new Color(51, 51, 51);
    protected static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    protected static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    public BasePanel() {
        setBackground(Color.WHITE);
    }
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 30));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
}