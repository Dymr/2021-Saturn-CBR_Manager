package com.example.cbr_manager.ui.clientlist;

import com.example.cbr_manager.service.client.Client;

public class ClientListRecyclerItem {
    private int mImageResource;
    private String mText1;
    private String mText2;

    private Client client;

    public ClientListRecyclerItem(int mImageResource, String name, String location, Client client) {
        this.mImageResource = mImageResource;
        this.mText1 = name;
        this.mText2 = location;
        this.client = client;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public Client getClient() {
        return client;
    }
}
