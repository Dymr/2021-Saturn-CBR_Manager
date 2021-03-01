package com.example.cbr_manager;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RoomRepository {
    private ClientDBDao clientDao;
    private LiveData<List<ClientDB>> allClient;


    RoomRepository(Application application){
        RoomDB db = RoomDB.getDatabase(application);
        clientDao = db.clientDao();
        allClient = clientDao.getAll();
    }


    LiveData<List<ClientDB>> getAllClient(){
        return allClient;
    }

    void insert(ClientDB client){
        RoomDB.databaseWriteExecutor.execute(() -> {
            clientDao.insert(client);
        });
    }
}
