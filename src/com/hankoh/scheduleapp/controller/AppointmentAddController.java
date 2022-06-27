package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.DAO.UserDao;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    ObservableList<Customer> customers = FXCollections.observableArrayList();
    ObservableList<User> users = FXCollections.observableArrayList();

    public AppointmentAddController() {
        super();
    }

    public void initialize() {
        super.initialize();

        startMeridiemComboBox.getItems().addAll("AM", "PM");
        startMeridiemComboBox.getSelectionModel().selectFirst();
        endMeridiemComboBox.getItems().addAll("AM", "PM");
        endMeridiemComboBox.getSelectionModel().selectFirst();

        CustomerDao customerDao = new CustomerDao();
        try {
            customers = customerDao.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerComboBox.setItems(customers);
        customerComboBox.getSelectionModel().selectFirst();

        UserDao userDao = new UserDao();
        try {
            users = userDao.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userComboBox.setItems(users);
        userComboBox.getSelectionModel().selectFirst();

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
        String startMeridiem = startMeridiemComboBox.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String strEndHour = endHourField.getText();
        String strEndMinute = endMinuteField.getText();
        String endMeridiem = endMeridiemComboBox.getValue();
        String userName = userField.getText();

        String startHour = startMeridiem == "AM" ? strStartHour : meridiemToMil(strStartHour);
        String endHour = endMeridiem == "AM" ? strEndHour : meridiemToMil(strEndHour);
        String st = startDate + " " + startHour + ":" + strStartMinute + ":00";
        String et = endDate + " " + endHour + ":" + strEndMinute + ":00";

        //Timestamp startTime = Timestamp.valueOf(startDate + " " + startHour + ":" + strStartMinute + ":00");
        //Timestamp endTime = Timestamp.valueOf(endDate + " " + endHour + ":" + strEndMinute + ":00");

        System.out.println(st);
        System.out.println(et);

        //Appointment appointment = new Appointment(
        //        title,
        //        description,
        //        location,
        //        type,
        //        st,
        //        et,
        //        customerId,
        //        userId,
        //        contactId
        //);

        //int startHour = Integer.parseInt(strStartHour);
        //int startMinute = Integer.parseInt(strStartMinute);
        //LocalTime startHourMinute = LocalTime.of(startHour, startMinute);
        //LocalDateTime startTime = LocalDateTime.of(startDate, startHourMinute);

        //int endHour = Integer.parseInt(strEndHour);
        //int endMinute = Integer.parseInt(strEndMinute);
        //LocalTime endHourMinute = LocalTime.of(endHour, endMinute);
        //LocalDateTime endTime = LocalDateTime.of(endDate, endHourMinute);



        Timestamp s = Timestamp.valueOf("2020-05-28 8:30:00");
        Timestamp e = Timestamp.valueOf("2020-05-28 10:30:00");

        //Appointment test = new Appointment("test title", "test description", "test location", "test type", s, e, 1, 2, 3);
        //Appointment test2 = new Appointment("updated test title", "updated test description", "updated test location", "test type", s, e, 1, 2, 3);
        //AppointmentDao appointmentDao = new AppointmentDao();
        //appointmentDao.addAppointment(test);
        //appointmentDao.updateAppointment(test2);
    }

    private String meridiemToMil(String hour) {
        return String.valueOf((Integer.parseInt(hour) + 12));
    }

}
