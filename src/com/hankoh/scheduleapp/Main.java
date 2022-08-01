package com.hankoh.scheduleapp;

import com.hankoh.scheduleapp.DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Main class. Entry point for application. Database connection initiated.
 */
public class Main extends Application {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        // Test location
        JDBC.makeConnection();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle msg = ResourceBundle.getBundle("com.hankoh.scheduleapp.properties.MessagesBundle", Locale.getDefault());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/login.fxml")));
        stage.setTitle(msg.getString("title"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
