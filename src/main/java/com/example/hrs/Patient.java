package com.example.hrs;
import javafx.scene.shape.PathElement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection con;
    private Scanner scanner;

    public Patient(Connection con, Scanner scanner){
        this.con = con;
        this.scanner = scanner;
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

}

