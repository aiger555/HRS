package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

public class Doctor {
    private static Connection con;
    private static Scanner scanner;
    static Patient patient;

    private static int loggedInDoctorId;
    private static Appointment appointment;


    public Doctor(Connection con, Scanner scanner) {
        this.con = con;
        this.scanner = scanner;
    }

    public void addDoctor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter doctor name: ");
        String doctorName = scanner.next();
        System.out.println("Enter specialization: ");
        String specialization = scanner.next();
        System.out.println("Enter qualification: ");
        String qualification = scanner.next();
        System.out.println("Enter doctor's room: ");
        int room = scanner.nextInt();

        try {
            String query = "INSERT INTO doctors(doctor, specialization, qualification, room) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, doctorName);
            preparedStatement.setString(2, specialization);
            preparedStatement.setString(3, qualification);
            preparedStatement.setInt(4, room);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Doctor added successfully!");
            } else {
                System.out.println("Failed to add Doctor");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor(int id) {
        if (getDoctorById(id)) {
            try {
                System.out.println("Enter new doctor name: ");
                String doctorName = scanner.next();
                System.out.println("Enter new specialization: ");
                String specialization = scanner.next();
                System.out.println("Enter new qualification: ");
                String qualification = scanner.next();
                System.out.println("Enter new doctor's room: ");
                int room = scanner.nextInt();

                String query = "UPDATE doctors SET doctor=?, specialization=?, qualification=?, room=? WHERE id=?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, doctorName);
                preparedStatement.setString(2, specialization);
                preparedStatement.setString(3, qualification);
                preparedStatement.setInt(4, room);
                preparedStatement.setInt(5, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Doctor updated successfully!");
                } else {
                    System.out.println("Failed to update doctor");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Doctor with ID " + id + " not found.");
        }
    }

    public void deleteDoctor(int id) {
        if (getDoctorById(id)) {
            try {
                String query = "DELETE FROM doctors WHERE id=?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Doctor deleted successfully!");
                } else {
                    System.out.println("Failed to delete doctor");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Doctor with ID " + id + " not found.");
        }
    }

    public void viewDoctor() {
        String query = "select * from doctors";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+-----+----------------+----------------+---------------+-------+");
            System.out.println("| ID  | Doctor         | Specialization | Qualification | Room  |");
            System.out.println("+-----+----------------+----------------+---------------+-------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String doctor = resultSet.getString("doctor");
                String spec = resultSet.getString("specialization");
                String qual = resultSet.getString("qualification");
                int room = resultSet.getInt("room");
                System.out.printf("| %-5s | %-16s | %-16s | %-15s | %-7s |\n", id, doctor, spec, qual, room);
                System.out.println("+-----+----------------+----------------+---------------+-------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "select * from doctors where id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static void registerDoctor(Doctor doctor, Connection con, Scanner scanner) {
        System.out.println("Enter doctor username: ");
        String username = scanner.next();
        System.out.println("Enter doctor password: ");
        String password = scanner.next();
        System.out.println("Enter doctor name: ");
        String name = scanner.next();
        System.out.println("Enter doctor specialization: ");
        String specialization = scanner.next();
        System.out.println("Enter doctor qualification: ");
        String qualification = scanner.next();
        System.out.println("Enter doctor room: ");
        int room = scanner.nextInt();

        try {
            String query = "INSERT INTO doctors(username, password, name, specialization, qualification, room) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, specialization);
            preparedStatement.setString(5, qualification);
            preparedStatement.setInt(6, room);

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

    static void signInDoctor(Doctor doctor, Connection con, Scanner scanner) {
        System.out.println("Enter doctor username: ");
        String username = scanner.next();
        System.out.println("Enter doctor password: ");
        String password = scanner.next();

        try {
            String query = "SELECT * FROM doctors WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                loggedInDoctorId = resultSet.getInt("id");
                System.out.println("Doctor signed in successfully!");
            } else {
                System.out.println("Doctor sign-in failed. Invalid credentials.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLoggedInDoctorId() {
        return loggedInDoctorId;
    }
    public static void bookAppointmentD(int loggedInDoctorId) {
        System.out.println("Enter patient ID for appointment: ");
        int patientId = scanner.nextInt();

        // Check if the patient exists
        if (patient.getPatientById(patientId)) {
            System.out.println("Enter appointment date (YYYY-MM-DD): ");
            String appointmentDate = scanner.next();

            // Check if the patient is available on the specified date
            if (checkDoctorAvailability(patientId, appointmentDate, con)) {
                try {
                    // Create a new appointment
                    String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, loggedInDoctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment booked successfully!");
                    } else {
                        System.out.println("Failed to book appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Patient is not available on this date!");
            }
        } else {
            System.out.println("Patient with ID " + patientId + " not found.");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection con){
        String query = "select count(*) from appointments where doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else{
                    return  false;
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }
    public void viewPatients(int loggedInDoctorId) {
        String query = "SELECT * FROM patients WHERE doctor_id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, Doctor.loggedInDoctorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patients: ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatientDetails(int patientId, int loggedInDoctorId) {
        String query = "SELECT * FROM patients WHERE id = ? AND doctor_id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, Doctor.loggedInDoctorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            } else {
                System.out.println("Patient not found or not assigned to this doctor.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewBookedAppointments(int loggedInDoctorId) {
        String query = "SELECT * FROM appointments WHERE doctor_id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, Doctor.loggedInDoctorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Booked Appointments: ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void doctorMenu(Doctor doctor, Connection con, Scanner scanner, int loggedInDoctorId) {
        while (true) {

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    //view Patients
                    doctor.viewPatients(loggedInDoctorId);
                case 2:
                    //view Patients details
                    System.out.println("Enter patient id to view details: ");
                    int patientID = scanner.nextInt();
                    doctor.viewPatientDetails(patientID, loggedInDoctorId);
                case 3:
                    //view booked appointments
                    doctor.viewBookedAppointments(loggedInDoctorId);
                case 4:
                    //book appointment
                    bookAppointmentD(loggedInDoctorId);
                case 5:
                    //Add patient
                    patient.addPatient();
                    System.out.println();
                case 6:
                    //Update patient
                    System.out.println("Enter patient id to update: ");
                    int patientid = scanner.nextInt();
                    patient.updatePatient(patientid);
                    System.out.println();
                case 7:
                    // Delete patient
                    System.out.println("Enter patient id to delete: ");
                    int patID = scanner.nextInt();
                    patient.deletePatient(patID);
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
                    appointment.addAppointment(patientId, doctorId, String.valueOf(date));
                case 9:
                    // Update appointment
                    System.out.println("Enter appointment id to update: ");
                    int appointmentIdToUpdate = scanner.nextInt();
                    System.out.println("Enter appointment date to update: ");
                    Date dates = Date.valueOf(scanner.next());
                    appointment.updateAppointment(appointmentIdToUpdate, String.valueOf(dates));
                case 10:
                    // Delete appointment
                    System.out.println("Enter appointment id to delete: ");
                    int appointmentIdToDelete = scanner.nextInt();
                    appointment.deleteAppointment(appointmentIdToDelete);
                case 11:
                    return;
                default:
                    System.out.println("Enter valid choice!!");
            }
        }
    }
    private void printDoctorMenu() {
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
}
