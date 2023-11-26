package com.example.hrs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection con;
    private Scanner scanner;

    public Doctor(Connection con, Scanner scanner){
        this.con = con;
        this.scanner = scanner;
    }

    public void addDoctor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter doctor name: ");
        String doctorName = scanner.next();
        System.out.println("Enter specialization: ");
        String specialization = scanner.next();
        System.out.println("Enter qualification: ");
        String qualification = scanner.next();
        System.out.println("Enter doctor's room: ");
        int room = scanner.nextInt();

        try {
            String query = "INSERT INTO doctors(doctor, specialization, qualification, room) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, doctorName);
            preparedStatement.setString(2, specialization);
            preparedStatement.setString(3, qualification);
            preparedStatement.setInt(4, room);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Doctor added successfully!");
            } else {
                System.out.println("Failed to add Doctor");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor(int id) {
        if (getDoctorById(id)) {
            try {
                System.out.println("Enter new doctor name: ");
                String doctorName = scanner.next();
                System.out.println("Enter new specialization: ");
                String specialization = scanner.next();
                System.out.println("Enter new qualification: ");
                String qualification = scanner.next();
                System.out.println("Enter new doctor's room: ");
                int room = scanner.nextInt();

                String query = "UPDATE doctors SET doctor=?, specialization=?, qualification=?, room=? WHERE id=?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, doctorName);
                preparedStatement.setString(2, specialization);
                preparedStatement.setString(3, qualification);
                preparedStatement.setInt(4, room);
                preparedStatement.setInt(5, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Doctor updated successfully!");
                } else {
                    System.out.println("Failed to update doctor");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Doctor with ID " + id + " not found.");
        }
    }

    public void deleteDoctor(int id) {
        if (getDoctorById(id)) {
            try {
                String query = "DELETE FROM doctors WHERE id=?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Doctor deleted successfully!");
                } else {
                    System.out.println("Failed to delete doctor");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Doctor with ID " + id + " not found.");
        }
    }

    public void viewDoctor(){
        String query = "select * from doctors";

        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+-----+----------------+----------------+---------------+-------+");
            System.out.println("| ID  | Doctor         | Specialization | Qualification | Room  |");
            System.out.println("+-----+----------------+----------------+---------------+-------+");

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String doctor = resultSet.getString("doctor");
                String spec = resultSet.getString("specialization");
                String qual = resultSet.getString("qualification");
                int room = resultSet.getInt("room");
                System.out.printf("| %-5s | %-16s | %-16s | %-15s | %-7s |\n", id, doctor, spec, qual, room);
                System.out.println("+-----+----------------+----------------+---------------+-------+");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id){
        String query = "select * from doctors where id = ?";
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
