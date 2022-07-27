package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.DataStorage;
import com.hankoh.scheduleapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database access object for users.
 */
public class UserDao {

    private final ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * Instantiates a new User dao.
     */
    public UserDao() {
    }

    /**
     * Logs in user based on provided username and password.
     *
     * @param username provided username
     * @param password provided password
     * @return confirmation of username and password
     * @throws SQLException the sql exception
     */
    public boolean loginUser(String username, String password) throws SQLException {
        Connection conn = JDBC.getConnection();
        String query = "SELECT * FROM users WHERE User_Name = \"" +
                username +
                "\" AND Password = \"" +
                password + "\"";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.isBeforeFirst()) {
            return false;
        }
        rs.next();
        DataStorage ds = DataStorage.getInstance();
        ds.setUser(new User(
                rs.getInt("User_ID"),
                rs.getString("User_Name")
        ));
        return true;
    }

    /**
     * Gets all users.
     *
     * @return the all users
     */
    public ObservableList<User> getAllUsers() throws SQLException {
        String query = """
                SELECT * FROM users
                """;
        Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int id = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");

            User user = new User(id, userName);

            users.add(user);
        }
        return users;
    }
}
