package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.DataStorage;
import com.hankoh.scheduleapp.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    public TableColumn<Appointment, String> appointmentEndColumn;
    public TableColumn<Appointment, String> appointmentStartColumn;
    public TableColumn<Appointment, String> appointmentTypeColumn;
    public TableColumn<Appointment, String> appointmentLocationColumn;
    public TableColumn<Appointment, String> appointmentDescriptionColumn;
    public TableColumn<Appointment, String> appointmentTitleColumn;
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    public TableView<Appointment> appointmentsTable;
    public TableColumn<Customer, Integer> customerIdColumn;
    public TableColumn<Customer, String> customerNameColumn;
    public TableColumn<Customer, String> customerAddressColumn;
    public TableColumn<Customer, String> customerPostalColumn;
    public TableColumn<Customer, String> customerPhoneColumn;
    public TableView<Customer> customersTable;
    public TabPane mainTabPane;
    ResourceBundle msg;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        msg = ResourceBundle.getBundle(
                "com.hankoh.scheduleapp.properties.MessagesBundle",
                Locale.getDefault()
        );

        DataStorage ds = DataStorage.getInstance();
        mainTabPane.getSelectionModel().select(ds.getCurrentTabIndex());

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
        //appointments.forEach(appt -> System.out.println(appt.getAppointmentId()));

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        //appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        //appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));;
        appointmentStartColumn.setCellValueFactory(cellData -> startFormatter(cellData));
        appointmentEndColumn.setCellValueFactory(cellData -> endFormatter(cellData));

        appointmentsTable.setItems(appointments);

        CustomerDao customerDao = new CustomerDao();
        customers = customerDao.getAllCustomers();
        //customers.forEach(cust -> System.out.println(cust.getName()));

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customersTable.setItems(customers);

        setSelectedTab();

        // saves selected customers
        customersTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(((ov, oldVal, newVal) -> setSelectedCustomer(newVal)));

    }

    private ObservableValue<String> startFormatter(TableColumn.CellDataFeatures<Appointment, String> val) {
        return new SimpleStringProperty(
                val.getValue()
                   .getStartTime()
                   .format(dateTimeFormatter())
        );
    }
    private ObservableValue<String> endFormatter(TableColumn.CellDataFeatures<Appointment, String> val) {
        return new SimpleStringProperty(
                val.getValue()
                   .getEndTime()
                   .format(dateTimeFormatter())
        );
    }

    private DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault());
    }



    private void setSelectedCustomer(Customer newVal) {
        DataStorage ds = DataStorage.getInstance();
        ds.setCurrentCustomer(newVal);
    }

    private void setSelectedTab() {
        mainTabPane
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, oldTab, newTab) -> setCurrentTab(newTab));
    }

    private void refreshAppointments() throws SQLException {
        AppointmentDao appointmentDao = new AppointmentDao();
        appointments = appointmentDao.getAllAppointments();
        appointmentsTable.setItems(appointments);
    }

    private TableCell<Appointment, Customer> customerCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Customer customer, boolean b) {
                super.updateItem(customer, b);
                if (customer == null || b) {
                    setText("");
                } else {
                    setText(customer.getName());
                }
            }
        };
    }

    private TableCell<Appointment, User> userCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(User user, boolean b) {
                super.updateItem(user, b);
                if (user == null || b) {
                    setText("");
                } else {
                    setText(user.getUserName());
                }
            }
        };
    }


    public void onNewAppointmentButtonClick(ActionEvent actionEvent) throws IOException {
        System.out.println("Add new appointment");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/appointment-add2.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("appointment.main_title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onEditAppointmentButtonClick(ActionEvent actionEvent) throws IOException {
        DataStorage ds = DataStorage.getInstance();
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            return;
        }
        ds.setCurrentAppointment(appointment);
        System.out.println("Modify new appointment");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/appointment-edit2.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Modify Appointment");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onDeleteAppointmentButtonClick(ActionEvent actionEvent) throws SQLException {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        // need to confirm before deleting.

        AppointmentDao appointmentDao = new AppointmentDao();
        appointmentDao.removeAppointment(selected.getAppointmentId());
        refreshAppointments();
    }

    public void onNewCustomerButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/customer-add.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("customer.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onEditCustomerButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/customer-edit.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("customer.title"));
        stage.setScene(new Scene(root));
        stage.show();
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

    private void setCurrentTab(Tab tab) {
        DataStorage ds = DataStorage.getInstance();
        int index = tab
                .getTabPane()
                .getSelectionModel()
                .getSelectedIndex();
        ds.setCurrentTab(tab);
        ds.setCurrentTabIndex(index);
        System.out.println(tab.getText());
    }
}
