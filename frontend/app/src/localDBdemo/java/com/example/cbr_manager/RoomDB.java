package com.example.cbr_manager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ClientDB.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    public abstract ClientDBDao clientDao();

    private static volatile RoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RoomDB getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (RoomDB.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "room_database").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
