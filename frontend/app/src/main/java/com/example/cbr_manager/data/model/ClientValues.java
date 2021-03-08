package com.example.cbr_manager.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.cbr_manager.service.client.Client;

public class ClientValues{

    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String CLIENT_FNAME = "CLIENT_FIRST_NAME";
    public static final String CLIENT_LNAME = "CLIENT_LAST_NAME";
    public static final String CLIENT_CONSENT = "CLIENT_CONSENT";
    public static final String CLIENT_DATE = "CLIENT_DATE";
    public static final String CLIENT_GENDER = "CLIENT_GENDER";
    public static final String CLIENT_AGE = "CLIENT_AGE";
    public static final String CLIENT_LOCATION = "CLIENT_LOCATION";
    public static final String CLIENT_VILLAGE = "CLIENT_VILLAGE";
    public static final String CLIENT_DISABILITY = "CLIENT_DISABILITY";
    public static final String CLIENT_CARE_PRESENT = "CLIENT_CARE_PRESENT";
    public static final String CLIENT_CONTACT_CLIENT = "CLIENT_CONTACT_CLIENT";
    public static final String CLIENT_CONTACT_CARE = "CLIENT_CONTACT_CARE";


    private ContentValues clientValues = new ContentValues();

    public ContentValues setClient(Client client){
        clientValues.put(CLIENT_ID, client.getId());
        clientValues.put(CLIENT_FNAME, client.getFirstName());
        clientValues.put(CLIENT_LNAME, client.getLastName());
        clientValues.put(CLIENT_CONSENT, client.getConsent());
        clientValues.put(CLIENT_DATE, client.getDate());
        clientValues.put(CLIENT_GENDER, client.getGender());
        clientValues.put(CLIENT_AGE, client.getAge());
        clientValues.put(CLIENT_LOCATION, client.getLocation());
        clientValues.put(CLIENT_VILLAGE, client.getVillageNo());
        clientValues.put(CLIENT_DISABILITY, client.getDisability());
        clientValues.put(CLIENT_CARE_PRESENT, client.getCarePresent());
        clientValues.put(CLIENT_CONTACT_CLIENT, client.getContactClient());
        clientValues.put(CLIENT_CONTACT_CARE, client.getContactCare());

        return clientValues;
    }

    public void clearAll(){
        clientValues.clear();
    }

    public static Client fromContentValues(ContentValues contentValues){
        Client client = new Client();
        if(contentValues.containsKey(CLIENT_ID)){
            client.setId(contentValues.getAsInteger(CLIENT_ID));
        }
        if(contentValues.containsKey(CLIENT_FNAME)){
            client.setFirstName(contentValues.getAsString(CLIENT_FNAME));
        }
        if(contentValues.containsKey(CLIENT_LNAME)){
            client.setFirstName(contentValues.getAsString(CLIENT_LNAME));
        }
        if(contentValues.containsKey(CLIENT_CONSENT)){
            client.setFirstName(contentValues.getAsString(CLIENT_CONSENT));
        }
        if(contentValues.containsKey(CLIENT_DATE)){
            client.setFirstName(contentValues.getAsString(CLIENT_DATE));
        }
        if(contentValues.containsKey(CLIENT_GENDER)){
            client.setFirstName(contentValues.getAsString(CLIENT_GENDER));
        }
        if(contentValues.containsKey(CLIENT_AGE)){
            client.setFirstName(contentValues.getAsString(CLIENT_AGE));
        }
        if(contentValues.containsKey(CLIENT_LOCATION)){
            client.setFirstName(contentValues.getAsString(CLIENT_LOCATION));
        }
        if(contentValues.containsKey(CLIENT_VILLAGE)){
            client.setFirstName(contentValues.getAsString(CLIENT_VILLAGE));
        }
        if(contentValues.containsKey(CLIENT_DISABILITY)){
            client.setFirstName(contentValues.getAsString(CLIENT_DISABILITY));
        }
        if(contentValues.containsKey(CLIENT_CARE_PRESENT)){
            client.setFirstName(contentValues.getAsString(CLIENT_CARE_PRESENT));
        }
        if(contentValues.containsKey(CLIENT_CONTACT_CLIENT)){
            client.setFirstName(contentValues.getAsString(CLIENT_CONTACT_CLIENT));
        }
        if(contentValues.containsKey(CLIENT_CONTACT_CARE)){
            client.setFirstName(contentValues.getAsString(CLIENT_CONTACT_CARE));
        }


        return client;
    }



}
