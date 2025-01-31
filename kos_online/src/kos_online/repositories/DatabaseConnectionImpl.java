package kos_online.repositories;

import kos_online.interfaces.DatabaseOperation;
import java.sql.*;

public class DatabaseConnectionImpl implements DatabaseOperation {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/KosOnline";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    @Override
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    @Override
    public void closeConnection(Connection connection) throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Error closing connection: " + e.getMessage());
        }
    }

    public void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) closeConnection(conn);
        } catch (SQLException e) {
            throw new SQLException("Error closing resources: " + e.getMessage());
        }
    }
}