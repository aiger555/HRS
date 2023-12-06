package com.example.hrs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class PatientController {
    private Connection con;
    private Scanner scanner;
    private int userId;

    public PatientController(Connection con, Scanner scanner, int userId) {
        this.con = con;
        this.scanner = scanner;
        this.userId = userId;
    }

    @FXML
    private void viewDoctors(ActionEvent event) {
        Patient.viewDoctors(userId);
    }

    @FXML
    private void addDoctor(ActionEvent event) {
        Doctor.addDoctor();
    }

    @FXML
    private void updateDoctor(ActionEvent event) {
        System.out.println("Enter doctor ID to update: ");
        int doctorid = scanner.nextInt();
        Doctor.updateDoctor(doctorid);
    }

    @FXML
    private void deleteDoctor(ActionEvent event) {
        System.out.println("Enter doctor ID to delete: ");
        int docID = scanner.nextInt();
        Doctor.deleteDoctor(docID);
    }

    @FXML
    private void viewBookedAppointments(ActionEvent event) {
        Patient.viewBookedAppointments(con, userId);
    }

    @FXML
    private void bookAppointment(ActionEvent event) {
        Patient.bookAppointmentP(userId);
    }

    @FXML
    private void addAppointment(ActionEvent event) {
        System.out.println("Enter patient ID to add: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter doctor ID to add: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date to add: ");
        java.sql.Date date = java.sql.Date.valueOf(scanner.next());
        Appointment.addAppointment(patientId, doctorId, date, con);
    }

    @FXML
    private void updateAppointment(ActionEvent event) {
        System.out.println("Enter appointment ID to update: ");
        int appointmentIdToUpdate = scanner.nextInt();
        System.out.println("Enter appointment date to update: ");
        java.sql.Date date = java.sql.Date.valueOf(scanner.next());
        Appointment.updateAppointment(con, appointmentIdToUpdate, date);
    }

    @FXML
    private void deleteAppointment(ActionEvent event) {
        System.out.println("Enter appointment ID to delete: ");
        int appointmentIdToDelete = scanner.nextInt();
        Appointment.deleteAppointment(con, appointmentIdToDelete);
    }

    @FXML
    private void exit(ActionEvent event) {
        try {
            if (con != null) {
                con.close();
            }
            scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
    @FXML
    private void switchToPatient(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Patient.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);

        // Get the current stage from any control within the current scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}