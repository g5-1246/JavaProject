
package cli;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Scanner;

import model.Bill;
import model.Patient;
import model.Doctor;
import service.BillingService;
import service.PatientService;
import service.DoctorService;
import java.time.format.DateTimeFormatter;
public class BillingMenu {

    private Scanner sc;
    private BillingService billingService;
    private PatientService patientService;
    private DoctorService doctorService;

    public BillingMenu(
            Scanner sc,
            BillingService billingService,
            PatientService patientService,
            DoctorService doctorService) {

        this.sc = sc;
        this.billingService = billingService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public void showMenu() {

        int choice;

        do {
            System.out.println("\n========== BILLING MANAGEMENT ==========");
            System.out.println("1. Generate New Bill");
            System.out.println("2. View All Bills");
            System.out.println("3. Search Bill by ID");
            System.out.println("4. View Patient Bills");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = readInt();

            try {
                switch (choice) {

                    case 1:
                        generateBill();
                        break;

                    case 2:
                        viewAllBills();
                        break;

                    case 3:
                        searchBill();
                        break;

                    case 4:
                        viewPatientBills();
                        break;

                    case 5:
                        System.out.println("Returning to Main Menu...");
                        break;

                    default:
                        System.out.println("Invalid choice! Try again.");
                }

            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 5);
    }

    // 1. Generate New Bill
    private void generateBill() {

        System.out.println("\n---------- GENERATE NEW BILL ----------");

        int billId = billingService.getNextBillId();

        System.out.println("Generated Bill ID: " + billId);

        System.out.print("Enter Patient ID: ");
        int patientId = readInt();

        Patient patient = patientService.findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        System.out.print("Enter Doctor ID: ");
        int doctorId = readInt();

        Doctor doctor = doctorService.findDoctor(doctorId);

        if (doctor == null) {
            System.out.println("Doctor not found!");
            return;
        }

        String patientName = patient.getName();
        String doctorName = doctor.getName();

        double consultationFee = doctor.getConsultationFee();

        System.out.println("\nPatient Name       : " + patientName);
        System.out.println("Doctor Name        : Dr. " + doctorName);
        System.out.printf("Consultation Fee   : Rs. %.2f%n", consultationFee);

        System.out.print("Enter Other Charges: Rs. ");
        double otherCharges = readDouble();

        if (otherCharges < 0) {
            System.out.println("Other charges cannot be negative!");
            return;
        }

        double total = consultationFee + otherCharges;

        System.out.printf("Total Amount       : Rs. %.2f%n", total);

        System.out.print("Enter Amount Paid  : Rs. ");
        double amountPaid = readDouble();

        if (amountPaid < 0 || amountPaid > total) {
            System.out.println("Invalid payment amount!");
            return;
        }

        System.out.println("\nPayment Methods:");
        System.out.println("1. Cash");
        System.out.println("2. UPI");
        System.out.println("3. Card");
        System.out.println("4. Net Banking");

        System.out.print("Select Payment Method: ");
        int paymentChoice = readInt();

        String paymentMethod;

        switch (paymentChoice) {
            case 1:
                paymentMethod = "Cash";
                break;
            case 2:
                paymentMethod = "UPI";
                break;
            case 3:
                paymentMethod = "Card";
                break;
            case 4:
                paymentMethod = "Net Banking";
                break;
            default:
                System.out.println("Invalid payment method!");
                return;
        }

        String paymentStatus;

        if (amountPaid == total) {
            paymentStatus = "Paid";
        } else if (amountPaid > 0) {
            paymentStatus = "Partial";
        } else {
            paymentStatus = "Unpaid";
        }

        Bill bill = new Bill(
                billId,
                patientId,
                patientName,
                doctorId,
                doctorName,
                consultationFee,
                otherCharges,
                amountPaid,
                paymentMethod,
                paymentStatus,
                LocalDateTime.now()
        );

        billingService.addBill(bill);

        System.out.println("\nBill generated successfully!");

        printBill(bill);
    }

    // 2. View All Bills
    private void viewAllBills() {

        System.out.println("\n---------- ALL BILLS ----------");

        List<Bill> bills = billingService.getAllBills();

        if (bills.isEmpty()) {
            System.out.println("No billing records found.");
            return;
        }

        for (Bill bill : bills) {
            printBill(bill);
        }
    }

    // 3. Search Bill
    private void searchBill() {

        System.out.println("\n---------- SEARCH BILL ----------");

        System.out.print("Enter Bill ID: ");
        int id = readInt();

        for (Bill bill : billingService.getAllBills()) {

            if (bill.getBillId() == id) {
                printBill(bill);
                return;
            }
        }

        System.out.println("Bill not found!");
    }

    // 4. View Patient Bills
    private void viewPatientBills() {

        System.out.println("\n---------- PATIENT BILL HISTORY ----------");

        System.out.print("Enter Patient ID: ");
        int patientId = readInt();

        Patient patient = patientService.findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found!");
            return;
        }

        List<Bill> patientBills = new ArrayList<>();

        for (Bill bill : billingService.getAllBills()) {

            if (bill.getPatientId() == patientId) {
                patientBills.add(bill);
            }
        }

        if (patientBills.isEmpty()) {
            System.out.println("No bills found for this patient.");
            return;
        }

        System.out.println("Patient Name: " + patient.getName());

        for (Bill bill : patientBills) {
            printBill(bill);
        }
    }

    // Print Bill
    private void printBill(Bill bill) {

        System.out.println("\n========================================");
        System.out.println("       CareX24 Hospital Management System");
        System.out.println("              BILL RECEIPT");
        System.out.println("========================================");

        System.out.println("Bill ID          : " + bill.getBillId());
        System.out.println("Patient ID       : " + bill.getPatientId());
        System.out.println("Patient Name     : " + bill.getPatientName());
        System.out.println("Doctor ID        : " + bill.getDoctorId());
        String doctorName = bill.getDoctorName();

        if (!doctorName.startsWith("Dr. ")) {
            doctorName = "Dr. " + doctorName;
        }
        System.out.println("Doctor Name      : " + doctorName);

        System.out.printf("Consultation Fee : Rs. %.2f%n",
                bill.getConsultationFee());

        System.out.printf("Other Charges    : Rs. %.2f%n",
                bill.getOtherCharges());

        System.out.println("----------------------------------------");

        System.out.printf("Total Amount     : Rs. %.2f%n",
                bill.getTotal());

        System.out.printf("Amount Paid      : Rs. %.2f%n",
                bill.getAmountPaid());

        System.out.printf("Balance Due      : Rs. %.2f%n",
                bill.getBalance());

        System.out.println("Payment Method   : " + bill.getPaymentMethod());
        System.out.println("Payment Status   : " + bill.getPaymentStatus());
        DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

        System.out.println("Created At       : " +bill.getCreatedAt().format(formatter));

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

    // Read Double
    private double readDouble() {

        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid amount: ");
            }
        }
    }
}
