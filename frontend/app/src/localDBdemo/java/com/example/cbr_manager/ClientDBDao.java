package com.example.cbr_manager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ClientDBDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ClientDB client);

    @Delete
    void delete(ClientDB client);

    // Actual usage for demo, clear all data in table
    @Query("DELETE FROM client")
    void clearAll();

    @Query("SELECT * FROM client ORDER BY id ASC")
    List<ClientDB> getName();

    @Query("SELECT * FROM client WHERE id IN (:clientId)")
    List<ClientDB> getByIds(int[] clientId);

    // Cannot be used to test display because there is too many fields
    @Query("SELECT * FROM client")
    LiveData<List<ClientDB>> getAll();

    @Query("SELECT first_name FROM client WHERE :cid LIKE id")
    String searchByName(int cid);

}
