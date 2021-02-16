package com.example.cbr_manager.service.user;

import com.example.cbr_manager.BuildConfig;
import com.example.cbr_manager.helper.Helper;
import com.example.cbr_manager.service.auth.AuthResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {

    private static final String BASE_URL = BuildConfig.API_URL;

    private final AuthResponse authToken;

    private final String authHeader;

    private UserAPI userAPI;

    public UserService(AuthResponse auth) {
        this.authToken = auth;

        this.authHeader = Helper.formatTokenHeader(this.authToken);

        this.userAPI = getUserAPI();
    }

    public UserService() {
        this.authToken = null;

        this.authHeader = null;

        this.userAPI = getUserAPI();
    }

    private UserAPI getUserAPI() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(UserAPI.class);
    }

    public Call<List<User>> getUsers() {
        return this.userAPI.getUsers(authHeader);
    }

    public Call<User> createUser(User user){
        // note: user id for the user object can be anything. default it manually to -1.
        return this.userAPI.createUser(authHeader, user);
    }

    public Call<User> getUser(int userId) {
        return this.userAPI.getUser(authHeader, userId);
    }

    public Call<User> getCurrentUser(){
        return this.userAPI.getCurrentUser(authHeader);
    }

}