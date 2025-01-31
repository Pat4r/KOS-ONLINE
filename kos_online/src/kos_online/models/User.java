package kos_online.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String userType;

    public User() {}

    public User(int id, String username, String email, String phoneNumber, String userType) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    public User(String username, String password, String email, String phoneNumber, String userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getUserType() { return userType; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setUserType(String userType) { this.userType = userType; }
}