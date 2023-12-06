package com.example.hrs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        switchToHelloView();
    }

    public void switchToHelloView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Welcome To HRS!");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDoctor() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Doctor.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Doctor View");
        stage.setScene(scene);
    }

    public void switchToPatient() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Patient.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Patient View");
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
