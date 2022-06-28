package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.DAO.ContactDao;
import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.DAO.UserDao;
import com.hankoh.scheduleapp.model.*;
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
    ObservableList<Contact> contacts = FXCollections.observableArrayList();

    public AppointmentAddController() {
        super();
    }

    public void initialize() {
        super.initialize();

        //DataStorage ds = DataStorage.getInstance();
        //ds.setCurrentTab(1);

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

        ContactDao contactDao = new ContactDao();
        try {
            contacts = contactDao.getAllContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactComboBox.setItems(contacts);
        contactComboBox.getSelectionModel().selectFirst();

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
        //String name = nameField.getText();
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDate startDate = startDatePicker.getValue();
        String strStartHour = startHourField.getText();
        String strStartMinute = startMinuteField.getText();
        String startMeridiem = startMeridiemComboBox.getValue();
        //LocalDate endDate = endDatePicker.getValue();
        String strEndHour = endHourField.getText();
        String strEndMinute = endMinuteField.getText();
        String endMeridiem = endMeridiemComboBox.getValue();
        //String userName = userField.getText();

        // validate title
        boolean inputError = false;
        if (title.isEmpty()) {
            titleError.setText(msg.getString("title_empty"));
            inputError = true;
        }

        if (description.isEmpty()) {
            descriptionError.setText(msg.getString("description_empty"));
            inputError = true;
        }

        if (location.isEmpty()) {
            locationError.setText(msg.getString("location_empty"));
            inputError = true;
        }

        if (type.isEmpty()) {
            typeError.setText(msg.getString("type_empty"));
            inputError = true;
        }

        if (startDate == null || startDate.toString().isEmpty()) {
            startDateError.setText(msg.getString("start_date_empty"));
            inputError = true;
        }

        if (strStartHour.isEmpty() || strStartMinute.isEmpty()) {
            startTimeError.setText(msg.getString("start_time_empty"));
            inputError = true;
        }

        if (inputError) {
            return;
        } else {
            clearAllError();
        }

        String startHour = startMeridiem == "AM" ? strStartHour : meridiemToMil(strStartHour);
        String endHour = endMeridiem == "AM" ? strEndHour : meridiemToMil(strEndHour);
        String st = startDate + " " + startHour + ":" + strStartMinute + ":00";
        String et = startDate + " " + endHour + ":" + strEndMinute + ":00";

        Customer selectedCustomer = customerComboBox
                .getSelectionModel()
                .getSelectedItem();
        User selectedUser = userComboBox
                .getSelectionModel()
                .getSelectedItem();
        Contact selectedContact = contactComboBox
                .getSelectionModel()
                .getSelectedItem();

        Timestamp startTime = Timestamp.valueOf(st);
        Timestamp endTime = Timestamp.valueOf(et);

        //System.out.println(st);
        //System.out.println(et);

        Appointment appointment = new Appointment(
                title,
                description,
                location,
                type,
                startTime,
                endTime,
                selectedCustomer.getCustomerId(),
                selectedUser.getUserId(),
                selectedContact.getId()
        );

        //int startHour = Integer.parseInt(strStartHour);
        //int startMinute = Integer.parseInt(strStartMinute);
        //LocalTime startHourMinute = LocalTime.of(startHour, startMinute);
        //LocalDateTime startTime = LocalDateTime.of(startDate, startHourMinute);

        //int endHour = Integer.parseInt(strEndHour);
        //int endMinute = Integer.parseInt(strEndMinute);
        //LocalTime endHourMinute = LocalTime.of(endHour, endMinute);
        //LocalDateTime endTime = LocalDateTime.of(endDate, endHourMinute);



        //Timestamp s = Timestamp.valueOf("2020-05-28 8:30:00");
        //Timestamp e = Timestamp.valueOf("2020-05-28 10:30:00");

        //Appointment test = new Appointment("test title", "test description", "test location", "test type", s, e, 1, 2, 3);
        //Appointment test2 = new Appointment("updated test title", "updated test description", "updated test location", "test type", s, e, 1, 2, 3);
        AppointmentDao appointmentDao = new AppointmentDao();
        appointmentDao.addAppointment(appointment);
        //appointmentDao.updateAppointment(test2);
    }

    private String meridiemToMil(String hour) {
        return String.valueOf((Integer.parseInt(hour) + 12));
    }

    private boolean isEmpty(String input) {
        return input == "";
    }

    private void clearAllError() {
        titleError.setText("");
        descriptionError.setText("");
        locationError.setText("");
        typeError.setText("");
        startTimeError.setText("");
        startDateError.setText("");
        endTimeError.setText("");
        //endDateError.setText("");
    }

}
