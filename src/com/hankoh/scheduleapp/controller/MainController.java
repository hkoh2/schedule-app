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

/**
 * Controller for main screen.
 */
public class MainController {
    /**
     * The Appointments tab.
     */
    public Tab appointmentsTab;
    /**
     * The Customers tab.
     */
    public Tab customersTab;
    /**
     * The New appointment button.
     */
    public Button newAppointmentButton;
    /**
     * The Edit appointment button.
     */
    public Button editAppointmentButton;
    /**
     * The Delete appointment button.
     */
    public Button deleteAppointmentButton;
    /**
     * The New customer button.
     */
    public Button newCustomerButton;
    /**
     * The Edit customer button.
     */
    public Button editCustomerButton;
    /**
     * The Delete customer button.
     */
    public Button deleteCustomerButton;
    /**
     * The Logout button.
     */
    public Button logoutButton;
    /**
     * The Exit button.
     */
    public Button exitButton;
    /**
     * The Customers label.
     */
    public Label customersLabel;
    /**
     * The Appointments label.
     */
    public Label appointmentsLabel;
    /**
     * The Welcome text.
     */
    public Text welcomeText;
    /**
     * The Main title label.
     */
    public Text mainTitleLabel;
    /**
     * The Appointment filter combo.
     */
    public ComboBox<String> appointmentFilterCombo;
    /**
     * The Appointment user column.
     */
    public TableColumn<Appointment, String> appointmentUserColumn;
    /**
     * The Appointment customer column.
     */
    public TableColumn<Appointment, String> appointmentCustomerColumn;
    /**
     * The Appointment end column.
     */
    public TableColumn<Appointment, ZonedDateTime> appointmentEndColumn;
    /**
     * The Appointment start column.
     */
    public TableColumn<Appointment, ZonedDateTime> appointmentStartColumn;
    /**
     * The Appointment type column.
     */
    public TableColumn<Appointment, String> appointmentTypeColumn;
    /**
     * The Appointment location column.
     */
    public TableColumn<Appointment, String> appointmentLocationColumn;
    /**
     * The Appointment description column.
     */
    public TableColumn<Appointment, String> appointmentDescriptionColumn;
    /**
     * The Appointment title column.
     */
    public TableColumn<Appointment, String> appointmentTitleColumn;
    /**
     * The Appointment id column.
     */
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    /**
     * The Appointments table.
     */
    public TableView<Appointment> appointmentsTable;
    /**
     * The Customer id column.
     */
    public TableColumn<Customer, Integer> customerIdColumn;
    /**
     * The Customer name column.
     */
    public TableColumn<Customer, String> customerNameColumn;
    /**
     * The Customer address column.
     */
    public TableColumn<Customer, String> customerAddressColumn;
    /**
     * The Customer postal column.
     */
    public TableColumn<Customer, String> customerPostalColumn;
    /**
     * The Customer phone column.
     */
    public TableColumn<Customer, String> customerPhoneColumn;
    /**
     * The Customers table.
     */
    public TableView<Customer> customersTable;
    /**
     * The Main tab pane.
     */
    public TabPane mainTabPane;
    /**
     * The Month filter combo box.
     */
    public ComboBox<YearMonth> monthFilterComboBox;
    /**
     * The Week filter combo box.
     */
    public ComboBox<YearWeek> weekFilterComboBox;
    /**
     * The Customer country column.
     */
    public TableColumn<Customer, String> customerCountryColumn;
    /**
     * The Contacts report tab.
     */
    public Tab contactsReportTab;
    /**
     * The Contacts report table view.
     */
    public TableView<Appointment> contactsReportTableView;
    /**
     * The Contact report id.
     */
    public TableColumn<Appointment, Integer> contactReportId;
    /**
     * The Contact report title.
     */
    public TableColumn<Appointment, String> contactReportTitle;
    /**
     * The Contact report type.
     */
    public TableColumn<Appointment, String> contactReportType;
    /**
     * The Contact report description.
     */
    public TableColumn<Appointment, String> contactReportDescription;
    /**
     * The Contact report start.
     */
    public TableColumn<Appointment, ZonedDateTime> contactReportStart;
    /**
     * The Contact report end.
     */
    public TableColumn<Appointment, ZonedDateTime> contactReportEnd;
    /**
     * The Contact report customer id.
     */
    public TableColumn<Contact, Integer> contactReportCustomerId;
    /**
     * The Contacts combo box.
     */
    public ComboBox<Contact> contactsComboBox;
    /**
     * The Type month report tab.
     */
    public Tab typeMonthReportTab;
    /**
     * The Type month table view.
     */
    public TableView<MonthTotal> typeMonthTableView;
    /**
     * The Month type column.
     */
    public TableColumn<MonthTotal, Month> monthTypeColumn;
    /**
     * The Month type total column.
     */
    public TableColumn<MonthTotal, Integer> monthTypeTotalColumn;
    /**
     * The Customers report tab.
     */
    public Tab customersReportTab;
    /**
     * The By customer table view.
     */
    public TableView<CustomerTotal> byCustomerTableView;
    /**
     * The By customer id.
     */
    public TableColumn<CustomerTotal, Integer> byCustomerId;
    /**
     * The By customer name.
     */
    public TableColumn<CustomerTotal, String> byCustomerName;
    /**
     * The By customer total.
     */
    public TableColumn<CustomerTotal, Integer> byCustomerTotal;
    /**
     * The By total time.
     */
    public TableColumn<CustomerTotal, Integer> byTotalTime;
    /**
     * The By average time.
     */
    public TableColumn<CustomerTotal, Integer> byAverageTime;
    /**
     * The Type combo box.
     */
    public ComboBox<String> typeComboBox;
    /**
     * The Reports tab.
     */
    public Tab reportsTab;
    /**
     * Language on locale.
     */
    protected ResourceBundle msg;
    /**
     * Contains all appointments.
     */
    private ObservableList<Appointment> appointments;
    /**
     * Contains all customers
     */
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    /**
     * The Appointments by month.
     */
    Map<YearMonth, List<Appointment>> appointmentsByMonth;
    /**
     * The Appointments by week.
     */
    Map<YearWeek, List<Appointment>> appointmentsByWeek;

    /**
     * Initialize.
     *
     * @throws SQLException the sql exception
     */
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

        reportsTab.setText(msg.getString("main.reports"));


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
        appointmentUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
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

    /**
     * Gets all appointments from database.
     *
     * @throws SQLException
     */
    private void getAllAppointments() throws SQLException {
        AppointmentDao appointmentDao = new AppointmentDao();
        appointments = appointmentDao.getAllAppointments();
    }

    /**
     * Gets all customers from database.
     *
     * @throws SQLException
     */
    private void getAllCustomers() throws SQLException {
        CustomerDao customerDao = new CustomerDao();
        customers = customerDao.getAllCustomers();
    }

    /**
     * Initializes customer reports.
     *
     * @throws SQLException
     */
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
        customers.forEach(customer -> {
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

    /**
     * Initializes reports by type and month.
     * @throws SQLException
     */
    private void initializeTypeMonthReport() throws SQLException {
        monthTypeColumn.setText(msg.getString("report.month_type"));
        monthTypeTotalColumn.setText(msg.getString("report.total"));

        monthTypeColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthTypeTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        getAllAppointments();

        HashSet<String> allTypesSet = new HashSet<>(appointments.stream()
                .map(Appointment::getType)
                .collect(Collectors.toCollection(HashSet::new)));
        ObservableList<String> allTypes = FXCollections.observableArrayList(allTypesSet);
        typeComboBox.setPromptText(msg.getString("report.type_month.combo"));
        typeComboBox.setItems(allTypes);

        typeComboBox.valueProperty().addListener((ov, oldVal, newVal) -> getTypeMonthTotal(newVal));
    }

    /**
     * Creates reports by type and month
     *
     * @param newVal
     */
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


    /**
     * Initializes reports by contacts
     */
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
        contactReportStart.setCellFactory(this::formatStart);
        contactReportEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        contactReportEnd.setCellFactory(this::formatStart);

        FilteredList<Appointment> filteredList= new FilteredList<>(appointments);
        SortedList<Appointment> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(contactsReportTableView.comparatorProperty());

        contactsReportTableView.setItems(sortedList);
        contactsComboBox.setPromptText(msg.getString("report.contacts.select"));
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

    /**
     * Adds contacts to report for filtering by contact.
     *
     * @throws SQLException
     */
    private void setContacts() throws SQLException {
        ObservableList<Contact> allContacts = getAllContacts();
        contactsComboBox.setItems(allContacts);
    }


    /**
     * Gets all available contacts from database.
     *
     * @return all available contacts
     * @throws SQLException
     */
    private ObservableList<Contact> getAllContacts() throws SQLException {
        ContactDao contactDao = new ContactDao();
        return contactDao.getAllContacts().stream()
                .sorted()
                .collect(toCollection(FXCollections::observableArrayList));
    }

    /**
     * Filters appointment by all, months, and weeks.
     * <p>
     *     Lambda used in stream to group appointment list elements
     *     by each report criteria.
     * </p>
     *
     * @param newVal type of filter for appointments.
     * @throws SQLException
     */
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
        }
    }

    /**
     * Separates appointments by month.
     * <p>
     *     Lambda stream used to separate appointments by month.
     * </p>
     *
     * @return reports by month
     */
    private Map<YearMonth, List<Appointment>> groupingByMonth() {
        return appointments.stream()
                .collect(groupingBy(
                        this::getYearMonth,
                mapping(appointment -> appointment, toCollection(FXCollections::observableArrayList))
        ));
    }

    /**
     * Separates appointments by week.
     * <p>
     *     Lambda stream used to separate appointment by week.
     * </p>
     *
     * @return reports by week.
     */
    private Map<YearWeek, List<Appointment>> groupingByWeek() {
        return appointments.stream()
                .collect(groupingBy(
                        this::getWeekRange,
                        mapping(appointment -> appointment, toCollection(FXCollections::observableArrayList))
                ));
    }

    /**
     * Determines the start and end date of the week for an appointment.
     *
     * @param apt appointment
     * @return YearWeek class that contains start and end date of a week for a given appointment.
     */
    public YearWeek getWeekRange(Appointment apt) {
        ZonedDateTime startTime = apt.getStartTime();
        ZonedDateTime weekStart = startTime.with(DayOfWeek.MONDAY);
        ZonedDateTime weekEnd = weekStart.plusDays(4);
        LocalDate start = weekStart.toLocalDate();
        LocalDate end = weekEnd.toLocalDate();
        return new YearWeek(start, end);
    }

    /**
     * Returns month that the appointment falls into.
     *
     * @param apt appointment.
     * @return YearMonth for the appointment.
     */
    public YearMonth getYearMonth(Appointment apt) {
        LocalDate start = apt.getStartTime().toLocalDate();
        return YearMonth.from(start);
    }

    /**
     * Custom time display for TableView
     *
     * @param zdt appointment time
     * @return TableCell display
     */
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

    /**
     * Date and time formatter for appointments
     *
     * @return formatted date and time
     */
    private DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault());
    }

    /**
     * Stores selected customer to data storage for sharing with other controllers.
     *
     * @param newVal selected customer from TableView
     */
    private void setSelectedCustomer(Customer newVal) {
        DataStorage ds = DataStorage.getInstance();
        ds.setCurrentCustomer(newVal);
    }

    /**
     * Stores the last selected tab in the main screen. When returning to main screen, the last tab selected
     * is saved.
     */
    private void setSelectedTab() {
        mainTabPane
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, oldTab, newTab) -> setCurrentTab(newTab));
    }


    /**
     * New appointment button. Transitions to new appointment screen.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onNewAppointmentButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/appointment-add.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("appointment.main_title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Edit appointment button. Transitions to edit appointment screen.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onEditAppointmentButtonClick(ActionEvent actionEvent) throws IOException {
        DataStorage ds = DataStorage.getInstance();
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            return;
        }
        ds.setCurrentAppointment(appointment);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/appointment-edit.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("modify.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Delete appointment screen. Deletes selected appointment.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void onDeleteAppointmentButtonClick(ActionEvent actionEvent) throws SQLException {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

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

    /**
     * New customer button. Transitions to new customer screen.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onNewCustomerButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hankoh/scheduleapp/view/customer-add.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(msg.getString("customer.title"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Edit customer button. Transitions to edit customer screen.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
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

    /**
     * Delete customer button. Deletes selected customer.
     *
     * @param actionEvent the action event
     * @throws SQLException the sql exception
     */
    public void onDeleteCustomerButtonClick(ActionEvent actionEvent) throws SQLException {
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

    /**
     * Log out button. Logs user out to login screen.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
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

    /**
     * Exit button. Exits application.
     *
     * @param actionEvent the action event
     */
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

    /**
     * Stores the current tab in data storage. Allows the app to remember the last location
     * in the main screen.
     *
     * @param tab last tab selected in main screen.
     */
    private void setCurrentTab(Tab tab) {
        DataStorage ds = DataStorage.getInstance();
        int index = tab
                .getTabPane()
                .getSelectionModel()
                .getSelectedIndex();
        ds.setCurrentTab(tab);
        ds.setCurrentTabIndex(index);
    }
}
