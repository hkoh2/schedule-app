package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.model.Country;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.Division;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerAddController extends CustomerController {

    //ObservableList<Country> countries = FXCollections.observableArrayList();
    //ObservableList<Division> divisions = FXCollections.observableArrayList();

    @Override
    public void initialize() {
        super.initialize();
    }

    private void changeDivision(ObservableValue<? extends Country> options, Country oldValue, Country newValue) {
        System.out.println("Country changed!!!!!");
        divisionComboBox.setItems(filterDivision(newValue.getCountryId()));
        divisionComboBox.getSelectionModel().selectFirst();
    }

    private ObservableList<Division> filterDivision(int countryId) {
        return divisions.stream()
                .filter(div -> div.getCountryId() == countryId)
                .sorted(Comparator.comparing(Division::getDivision))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
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
        int countryId = countryComboBox
                .getSelectionModel()
                .getSelectedItem()
                .getCountryId();
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
            System.out.println("DB Error");
        }
    }
}
