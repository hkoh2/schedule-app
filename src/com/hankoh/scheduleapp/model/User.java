package com.hankoh.scheduleapp.model;

/**
 * User class contains application users.
 */
public class User {
    private int userId;
    private String userName;
    private String password;

    /**
     * Instantiates a new User.
     *
     * @param userId   the user id
     * @param userName the user name
     */
    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets username.
     *
     * @param userName the username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return userName;
    }
}
