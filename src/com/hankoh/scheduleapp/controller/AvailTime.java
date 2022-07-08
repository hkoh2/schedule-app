package com.hankoh.scheduleapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalTime;

public class AvailTime {
    private LocalTime start;
    private LocalTime end;
    private ObservableList<AppointmentDuration> durations = FXCollections.observableArrayList();

    public AvailTime(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }


    public ObservableList<AppointmentDuration> getDurations() {
        return durations;
    }

    public void setDurations(ObservableList<AppointmentDuration> durations) {
        this.durations = durations;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
