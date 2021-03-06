package com.example.cbr_manager.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.cbr_manager.service.goal.Goal;
import com.example.cbr_manager.service.goal.GoalAPI;
import com.example.cbr_manager.service.goal.GoalDao;
import com.example.cbr_manager.workmanager.goal.CreateGoalWorker;
import com.example.cbr_manager.workmanager.goal.ModifyGoalWorker;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GoalRepository {

    private final static String TAG = GoalRepository.class.getSimpleName();
    private final GoalAPI goalAPI;
    private final GoalDao goalDao;
    private final String authHeader;
    private WorkManager workManager;

    @Inject
    GoalRepository(GoalAPI goalAPI, GoalDao goalDao, String authHeader, WorkManager workManager) {
        this.goalAPI = goalAPI;
        this.goalDao = goalDao;
        this.authHeader = authHeader;
        this.workManager = workManager;
    }

    public LiveData<List<Goal>> getGoalsAsLiveData() {
        fetchGoalsAsync();
        return goalDao.getGoalsAsLiveData();
    }

    private void fetchGoalsAsync() {
        goalAPI.getGoalsAsSingle(authHeader)
                .doOnSuccess(goals -> {
                            for (Goal goal :
                                    goals) {
                                insertGoalToLocalDB(goal);
                            }
                        }
                )
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<Goal>>() {
                    @Override
                    public void onSuccess(@NonNull List<Goal> goals) {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }

    private void insertGoalToLocalDB(Goal goal) {
        Goal localGoal = goalDao.getGoalByServerId(goal.getServerId());
        if (localGoal != null) {
            goal.setId(localGoal.getId());
            goalDao.update(goal);
        } else {
            goalDao.insert(goal);
        }
    }

    public LiveData<Goal> getGoalAsLiveData(int id) {
        return goalDao.getGoalAsLiveData(id);
    }

    public Single<Goal> createGoal(Goal goal) {
        return Single.fromCallable(() -> goalDao.insert(goal))
                .map(aLong -> {
                    goal.setId(aLong.intValue());
                    return goal;
                })
                .doOnSuccess(this::enqueueCreateGoalWorker)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateGoal(Goal goal) {
        return Completable.fromAction(() -> goalDao.update(goal))
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> enqueueModifyGoalWorker(goal))
                .observeOn(AndroidSchedulers.mainThread());
    }

    private UUID enqueueCreateGoalWorker(Goal goal) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest createGoalRequest =
                new OneTimeWorkRequest.Builder(CreateGoalWorker.class)
                        .setConstraints(constraints)
                        .setInputData(CreateGoalWorker.buildInputData(authHeader, goal.getId()))
                        .build();
        workManager.enqueue(createGoalRequest);
        return createGoalRequest.getId();
    }

    private void enqueueModifyGoalWorker(Goal goal) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest createGoalRequest =
                new OneTimeWorkRequest.Builder(ModifyGoalWorker.class)
                        .setConstraints(constraints)
                        .setInputData(ModifyGoalWorker.buildInputData(authHeader, goal.getId()))
                        .build();
        workManager.enqueue(createGoalRequest);
    }

    public Completable modifyGoal(Goal goal) {
        return Completable.fromAction(() -> goalDao.update(goal))
                .doOnComplete(() -> enqueueModifyGoal(goal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void enqueueModifyGoal(Goal goal) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest modifyGoalRequest =
                new OneTimeWorkRequest.Builder(ModifyGoalWorker.class)
                        .setConstraints(constraints)
                        .setInputData(ModifyGoalWorker.buildInputData(authHeader, goal.getId()))
                        .build();
        workManager.enqueue(modifyGoalRequest);
    }

}
