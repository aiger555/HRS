package com.example.hrs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection con;

    public Doctor(Connection con){
        this.con = con;
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
