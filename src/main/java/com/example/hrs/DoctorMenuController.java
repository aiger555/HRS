package com.example.hrs;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.util.Scanner;

public class DoctorMenuController {
    private Doctor doctor;
    private Connection con;
    private Scanner scanner;

    public void initData(Doctor doctor, Connection con, Scanner scanner) {
        this.doctor = doctor;
        this.con = con;
        this.scanner = scanner;
    }

    @FXML
    void handleViewPatients() {
        // Implement logic to view patients assigned to the doctor
        // You can use doctor object and interact with the database
        doctor.viewPatients(con, doctor.getLoggedInDoctorId());
    }

    @FXML
    void handleViewPatientDetails() {
        // Implement logic to view details of a specific patient
        // You can use doctor, patient, and appointment objects
        // and interact with the database
        System.out.println("Enter patient id to view details: ");
        int patientID = scanner.nextInt();
        doctor.viewPatientDetails(patientID, doctor.getLoggedInDoctorId());
    }

    // Add more methods for other doctor actions

    @FXML
    void handleLogout() {
        // Implement logic for logging out
        // You may need to close the current window or switch scenes
    }
}
