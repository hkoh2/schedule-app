package com.hankoh.scheduleapp.model;

/**
 * Country class for customer country.
 */
public class Country {
    private int countryId;
    private String country;

    /**
     * Instantiates a new Country.
     *
     * @param countryId the country id
     * @param country   the country
     */
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
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

    /**
     * Gets country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return country;
    }
}
