package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerDao {
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    Connection conn;

    public CustomerDao() {
        conn = JDBC.getConnection();

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

    public boolean addCustomer(Customer customer) throws SQLException {
        PreparedStatement stmt = getInsertStatement(customer);
        int count = stmt.executeUpdate();
        if (count > 0) {
            System.out.println(count + " customer added");
            return true;
        }
        return false;
    }

    public boolean editCustomer(Customer customer) throws SQLException {
        PreparedStatement stmt = getUpdateStatement(customer);
        int count = stmt.executeUpdate();
        if (count > 0) {
            System.out.println(count + " customer updated");
            return true;
        }
        return false;
    }

    private PreparedStatement getUpdateStatement(Customer customer) throws SQLException {
        String query = """
                UPDATE customers
                SET
                Customer_Name = ?,
                Address = ?,
                Postal_Code = ?,
                Phone = ?,
                Division_ID = ?
                WHERE Customer_ID = ?
                """;
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getAddress());
        stmt.setString(3, customer.getPostalCode());
        stmt.setString(4, customer.getPhone());
        stmt.setInt(5, customer.getDivisionId());
        stmt.setInt(6, customer.getCustomerId());

        return stmt;
    }

    private PreparedStatement getInsertStatement(Customer customer) throws SQLException {
        String query = """
                INSERT INTO customers
                (Customer_Name, Address, Postal_Code, Phone, Division_ID)
                VALUES (?, ?, ?, ?, ?)
                """;
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getAddress());
        stmt.setString(3, customer.getPostalCode());
        stmt.setString(4, customer.getPhone());
        stmt.setInt(5, customer.getDivisionId());

        return stmt;
    }
}
