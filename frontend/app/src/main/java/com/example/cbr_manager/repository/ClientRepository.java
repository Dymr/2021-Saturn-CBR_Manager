package com.example.cbr_manager.repository;

import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.client.ClientDao;
import com.example.cbr_manager.service.client.api.ClientAPI;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

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
        return clientAPI.getClients(authHeader)
                .subscribeOn(Schedulers.io())
                .doOnNext(clients -> {
                    System.out.println(clients);
                });
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