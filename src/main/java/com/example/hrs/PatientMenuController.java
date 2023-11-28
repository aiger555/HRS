package com.example.hrs;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.util.Scanner;

public class PatientMenuController {
    private Patient patient;
    private Connection con;
    private Scanner scanner;

    public void initData(Patient patient, Connection con, Scanner scanner) {
        this.patient = patient;
        this.con = con;
        this.scanner = scanner;
    }

    @FXML
    void handleViewPatientDetails() {
        // Implement logic to view patient details
        // You can use patient object and interact with the database
        patient.viewPatient();
    }

    @FXML
    void handleBookAppointment() {
        // Implement logic to book an appointment
        // You can use patient, doctor, and appointment objects
        // and interact with the database
        Patient.bookAppointmentP(patient.getLoggedInPatientId());
    }

    // Add more methods for other patient actions

    @FXML
    void handleLogout() {
        // Implement logic for logging out
        // You may need to close the current window or switch scenes
    }
}
