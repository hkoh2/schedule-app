package com.hankoh.scheduleapp.controller;

import com.hankoh.scheduleapp.DAO.AppointmentDao;
import com.hankoh.scheduleapp.DAO.ContactDao;
import com.hankoh.scheduleapp.DAO.CustomerDao;
import com.hankoh.scheduleapp.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.util.stream.Collectors;

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
    public Tab contactsReportTab;
    public TableView<Appointment> contactsReportTableView;
    public TableColumn<Appointment, Integer> contactReportId;
    public TableColumn<Appointment, String> contactReportTitle;
    public TableColumn<Appointment, String> contactReportType;
    public TableColumn<Appointment, String> contactReportDescription;
    public TableColumn<Appointment, ZonedDateTime> contactReportStart;
    public TableColumn<Appointment, ZonedDateTime> contactReportEnd;
    public TableColumn<Contact, Integer> contactReportCustomerId;
    public ComboBox<Contact> contactsComboBox;
    public Tab typeMonthReportTab;
    public TableView<MonthTotal> typeMonthTableView;
    public TableColumn<MonthTotal, Month> monthTypeColumn;
    public TableColumn<MonthTotal, Integer> monthTypeTotalColumn;
    public Tab customersReportTab;
    public TableView<CustomerTotal> byCustomerTableView;
    public TableColumn<CustomerTotal, Integer> byCustomerId;
    public TableColumn<CustomerTotal, String> byCustomerName;
    public TableColumn<CustomerTotal, Integer> byCustomerTotal;
    public TableColumn<CustomerTotal, Integer> byTotalTime;
    public TableColumn<CustomerTotal, Double> byAverageTime;
    public ComboBox<String> typeComboBox;
    protected ResourceBundle msg;
    private ObservableList<Appointment> appointments;
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    Map<YearMonth, List<Appointment>> appointmentsByMonth;
    Map<YearWeek, List<Appointment>> appointmentsByWeek;
    public void initialize() throws SQLException {
        msg = ResourceBundle.getBundle(
                "com.hankoh.scheduleapp.properties.MessagesBundle",
                Locale.getDefault()
        );

        mainTitleLabel.setText(msg.getString("title"));

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

        appointmentIdColumn.setText(msg.getString("appointment.column.id"));
        appointmentTitleColumn.setText(msg.getString("appointment.column.title"));
        appointmentDescriptionColumn.setText(msg.getString("appointment.column.description"));
        appointmentLocationColumn.setText(msg.getString("appointment.column.type"));
        appointmentStartColumn.setText(msg.getString("appointment.column.start"));
        appointmentEndColumn.setText(msg.getString("appointment.column.end"));
        appointmentCustomerColumn.setText(msg.getString("appointment.column.customer"));
        appointmentUserColumn.setText(msg.getString("appointment.column.user"));

        customerIdColumn.setText(msg.getString("customer.column.id"));
        customerNameColumn.setText(msg.getString("customer.column.name"));
        customerAddressColumn.setText(msg.getString("customer.column.address"));
        customerCountryColumn.setText(msg.getString("customer.column.country"));
        customerPostalColumn.setText(msg.getString("customer.column.postal"));
        customerPhoneColumn.setText(msg.getString("customer.column.phone"));


        getAllAppointments();

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));;
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentStartColumn.setCellFactory(this::formatStart);
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        appointmentEndColumn.setCellFactory(this::formatStart);
        appointmentsTable.setItems(appointments);

        getAllCustomers();

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("fullAddress"));
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
                        appointments = FXCollections.observableArrayList();
                        return;
                    }
                    if (selectedWeek != null && filteredApt != null) {
                        appointments = FXCollections.observableArrayList(filteredApt);
                        appointmentsTable.setItems(appointments);
                    } else {
                        appointmentsTable.setItems(null);
                    }
                });

        // reports

        initializeContactReport();
        initializeTypeMonthReport();
        initializeCustomerReport();

    }

    private void getAllAppointments() throws SQLException {
        AppointmentDao appointmentDao = new AppointmentDao();
        appointments = appointmentDao.getAllAppointments();
    }

    private void getAllCustomers() throws SQLException {
        CustomerDao customerDao = new CustomerDao();
        customers = customerDao.getAllCustomers();
    }

    private void initializeCustomerReport() throws SQLException {

        contactsReportTab.setText(msg.getString("report.by_contacts"));
        typeMonthReportTab.setText(msg.getString("report.by_type_month"));
        customersReportTab.setText(msg.getString("report.by_customer"));

        byCustomerId.setText(msg.getString("report.id"));
        byCustomerName.setText(msg.getString("report.name"));
        byCustomerTotal.setText(msg.getString("report.total"));
        byTotalTime.setText(msg.getString("report.total_time"));
        byAverageTime.setText(msg.getString("report.average_time"));

        byCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        byCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        byCustomerTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        byTotalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        byAverageTime.setCellValueFactory(new PropertyValueFactory<>("averageTime"));

        //byCustomerTableView
        ObservableList<CustomerTotal> customerTotals = FXCollections.observableArrayList();

        getAllCustomers();
        getAllAppointments();
        customers.stream().forEach(customer -> {
            ObservableList<Appointment> filteredAppointments = appointments.stream()
                    .filter(appointment ->
                            appointment.getCustomerId() == customer.getCustomerId())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            long total = filteredAppointments.size();
            long totalTime = filteredAppointments.stream().mapToLong(
                            appointment -> Duration.between(
                                    appointment.getStartTime(),
                                    appointment.getEndTime()).toMinutes())
                    .sum();
            customerTotals.add(new CustomerTotal(customer, (int) total, (int) totalTime));
        });
        byCustomerTableView.setItems(customerTotals);

    }



    private void initializeTypeMonthReport() throws SQLException {
        monthTypeColumn.setText(msg.getString("report.month_type"));
        monthTypeTotalColumn.setText(msg.getString("report.total"));

        monthTypeColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthTypeTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        getAllAppointments();

        HashSet<String> allTypesSet = new HashSet<String>(appointments.stream()
                .map(Appointment::getType)
                .collect(Collectors.toCollection(HashSet::new)));
        ObservableList<String> allTypes = FXCollections.observableArrayList(allTypesSet);
        typeComboBox.setItems(allTypes);

        typeComboBox.valueProperty().addListener((ov, oldVal, newVal) -> getTypeMonthTotal(newVal));
    }

    private void getTypeMonthTotal(String newVal) {
        Map<Month, List<Appointment>> appointmentsByMonth = appointments.stream()
                .filter(apt -> apt.getType().equals(newVal))
                .collect(groupingBy(
                        apt -> apt.getStartTime().getMonth(),
                        mapping(apt -> apt, toCollection(FXCollections::observableArrayList))));

        ObservableList<MonthTotal> typeMonths = FXCollections.observableArrayList();
        appointmentsByMonth.forEach((key, val) -> {
            int total = val.size();
            typeMonths.add(new MonthTotal(newVal, key, total));
        });
        typeMonthTableView.setItems(typeMonths);
    }


    private void initializeContactReport() {

        contactReportId.setText(msg.getString("appointment.column.id"));
        contactReportTitle.setText(msg.getString("appointment.column.title"));
        contactReportDescription.setText(msg.getString("appointment.column.description"));
        contactReportStart.setText(msg.getString("appointment.column.start"));
        contactReportEnd.setText(msg.getString("appointment.column.end"));
        contactReportCustomerId.setText(msg.getString("appointment.column.customer"));

        contactReportId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        contactReportTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactReportDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactReportType.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactReportCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contactReportStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        contactReportStart.setCellFactory(column -> formatStart(column));
        contactReportEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        contactReportEnd.setCellFactory(column -> formatStart(column));

        FilteredList<Appointment> filteredList= new FilteredList<>(appointments);
        SortedList<Appointment> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(contactsReportTableView.comparatorProperty());

        contactsReportTableView.setItems(sortedList);
        contactsComboBox.valueProperty().addListener((ov, oldVal, newVal) -> {
            if (newVal == null) {
                filteredList.setPredicate(s -> true);
            } else {
                filteredList.setPredicate(s -> s.getContactId() == newVal.getId());
            }
        });
        try {
            setContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setContacts() throws SQLException {
        ObservableList<Contact> allContacts = getAllContacts();
        contactsComboBox.setItems(allContacts);
    }


    private ObservableList<Contact> getAllContacts() throws SQLException {
        ContactDao contactDao = new ContactDao();
        return contactDao.getAllContacts().stream()
                .sorted()
                .collect(toCollection(FXCollections::observableArrayList));
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
        String all = msg.getString("appointment.all");
        String month = msg.getString("appointment.month");
        String week = msg.getString("appointment.week");

        monthFilterComboBox.getItems().clear();
        weekFilterComboBox.getItems().clear();
        getAllAppointments();

        if (newVal.equals(all)) {
            monthFilterComboBox.setVisible(false);
            weekFilterComboBox.setVisible(false);
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
            appointmentsByWeek = groupingByWeek();
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
        return weekRange;
    }


    public YearMonth getYearMonth(Appointment apt) {
        LocalDate start = apt.getStartTime().toLocalDate();
        return YearMonth.from(start);
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


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("appointment.delete.title"));
        alert.setHeaderText(msg.getString("appointment.delete.header"));
        alert.setContentText(msg.getString("appointment.delete.context") + ", " + selected.getTitle() + "?");
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            AppointmentDao appointmentDao = new AppointmentDao();
            if (appointmentDao.removeAppointment(selected.getAppointmentId())) {
                appointments = appointmentDao.getAllAppointments();
                appointmentsTable.setItems(appointments);
                appointmentsByMonth = groupingByMonth();
                appointmentsByWeek = groupingByWeek();
                monthFilterComboBox.getItems().clear();
                weekFilterComboBox.getItems().clear();
                appointmentsByWeek.keySet().stream().sorted().forEach(
                        key -> weekFilterComboBox.getItems().add(key)
                );
                appointmentsByMonth.keySet().stream().sorted(Comparator.reverseOrder()).forEach(
                        key -> monthFilterComboBox.getItems().add(key)
                );
                return;
            }
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setTitle(msg.getString("appointment.error.title"));
            errorAlert.setHeaderText(msg.getString("appointment.error.header"));
            errorAlert.setContentText(msg.getString("appointment.error.context"));
            errorAlert.showAndWait();
        }
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
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/customer-edit.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("customer.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onDeleteCustomerButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(msg.getString("customer.delete.title"));
        alert.setHeaderText(msg.getString("customer.delete.header"));
        alert.setContentText(msg.getString("customer.delete.context") + ", " + selectedCustomer.getName() + "?");
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            CustomerDao customerDao = new CustomerDao();
            if (customerDao.deleteCustomerById(selectedCustomer.getCustomerId())) {
                customersTable.setItems(customerDao.getAllCustomers());
                return;
            }
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
