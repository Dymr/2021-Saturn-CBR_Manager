package com.example.cbr_manager.service.visit;

import com.example.cbr_manager.BuildConfig;
import com.example.cbr_manager.helper.Helper;
import com.example.cbr_manager.service.auth.AuthResponse;
import com.example.cbr_manager.service.client.ClientAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VisitService {
    private static final String BASE_URL = BuildConfig.API_URL;

    private final AuthResponse authToken;

    private final String authHeader;

    private VisitAPI visitAPI;

    public VisitService(AuthResponse authToken) {
        this.authToken = authToken;
        this.authHeader = Helper.formatTokenHeader(this.authToken);
        this.visitAPI = getVisitAPI();
    }

    public VisitService() {
        this.authToken = null;
        this.authHeader = null;
        this.visitAPI = getVisitAPI();
    }


    private VisitAPI getVisitAPI() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(VisitAPI.class);
    }

    public Call<List<Visit>> getVisits() {
        return this.visitAPI.getVisits(authHeader);
    }

    public Call<Visit> getVisit(int visitID) {
        return this.visitAPI.getVisit(authHeader, visitID);
    }

    public Call<Visit> createVisit(Visit visit) {
        return this.visitAPI.createVisit(authHeader, visit);
    }




}
