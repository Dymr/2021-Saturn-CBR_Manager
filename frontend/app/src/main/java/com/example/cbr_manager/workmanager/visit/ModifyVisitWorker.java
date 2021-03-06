package com.example.cbr_manager.workmanager.visit;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Data;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.service.visit.VisitAPI;
import com.example.cbr_manager.service.visit.VisitDao;
import com.example.cbr_manager.utils.Helper;

import org.jetbrains.annotations.NotNull;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.Single;

@HiltWorker
public class ModifyVisitWorker extends RxWorker {

    public static final String KEY_AUTH_HEADER = "KEY_AUTH_HEADER";
    public static final String KEY_VISIT_OBJ_ID = "KEY_VISIT_OBJ_ID";
    private static final String TAG = ModifyVisitWorker.class.getSimpleName();
    private final VisitAPI visitAPI;
    private final VisitDao visitDao;

    @AssistedInject
    public ModifyVisitWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters params, VisitAPI visitAPI, VisitDao visitDao) {
        super(context, params);

        this.visitAPI = visitAPI;
        this.visitDao = visitDao;
    }

    public static Data buildInputData(String authHeader, int visitId) {
        Data.Builder builder = new Data.Builder();
        builder.putString(ModifyVisitWorker.KEY_AUTH_HEADER, authHeader);
        builder.putInt(ModifyVisitWorker.KEY_VISIT_OBJ_ID, visitId);
        return builder.build();
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        String authHeader = getInputData().getString(KEY_AUTH_HEADER);
        int visitObjId = getInputData().getInt(KEY_VISIT_OBJ_ID, -1);

        return visitDao.getVisitAsSingle(visitObjId)
                .flatMap(localVisit -> visitAPI.modifyVisitAsSingle(authHeader, localVisit.getServerId(), localVisit)
                        .doOnSuccess(serverVisit -> updateDBEntry(localVisit, serverVisit)))
                .map(visitSingle -> {
                    Log.d(TAG, "modified Visit: " + visitSingle.getId());
                    return Result.success();
                })
                .onErrorReturn(this::handleReturnResult);
    }

    private void updateDBEntry(Visit localVisit, Visit serverVisit) {
        Integer localId = localVisit.getId();
        serverVisit.setId(localId);
        visitDao.update(serverVisit);
    }

    @NotNull
    private Result handleReturnResult(Throwable throwable) {
        if (Helper.isConnectionError(throwable)) {
            return Result.retry();
        }
        return Result.failure();
    }
}
