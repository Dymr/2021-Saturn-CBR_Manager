package com.example.cbr_manager.service.visit;

import com.example.cbr_manager.BuildConfig;
import com.example.cbr_manager.helper.Helper;
import com.example.cbr_manager.service.auth.AuthToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VisitService {

    private static final String BASE_URL = BuildConfig.API_URL;

    private final AuthToken authToken;

    private final String authHeader;

    private VisitAPI visitAPI;

    public VisitService(AuthToken auth) {
        this.authToken = auth;

        this.authHeader = Helper.formatTokenHeader(this.authToken);

        this.visitAPI = getVisitAPI();
    }

    private VisitAPI getVisitAPI() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(VisitAPI.class);
    }

    public Call<List<Visit>> getVisits() {
        return this.visitAPI.getVisits(authHeader);
    }

    public Call<Visit> modifyVisit(Visit visit) {
        return this.visitAPI.modifyVisit(authHeader, visit.getId(), visit);
    }

    public Call<Visit> createVisit(Visit visit){
        // note: visit id for the visit object can be anything. default it manually to -1.
        return this.visitAPI.createVisit(authHeader, visit);
    }

    public Call<Visit> getVisit(int visitId) {
        return this.visitAPI.getVisits(authHeader, visitId);
    }

}