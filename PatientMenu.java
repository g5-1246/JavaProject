package cli;

import java.util.Scanner;
import model.Patient;
import service.PatientService;

public class PatientMenu {

    private Scanner sc;
    private PatientService patientService;

    public PatientMenu(Scanner sc, PatientService patientService) {
        this.sc = sc;
        this.patientService = patientService;
    }

    public void showMenu() {

        int choice = 0;

        do {
            System.out.println("\n====================================");
            System.out.println("        PATIENT MANAGEMENT");
            System.out.println("====================================");
            System.out.println("1. Add New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Back to Main Menu");
            System.out.println("====================================");

            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        addPatient();
                        break;

                    case 2:
                        patientService.viewPatients();
                        break;

                    case 3:
                        searchPatient();
                        break;

                    case 4:
                        updatePatient();
                        break;

                    case 5:
                        deletePatient();
                        break;

                    case 6:
                        System.out.println("Returning to Main Menu...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            } catch (IllegalStateException e) {
                System.out.println("Database Error: " + e.getMessage());
            }

        } while (choice != 6);
    }

    private void addPatient() {

        System.out.println("\n------ ADD NEW PATIENT ------");

        int id = patientService.getNextPatientId();

        System.out.println("Generated Patient ID: " + id);

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter contact: ");
        String contact = sc.nextLine();

        System.out.print("Enter address: ");
        String address = sc.nextLine();

        Patient patient = new Patient(
            id, name, age, gender, contact, address
        );

        patientService.addPatient(patient);
    }

    private void searchPatient() {

        System.out.print("Enter Patient ID: ");
        int id = Integer.parseInt(sc.nextLine());

        Patient p = patientService.findPatient(id);

        if (p == null) {
            System.out.println("Patient not found!");
            return;
        }

        displayPatient(p);
    }

    private void updatePatient() {

        System.out.print("Enter Patient ID: ");
        int id = Integer.parseInt(sc.nextLine());

        Patient p = patientService.findPatient(id);

        if (p == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter new name: ");
        String name = sc.nextLine();

        System.out.print("Enter new age: ");
        int age = Integer.parseInt(sc.nextLine());

        System.out.print("Enter new gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter new contact: ");
        String contact = sc.nextLine();

        System.out.print("Enter new address: ");
        String address = sc.nextLine();

        boolean result = patientService.updatePatient(
            id, name, age, gender, contact, address
        );

        System.out.println(
            result ? "Patient updated successfully!"
                   : "Patient update failed!"
        );
    }

    private void deletePatient() {

        System.out.print("Enter Patient ID: ");
        int id = Integer.parseInt(sc.nextLine());

        Patient p = patientService.findPatient(id);

        if (p == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Confirm deletion (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {

            boolean result = patientService.deletePatient(id);

            System.out.println(
                result ? "Patient deleted successfully!"
                       : "Patient deletion failed!"
            );

        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void displayPatient(Patient p) {

        System.out.println("\n--------------------------------");
        System.out.println("Patient ID : " + p.getPatientId());
        System.out.println("Name       : " + p.getName());
        System.out.println("Age        : " + p.getAge());
        System.out.println("Gender     : " + p.getGender());
        System.out.println("Contact    : " + p.getContact());
        System.out.println("Address    : " + p.getAddress());
        System.out.println("--------------------------------");
    }
}