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
                String doctor = resultSet.getString("name");
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
    public static void viewPatients(int loggedInDoctorId) {
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

    public static void viewPatientDetails(int patientId, int loggedInDoctorId) {
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

    public static void viewBookedAppointments(int loggedInDoctorId) {
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


    public int getLoggedInDoctorId() {
        return loggedInDoctorId;
    }

}
