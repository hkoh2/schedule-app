package com.hankoh.scheduleapp.model;


import javafx.scene.control.Tab;

/**
 * Singleton for data shared across all scenes. Contains current user,
 * selected tabs, and selected appointments or customers.
 */
public final class DataStorage {
    private User user;
    private final static DataStorage storage = new DataStorage();
    private Tab currentTab;
    private int currentTabIndex;
    private Customer currentCustomer;
    private Appointment currentAppointment;

    private DataStorage() {}

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static DataStorage getInstance() {
        return storage;
    }

    /**
     * Gets current appointment.
     *
     * @return the current appointment
     */
    public Appointment getCurrentAppointment() {
        return currentAppointment;
    }

    /**
     * Sets current appointment.
     *
     * @param currentAppointment the current appointment
     */
    public void setCurrentAppointment(Appointment currentAppointment) {
        this.currentAppointment = currentAppointment;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Clear all.
     */
    public void clearAll() {
        this.user = null;

    }

    /**
     * Gets current tab.
     *
     * @return the current tab
     */
    public Tab getCurrentTab() {
        return currentTab;
    }

    /**
     * Sets current tab.
     *
     * @param currentTab the current tab
     */
    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }

    /**
     * Gets current tab index.
     *
     * @return the current tab index
     */
    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    /**
     * Sets current tab index.
     *
     * @param currentTabIndex the current tab index
     */
    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

    /**
     * Sets current customer.
     *
     * @param newVal the new val
     */
    public void setCurrentCustomer(Customer newVal) {
        this.currentCustomer = newVal;
    }

    /**
     * Gets current customer.
     *
     * @return the current customer
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
