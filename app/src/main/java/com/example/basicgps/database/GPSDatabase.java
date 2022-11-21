package com.example.basicgps.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.basicgps.database.entities.*;
import com.example.basicgps.database.entityDAOs.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Metric.class, Score.class}, version = 1)
public abstract class GPSDatabase extends RoomDatabase {

    public abstract MetricDAO metricDAO();
    public abstract ScoreDAO scoreDAO();

    private static volatile GPSDatabase INSTANCE;

    private static final int NUM_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_THREADS);

    public static GPSDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = createDatabase(context);
        }
        return INSTANCE;
    }

    private static GPSDatabase createDatabase(final Context context) {
        return Room.databaseBuilder(context, GPSDatabase.class, "gps_database").build();
    }
}
