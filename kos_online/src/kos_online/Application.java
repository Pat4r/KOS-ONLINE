package kos_online;

import kos_online.config.ApplicationConfig;
import kos_online.ui.SignIn;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Inisialisasi config dan dapatkan service sebelum membuat UI
        ApplicationConfig.initialize();

        SwingUtilities.invokeLater(() -> {
            // Gunakan authService yang sudah diinisialisasi
            SignIn signIn = new SignIn(ApplicationConfig.getAuthenticationService());
            signIn.setVisible(true);
        });
    }
}