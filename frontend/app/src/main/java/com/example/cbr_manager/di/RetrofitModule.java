package com.example.cbr_manager.di;

import com.example.cbr_manager.BuildConfig;
import com.example.cbr_manager.service.auth.AuthAPI;
import com.example.cbr_manager.service.client.ClientAPI;
import com.example.cbr_manager.service.referral.ReferralAPI;
import com.example.cbr_manager.service.sync.StatusAPI;
import com.example.cbr_manager.service.user.UserAPI;
import com.example.cbr_manager.service.visit.VisitAPI;
import com.example.cbr_manager.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    @Singleton
    @Provides
    Gson provideGSonBuilder() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Singleton
    @Provides
    OkHttpClient provideHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    StatusAPI provideStatusAPI(Retrofit retrofit) {
        return retrofit.create(StatusAPI.class);
    }

    @Singleton
    @Provides
    AuthAPI provideAuthAPI(Retrofit retrofit) {
        return retrofit.create(AuthAPI.class);
    }

    @Singleton
    @Provides
    UserAPI provideUserAPI(Retrofit retrofit) {
        return retrofit.create(UserAPI.class);
    }

    @Singleton
    @Provides
    ClientAPI provideClientAPI(Retrofit retrofit) {
        return retrofit.create(ClientAPI.class);
    }

    @Singleton
    @Provides
    ReferralAPI provideReferralAPI(Retrofit retrofit) {
        return retrofit.create(ReferralAPI.class);
    }

    @Singleton
    @Provides
    VisitAPI provideVisitAPI(Retrofit retrofit) {
        return retrofit.create(VisitAPI.class);
    }

    @Provides
    String provideAuthHeader(SharedPreferencesHelper helper) {
        return Helper.formatTokenHeader(helper.getAuthToken());
    }
}
