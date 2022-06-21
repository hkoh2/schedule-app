package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerDao {
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    public CustomerDao() {

    }

    public ObservableList<Customer> getAllCustomers() throws SQLException {
        getCustomersDB();
        return customers;
    }

    private void getCustomersDB() throws SQLException {

        Connection conn = JDBC.getConnection();
        String query = "SELECT * FROM customers";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int id = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divisionId = rs.getInt("Division_ID");

            Customer customer = new Customer(
                    id,
                    name,
                    address,
                    postalCode,
                    phone,
                    divisionId
            );

            customers.add(customer);
        }
    }
}
