package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.Customer;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static java.util.stream.Collectors.*;

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
    public TableColumn<Appointment, ZonedDateTime> appointmentEndColumn;
    public TableColumn<Appointment, ZonedDateTime> appointmentStartColumn;
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
    public ComboBox<YearMonth> monthFilterComboBox;
    public ComboBox<YearWeek> weekFilterComboBox;
    public TableColumn<Customer, String> customerCountryColumn;
    protected ResourceBundle msg;
    private ObservableList<Appointment> appointments; //= FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    Map<YearMonth, List<Appointment>> appointmentsByMonth;
    //Map<>
    Map<YearWeek, List<Appointment>> appointmentsByWeek;
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


        customersTab.setText(msg.getString("main.customers"));
        customersLabel.setText(msg.getString("main.customers"));
        newCustomerButton.setText(msg.getString("new"));
        editCustomerButton.setText(msg.getString("edit"));
        deleteCustomerButton.setText(msg.getString("delete"));

        logoutButton.setText(msg.getString("logout"));
        exitButton.setText(msg.getString("exit_button"));

        // Table column text
        appointmentIdColumn.setText(msg.getString("appointment.column.id"));
        appointmentTitleColumn.setText(msg.getString("appointment.column.title"));
        appointmentDescriptionColumn.setText(msg.getString("appointment.column.description"));
        appointmentLocationColumn.setText(msg.getString("appointment.column.type"));
        appointmentStartColumn.setText(msg.getString("appointment.column.start"));
        appointmentEndColumn.setText(msg.getString("appointment.column.end"));
        appointmentCustomerColumn.setText(msg.getString("appointment.column.customer"));
        appointmentUserColumn.setText(msg.getString("appointment.column.user"));


        AppointmentDao appointmentDao = new AppointmentDao();
        appointments = appointmentDao.getAllAppointments();

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));;
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentStartColumn.setCellFactory(column -> formatStart(column));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        appointmentEndColumn.setCellFactory(column -> formatStart(column));

        appointmentsTable.setItems(appointments);

        CustomerDao customerDao = new CustomerDao();
        customers = customerDao.getAllCustomers();

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("fullAddress"));
        //customerAddressColumn.setCellFactory(column -> formatAddress(column));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        customersTable.setItems(customers);

        setSelectedTab();

        // saves selected customers
        customersTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(((ov, oldVal, newVal) -> setSelectedCustomer(newVal)));

        // Sort by descending start time
        appointmentsTable.getSortOrder().add(appointmentStartColumn);
        appointmentStartColumn.setComparator(
                appointmentStartColumn.getComparator().reversed()
        );


        String strAll = msg.getString("appointment.all");
        String strMonth = msg.getString("appointment.month");
        String strWeek = msg.getString("appointment.week");

        appointmentFilterCombo
                .getItems()
                .addAll(strAll, strMonth, strWeek);
        appointmentFilterCombo
                .getSelectionModel()
                .select(strAll);

        appointmentFilterCombo.valueProperty()
                        .addListener((ov, oldVal, newVal) -> {
                            try {
                                filterAppointments(newVal);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

        monthFilterComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> {
                    YearMonth selectedMonth = monthFilterComboBox.getSelectionModel().getSelectedItem();
                    List<Appointment> filteredApt = appointmentsByMonth.get(selectedMonth);
                    if (appointmentsByMonth.get(selectedMonth) == null) {
                        return;
                    }
                    if (selectedMonth != null) {
                        appointments = FXCollections.observableArrayList(filteredApt);
                        appointmentsTable.setItems(appointments);
                    } else {
                        appointmentsTable.setItems(null);
                    }
                });
        weekFilterComboBox.valueProperty()
                .addListener((ov, oldVal, newVal) -> {
                    YearWeek selectedWeek = weekFilterComboBox.getSelectionModel().getSelectedItem();
                    List<Appointment> filteredApt = appointmentsByWeek.get(selectedWeek);
                    if (appointmentsByWeek.get(selectedWeek) == null) {
                        return;
                    }
                    if (selectedWeek != null && filteredApt != null) {
                        appointments = FXCollections.observableArrayList(filteredApt);
                        appointmentsTable.setItems(appointments);
                    } else {
                        appointmentsTable.setItems(null);
                    }
                });

    }

    private TableCell<Customer, String> formatAddress(TableColumn<Customer, String> column) {
        return new TableCell<>() {

            @Override
            protected void updateItem(String address, boolean empty) {

                super.updateItem(address, empty);
                if (empty) {
                    setText("");
                } else {
                    setText("");
                }
            }
        };
    }

    private void filterAppointments(String newVal) throws SQLException {
        System.out.println("Changing appointment filter: " + newVal);
        String all = msg.getString("appointment.all");
        String month = msg.getString("appointment.month");
        String week = msg.getString("appointment.week");

        //AppointmentDao appointmentDao = new AppointmentDao();
        //appointments = appointmentDao.getAllAppointments();
        monthFilterComboBox.getItems().clear();
        weekFilterComboBox.getItems().clear();
        AppointmentDao appointmentDao = new AppointmentDao();
        appointments = appointmentDao.getAllAppointments();

        if (newVal.equals(all)) {
            monthFilterComboBox.setVisible(false);
            weekFilterComboBox.setVisible(false);
            // TODO show all appointments.
            appointmentsTable.setItems(appointments);
            return;
        }

        if (newVal.equals(month)) {
            monthFilterComboBox.setVisible(true);
            weekFilterComboBox.setVisible(false);
            appointmentsByMonth = groupingByMonth();
            appointmentsByMonth.keySet().stream().sorted(Comparator.reverseOrder()).forEach(
                    key -> monthFilterComboBox.getItems().add(key)
            );
            return;
        }

        if (newVal.equals(week)) {
            monthFilterComboBox.setVisible(false);
            weekFilterComboBox.setVisible(true);
            // TODO show weeks
            appointmentsByWeek = groupingByWeek();
            //appointmentsByWeek.keySet().stream().sorted(Comparator.reverseOrder()).forEach(
            appointmentsByWeek.keySet().stream().sorted().forEach(
                    key -> weekFilterComboBox.getItems().add(key)
            );
            return;
        }

    }

    private Map groupingByMonth() {
        Map<YearMonth, List<Appointment>> appointmentMap = appointments.stream()
                .collect(groupingBy(
                appointment -> getYearMonth(appointment),
                mapping(appointment -> appointment, toCollection(FXCollections::observableArrayList))
        ));
        return appointmentMap;
    }

    private Map groupingByWeek() {
        Map<YearWeek, List<Appointment>> appointmentMap = appointments.stream()
                .collect(groupingBy(
                        appointment -> getWeekRange(appointment),
                        mapping(appointment -> appointment, toCollection(FXCollections::observableArrayList))
                ));
        return appointmentMap;
    }

    public YearWeek getWeekRange(Appointment apt) {
        ZonedDateTime startTime = apt.getStartTime();
        ZonedDateTime weekStart = startTime.with(DayOfWeek.MONDAY);
        ZonedDateTime weekEnd = weekStart.plusDays(4);
        LocalDate start = weekStart.toLocalDate();
        LocalDate end = weekEnd.toLocalDate();
        YearWeek weekRange = new YearWeek(start, end);

        //System.out.println(start + " --- " + end);
        return weekRange;
    }


    public YearMonth getYearMonth(Appointment apt) {
        LocalDate start = apt.getStartTime().toLocalDate();
        //YearMonth startYearMont = YearMonth.from(start);
        return YearMonth.from(start);
    }


    public Month getDate(Appointment appointment) {
        return appointment.getStartTime().toLocalDate().getMonth();
    }

    private TableCell<Appointment, ZonedDateTime> formatStart(TableColumn<Appointment, ZonedDateTime> zdt) {
        return new TableCell<>() {
            @Override
            protected void updateItem(ZonedDateTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(time.format(dateTimeFormatter()));
                }
            }
        };
    }

    private DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
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


    public void onNewAppointmentButtonClick(ActionEvent actionEvent) throws IOException {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/appointment-edit2.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("modify.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onDeleteAppointmentButtonClick(ActionEvent actionEvent) throws SQLException {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();

        AppointmentDao appointmentDao = new AppointmentDao();
        if (appointmentDao.removeAppointment(selected.getAppointmentId())) {
            appointments.remove(selected);
            appointments = appointmentDao.getAllAppointments();
            appointmentsByMonth = groupingByMonth();
            appointmentsByWeek = groupingByWeek();
            //refreshAppointments();
        } else {
            System.out.println("Failed to remove Appointment");
        }

    }

    private void refreshAppointments() throws SQLException {
        //AppointmentDao appointmentDao = new AppointmentDao();
        //appointments = appointmentDao.getAllAppointments();
        appointmentsTable.setItems(appointments);
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

    public void onDeleteCustomerButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("customer.delete.title"));
        alert.setHeaderText(msg.getString("customer.delete.header"));
        alert.setContentText(msg.getString("customer.delete.context") + ", " + selectedCustomer.getName() + "?");
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            CustomerDao customerDao = new CustomerDao();
            if (customerDao.deleteCustomerById(selectedCustomer.getCustomerId())) {
                System.out.println("Customer deleted");
                customers.remove(selectedCustomer);
                return;
            }
            System.out.println("Error");
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setTitle(msg.getString("customer.error.title"));
            errorAlert.setHeaderText(msg.getString("customer.error.header"));
            errorAlert.setContentText(msg.getString("customer.error.context"));
            errorAlert.showAndWait();
        }


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
