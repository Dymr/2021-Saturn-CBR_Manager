package com.example.cbr_manager.service.goal;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.utils.CBRTimestamp;
import com.example.cbr_manager.utils.Helper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

@Entity(tableName = "goal")
public class Goal{
    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    private Integer id;

    @SerializedName("datetime_created")
    @Expose
    private Timestamp datetimeCreated;

    @SerializedName("datetime_completed")
    @Expose
    private Timestamp datetimeCompleted;

    @SerializedName("user_creator")
    @Expose
    private Integer userId;

    @SerializedName("client_id")
    @Expose
    private Integer clientId;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("is_initial_goal")
    @Expose
    private boolean isInitialGoal;

    @SerializedName("status")
    @Expose
    private String status;

    public Goal() {
        this.userId = 0;
        this.clientId = -1;
        this.category = "";
        this.title = "";
        this.description = "";
        this.isInitialGoal = false;
        this.status = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Timestamp datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public Timestamp getDatetimeCompleted() {
        return datetimeCompleted;
    }

    public void setDatetimeCompleted(Timestamp datetimeCompleted) {
        this.datetimeCompleted = datetimeCompleted;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInitialGoal() {
        return isInitialGoal;
    }

    public void setIsInitialGoal(boolean isInitialGoal) {
        this.isInitialGoal = isInitialGoal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
