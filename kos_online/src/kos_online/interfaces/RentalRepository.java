package kos_online.interfaces;

import kos_online.models.Rental;
import java.sql.SQLException;
import java.util.List;

public interface RentalRepository {
    List<Rental> findAll() throws SQLException, ClassNotFoundException;
    List<Rental> findByUserId(int userId) throws SQLException, ClassNotFoundException;
    Rental findByRoomNumber(String roomNumber) throws SQLException, ClassNotFoundException;
    void save(Rental rental) throws SQLException, ClassNotFoundException;
    void update(Rental rental) throws SQLException, ClassNotFoundException;
    void delete(String roomNumber) throws SQLException, ClassNotFoundException;
    void updatePaymentStatus(int rentalId, String status, String proofPath) throws SQLException, ClassNotFoundException;
}