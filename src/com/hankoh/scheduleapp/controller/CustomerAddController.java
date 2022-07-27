package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.model.Customer;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerAddController extends CustomerController {

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("customer.cancel"));
        alert.setHeaderText(msg.getString("customer.confirm_cancel"));
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            returnToMain(actionEvent);
        }
    }

    @Override
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        String name = nameField.getText();
        String address = addressField.getText();
        String postal = postalField.getText();
        String phone = phoneField.getText();
        int divisionId = divisionComboBox
                .getSelectionModel()
                .getSelectedItem()
                .getDivisionId();

        boolean nameIsValid = fieldIsValid(
                name,
                nameErrorLabel,
                "name_error"
        );

        boolean addressIsValid = fieldIsValid(
                address,
                addressErrorLabel,
                "address_error"
        );

        boolean postalIsValid = fieldIsValid(
                postal,
                postalErrorLabel,
                "postal_error"
        );

        boolean phoneIsValid = fieldIsValid(
                phone,
                phoneErrorLabel,
                "phone_error"
        );

        if (!nameIsValid ||
                !addressIsValid ||
                !postalIsValid ||
                !phoneIsValid) {
            return;
        }

        Customer customer = new Customer(
                name,
                address,
                postal,
                phone,
                divisionId
        );

        CustomerDao customerDao = new CustomerDao();

        boolean customerAdded = false;
        try {
            customerAdded = customerDao.addCustomer(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (customerAdded) {

            returnToMain(actionEvent);
        } else {
            System.out.println("Customer Add Error");
        }
    }
}
