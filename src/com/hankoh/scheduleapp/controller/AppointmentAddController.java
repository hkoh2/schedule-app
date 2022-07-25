package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.Contact;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AppointmentAddController extends AppointmentController {

    public AppointmentAddController() {
        super();
    }

    private void setAllDuration() {
        allDuration.removeAll();
        allDuration.clear();

        int max = getMaxDuration();

        int time = 0;
        while (time < max) {
            time += 15;
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
            return Math.min(MAX_DURATION, durationToEnd);
        }

        ZonedDateTime minStart = minApt.getStartTime();
        int nextAppointmentMinutes = (int) Duration.between(selectedTime, minStart).toMinutes();
        int maxMinutes = Math.min(nextAppointmentMinutes, durationToEnd);

        return maxMinutes > MAX_DURATION ? MAX_DURATION : maxMinutes;
    }

    private boolean filterTimeAfter(Appointment apt, ZonedDateTime time) {
        ZonedDateTime startTime = apt.getStartTime();
        if (startTime.isBefore(time)) {
            return false;
        }
        return true;
    }

    public void initialize() {
        super.initialize();

        customerComboBox.getSelectionModel().selectFirst();
        userComboBox.getSelectionModel().selectFirst();
        contactComboBox.getSelectionModel().selectFirst();

        datePicker.valueProperty()
                .addListener((ov, oldVal, newVal) -> filterAvailableTime(newVal));

        customerComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> clearDatePicker());

        durationComboBox.setItems(allDuration);
        timeComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> setAllDuration());
    }

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
        boolean titleIsValid = fieldIsValid(
                title,
                titleError,
                "title_empty"
        );
        boolean descriptionIsValid = fieldIsValid(
                description,
                descriptionError,
                "description_empty"
        );
        boolean locationIsValid = fieldIsValid(
                location,
                locationError,
                "location_empty"
        );
        boolean typeIsValid = fieldIsValid(
                type,
                typeError,
                "type_empty"
        );

        //if (date == null || date.toString().isEmpty()) {
        //    startDateError.setText(msg.getString("start_date_empty"));
        //    inputError = true;
        //}
        boolean dateIsValid = fieldIsValid(
                date,
                startDateError,
                "start_date_empty"
        );

        boolean timeIsValid = timeFieldIsValid(
                time,
                startTimeError,
                "time_error"
        );

        boolean durationIsValid = comboBoxIsValid(
                duration,
                endTimeError,
                "duration_error"
        );


        if (!titleIsValid ||
                !descriptionIsValid ||
                !locationIsValid ||
                !typeIsValid ||
                !timeIsValid ||
                !durationIsValid ||
                !dateIsValid) {
            System.out.println("Field error");
            return;
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
        returnToMain(actionEvent);
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
