package com.example.cbr_manager.service.client;

import com.example.cbr_manager.BuildConfig;
import com.example.cbr_manager.helper.Helper;
import com.example.cbr_manager.service.auth.AuthResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientService {

    private static final String BASE_URL = BuildConfig.API_URL;

    private final AuthResponse authToken;

    private final String authHeader;

    private ClientAPI clientAPI;

    public ClientService(AuthResponse auth) {
        this.authToken = auth;

        this.authHeader = Helper.formatTokenHeader(this.authToken);

        this.clientAPI = getClientAPI();
    }

    private ClientAPI getClientAPI() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(ClientAPI.class);
    }

    public Call<List<Client>> getClients() {
        return this.clientAPI.getClients(authHeader);
    }

    public Call<Client> modifyClient(Client client) {
        return this.clientAPI.modifyClient(authHeader, client.getId(), client);
    }

    public Call<Client> getClient(int clientId) {
        return this.clientAPI.getClient(authHeader, clientId);
    }

    public Call<Client> createClientManual(Client client) {
        // TODO: Add more client fields when finalized. Restricted to manual fields for now.
        // Need to manually build client request object because of its image field

        RequestBody firstName = RequestBody.create(client.getFirstName(), MediaType.parse("text/plain"));
        RequestBody lastName = RequestBody.create(client.getLastName(), MediaType.parse("text/plain"));
        RequestBody location = RequestBody.create(client.getLocation(), MediaType.parse("text/plain"));
        RequestBody consent = RequestBody.create(client.getConsent(), MediaType.parse("text/plain"));
        RequestBody gender = RequestBody.create(client.getGender(), MediaType.parse("text/plain"));
        RequestBody carePresent = RequestBody.create(client.getCarePresent(), MediaType.parse("text/plain"));
        RequestBody disability = RequestBody.create(client.getDisability(), MediaType.parse("text/plain"));
        RequestBody healthRisk = RequestBody.create(client.getHealthRisk().toString(), MediaType.parse("text/plain"));
        RequestBody socialRisk = RequestBody.create(client.getSocialRisk().toString(), MediaType.parse("text/plain"));
        RequestBody educationRisk = RequestBody.create(client.getEducationRisk().toString(), MediaType.parse("text/plain"));

        return this.clientAPI.createClientManual(
                authHeader,
                firstName,
                lastName,
                location,
                consent,
                gender,
                carePresent,
                disability,
                healthRisk,
                socialRisk,
                educationRisk
        );
    }

    public Call<Client> createClient(Client client){
        return this.clientAPI.createClient(authHeader, client);
    }

}