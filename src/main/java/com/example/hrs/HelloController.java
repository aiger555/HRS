package com.example.hrs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import static com.example.hrs.Doctor.bookAppointmentD;
import static com.example.hrs.Doctor.getLoggedInDoctorId;
import static com.example.hrs.Patient.bookAppointmentP;
import static com.example.hrs.Patient.viewBookedAppointments;

public class HelloController {

    private Connection con;
    private Scanner scanner;

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
            System.out.println("Database connected...");
            scanner = new Scanner(System.in);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }

    }

    @FXML
    private void handleViewPatients(ActionEvent event) {
        Patient.viewPatient();
    }

    @FXML
    private void handleViewDoctors(ActionEvent event) {
        Doctor.viewDoctor();
    }

    @FXML
    private void handleRegisterDoctor(ActionEvent event) {
        User.registerDoctor(scanner);
    }

    @FXML
    private void handleSignInDoctor(ActionEvent event) {
        System.out.println("Enter doctor ID to sign in: ");
        int doctorId = scanner.nextInt();
        User.signInAsDoctor(doctorId, scanner);
    }

    @FXML
    private void handleRegisterPatient(ActionEvent event) {
        User.registerPatient(scanner);
    }

    @FXML
    private void handleSignInPatient(ActionEvent event) {
        System.out.println("Enter patient ID to sign in: ");
        int patientId = scanner.nextInt();
        User.signInAsPatient(patientId, scanner);
    }

    @FXML
    private void handlePatientMenu(ActionEvent event) {
        // Display patient menu
        displayMenu("Patient Menu:", "1. View Doctors\n2. Add doctor\n3. Update doctor\n4. Delete doctor\n5. View booked appointments\n6. Book appointment\n7. Add appointment\n8. Update appointment\n9. Delete appointment\n10. Exit");
        int choice = getUserChoice();

        switch (choice) {
            case 1:
                //View doctors
                int userId = scanner.nextInt();
                Patient.viewDoctors(userId);
                break;
            case 2:
                //Add doctor
                Doctor.addDoctor();
                System.out.println();
                break;
            case 3:
                //Update doctor
                System.out.println("Enter doctor id to update: ");
                int doctorid = scanner.nextInt();
                Doctor.updateDoctor(doctorid, scanner);
                System.out.println();
                break;
            case 4:
                // Delete doctor
                System.out.println("Enter doctor id to delete: ");
                int docID = scanner.nextInt();
                Doctor.deleteDoctor(docID);
                System.out.println();
                break;
            case 5:
                //view booked appointments
                int userID = scanner.nextInt();
                viewBookedAppointments(userID);
                break;
            case 6:
                //book appointment
                int userid = scanner.nextInt();
                bookAppointmentP(userid, scanner);
                break;
            case 7:
                // Add appointment
                Appointment.addAppointment();
                break;
            case 8:
                //update appointment
                System.out.println("Enter appointment id to update: ");
                int appointmentIdToUpdate = scanner.nextInt();
                System.out.println("Enter appointment date to update: ");
                Date dates = Date.valueOf(scanner.next());
                Appointment.updateAppointment(appointmentIdToUpdate, dates);
                break;
            case 9:
                //delete appointment
                System.out.println("Enter appointment id to delete: ");
                int appointmentIdToDelete = scanner.nextInt();
                Appointment.deleteAppointment(appointmentIdToDelete);
                break;
            case 10:
                //exit
                return;
            default:
                System.out.println("Enter valid choice!!");
        }
    }

    @FXML
    private void handleDoctorMenu(ActionEvent event) {
        // Display doctor menu
        displayMenu("Doctor Menu:", "1. View Patients\n2. View Patients details\n3. View booked appointments\n4. Book appointment\n5. Add patient\n6. Update patient\n7. Delete patient\n8. Add appointment\n9. Update appointment\n10. Delete appointment\n11. Exit");
        int choice = getUserChoice();

        switch (choice) {
            case 1:
                //view Patients
                int pid = scanner.nextInt();
                Doctor.viewPatients(pid);
                break;
            case 2:
                //view Patients details
                System.out.println("Enter patient id to view details: ");
                int patientID = scanner.nextInt();
                Doctor.viewPatientDetails(patientID);
                break;
            case 3:
                //view booked appointments
                int paid = scanner.nextInt();
                Doctor.viewBookedAppointments(paid);
                break;
            case 4:
                //book appointment
                int patid = scanner.nextInt();
                bookAppointmentD(patid, scanner);
                break;
            case 5:
                //Add patient
                Patient.addPatient();
                System.out.println();
                break;
            case 6:
                //Update patient
                System.out.println("Enter patient id to update: ");
                int patientid = scanner.nextInt();
                Patient.updatePatient(patientid, scanner);
                System.out.println();
                break;
            case 7:
                // Delete patient
                System.out.println("Enter patient id to delete: ");
                int patID = scanner.nextInt();
                Patient.deletePatient(patID);
                System.out.println();
                break;
            case 8:
                // Add appointment
                Appointment.addAppointment();
                break;
            case 9:
                // Update appointment
                System.out.println("Enter appointment id to update: ");
                int appointmentIdToUpdate = scanner.nextInt();
                System.out.println("Enter appointment date to update: ");
                Date dates = Date.valueOf(scanner.next());
                Appointment.updateAppointment(appointmentIdToUpdate, dates);
                break;
            case 10:
                // Delete appointment
                System.out.println("Enter appointment id to delete: ");
                int appointmentIdToDelete = scanner.nextInt();
                Appointment.deleteAppointment(appointmentIdToDelete);
                break;
            case 11:
                return;
            default:
                System.out.println("Enter valid choice!!");
        }

    }

    private void displayMenu(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private int getUserChoice() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Choice");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your choice:");
        Optional<String> result = dialog.showAndWait();
        return result.map(Integer::parseInt).orElse(0);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Handle exit action for the application
        System.exit(0);
    }

    @FXML
    private void handleRedirect(ActionEvent event) {
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
    private void handleRedirectP(ActionEvent event) {
        try {
            // Load the doctor.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Patient.fxml"));
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

