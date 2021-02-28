package com.example.cbr_manager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClientDBDao {
    @Insert
    void insert(ClientDB client);

    @Delete
    void delete(ClientDB client);

    // Actual usage for demo, clear all data in table
    @Query("DELETE FROM client")
    void clearAll();

    @Query("SELECT first_name FROM client")
    List<String> getName();

    // Cannot be used to test display because there is too many fields
    @Query("SELECT * FROM client")
    List<ClientDB> getAll();


}
