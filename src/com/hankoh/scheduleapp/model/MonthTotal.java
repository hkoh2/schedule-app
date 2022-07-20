package com.hankoh.scheduleapp.model;

import java.time.Month;
import java.util.Objects;

public class MonthTotal {
    private Month month;
    private Integer total;
    private String type;

    public MonthTotal(String type, Month month, Integer total) {
        this.type = type;
        this.month = month;
        this.total = total;
    }
    public MonthTotal(Month month) {
        this.month = month;
        this.total = 0;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


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
