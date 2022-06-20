package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.DataStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
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
    public ComboBox<String> appointmentFilterCombo;
    public TableColumn<Appointment, String> appointmentUserColumn;
    public TableColumn<Appointment, String> appointmentCustomerColumn;
    public TableColumn<Appointment, Time> appointmentEndColumn;
    public TableColumn<Appointment, String> appointmentStartColumn;
    public TableColumn<Appointment, String> appointmentTypeColumn;
    public TableColumn<Appointment, String> appointmentLocationColumn;
    public TableColumn<Appointment, String> appointmentDescriptionColumn;
    public TableColumn<Appointment, String> appointmentTitleColumn;
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    public TableView<Appointment> appointmentsTable;
    ResourceBundle msg;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        msg = ResourceBundle.getBundle(
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

        String strAll = msg.getString("appointment.all");
        String strMonth = msg.getString("appointment.month");
        String strWeek = msg.getString("appointment.week");
        appointmentFilterCombo
                .getItems()
                .addAll(strAll, strMonth, strWeek);
        appointmentFilterCombo
                .getSelectionModel()
                .select(strAll);

        customersTab.setText(msg.getString("main.customers"));
        customersLabel.setText(msg.getString("main.customers"));
        newCustomerButton.setText(msg.getString("new"));
        editCustomerButton.setText(msg.getString("edit"));
        deleteCustomerButton.setText(msg.getString("delete"));

        logoutButton.setText(msg.getString("logout"));
        exitButton.setText(msg.getString("exit_button"));


        AppointmentDao appointmentDao = new AppointmentDao();
        appointments = appointmentDao.getAllAppointments();
        appointments.forEach(appt -> System.out.println(appt.getAppointmentId()));

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentUserColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        appointmentsTable.setItems(appointments);

        
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

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("logout"));
        alert.setHeaderText(msg.getString("logout"));
        alert.setContentText(msg.getString("logout_msg"));
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            DataStorage.getInstance().clearAll();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/login.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle(msg.getString("login.title"));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void onExitButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("confirm"));
        alert.setHeaderText(msg.getString("confirm"));
        alert.setContentText(msg.getString("exit_msg"));
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            stage.close();
        }
    }
}
