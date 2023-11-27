package com.example.hrs;
import javafx.scene.shape.PathElement;

import java.sql.*;
import java.util.Scanner;

import static com.example.hrs.Doctor.checkDoctorAvailability;

public class Patient {
    private Connection con;
    private Scanner scanner;
    private static int loggedInPatientId;
    private Doctor doctor;
    private Appointment appointment;


    public Patient(Connection con, Scanner scanner){
        this.con = con;
        this.scanner = scanner;
    }

    static void registerPatient(Patient patient, Connection con, Scanner scanner) {
        System.out.println("Enter patient name: ");
        String name = scanner.next();
        System.out.println("Enter patient surname: ");
        String surname = scanner.next();
        System.out.println("Enter patient phone number: ");
        int p_number = scanner.nextInt();
        System.out.println("Enter patient reason of visiting: ");
        String reason = scanner.next();
        System.out.println("Specify patient room: ");
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
                System.out.println("Patient registered successfully!");
            } else {
                System.out.println("Failed to register patient");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void signInPatient(Patient patient, Connection con, Scanner scanner) {
        System.out.println("Enter patient ID: ");
        int patientId = scanner.nextInt();

        if (patient.getPatientById(patientId)) {
            System.out.println("Patient signed in successfully!");
        } else {
            System.out.println("Patient sign-in failed. Invalid ID.");
        }
    }
    public void addPatient(){
        System.out.println("Enter patient name: ");
        String name = scanner.next();
        System.out.println("Enter patient surname: ");
        String surname = scanner.next();
        System.out.println("Enter patient phone number: ");
        int p_number = scanner.nextInt();
        System.out.println("Enter patient reason of visiting: ");
        String reason = scanner.next();
        System.out.println("Specify patient room: ");
        int room = scanner.nextInt();

        try{
            String query =
                    "INSERT INTO patients(name, surname, phone number, reason, room) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setInt(3, p_number);
            preparedStatement.setString(4, reason);
            preparedStatement.setInt(5, room);

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient added succesfully!");
            }else{
                System.out.println("Failed to add Patient");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updatePatient(int id) {
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

    public void deletePatient(int id) {
        if (getPatientById(id)) {
            try {
                String query = "DELETE FROM patients WHERE id=?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Patient deleted successfully!");
                } else {
                    System.out.println("Failed to delete patient");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Patient with ID " + id + " not found.");
        }
    }

    public void viewPatient(){
        String query = "select * from patients";

        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+-----+--------------+-------------+-------------+-------------+------+");
            System.out.println("| ID  | Name         | Surname     | Phone number| Reason      | Room  ");
            System.out.println("+-----+--------------+-------------+-------------+-------------+------+");

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int p_number = resultSet.getInt("phone number");
                String reason = resultSet.getString("reason");
                int room = resultSet.getInt("room");
                System.out.printf("| %-5s | %-14s | %-13s | %-13s | %-13s | %-7s |");
                System.out.println("+-----+--------------+-------------+-------------+-------------+------+");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
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

    public void viewDoctors(int loggedInPatientId) {
        String query = "SELECT * FROM doctors WHERE patient_id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, Patient.loggedInPatientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors: ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void bookAppointmentP(int loggedInPatientId) {
        System.out.println("Enter doctor ID for appointment: ");
        int doctorId = scanner.nextInt();

        // Check if the doctor exists
        if (doctor.getDoctorById(doctorId)) {
            System.out.println("Enter appointment date (YYYY-MM-DD): ");
            String appointmentDate = scanner.next();

            // Check if the doctor is available on the specified date
            if (checkDoctorAvailability(doctorId, appointmentDate, con)) {
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
    public void viewBookedAppointments(int loggedInPatientId) {
        String query = "SELECT * FROM appointments WHERE patient_id = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, Patient.loggedInPatientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Booked Appointments: ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void patientMenu() {
        while (true) {
            printPatientMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    //View doctors
                    viewDoctors(loggedInPatientId);
                case 2:
                    //Add doctor
                    doctor.addDoctor();
                    System.out.println();
                case 3:
                    //Update doctor
                    System.out.println("Enter doctor id to update: ");
                    int doctorid = scanner.nextInt();
                    doctor.updateDoctor(doctorid);
                    System.out.println();
                case 4:
                    // Delete doctor
                    System.out.println("Enter doctor id to delete: ");
                    int docID = scanner.nextInt();
                    doctor.deleteDoctor(docID);
                    System.out.println();
                case 5:
                    //view booked appointments
                    viewBookedAppointments(loggedInPatientId);
                case 6:
                    //book appointment
                    bookAppointmentP(loggedInPatientId);
                case 7:
                    // Add appointment
                    System.out.println("Enter patient id to add: ");
                    int patientId = scanner.nextInt();
                    System.out.println("Enter doctor id to add: ");
                    int doctorId = scanner.nextInt();
                    System.out.println("Enter appointment date to add: ");
                    Date date = Date.valueOf(scanner.next());
                    appointment.addAppointment(patientId, doctorId, String.valueOf(date));
                case 8:
                    //update appointment
                    System.out.println("Enter appointment id to update: ");
                    int appointmentIdToUpdate = scanner.nextInt();
                    System.out.println("Enter appointment date to update: ");
                    Date dates = Date.valueOf(scanner.next());
                    appointment.updateAppointment(appointmentIdToUpdate, String.valueOf(dates));
                case 9:
                    //delete appointment
                    System.out.println("Enter appointment id to delete: ");
                    int appointmentIdToDelete = scanner.nextInt();
                    appointment.deleteAppointment(appointmentIdToDelete);
                case 10:
                    //exit
                    return;
                default:
                    System.out.println("Enter valid choice!!");
            }
        }
    }

    private void printPatientMenu() {
        System.out.println("Patient Menu:");
        System.out.println("1. View Doctors");
        System.out.println("2. Add doctor");
        System.out.println("3. Update doctor");
        System.out.println("4. Delete doctor");
        System.out.println("5. view booked appointments");
        System.out.println("6. book appointment");
        System.out.println("7. Add appointment");
        System.out.println("8. Update appointment");
        System.out.println("9. Delete appointment");
        System.out.println("10. Exit");
    }

    public int getLoggedInPatientId() {
        return loggedInPatientId;
    }
}

