package kos_online.config;

import kos_online.interfaces.*;
import kos_online.repositories.*;
import kos_online.services.*;

public class ApplicationConfig {
    private static DatabaseOperation dbOperation;
    private static UserRepository userRepository;
    private static RoomRepository roomRepository;
    private static RentalRepository rentalRepository;
    private static AuthenticationService authService;
    private static RoomService roomService;
    private static RentalService rentalService;
    private static UserService userService;
    
    public static void initialize() {
        try {
            // Initialize database connection
            if (dbOperation == null) {
                dbOperation = new DatabaseConnectionImpl();
            }
            
            // Initialize repositories
            if (userRepository == null) {
                userRepository = new UserRepositoryImpl(dbOperation);
            }
            if (roomRepository == null) {
                roomRepository = new RoomRepositoryImpl(dbOperation);
            }
            if (rentalRepository == null) {
                rentalRepository = new RentalRepositoryImpl(dbOperation);
            }
            
            // Initialize services
            if (authService == null) {
                authService = new AuthenticationService(userRepository);
            }
            if (roomService == null) {
                roomService = new RoomService(roomRepository);
            }
            if (rentalService == null) {
                rentalService = new RentalService(rentalRepository, roomRepository, dbOperation);
            }
            if (userService == null) {
                userService = new UserService(userRepository);
            }
        } catch (Exception e) {
            System.err.println("Error initializing application config: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void cleanup() {
        try {
            // Cleanup all resources
            if (dbOperation != null) {
                // Close any open database connections
                dbOperation = null;
            }
            
            // Reset all repositories
            userRepository = null;
            roomRepository = null;
            rentalRepository = null;
            
            // Reset all services
            authService = null;
            roomService = null;
            rentalService = null;
            userService = null;
            
            // Force garbage collection
            System.gc();
            
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Getters for services with lazy initialization
    public static AuthenticationService getAuthenticationService() {
        if (authService == null) {
            initialize();
        }
        return authService;
    }
    
    public static RoomService getRoomService() {
        if (roomService == null) {
            initialize();
        }
        return roomService;
    }
    
    public static RentalService getRentalService() {
        if (rentalService == null) {
            initialize();
        }
        return rentalService;
    }
    
    public static UserService getUserService() {
        if (userService == null) {
            initialize();
        }
        return userService;
    }
    
    // Getter for database operation
    public static DatabaseOperation getDatabaseOperation() {
        if (dbOperation == null) {
            initialize();
        }
        return dbOperation;
    }
    
    // Method to check if config is initialized
    public static boolean isInitialized() {
        return dbOperation != null && 
               userRepository != null && 
               roomRepository != null && 
               rentalRepository != null && 
               authService != null && 
               roomService != null && 
               rentalService != null &&
               userService != null;
    }
    
    // Method to reinitialize if needed
    public static void reinitialize() {
        cleanup();
        initialize();
    }
    
    // Method to check database connection
    public static boolean checkDatabaseConnection() {
        try {
            if (dbOperation == null) {
                return false;
            }
            dbOperation.getConnection().close();
            return true;
        } catch (Exception e) {
            System.err.println("Database connection check failed: " + e.getMessage());
            return false;
        }
    }
}