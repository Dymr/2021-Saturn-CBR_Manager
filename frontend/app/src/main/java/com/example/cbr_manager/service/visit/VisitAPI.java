package com.example.cbr_manager.service.visit;

import com.example.cbr_manager.service.client.Client;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface VisitAPI {

    @GET("api/visits/")
    Call<List<Visit>> getVisits(@Header("Authorization") String authHeader);

    @GET("api/visits/{id}")
    Call<Visit> getVisit(@Header("Authorization") String authHeader, @Path("id") int id);

    @POST("api/visits/")
    Call<Visit> createVisit(@Header("Authorization") String authHeader, @Body Visit visit);

}


