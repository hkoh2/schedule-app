package com.hankoh.scheduleapp.controller;

import java.time.LocalDate;

/**
 * YearWeek class contains the start and end date of a week.
 * Used for filtering appointments.
 */
public class YearWeek implements Comparable<YearWeek> {
    private LocalDate start;
    private LocalDate end;

    /**
     * Instantiates a YearWeek class.
     *
     * @param start start date of appointment.
     * @param end   end date of appointment.
     */
    public YearWeek(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets start date.
     *
     * @return start date.
     */
    public LocalDate getStart() {
        return start;
    }

    /**
     * Sets start date.
     *
     * @param start start date.
     */
    public void setStart(LocalDate start) {
        this.start = start;
    }

    /**
     * Gets end date.
     *
     * @return end date.
     */
    public LocalDate getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end end date.
     */
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
