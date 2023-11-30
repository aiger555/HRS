package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

import static com.example.hrs.Doctor.bookAppointmentD;
import static com.example.hrs.Patient.*;

public class User {
    private static Connection con;
    private static Scanner scanner;

    private Patient patient;

    private Doctor doctor;

    public User(Connection con, Scanner scanner) {
        this.con = con;
        this.scanner = scanner;
    }

    public boolean getUserById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Returns true if a user with the given ID exists, false otherwise

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String performUserSpecificAction(int userId) {
        String query = "SELECT role FROM users WHERE id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if ("patient".equalsIgnoreCase(role) && "PATIENT".equalsIgnoreCase(role)) {
                    // Call patientMenu method
                    patientMenu(userId);
                } else if ("doctor".equalsIgnoreCase(role) && "DOCTOR".equalsIgnoreCase(role)) {
                    // Call doctorMenu method
                    doctorMenu(userId, con, scanner);
                } else {
                    System.out.println("Invalid user role");
                }
            } else {
                System.out.println("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (isPatient(userId, con)) {
            return "PATIENT";
        } else if (isDoctor(userId, con)) {
            return "DOCTOR";
        } else {
            return "unknown"; // Or handle the case where the user has neither patient nor doctor role
        }
    }

    public boolean isPatient(int userId, Connection con) {
        String query = "SELECT * FROM patients WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = User.con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();  // Return true if a patient with the given userId is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDoctor(int userId, Connection con) {
        String query = "SELECT * FROM doctors WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = User.con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();  // Return true if a doctor with the given userId is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void registerPatient() {
        System.out.println("Patient Registration:");
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        System.out.println("Enter role: ");
        String role = scanner.next();
        System.out.println("Enter name: ");
        String name = scanner.next();


        try {
            String query = "INSERT INTO users(username, password, role, name) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            preparedStatement.setString(4, name);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient registered successfully!");
            } else {
                System.out.println("Failed to register patient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registerDoctor() {
        System.out.println("Doctor Registration:");
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        System.out.println("Enter role: ");
        String role = scanner.next();
        System.out.println("Enter name: ");
        String name = scanner.next();
        System.out.println("Enter specialization: ");
        String specialization = scanner.next();
        System.out.println("Enter qualification: ");
        String qualification = scanner.next();
        System.out.println("Enter room: ");
        int room = scanner.nextInt();

        try {
            String query = "INSERT INTO users(username, password, role, name, specialization, qualification, room) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, specialization);
            preparedStatement.setString(6, qualification);
            preparedStatement.setInt(7, room);


            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Doctor registered successfully!");
            } else {
                System.out.println("Failed to register doctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void signIn(int userId, Connection con, Scanner scanner) {
        try {
            String query = "SELECT role FROM users WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if ("PATIENT".equals(role) || "patient".equals(role)) {
                    patientMenu(userId);
                } else if ("DOCTOR".equals(role) || "doctor".equals(role)) {
                    doctorMenu(userId, con, scanner);
                } else {
                    System.out.println("Invalid role for user ID " + userId);
                }
            } else {
                System.out.println("User not found with ID " + userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void patientMenu(int userId) {
        while (true) {
            printPatientMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    //View doctors
                    viewDoctors(userId);
                    break;
                case 2:
                    //Add doctor
                    Doctor.addDoctor();
                    System.out.println();
                    break;
                case 3:
                    //Update doctor
                    System.out.println("Enter doctor id to update: ");
                    int doctorid = scanner.nextInt();
                    Doctor.updateDoctor(doctorid);
                    System.out.println();
                    break;
                case 4:
                    // Delete doctor
                    System.out.println("Enter doctor id to delete: ");
                    int docID = scanner.nextInt();
                    Doctor.deleteDoctor(docID);
                    System.out.println();
                    break;
                case 5:
                    //view booked appointments
                    viewBookedAppointments(con,userId);
                    break;
                case 6:
                    //book appointment
                    bookAppointmentP(userId);
                    break;
                case 7:
                    // Add appointment
                    System.out.println("Enter patient id to add: ");
                    int patientId = scanner.nextInt();
                    System.out.println("Enter doctor id to add: ");
                    int doctorId = scanner.nextInt();
                    System.out.println("Enter appointment date to add: ");
                    Date date = Date.valueOf(scanner.next());
                    Appointment.addAppointment(patientId, doctorId, date, con);
                    break;
                case 8:
                    //update appointment
                    System.out.println("Enter appointment id to update: ");
                    int appointmentIdToUpdate = scanner.nextInt();
                    System.out.println("Enter appointment date to update: ");
                    Date dates = Date.valueOf(scanner.next());
                    Appointment.updateAppointment(con,appointmentIdToUpdate,dates);
                    break;
                case 9:
                    //delete appointment
                    System.out.println("Enter appointment id to delete: ");
                    int appointmentIdToDelete = scanner.nextInt();
                    Appointment.deleteAppointment(con,appointmentIdToDelete);
                    break;
                case 10:
                    //exit
                    return;
                default:
                    System.out.println("Enter valid choice!!");
            }
        }
    }

    private static void printPatientMenu() {
        System.out.println("Patient Menu:");
        System.out.println("1. View Doctors");
        System.out.println("2. Add doctor");
        System.out.println("3. Update doctor");
        System.out.println("4. Delete doctor");
        System.out.println("5. view booked appointments");
        System.out.println("6. book appointment");
        System.out.println("7. Add appointment");
        System.out.println("8. Update appointment");
        System.out.println("9. Delete appointment");
        System.out.println("10. Exit");
    }


    private static void printDoctorMenu() {
        System.out.println("Doctor Menu:");
        System.out.println("1. View Patients");
        System.out.println("2. view Patients details");
        System.out.println("3. view booked appointments");
        System.out.println("4. book appointment");
        System.out.println("5. Add patient");
        System.out.println("6. Update patient");
        System.out.println("7. Delete patient");
        System.out.println("8. Add appointment");
        System.out.println("9. Update appointment");
        System.out.println("10. Delete appointment");
        System.out.println("11. Exit");
    }

    public static void doctorMenu(int userId, Connection con, Scanner scanner) {
        while (true) {
            printDoctorMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    //view Patients
                    Doctor.viewPatients(con, userId);
                    break;
                case 2:
                    //view Patients details
                    System.out.println("Enter patient id to view details: ");
                    int patientID = scanner.nextInt();
                    Doctor.viewPatientDetails(patientID, userId);
                    break;
                case 3:
                    //view booked appointments
                    Doctor.viewBookedAppointments(con, userId);
                    break;
                case 4:
                    //book appointment
                    bookAppointmentD(userId, con, scanner);
                    break;
                case 5:
                    //Add patient
                    Patient.addPatient();
                    System.out.println();
                    break;
                case 6:
                    //Update patient
                    System.out.println("Enter patient id to update: ");
                    int patientid = scanner.nextInt();
                    Patient.updatePatient(patientid);
                    System.out.println();
                    break;
                case 7:
                    // Delete patient
                    System.out.println("Enter patient id to delete: ");
                    int patID = scanner.nextInt();
                    Patient.deletePatient(patID);
                    System.out.println();
                    break;
                case 8:
                    // Add appointment
                    System.out.println("Enter patient id to add: ");
                    int patientId = scanner.nextInt();
                    System.out.println("Enter doctor id to add: ");
                    int doctorId = scanner.nextInt();
                    System.out.println("Enter appointment date to add: ");
                    Date date = Date.valueOf(scanner.next());
                    Appointment.addAppointment(patientId, doctorId, date, con);
                    break;
                case 9:
                    // Update appointment
                    System.out.println("Enter appointment id to update: ");
                    int appointmentIdToUpdate = scanner.nextInt();
                    System.out.println("Enter appointment date to update: ");
                    Date dates = Date.valueOf(scanner.next());
                    Appointment.updateAppointment(con,appointmentIdToUpdate, dates);
                    break;
                case 10:
                    // Delete appointment
                    System.out.println("Enter appointment id to delete: ");
                    int appointmentIdToDelete = scanner.nextInt();
                    Appointment.deleteAppointment(con,appointmentIdToDelete);
                    break;
                case 11:
                    return;
                default:
                    System.out.println("Enter valid choice!!");
            }
        }
    }

}
