package com.hankoh.scheduleapp.model;


/**
 * Singleton for data shared across all scenes.
 */
public final class DataStorage {
    private User user;
    private final static DataStorage storage = new DataStorage();

    private DataStorage() {}

    public static DataStorage getInstance() {
        return storage;
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

}
