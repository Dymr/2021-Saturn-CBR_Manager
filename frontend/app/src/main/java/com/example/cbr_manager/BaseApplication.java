package com.example.cbr_manager;

import androidx.hilt.work.HiltWorkerFactory;
import androidx.multidex.MultiDexApplication;
import androidx.work.Configuration;

import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public final class BaseApplication extends MultiDexApplication implements Configuration.Provider {

    @Inject
    HiltWorkerFactory workerFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }


    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
    }
}
