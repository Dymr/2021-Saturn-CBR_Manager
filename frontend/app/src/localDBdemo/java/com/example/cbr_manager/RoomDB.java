package com.example.cbr_manager;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ClientDB.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    public abstract ClientDBDao clientDao();
}
