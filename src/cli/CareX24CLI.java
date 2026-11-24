
package cli;

import java.util.Scanner;

import service.PatientService;
import service.DoctorService;
import service.AppointmentService;
import service.EmergencyService;
import service.BillingService;

public class CareX24CLI {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Initialize Services
        PatientService patientService = new PatientService();
        DoctorService doctorService = new DoctorService();
        AppointmentService appointmentService = new AppointmentService();
        EmergencyService emergencyService = new EmergencyService();
        BillingService billingService = new BillingService();

        // Initialize Menus
        PatientMenu patientMenu =
                new PatientMenu(sc, patientService);

        DoctorMenu doctorMenu =
                new DoctorMenu(sc, doctorService);

        AppointmentMenu appointmentMenu =
                new AppointmentMenu(
                        sc,
                        appointmentService,
                        patientService,
                        doctorService
                );

        EmergencyMenu emergencyMenu =
                new EmergencyMenu(
                        sc,
                        emergencyService,
                        patientService
                );

        BillingMenu billingMenu =
                new BillingMenu(
                        sc,
                        billingService,
                        patientService,
                        doctorService
                );

        int choice;

        do {

            System.out.println("\n======================================");
            System.out.println("              CAREX24");
            System.out.println("     HEALTHCARE MANAGEMENT SYSTEM");
            System.out.println("======================================");

            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Emergency Management");
            System.out.println("5. Billing Management");
            System.out.println("6. Exit");

            System.out.print("\nEnter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                sc.next();
            }

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    patientMenu.showMenu();
                    break;

                case 2:
                    doctorMenu.showMenu();
                    break;

                case 3:
                    appointmentMenu.showMenu();
                    break;

                case 4:
                    emergencyMenu.showMenu();
                    break;

                case 5:
                    billingMenu.showMenu();
                    break;

                case 6:
                    System.out.println(
                            "\nThank you for using CareX24!"
                    );
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

        } while (choice != 6);

        sc.close();
    }
}
