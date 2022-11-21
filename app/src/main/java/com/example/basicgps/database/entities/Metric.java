package com.example.basicgps.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.basicgps.database.Units;

@Entity(tableName = "metrics")
public class Metric {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public double latitude;
    public double longitude;
    public double altitude;
    @ColumnInfo(name = "altitude_units")
    public Units.Distance altitudeUnits;

    public double speed;
    @ColumnInfo(name = "speed_units")
    public Units.Speed speedUnits;

    @ColumnInfo(name = "distance_traveled")
    public double distanceTraveled;
    @ColumnInfo(name = "dist_traveled_units")
    public Units.Distance distTraveledUnits;


    @ColumnInfo(name = "moving_time")
    public long movingTime;
    @ColumnInfo(name = "moving_time_units")
    public Units.Time movingTimeUnits;


    public Metric(double latitude, double longitude, double altitude, Units.Distance altitudeUnits, double speed, Units.Speed speedUnits, double distanceTraveled, Units.Distance distTraveledUnits, long movingTime, Units.Time movingTimeUnits) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.altitudeUnits = altitudeUnits;
        this.speed = speed;
        this.speedUnits = speedUnits;
        this.distanceTraveled = distanceTraveled;
        this.distTraveledUnits = distTraveledUnits;
        this.movingTime = movingTime;
        this.movingTimeUnits = movingTimeUnits;
    }

    public double getAltitude(){
        return altitude;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getSpeed(){
        return speed;
    }
    public long getMovingTime(){
        return movingTime;
    }
    public double getDistanceTraveled(){
        return distanceTraveled;
    }
}
