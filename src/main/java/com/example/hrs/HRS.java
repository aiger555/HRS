package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

import static com.example.hrs.Doctor.*;
import static com.example.hrs.Patient.registerPatient;
import static com.example.hrs.Patient.signInPatient;

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

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(con, scanner);
            Doctor doctor = new Doctor(con, scanner);
            if (patient.getLoggedInPatientId() > 0) {
                patient.patientMenu();
            }

            // Call doctorMenu if a doctor is logged in
            if (doctor.getLoggedInDoctorId() > 0) {
                doctor.doctorMenu(doctor, con, scanner, doctor.getLoggedInDoctorId());
            }

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
                        //View patients
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View doctors
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 3:
                        //register doctor
                        registerDoctor(doctor, con, scanner);
                        break;
                    case 4:
                        // sign in doctor
                        signInDoctor(doctor, con, scanner);
                        int loggedInDoctorId = doctor.getLoggedInDoctorId();
                        if (loggedInDoctorId > 0) {
                            doctorMenu(doctor, con, scanner, loggedInDoctorId);
                        }
                        break;
                    case 5:
                        //register patient
                        registerPatient(patient, con, scanner);
                        break;
                    case 6:
                        // sign in patient
                        Patient.signInPatient(patient, con, scanner);
                        if (patient.getLoggedInPatientId() > 0) {
                            // Call patientMenu if a patient is logged in
                            patient.patientMenu();
                        }
                        break;
                    case 7:
                        //Exit
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
