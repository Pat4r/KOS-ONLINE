package kos_online.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseOperation {
    Connection getConnection() throws SQLException, ClassNotFoundException;
    void closeConnection(Connection connection) throws SQLException;
}