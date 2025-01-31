package kos_online.models;

public class Room {
    private int id;
    private String number;
    private String status;
    private double pricePerMonth;
    private String facilities;

    public Room() {}

    public Room(String number, String status, double pricePerMonth, String facilities) {
        this.number = number;
        this.status = status;
        this.pricePerMonth = pricePerMonth;
        this.facilities = facilities;
    }

    public Room(int id, String number, String status, double pricePerMonth, String facilities) {
        this.id = id;
        this.number = number;
        this.status = status;
        this.pricePerMonth = pricePerMonth;
        this.facilities = facilities;
    }

    // Getters
    public int getId() { return id; }
    public String getNumber() { return number; }
    public String getStatus() { return status; }
    public double getPricePerMonth() { return pricePerMonth; }
    public String getFacilities() { return facilities; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNumber(String number) { this.number = number; }
    public void setStatus(String status) { this.status = status; }
    public void setPricePerMonth(double pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public void setFacilities(String facilities) { this.facilities = facilities; }
}
