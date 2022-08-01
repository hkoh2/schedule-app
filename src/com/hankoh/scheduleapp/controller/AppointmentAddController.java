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

/**
 * The type Appointment add controller.
 */
public class AppointmentAddController extends AppointmentController {

    /**
     * Instantiates a new Appointment add controller.
     */
    public AppointmentAddController() {
        super();
    }

    /**
     * Initializing FXML
     *
     * <p>
     *     Lambda for adding listener to various FXML components.
     * </p>
     */
    public void initialize() {
        super.initialize();

        idField.setText(msg.getString("appointment.id_auto"));

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

    /**
     * Sets duration of appointments by available time in 15 minutes intervals.
     */
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

    /**
     * Calculates the max duration for a time chosen.
     * <p>
     *     Lambda for filtering overlapping appointment times using stream.
     * </p>
     *
     * @return Maximum duration of appointment for a time chosen.
     */
    private int getMaxDuration() {

        ZonedDateTime selectedTime = timeComboBox.getSelectionModel().getSelectedItem();
        LocalDate endDate = datePicker.getValue();
        ZonedDateTime businessEndTime = ZonedDateTime.of(endDate, LocalTime.of(22, 0), businessZoneId);

        if (selectedTime == null) {
            return 0;
        }

        Appointment minApt = appointments.stream()
                .filter(apt -> filterTimeAfter(apt, selectedTime))
                .min(Comparator.comparing(Appointment::getStartTime))
                .orElse(null);

        int durationToEnd = (int) Duration.between(selectedTime, businessEndTime).toMinutes();

        if (minApt == null) {
            return Math.min(MAX_DURATION, durationToEnd);
        }

        ZonedDateTime minStart = minApt.getStartTime();
        int nextAppointmentMinutes = (int) Duration.between(selectedTime, minStart).toMinutes();
        int maxMinutes = Math.min(nextAppointmentMinutes, durationToEnd);

        return Math.min(maxMinutes, MAX_DURATION);
    }

    /**
     * Checks if there are any appointment before a chosen time. If time is before appointment if filtered out.
     *
     * @param apt Appointments to filter if before the chosen time
     * @param time Time picked
     * @return Appointments that exists after the selected time
     */
    private boolean filterTimeAfter(Appointment apt, ZonedDateTime time) {
        ZonedDateTime startTime = apt.getStartTime();
        return !startTime.isBefore(time);
    }

    /**
     * Filters available time for a date picked.
     * <p>
     *     Lambda used for stream. Filters any conflicting times from available times.
     * </p>
     *
     * @param date the date picked
     */
    public void filterAvailableTime(LocalDate date) {
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
                .filter(this::getValidTimes)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        timeComboBox.setItems(filteredAvailableTimes);
    }

    /**
     * Filters out any times that exists between existing appointments.
     *
     * <p>
     *     Lambda using streams to filter available times.
     * </p>
     *
     * @param time time chosen
     * @return boolean if time between existing appointments
     */
    private boolean getValidTimes(ZonedDateTime time) {
        if (appointments.isEmpty()) {
            return true;
        }
        boolean isNotValidTime = appointments.stream().anyMatch(apt -> {
            ZonedDateTime aptStartTime = apt.getStartTime();
            ZonedDateTime aptEndTime = apt.getEndTime();
            return !time.isBefore(aptStartTime) && time.isBefore(aptEndTime);
        });
        return !isNotValidTime;
    }

    /**
     * On save button click. Saves new appointments.
     *
     * @param actionEvent the action event
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
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
}
