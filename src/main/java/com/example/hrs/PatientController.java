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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class PatientController {
    private Connection con;
    private Scanner scanner;
    private int userId;
    private static final String url = "jdbc:postgresql://localhost/hrs";
    private static final String username = "postgres";
    private static final String password = "postgres";

    @FXML
    private void initialize() {
        // Initialize con and scanner here
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Doctor.con = con;
            Patient.con = con;
            User.con = con;
            Appointment.con = con;
            scanner = new Scanner(System.in);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }

    }
    @FXML
    private void viewDoctors(ActionEvent event) {
        System.out.println("Enter your id in order to see your doctors: ");
        int pid = this.scanner.nextInt();
        Patient.viewDoctors(pid);
    }


    @FXML
    private void addDoctor(ActionEvent event) {
        Doctor.addDoctor();
    }

    @FXML
    private void updateDoctor(ActionEvent event) {
        System.out.println("Enter doctor ID to update: ");
        int doctorid = scanner.nextInt();
        Doctor.updateDoctor(doctorid, scanner);
    }

    @FXML
    private void deleteDoctor(ActionEvent event) {
        System.out.println("Enter doctor ID to delete: ");
        int docID = scanner.nextInt();
        Doctor.deleteDoctor(docID);
    }

    @FXML
    private void viewBookedAppointments(ActionEvent event) {
        Patient.viewBookedAppointments(userId);
    }

    @FXML
    private void bookAppointment(ActionEvent event) {
        Patient.bookAppointmentP(userId, scanner);
    }

    @FXML
    private void addAppointment(ActionEvent event) {
        Appointment.addAppointment();
    }

    @FXML
    private void updateAppointment(ActionEvent event) {
        System.out.println("Enter appointment ID to update: ");
        int appointmentIdToUpdate = scanner.nextInt();
        System.out.println("Enter appointment date to update: ");
        java.sql.Date date = java.sql.Date.valueOf(scanner.next());
        Appointment.updateAppointment(appointmentIdToUpdate, date);
    }

    @FXML
    private void deleteAppointment(ActionEvent event) {
        System.out.println("Enter appointment ID to delete: ");
        int appointmentIdToDelete = scanner.nextInt();
        Appointment.deleteAppointment(appointmentIdToDelete);
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
    private void handleRedirectD(ActionEvent event) {
        try {
            // Load the doctor.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Doctor.fxml"));
            Parent root = loader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRedirectM(ActionEvent event) {
        try {
            // Load the doctor.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Show the new stage
            stage.show();
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}