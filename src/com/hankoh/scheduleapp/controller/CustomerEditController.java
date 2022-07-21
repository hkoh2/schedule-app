package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.model.Country;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.DataStorage;
import com.hankoh.scheduleapp.model.Division;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

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

        int divisionId = customer.getDivisionId();

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
        moveToMain(actionEvent);
    }

    @Override
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {

        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String address = addressField.getText();
        Country country = countryComboBox
                .getSelectionModel()
                .getSelectedItem();
        Division division = divisionComboBox
                .getSelectionModel()
                .getSelectedItem();
        String postal = postalField.getText();
        String phone = phoneField.getText();

        Customer customer = new Customer(
                id,
                name,
                address,
                postal,
                phone,
                division.getDivisionId()
        );

        CustomerDao customerDao = new CustomerDao();
        try {
            customerDao.editCustomer(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
