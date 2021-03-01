package com.example.cbr_manager;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClientViewModel extends AndroidViewModel {

    private RoomRepository repo;

    private final LiveData<List<ClientDB>> allClient;

    public ClientViewModel (Application application){
        super(application);
        repo = new RoomRepository(application);
        allClient = repo.getAllClient();
    }

    LiveData<List<ClientDB>> getAllClient() {return allClient;}

    public void insert(ClientDB client) { repo.insert(client); }
}
