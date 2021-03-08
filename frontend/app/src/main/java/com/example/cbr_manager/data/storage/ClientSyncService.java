package com.example.cbr_manager.data.storage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ClientSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static ClientSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate(){
        synchronized (sSyncAdapterLock){
            if(sSyncAdapter == null){
                sSyncAdapter = new ClientSyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
