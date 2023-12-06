package com.example.hrs;

import java.net.Socket;
import java.sql.*;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Doctor {
    public static Connection con;
    private static Scanner scanner;
    static Patient patient;

    private static int loggedInDoctorId;
    private static Appointment appointment;


    public Doctor(Connection con, Scanner scanner) {
        this.con = con;
        this.scanner = scanner;
    }

    public static void addDoctor() {
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
            String query = "INSERT INTO doctors(name, specialization, qualification, room) VALUES (?, ?, ?, ?)";
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

    public static void updateDoctor(int id) {
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

                String query = "UPDATE doctors SET name=?, specialization=?, qualification=?, room=? WHERE id=?";
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

    public static void deleteDoctor(int id) {
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

    public static void viewDoctor() {
        String query = "select * from doctors";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+----+---------+----------------+----------------+------+");
            System.out.println("| ID | Doctor  | Specialization | Qualification  | Room |");
            System.out.println("+----+---------+----------------+----------------+------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String doctor = resultSet.getString("name");
                String spec = resultSet.getString("specialization");
                String qual = resultSet.getString("qualification");
                int room = resultSet.getInt("room");
                System.out.printf("| %-5s | %-16s | %-16s | %-15s | %-7s |\n", id, doctor, spec, qual, room);
                System.out.println("+----+---------+----------------+----------------+---------------+------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean getDoctorById(int id) {
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
    public static void bookAppointmentD(int doctorId, Connection con, Scanner scanner) {
        System.out.println("Enter patient ID for appointment: ");
        int patientId = scanner.nextInt();

        // Check if the patient exists
        if (patient.getPatientById(patientId)) {
            System.out.println("Enter appointment date (YYYY-MM-DD): ");
            String appointmentDateString = scanner.next();

            try {
                // Parse the appointment date
                Date appointmentDate = Date.valueOf(appointmentDateString);

                // Check if the patient is available on the specified date
                if (checkDoctorAvailability(doctorId, appointmentDate, con)) {
                    // Create a new appointment
                    String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = con.prepareStatement(appointmentQuery)) {
                        preparedStatement.setInt(1, patientId);
                        preparedStatement.setInt(2, doctorId);
                        preparedStatement.setDate(3, appointmentDate);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Appointment booked successfully!");
                        } else {
                            System.out.println("Failed to book appointment");
                        }
                    }
                } else {
                    System.out.println("Doctor is not available on this date!");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use the format YYYY-MM-DD.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Patient with ID " + patientId + " not found.");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, Date appointmentDate, Connection con){
        String query = "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setDate(2, appointmentDate);
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
    public static void viewPatients(int loggedInDoctorId) {
        String query = "SELECT u.id, u.name FROM appointments a " +
                "JOIN doctors ON doctors.id = a.doctor_id " +
                "JOIN patients p ON p.id = a.patient_id " +
                "JOIN users u ON p.id = u.id " +
                "WHERE doctors.id = ?";

        try {
            PreparedStatement preparedStatement = Doctor.con.prepareStatement(query);
            preparedStatement.setInt(1, loggedInDoctorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patients: ");
            while (resultSet.next()) {
                int patientId = resultSet.getInt("id");
                String patientName = resultSet.getString("name");
                // Add other patient details as needed

                // Print or process patient information as needed
                System.out.println("Patient ID: " + patientId);
                System.out.println("Patient Name: " + patientName);
                System.out.println("-----------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void viewPatientDetails(int patientId, int loggedInDoctorId) {
        String query = "SELECT * FROM patients WHERE id = ? AND id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, loggedInDoctorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve patient details from the result set
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                // Add more fields as needed

                // Display patient details
                System.out.println("Patient Details:");
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                // Print more details

            } else {
                System.out.println("Patient not found or not assigned to this doctor.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void viewBookedAppointments(Connection con, int loggedInDoctorId) {
        String query = "SELECT * FROM appointments WHERE doctor_id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, loggedInDoctorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Booked Appointments: ");
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                Date appointmentDate = resultSet.getDate("appointment_date");

                // Print or process appointment information as needed
                System.out.println("Appointment ID: " + appointmentId);
                System.out.println("Patient ID: " + patientId);
                System.out.println("Appointment Date: " + appointmentDate);
                System.out.println("-----------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static int getLoggedInDoctorId() {
        return loggedInDoctorId;
    }

}
