package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.DAO.ContactDao;
import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.DAO.UserDao;
import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.Contact;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class AppointmentAddController extends AppointmentController {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();


    public AppointmentAddController() {
        super();
    }

    private ObservableList<LocalTime> getAllTime() {
        LocalTime time = LocalTime.of(8, 0);
        ObservableList<LocalTime> allTimes = FXCollections.observableArrayList();
        allTimes.add(time);
        while(time.isBefore(LocalTime.of(21, 45))) {
            time = time.plusMinutes(15);
            allTimes.add(time);
            System.out.println(time);
        }
        return allTimes;
    }

    private ObservableList<AppointmentDuration> getAllDuration(int max) {

        int time = 0;
        //AppointmentDuration duration = new AppointmentDuration(time);
        ObservableList<AppointmentDuration> allDuration = FXCollections.observableArrayList();
        //allDuration.add(duration);
        while (time < max) {
            time += 15;
            allDuration.add(new AppointmentDuration(time));
        }
        return allDuration;
    }

    public void initialize() {
        super.initialize();

        //DataStorage ds = DataStorage.getInstance();
        //ds.setCurrentTab(1);

        // add all available time in increments of 15 minutes from 8am to 10pm


        timeComboBox.setItems(getAllTime());
        //timeComboBox.getSelectionModel().selectFirst();

        int max = 60;
        durationComboBox.setItems(getAllDuration(max));

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
        LocalDate date = datePicker.getValue();
        LocalTime time = timeComboBox
                .getSelectionModel()
                .getSelectedItem();
        //String strStartHour = startHourField.getText();
        //String strStartMinute = startMinuteField.getText();
        //String startMeridiem = startMeridiemComboBox.getValue();
        //LocalDate endDate = endDatePicker.getValue();
        //String strEndHour = endHourField.getText();
        //String strEndMinute = endMinuteField.getText();
        //String endMeridiem = endMeridiemComboBox.getValue();
        //String userName = userField.getText();
        AppointmentDuration duration = durationComboBox
                .getSelectionModel()
                .getSelectedItem();

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

        //if (startDate == null || startDate.toString().isEmpty()) {
        //    startDateError.setText(msg.getString("start_date_empty"));
        //    inputError = true;
        //}

        //if (strStartHour.isEmpty() || strStartMinute.isEmpty()) {
        //    startTimeError.setText(msg.getString("start_time_empty"));
        //    inputError = true;
        //}

        if (inputError) {
            return;
        } else {
            clearAllError();
        }

        //String startHour = startMeridiem == "AM" ? strStartHour : meridiemToMil(strStartHour);
        //String endHour = endMeridiem == "AM" ? strEndHour : meridiemToMil(strEndHour);
        //String st = startDate + " " + startHour + ":" + strStartMinute + ":00";
        //String et = startDate + " " + endHour + ":" + strEndMinute + ":00";

        Customer selectedCustomer = customerComboBox
                .getSelectionModel()
                .getSelectedItem();
        User selectedUser = userComboBox
                .getSelectionModel()
                .getSelectedItem();
        Contact selectedContact = contactComboBox
                .getSelectionModel()
                .getSelectedItem();

        LocalDateTime st = LocalDateTime.of(date, time);
        LocalDateTime et = LocalDateTime.of(
                date,
                time.plusMinutes(duration.getDuration())
        );
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
