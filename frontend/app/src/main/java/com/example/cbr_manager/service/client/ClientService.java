package com.example.cbr_manager.service.client;

import com.example.cbr_manager.service.BaseService;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ClientService extends BaseService {

    private ClientAPI clientAPI;

    public ClientService(String authToken) {
        super(authToken, ClientAPI.class);
        this.clientAPI = buildRetrofitAPI();
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
        RequestBody villageNo = RequestBody.create(client.getVillageNo().toString(), MediaType.parse("text/plain"));
        RequestBody gender = RequestBody.create(client.getGender(), MediaType.parse("text/plain"));
        RequestBody age = RequestBody.create(client.getAge().toString(), MediaType.parse("text/plain"));
        RequestBody contactClient = RequestBody.create(client.getContactClient().toString(), MediaType.parse("text/plain"));
        RequestBody carePresent = RequestBody.create(client.getCarePresent(), MediaType.parse("text/plain"));
        RequestBody contactCare = RequestBody.create(client.getContactCare().toString(), MediaType.parse("text/plain"));
        RequestBody disability = RequestBody.create(client.getDisability(), MediaType.parse("text/plain"));
        RequestBody healthRisk = RequestBody.create(client.getHealthRisk().toString(), MediaType.parse("text/plain"));
        RequestBody socialRisk = RequestBody.create(client.getSocialRisk().toString(), MediaType.parse("text/plain"));
        RequestBody educationRisk = RequestBody.create(client.getEducationRisk().toString(), MediaType.parse("text/plain"));
        RequestBody educationGoal = RequestBody.create(client.getEducationGoal().toString(), MediaType.parse("text/plain"));
        RequestBody socialGoal = RequestBody.create(client.getSocialGoal().toString(), MediaType.parse("text/plain"));
        RequestBody healthGoal = RequestBody.create(client.getHealthGoal().toString(), MediaType.parse("text/plain"));


        return this.clientAPI.createClientManual(
                authHeader,
                firstName,
                lastName,
                location,
                consent,
                villageNo,
                gender,
                age,
                contactClient,
                carePresent,
                contactCare,
                disability,
                healthRisk,
                socialRisk,
                educationRisk,
                educationGoal,
                socialGoal,
                healthGoal
        );
    }

    public Call<Client> createClient(Client client) {
        return this.clientAPI.createClient(authHeader, client);
    }

    public Call<ResponseBody> uploadClientPhoto(File file, int clientId) {
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        return this.clientAPI.uploadPhoto(authHeader, clientId, body);
    }

    public Call<List<ClientHistoryRecord>> getClientHistoryRecords(int clientId) {
        return this.clientAPI.getClientHistoryRecords(authHeader, clientId);
    }

    public Call<List<ClientHistoryRecord>> getClientHistoryRecords(int clientId, String filterByField) {
        return this.clientAPI.getClientHistoryRecords(authHeader, clientId, filterByField);
    }

}