package com.example.cbr_manager.service.user;

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
public interface UserAPI {
    @GET("api/users/")
    Call<List<User>> getUsers(@Header("Authorization") String authHeader);

    @GET("api/users/{id}")
    Call<User> getUser(@Header("Authorization") String authHeader, @Path("id") int id);

    @PUT("api/users/{id}")
    Call<User> modifyUser(@Header("Authorization") String authHeader, @Path("id") int id, @Body User user);

    @POST("api/users/")
    Call<User> createUser(@Header("Authorization") String authHeader, @Body User user);

    @GET("api/users/current_user")
    Call<User> getCurrentUser(@Header("Authorization") String authHeader);
}
