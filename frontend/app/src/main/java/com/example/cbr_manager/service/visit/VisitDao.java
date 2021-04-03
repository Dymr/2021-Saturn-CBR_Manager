package com.example.cbr_manager.service.visit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Visit visit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> SingleInsert(Visit visit);

    @Delete
    void delete(Visit visit);

    @Update
    void update(Visit visit);

    @Query("DELETE FROM visit")
    void clearAll();

    @Query("SELECT * FROM visit")
    Single<List<Visit>> getVisits();

    @Query("SELECT * FROM visit WHERE visit_id = :id")
    Single<Visit> getVisit(int id);
}
