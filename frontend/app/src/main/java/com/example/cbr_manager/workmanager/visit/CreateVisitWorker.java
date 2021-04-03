package com.example.cbr_manager.workmanager.visit;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Data;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.service.visit.VisitAPI;
import com.example.cbr_manager.service.visit.VisitDao;

import org.jetbrains.annotations.NotNull;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.Single;

@HiltWorker
public class CreateVisitWorker extends RxWorker {

    public static final String KEY_AUTH_HEADER = "KEY_AUTH_HEADER";
    public static final String KEY_VISIT_OBJ_ID = "KEY_VISIT_OBJ_ID";
    private static final String TAG = CreateVisitWorker.class.getSimpleName();
    private final VisitAPI visitAPI;
    private final VisitDao visitDao;

    @AssistedInject
    public CreateVisitWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters params, VisitAPI visitAPI, VisitDao visitDao) {
        super(context, params);

        this.visitAPI = visitAPI;
        this.visitDao = visitDao;
    }

    public static Data buildInputData(String authHeader, int visitId){
        Data.Builder builder = new Data.Builder();
        builder.putString(CreateVisitWorker.KEY_AUTH_HEADER, authHeader);
        builder.putInt(CreateVisitWorker.KEY_VISIT_OBJ_ID, visitId);
        return builder.build();
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        String authHeader = getInputData().getString(KEY_AUTH_HEADER);
        int visitObjId = getInputData().getInt(KEY_VISIT_OBJ_ID, -1);

        return visitDao.getVisit(visitObjId)
                .flatMap(visit -> visitAPI.createVisitObs(authHeader, visit)
                        .doOnSuccess(visitResult -> onSuccessfulCreateVisit(visit, visitResult)))
                .map(visitSingle -> Result.success())
                .onErrorReturn(this::handleReturnResult);
    }

    private void onSuccessfulCreateVisit(Visit visit, Visit visitResult) {
        visitDao.delete(visit);
        visit.setId(visitResult.getId());
        visitDao.insert(visit);
    }

    @NotNull
    private Result handleReturnResult(Throwable throwable) {
        if (throwable instanceof ConnectException || throwable instanceof SocketTimeoutException) {
            return Result.retry();
        }
        return Result.failure();
    }
}
