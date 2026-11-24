package service;
import db.DBConnection;
import model.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
public class BillingService {
    // Get all bills from MySQL
    public synchronized List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String query =
            "SELECT b.bill_id, b.patient_id, p.name AS patient_name, " +
            "b.doctor_id, d.name AS doctor_name, " +
            "b.consultation_fee, b.other_charges, b.amount_paid, " +
            "b.payment_method, b.payment_status, b.created_at " +
            "FROM billing b " +
            "LEFT JOIN patients p ON b.patient_id = p.patient_id " +
            "LEFT JOIN doctors d ON b.doctor_id = d.doctor_id " +
            "ORDER BY b.bill_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("created_at");
                Bill bill = new Bill(
                    rs.getInt("bill_id"),
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getDouble("consultation_fee"),
                    rs.getDouble("other_charges"),
                    rs.getDouble("amount_paid"),
                    rs.getString("payment_method"),
                    rs.getString("payment_status"),
                    timestamp.toLocalDateTime()
                );
                bills.add(bill);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Unable to fetch billing records: " + e.getMessage(), e
            );
        }
        return bills;
    }
    // Generate next bill ID
    public synchronized int getNextBillId() {
        String query =
            "SELECT COALESCE(MAX(bill_id), 3000) + 1 AS next_id FROM billing";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_id");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Unable to generate bill ID: " + e.getMessage(), e
            );
        }
        return 3001;
    }
    // Add new bill to MySQL
    public synchronized void addBill(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be empty.");
        }
        String query =
            "INSERT INTO billing " +
            "(bill_id, patient_id, doctor_id, consultation_fee, " +
            "other_charges, amount_paid, payment_method, " +
            "payment_status, created_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, bill.getBillId());
            ps.setInt(2, bill.getPatientId());
            ps.setInt(3, bill.getDoctorId());
            ps.setDouble(4, bill.getConsultationFee());
            ps.setDouble(5, bill.getOtherCharges());
            ps.setDouble(6, bill.getAmountPaid());
            ps.setString(7, bill.getPaymentMethod());
            ps.setString(8, bill.getPaymentStatus());
            ps.setTimestamp(9, Timestamp.valueOf(bill.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Unable to save billing record: " + e.getMessage(), e
            );
        }
    }
}
