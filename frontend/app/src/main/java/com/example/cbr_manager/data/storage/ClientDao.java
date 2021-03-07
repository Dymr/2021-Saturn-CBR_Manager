package com.example.cbr_manager.data.storage;

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
    void insert(Client...clients);

    // This function delete every client where the @PrimaryKey matches, in this case it is id
    // Thus it is very important that @PrimaryKey field must be unique
    @Delete
    int delete(Client client);

    // This function update every client where the @PrimaryKey matches, in this case it is id
    @Update
    int update(Client client);

    // Read all clients in client table
    @Query("SELECT * FROM client")
    List<Client> getClients();

    // Read client by id
    @Query("SELECT * FROM client WHERE id LIKE :clientId")
    Client getClient(int clientId);

}
