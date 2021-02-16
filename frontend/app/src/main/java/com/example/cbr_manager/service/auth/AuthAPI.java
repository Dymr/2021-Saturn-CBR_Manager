package com.example.cbr_manager.service.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*
Define our API request endpoints here
 */
public interface AuthAPI {
    @POST("api/token-auth")
    Call<AuthResponse> getToken(@Body LoginUserPass loginUserPass);
}
