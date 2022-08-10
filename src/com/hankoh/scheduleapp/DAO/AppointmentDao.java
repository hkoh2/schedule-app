package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;

/**
 * Data access object for appointments.
 */
public class AppointmentDao {

    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    /**
     * The Conn.
     */
    Connection conn;

    /**
     * Instantiates a new Appointment dao.
     */
    public AppointmentDao() {
        conn = JDBC.getConnection();
    }

    /**
     * Gets all appointments from database.
     *
     * @return the all appointments
     * @throws SQLException the sql exception
     */
    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        String query = """
                SELECT *
                FROM appointments
                INNER JOIN customers
                ON appointments.Customer_ID = customers.Customer_ID
                INNER JOIN users
                ON appointments.User_ID = users.User_ID
                INNER JOIN contacts
                ON appointments.Contact_ID = contacts.Contact_ID
                """;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerId = rs.getInt("Customers.Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String userName = rs.getString("User_Name");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String contactEmail = rs.getString("Email");

            LocalDateTime localStart = start.toLocalDateTime();
            ZonedDateTime zonedStart = localStart.atZone(ZoneId.systemDefault());

            LocalDateTime localEnd = end.toLocalDateTime();
            ZonedDateTime zonedEnd = localEnd.atZone(ZoneId.systemDefault());

            Appointment appointment = new Appointment(
                    id,
                    title,
                    description,
                    location,
                    type,
                    zonedStart,
                    zonedEnd,
                    customerId,
                    customerName,
                    userId,
                    userName,
                    contactId,
                    contactName,
                    contactEmail
            );

            appointments.add(appointment);
        }

        return appointments;
    }

    /**
     * Gets appointments by date.
     *
     * @param date the date
     * @return the appointments by date
     * @throws SQLException the sql exception
     */
    public ObservableList<Appointment> getAppointmentsByDate(LocalDate date) throws SQLException {
        String query = """
        SELECT Appointment_ID, Start, End
        FROM appointments
        WHERE Start >= ? AND End <= ?
        """;
        PreparedStatement stmt = conn.prepareStatement(query);
        LocalTime startTimeLocal = LocalTime.of(0, 1);
        LocalTime endTimeLocal = LocalTime.of(23, 59);
        LocalDateTime startDate = LocalDateTime.of(date, startTimeLocal);
        LocalDateTime endDate = LocalDateTime.of(date, endTimeLocal);

        Timestamp startStamp = Timestamp.valueOf(startDate);
        Timestamp endStamp = Timestamp.valueOf(endDate);
        stmt.setTimestamp(1, startStamp);
        stmt.setTimestamp(2, endStamp);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            int id = rs.getInt("Appointment_ID");
            Timestamp startTime = rs.getTimestamp("Start");
            Timestamp endTime = rs.getTimestamp("End");
            ZonedDateTime zonedStart = startTime.toInstant().atZone(ZoneId.systemDefault());
            ZonedDateTime zonedEnd = endTime.toInstant().atZone(ZoneId.systemDefault());

            Appointment appointment = new Appointment(
                    id,
                    zonedStart,
                    zonedEnd
            );

            appointments.add(appointment);
        }

        return appointments;

    }

    /**
     * Adds new appointment to database.
     *
     * @param appointment new appointment to add
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean addAppointment(Appointment appointment) throws SQLException {

        PreparedStatement conflictStmt = getConflictTimeStatement(appointment);
        ResultSet rs = conflictStmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            System.out.println("No conflicts found!");
        } else {
            System.out.println("CONFLICT FOUND!");
            return false;
        }
        PreparedStatement stmt = getInsertStatement(appointment);
        int count = stmt.executeUpdate();
        System.out.println(count + " appointment added");
        return true;
    }

    /**
     * Removes appointment by id
     *
     * @param appointmentId appointment id to remove
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean removeAppointment(int appointmentId) throws SQLException {
        PreparedStatement stmt = getRemoveStatement(appointmentId);
        int count = stmt.executeUpdate();
        if (count > 0) {
            System.out.println(count + " appointment removed");
            return true;
        }
        return false;
    }

    /**
     * Removes appointments by customer ID.
     *
     * @param customerId customer id to remove
     * @return confirm if appointments are removed
     * @throws SQLException
     */
    public boolean removeAppointmentByCustomerId(int customerId) throws SQLException {
        PreparedStatement stmt = getRemoveByCustomer(customerId);
        int count = stmt.executeUpdate();
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * Gets prepared statement to remove appointments by customer ID.
     *
     * @param customerId customer id to remove
     * @return prepared statement
     * @throws SQLException
     */
    private PreparedStatement getRemoveByCustomer(int customerId) throws SQLException {
        String query = """
                DELETE FROM appointments
                WHERE Customer_ID = ?
                """;
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, customerId);
        return stmt;
    }

    /**
     * Updates appointment.
     *
     * @param appointment the appointment
     * @throws SQLException the sql exception
     */
    public void updateAppointment(Appointment appointment) throws SQLException {
        PreparedStatement stmt = getUpdateStatement(appointment);
        int count = stmt.executeUpdate();
        System.out.println(count + " appointment updated");
    }

    /**
     * Prepared statement for checking appointment conflicts.
     *
     * @param appointment appointment to check
     * @return
     * @throws SQLException
     */
    private PreparedStatement getConflictTimeStatement(Appointment appointment) throws SQLException {
        String query = """
                SELECT * FROM appointments
                WHERE Customer_ID = ?
                AND (Start <= ? AND End > ?)
                OR (Start < ? AND End >= ?)
                OR (Start > ? AND End < ?)
                """;
        PreparedStatement stmt = conn.prepareStatement(query);
        Timestamp startTime = Timestamp.valueOf(
                appointment.getStartTime().toLocalDateTime()
        );
        Timestamp endTime = Timestamp.valueOf(
                appointment.getEndTime().toLocalDateTime()
        );
        stmt.setInt(1, appointment.getCustomerId());
        stmt.setTimestamp(2, startTime);
        stmt.setTimestamp(3, startTime);
        stmt.setTimestamp(4, endTime);
        stmt.setTimestamp(5, endTime);
        stmt.setTimestamp(6, startTime);
        stmt.setTimestamp(7, endTime);
        return stmt;

    }

    /**
     * Creates prepared statement for updating appointments.
     *
     * @param appointment appointment to update
     * @return
     * @throws SQLException
     */
    private PreparedStatement getUpdateStatement(Appointment appointment) throws SQLException {
        String query = """
                UPDATE appointments
                SET
                Title = ?,
                Description = ?,
                Location = ?,
                Type = ?,
                Start = ?,
                End = ?,
                Customer_ID = ?,
                User_ID = ?,
                Contact_ID = ?
                WHERE Appointment_ID = ?
                """;
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, appointment.getTitle());
        stmt.setString(2, appointment.getDescription());
        stmt.setString(3, appointment.getLocation());
        stmt.setString(4, appointment.getType());


        stmt.setTimestamp(5, appointment.getStartTimestamp());
        stmt.setTimestamp(6, appointment.getEndTimestamp());
        stmt.setInt(7, appointment.getCustomerId());
        stmt.setInt(8, appointment.getUserId());
        stmt.setInt(9, appointment.getContactId());
        stmt.setInt(10, appointment.getAppointmentId());
        return stmt;
    }

    /**
     * Gets insert statement to add new appointment to database.
     *
     * @param appointment appointment to add.
     * @return the insert statement
     * @throws SQLException the sql exception
     */
    public PreparedStatement getInsertStatement(Appointment appointment) throws SQLException {
        String query = """
                INSERT INTO appointments
                (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, appointment.getTitle());
        stmt.setString(2, appointment.getDescription());
        stmt.setString(3, appointment.getLocation());
        stmt.setString(4, appointment.getType());
        stmt.setTimestamp(5, appointment.getStartTimestamp());
        stmt.setTimestamp(6, appointment.getEndTimestamp());
        stmt.setInt(7, appointment.getCustomerId());
        stmt.setInt(8, appointment.getUserId());
        stmt.setInt(9, appointment.getContactId());

        return stmt;
    }

    /**
     * Gets remove statement.
     *
     * @param appointmentId appointment id to remove
     * @return the remove statement
     * @throws SQLException the sql exception
     */
    public PreparedStatement getRemoveStatement(int appointmentId) throws SQLException {
        String query = """
                DELETE FROM appointments
                WHERE Appointment_ID = ?
                """;
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, appointmentId);
        return stmt;
    }
}
