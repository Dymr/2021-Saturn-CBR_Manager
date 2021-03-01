package com.example.cbr_manager;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DBExecutor {

    private static DBExecutor mInstance;
    private Executor diskIO;
    private Executor UIThread;

    private DBExecutor(Executor diskIO, Executor UIThread){
        this.diskIO = diskIO;
        this.UIThread = UIThread;
    }

    public static DBExecutor getInstance(){
        if(mInstance==null){
            mInstance = new DBExecutor(Executors.newSingleThreadExecutor(), ContextCompat.getMainExecutor(MyApplication.getAppContext()));
        }
        return mInstance;
    }

    public Executor getDiskIO(){
        return diskIO;
    }

    public Executor getUIThread(){
        return UIThread;
    }

}
