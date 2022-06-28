package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CountryDao {
    private final ObservableList<Country> countries = FXCollections.observableArrayList();

    public ObservableList<Country> getAllCountries() throws SQLException {
        getCountriesDB();
        return countries;
    }

    private void getCountriesDB() throws SQLException {
        Connection conn = JDBC.getConnection();
        String query = "SELECT * FROM countries";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            Country country = new Country(
                    countryId,
                    countryName
            );
            countries.add(country);
        }
    }
}
