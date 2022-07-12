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

public class AppointmentEditController extends AppointmentController {
    public AppointmentEditController() {
        super();
    }

    public void initialize() {
        super.initialize();
        System.out.println("Appointment modify loaded");
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
        filterAvailableTime(startDate, id); //, selectedAppointment.getAppointmentId());
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
        if (startTime.isBefore(time)) {
            return false;
        }
        return true;
    }

    public void filterAvailableTime(LocalDate date, int id) {
        System.out.println(date);
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
                .filter(time -> getValidTimes(time, id))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        timeComboBox.setItems(filteredAvailableTimes);

    }

    private boolean getValidTimes(ZonedDateTime time, int id) {
        if (appointments.isEmpty()) {
            System.out.println("Appointment Empty!!!");
            return true;
        }
        boolean isNotValidTime = appointments.stream()
                // Removes selected appointment time block so that the
                // same time can be selected
                //.filter(appointment -> appointment.getAppointmentId() != id)
                .anyMatch(apt -> {
                    ZonedDateTime aptStartTime = apt.getStartTime();
                    ZonedDateTime aptEndTime = apt.getEndTime();
                    if (!time.isBefore(aptStartTime) && time.isBefore(aptEndTime)) {
                        return true;
                    }
                    return false;
                });
        return !isNotValidTime;
    }
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
