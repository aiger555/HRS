package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

public class Appointment {
    private static Connection con;
    private Scanner scanner;

    public Appointment(Connection con, Scanner scanner) {
        this.con = con;
        this.scanner = scanner;
    }

    public static void addAppointment(int patientId, int doctorId, Date appointmentDate, Connection con) {
        try {
            String query = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, doctorId);
            preparedStatement.setDate(3, appointmentDate);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment added successfully!");
            } else {
                System.out.println("Failed to add appointment");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAppointment(Connection con, int appointmentId, Date appointmentDate) {
        try {
            String query = "UPDATE appointments SET appointment_date=? WHERE id=?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setDate(1, appointmentDate);
            preparedStatement.setInt(2, appointmentId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment updated successfully!");
            } else {
                System.out.println("Failed to update appointment");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteAppointment(Connection con,int appointmentId) {
        try {
            String query = "DELETE FROM appointments WHERE id=?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment deleted successfully!");
            } else {
                System.out.println("Failed to delete appointment");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAppointments() {
        try {
            String query = "SELECT * FROM appointments";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Appointments: ");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                String appointmentDate = resultSet.getString("appointment_date");

                System.out.printf("| %-5s | %-10s | %-10s | %-15s |\n", id, patientId, doctorId, appointmentDate);
            }
            System.out.println("+-----+------------+------------+-----------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void setConnection(Connection connection) {
        con = connection;
    }
}
