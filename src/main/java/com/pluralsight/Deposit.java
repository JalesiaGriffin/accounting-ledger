package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Deposit {
    private LocalDate date;
    private LocalTime time;
    private String vendor;
    private double amount;

    public Deposit(LocalDate date, LocalTime time, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "date=" + date +
                ", time=" + time +
                ", vendor='" + vendor + '\'' +
                ", amount=" + amount +
                '}';
    }
}
