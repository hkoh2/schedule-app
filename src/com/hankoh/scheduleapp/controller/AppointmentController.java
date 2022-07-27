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
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Appointment controller.
 */
public class AppointmentController extends Internationalizable {
    /**
     * The Id field.
     */
    public TextField idField;
    /**
     * The Title field.
     */
    public TextField titleField;
    /**
     * The Id label.
     */
    public Label idLabel;
    /**
     * The Title label.
     */
    public Label titleLabel;
    /**
     * The Description label.
     */
    public Label descriptionLabel;
    /**
     * The Description area.
     */
    public TextArea descriptionArea;
    /**
     * The Type label.
     */
    public Label typeLabel;
    /**
     * The Start time label.
     */
    public Label startTimeLabel;
    /**
     * The End time label.
     */
    public Label endTimeLabel;
    /**
     * The Type field.
     */
    public TextField typeField;
    /**
     * The Start date picker.
     */
    public DatePicker startDatePicker;
    /**
     * The End date picker.
     */
    public DatePicker endDatePicker;
    /**
     * The Name label.
     */
    public Label nameLabel;
    /**
     * The Name field.
     */
    public TextField nameField;
    /**
     * The User field.
     */
    public TextField userField;
    /**
     * The User label.
     */
    public Label userLabel;
    /**
     * The Location label.
     */
    public Label locationLabel;
    /**
     * The Location field.
     */
    public TextField locationField;
    /**
     * The Start hour field.
     */
    public TextField startHourField;
    /**
     * The Start minute field.
     */
    public TextField startMinuteField;
    /**
     * The Start meridiem combo box.
     */
    public ComboBox<String> startMeridiemComboBox;
    /**
     * The End hour field.
     */
    public TextField endHourField;
    /**
     * The End minute field.
     */
    public TextField endMinuteField;
    /**
     * The End meridiem combo box.
     */
    public ComboBox<String> endMeridiemComboBox;
    /**
     * The Save button.
     */
    public Button saveButton;
    /**
     * The Exit button.
     */
    public Button exitButton;
    /**
     * The Appointment title label.
     */
    public Label appointmentTitleLabel;
    /**
     * The User combo box.
     */
    public ComboBox<User> userComboBox;
    /**
     * The Customer combo box.
     */
    public ComboBox<Customer> customerComboBox;
    /**
     * The Contact label.
     */
    public Label contactLabel;
    /**
     * The Contact combo box.
     */
    public ComboBox<Contact> contactComboBox;
    /**
     * The Title error.
     */
    public Label titleError;
    /**
     * The Description error.
     */
    public Label descriptionError;
    /**
     * The Location error.
     */
    public Label locationError;
    /**
     * The Start date error.
     */
    public Label startDateError;
    /**
     * The Start time error.
     */
    public Label startTimeError;
    /**
     * The End time error.
     */
    public Label endTimeError;
    /**
     * The Type error.
     */
    public Label typeError;
    /**
     * The Time label.
     */
    public Label timeLabel;
    /**
     * The Duration label.
     */
    public Label durationLabel;
    /**
     * The Start end time label.
     */
    public Label startEndTimeLabel;
    /**
     * The Duration combo box.
     */
    public ComboBox<AppointmentDuration> durationComboBox;
    /**
     * The Time combo box.
     */
    public ComboBox<ZonedDateTime> timeComboBox;
    /**
     * The Date picker.
     */
    public DatePicker datePicker;
    /**
     * The Date label.
     */
    public Label dateLabel;
    /**
     * The Duration inc.
     */
    protected final int DURATION_INC = 15;
    /**
     * The Customers.
     */
    protected ObservableList<Customer> customers = FXCollections.observableArrayList();
    /**
     * The Users.
     */
    protected ObservableList<User> users = FXCollections.observableArrayList();
    /**
     * The Contacts.
     */
    protected ObservableList<Contact> contacts = FXCollections.observableArrayList();
    /**
     * The Appointments.
     */
    protected ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    /**
     * The All duration.
     */
    protected ObservableList<AppointmentDuration> allDuration = FXCollections.observableArrayList();
    /**
     * The Business zone id.
     */
    protected final ZoneId businessZoneId = ZoneId.of("US/Eastern");
    /**
     * The Max duration.
     */
    protected final int MAX_DURATION = 60;
    private boolean dateError = false;

    /**
     * Initialize.
     */
    public void initialize() {
        appointmentTitleLabel.setText(msg.getString("appointment.main_title"));
        titleLabel.setText(msg.getString("appointment.title"));
        idLabel.setText(msg.getString("appointment.id"));
        nameLabel.setText(msg.getString("appointment.customer_name"));
        descriptionLabel.setText(msg.getString("appointment.description"));
        locationLabel.setText(msg.getString("appointment.location"));
        typeLabel.setText(msg.getString("appointment.type"));
        userLabel.setText(msg.getString("appointment.user"));
        timeLabel.setText(msg.getString("appointment.start_time"));
        timeComboBox.setPromptText(msg.getString("appointment.prompt_time"));
        durationLabel.setText(msg.getString("appointment.duration"));
        durationComboBox.setPromptText(msg.getString("appointment.prompt_duration"));
        contactLabel.setText(msg.getString("appointment.contact"));
        exitButton.setText(msg.getString("cancel"));
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

        final StringConverter<LocalDate> defaultConverter = datePicker.getConverter();
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                return defaultConverter.toString(localDate);
            }

            @Override
            public LocalDate fromString(String s) {
                try {
                    startDateError.setText("");
                    dateError = false;
                    return defaultConverter.fromString(s);
                } catch (DateTimeException ex) {
                    dateError = true;
                    startDateError.setText(msg.getString("start_date_error"));
                    ex.printStackTrace();
                    throw ex;
                }
            }
        });
        timeComboBox.setCellFactory(time -> timeFormat());
        timeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ZonedDateTime zonedDateTime) {
                return zonedDateTime.toLocalTime().toString();
            }

            @Override
            public ZonedDateTime fromString(String s) {
                return null;
            }
        });
    }

    private ListCell<ZonedDateTime> timeFormat() {
        return new ListCell<>() {
            @Override
            protected void updateItem(ZonedDateTime time, boolean b) {
                super.updateItem(time, b);
                if (b || time == null) {
                    setText(null);
                } else {
                    setText(time.toLocalTime().toString());
                }
            }
        };
    }

    /**
     * Gets avail times.
     *
     * @param date the date
     * @return the avail times
     */
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
        return (final DatePicker datePicker1) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #808080;");
                }
            }
        };
    }

    /**
     * On exit button click.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onExitButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("appointment.cancel.title"));
        alert.setHeaderText(msg.getString("appointment.cancel.header"));
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hankoh/scheduleapp/view/main.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle(msg.getString("title"));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /**
     * Field is valid boolean.
     *
     * @param title   the title
     * @param label   the label
     * @param message the message
     * @return the boolean
     */
    protected boolean fieldIsValid(String title, Label label, String message) {
        if (title == null || title.isBlank()) {
            label.setText(msg.getString(message));
            return false;
        }
        label.setText("");
        return true;
    }

    /**
     * Field is valid boolean.
     *
     * @param date    the date
     * @param label   the label
     * @param message the message
     * @return the boolean
     */
    protected boolean fieldIsValid(LocalDate date, Label label, String message) {
        if (dateError) {
            return false;
        }
        if (date == null || date.toString().isEmpty()) {
            startDateError.setText(msg.getString(message));
            return false;
        }
        label.setText("");
        return true;
    }

    /**
     * Time field is valid boolean.
     *
     * @param time    the time
     * @param label   the label
     * @param message the message
     * @return the boolean
     */
    protected boolean timeFieldIsValid(ZonedDateTime time, Label label, String message) {
        if (time == null) {
            label.setText(msg.getString(message));
            return false;
        }
        label.setText("");
        return true;
    }

    /**
     * Combo box is valid boolean.
     *
     * @param duration the duration
     * @param label    the label
     * @param message  the message
     * @return the boolean
     */
    protected boolean comboBoxIsValid(AppointmentDuration duration, Label label, String message) {
        if (duration == null) {
            label.setText(msg.getString(message));
            return false;
        }
        label.setText("");
        return true;
    }

    /**
     * Clear date picker.
     */
    public void clearDatePicker() {
        datePicker.setValue(null);
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
