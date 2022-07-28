package com.hankoh.scheduleapp.model;

/**
 * Contacts class for contacts.
 */
public class Contact implements Comparable<Contact> {
    private int id;
    private String name;
    private String email;

    /**
     * Instantiates a new Contact.
     *
     * @param id    the id
     * @param name  the name
     * @param email the email
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Formatted for Contacts ComboBox.
     *
     * @return
     */
    @Override
    public String toString() {
        return id + " - " + name + " - " + email;
    }

    /**
     * Sorting contacts
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Contact o) {
        return name.compareTo(o.getName());
    }
}
