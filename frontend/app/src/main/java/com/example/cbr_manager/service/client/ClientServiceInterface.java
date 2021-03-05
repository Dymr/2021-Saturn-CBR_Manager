package com.example.cbr_manager.service.client;

import com.example.cbr_manager.service.CBRCall;
import com.example.cbr_manager.service.CBRCallback;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.File;
import java.util.List;

public interface ClientServiceInterface {
//    CBRCall<List<Client>> getClients();
//
//    CBRCall<Client> getClient(int clientId);
//
//    CBRCall<Client> createClient(Client client);
//
//    CBRCall<JSONPObject> uploadClientPhoto(File file, int clientId);
    void testGetClientMethod(int clientId, CBRCallback<Client> clientCBRCall);
}
