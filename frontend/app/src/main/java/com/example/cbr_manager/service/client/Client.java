package com.example.cbr_manager.service.client;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.cbr_manager.utils.CBRTimestamp;
import com.example.cbr_manager.utils.Helper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "client")
public class Client extends CBRTimestamp {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer id;

    @SerializedName("id")
    @Expose
    private Integer serverId;

    @SerializedName("cbr_client_id")
    @Expose
    private String cbrClientId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("gps_location")
    @Expose
    private String gpsLocation;
    @SerializedName("consent")
    @Expose
    private String consent;
    @SerializedName("village_no")
    @Expose
    private Integer villageNo;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("contact_client")
    @Expose
    private String contactClient;
    @SerializedName("care_present")
    @Expose
    private String carePresent;
    @SerializedName("contact_care")
    @Expose
    private String contactCare;
    @SerializedName("photo")
    @Expose(serialize = false) // Do not send 'photo' attribute to API
    private String photoURL;
    @SerializedName("disability")
    @Expose
    private String disability;
    @SerializedName("health_risk")
    @Expose
    private Integer healthRisk;
    @SerializedName("health_require")
    @Expose
    private String healthRequire;
    @SerializedName("health_goal")
    @Expose
    private String healthGoal;
    @SerializedName("education_risk")
    @Expose
    private Integer educationRisk;
    @SerializedName("education_require")
    @Expose
    private String educationRequire;
    @SerializedName("education_goal")
    @Expose
    private String educationGoal;
    @SerializedName("social_risk")
    @Expose
    private Integer socialRisk;
    @SerializedName("social_require")
    @Expose
    private String socialRequire;
    @SerializedName("social_goal")
    @Expose
    private String socialGoal;
    @SerializedName("goal_met_health_provision")
    @Expose
    private String goalMetHealthProvision;
    @SerializedName("goal_met_education_provision")
    @Expose
    private String goalMetEducationProvision;
    @SerializedName("goal_met_social_provision")
    @Expose
    private String goalMetSocialProvision;

    private boolean isNewClient;

    @SerializedName("taken_baseline_survey")
    @Expose
    private boolean baselineSurveyTaken;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;



    //Initializing fields that are needed for POST request in itr1
    public Client() {
        super(Helper.getCurrentUTCTime().toString(), Helper.getCurrentUTCTime().toString());
        this.consent = "";
        this.date = "";
        this.firstName = "";
        this.lastName = "";
        this.contactClient = "";
        this.age = 0;
        this.birthdate = "";
        this.gender = "";
        this.location = "";
        this.villageNo = 0;
        this.disability = "";
        this.carePresent = "";
        this.contactCare = "";
        this.healthRisk = 0;
        this.healthGoal = "";
        this.healthRequire = "";
        this.socialRisk = 0;
        this.educationRisk = 0;
        this.isNewClient = true;
        this.cbrClientId = "";
        this.baselineSurveyTaken = false;
        this.longitude = 0;
        this.latitude = 0;

    }

    @Ignore
    public Client(String consent, String date, String firstName, String lastName, String contactClient, String birthdate,
                  String gender, int id, String location, int villageNo, String disability,
                  String carePresent, String contactCare) {
        super(Helper.getCurrentUTCTime().toString(), Helper.getCurrentUTCTime().toString());
        this.consent = consent;
        this.date = date;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactClient = contactClient;
        this.birthdate = birthdate;
        this.gender = gender;
        this.id = id;
        this.location = location;
        this.villageNo = villageNo;
        this.disability = disability;
        this.carePresent = carePresent;
        this.contactCare = contactCare;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public Integer getVillageNo() {
        return villageNo;
    }

    public void setVillageNo(Integer villageNo) {
        this.villageNo = villageNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthdate() { return birthdate; }

    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getContactClient() {
        return contactClient;
    }

    public void setContactClient(String contactClient) {
        this.contactClient = contactClient;
    }

    public String getCarePresent() {
        return carePresent;
    }

    public void setCarePresent(String carePresent) {
        this.carePresent = carePresent;
    }

    public String getContactCare() {
        return contactCare;
    }

    public void setContactCare(String contactCare) {
        this.contactCare = contactCare;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public Integer getHealthRisk() {
        return healthRisk;
    }

    public void setHealthRisk(Integer healthRisk) {
        this.healthRisk = healthRisk;
    }

    public String getHealthRequire() {
        return healthRequire;
    }

    public void setHealthRequire(String healthRequire) {
        this.healthRequire = healthRequire;
    }

    public String getHealthGoal() {
        return healthGoal;
    }

    public void setHealthGoal(String healthGoal) {
        this.healthGoal = healthGoal;
    }

    public Integer getEducationRisk() {
        return educationRisk;
    }

    public void setEducationRisk(Integer educationRisk) {
        this.educationRisk = educationRisk;
    }

    public String getEducationRequire() {
        return educationRequire;
    }

    public void setEducationRequire(String educationRequire) {
        this.educationRequire = educationRequire;
    }

    public String getEducationGoal() {
        return educationGoal;
    }

    public void setEducationGoal(String educationGoal) {
        this.educationGoal = educationGoal;
    }

    public Integer getSocialRisk() {
        return socialRisk;
    }

    public void setSocialRisk(Integer socialRisk) {
        this.socialRisk = socialRisk;
    }

    public String getSocialRequire() {
        return socialRequire;
    }

    public void setSocialRequire(String socialRequire) {
        this.socialRequire = socialRequire;
    }

    public String getSocialGoal() {
        return socialGoal;
    }

    public void setSocialGoal(String socialGoal) {
        this.socialGoal = socialGoal;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getCbrClientId() {
        return cbrClientId;
    }

    public void setCbrClientId(String cbrClientId) {
        this.cbrClientId = cbrClientId;
    }

    public String getGoalMetHealthProvision() {
        return goalMetHealthProvision;
    }

    public void setGoalMetHealthProvision(String goalMetHealthProvision) {
        this.goalMetHealthProvision = goalMetHealthProvision;
    }

    public String getGoalMetEducationProvision() {
        return goalMetEducationProvision;
    }

    public void setGoalMetEducationProvision(String goalMetEducationProvision) {
        this.goalMetEducationProvision = goalMetEducationProvision;
    }

    public String getGoalMetSocialProvision() {
        return goalMetSocialProvision;
    }

    public void setGoalMetSocialProvision(String goalMetSocialProvision) {
        this.goalMetSocialProvision = goalMetSocialProvision;
    }

    public boolean isNewClient() {
        return isNewClient;
    }

    public void setNewClient(boolean newClient) {
        isNewClient = newClient;
    }

    public boolean isBaselineSurveyTaken() { return baselineSurveyTaken; }

    public void setBaselineSurveyTaken(boolean newBaselineSurveyStatus) { baselineSurveyTaken = newBaselineSurveyStatus; }

    public Integer calculateRiskScore() {
        double healthRiskLogScale = Math.pow(10, healthRisk)*1.2;
        double socialRiskLogScale = Math.pow(10, socialRisk)*1.1;
        double educationRiskLogScale = Math.pow(10, educationRisk);
        return (int) (healthRiskLogScale + socialRiskLogScale + educationRiskLogScale);
    }

    // some code here inspired by https://stackoverflow.com/questions/6667243/using-enum-values-as-string-literals
    public enum RiskLabel {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        CRITICAL("Critical");

        private final String name;

        RiskLabel(String string) {
            name = string;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static RiskLabel assignLabelToRiskScore(Integer riskScore) {
        if (riskScore < 100) {
            return RiskLabel.LOW;
        }
        else if (riskScore < 1000) {
            return RiskLabel.MEDIUM;
        }
        else if (riskScore < 10000) {
            return RiskLabel.HIGH;
        }
        else {
            return RiskLabel.CRITICAL;
        }
    }

    public RiskLabel assignRiskLabel() {
        Integer riskScore = this.calculateRiskScore();
        return assignLabelToRiskScore(riskScore);
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double newLatitude) { latitude = newLatitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double newLongitude) { longitude = newLongitude; }




}
