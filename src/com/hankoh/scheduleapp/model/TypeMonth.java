package com.hankoh.scheduleapp.model;

import java.time.Month;
import java.util.Objects;

public class TypeMonth {
    private String type;
    private Month month;

    public TypeMonth(String type, Month month) {
        this.type = type;
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeMonth typeMonth = (TypeMonth) o;
        return Objects.equals(type, typeMonth.type) && month == typeMonth.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, month);
    }
}
