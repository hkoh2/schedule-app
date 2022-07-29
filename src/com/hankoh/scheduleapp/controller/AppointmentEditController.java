package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.model.*;
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
 * Controller for updating existing appointments.
 */
public class AppointmentEditController extends AppointmentController {
    /**
     * Instantiates a new Appointment edit controller.
     */
    public AppointmentEditController() {
        super();
    }

    /**
     * Initializes appointment edit screen.
     *
     * <p>
     *     Lambda used for streams to select customer, user, and contacts for
     *     an existing appointment. Additional lambdas used for event
     *     listener.
     * </p>
     */
    public void initialize() {
        super.initialize();
        DataStorage ds = DataStorage.getInstance();
        Appointment selectedAppointment = ds.getCurrentAppointment();

        idField.setText(String.valueOf(selectedAppointment.getAppointmentId()));

        Customer customer = customers.stream()
                .filter(cust -> cust.getCustomerId() == selectedAppointment.getCustomerId())
                .findAny()
                .orElse(null);
        customerComboBox.getSelectionModel().select(customer);

        User currentUser = users.stream()
                .filter(user -> user.getUserId() == selectedAppointment.getUserId())
                .findAny()
                .orElse(null);
        userComboBox.getSelectionModel().select(currentUser);

        Contact currentContact = contacts.stream()
                .filter(contact -> contact.getId() == selectedAppointment.getContactId())
                .findAny()
                .orElse(null);
        contactComboBox.getSelectionModel().select(currentContact);

        titleField.setText(selectedAppointment.getTitle());
        descriptionArea.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        typeField.setText(selectedAppointment.getType());

        ZonedDateTime startDateTime = selectedAppointment.getStartTime();
        ZonedDateTime endDateTime = selectedAppointment.getEndTime();
        LocalDate startDate = selectedAppointment.getStartTime().toLocalDate();

        int id = selectedAppointment.getAppointmentId();
        datePicker.setValue(startDate);
        filterAvailableTime(startDate, id);
        setAllDuration();
        timeComboBox.getSelectionModel().select(startDateTime);

        AppointmentDuration currentDuration = new AppointmentDuration(
                (int) Duration.between(startDateTime, endDateTime).toMinutes()
        );

        durationComboBox.setItems(allDuration);
        setAllDuration();
        durationComboBox.getSelectionModel().select(currentDuration);

        datePicker.valueProperty()
                .addListener((ov, oldVal, newVal) -> filterAvailableTime(newVal, id));

        customerComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> clearDatePicker());

        timeComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> setAllDuration());
    }

    /**
     * Sets available duration for picked time.
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
     * Returns maximum duration a selected time.
     *
     * <p>
     *     Lambda used in stream to find earliest appointments from the
     *     selected time. If appointment is closer than the maximum duration
     *     allowed, maximum duration is adjusted.
     * </p>
     *
     * @return Max duration for selected time
     */
    private int getMaxDuration() {
        ZonedDateTime selectedTime = timeComboBox.getSelectionModel().getSelectedItem();
        if (selectedTime == null) {
            return 0;
        }

        LocalDate endDate = datePicker.getValue();
        ZonedDateTime businessEndTime = ZonedDateTime.of(endDate, LocalTime.of(22, 0), businessZoneId);

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
        return Math.min(maxMinutes, MAX_DURATION);
    }

    private boolean filterTimeAfter(Appointment apt, ZonedDateTime time) {
        ZonedDateTime startTime = apt.getStartTime();
        return !startTime.isBefore(time);
    }

    /**
     * Filters available time.
     * <p>
     *     Lambda used for stream to filter available time from
     *     all available time.
     * </p>
     *
     * @param date the date
     * @param id   the id
     */
    public void filterAvailableTime(LocalDate date, int id) {
        timeComboBox.setValue(null);
        timeComboBox.setItems(null);
        durationComboBox.setValue(null);
        if (date == null) {
            datePicker.setValue(null);
            return;
        }

        AppointmentDao appointmentDao = new AppointmentDao();
        try {
            // gets all appointments in selected date
            appointments = appointmentDao.getAppointmentsByDate(date).stream()
                    .filter(appointment -> appointment.getAppointmentId() != id)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
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
     * Checks for overlapping appointments
     * <p>
     *     Lambda used in stream to find if the time is
     *     during an existing appointment.
     * </p>
     *
     * @param time time selected
     * @return confirms valid time.
     */
    private boolean getValidTimes(ZonedDateTime time) {
        if (appointments.isEmpty()) {
            return true;
        }
        boolean isNotValidTime = appointments.stream()
                // Removes selected appointment time block so that the
                // same time can be selected
                .anyMatch(apt -> {
                    ZonedDateTime aptStartTime = apt.getStartTime();
                    ZonedDateTime aptEndTime = apt.getEndTime();
                    return !time.isBefore(aptStartTime) && time.isBefore(aptEndTime);
                });
        return !isNotValidTime;
    }

    /**
     * Saves updated appointments.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public void onSaveButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
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

        DataStorage ds = DataStorage.getInstance();
        Appointment selectedAppointment = ds.getCurrentAppointment();
        int id = selectedAppointment.getAppointmentId();
        Appointment appointment = new Appointment(
                id,
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
        appointmentDao.updateAppointment(appointment);
        returnToMain(actionEvent);
    }
}
