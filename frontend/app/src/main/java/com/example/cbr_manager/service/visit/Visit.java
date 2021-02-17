package com.example.cbr_manager.service.visit;

import com.example.cbr_manager.service.client.Client;
import java.sql.Timestamp;
import com.example.cbr_manager.service.user.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Visit {

    @SerializedName("client_id")
    @Expose
    private int clientID;
    @SerializedName("user_creator")
    @Expose
    private int userID;
    @SerializedName("additional_notes")
    @Expose
    private String additionalInfo;
    @SerializedName("client")
    @Expose
    private Client client = new Client();

    public Visit(String additionalInfo, int clientID, int userID, Client client) {
        this.additionalInfo = additionalInfo;
        this.clientID = clientID;
        this.userID = userID;
        this.client = client;
    }

    public Visit() {

    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getUserID() {
        return userID;
    }

    public int setUserID(int userID) {
        return this.userID = userID;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
