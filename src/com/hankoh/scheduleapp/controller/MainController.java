package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.model.DataStorage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {
    public Tab appointmentsTab;
    public Tab customersTab;
    public Button newAppointmentButton;
    public Button editAppointmentButton;
    public Button deleteAppointmentButton;
    public Button newCustomerButton;
    public Button editCustomerButton;
    public Button deleteCustomerButton;
    public Button logoutButton;
    public Button exitButton;
    public Label customersLabel;
    public Label appointmentsLabel;
    public Text welcomeText;
    public Text mainTitleLabel;

    public void initialize() {
        ResourceBundle msg = ResourceBundle.getBundle(
                "com.hankoh.scheduleapp.properties.MessagesBundle",
                Locale.getDefault()
        );

        String username = DataStorage.getInstance().getUser().getUserName();
        welcomeText.setText(msg.getString("main.welcome") + username);

        appointmentsTab.setText(msg.getString("main.appointments"));
        appointmentsLabel.setText(msg.getString("main.appointments"));
        newAppointmentButton.setText(msg.getString("new"));
        editAppointmentButton.setText(msg.getString("edit"));
        deleteAppointmentButton.setText(msg.getString("delete"));

        customersTab.setText(msg.getString("main.customers"));
        customersLabel.setText(msg.getString("main.customers"));
        newCustomerButton.setText(msg.getString("new"));
        editCustomerButton.setText(msg.getString("edit"));
        deleteCustomerButton.setText(msg.getString("delete"));

        logoutButton.setText(msg.getString("logout"));
        exitButton.setText(msg.getString("exit_button"));
    }

    public void onNewAppointmentButtonClick(ActionEvent actionEvent) {
    }

    public void onEditAppointmentButtonClick(ActionEvent actionEvent) {
    }

    public void onDeleteAppointmentButtonClick(ActionEvent actionEvent) {
    }

    public void onNewCustomerButtonClick(ActionEvent actionEvent) {
    }

    public void onEditCustomerButtonClick(ActionEvent actionEvent) {
    }

    public void onDeleteCustomerButtonClick(ActionEvent actionEvent) {
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) {
    }

    public void onExitButtonClick(ActionEvent actionEvent) {
    }
}
