package kos_online.models;

import java.sql.Date;

public class Rental {
    private int id;
    private int userId;
    private int roomId;
    private Date startDate;
    private int duration;
    private double totalPayment;
    private String paymentStatus;
    private Date paymentDate;
    private String paymentProof;

    public Rental() {}

    public Rental(int userId, int roomId, Date startDate, int duration, double totalPayment) {
        this.userId = userId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.duration = duration;
        this.totalPayment = totalPayment;
        this.paymentStatus = "Menunggu";
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getRoomId() { return roomId; }
    public Date getStartDate() { return startDate; }
    public int getDuration() { return duration; }
    public double getTotalPayment() { return totalPayment; }
    public String getPaymentStatus() { return paymentStatus; }
    public Date getPaymentDate() { return paymentDate; }
    public String getPaymentProof() { return paymentProof; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setTotalPayment(double totalPayment) { this.totalPayment = totalPayment; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public void setPaymentProof(String paymentProof) { this.paymentProof = paymentProof; }
}
