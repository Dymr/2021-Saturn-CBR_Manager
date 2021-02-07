package com.example.cbr_manager.ui.clientlist;

import com.example.cbr_manager.service.client.Client;

public class ClientListRecyclerItem {
    private int mImageResource;
    private String mText1;
    private String mText2;

    private Client client;

    public ClientListRecyclerItem(int mImageResource, String mText1, String mText2, Client client) {
        this.mImageResource = mImageResource;
        this.mText1 = mText1;
        this.mText2 = mText2;
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
}
