package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.model.Contact;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.ZonedDateTime;

public class AppointmentController extends Internationalizable {
    public TextField idField;
    public TextField titleField;
    public Label idLabel;
    public Label titleLabel;
    public Label descriptionLabel;
    public TextArea descriptionArea;
    public Label typeLabel;
    public Label startTimeLabel;
    public Label endTimeLabel;
    public TextField typeField;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public Label nameLabel;
    public TextField nameField;
    public TextField userField;
    public Label userLabel;
    public Label locationLabel;
    public TextField locationField;
    public TextField startHourField;
    public TextField startMinuteField;
    public ComboBox<String> startMeridiemComboBox;
    public TextField endHourField;
    public TextField endMinuteField;
    public ComboBox<String> endMeridiemComboBox;
    public Button saveButton;
    public Button exitButton;
    public Label appointmentTitleLabel;
    public ComboBox<User> userComboBox;
    public ComboBox<Customer> customerComboBox;
    public Label contactLabel;
    public ComboBox<Contact> contactComboBox;
    public Label titleError;
    public Label descriptionError;
    public Label locationError;
    public Label startDateError;
    public Label startTimeError;
    public Label endDateError;
    public Label endTimeError;

    public Label typeError;
    public Label timeLabel;
    public Label durationLabel;
    public Label startEndTimeLabel;
    public ComboBox<AppointmentDuration> durationComboBox;
    public ComboBox<ZonedDateTime> timeComboBox;
    public DatePicker datePicker;
    public Label dateLabel;

    //protected LocalTime businessStartTime = ZonedDateTime.of;
    //protected ZonedDateTime businessEndTime;

    public void initialize() {
        appointmentTitleLabel.setText(msg.getString("appointment.main_title"));
        titleLabel.setText(msg.getString("appointment.title"));
        idLabel.setText(msg.getString("appointment.id"));
        nameLabel.setText(msg.getString("appointment.customer_name"));
        descriptionLabel.setText(msg.getString("appointment.description"));
        locationLabel.setText(msg.getString("appointment.location"));
        typeLabel.setText(msg.getString("appointment.type"));
        //startTimeLabel.setText(msg.getString("appointment.start_time"));
        //endTimeLabel.setText(msg.getString("appointment.end_time"));
        userLabel.setText(msg.getString("appointment.user"));

        exitButton.setText(msg.getString("exit_button"));
        saveButton.setText(msg.getString("save_button"));
    }

    public void onExitButtonClick(ActionEvent actionEvent) throws IOException {
    }
}
