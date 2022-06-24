package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class AppointmentAddController extends AppointmentController {

    public AppointmentAddController() {
        super();
    }

    public void initialize() {
        super.initialize();

        startMerdiemComboBox.getItems().addAll("AM", "PM");
        startMerdiemComboBox.getSelectionModel().selectFirst();
        endMeridiemComboBox.getItems().addAll("AM", "PM");
        endMeridiemComboBox.getSelectionModel().selectFirst();

    }

    public void onExitButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("logout"));
        alert.setHeaderText(msg.getString("logout"));
        alert.setContentText(msg.getString("logout_msg"));
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle(msg.getString("login.title"));
            stage.setScene(new Scene(root));
            stage.show();
        }

    }

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        String name = nameField.getText();
        String Title = titleField.getText();
        String description = descriptionArea.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDate startDate = startDatePicker.getValue();
        String startHour = startHourField.getText();
        String startMinute = startMinuteField.getText();
        LocalDate endDate = endDatePicker.getValue();
        String endHour = endHourField.getText();
        String endMinute = endMinuteField.getText();
        String user = userField.getText();

        Appointment newAppointment = new Appointment();

        AppointmentDao appointmentDao = new AppointmentDao();
        appointmentDao.addAppointment(newAppointment);
    }

}
