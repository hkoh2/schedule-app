package com.hankoh.scheduleapp.model;


import javafx.scene.control.Tab;

/**
 * Singleton for data shared across all scenes.
 */
public final class DataStorage {
    private User user;
    private final static DataStorage storage = new DataStorage();
    private Tab currentTab;
    private int currentTabIndex;
    private Customer currentCustomer;
    private Appointment currentAppointment;

    private DataStorage() {}

    public static DataStorage getInstance() {
        return storage;
    }

    public Appointment getCurrentAppointment() {
        return currentAppointment;
    }

    public void setCurrentAppointment(Appointment currentAppointment) {
        this.currentAppointment = currentAppointment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void clearAll() {
        this.user = null;

    }

    public Tab getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }

    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

    public void setCurrentCustomer(Customer newVal) {
        this.currentCustomer = newVal;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
