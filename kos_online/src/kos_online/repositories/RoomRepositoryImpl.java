package kos_online.repositories;

import kos_online.interfaces.RoomRepository;
import kos_online.models.Room;
import kos_online.interfaces.DatabaseOperation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository {
    private final DatabaseOperation dbOperation;
    
    public RoomRepositoryImpl(DatabaseOperation dbOperation) {
        this.dbOperation = dbOperation;
    }
    
    @Override
    public List<Room> findAll() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            List<Room> rooms = new ArrayList<>();
            String query = "SELECT * FROM Kamar ORDER BY Nomor_Kamar";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, rs);
        }
    }
    
    @Override
    public Room findByNumber(String number) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "SELECT * FROM Kamar WHERE Nomor_Kamar = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, number);
            
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
            return null;
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, rs);
        }
    }
    
    @Override
    public void save(Room room) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "INSERT INTO Kamar (Nomor_Kamar, Status, Harga_Per_Bulan, Fasilitas) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, room.getNumber());
            ps.setString(2, room.getStatus());
            ps.setDouble(3, room.getPricePerMonth());
            ps.setString(4, room.getFacilities());
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    @Override
    public void update(Room room) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "UPDATE Kamar SET Status = ?, Harga_Per_Bulan = ?, Fasilitas = ? WHERE Nomor_Kamar = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, room.getStatus());
            ps.setDouble(2, room.getPricePerMonth());
            ps.setString(3, room.getFacilities());
            ps.setString(4, room.getNumber());
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
            String query = "DELETE FROM Kamar WHERE Nomor_Kamar = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, roomNumber);
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    @Override
    public void updateStatus(String roomNumber, String status) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "UPDATE Kamar SET Status = ? WHERE Nomor_Kamar = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setString(2, roomNumber);
            ps.executeUpdate();
        } finally {
            ((DatabaseConnectionImpl)dbOperation).closeAll(conn, ps, null);
        }
    }
    
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        return new Room(
            rs.getInt("ID_Kamar"),
            rs.getString("Nomor_Kamar"),
            rs.getString("Status"),
            rs.getDouble("Harga_Per_Bulan"),
            rs.getString("Fasilitas")
        );
    }
}