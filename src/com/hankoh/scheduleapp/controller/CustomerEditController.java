package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.model.Country;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.DataStorage;
import com.hankoh.scheduleapp.model.Division;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerEditController extends CustomerController {
    @Override
    public void initialize() {
        super.initialize();
        DataStorage ds = DataStorage.getInstance();
        Customer customer = ds.getCurrentCustomer();

        int customerId = customer.getCustomerId();
        idField.setText(String.valueOf(customerId));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        phoneField.setText(customer.getPhone());
        postalField.setText(customer.getPostalCode());

        Division division = divisions.stream()
                .filter(div -> div.getDivisionId() == customer.getDivisionId())
                .findFirst()
                .orElse(null);
        Country country = countries.stream()
                .filter(coun -> division.getCountryId() == coun.getCountryId())
                .findFirst()
                .orElse(null);

        countryComboBox.getSelectionModel().select(country);
        divisionComboBox.getSelectionModel().select(division);
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

        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String address = addressField.getText();
        Division division = divisionComboBox
                .getSelectionModel()
                .getSelectedItem();
        String postal = postalField.getText();
        String phone = phoneField.getText();

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
                id,
                name,
                address,
                postal,
                phone,
                division.getDivisionId()
        );

        CustomerDao customerDao = new CustomerDao();
        boolean customerEdited = false;
        try {
            customerEdited = customerDao.editCustomer(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (customerEdited) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(msg.getString("customer.updated.title"));
            alert.setHeaderText(msg.getString("customer.updated.header"));
            alert.showAndWait();
            returnToMain(actionEvent);
        } else {
            System.out.println("Customer update error");
        }
    }
}
