package com.example.cbr_manager.data.storage;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.cbr_manager.data.model.ClientValues;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.auth.AuthService;
import com.example.cbr_manager.service.client.Client;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientSyncAdapter extends AbstractThreadedSyncAdapter{

    ContentResolver contentResolver;
    APIService apiService;

    public ClientSyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
        apiService = APIService.getInstance();
    }

    public ClientSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs){
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("cbr_manager", "onPerformSync for account[" + account.name + "]");
        try{
            // Get auth token for current account
            String authToken = apiService.authService.getAuthToken().token;

            // Get Client from remote server
            List<Client> clientRemote = new ArrayList<>();
            fetchClientsToList(clientRemote);

            // Get Client from local database
            List<Client> clientLocal = new ArrayList<>();
            Cursor clientCursor = provider.query(ClientContract.BASE_CONTENT_URI, null, null, null, null);
            if(clientCursor != null){
                clientCursor.moveToFirst();
                Client client = new Client();
                while(clientCursor.moveToNext()){
                    clientLocal.add(client.fromCursor(clientCursor));
                }
                clientCursor.close();
            }

            // TODO: See what is missing on local

            // TODO: See what is missing on remote

            // TODO: Update local Clients

            // TODO: Update remote Clients ::In testing
            for(int i=0; i<clientLocal.size(); i++){
                Client newClient = clientLocal.get(i);
                apiService.clientService.createClient(newClient);
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }


    // Function copied from ClientListFragment
    public void fetchClientsToList(List<Client> clientList) {
        if (apiService.isAuthenticated()) {
            apiService.clientService.getClients().enqueue(new Callback<List<Client>>() {
                @Override
                public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                    if (response.isSuccessful()) {
                        List<Client> clients = response.body();
                        clientList.addAll(clients);
                    }
                }

                @Override
                public void onFailure(Call<List<Client>> call, Throwable t) {

                }
            });
        }

    }





}


/* Basic Content Resolver to work with provider and syncadapter, we need to put these code in for activity that accesses the client database

    private ContentResolver contentResolver;
    contentResolver = getContext().getContentResolver();

    @insert:
    ContentValues clientContent = ClientValues.setClient(client);
    Uri insertUri = contentResolver.insert(ClientContract.CLIENT_URI, clientContent);

    @query:
    Cursor clientCursor = contentResolver.query(ClientContract.CLIENT_URI, null, null, null, null);
    List<Client> clientLocal = new ArrayList<>();
    if(clientCursor != null){
        clientCursor.moveToFirst();
        Client client = new Client();
        while(clientCursor.moveToNext()){
            clientLocal.add(client.fromCursor(clientCursor));
        }
        clientCursor.close();
    }

    @update with uri from insert:
    ContentValues newClientContent = ClientValues.setClient(new_client);
    int count_updated = contentResolver.update(insertUri, newClientContent, null, null);

    @delete with uri from insert:
    int count_deleted = contentResolver.delete(insertUri,null, null);

    @Running Sync on button click:
    Bundle settingBundle = new Bundle();
    settingBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
    settingBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
    ContentResolver.requestSync(mAccount, ClientContract.CONTENT_AUTHORITY, settingBundle);
 */