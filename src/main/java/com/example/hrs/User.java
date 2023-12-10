package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

import static com.example.hrs.Doctor.bookAppointmentD;
import static com.example.hrs.Patient.*;

public class User {
    public static Connection con;
    public static Scanner scanner;

    private Patient patient;

    private Doctor doctor;

    public User(Connection con, Scanner scanner) {
        this.con = con;
        scanner = new Scanner(System.in);
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
                    patientMenu(userId, con, scanner);
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
        String query = "SELECT * FROM patients WHERE id = ?";
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
        String query = "SELECT * FROM doctors WHERE id = ?";
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
    public static void registerPatient(Scanner scanner) {
        System.out.println("Patient Registration:");
        System.out.println("Enter username: ");
        String username = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner.next();
        System.out.println("Enter role: ");
        String role = scanner.next();
        System.out.println("Enter name: ");
        String name = scanner.next();
        System.out.println("Enter surname: ");
        String surname = scanner.next();
        System.out.println("Enter specialization: ");
        String specialization = scanner.next();
        System.out.println("Enter qualification: ");
        String qualification = scanner.next();
        System.out.println("Enter phone number: ");
        int phone_number = scanner.nextInt();
        System.out.println("Enter reason for visiting: ");
        String reason = scanner.next();
        System.out.println("Enter room: ");
        int room = scanner.nextInt();

        try {
            // Insert into the "users" table
            String userQuery = "INSERT INTO users(username, password, role, name, specialization, qualification, room) VALUES (?, ?, ?, ?, NULL, NULL, ?)";
            PreparedStatement userStatement = con.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, username);
            userStatement.setString(2, password);
            userStatement.setString(3, role);
            userStatement.setString(4, name);
//            userStatement.setString(5, specialization);
//            userStatement.setString(6, qualification);
            userStatement.setInt(5, room);

            int userRowsAffected = userStatement.executeUpdate();

            if (userRowsAffected > 0) {
                // Retrieve the generated user ID
                ResultSet userGeneratedKeys = userStatement.getGeneratedKeys();
                int userId = -1;
                if (userGeneratedKeys.next()) {
                    userId = userGeneratedKeys.getInt(1);

                    // Insert into the "patients" table
                    String patientQuery = "INSERT INTO patients (name, surname, phone_number, reason, room) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement patientStatement = con.prepareStatement(patientQuery);
                    patientStatement.setString(1,name);
                    patientStatement.setString(2, surname);
                    patientStatement.setInt(3, phone_number);
                    patientStatement.setString(4, reason);
                    patientStatement.setInt(5, room);

                    int patientRowsAffected = patientStatement.executeUpdate();

                    if (patientRowsAffected > 0) {
                        System.out.println("Patient registered successfully!");
                    } else {
                        System.out.println("Failed to register patient");
                    }
                } else {
                    System.out.println("Failed to retrieve the generated user ID");
                }
            } else {
                System.out.println("Failed to register patient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void registerDoctor(Scanner scanner) {
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
                // Insert user
                String insertUserQuery = "INSERT INTO users(username, password, role, name, specialization, qualification, room) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement userStatement = con.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
                userStatement.setString(1, username);
                userStatement.setString(2, password);
                userStatement.setString(3, role);
                userStatement.setString(4, name);
                userStatement.setString(5, specialization);
                userStatement.setString(6, qualification);
                userStatement.setInt(7, room);

                int affectedRows = userStatement.executeUpdate();

                // Check if user insertion was successful
                if (affectedRows > 0) {
                    // Get the generated user ID
                    try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            String dname = generatedKeys.getString(1);
                            int dId = generatedKeys.getInt(1);


                            // Insert doctor with the obtained user ID
                            String insertDoctorQuery = "INSERT INTO doctors(id, name, specialization, qualification, room) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement doctorStatement = con.prepareStatement(insertDoctorQuery);
                            doctorStatement.setInt(1, dId);
                            doctorStatement.setString(2, dname);
                            doctorStatement.setString(3, specialization);
                            doctorStatement.setString(4, qualification);
                            doctorStatement.setInt(5, room);

                            int doctorRowsAffected = doctorStatement.executeUpdate();

                            // Check doctor insertion result
                            if (doctorRowsAffected > 0) {
                                System.out.println("Doctor registered successfully!");
                            } else {
                                System.out.println("Failed to register doctor");
                            }
                        } else {
                            System.out.println("Failed to get user ID.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Failed to register doctor");
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }


    public static void signInAsPatient(int userId, Scanner scanner) {
        try {
            String query = "SELECT role FROM users WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if ("PATIENT".equalsIgnoreCase(role)) {
                    patientMenu(userId, con, scanner);
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

    public static void signInAsDoctor(int userId, Scanner scanner) {
        try {
            String query = "SELECT role FROM users WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if ("DOCTOR".equalsIgnoreCase(role)) {
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

    public static void patientMenu(int userId, Connection con, Scanner scanner) {
        while (true) {
            printPatientMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    //View doctors
                    Patient.viewDoctors(userId);
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
                    Appointment.addAppointment();
                    break;
                case 8:
                    //update appointment
                    System.out.println("Enter appointment id to update: ");
                    int appointmentIdToUpdate = scanner.nextInt();
                    System.out.println("Enter appointment date to update: ");
                    Date dates = Date.valueOf(scanner.next());
                    Appointment.updateAppointment(appointmentIdToUpdate, dates);
                    break;
                case 9:
                    //delete appointment
                    System.out.println("Enter appointment id to delete: ");
                    int appointmentIdToDelete = scanner.nextInt();
                    Appointment.deleteAppointment(appointmentIdToDelete);
                    break;
                case 10:
                    //exit
                    return;
                default:
                    System.out.println("Enter valid choice!!");
            }
        }
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
                    Doctor.viewPatients(userId);
                    break;
                case 2:
                    //view Patients details
                    System.out.println("Enter patient id to view details: ");
                    int patientID = scanner.nextInt();
                    Doctor.viewPatientDetails(patientID);
                    break;
                case 3:
                    //view booked appointments
                    Doctor.viewBookedAppointments(userId);
                    break;
                case 4:
                    //book appointment
                    bookAppointmentD(userId, scanner);
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
                    Patient.updatePatient(patientid, scanner);
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
                    Appointment.addAppointment();
                    break;
                case 9:
                    // Update appointment
                    System.out.println("Enter appointment id to update: ");
                    int appointmentIdToUpdate = scanner.nextInt();
                    System.out.println("Enter appointment date to update: ");
                    Date dates = Date.valueOf(scanner.next());
                    Appointment.updateAppointment(appointmentIdToUpdate, dates);
                    break;
                case 10:
                    // Delete appointment
                    System.out.println("Enter appointment id to delete: ");
                    int appointmentIdToDelete = scanner.nextInt();
                    Appointment.deleteAppointment(appointmentIdToDelete);
                    break;
                case 11:
                    return;
                default:
                    System.out.println("Enter valid choice!!");
            }
        }
    }

}
