package com.hankoh.scheduleapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;

public class Main extends Application {
    public static void main(String[] args) {

        // Test location
        Locale.setDefault(new Locale("fr", "FR"));


        System.out.println("Hello world!");
        System.out.println(LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        Locale current = Locale.getDefault();
        String displayCountry = current.getDisplayCountry();

        String country = current.getCountry();
        System.out.println("country - " + country + displayCountry);
        JDBC.makeConnection();

        String query = "SELECT * FROM users";

        try {
            Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("User_Name"));
                System.out.println("PW: " + rs.getString("Password"));
                System.out.println("Time stamp: " + rs.getTimestamp("Create_Date"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml"));
        stage.setTitle("Inventory Management");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }


}
