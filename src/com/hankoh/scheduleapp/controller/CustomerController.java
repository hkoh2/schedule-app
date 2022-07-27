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

/**
 * Parent controller for Customer controllers. Shares FXML elements for
 * add and edit.
 */
public class CustomerController extends Internationalizable {
    /**
     * The Name label.
     */
    public Label nameLabel;
    /**
     * The Address label.
     */
    public Label addressLabel;
    /**
     * The Postal label.
     */
    public Label postalLabel;
    /**
     * The Phone label.
     */
    public Label phoneLabel;
    /**
     * The Division label.
     */
    public Label divisionLabel;
    /**
     * The Name field.
     */
    public TextField nameField;
    /**
     * The Address field.
     */
    public TextField addressField;
    /**
     * The Postal field.
     */
    public TextField postalField;
    /**
     * The Phone field.
     */
    public TextField phoneField;
    /**
     * The Division combo box.
     */
    public ComboBox<Division> divisionComboBox;
    /**
     * The Name error label.
     */
    public Label nameErrorLabel;
    /**
     * The Address error label.
     */
    public Label addressErrorLabel;
    /**
     * The Postal error label.
     */
    public Label postalErrorLabel;
    /**
     * The Phone error label.
     */
    public Label phoneErrorLabel;
    /**
     * The Id label.
     */
    public Label idLabel;
    /**
     * The Save button.
     */
    public Button saveButton;
    /**
     * The Cancel button.
     */
    public Button cancelButton;
    /**
     * The Title label.
     */
    public Label titleLabel;
    /**
     * The Id field.
     */
    public TextField idField;
    /**
     * The Country label.
     */
    public Label countryLabel;
    /**
     * The Country combo box.
     */
    public ComboBox<Country> countryComboBox;
    /**
     * Holds all countries available.
     */
    ObservableList<Country> countries = FXCollections.observableArrayList();
    /**
     * Contains all divisions available.
     */
    ObservableList<Division> divisions = FXCollections.observableArrayList();

    /**
     * Initialize.
     */
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

    /**
     * Field validation for customer controllers.
     *
     * @param val     the val
     * @param label   the label
     * @param message the message
     * @return the boolean
     */
    protected boolean fieldIsValid(String val, Label label, String message) {
        if (val == null || val.isBlank()) {
            label.setText(msg.getString(message));
            return false;
        }
        label.setText("");
        return true;
    }

    /**
     * Cancel button. Returns to main screen.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
    }

    /**
     * Save button. Saves new customer and returns to main.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
    }

    /**
     * Adds available divisions to customer controllers.
     *
     * @param options
     * @param oldValue
     * @param newValue
     */
    private void changeDivision(ObservableValue<? extends Country> options, Country oldValue, Country newValue) {
        divisionComboBox.setItems(filterDivision(newValue.getCountryId()));
        divisionComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Filters divisions by country ID.
     *
     * @param countryId
     * @return
     */
    private ObservableList<Division> filterDivision(int countryId) {
        return divisions.stream()
                .filter(div -> div.getCountryId() == countryId)
                .sorted(Comparator.comparing(Division::getDivision))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Return to main.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    protected void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("main.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
