
package service;

import model.Patient;
import db.DBConnection;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientService {

    // Constructor
    public PatientService() {
    }

    // Add Patient
    public void addPatient(Patient patient) {

        String query = "INSERT INTO patients " +
                "(patient_id, name, age, gender, contact, address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)
        ) {

            ps.setInt(1, patient.getPatientId());
            ps.setString(2, patient.getName());
            ps.setInt(3, patient.getAge());
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getContact());
            ps.setString(6, patient.getAddress());

            ps.executeUpdate();

            System.out.println("Patient added successfully!");

        } catch (SQLException ex) {

            throw new IllegalStateException(
                "Unable to add patient: " + ex.getMessage(), ex
            );
        }
    }

    // View All Patients
    public void viewPatients() {

        ArrayList<Patient> patients = getAllPatients();

        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        for (int i = 0; i < patients.size(); i++) {

            Patient p = patients.get(i);

            System.out.println("Patient ID: " + p.getPatientId());
            System.out.println("Name: " + p.getName());
            System.out.println("Age: " + p.getAge());
            System.out.println("Gender: " + p.getGender());
            System.out.println("Contact: " + p.getContact());
            System.out.println("Address: " + p.getAddress());

            System.out.println("-------------------------");
        }
    }

    // Find Patient by ID
    public Patient findPatient(int patientId) {

        String query = "SELECT * FROM patients WHERE patient_id = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)
        ) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("contact"),
                        rs.getString("address")
                    );
                }
            }

        } catch (SQLException ex) {

            throw new IllegalStateException(
                "Unable to find patient: " + ex.getMessage(), ex
            );
        }

        return null;
    }

    // Delete Patient
    public boolean deletePatient(int patientId) {

        String query = "DELETE FROM patients WHERE patient_id = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)
        ) {

            ps.setInt(1, patientId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException ex) {

            throw new IllegalStateException(
                "Unable to delete patient: " + ex.getMessage(), ex
            );
        }
    }

    // Update Patient
    public boolean updatePatient(int patientId, String name, int age,
                                 String gender, String contact, String address) {

        String query = "UPDATE patients SET name = ?, age = ?, " +
                "gender = ?, contact = ?, address = ? " +
                "WHERE patient_id = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)
        ) {

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, contact);
            ps.setString(5, address);
            ps.setInt(6, patientId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException ex) {

            throw new IllegalStateException(
                "Unable to update patient: " + ex.getMessage(), ex
            );
        }
    }

    // Get All Patients
    public ArrayList<Patient> getAllPatients() {

        ArrayList<Patient> patients = new ArrayList<>();

        String query = "SELECT * FROM patients ORDER BY patient_id";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("contact"),
                    rs.getString("address")
                );

                patients.add(patient);
            }

        } catch (SQLException ex) {

            throw new IllegalStateException(
                "Unable to retrieve patients: " + ex.getMessage(), ex
            );
        }

        return patients;
    }

    // Generate Next Patient ID
    public int getNextPatientId() {

        String query = "SELECT COALESCE(MAX(patient_id), 1000) + 1 AS next_id " +
                "FROM patients";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("next_id");
            }

        } catch (SQLException ex) {

            throw new IllegalStateException(
                "Unable to generate patient ID: " + ex.getMessage(), ex
            );
        }

        return 1001;
    }
}
