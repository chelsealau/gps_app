package com.example.basicgps.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scores")
public class Score {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "max_speed")
    public double maxSpeed;
    @ColumnInfo(name = "min_speed")
    public double minSpeed;

    @ColumnInfo(name = "max_distance")
    public double maxDistance;
    @ColumnInfo(name = "min_distance")
    public double minDistance;

    @ColumnInfo(name = "max_time")
    public long maxTime;
    @ColumnInfo(name = "min_time")
    public long minTime;

    public Score(double maxSpeed, double minSpeed, double maxDistance, double minDistance, long maxTime, long minTime) {
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.maxDistance = maxDistance;
        this.minDistance = minDistance;
        this.maxTime = maxTime;
        this.minTime = minTime;
    }
}
