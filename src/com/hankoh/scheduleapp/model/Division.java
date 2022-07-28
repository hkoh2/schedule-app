package com.hankoh.scheduleapp.model;

/**
 * Division class contains division details.
 */
public class Division {
    private int divisionId;
    private String division;
    private int countryId;

    /**
     * Instantiates a new Division.
     *
     * @param divisionId the division id
     * @param division   the division
     * @param countryId  the country id
     */
    public Division(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    /**
     * Gets division id.
     *
     * @return the division id
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets division id.
     *
     * @param divisionId the division id
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets division.
     *
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets division.
     *
     * @param division the division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets country id.
     *
     * @return the country id
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets country id.
     *
     * @param countryId the country id
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return division;
    }
}
