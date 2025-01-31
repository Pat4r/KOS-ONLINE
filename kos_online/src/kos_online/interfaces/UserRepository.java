package kos_online.interfaces;

import kos_online.models.User;
import java.sql.SQLException;

public interface UserRepository {
    DatabaseOperation getDatabaseOperation();
    User findByUsernameAndPassword(String username, String password) throws SQLException, ClassNotFoundException;
    boolean usernameExists(String username) throws SQLException, ClassNotFoundException;
    void save(User user) throws SQLException, ClassNotFoundException;
    void update(User user) throws SQLException, ClassNotFoundException;
    void delete(int userId) throws SQLException, ClassNotFoundException;
}