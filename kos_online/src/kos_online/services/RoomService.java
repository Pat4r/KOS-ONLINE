package kos_online.services;

import kos_online.interfaces.RoomRepository;
import kos_online.models.Room;
import java.sql.SQLException;
import java.util.List;

public class RoomService {
    private final RoomRepository roomRepository;
    
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    public List<Room> getAllRooms() throws Exception {
        try {
            return roomRepository.findAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error loading rooms: " + e.getMessage());
        }
    }
    
    public Room getRoomByNumber(String number) throws Exception {
        try {
            return roomRepository.findByNumber(number);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error finding room: " + e.getMessage());
        }
    }
    
    public void saveRoom(Room room) throws Exception {
        try {
            roomRepository.save(room);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error saving room: " + e.getMessage());
        }
    }
    
    public void updateRoom(Room room) throws Exception {
        try {
            Room existingRoom = roomRepository.findByNumber(room.getNumber());
            if (existingRoom == null) {
                throw new Exception("Room not found");
            }
            if ("Terisi".equals(existingRoom.getStatus()) && 
                !"Terisi".equals(room.getStatus())) {
                throw new Exception("Cannot modify occupied room");
            }
            roomRepository.update(room);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error updating room: " + e.getMessage());
        }
    }
    
    public void deleteRoom(String roomNumber) throws Exception {
        try {
            Room room = roomRepository.findByNumber(roomNumber);
            if (room == null) {
                throw new Exception("Room not found");
            }
            if ("Terisi".equals(room.getStatus())) {
                throw new Exception("Cannot delete occupied room");
            }
            roomRepository.delete(roomNumber);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error deleting room: " + e.getMessage());
        }
    }
    
    public void updateRoomStatus(String roomNumber, String status) throws Exception {
        try {
            roomRepository.updateStatus(roomNumber, status);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error updating room status: " + e.getMessage());
        }
    }
}