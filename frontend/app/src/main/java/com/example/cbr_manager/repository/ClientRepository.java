package com.example.cbr_manager.repository;

import com.example.cbr_manager.data.storage.RoomDB;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.client.ClientAPI;
import com.example.cbr_manager.service.client.ClientDao;
import com.example.cbr_manager.utils.Helper;

import org.threeten.bp.ZonedDateTime;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ClientRepository {

    private ClientDao clientDao;
    private ClientAPI clientAPI;
    private String authHeader;

    @Inject
    ClientRepository(ClientDao clientDao, ClientAPI clientAPI, String authHeader) {
        this.clientAPI = clientAPI;
        this.clientDao = clientDao;
        this.authHeader = authHeader;
    }

    public Observable<Client> getAllClient(){
        return clientAPI.getClientsObs(authHeader)
                .flatMapIterable(clients -> clients)
                .doOnNext(client -> clientDao.insert(client))
                .onErrorResumeNext(this::getLocalClient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private ObservableSource<? extends Client> getLocalClient(Throwable throwable) {
        return clientDao.getClientsObs().toObservable().flatMap(Observable::fromIterable);
    }

    public Single<Client> getClient(int id) {
        return clientAPI.getClientSingle(authHeader, id)
                .onErrorResumeNext(throwable -> clientDao.getClientSingle(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Client> insert(Client client) {
        return clientAPI.createClientSingle(authHeader, client)
                .onErrorResumeNext(throwable -> handleOfflineCreateClient(client, throwable))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private SingleSource<? extends Client> handleOfflineCreateClient(Client client, Throwable throwable) {
        if(throwable instanceof SocketTimeoutException) {
            long id = clientDao.insert(client);
            client.setId((int)id);
            return Single.just(client);
        }
        return Single.error(throwable);
    }

    public Single<ResponseBody> uploadPhoto(File file, int clientId) {
        //TODO: Handle image caching offline on error (picasso?)
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        return clientAPI.uploadPhotoSingle(authHeader, clientId, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Client> update(Client client) {
        return clientAPI.modifyClientSingle(authHeader, client.getId(), client)
                .onErrorResumeNext(throwable -> handleOfflineModifyClient(client, throwable))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private SingleSource<? extends Client> handleOfflineModifyClient(Client client, Throwable throwable) {
        clientDao.update(client);
        return Single.just(client);
    }

    // Below are functions for synchronizing data from local and remote server

    private void uploadNewClient(Client client) {
        if (client.isNewClient()) {
            clientAPI.createClient(authHeader, client);
            File file = new File(client.getPhotoURL());
            RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            clientAPI.uploadPhoto(authHeader, client.getId(), body);
            clientDao.delete(client);
        }

    }

    private boolean matchID(Client client1, Client client2) {
        return client1.getId() == client2.getId();
    }

    private boolean compareZoneDateTime(String date1, String date2) {
        ZonedDateTime zdt1 = Helper.parseUTCDateTime(date1);
        ZonedDateTime zdt2 = Helper.parseUTCDateTime(date2);
        int result = zdt1.compareTo(zdt2);
        return result > 0;
    }

    private void updateLocal(Client serverClient) {
        if(clientDao.ifClientExist(serverClient.getId())) {
            Client localClient = clientDao.getClient(serverClient.getId());
            if(compareZoneDateTime(localClient.getUpdatedAt(), serverClient.getUpdatedAt())) {
                clientAPI.modifyClient(authHeader, localClient.getId(), localClient);
                File file = new File(localClient.getPhotoURL());
                RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
                MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
                clientAPI.uploadPhoto(authHeader, localClient.getId(), body);
            }
        }
        else {
            return;
        }

    }


    private void setNewClient(Client localClient) {
        localClient.setNewClient(false);
        clientDao.update(localClient);

    }


    // Sync function concatenating multiple observable
    public Completable sync() {
        Observable<Client> uploadNew = clientDao.getClientsObs()
                .toObservable().flatMap(Observable::fromIterable)
                .doOnNext(this::uploadNewClient)
                .doOnComplete(() -> {});

        Observable<Client> updateLocal = clientAPI.getClientsObs(authHeader)
                .flatMap(Observable::fromIterable)
                .doOnNext(this::updateLocal)
                .doOnComplete(() -> {clientDao.clearAll();});

        Observable<Client> downloadLocal = clientAPI.getClientsObs(authHeader)
                .flatMap(Observable::fromIterable)
                .doOnNext(client -> clientDao.insert(client))
                .doOnComplete(() -> {});

        Observable<Client> setNew = clientDao.getClientsObs()
                .toObservable().flatMap(Observable::fromIterable)
                .doOnNext(this::setNewClient)
                .doOnComplete(() -> {});

        return Completable.fromObservable(uploadNew)
                .andThen(Completable.fromObservable(updateLocal))
                .andThen(Completable.fromObservable(downloadLocal))
                .andThen(Completable.fromObservable(setNew))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
















}
