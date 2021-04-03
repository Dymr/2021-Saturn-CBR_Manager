package com.example.cbr_manager.service;

import com.example.cbr_manager.service.alert.AlertService;
import com.example.cbr_manager.service.auth.AuthDetail;
import com.example.cbr_manager.service.auth.AuthService;
import com.example.cbr_manager.service.auth.LoginUserPass;
import com.example.cbr_manager.service.baseline_survey.BaselineSurveyService;
import com.example.cbr_manager.service.client.ClientService;
import com.example.cbr_manager.service.goal.GoalService;
import com.example.cbr_manager.service.referral.ReferralService;
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
    public AlertService alertService;
    public ReferralService referralService;
    public BaselineSurveyService baselineSurveyService;
    private String token;
    public GoalService goalService;

    private APIService() {
    }

    public static APIService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new APIService();
        }
        return (INSTANCE);
    }

    public void initializeServices(String token) {
        this.token = token;
        this.clientService = new ClientService(token);
        this.userService = new UserService(token);
        this.visitService = new VisitService(token);
        this.alertService = new AlertService(token);
        this.referralService = initializeReferralService(token);
        this.baselineSurveyService = new BaselineSurveyService(token);
        this.goalService = new GoalService(token);
    }

    @Deprecated // use AuthViewModel!
    public boolean isAuthenticated() {
        return token != null && !token.equals("");
    }

    private ReferralService initializeReferralService(String authResponse) {
        return new ReferralService(authResponse);
    }

    public ReferralService getReferralService() {
        return referralService;
    }
}
