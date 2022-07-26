package com.hankoh.scheduleapp.controller;

public class AppointmentDuration extends Internationalizable {
    private int duration;

    public AppointmentDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return duration + " " + msg.getString("minutes");
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
