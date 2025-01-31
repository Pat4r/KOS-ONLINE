package kos_online.services;

import kos_online.interfaces.UserRepository;
import kos_online.models.User;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public List<User> getAllTenants() throws Exception {
        try {
            Connection conn = userRepository.getDatabaseOperation().getConnection();
            List<User> tenants = new ArrayList<>();
            
            String query = "SELECT * FROM Pengguna WHERE Tipe_Pengguna = 'Pencari Kos'";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    User user = new User(
                        rs.getInt("ID_Pengguna"),
                        rs.getString("Nama_Pengguna"),
                        rs.getString("Email"),
                        rs.getString("Nomor_HP"),
                        rs.getString("Tipe_Pengguna")
                    );
                    tenants.add(user);
                }
            }
            
            return tenants;
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting tenants: " + e.getMessage());
        }
    }
    
    public void updateUser(User user) throws Exception {
        try {
            userRepository.update(user);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error updating user: " + e.getMessage());
        }
    }
    
    public void deleteUser(int userId) throws Exception {
        try {
            userRepository.delete(userId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error deleting user: " + e.getMessage());
        }
    }
    
    public User findUserById(int userId) throws Exception {
        try {
            Connection conn = userRepository.getDatabaseOperation().getConnection();
            String query = "SELECT * FROM Pengguna WHERE ID_Pengguna = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    return new User(
                        rs.getInt("ID_Pengguna"),
                        rs.getString("Nama_Pengguna"),
                        rs.getString("Email"),
                        rs.getString("Nomor_HP"),
                        rs.getString("Tipe_Pengguna")
                    );
                }
                return null;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error finding user: " + e.getMessage());
        }
    }
    
    public List<User> getAllUsers() throws Exception {
        try {
            Connection conn = userRepository.getDatabaseOperation().getConnection();
            List<User> users = new ArrayList<>();
            
            String query = "SELECT * FROM Pengguna";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    User user = new User(
                        rs.getInt("ID_Pengguna"),
                        rs.getString("Nama_Pengguna"),
                        rs.getString("Email"),
                        rs.getString("Nomor_HP"),
                        rs.getString("Tipe_Pengguna")
                    );
                    users.add(user);
                }
            }
            
            return users;
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting users: " + e.getMessage());
        }
    }
    
    public boolean validateUser(int userId, String userType) throws Exception {
        try {
            Connection conn = userRepository.getDatabaseOperation().getConnection();
            String query = "SELECT COUNT(*) FROM Pengguna WHERE ID_Pengguna = ? AND Tipe_Pengguna = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setString(2, userType);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error validating user: " + e.getMessage());
        }
    }
    
    public String getUserName(int userId) throws Exception {
        try {
            Connection conn = userRepository.getDatabaseOperation().getConnection();
            String query = "SELECT Nama_Pengguna FROM Pengguna WHERE ID_Pengguna = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    return rs.getString("Nama_Pengguna");
                }
                return "Unknown";
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting username: " + e.getMessage());
        }
    }
    
    public boolean isEmailExists(String email) throws Exception {
        try {
            Connection conn = userRepository.getDatabaseOperation().getConnection();
            String query = "SELECT COUNT(*) FROM Pengguna WHERE Email = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error checking email: " + e.getMessage());
        }
    }
    
    public void changePassword(int userId, String newPassword) throws Exception {
        try {
            Connection conn = userRepository.getDatabaseOperation().getConnection();
            String query = "UPDATE Pengguna SET Password = ? WHERE ID_Pengguna = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, newPassword);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error changing password: " + e.getMessage());
        }
    }
}