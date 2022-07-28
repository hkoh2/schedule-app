package com.hankoh.scheduleapp.model;

/**
 * CustomerTotal class for reports. Contains data for reports and to fill
 * TableView.
 */
public class CustomerTotal {
    private Customer customer;
    private Integer total;
    private Integer totalTime;

    /**
     * Instantiates a new Customer total.
     *
     * @param customer  the customer
     * @param total     the total
     * @param totalTime the total time
     */
    public CustomerTotal(Customer customer, int total, int totalTime) {
        this.customer = customer;
        this.total = total;
        this.totalTime = totalTime;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return customer.getCustomerId();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return customer.getName();
    }


    /**
     * Gets customer.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets customer.
     *
     * @param customer the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets total.
     *
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * Gets total time.
     *
     * @return the total time
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /**
     * Sets total time.
     *
     * @param totalTime the total time
     */
    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
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
     * Gets average time. Rounds average time to a whole number.
     *
     * @return the average time
     */
    public double getAverageTime() {
        if (total == 0) {
            return 0;
        }
        return Math.round(totalTime / (double) total);
    }
}
