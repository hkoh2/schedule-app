package com.hankoh.scheduleapp.controller;

/**
 * Duration for Appointments TableView.
 */
public class AppointmentDuration extends Internationalizable {
    private int duration;

    /**
     * Instantiates a new Appointment duration.
     *
     * @param duration the duration
     */
    public AppointmentDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return duration + " " + msg.getString("minutes");
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
