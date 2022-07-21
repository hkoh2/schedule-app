package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.ContactDao;
import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.DAO.UserDao;
import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.Contact;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.Optional;

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
    public TextField nameField;
    public TextField userField;
    public Label userLabel;
    public Label locationLabel;
    public TextField locationField;
    public TextField startHourField;
    public TextField startMinuteField;
    public ComboBox<String> startMeridiemComboBox;
    public TextField endHourField;
    public TextField endMinuteField;
    public ComboBox<String> endMeridiemComboBox;
    public Button saveButton;
    public Button exitButton;
    public Label appointmentTitleLabel;
    public ComboBox<User> userComboBox;
    public ComboBox<Customer> customerComboBox;
    public Label contactLabel;
    public ComboBox<Contact> contactComboBox;
    public Label titleError;
    public Label descriptionError;
    public Label locationError;
    public Label startDateError;
    public Label startTimeError;
    public Label endDateError;
    public Label endTimeError;
    public Label typeError;
    public Label timeLabel;
    public Label durationLabel;
    public Label startEndTimeLabel;
    public ComboBox<AppointmentDuration> durationComboBox;
    public ComboBox<ZonedDateTime> timeComboBox;
    public DatePicker datePicker;
    public Label dateLabel;
    protected final int DURATION_INC = 15;
    protected ObservableList<Customer> customers = FXCollections.observableArrayList();
    protected ObservableList<User> users = FXCollections.observableArrayList();
    protected ObservableList<Contact> contacts = FXCollections.observableArrayList();
    protected ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    protected ObservableList<AppointmentDuration> allDuration = FXCollections.observableArrayList();
    protected final ZoneId businessZoneId = ZoneId.of("US/Eastern");
    protected final int MAX_DURATION = 60;

    public void initialize() {
        appointmentTitleLabel.setText(msg.getString("appointment.main_title"));
        titleLabel.setText(msg.getString("appointment.title"));
        idLabel.setText(msg.getString("appointment.id"));
        nameLabel.setText(msg.getString("appointment.customer_name"));
        descriptionLabel.setText(msg.getString("appointment.description"));
        locationLabel.setText(msg.getString("appointment.location"));
        typeLabel.setText(msg.getString("appointment.type"));
        //startTimeLabel.setText(msg.getString("appointment.start_time"));
        //endTimeLabel.setText(msg.getString("appointment.end_time"));
        userLabel.setText(msg.getString("appointment.user"));

        exitButton.setText(msg.getString("exit_button"));
        saveButton.setText(msg.getString("save_button"));


        CustomerDao customerDao = new CustomerDao();
        try {
            customers = customerDao.getAllCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerComboBox.setItems(customers);

        UserDao userDao = new UserDao();
        try {
            users = userDao.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userComboBox.setItems(users);

        ContactDao contactDao = new ContactDao();
        try {
            contacts = contactDao.getAllContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactComboBox.setItems(contacts);

        datePicker.setDayCellFactory(disableWeekends());
    }

     protected ObservableList<ZonedDateTime> getAvailTimes(LocalDate date) {

        if (date == null) {
            return null;
        }
        LocalTime localStart= LocalTime.of(8, 0);
        LocalTime localEnd = LocalTime.of(21, 45);
        LocalDateTime startTime = LocalDateTime.of(date, localStart);
        LocalDateTime endTime = LocalDateTime.of(date, localEnd);
        ZonedDateTime zonedBusinessStartTime = ZonedDateTime.of(startTime, businessZoneId);
        ZonedDateTime zonedBusinessEndTime = ZonedDateTime.of(endTime, businessZoneId);
        ZonedDateTime zonedStartTimeLocal = zonedBusinessStartTime.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime zonedEndTimeLocal = zonedBusinessEndTime.withZoneSameInstant(ZoneId.systemDefault());
        ObservableList<ZonedDateTime> allTimes = FXCollections.observableArrayList();
        allTimes.add(zonedStartTimeLocal);

        while(zonedStartTimeLocal.isBefore(zonedEndTimeLocal)) {
            zonedStartTimeLocal = zonedStartTimeLocal.plusMinutes(DURATION_INC);
            allTimes.add(zonedStartTimeLocal);
        }
        return allTimes;
    }

    private Callback<DatePicker, DateCell> disableWeekends() {
        final Callback<DatePicker, DateCell> weekendCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #808080;");
                }
            }
        };
        return weekendCellFactory;
    }

    public void onExitButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("logout"));
        alert.setHeaderText(msg.getString("logout"));
        alert.setContentText(msg.getString("logout_msg"));
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle(msg.getString("login.title"));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void clearDatePicker() {
        datePicker.setValue(null);
    }

    protected void returnToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("main.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
