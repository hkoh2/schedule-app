package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Appointment;
import com.hankoh.scheduleapp.model.Customer;
import com.hankoh.scheduleapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AppointmentDao {

    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public AppointmentDao() {

    }

    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        Connection conn = JDBC.getConnection();
        String query = "SELECT * FROM appointments INNER JOIN customers ON appointments.Customer_ID = customers.Customer_ID INNER JOIN users ON appointments.User_ID = users.User_ID";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Time start = rs.getTime("Start");
            Time end = rs.getTime("End");
            int customerId = rs.getInt("Customers.Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String userName = rs.getString("User_Name");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");
            System.out.println("Cust and User: " + customerId + " " + customerName + " " + userName);

            //
            Customer customer = new Customer(
                    customerId,
                    customerName,
                    "",
                    "",
                    "",
                    0
            );

            User user = new User(
                    userId,
                    userName
            );

            Appointment appointment = new Appointment(
                    id,
                    title,
                    description,
                    location,
                    type,
                    start,
                    end,
                    customer,
                    user,
                    contactId
            );

            appointments.add(appointment);
        }

        System.out.println("Appointment size - " + appointments.size());
        return appointments;
    }

    public void addAppointment() {

    }
}
