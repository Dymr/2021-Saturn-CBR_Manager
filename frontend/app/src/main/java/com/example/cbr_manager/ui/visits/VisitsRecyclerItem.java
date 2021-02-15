package com.example.cbr_manager.ui.visits;

import com.example.cbr_manager.service.visit.Visit;

public class VisitsRecyclerItem {
    private int mImageResource;
    private String mText1;
    private String mText2;

    private Visit visit;

    public VisitsRecyclerItem(int mImageResource, String mText1, String mText2, Visit visit) {
        this.mImageResource = mImageResource;
        this.mText1 = mText1;
        this.mText2 = mText2;
        this.visit = visit;
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

    public Visit getVisit() {
        return visit;
    }
}
