
package cli;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import model.Appointment;
import service.AppointmentService;
import service.PatientService;
import service.DoctorService;

public class AppointmentMenu {

    private Scanner sc;
    private AppointmentService appointmentService;
    private PatientService patientService;
    private DoctorService doctorService;

    public AppointmentMenu(
            Scanner sc,
            AppointmentService appointmentService,
            PatientService patientService,
            DoctorService doctorService) {

        this.sc = sc;
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public void showMenu() {

        int choice;

        do {
            System.out.println("\n========== APPOINTMENT MANAGEMENT ==========");
            System.out.println("1. Book New Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. Search Appointment by ID");
            System.out.println("4. Update Appointment");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. View Appointments by Patient ID");
            System.out.println("7. Delete Appointment");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = readInt();

            try {
                switch (choice) {
                    case 1:
                        bookAppointment();
                        break;

                    case 2:
                        viewAllAppointments();
                        break;

                    case 3:
                        searchAppointment();
                        break;

                    case 4:
                        updateAppointment();
                        break;

                    case 5:
                        cancelAppointment();
                        break;

                    case 6:
                        viewPatientAppointments();
                        break;

                    case 7:
                        deleteAppointment();
                        break;

                    case 8:
                        System.out.println("Returning to Main Menu...");
                        break;

                    default:
                        System.out.println("Invalid choice! Try again.");
                }

            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 8);
    }

    // 1. Book Appointment
    private void bookAppointment() {

        System.out.println("\n---------- BOOK NEW APPOINTMENT ----------");

        int appointmentId = appointmentService.getNextAppointmentId();

        System.out.println("Generated Appointment ID: " + appointmentId);

        System.out.print("Enter Patient ID: ");
        int patientId = readInt();

        if (patientService.findPatient(patientId) == null) {
            System.out.println("Patient not found! Please enter a valid Patient ID.");
            return;
        }

        System.out.print("Enter Doctor ID: ");
        int doctorId = readInt();

        if (doctorService.findDoctor(doctorId) == null) {
            System.out.println("Doctor not found! Please enter a valid Doctor ID.");
            return;
        }

        String date = readDate();
        String time = readTime();

        System.out.print("Enter Appointment Reason: ");
        String reason = sc.nextLine();

        System.out.print("Enter Status (Scheduled/Pending): ");
        String status = sc.nextLine().trim();

        if (status.isEmpty()) {
            status = "Scheduled";
        }

        Appointment appointment = new Appointment(
                appointmentId,
                patientId,
                doctorId,
                date,
                time,
                reason,
                status
        );

        appointmentService.addAppointment(appointment);
    }

    // 2. View All Appointments
    private void viewAllAppointments() {

        System.out.println("\n---------- ALL APPOINTMENTS ----------");

        ArrayList<Appointment> appointments =
                appointmentService.getAllAppointments();

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        printAppointments(appointments);
    }

    // 3. Search Appointment
    private void searchAppointment() {

        System.out.println("\n---------- SEARCH APPOINTMENT ----------");

        System.out.print("Enter Appointment ID: ");
        int id = readInt();

        Appointment appointment =
                appointmentService.findAppointment(id);

        if (appointment == null) {
            System.out.println("Appointment not found!");
            return;
        }

        printAppointment(appointment);
    }

    // 4. Update Appointment
    private void updateAppointment() {

        System.out.println("\n---------- UPDATE APPOINTMENT ----------");

        System.out.print("Enter Appointment ID: ");
        int id = readInt();

        Appointment old = appointmentService.findAppointment(id);

        if (old == null) {
            System.out.println("Appointment not found!");
            return;
        }

        System.out.println("Current Appointment Details:");
        printAppointment(old);

        System.out.print("Enter New Patient ID: ");
        int patientId = readInt();

        if (patientService.findPatient(patientId) == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter New Doctor ID: ");
        int doctorId = readInt();

        if (doctorService.findDoctor(doctorId) == null) {
            System.out.println("Doctor not found!");
            return;
        }

        String date = readDate();
        String time = readTime();

        System.out.print("Enter New Reason: ");
        String reason = sc.nextLine();

        System.out.print("Enter New Status: ");
        String status = sc.nextLine().trim();

        if (status.isEmpty()) {
            status = old.getStatus();
        }

        boolean updated = appointmentService.updateAppointment(
                id, patientId, doctorId, date, time, reason, status
        );

        if (updated) {
            System.out.println("Appointment updated successfully!");
        } else {
            System.out.println("Appointment could not be updated.");
        }
    }

    // 5. Cancel Appointment
    private void cancelAppointment() {

        System.out.println("\n---------- CANCEL APPOINTMENT ----------");

        System.out.print("Enter Appointment ID: ");
        int id = readInt();

        Appointment appointment =
                appointmentService.findAppointment(id);

        if (appointment == null) {
            System.out.println("Appointment not found!");
            return;
        }

        boolean updated = appointmentService.updateAppointment(
                id,
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getReason(),
                "Cancelled"
        );

        if (updated) {
            System.out.println("Appointment cancelled successfully!");
        } else {
            System.out.println("Unable to cancel appointment.");
        }
    }

    // 6. View Appointments by Patient ID
    private void viewPatientAppointments() {

        System.out.println("\n---------- PATIENT APPOINTMENT HISTORY ----------");

        System.out.print("Enter Patient ID: ");
        int patientId = readInt();

        if (patientService.findPatient(patientId) == null) {
            System.out.println("Patient not found!");
            return;
        }

        ArrayList<Appointment> all =
                appointmentService.getAllAppointments();

        ArrayList<Appointment> patientAppointments = new ArrayList<>();

        for (Appointment appointment : all) {
            if (appointment.getPatientId() == patientId) {
                patientAppointments.add(appointment);
            }
        }

        if (patientAppointments.isEmpty()) {
            System.out.println("No appointments found for this patient.");
            return;
        }

        printAppointments(patientAppointments);
    }

    // 7. Delete Appointment Permanently
    private void deleteAppointment() {

        System.out.println("\n---------- DELETE APPOINTMENT ----------");

        System.out.print("Enter Appointment ID: ");
        int id = readInt();

        Appointment appointment =
                appointmentService.findAppointment(id);

        if (appointment == null) {
            System.out.println("Appointment not found!");
            return;
        }

        System.out.print("Are you sure you want to delete it? (yes/no): ");
        String confirm = sc.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes")) {

            boolean deleted = appointmentService.deleteAppointment(id);

            if (deleted) {
                System.out.println("Appointment deleted successfully!");
            } else {
                System.out.println("Appointment could not be deleted.");
            }

        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Display all appointments in table format
    private void printAppointments(ArrayList<Appointment> appointments) {

        System.out.printf(
                "%-8s %-10s %-10s %-13s %-12s %-22s %-12s%n",
                "App.ID", "Patient", "Doctor", "Date",
                "Time", "Reason", "Status"
        );

        System.out.println(
                "---------------------------------------------------------------------------------------------"
        );

        for (Appointment a : appointments) {
            System.out.printf(
                    "%-8d %-10d %-10d %-13s %-12s %-22s %-12s%n",
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getDoctorId(),
                    a.getDate(),
                    a.getTime(),
                    a.getReason(),
                    a.getStatus()
            );
        }
    }

    // Display one appointment
    private void printAppointment(Appointment a) {

        System.out.println("-----------------------------------");
        System.out.println("Appointment ID : " + a.getAppointmentId());
        System.out.println("Patient ID     : " + a.getPatientId());
        System.out.println("Doctor ID      : " + a.getDoctorId());
        System.out.println("Date           : " + a.getDate());
        System.out.println("Time           : " + a.getTime());
        System.out.println("Reason         : " + a.getReason());
        System.out.println("Status         : " + a.getStatus());
        System.out.println("-----------------------------------");
    }

    // Read integer safely
    private int readInt() {

        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    // Read date in yyyy-MM-dd format
    private String readDate() {

        while (true) {
            System.out.print("Enter Date (yyyy-MM-dd): ");
            String date = sc.nextLine().trim();

            try {
                return LocalDate.parse(date).toString();
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Example: 2026-11-20");
            }
        }
    }

    // Read time in HH:mm or HH:mm:ss format
    private String readTime() {

        while (true) {
            System.out.print("Enter Time (HH:mm): ");
            String time = sc.nextLine().trim();

            try {
                LocalTime parsedTime;

                if (time.length() == 5) {
                    parsedTime = LocalTime.parse(time);
                } else {
                    parsedTime = LocalTime.parse(time);
                }

                return parsedTime.toString().length() == 5
                        ? parsedTime.toString() + ":00"
                        : parsedTime.toString();

            } catch (DateTimeParseException e) {
                System.out.println("Invalid time! Example: 10:30 or 10:30:00");
            }
        }
    }
}
