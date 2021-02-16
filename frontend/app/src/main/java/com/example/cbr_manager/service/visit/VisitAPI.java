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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/*
Define our API request endpoints here
 */
public interface VisitAPI {
    @GET("api/visits/")
    Call<List<Visit>> getVisits(@Header("Authorization") String authHeader);

    @GET("api/visits/{id}")
    Call<Visit> getVisit(@Header("Authorization") String authHeader, @Path("id") int id);

    @PUT("api/visits/{id}")
    Call<Visit> modifyVisit(@Header("Authorization") String authHeader, @Path("id") int id, @Body Visit visit);

    @POST("api/visits/}")
    Call<Visit> createVisit(@Header("Authorization") String authHeader, @Body Visit visit);

    @Multipart
    @POST("api/visits/")
    Call<Client> createVisitManual(@Header("Authorization") String authHeader,
                                    @Part("datetime_created") RequestBody datetimeCreated,
                                    @Part("user_creator") RequestBody userCreator,
                                    @Part("client") RequestBody client,
                                    @Part("client_state_previous") RequestBody clientStatePrevious,
                                    @Part("client_state_updated") RequestBody clientStateUpdated,
                                    @Part("client_info_changed") RequestBody clientInfoChanged,
                                    @Part("additional_notes") RequestBody additionalNotes);
}
