package com.example.cbr_manager.service.visit;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Visit {
    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private Date date;

    private final int id;

    public Visit(String firstName, String lastName, Date date, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
