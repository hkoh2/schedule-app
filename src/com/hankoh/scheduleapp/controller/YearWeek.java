package com.hankoh.scheduleapp.controller;

import java.time.LocalDate;

public class YearWeek implements Comparable<YearWeek> {
    private LocalDate start;
    private LocalDate end;

    public YearWeek(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }

    @Override
    public int compareTo(YearWeek o) {
        if (start.isEqual(o.getStart())) {
            return 0;
        }
        if (start.isBefore(o.getStart())) {
            return 1;
        }
        return -1;
    }
    @Override
    public int hashCode() {
        return 31 * 1 + start.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof YearWeek)) {
            return false;
        }
        YearWeek other = (YearWeek) obj;
        return start.isEqual(other.getStart());
    }
}
