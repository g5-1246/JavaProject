
package cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.EmergencyCase;
import model.Patient;
import service.EmergencyService;
import service.PatientService;

public class EmergencyMenu {

    private Scanner sc;
    private EmergencyService emergencyService;
    private PatientService patientService;

    public EmergencyMenu(
            Scanner sc,
            EmergencyService emergencyService,
            PatientService patientService) {

        this.sc = sc;
        this.emergencyService = emergencyService;
        this.patientService = patientService;
    }

    public void showMenu() {

        int choice;

        do {
            System.out.println("\n========== EMERGENCY MANAGEMENT ==========");
            System.out.println("1. Register New Emergency Case");
            System.out.println("2. View All Emergency Cases");
            System.out.println("3. Search Case by ID");
            System.out.println("4. Update Case Status");
            System.out.println("5. Resolve Emergency Case");
            System.out.println("6. View Patient Emergency History");
            System.out.println("7. Back to Main Menu");

            System.out.print("Enter your choice: ");

            choice = readInt();

            try {
                switch (choice) {

                    case 1:
                        registerCase();
                        break;

                    case 2:
                        viewAllCases();
                        break;

                    case 3:
                        searchCase();
                        break;

                    case 4:
                        updateCaseStatus();
                        break;

                    case 5:
                        resolveCase();
                        break;

                    case 6:
                        viewPatientHistory();
                        break;

                    case 7:
                        System.out.println("Returning to Main Menu...");
                        break;

                    default:
                        System.out.println("Invalid choice! Try again.");
                }

            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 7);
    }

    // 1. Register New Emergency Case
    private void registerCase() {

        System.out.println("\n---------- REGISTER EMERGENCY CASE ----------");

        int caseId = emergencyService.getNextCaseId();

        System.out.println("Generated Case ID: " + caseId);

        System.out.print("Enter Patient ID: ");
        int patientId = readInt();

        Patient patient = patientService.findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        String patientName = patient.getName();

        System.out.println("Patient Name: " + patientName);

        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine().trim();

        if (contact.isEmpty()) {
            System.out.println("Contact number cannot be empty!");
            return;
        }

        System.out.print("Enter Emergency Location: ");
        String location = sc.nextLine().trim();

        if (location.isEmpty()) {
            System.out.println("Location cannot be empty!");
            return;
        }

        System.out.println("\nSelect Severity:");
        System.out.println("1. Critical");
        System.out.println("2. High");
        System.out.println("3. Medium");
        System.out.println("4. Low");

        System.out.print("Enter Severity Choice: ");

        int severityChoice = readInt();

        String severity;

        switch (severityChoice) {
            case 1:
                severity = "Critical";
                break;
            case 2:
                severity = "High";
                break;
            case 3:
                severity = "Medium";
                break;
            case 4:
                severity = "Low";
                break;
            default:
                System.out.println("Invalid severity choice!");
                return;
        }

        System.out.print("Enter Emergency Description: ");
        String description = sc.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("Description cannot be empty!");
            return;
        }

        String status = "Open";

        EmergencyCase emergencyCase = new EmergencyCase(
                caseId,
                patientId,
                patientName,
                contact,
                location,
                severity,
                description,
                status,
                LocalDateTime.now()
        );

        emergencyService.addCase(emergencyCase);

        System.out.println("\nEmergency case registered successfully!");

        printCase(emergencyCase);
    }

    // 2. View All Emergency Cases
    private void viewAllCases() {

        System.out.println("\n---------- ALL EMERGENCY CASES ----------");

        List<EmergencyCase> cases = emergencyService.getAllCases();

        if (cases.isEmpty()) {
            System.out.println("No emergency cases found.");
            return;
        }

        for (EmergencyCase c : cases) {
            printCase(c);
        }
    }

    // 3. Search Case by ID
    private void searchCase() {

        System.out.println("\n---------- SEARCH EMERGENCY CASE ----------");

        System.out.print("Enter Case ID: ");
        int id = readInt();

        EmergencyCase emergencyCase = findCase(id);

        if (emergencyCase == null) {
            System.out.println("Emergency case not found!");
            return;
        }

        printCase(emergencyCase);
    }

    // 4. Update Case Status
    private void updateCaseStatus() {

        System.out.println("\n---------- UPDATE CASE STATUS ----------");

        System.out.print("Enter Case ID: ");
        int id = readInt();

        EmergencyCase emergencyCase = findCase(id);

        if (emergencyCase == null) {
            System.out.println("Emergency case not found!");
            return;
        }

        System.out.println("Current Status: " + emergencyCase.getStatus());

        System.out.println("\nSelect New Status:");
        System.out.println("1. Open");
        System.out.println("2. In Progress");
        System.out.println("3. Resolved");

        System.out.print("Enter Choice: ");

        int choice = readInt();

        String status;

        switch (choice) {
            case 1:
                status = "Open";
                break;
            case 2:
                status = "In Progress";
                break;
            case 3:
                status = "Resolved";
                break;
            default:
                System.out.println("Invalid status choice!");
                return;
        }

        boolean updated = emergencyService.updateStatus(id, status);

        if (updated) {
            System.out.println("Emergency status updated successfully!");
        } else {
            System.out.println("Unable to update status.");
        }
    }

    // 5. Resolve Emergency Case
    private void resolveCase() {

        System.out.println("\n---------- RESOLVE EMERGENCY CASE ----------");

        System.out.print("Enter Case ID: ");
        int id = readInt();

        EmergencyCase emergencyCase = findCase(id);

        if (emergencyCase == null) {
            System.out.println("Emergency case not found!");
            return;
        }

        if (emergencyCase.getStatus().equalsIgnoreCase("Resolved")) {
            System.out.println("This case is already resolved.");
            return;
        }

        boolean updated = emergencyService.updateStatus(id, "Resolved");

        if (updated) {
            System.out.println("Emergency case resolved successfully!");
        } else {
            System.out.println("Unable to resolve emergency case.");
        }
    }

    // 6. View Patient Emergency History
    private void viewPatientHistory() {

        System.out.println("\n---------- PATIENT EMERGENCY HISTORY ----------");

        System.out.print("Enter Patient ID: ");
        int patientId = readInt();

        Patient patient = patientService.findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        List<EmergencyCase> patientCases = new ArrayList<>();

        for (EmergencyCase c : emergencyService.getAllCases()) {

            if (c.getPatientId() == patientId) {
                patientCases.add(c);
            }
        }

        if (patientCases.isEmpty()) {
            System.out.println("No emergency history found for this patient.");
            return;
        }

        System.out.println("Patient Name: " + patient.getName());

        for (EmergencyCase c : patientCases) {
            printCase(c);
        }
    }

    // Find Case using ID
    private EmergencyCase findCase(int id) {

        for (EmergencyCase c : emergencyService.getAllCases()) {

            if (c.getCaseId() == id) {
                return c;
            }
        }

        return null;
    }

    // Print Emergency Case
    private void printCase(EmergencyCase c) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

        System.out.println("\n========================================");
        System.out.println("          EMERGENCY CASE DETAILS");
        System.out.println("========================================");

        System.out.println("Case ID       : " + c.getCaseId());
        System.out.println("Patient ID    : " + c.getPatientId());
        System.out.println("Patient Name  : " + c.getPatientName());
        System.out.println("Contact       : " + c.getContact());
        System.out.println("Location      : " + c.getLocation());
        System.out.println("Severity      : " + c.getSeverity());
        System.out.println("Description   : " + c.getDescription());
        System.out.println("Status        : " + c.getStatus());

        System.out.println("Reported At   : " +
                c.getReportedAt().format(formatter));

        System.out.println("========================================");
    }

    // Read Integer
    private int readInt() {

        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
