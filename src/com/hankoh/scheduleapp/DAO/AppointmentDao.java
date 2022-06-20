package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AppointmentDao {

    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public AppointmentDao() {

    }

    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        Connection conn = JDBC.getConnection();
        String query = "SELECT * FROM appointments";
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
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");

            Appointment appointment = new Appointment(
                    id,
                    title,
                    location,
                    description,
                    type,
                    start,
                    end,
                    customerId,
                    userId,
                    contactId
            );

            appointments.add(appointment);
        }

        return appointments;
    }

    public void addAppointment() {

    }
}
