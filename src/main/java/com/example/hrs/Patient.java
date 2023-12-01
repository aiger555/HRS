package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

import static com.example.hrs.Doctor.checkDoctorAvailability;

public class Patient {
    private static Connection con;
    private static Scanner scanner;
    private static int loggedInPatientId;
    private static Doctor doctor;
    private static Appointment appointment;


    public Patient(Connection con, Scanner scanner){
        this.con = con;
        this.scanner = scanner;
    }

    public static void addPatient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter patient name: ");
        String name = scanner.next();
        System.out.println("Enter surname: ");
        String surname = scanner.next();
        System.out.println("Enter phone number: ");
        int p_number = scanner.nextInt();
        System.out.println("Enter reason: ");
        String reason = scanner.next();
        System.out.println("Enter patient's room: ");
        int room = scanner.nextInt();

        try {
            String query = "INSERT INTO patients(name, surname, phone_number, reason, room) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setInt(3, p_number);
            preparedStatement.setString(4, reason);
            preparedStatement.setInt(5, room);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient added successfully!");
            } else {
                System.out.println("Failed to add Doctor");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePatient(int id) {
        if (getPatientById(id)) {
            try {
                System.out.println("Enter new patient name: ");
                String name = scanner.next();
                System.out.println("Enter new patient surname: ");
                String surname = scanner.next();
                System.out.println("Enter new patient phone number: ");
                int p_number = scanner.nextInt();
                System.out.println("Enter new reason of visiting: ");
                String reason = scanner.next();
                System.out.println("Enter new patient room: ");
                int room = scanner.nextInt();

                String query = "UPDATE patients SET name=?, surname=?, phone_number=?, reason=?, room=? WHERE id=?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.setInt(3, p_number);
                preparedStatement.setString(4, reason);
                preparedStatement.setInt(5, room);
                preparedStatement.setInt(6, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Patient updated successfully!");
                } else {
                    System.out.println("Failed to update patient");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Patient with ID " + id + " not found.");
        }
    }

    public static void deletePatient(int patientId, Connection con) {
        try {
            // Delete appointments for the patient
            String deleteAppointmentsQuery = "DELETE FROM appointments WHERE patient_id = ?";
            try (PreparedStatement deleteAppointmentsStatement = con.prepareStatement(deleteAppointmentsQuery)) {
                deleteAppointmentsStatement.setInt(1, patientId);
                deleteAppointmentsStatement.executeUpdate();
            }

            // Now delete the patient
            String deletePatientQuery = "DELETE FROM patients WHERE id = ?";
            try (PreparedStatement deletePatientStatement = con.prepareStatement(deletePatientQuery)) {
                deletePatientStatement.setInt(1, patientId);
                int rowsAffected = deletePatientStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Patient deleted successfully!");
                } else {
                    System.out.println("Failed to delete patient.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void viewPatient(){
        String query = "select * from patients";

        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+----+--------+----------+-------------+--------------+------+");
            System.out.println("| ID | Name   | Surname  | Phone number| Reason       | Room |");
            System.out.println("+----+--------+----------+-------------+--------------+------+");

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int p_number = resultSet.getInt("phone_number");
                String reason = resultSet.getString("reason");
                int room = resultSet.getInt("room");
                System.out.printf("| %-4s | %-14s | %-13s | %-13s | %-13s | %-7s |\n", id, name, surname, p_number, reason, room);
                System.out.println("+----+--------+----------+-------------+--------------+------+");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean getPatientById(int id){
        String query = "select * from patients where id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return  true;
            }else{
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void viewDoctors(int loggedInPatientId) {
        String query = "SELECT doctors.* " +
                "FROM doctors " +
                "JOIN users ON doctors.id = users.id " +
                "JOIN patients ON doctors.id = patients.id " +
                "WHERE patients.id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, Patient.loggedInPatientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors: ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void bookAppointmentP(int loggedInPatientId) {
        System.out.println("Enter doctor ID for appointment: ");
        int doctorId = scanner.nextInt();

        // Check if the doctor exists
        if (doctor.getDoctorById(doctorId)) {
            System.out.println("Enter appointment date (YYYY-MM-DD): ");
            String appointmentDate = scanner.next();

            // Check if the doctor is available on the specified date
            if (checkDoctorAvailability(doctorId, Date.valueOf(appointmentDate), con)) {
                try {
                    // Create a new appointment
                    String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, loggedInPatientId);
                    preparedStatement.setInt(2, doctorId);
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
                System.out.println("Doctor is not available on this date!");
            }
        } else {
            System.out.println("Doctor with ID " + doctorId + " not found.");
        }
    }
    public static void viewBookedAppointments(Connection con, int loggedInDoctorId) {
        String query = "SELECT * FROM appointments WHERE patient_id = ?";

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


    public int getLoggedInPatientId() {
        return loggedInPatientId;
    }

}

