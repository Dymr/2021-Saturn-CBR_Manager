package com.example.cbr_manager.service.client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHistoryRecord {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("old_value")
    @Expose
    private String oldValue;
    @SerializedName("new_value")
    @Expose
    private String newValue;
    @SerializedName("client")
    @Expose
    private Integer client;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public String getFormattedDate() {
        String datePython = getDateCreated().substring(0,19);
        String patternOutput = "MM/dd/yyyy   HH:mm";
        String patternInput = "yyyy-MM-DD'T'HH:mm:ss";

        SimpleDateFormat sdfInput = new SimpleDateFormat(patternInput);
        SimpleDateFormat sdfOutput = new SimpleDateFormat(patternOutput);
        Date date = null;
        try {
            date = sdfInput.parse(datePython);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfOutput.format(date);
    }
}
