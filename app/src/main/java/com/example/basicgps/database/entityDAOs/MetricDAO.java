package com.example.basicgps.database.entityDAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.basicgps.database.entities.Metric;

import java.util.List;

@Dao
public interface MetricDAO {
    @Insert
    void insert(Metric metricToSave);

    // Update works by checking the primary key. If no PK is available, nothing will happen
    @Update
    void updateMetric(Metric metricToUpdate);

    // Delete works by checking the primary key. If no PK is available, nothing will happen
    @Delete
    void deleteMetric(Metric metricToDelete);

    @Query("SELECT * FROM metrics")
    List<Metric> getAllMetrics();

    @Query("SELECT COUNT(*) FROM metrics")
    int documentCount();

    @Query("DELETE FROM metrics")
    void clearTable();
}
