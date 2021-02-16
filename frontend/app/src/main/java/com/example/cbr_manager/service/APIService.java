package com.example.cbr_manager.service;

import com.example.cbr_manager.service.auth.AuthService;
import com.example.cbr_manager.service.auth.AuthResponse;
import com.example.cbr_manager.service.auth.LoginUserPass;
import com.example.cbr_manager.service.client.ClientService;
import com.example.cbr_manager.service.user.User;
import com.example.cbr_manager.service.user.UserService;
import com.example.cbr_manager.service.visit.VisitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class APIService {

    private static APIService INSTANCE = null;
    public AuthService authService;
    public ClientService clientService;
    public UserService userService;
    public VisitService visitService;

    public User currentUser;

    private APIService() {
    }

    public static APIService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new APIService();
        }
        return (INSTANCE);
    }

    public void authenticate(LoginUserPass loginUserPass, Callback<AuthResponse> listener) {
        authService = new AuthService(loginUserPass);
        authService.fetchAuthToken().enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    authService.setAuthToken(authResponse);
                    initializeServices(authResponse);

                    setCurrentUser(authResponse.user);
                }
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Todo: not sure proper method of error handling

                listener.onFailure(call, t);
            }
        });
    }

    public void initializeServices(AuthResponse token) {
        this.clientService = new ClientService(token);
        this.userService = new UserService(token);
        this.visitService = new VisitService(token);
    }

    public boolean isAuthenticated() {
        // Todo needs a better check, maybe a specific endpoint to check validity of auth token
        return authService.getAuthToken() != null;
    }

    public void testInitializeUserService() {
        this.userService = new UserService();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}
