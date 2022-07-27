package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.CountryDao;
import com.hankoh.scheduleapp.DAO.DivisionDao;
import com.hankoh.scheduleapp.model.Country;
import com.hankoh.scheduleapp.model.Division;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

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
    ObservableList<Country> countries = FXCollections.observableArrayList();
    ObservableList<Division> divisions = FXCollections.observableArrayList();

    public void initialize() {
        titleLabel.setText(msg.getString("customer.title"));
        nameLabel.setText(msg.getString("customer.name"));
        addressLabel.setText(msg.getString("customer.address"));
        postalLabel.setText(msg.getString("customer.postal"));
        phoneLabel.setText(msg.getString("customer.phone"));
        divisionLabel.setText(msg.getString("customer.division"));

        CountryDao countryDao = new CountryDao();
        try {
            countries = countryDao.getAllCountries();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        countryComboBox.setItems(countries);
        countryComboBox.getSelectionModel().selectFirst();
        countryComboBox.valueProperty().addListener(this::changeDivision);

        DivisionDao divisionDao = new DivisionDao();
        try {
            divisions = divisionDao.getAllDivisions();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int selectedCountryId = countryComboBox
                .getSelectionModel()
                .getSelectedItem()
                .getCountryId();

        ObservableList<Division> filteredDivision = divisions.stream()
                .filter(div -> div.getCountryId() == selectedCountryId)
                .sorted(Comparator.comparing(Division::getDivision))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        divisionComboBox.setItems(filteredDivision);
        divisionComboBox.getSelectionModel().selectFirst();
    }

    protected boolean fieldIsValid(String val, Label label, String message) {
        if (val == null || val.isBlank()) {
            label.setText(msg.getString(message));
            return false;
        }
        label.setText("");
        return true;
    }

    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
    }

    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
    }

    private void changeDivision(ObservableValue<? extends Country> options, Country oldValue, Country newValue) {
        divisionComboBox.setItems(filterDivision(newValue.getCountryId()));
        divisionComboBox.getSelectionModel().selectFirst();
    }

    private ObservableList<Division> filterDivision(int countryId) {
        return divisions.stream()
                .filter(div -> div.getCountryId() == countryId)
                .sorted(Comparator.comparing(Division::getDivision))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

    }
    protected void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("main.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
