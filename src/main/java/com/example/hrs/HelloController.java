package com.example.hrs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class HelloController {
    private Connection con;
    private Scanner scanner;
    private Patient patient;
    private Doctor doctor;

    public HelloController(Connection con, Scanner scanner) {
        this.con = con;
        this.scanner = scanner;
        this.patient = new Patient(con, scanner);
        this.doctor = new Doctor(con, scanner);
    }

    // ... Other methods

    @FXML
    void patientMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("patient-menu.fxml"));
            Parent root = loader.load();

            // Assuming PatientMenuController is the controller for the patient menu
            PatientMenuController patientMenuController = loader.getController();
            patientMenuController.initData(patient, con, scanner);

            Stage stage = new Stage();
            stage.setTitle("Patient Menu");
            stage.setScene(new Scene(root));
            stage.show();

            // You can close the current window if needed
            // ((Stage) signInButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load patient menu", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void doctorMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("doctor-menu.fxml"));
            Parent root = loader.load();

            // Assuming DoctorMenuController is the controller for the doctor menu
            DoctorMenuController doctorMenuController = loader.getController();
            doctorMenuController.initData(doctor, con, scanner);

            Stage stage = new Stage();
            stage.setTitle("Doctor Menu");
            stage.setScene(new Scene(root));
            stage.show();

            // You can close the current window if needed
            // ((Stage) signInButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load doctor menu", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
