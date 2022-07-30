package com.hankoh.scheduleapp.model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * Appointment class hold customer appointments. All time elements are
 * in ZonedDateTime.
 */
public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int customerId;
    private String customerName;
    private int userId;
    private String userName;
    private int contactId;
    private String contactName;
    private String contactEmail;

    /**
     * Instantiates a new Appointment. Used to check for any conflicts
     * between appointments.
     *
     * @param appointmentId appointment id
     * @param startTime     start time
     * @param endTime       end time
     */
    public Appointment(
            int appointmentId,
            ZonedDateTime startTime,
            ZonedDateTime endTime
    ) {
        this.appointmentId = appointmentId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Instantiates a new Appointment. Contains all properties for appointment.
     *
     * @param appointmentId appointment id
     * @param title         title
     * @param description   description
     * @param location      location
     * @param type          type
     * @param startTime     start time
     * @param endTime       end time
     * @param customerId    customer id
     * @param customerName  customer name
     * @param userId        user id
     * @param userName      username
     * @param contactId     contact id
     * @param contactName   contact name
     * @param contactEmail  contact email
     */
    public Appointment(
            int appointmentId,
            String title,
            String description,
            String location,
            String type,
            ZonedDateTime startTime,
            ZonedDateTime endTime,
            int customerId,
            String customerName,
            int userId,
            String userName,
            int contactId,
            String contactName,
            String contactEmail) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
        this.userName = userName;
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Instantiates a new Appointment. Used for creating new appointment
     * without user ID. IDs are auto-generated.
     *
     * @param title       the title
     * @param description the description
     * @param location    the location
     * @param type        the type
     * @param startTime   the start time
     * @param endTime     the end time
     * @param customerId  the customer id
     * @param userId      the user id
     * @param contactId   the contact id
     */
    public Appointment(
            String title,
            String description,
            String location,
            String type,
            ZonedDateTime startTime,
            ZonedDateTime endTime,
            int customerId,
            int userId,
            int contactId
    ) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Instantiates a new Appointment. Used for updating appointments.
     *
     * @param id          the id
     * @param title       the title
     * @param description the description
     * @param location    the location
     * @param type        the type
     * @param startTime   the start time
     * @param endTime     the end time
     * @param customerId  the customer id
     * @param userId      the user id
     * @param contactId   the contact id
     */
    public Appointment(
            int id,
            String title,
            String description,
            String location,
            String type,
            ZonedDateTime startTime,
            ZonedDateTime endTime,
            int customerId,
            int userId,
            int contactId
    ) {
        this.appointmentId = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Gets appointment id.
     *
     * @return the appointment id
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets appointment id.
     *
     * @param appointmentId the appointment id
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public ZonedDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets start timestamp.
     *
     * @return the start timestamp
     */
    public Timestamp getStartTimestamp() {
        return Timestamp.valueOf(startTime.toLocalDateTime());
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets end timestamp.
     *
     * @return the end timestamp
     */
    public Timestamp getEndTimestamp() {
        return Timestamp.valueOf(endTime.toLocalDateTime());
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets customer id.
     *
     * @param customerId the customer id
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
     * Gets customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets customer name.
     *
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact name.
     *
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets contact email.
     *
     * @return the contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Sets contact email.
     *
     * @param contactEmail the contact email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Gets contact id.
     *
     * @return the contact id
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets contact id.
     *
     * @param contactId the contact id
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String toStringAlert() {
        System.out.println(getStartTime());
        return this.appointmentId + " " + this.customerName;
    }
}
