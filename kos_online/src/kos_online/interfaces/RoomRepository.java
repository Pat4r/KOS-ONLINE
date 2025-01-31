package kos_online.interfaces;

import kos_online.models.Room;
import java.sql.SQLException;
import java.util.List;

public interface RoomRepository {
    List<Room> findAll() throws SQLException, ClassNotFoundException;
    Room findByNumber(String number) throws SQLException, ClassNotFoundException;
    void save(Room room) throws SQLException, ClassNotFoundException;
    void update(Room room) throws SQLException, ClassNotFoundException;
    void delete(String roomNumber) throws SQLException, ClassNotFoundException;
    void updateStatus(String roomNumber, String status) throws SQLException, ClassNotFoundException;
}