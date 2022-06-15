package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.DataStorage;
import com.hankoh.scheduleapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

    private final ObservableList<User> users = FXCollections.observableArrayList();

    public UserDao() {
    }

    public boolean addUser(User user) {
        return false;
    }

    public boolean getUser(User user) {
        return false;
    }

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

    public boolean updateUser(User user) {
        return false;
    }

    public ObservableList<User> getAllUsers() {
        return users;
    }
}
