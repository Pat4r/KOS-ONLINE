package kos_online.repositories;

import kos_online.interfaces.RentalRepository;
import kos_online.models.Rental;
import kos_online.interfaces.DatabaseOperation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {
    private final DatabaseOperation dbOperation;
    
    public RentalRepositoryImpl(DatabaseOperation dbOperation) {
        this.dbOperation = dbOperation;
    }
    
    @Override
    public List<Rental> findAll() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            List<Rental> rentals = new ArrayList<>();
            String query = """
                SELECT p.*, k.Nomor_Kamar 
                FROM Penyewaan p
                JOIN Kamar k ON p.ID_Kamar = k.ID_Kamar
                ORDER BY p.Tanggal_Mulai DESC
                """;
                
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                rentals.add(mapResultSetToRental(rs));
            }
            return rentals;
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, rs);
        }
    }
    
    @Override
    public List<Rental> findByUserId(int userId) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            List<Rental> rentals = new ArrayList<>();
            String query = "SELECT * FROM Penyewaan WHERE ID_Pengguna = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                rentals.add(mapResultSetToRental(rs));
            }
            return rentals;
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, rs);
        }
    }
    
    @Override
    public Rental findByRoomNumber(String roomNumber) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = """
                SELECT p.* FROM Penyewaan p
                JOIN Kamar k ON p.ID_Kamar = k.ID_Kamar
                WHERE k.Nomor_Kamar = ?
                """;
            ps = conn.prepareStatement(query);
            ps.setString(1, roomNumber);
            
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToRental(rs);
            }
            return null;
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, rs);
        }
    }
    
    @Override
    public void save(Rental rental) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = """
                INSERT INTO Penyewaan (ID_Pengguna, ID_Kamar, Tanggal_Mulai, 
                Durasi_Sewa, Total_Pembayaran, Status_Pembayaran, 
                Tanggal_Pembayaran, Bukti_Pembayaran)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

            ps = conn.prepareStatement(query);
            ps.setInt(1, rental.getUserId());
            ps.setInt(2, rental.getRoomId());
            ps.setDate(3, rental.getStartDate());
            ps.setInt(4, rental.getDuration());
            ps.setDouble(5, rental.getTotalPayment());
            ps.setString(6, rental.getPaymentStatus());
            ps.setDate(7, rental.getPaymentDate()); 
            ps.setString(8, rental.getPaymentProof()); 
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    @Override
    public void update(Rental rental) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = """
                UPDATE Penyewaan 
                SET Durasi_Sewa = ?,
                    Total_Pembayaran = ?,
                    Status_Pembayaran = ?,
                    Tanggal_Pembayaran = ?,
                    Bukti_Pembayaran = ?
                WHERE ID_Penyewaan = ?
                """;
            ps = conn.prepareStatement(query);
            ps.setInt(1, rental.getDuration());
            ps.setDouble(2, rental.getTotalPayment());
            ps.setString(3, rental.getPaymentStatus());
            ps.setDate(4, rental.getPaymentDate());
            ps.setString(5, rental.getPaymentProof());
            ps.setInt(6, rental.getId());
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    @Override
    public void delete(String roomNumber) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = """
                DELETE FROM Penyewaan 
                WHERE ID_Kamar = (
                    SELECT ID_Kamar FROM Kamar WHERE Nomor_Kamar = ?
                )
                """;
            ps = conn.prepareStatement(query);
            ps.setString(1, roomNumber);
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    @Override
    public void updatePaymentStatus(int rentalId, String status, String proofPath) 
            throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = """
                UPDATE Penyewaan 
                SET Status_Pembayaran = ?,
                    Bukti_Pembayaran = ?,
                    Tanggal_Pembayaran = CURRENT_DATE
                WHERE ID_Penyewaan = ?
                """;
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setString(2, proofPath);
            ps.setInt(3, rentalId);
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    private Rental mapResultSetToRental(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setId(rs.getInt("ID_Penyewaan"));
        rental.setUserId(rs.getInt("ID_Pengguna"));
        rental.setRoomId(rs.getInt("ID_Kamar")); 
        rental.setStartDate(rs.getDate("Tanggal_Mulai"));
        rental.setDuration(rs.getInt("Durasi_Sewa"));
        rental.setTotalPayment(rs.getDouble("Total_Pembayaran"));
        rental.setPaymentStatus(rs.getString("Status_Pembayaran"));
        rental.setPaymentDate(rs.getDate("Tanggal_Pembayaran"));
        rental.setPaymentProof(rs.getString("Bukti_Pembayaran"));
        return rental;
    }
}