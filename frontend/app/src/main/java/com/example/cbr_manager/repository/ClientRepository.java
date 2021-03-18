package com.example.cbr_manager.repository;

import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.client.ClientAPI;
import com.example.cbr_manager.service.client.ClientDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ClientRepository {

    private ClientAPI clientAPI;

    private ClientDao clientDao;

    private String authHeader;

    @Inject
    ClientRepository(ClientDao clientDao, ClientAPI clientAPI, String authHeader) {
        this.clientDao = clientDao;
        this.clientAPI = clientAPI;
        this.authHeader = authHeader;
    }


    public Observable<List<Client>> getClients() {
        return null;
    }

    public Single<Client> getClient(int clientID) {
        return null;
    }

    public Single<Client> modifyClient(Client client) {
        return null;
    }

    public Single<Client> createClient(Client client) {
        return null;
    }

}