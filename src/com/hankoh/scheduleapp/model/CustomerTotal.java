package com.hankoh.scheduleapp.model;

public class CustomerTotal {
    private Customer customer;
    private Integer total;
    private Integer totalTime;

    public CustomerTotal(Customer customer, int total, int totalTime) {
        this.customer = customer;
        this.total = total;
        this.totalTime = totalTime;
    }

    public int getId() {
        return customer.getCustomerId();
    }

    public String getName() {
        return customer.getName();
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getTotal() {
        return total;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public double getAverageTime() {
        if (total == 0) {
            return 0;
        }
        return Math.round(totalTime / (double) total);
    }
}
