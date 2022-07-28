package com.hankoh.scheduleapp.model;

import java.time.Month;
import java.util.Objects;

/**
 * MonthTotal class contains total number of appointments for each month.
 */
public class MonthTotal {
    private Month month;
    private Integer total;
    private String type;

    /**
     * Instantiates a new Month total.
     *
     * @param type  the type
     * @param month the month
     * @param total the total
     */
    public MonthTotal(String type, Month month, Integer total) {
        this.type = type;
        this.month = month;
        this.total = total;
    }

    /**
     * Instantiates a new Month total.
     *
     * @param month the month
     */
    public MonthTotal(Month month) {
        this.month = month;
        this.total = 0;
    }

    /**
     * Gets month.
     *
     * @return the month
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Sets month.
     *
     * @param month the month
     */
    public void setMonth(Month month) {
        this.month = month;
    }

    /**
     * Gets total.
     *
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }


    /**
     * Increment.
     */
    public void increment() {
        this.total = this.total + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthTotal that = (MonthTotal) o;
        return month == that.month && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, type);
    }
}
