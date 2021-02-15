package com.example.cbr_manager.service.visit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/*
Define our API request endpoints here
 */
public interface VisitAPI {
    @GET("api/visits/")
    Call<List<Visit>> getVisits(@Header("Authorization") String authHeader);

    @GET("api/visits/{id}")
    Call<Visit> getVisits(@Header("Authorization") String authHeader, @Path("id") int id);

    @PUT("api/visits/{id}")
    Call<Visit> modifyVisit(@Header("Authorization") String authHeader, @Path("id") int id, @Body Visit visit);

    @POST("api/visits/}")
    Call<Visit> createVisit(@Header("Authorization") String authHeader, @Body Visit visit);
}
