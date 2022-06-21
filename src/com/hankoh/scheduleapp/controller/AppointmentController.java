package com.hankoh.scheduleapp.controller;

import javafx.scene.control.*;

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
    public TextField labelField;
    public TextField userField;
    public Label userLabel;
    public Label locationLabel;
    public TextField locationField;
    public TextField startHourField;
    public TextField startMinuteField;
    public ComboBox startMerdiemComboBox;
    public TextField endHourField;
    public TextField endMinuteField;
    public ComboBox endMeridiemComboBox;
    public Button saveButton;
    public Button exitButton;
    public Label appointmentTitleLabel;

    public void initialize() {
        appointmentTitleLabel.setText(msg.getString("appointment.main_title"));
        titleLabel.setText(msg.getString("appointment.title"));
        idLabel.setText(msg.getString("appointment.id"));
        nameLabel.setText(msg.getString("appointment.customer_name"));
        descriptionLabel.setText(msg.getString("appointment.description"));
        locationLabel.setText(msg.getString("appointment.location"));
        typeLabel.setText(msg.getString("appointment.type"));
        startTimeLabel.setText(msg.getString("appointment.start_time"));
        endTimeLabel.setText(msg.getString("appointment.end_time"));
        userLabel.setText(msg.getString("appointment.user"));

        exitButton.setText(msg.getString("exit_button"));
        saveButton.setText(msg.getString("save_button"));
    }

}
