
package service;

import model.Doctor;
import db.DBConnection;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorService {

    // Constructor
    public DoctorService() {
    }

    // Add Doctor
    public void addDoctor(Doctor doctor) {

        String query = "INSERT INTO doctors (doctor_id, name, specialization, experience, contact, consultation_fee) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, doctor.getDoctorId());
            ps.setString(2, doctor.getName());
            ps.setString(3, doctor.getSpecialization());
            ps.setInt(4, doctor.getExperience());
            ps.setString(5, doctor.getContact());
            ps.setDouble(6, doctor.getConsultationFee());

            ps.executeUpdate();

            System.out.println("Doctor added successfully!");

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to add doctor: " + e.getMessage(), e);
        }
    }

    // View All Doctors
    public ArrayList<Doctor> getAllDoctors() {

        ArrayList<Doctor> doctors = new ArrayList<>();

        String query = "SELECT * FROM doctors ORDER BY doctor_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Doctor doctor = new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getInt("experience"),
                        rs.getString("contact"),
                        rs.getDouble("consultation_fee")
                );

                doctors.add(doctor);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to retrieve doctors: " + e.getMessage(), e);
        }

        return doctors;
    }

    // Find Doctor
    public Doctor findDoctor(int doctorId) {

        String query = "SELECT * FROM doctors WHERE doctor_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Doctor(
                            rs.getInt("doctor_id"),
                            rs.getString("name"),
                            rs.getString("specialization"),
                            rs.getInt("experience"),
                            rs.getString("contact"),
                            rs.getDouble("consultation_fee")
                    );
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to find doctor: " + e.getMessage(), e);
        }

        return null;
    }

    // Delete Doctor
    public boolean deleteDoctor(int doctorId) {

        String query = "DELETE FROM doctors WHERE doctor_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, doctorId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to delete doctor: " + e.getMessage(), e);
        }
    }

    // Update Doctor
    public boolean updateDoctor(int doctorId, String name,
            String specialization, int experience,
            String contact, double consultationFee) {

        String query = "UPDATE doctors SET name = ?, specialization = ?, experience = ?, contact = ?, consultation_fee = ? WHERE doctor_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setInt(3, experience);
            ps.setString(4, contact);
            ps.setDouble(5, consultationFee);
            ps.setInt(6, doctorId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to update doctor: " + e.getMessage(), e);
        }
    }

    // Generate Next Doctor ID
    public int getNextDoctorId() {

        String query = "SELECT COALESCE(MAX(doctor_id), 2000) + 1 AS next_id FROM doctors";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("next_id");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to generate doctor ID: " + e.getMessage(), e);
        }

        return 2001;
    }
}
