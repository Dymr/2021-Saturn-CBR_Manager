package com.example.cbr_manager.repository;

import com.example.cbr_manager.data.storage.RoomDB;
import com.example.cbr_manager.service.sync.Status;
import com.example.cbr_manager.service.sync.StatusAPI;
import com.example.cbr_manager.service.sync.StatusDao;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class StatusRepository {

    private StatusAPI statusAPI;

    private StatusDao statusDao;

    private String authHeader;

    @Inject
    StatusRepository(StatusDao statusDao, StatusAPI statusAPI, String authHeader) {
        this.statusDao = statusDao;
        this.statusAPI = statusAPI;
        this.authHeader = authHeader;
    }

    public Single<Status> getStatus() {
        return statusAPI.getStatus(authHeader)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(status -> statusDao.insert(status))
                .onErrorResumeNext((e) -> statusDao.getStatus());
    }

    public void insert(Status status) {
        RoomDB.databaseWriteExecutor.execute(()->{
            statusDao.insert(status);
        });
    }

    public void update(Status status) {
        RoomDB.databaseWriteExecutor.execute(()->{
            statusDao.update(status);
        });
    }
}
