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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppointmentAddController extends AppointmentController {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    ObservableList<AppointmentDuration> allDuration = FXCollections.observableArrayList();

    private final int MAX_DURATION = 60;
    final ZoneId businessZoneId = ZoneId.of("US/Eastern");


    public AppointmentAddController() {
        super();
    }

    private ObservableList<ZonedDateTime> getAvailTimes(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalTime localStart= LocalTime.of(8, 0);
        LocalTime localEnd = LocalTime.of(21, 45);
        LocalDateTime startTime = LocalDateTime.of(date, localStart);
        LocalDateTime endTime = LocalDateTime.of(date, localEnd);
        ZonedDateTime zonedBusinessStartTime = ZonedDateTime.of(startTime, businessZoneId);
        ZonedDateTime zonedBusinessEndTime = ZonedDateTime.of(endTime, businessZoneId);
        ZonedDateTime zonedStartTimeLocal = zonedBusinessStartTime.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime zonedEndTimeLocal = zonedBusinessEndTime.withZoneSameInstant(ZoneId.systemDefault());
        ObservableList<ZonedDateTime> allTimes = FXCollections.observableArrayList();

        // TODO need to convert eastern time to local time


        allTimes.add(zonedStartTimeLocal);
        while(zonedStartTimeLocal.isBefore(zonedEndTimeLocal)) {
            zonedStartTimeLocal = zonedStartTimeLocal.plusMinutes(DURATION_INC);
            allTimes.add(zonedStartTimeLocal);
            System.out.println(zonedBusinessStartTime);
        }
        return allTimes;
    }

    //private ObservableList<AppointmentDuration> setAllDuration() {
    private void setAllDuration() {
        allDuration.removeAll();
        allDuration.clear();

        int max = getMaxDuration();

        int time = 0;
        while (time < max) {
            time += 15;
            //System.out.println("setting time duration");
            //System.out.println(time);
            allDuration.add(new AppointmentDuration(time));
        }
    }

    private int getMaxDuration() {


        ZonedDateTime selectedTime = timeComboBox.getSelectionModel().getSelectedItem();
        LocalDate endDate = datePicker.getValue();
        ZonedDateTime businessEndTime = ZonedDateTime.of(endDate, LocalTime.of(22, 0), businessZoneId);


        if (selectedTime == null) {
            return 0;
        }

        Appointment minApt = appointments.stream()
                .filter(apt -> filterTimeAfter(apt, selectedTime))
                .peek(apt -> System.out.println(apt.getStartTime()))
                .min(Comparator.comparing(Appointment::getStartTime))
                .orElse(null);

        int durationToEnd = (int) Duration.between(selectedTime, businessEndTime).toMinutes();

        if (minApt == null) {
            System.out.println("duration to end: " + durationToEnd);
            return Math.min(MAX_DURATION, durationToEnd);
        }

        //LocalDateTime minStart = minApt.getStartTime().toLocalDateTime();
        ZonedDateTime minStart = minApt.getStartTime();

        int nextAppointmentMinutes = (int) Duration.between(selectedTime, minStart).toMinutes();
        System.out.println("business end time: " + businessEndTime);
        System.out.println("selected time to end time: " + durationToEnd);

        int maxMinutes = Math.min(nextAppointmentMinutes, durationToEnd);

        //System.out.println("Adjusting DURATION: " + maxMinutes);
        return maxMinutes > MAX_DURATION ? MAX_DURATION : maxMinutes;
    }

    private boolean filterTimeAfter(Appointment apt, ZonedDateTime time) {
        //ZonedDateTime startTime = ZonedDateTime.of(apt.getStartTime().toLocalDateTime(), businessZoneId);
        ZonedDateTime startTime = apt.getStartTime();
        System.out.println("Selected time: " + time);
        System.out.println("Appoint time:  " + startTime);
        if (startTime.isBefore(time)) {
            System.out.println("Appt before selected time");
            return false;
        }
        return true;
    }

    public void initialize() {
        super.initialize();

        int max = 60;

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

        datePicker.valueProperty()
                .addListener((ov, oldVal, newVal) -> filterAvailableTime(newVal));

        customerComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> clearDatePicker());

        timeComboBox.setItems(getAvailTimes(datePicker.getValue()));
        durationComboBox.setItems(allDuration);
        timeComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> setAllDuration());
    }

    //public void clearDatePicker() {
    //    datePicker.setValue(null);
    //}

    public void filterAvailableTime(LocalDate date) {
        System.out.println(date);
        if (date == null) {
            datePicker.setValue(null);
            return;
        }

        AppointmentDao appointmentDao = new AppointmentDao();
        try {
            // gets all appointments in selected date
            appointments = appointmentDao.getAppointmentsByDate(date);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // availTimes is open time slots
        ObservableList<ZonedDateTime> availTimes = getAvailTimes(date);
        ObservableList<ZonedDateTime> filteredAvailableTimes = availTimes.stream()
                // filter needs to return true if time is not conflicting
                // with other appointments
                .filter(time -> getValidTimes(time))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        timeComboBox.setItems(filteredAvailableTimes);

    }

    private boolean getValidTimes(ZonedDateTime time) {
        if (appointments.isEmpty()) {
            System.out.println("Appointment Empty!!!");
            return true;
        }
        boolean isNotValidTime = appointments.stream().anyMatch(apt -> {
            ZonedDateTime aptStartTime = apt.getStartTime();
            ZonedDateTime aptEndTime = apt.getEndTime();
            if (!time.isBefore(aptStartTime) && time.isBefore(aptEndTime)) {
                return true;
            }
            return false;
        });
        return !isNotValidTime;
    }


    //public void onExitButtonClick(ActionEvent actionEvent) throws IOException {
    //    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    //    alert.setTitle(msg.getString("logout"));
    //    alert.setHeaderText(msg.getString("logout"));
    //    alert.setContentText(msg.getString("logout_msg"));
    //    Optional<ButtonType> choice = alert.showAndWait();
    //    if (choice.isPresent() && choice.get() == ButtonType.OK) {
    //        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
    //        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    //        stage.setTitle(msg.getString("login.title"));
    //        stage.setScene(new Scene(root));
    //        stage.show();
    //    }
    //}

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        //String name = nameField.getText();
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDate date = datePicker.getValue();
        ZonedDateTime time = timeComboBox
                .getSelectionModel()
                .getSelectedItem();
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

        if (date == null || date.toString().isEmpty()) {
            startDateError.setText(msg.getString("start_date_empty"));
            inputError = true;
        }

        if (inputError) {
            return;
        } else {
            clearAllError();
        }

        Customer selectedCustomer = customerComboBox
                .getSelectionModel()
                .getSelectedItem();
        User selectedUser = userComboBox
                .getSelectionModel()
                .getSelectedItem();
        Contact selectedContact = contactComboBox
                .getSelectionModel()
                .getSelectedItem();

        ZonedDateTime endTime = time.plusMinutes(duration.getDuration());

        Appointment appointment = new Appointment(
                title,
                description,
                location,
                type,
                time,
                endTime,
                selectedCustomer.getCustomerId(),
                selectedUser.getUserId(),
                selectedContact.getId()
        );

        AppointmentDao appointmentDao = new AppointmentDao();
        appointmentDao.addAppointment(appointment);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("main.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean isEmpty(String input) {
        return input == "";
    }

    private void clearAllError() {
        titleError.setText("");
        descriptionError.setText("");
        locationError.setText("");
        typeError.setText("");
    }

}
