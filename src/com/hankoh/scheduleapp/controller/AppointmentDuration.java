package com.hankoh.scheduleapp.controller;

public class AppointmentDuration {
    private int duration;

    public AppointmentDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return duration + " minutes";
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
