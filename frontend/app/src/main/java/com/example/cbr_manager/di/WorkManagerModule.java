package com.example.cbr_manager.di;

import android.content.Context;

import androidx.work.WorkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class WorkManagerModule {

    @Singleton
    @Provides
    WorkManager provideWorkManager(@ApplicationContext Context context) {
        return WorkManager.getInstance(context);
    }
}
