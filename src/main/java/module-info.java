module com.example.hrs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.hrs to javafx.fxml;
    exports com.example.hrs;
}