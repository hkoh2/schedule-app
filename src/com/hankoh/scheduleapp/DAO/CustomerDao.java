package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Data access object for customers.
 */
public class CustomerDao {
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    /**
     * The Conn.
     */
    Connection conn;

    /**
     * Instantiates a new Customer dao.
     */
    public CustomerDao() {
        conn = JDBC.getConnection();
    }

    /**
     * Gets all customers.
     *
     * @return the all customers
     * @throws SQLException the sql exception
     */
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        getCustomersDB();
        return customers;
    }

    /**
     * Retrieves customers from the database.
     * @throws SQLException
     */
    private void getCustomersDB() throws SQLException {

        Connection conn = JDBC.getConnection();
        String query = """
                SELECT * FROM customers
                INNER JOIN first_level_divisions
                ON customers.Division_ID = first_level_divisions.Division_ID
                INNER JOIN countries
                ON first_level_divisions.COUNTRY_ID = countries.Country_ID
                """;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int id = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            String division = rs.getString("Division");
            String country = rs.getString("Country");
            int divisionId = rs.getInt("Division_ID");

            Customer customer = new Customer(
                    id,
                    name,
                    address,
                    postalCode,
                    phone,
                    division,
                    country,
                    divisionId
            );
            customers.add(customer);
        }
    }

    /**
     * Adds customers to database.
     *
     * @param customer customer to add
     * @return confirmation of database insert
     * @throws SQLException the sql exception
     */
    public boolean addCustomer(Customer customer) throws SQLException {
        PreparedStatement stmt = getInsertStatement(customer);
        int count = stmt.executeUpdate();
        if (count > 0) {
            System.out.println(count + " customer added");
            return true;
        }
        return false;
    }

    /**
     * Updates customer details.
     *
     * @param customer customer to update
     * @return confirmation of updating customer
     * @throws SQLException the sql exception
     */
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

    /**
     * Gets prepared statement for inserting new customer.
     *
     * @param customer
     * @return prepared statement for JDBC
     * @throws SQLException
     */
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

    /**
     * Deletes customer by id.
     *
     * @param id customer ID to delete
     * @return confirmation of deleting customer
     * @throws SQLException the sql exception
     */
    public boolean deleteCustomerById(int id) throws SQLException {
        PreparedStatement stmt = getDeleteStatement(id);
        int count = 0;
        try {
            count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count > 0) {
            System.out.println(count + " customer deleted");
            return true;
        }
        return false;
    }

    /**
     * Creates prepared statement for deleting customer from database.
     *
     * @param id customer ID to delete
     * @return prepared statement
     * @throws SQLException
     */
    private PreparedStatement getDeleteStatement(int id) throws SQLException {
        String query = """
                DELETE FROM customers WHERE Customer_ID = ?
                """;
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        return stmt;
    }
}
