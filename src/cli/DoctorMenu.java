package cli;

import java.util.ArrayList;
import java.util.Scanner;

import model.Doctor;
import service.DoctorService;

public class DoctorMenu {

    private Scanner sc;
    private DoctorService doctorService;

    public DoctorMenu(Scanner sc, DoctorService doctorService) {
        this.sc = sc;
        this.doctorService = doctorService;
    }

    public void showMenu() {

        int choice = 0;

        do {

            System.out.println("\n======================================");
            System.out.println("          DOCTOR MANAGEMENT");
            System.out.println("======================================");
            System.out.println("1. Add New Doctor");
            System.out.println("2. View All Doctors");
            System.out.println("3. Search Doctor by ID");
            System.out.println("4. Update Doctor Details");
            System.out.println("5. Delete Doctor");
            System.out.println("6. Back to Main Menu");
            System.out.println("======================================");

            choice = readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:
                        addDoctor();
                        break;

                    case 2:
                        viewDoctors();
                        break;

                    case 3:
                        searchDoctor();
                        break;

                    case 4:
                        updateDoctor();
                        break;

                    case 5:
                        deleteDoctor();
                        break;

                    case 6:
                        System.out.println("Returning to Main Menu...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (IllegalStateException e) {

                System.out.println("Database Error: " + e.getMessage());

            }

        } while (choice != 6);
    }

    // 1. ADD DOCTOR
    private void addDoctor() {

        System.out.println("\n------ ADD NEW DOCTOR ------");

        int id = doctorService.getNextDoctorId();

        System.out.println("Generated Doctor ID: " + id);

        System.out.print("Enter doctor name: ");
        String name = sc.nextLine();

        System.out.print("Enter specialization: ");
        String specialization = sc.nextLine();

        int experience = readInt("Enter experience (years): ");

        System.out.print("Enter contact: ");
        String contact = sc.nextLine();

        double fee = readDouble("Enter consultation fee: ");

        Doctor doctor = new Doctor(
            id,
            name,
            specialization,
            experience,
            contact,
            fee
        );

        doctorService.addDoctor(doctor);
    }

    // 2. VIEW ALL DOCTORS
    private void viewDoctors() {

        System.out.println("\n------ ALL DOCTORS ------");

        ArrayList<Doctor> doctors =
                doctorService.getAllDoctors();

        if (doctors.isEmpty()) {
            System.out.println("No doctors found!");
            return;
        }

        System.out.println("-------------------------------------------------------------");
        System.out.printf(
            "%-10s %-20s %-15s %-12s %-15s %-12s%n",
            "ID", "Name", "Specialization",
            "Experience", "Contact", "Fee"
        );
        System.out.println("-------------------------------------------------------------");

        for (Doctor d : doctors) {

            System.out.printf(
                "%-10d %-20s %-15s %-12d %-15s Rs.%-9.2f%n",
                d.getDoctorId(),
                d.getName(),
                d.getSpecialization(),
                d.getExperience(),
                d.getContact(),
                d.getConsultationFee()
            );
        }

        System.out.println("-------------------------------------------------------------");
    }

    // 3. SEARCH DOCTOR
    private void searchDoctor() {

        System.out.println("\n------ SEARCH DOCTOR ------");

        int id = readInt("Enter Doctor ID: ");

        Doctor doctor = doctorService.findDoctor(id);

        if (doctor == null) {

            System.out.println("Doctor not found!");

        } else {

            displayDoctor(doctor);
        }
    }

    // 4. UPDATE DOCTOR
    private void updateDoctor() {

        System.out.println("\n------ UPDATE DOCTOR ------");

        int id = readInt("Enter Doctor ID: ");

        Doctor doctor = doctorService.findDoctor(id);

        if (doctor == null) {

            System.out.println("Doctor not found!");
            return;
        }

        System.out.println("Current Doctor: " + doctor.getName());

        System.out.print("Enter new name: ");
        String name = sc.nextLine();

        System.out.print("Enter new specialization: ");
        String specialization = sc.nextLine();

        int experience = readInt("Enter new experience: ");

        System.out.print("Enter new contact: ");
        String contact = sc.nextLine();

        double fee = readDouble("Enter new consultation fee: ");

        boolean result = doctorService.updateDoctor(
            id,
            name,
            specialization,
            experience,
            contact,
            fee
        );

        if (result) {
            System.out.println("Doctor updated successfully!");
        } else {
            System.out.println("Doctor update failed!");
        }
    }

    // 5. DELETE DOCTOR
    private void deleteDoctor() {

        System.out.println("\n------ DELETE DOCTOR ------");

        int id = readInt("Enter Doctor ID: ");

        Doctor doctor = doctorService.findDoctor(id);

        if (doctor == null) {

            System.out.println("Doctor not found!");
            return;
        }

        System.out.println("Doctor Name: " + doctor.getName());

        System.out.print("Confirm deletion (Y/N): ");

        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {

            boolean result = doctorService.deleteDoctor(id);

            if (result) {
                System.out.println("Doctor deleted successfully!");
            } else {
                System.out.println("Doctor deletion failed!");
            }

        } else {

            System.out.println("Deletion cancelled!");
        }
    }

    // DISPLAY DOCTOR DETAILS
    private void displayDoctor(Doctor d) {

        System.out.println("\n==================================");
        System.out.println("         DOCTOR DETAILS");
        System.out.println("==================================");

        System.out.println("Doctor ID       : " + d.getDoctorId());
        System.out.println("Name            : " + d.getName());
        System.out.println("Specialization  : " + d.getSpecialization());
        System.out.println("Experience      : " + d.getExperience() + " years");
        System.out.println("Contact         : " + d.getContact());
        System.out.printf("Consultation Fee: Rs. %.2f%n",
                d.getConsultationFee());

        System.out.println("==================================");
    }

    // INTEGER INPUT VALIDATION
    private int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Please enter a valid integer!");
            }
        }
    }

    // DECIMAL INPUT VALIDATION
    private double readDouble(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Double.parseDouble(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println("Please enter a valid amount!");
            }
        }
    }
}