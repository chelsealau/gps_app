package com.example.basicgps.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.basicgps.database.entities.*;
import com.example.basicgps.database.entityDAOs.*;

@Database(entities = {Metric.class, Score.class}, version = 1)
public abstract class GPSDatabase extends RoomDatabase {
    public abstract MetricDAO metricDAO();
    public abstract ScoreDAO scoreDAO();

    private static volatile GPSDatabase instance;
    private GPSDatabase() {};

    public static GPSDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (GPSDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), GPSDatabase.class, "gps_database").build();
                }
            }
        }
        return instance;
    }
}
