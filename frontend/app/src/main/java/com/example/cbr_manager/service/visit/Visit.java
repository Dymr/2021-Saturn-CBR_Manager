package com.example.cbr_manager.service.visit;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.utils.CBRTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "visit")
public class Visit extends CBRTimestamp {

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "visit_id")
    private Integer id;

    @SerializedName("client_id")
    @Expose
    private Integer clientId;

    @SerializedName("user_creator")
    @Expose
    private Integer userId;

    @SerializedName("additional_notes")
    @Expose
    private String additionalInfo;

    @SerializedName("client")
    @Expose
    @Embedded(prefix = "client_")
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    private Client client;

    @SerializedName("is_cbr_purpose")
    @Expose
    private boolean isCBRPurpose;

    @SerializedName("is_disability_referral_purpose")
    @Expose
    private boolean isDisabilityReferralPurpose;

    @SerializedName("is_disability_follow_up_purpose")
    @Expose
    private boolean isDisabilityFollowUpPurpose;

    @SerializedName("is_health_provision")
    @Expose
    private boolean isHealthProvision;

    @SerializedName("is_education_provision")
    @Expose
    private boolean isEducationProvision;

    @SerializedName("is_social_provision")
    @Expose
    private boolean isSocialProvision;

    @SerializedName("cbr_worker_name")
    @Expose
    private String cbrWorkerName;

    @SerializedName("location_visit_gps")
    @Expose
    private String locationVisitGPS;

    @SerializedName("location_drop_down")
    @Expose
    private String locationDropDown;

    @SerializedName("village_no_visit")
    @Expose
    private Integer villageNoVisit;

    @SerializedName("wheelchair_health_provision")
    @Expose
    private boolean wheelchairHealthProvision;

    @SerializedName("prosthetic_health_provision")
    @Expose
    private boolean prostheticHealthProvision;

    @SerializedName("orthotic_health_provision")
    @Expose
    private boolean orthoticHealthProvision;

    @SerializedName("repairs_health_provision")
    @Expose
    private boolean repairsHealthProvision;

    @SerializedName("referral_health_provision")
    @Expose
    private boolean referralHealthProvision;

    @SerializedName("advice_health_provision")
    @Expose
    private boolean adviceHealthProvision;

    @SerializedName("advocacy_health_provision")
    @Expose
    private boolean advocacyHealthProvision;

    @SerializedName("encouragement_health_provision")
    @Expose
    private boolean encouragementHealthProvision;

    @SerializedName("wheelchair_health_provision_text")
    @Expose
    private String wheelchairHealthProvisionText;

    @SerializedName("prosthetic_health_provision_text")
    @Expose
    private String prostheticHealthProvisionText;

    @SerializedName("orthotic_health_provision_text")
    @Expose
    private String orthoticHealthProvisionText;

    @SerializedName("repairs_health_provision_text")
    @Expose
    private String repairsHealthProvisionText;

    @SerializedName("referral_health_provision_text")
    @Expose
    private String referralHealthProvisionText;

    @SerializedName("advice_health_provision_text")
    @Expose
    private String adviceHealthProvisionText;

    @SerializedName("advocacy_health_provision_text")
    @Expose
    private String advocacyHealthProvisionText;

    @SerializedName("encouragement_health_provision_text")
    @Expose
    private String encouragementHealthProvisionText;

    @SerializedName("conclusion_health_provision")
    @Expose
    private String conclusionHealthProvision;

    @SerializedName("advice_education_provision")
    @Expose
    private boolean adviceEducationProvision;

    @SerializedName("advocacy_education_provision")
    @Expose
    private boolean advocacyEducationProvision;

    @SerializedName("referral_education_provision")
    @Expose
    private boolean referralEducationProvision;

    @SerializedName("encouragement_education_provision")
    @Expose
    private boolean encouragementEducationProvision;

    @SerializedName("advice_education_provision_text")
    @Expose
    private String adviceEducationProvisionText;

    @SerializedName("advocacy_education_provision_text")
    @Expose
    private String advocacyEducationProvisionText;

    @SerializedName("referral_education_provision_text")
    @Expose
    private String referralEducationProvisionText;

    @SerializedName("encouragement_education_provision_text")
    @Expose
    private String encouragementEducationProvisionText;

    @SerializedName("conclusion_education_provision")
    @Expose
    private String conclusionEducationProvision;

    @SerializedName("advice_social_provision")
    @Expose
    private boolean adviceSocialProvision;

    @SerializedName("advocacy_social_provision")
    @Expose
    private boolean advocacySocialProvision;

    @SerializedName("referral_social_provision")
    @Expose
    private boolean referralSocialProvision;

    @SerializedName("encouragement_social_provision")
    @Expose
    private boolean encouragementSocialProvision;

    @SerializedName("advice_social_provision_text")
    @Expose
    private String adviceSocialProvisionText;

    @SerializedName("advocacy_social_provision_text")
    @Expose
    private String advocacySocialProvisionText;

    @SerializedName("referral_social_provision_text")
    @Expose
    private String referralSocialProvisionText;

    @SerializedName("encouragement_social_provision_text")
    @Expose
    private String encouragementSocialProvisionText;

    @SerializedName("conclusion_social_provision")
    @Expose
    private String conclusionSocialProvision;

    @Ignore
    public Visit(String additionalInfo, Integer clientId, Integer userId, Client client) {
        this.additionalInfo = additionalInfo;
        this.clientId = clientId;
        this.userId = userId;
        this.client = client;
    }

    public Visit() {
        this.additionalInfo = "";
        this.clientId = 0;
        this.userId = 0;
        this.client = new Client();
        this.adviceEducationProvision = false;
        this.adviceEducationProvisionText = "";
        this.adviceHealthProvision = false;
        this.adviceHealthProvisionText = "";
        this.adviceSocialProvision = false;
        this.adviceSocialProvisionText = "";
        this.advocacyEducationProvision = false;
        this.advocacyEducationProvisionText = "";
        this.advocacyHealthProvision = false;
        this.advocacyHealthProvisionText = "";
        this.advocacySocialProvision = false;
        this.advocacySocialProvisionText = "";
        this.conclusionEducationProvision = "";
        this.conclusionHealthProvision = "";
        this.conclusionSocialProvision = "";
        this.cbrWorkerName = "";
        this.encouragementEducationProvision = false;
        this.encouragementEducationProvisionText = "";
        this.encouragementHealthProvision = false;
        this.encouragementHealthProvisionText = "";
        this.encouragementSocialProvision = false;
        this.encouragementSocialProvisionText = "";
        this.isCBRPurpose = false;
        this.isDisabilityFollowUpPurpose = false;
        this.isDisabilityReferralPurpose = false;
        this.isEducationProvision = false;
        this.isHealthProvision = false;
        this.isSocialProvision = false;
        this.locationDropDown = "";
        this.locationVisitGPS = "";
        this.orthoticHealthProvision = false;
        this.orthoticHealthProvisionText = "";
        this.prostheticHealthProvision = false;
        this.prostheticHealthProvisionText = "";
        this.repairsHealthProvision = false;
        this.repairsHealthProvisionText = "";
        this.referralEducationProvision = false;
        this.referralEducationProvisionText = "";
        this.referralHealthProvision = false;
        this.referralHealthProvisionText = "";
        this.referralSocialProvision = false;
        this.referralSocialProvisionText = "";
        this.villageNoVisit = 0;
        this.wheelchairHealthProvision = false;
        this.wheelchairHealthProvisionText = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isCBRPurpose() {
        return isCBRPurpose;
    }

    public void setCBRPurpose(boolean CBRPurpose) {
        isCBRPurpose = CBRPurpose;
    }

    public boolean isDisabilityReferralPurpose() {
        return isDisabilityReferralPurpose;
    }

    public void setDisabilityReferralPurpose(boolean disabilityReferralPurpose) {
        isDisabilityReferralPurpose = disabilityReferralPurpose;
    }

    public boolean isDisabilityFollowUpPurpose() {
        return isDisabilityFollowUpPurpose;
    }

    public void setDisabilityFollowUpPurpose(boolean disabilityFollowUpPurpose) {
        isDisabilityFollowUpPurpose = disabilityFollowUpPurpose;
    }

    public boolean isHealthProvision() {
        return isHealthProvision;
    }

    public void setHealthProvision(boolean healthProvision) {
        isHealthProvision = healthProvision;
    }

    public boolean isEducationProvision() {
        return isEducationProvision;
    }

    public void setEducationProvision(boolean educationProvision) {
        isEducationProvision = educationProvision;
    }

    public boolean isSocialProvision() {
        return isSocialProvision;
    }

    public void setSocialProvision(boolean socialProvision) {
        isSocialProvision = socialProvision;
    }

    public String getCbrWorkerName() {
        return cbrWorkerName;
    }

    public void setCbrWorkerName(String cbrWorkerName) {
        this.cbrWorkerName = cbrWorkerName;
    }

    public String getLocationVisitGPS() {
        return locationVisitGPS;
    }

    public void setLocationVisitGPS(String locationVisitGPS) {
        this.locationVisitGPS = locationVisitGPS;
    }

    public String getLocationDropDown() {
        return locationDropDown;
    }

    public void setLocationDropDown(String locationDropDown) {
        this.locationDropDown = locationDropDown;
    }

    public Integer getVillageNoVisit() {
        return villageNoVisit;
    }

    public void setVillageNoVisit(Integer villageNoVisit) {
        this.villageNoVisit = villageNoVisit;
    }

    public boolean isWheelchairHealthProvision() {
        return wheelchairHealthProvision;
    }

    public void setWheelchairHealthProvision(boolean wheelchairHealthProvision) {
        this.wheelchairHealthProvision = wheelchairHealthProvision;
    }

    public boolean isProstheticHealthProvision() {
        return prostheticHealthProvision;
    }

    public void setProstheticHealthProvision(boolean prostheticHealthProvision) {
        this.prostheticHealthProvision = prostheticHealthProvision;
    }

    public boolean isOrthoticHealthProvision() {
        return orthoticHealthProvision;
    }

    public void setOrthoticHealthProvision(boolean orthoticHealthProvision) {
        this.orthoticHealthProvision = orthoticHealthProvision;
    }

    public boolean isRepairsHealthProvision() {
        return repairsHealthProvision;
    }

    public void setRepairsHealthProvision(boolean repairsHealthProvision) {
        this.repairsHealthProvision = repairsHealthProvision;
    }

    public boolean isReferralHealthProvision() {
        return referralHealthProvision;
    }

    public void setReferralHealthProvision(boolean referralHealthProvision) {
        this.referralHealthProvision = referralHealthProvision;
    }

    public boolean isAdviceHealthProvision() {
        return adviceHealthProvision;
    }

    public void setAdviceHealthProvision(boolean adviceHealthProvision) {
        this.adviceHealthProvision = adviceHealthProvision;
    }

    public boolean isAdvocacyHealthProvision() {
        return advocacyHealthProvision;
    }

    public void setAdvocacyHealthProvision(boolean advocacyHealthProvision) {
        this.advocacyHealthProvision = advocacyHealthProvision;
    }

    public boolean isEncouragementHealthProvision() {
        return encouragementHealthProvision;
    }

    public void setEncouragementHealthProvision(boolean encouragementHealthProvision) {
        this.encouragementHealthProvision = encouragementHealthProvision;
    }

    public String getWheelchairHealthProvisionText() {
        return wheelchairHealthProvisionText;
    }

    public void setWheelchairHealthProvisionText(String wheelchairHealthProvisionText) {
        this.wheelchairHealthProvisionText = wheelchairHealthProvisionText;
    }

    public String getProstheticHealthProvisionText() {
        return prostheticHealthProvisionText;
    }

    public void setProstheticHealthProvisionText(String prostheticHealthProvisionText) {
        this.prostheticHealthProvisionText = prostheticHealthProvisionText;
    }

    public String getOrthoticHealthProvisionText() {
        return orthoticHealthProvisionText;
    }

    public void setOrthoticHealthProvisionText(String orthoticHealthProvisionText) {
        this.orthoticHealthProvisionText = orthoticHealthProvisionText;
    }

    public String getRepairsHealthProvisionText() {
        return repairsHealthProvisionText;
    }

    public void setRepairsHealthProvisionText(String repairsHealthProvisionText) {
        this.repairsHealthProvisionText = repairsHealthProvisionText;
    }

    public String getReferralHealthProvisionText() {
        return referralHealthProvisionText;
    }

    public void setReferralHealthProvisionText(String referralHealthProvisionText) {
        this.referralHealthProvisionText = referralHealthProvisionText;
    }

    public String getAdviceHealthProvisionText() {
        return adviceHealthProvisionText;
    }

    public void setAdviceHealthProvisionText(String adviceHealthProvisionText) {
        this.adviceHealthProvisionText = adviceHealthProvisionText;
    }

    public String getAdvocacyHealthProvisionText() {
        return advocacyHealthProvisionText;
    }

    public void setAdvocacyHealthProvisionText(String advocacyHealthProvisionText) {
        this.advocacyHealthProvisionText = advocacyHealthProvisionText;
    }

    public String getEncouragementHealthProvisionText() {
        return encouragementHealthProvisionText;
    }

    public void setEncouragementHealthProvisionText(String encouragementHealthProvisionText) {
        this.encouragementHealthProvisionText = encouragementHealthProvisionText;
    }

    public String getConclusionHealthProvision() {
        return conclusionHealthProvision;
    }

    public void setConclusionHealthProvision(String conclusionHealthProvision) {
        this.conclusionHealthProvision = conclusionHealthProvision;
    }

    public boolean isAdviceEducationProvision() {
        return adviceEducationProvision;
    }

    public void setAdviceEducationProvision(boolean adviceEducationProvision) {
        this.adviceEducationProvision = adviceEducationProvision;
    }

    public boolean isAdvocacyEducationProvision() {
        return advocacyEducationProvision;
    }

    public void setAdvocacyEducationProvision(boolean advocacyEducationProvision) {
        this.advocacyEducationProvision = advocacyEducationProvision;
    }

    public boolean isReferralEducationProvision() {
        return referralEducationProvision;
    }

    public void setReferralEducationProvision(boolean referralEducationProvision) {
        this.referralEducationProvision = referralEducationProvision;
    }

    public boolean isEncouragementEducationProvision() {
        return encouragementEducationProvision;
    }

    public void setEncouragementEducationProvision(boolean encouragementEducationProvision) {
        this.encouragementEducationProvision = encouragementEducationProvision;
    }

    public String getAdviceEducationProvisionText() {
        return adviceEducationProvisionText;
    }

    public void setAdviceEducationProvisionText(String adviceEducationProvisionText) {
        this.adviceEducationProvisionText = adviceEducationProvisionText;
    }

    public String getAdvocacyEducationProvisionText() {
        return advocacyEducationProvisionText;
    }

    public void setAdvocacyEducationProvisionText(String advocacyEducationProvisionText) {
        this.advocacyEducationProvisionText = advocacyEducationProvisionText;
    }

    public String getReferralEducationProvisionText() {
        return referralEducationProvisionText;
    }

    public void setReferralEducationProvisionText(String referralEducationProvisionText) {
        this.referralEducationProvisionText = referralEducationProvisionText;
    }

    public String getEncouragementEducationProvisionText() {
        return encouragementEducationProvisionText;
    }

    public void setEncouragementEducationProvisionText(String encouragementEducationProvisionText) {
        this.encouragementEducationProvisionText = encouragementEducationProvisionText;
    }


    public String getConclusionEducationProvision() {
        return conclusionEducationProvision;
    }

    public void setConclusionEducationProvision(String conclusionEducationProvision) {
        this.conclusionEducationProvision = conclusionEducationProvision;
    }

    public boolean isAdviceSocialProvision() {
        return adviceSocialProvision;
    }

    public void setAdviceSocialProvision(boolean adviceSocialProvision) {
        this.adviceSocialProvision = adviceSocialProvision;
    }

    public boolean isAdvocacySocialProvision() {
        return advocacySocialProvision;
    }

    public void setAdvocacySocialProvision(boolean advocacySocialProvision) {
        this.advocacySocialProvision = advocacySocialProvision;
    }

    public boolean isReferralSocialProvision() {
        return referralSocialProvision;
    }

    public void setReferralSocialProvision(boolean referralSocialProvision) {
        this.referralSocialProvision = referralSocialProvision;
    }

    public boolean isEncouragementSocialProvision() {
        return encouragementSocialProvision;
    }

    public void setEncouragementSocialProvision(boolean encouragementSocialProvision) {
        this.encouragementSocialProvision = encouragementSocialProvision;
    }

    public String getAdviceSocialProvisionText() {
        return adviceSocialProvisionText;
    }

    public void setAdviceSocialProvisionText(String adviceSocialProvisionText) {
        this.adviceSocialProvisionText = adviceSocialProvisionText;
    }

    public String getAdvocacySocialProvisionText() {
        return advocacySocialProvisionText;
    }

    public void setAdvocacySocialProvisionText(String advocacySocialProvisionText) {
        this.advocacySocialProvisionText = advocacySocialProvisionText;
    }

    public String getReferralSocialProvisionText() {
        return referralSocialProvisionText;
    }

    public void setReferralSocialProvisionText(String referralSocialProvisionText) {
        this.referralSocialProvisionText = referralSocialProvisionText;
    }

    public String getEncouragementSocialProvisionText() {
        return encouragementSocialProvisionText;
    }

    public void setEncouragementSocialProvisionText(String encouragementSocialProvisionText) {
        this.encouragementSocialProvisionText = encouragementSocialProvisionText;
    }

    public String getConclusionSocialProvision() {
        return conclusionSocialProvision;
    }

    public void setConclusionSocialProvision(String conclusionSocialProvision) {
        this.conclusionSocialProvision = conclusionSocialProvision;
    }

}
