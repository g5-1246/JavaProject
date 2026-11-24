package model;

import java.time.LocalDateTime;

public class Bill {

    private int billId;
    private int patientId;
    private int doctorId;

    private String patientName;
    private String doctorName;

    private double consultationFee;
    private double otherCharges;
    private double amountPaid;

    private String paymentMethod;
    private String paymentStatus;

    private LocalDateTime createdAt;

    public Bill(int billId, int patientId, String patientName,
                int doctorId, String doctorName,
                double consultationFee, double otherCharges,
                double amountPaid, String paymentMethod,
                String paymentStatus, LocalDateTime createdAt) {

        this.billId = billId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.consultationFee = consultationFee;
        this.otherCharges = otherCharges;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    public int getBillId() {
        return billId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public double getOtherCharges() {
        return otherCharges;
    }

    public double getTotal() {
        return consultationFee + otherCharges;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public double getBalance() {
        return Math.max(0, getTotal() - amountPaid);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}