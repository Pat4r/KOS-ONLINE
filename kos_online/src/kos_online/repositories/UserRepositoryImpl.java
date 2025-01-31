package kos_online.repositories;

import kos_online.interfaces.UserRepository;
import kos_online.models.User;
import kos_online.interfaces.DatabaseOperation;
import java.sql.*;

public class UserRepositoryImpl implements UserRepository {
    private final DatabaseOperation dbOperation;
    
    public UserRepositoryImpl(DatabaseOperation dbOperation) {
        this.dbOperation = dbOperation;
    }
    
    @Override
    public DatabaseOperation getDatabaseOperation() {
        return dbOperation;
    }
    
    @Override
    public User findByUsernameAndPassword(String username, String password) 
            throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "SELECT * FROM Pengguna WHERE Nama_Pengguna = ? AND Password = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, rs);
        }
    }
    
    @Override
    public boolean usernameExists(String username) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "SELECT COUNT(*) FROM Pengguna WHERE Nama_Pengguna = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, rs);
        }
    }
    
    @Override
    public void save(User user) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "INSERT INTO Pengguna (Nama_Pengguna, Password, Email, Nomor_HP, Tipe_Pengguna) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getUserType());
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    @Override
    public void update(User user) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "UPDATE Pengguna SET Nama_Pengguna = ?, Email = ?, Nomor_HP = ?, Tipe_Pengguna = ? WHERE ID_Pengguna = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getUserType());
            ps.setInt(5, user.getId());
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    @Override
    public void delete(int userId) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "DELETE FROM Pengguna WHERE ID_Pengguna = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("ID_Pengguna"),
            rs.getString("Nama_Pengguna"),
            rs.getString("Email"),
            rs.getString("Nomor_HP"),
            rs.getString("Tipe_Pengguna")
        );
    }
}