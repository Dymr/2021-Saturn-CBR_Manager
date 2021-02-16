package com.example.cbr_manager.service.visit;

import com.example.cbr_manager.service.client.Client;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Visit {
    @SerializedName("datetime_created")
    @Expose
    private Timestamp datetimeCreated;
    @SerializedName("user_creator")
    @Expose
    private Integer userCreator;
    @SerializedName("client")
    @Expose
    private Integer clientID;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
