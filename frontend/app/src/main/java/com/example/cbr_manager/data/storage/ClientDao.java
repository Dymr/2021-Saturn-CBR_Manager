package com.example.cbr_manager.data.storage;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cbr_manager.service.client.Client;

import java.util.List;

@Dao
public interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Client client);

    // This function delete every client where the id matches
    // Return numbers of client deleted
    @Query("DELETE FROM client WHERE id = :id")
    int delete(long id);

    // This function update every client where the @PrimaryKey matches, in this case it is id
    // Likewise with @Delete, also return the number of clients updated
    @Update
    int update(Client client);

    // Read all clients in client table, return a cursor to client table object inside database
    @Query("SELECT * FROM client")
    Cursor getClients();

    // Read client by id
    @Query("SELECT * FROM client WHERE id = :clientId")
    Client getClient(int clientId);

}
