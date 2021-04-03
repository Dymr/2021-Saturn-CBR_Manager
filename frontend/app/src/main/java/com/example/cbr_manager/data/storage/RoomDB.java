package com.example.cbr_manager.data.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cbr_manager.service.auth.AuthDetail;
import com.example.cbr_manager.service.auth.AuthDetailDao;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.client.ClientDao;
import com.example.cbr_manager.service.goal.Goal;
import com.example.cbr_manager.service.goal.GoalDao;
import com.example.cbr_manager.service.referral.Referral;
import com.example.cbr_manager.service.referral.ReferralDao;
import com.example.cbr_manager.service.sync.Status;
import com.example.cbr_manager.service.sync.StatusDao;
import com.example.cbr_manager.service.user.User;
import com.example.cbr_manager.service.user.UserDao;
import com.example.cbr_manager.service.visit.Visit;
import com.example.cbr_manager.service.visit.VisitDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Client.class, Visit.class, Status.class, User.class, AuthDetail.class, Referral.class, Goal.class}, version = 1, exportSchema = false)
@TypeConverters({TimeStampConverter.class})
public abstract class RoomDB extends RoomDatabase {

    public abstract ClientDao clientDao();
    public abstract VisitDao visitDao();
    public abstract GoalDao goalDao();
    public abstract StatusDao statusDao();
    public abstract UserDao userDao();
    public abstract AuthDetailDao authDetailDao();
    public abstract ReferralDao referralDao();

    private static volatile RoomDB Instance;

    public static String DATABASE_NAME = "app_database";

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDB getDatabase(final Context context){
        if (Instance == null){
            synchronized (RoomDB.class){
                if(Instance == null){
                    Instance = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, DATABASE_NAME).build();
                }
            }
        }
        return Instance;
    }
}
