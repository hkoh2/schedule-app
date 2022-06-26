package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        String name = nameField.getText();
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDate startDate = startDatePicker.getValue();
        String strStartHour = startHourField.getText();
        String strStartMinute = startMinuteField.getText();
        LocalDate endDate = endDatePicker.getValue();
        String strEndHour = endHourField.getText();
        String strEndMinute = endMinuteField.getText();
        String userName = userField.getText();

        //int startHour = Integer.parseInt(strStartHour);
        //int startMinute = Integer.parseInt(strStartMinute);
        //LocalTime startHourMinute = LocalTime.of(startHour, startMinute);
        //LocalDateTime startTime = LocalDateTime.of(startDate, startHourMinute);

        //int endHour = Integer.parseInt(strEndHour);
        //int endMinute = Integer.parseInt(strEndMinute);
        //LocalTime endHourMinute = LocalTime.of(endHour, endMinute);
        //LocalDateTime endTime = LocalDateTime.of(endDate, endHourMinute);

        Customer customer = new Customer(
                0,
                name,
                "address",
                "postal_code",
                "phone",
                1234
        );

        User user = new User(
                0,
                userName
        );

        //Appointment newAppointment = new Appointment(
        //        title,
        //        description,
        //        location,
        //        type,
        //        startTime,
        //        endTime,
        //        customer,
        //        user,
        //        4321
        //);

        //// USE ZonedDateTime!!!!!!!!!!!!!!!!!!!

        Timestamp s = new Timestamp(System.currentTimeMillis());
        Timestamp e = new Timestamp(System.currentTimeMillis());

        Appointment test = new Appointment("test title", "test description", "test location", "test type", s, e, 1, 2, 3);
        AppointmentDao appointmentDao = new AppointmentDao();
        appointmentDao.addAppointment(test);
    }

}
