package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactDao {
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    public ObservableList<Contact> getAllContacts() throws SQLException {
        getContactsDB();
        return contacts;
    }

    private void getContactsDB() throws SQLException {
        Connection conn = JDBC.getConnection();
        String query = "SELECT * FROM contacts";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int id = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            Contact contact = new Contact(
                    id,
                    name,
                    email
            );

            contacts.add(contact);
        }
    }
}
