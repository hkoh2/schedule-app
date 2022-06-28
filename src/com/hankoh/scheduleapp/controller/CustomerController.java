package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.model.Country;
import com.hankoh.scheduleapp.model.Division;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CustomerController extends Internationalizable {
    public Label nameLabel;
    public Label addressLabel;
    public Label postalLabel;
    public Label phoneLabel;
    public Label divisionLabel;
    public TextField nameField;
    public TextField addressField;
    public TextField postalField;
    public TextField phoneField;
    public ComboBox<Division> divisionComboBox;
    public Label nameErrorLabel;
    public Label addressErrorLabel;
    public Label postalErrorLabel;
    public Label phoneErrorLabel;
    public Label idLabel;
    public Button saveButton;
    public Button cancelButton;
    public Label titleLabel;
    public TextField idField;
    public Label countryLabel;
    public ComboBox<Country> countryComboBox;

    public void initialize() {
        titleLabel.setText(msg.getString("customer.title"));
        nameLabel.setText(msg.getString("customer.name"));
        addressLabel.setText(msg.getString("customer.address"));
        postalLabel.setText(msg.getString("customer.postal"));
        phoneLabel.setText(msg.getString("customer.phone"));
        divisionLabel.setText(msg.getString("customer.division"));

    }

    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
    }
}
