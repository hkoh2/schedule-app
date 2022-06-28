package com.hankoh.scheduleapp.DAO;

import com.hankoh.scheduleapp.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DivisionDao {

    private final ObservableList<Division> divisions = FXCollections.observableArrayList();

    public ObservableList<Division> getAllDivisions() throws SQLException {
        getDivisionsDB();
        return divisions;
    }

    private void getDivisionsDB() throws SQLException {
        Connection conn = JDBC.getConnection();
        String query = "SELECT * FROM first_level_divisions";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryId = rs.getInt("COUNTRY_ID");

            Division division = new Division(
                    divisionId,
                    divisionName,
                    countryId
            );
            divisions.add(division);
        }
    }
}
