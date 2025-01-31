package kos_online.services;

import kos_online.interfaces.DatabaseOperation;
import kos_online.interfaces.RentalRepository;
import kos_online.interfaces.RoomRepository;
import kos_online.models.Rental;
import kos_online.models.Room;
import kos_online.models.RentalView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RentalService {
    private final RentalRepository rentalRepository;
    private final RoomRepository roomRepository;
    private final DatabaseOperation dbOperation;
    
    public RentalService(RentalRepository rentalRepository, RoomRepository roomRepository, DatabaseOperation dbOperation) {
        this.rentalRepository = rentalRepository;
        this.roomRepository = roomRepository;
        this.dbOperation = dbOperation;
    }
    
    public List<Rental> getAllRentals() throws Exception {
        try {
            return rentalRepository.findAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error loading rentals: " + e.getMessage());
        }
    }
    
    public List<RentalView> getAllRentalsView() throws Exception {
        try {
            List<Rental> rentals = rentalRepository.findAll();
            List<RentalView> rentalViews = new ArrayList<>();
            
            for (Rental rental : rentals) {
                RentalView view = new RentalView();
                Room room = null;
                
                // Cari room berdasarkan roomId
                for (Room r : roomRepository.findAll()) {
                    if (r.getId() == rental.getRoomId()) {
                        room = r;
                        break;
                    }
                }
                
                if (room != null) {
                    view.setRoomNumber(room.getNumber());
                    view.setTenantName(getTenantName(rental.getUserId())); 
                    view.setDuration(rental.getDuration());
                    view.setStartDate(rental.getStartDate());
                    view.setTotalPayment(rental.getTotalPayment());
                    view.setPaymentStatus(rental.getPaymentStatus());
                    view.setPaymentDate(rental.getPaymentDate());
                    view.setPaymentProof(rental.getPaymentProof());
                    rentalViews.add(view);
                }
            }
            return rentalViews;
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error loading rental views: " + e.getMessage());
        }
    }
    
    public List<Rental> getRentalsByUser(int userId) throws Exception {
        try {
            return rentalRepository.findByUserId(userId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error loading user rentals: " + e.getMessage());
        }
    }
    
    public RentalView getRentalViewByRoomNumber(String roomNumber) throws Exception {
        try {
            Rental rental = rentalRepository.findByRoomNumber(roomNumber);
            if (rental == null) {
                return null;
            }
            
            Room room = roomRepository.findByNumber(roomNumber);
            if (room == null) {
                throw new Exception("Room not found for rental");
            }
            
            RentalView view = new RentalView();
            view.setRoomNumber(room.getNumber());
            view.setTenantName(getTenantName(rental.getUserId()));
            view.setDuration(rental.getDuration());
            view.setStartDate(rental.getStartDate());
            view.setTotalPayment(rental.getTotalPayment());
            view.setPaymentStatus(rental.getPaymentStatus());
            view.setPaymentDate(rental.getPaymentDate());
            view.setPaymentProof(rental.getPaymentProof());
            
            return view;
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting rental view: " + e.getMessage());
        }
    }
    
    public void createRental(Rental rental, String roomNumber) throws Exception {
        try {
            // Validasi ID Pengguna
            if (rental.getUserId() <= 0) {
                throw new Exception("Invalid user ID");
            }
            
            // Validasi keberadaan user ID di database
            if (!isUserExists(rental.getUserId())) {
                throw new Exception("User ID not found in database");
            }
            
            Room room = roomRepository.findByNumber(roomNumber);
            if (room == null) {
                throw new Exception("Room not found");
            }
            
            if ("Terisi".equals(room.getStatus())) {
                throw new Exception("Room is already occupied");
            }
            
            rental.setRoomId(room.getId());
            rentalRepository.save(rental);
            roomRepository.updateStatus(roomNumber, "Terisi");
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error creating rental: " + e.getMessage());
        }
    }
    
    private boolean isUserExists(int userId) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = dbOperation.getConnection();
            String query = "SELECT COUNT(*) FROM Pengguna WHERE ID_Pengguna = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
    
public void createRental(String roomNumber, int userId, int duration, double total, 
        String status, Date startDate, Date paymentDate, String imagePath) throws Exception {
        try {
            Room room = roomRepository.findByNumber(roomNumber);
            if (room == null) {
                throw new Exception("Room not found");
            }
            
            if ("Terisi".equals(room.getStatus())) {
                throw new Exception("Room is already occupied");
            }
            
            Rental rental = new Rental();
            rental.setUserId(userId);  // Set user ID yang valid
            rental.setRoomId(room.getId());
            rental.setDuration(duration);
            rental.setTotalPayment(total);
            rental.setPaymentStatus(status);
            rental.setStartDate(new java.sql.Date(startDate.getTime()));
            
            if (paymentDate != null) {
                rental.setPaymentDate(new java.sql.Date(paymentDate.getTime()));
            }
            
            rental.setPaymentProof(imagePath);
            rentalRepository.save(rental);
            roomRepository.updateStatus(roomNumber, "Terisi");
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error creating rental: " + e.getMessage());
        }
    }
    
    public void updateRental(Rental rental) throws Exception {
        try {
            rentalRepository.update(rental);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error updating rental: " + e.getMessage());
        }
    }
    
    public void updateRental(String roomNumber, int duration, double total,
            String status, Date paymentDate, String imagePath) throws Exception {
        try {
            Rental rental = rentalRepository.findByRoomNumber(roomNumber);
            if (rental == null) {
                throw new Exception("Rental not found");
            }
            
            rental.setDuration(duration);
            rental.setTotalPayment(total);
            rental.setPaymentStatus(status);
            
            if (paymentDate != null) {
                rental.setPaymentDate(new java.sql.Date(paymentDate.getTime()));
            }
            
            rental.setPaymentProof(imagePath);
            rentalRepository.update(rental);
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error updating rental: " + e.getMessage());
        }
    }
    
    public void updatePayment(String roomNumber, String proofPath) throws Exception {
        try {
            Rental rental = rentalRepository.findByRoomNumber(roomNumber);
            if (rental == null) {
                throw new Exception("Rental not found");
            }
            rentalRepository.updatePaymentStatus(rental.getId(), "Dibayar", proofPath);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error updating payment: " + e.getMessage());
        }
    }
    
    public void cancelRental(String roomNumber) throws Exception {
        try {
            rentalRepository.delete(roomNumber);
            roomRepository.updateStatus(roomNumber, "Kosong");
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error canceling rental: " + e.getMessage());
        }
    }

    private String getTenantName(int userId) throws SQLException, ClassNotFoundException {
        String query = "SELECT Nama_Pengguna FROM Pengguna WHERE ID_Pengguna = ?";
        try (Connection conn = dbOperation.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Nama_Pengguna");
            }
            return "Unknown";
        }
    }
}