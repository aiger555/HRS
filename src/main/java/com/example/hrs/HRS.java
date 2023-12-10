package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

public class HRS {
    private static final String url = "jdbc:postgresql://localhost/hrs";
    private static final String username = "postgres";
    private static final String password = "postgres";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            Patient patient = new Patient(con, scanner);
            Doctor doctor = new Doctor(con, scanner);
            User user = new User(con, scanner);

            while (true) {
                System.out.println("Hospital Reservation System (HRS)");
                System.out.println("1. View patients");
                System.out.println("2. View doctors");
                System.out.println("3. Register as Doctor");
                System.out.println("4. Sign In as Doctor");
                System.out.println("5. Register as Patient");
                System.out.println("6. Sign In as Patient");
                System.out.println("7. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        // View patients
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View doctors
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 3:
                        // Register doctor
                        user.registerDoctor(scanner);
                        break;
                    case 4:
                        // Sign in doctor and call doctorMenu
                        System.out.println("Enter doctor ID to sign in: ");
                        int doctorId = scanner.nextInt();
                        user.signInAsDoctor(doctorId, scanner);
                        break;
                    case 5:
                        // Register patient
                        user.registerPatient(scanner);
                        break;
                    case 6:
                        // Sign in patient and call patientMenu
                        System.out.println("Enter patient ID to sign in: ");
                        int patientId = scanner.nextInt();
                        user.signInAsPatient(patientId, scanner);
                        break;
                    case 7:
                        // Exit
                        return;
                    default:
                        System.out.println("Enter valid choice!!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
