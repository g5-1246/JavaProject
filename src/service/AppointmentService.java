package service;

import model.Appointment;
import db.DBConnection;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;

public class AppointmentService {

    public AppointmentService() {
    }

    // Add Appointment
    public void addAppointment(Appointment appointment) {

        String query = "INSERT INTO appointments " +
                "(appointment_id, patient_id, doctor_id, appointment_date, " +
                "appointment_time, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, appointment.getAppointmentId());
            ps.setInt(2, appointment.getPatientId());
            ps.setInt(3, appointment.getDoctorId());
            ps.setDate(4, Date.valueOf(appointment.getDate()));
            ps.setTime(5, Time.valueOf(appointment.getTime()));
            ps.setString(6, appointment.getReason());
            ps.setString(7, appointment.getStatus());

            ps.executeUpdate();

            System.out.println("Appointment added successfully!");

        } catch (SQLException | IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Unable to add appointment: " + e.getMessage(), e);
        }
    }

    // Get All Appointments
    public ArrayList<Appointment> getAllAppointments() {

        ArrayList<Appointment> appointments = new ArrayList<>();

        String query = "SELECT * FROM appointments ORDER BY appointment_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Appointment appointment = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("reason"),
                        rs.getString("status")
                );

                appointments.add(appointment);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to retrieve appointments: " + e.getMessage(), e);
        }

        return appointments;
    }

    // Find Appointment
    public Appointment findAppointment(int appointmentId) {

        String query = "SELECT * FROM appointments WHERE appointment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Appointment(
                            rs.getInt("appointment_id"),
                            rs.getInt("patient_id"),
                            rs.getInt("doctor_id"),
                            rs.getString("appointment_date"),
                            rs.getString("appointment_time"),
                            rs.getString("reason"),
                            rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to find appointment: " + e.getMessage(), e);
        }

        return null;
    }

    // Delete Appointment
    public boolean deleteAppointment(int appointmentId) {

        String query = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, appointmentId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to delete appointment: " + e.getMessage(), e);
        }
    }

    // Update Appointment
    public boolean updateAppointment(
            int appointmentId,
            int patientId,
            int doctorId,
            String date,
            String time,
            String reason,
            String status) {

        String query = "UPDATE appointments SET patient_id = ?, " +
                "doctor_id = ?, appointment_date = ?, appointment_time = ?, " +
                "reason = ?, status = ? WHERE appointment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setDate(3, Date.valueOf(date));
            ps.setTime(4, Time.valueOf(time));
            ps.setString(5, reason);
            ps.setString(6, status);
            ps.setInt(7, appointmentId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException | IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Unable to update appointment: " + e.getMessage(), e);
        }
    }

    // Generate Next Appointment ID
    public int getNextAppointmentId() {

        String query = "SELECT COALESCE(MAX(appointment_id), 3000) + 1 " +
                "AS next_id FROM appointments";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("next_id");
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to generate appointment ID: " + e.getMessage(), e);
        }

        return 3001;
    }
}