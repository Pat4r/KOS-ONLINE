package kos_online.models;

import java.util.Date;

public class RentalView {
    private String roomNumber;
    private String tenantName;
    private Date startDate;
    private int duration;
    private double totalPayment;
    private String paymentStatus;
    private Date paymentDate;
    private String paymentProof;

    public RentalView() {}

    // Getters
    public String getRoomNumber() { return roomNumber; }
    public String getTenantName() { return tenantName; }
    public Date getStartDate() { return startDate; }
    public int getDuration() { return duration; }
    public double getTotalPayment() { return totalPayment; }
    public String getPaymentStatus() { return paymentStatus; }
    public Date getPaymentDate() { return paymentDate; }
    public String getPaymentProof() { return paymentProof; }

    // Setters
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setTotalPayment(double totalPayment) { this.totalPayment = totalPayment; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public void setPaymentProof(String paymentProof) { this.paymentProof = paymentProof; }

    public int getId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}