package kos_online.services;

import kos_online.interfaces.UserRepository;
import kos_online.models.User;
import java.sql.SQLException;

public class AuthenticationService {
    private final UserRepository userRepository;
    
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User authenticate(String username, String password) throws Exception {
        try {
            User user = userRepository.findByUsernameAndPassword(username, password);
            if (user == null) {
                throw new Exception("Invalid username or password");
            }
            return user;
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }
    
    public void register(User user) throws Exception {
        try {
            if (userRepository.usernameExists(user.getUsername())) {
                throw new Exception("Username already exists");
            }
            userRepository.save(user);
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Database error: " + e.getMessage());
        }
    }
}
