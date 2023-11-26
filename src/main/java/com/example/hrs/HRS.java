package com.example.hrs;

import java.sql.*;
import java.util.Scanner;

public class HRS {
    private static final String url = "jdbc:postgresql://localhost/hrs";
    private static final String username = "postgres";
    private static final String password = "postgres";

    public static void main(String[] args){
        try{
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(con, scanner);
            Doctor doctor = new Doctor(con, scanner);
            while(true){
                System.out.println("Hospital Reservation System");
                System.out.println("1. Add Patient");
                System.out.println("2. Update Patient");
                System.out.println("3. Delete Patient");
                System.out.println("4. View Patients");
                System.out.println("5. Add Doctor");
                System.out.println("6. Update Doctor");
                System.out.println("7. Delete Doctor");
                System.out.println("8. View Doctors");
                System.out.println("9. Book Appointment");
                System.out.println("10. Exit");
                System.out.println("Enter your choice: ");

                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        //Add patient
                        patient.addPatient();
                        System.out.println();
                    case 2:
                        //Update patient
                        System.out.println("Enter patient id to update: ");
                        int patientID = scanner.nextInt();
                        patient.updatePatient(patientID);
                        System.out.println();
                    case 3:
                        // Delete patient
                        System.out.println("Enter patient id to delete: ");
                        int patID = scanner.nextInt();
                        patient.deletePatient(patID);
                        System.out.println();
                    case 4:
                        //View patients
                        patient.viewPatient();
                        System.out.println();
                    case 5:
                        // Add doctor
                        doctor.addDoctor();
                        System.out.println();
                    case 6:
                        // Update doctor
                        System.out.println("Enter doctor id to update: ");
                        int doctorID = scanner.nextInt();
                        doctor.updateDoctor(doctorID);
                        System.out.println();
                    case 7:
                        // Delete doctor
                        System.out.println("Enter doctor id to update: ");
                        int dID = scanner.nextInt();
                        doctor.deleteDoctor(dID);
                        System.out.println();
                    case 8:
                        //View doctors
                        doctor.viewDoctor();
                        System.out.println();
                    case 9:
                        //Book appointment
                        bookAppointment(patient, doctor, con, scanner);
                        System.out.println();
                    case 10:
                        //Exit
                        return;
                    default:
                        System.out.println("Enter valid choice!!");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection con, Scanner scanner){
        System.out.println("Enter patient ID: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter doctor ID: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, con)){
                String appointmentQuery = "insert into appointments(patient_id, doctor_id, appointment_date) values (?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = con.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0) {
                        System.out.println("Appointment booked");
                    }else{
                        System.out.println("Failed to Book!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor is not available on this date!");
            }

        }else{
            System.out.println("Either doctor or patient doesn't exist!");
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
}
