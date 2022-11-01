// REFERENCE: https://www.youtube.com/watch?v=_xUcYfbtfsI
// Android Studio Tutorial - Build a GPS App

package com.example.basicgps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
//import android.location.LocationRequest;
import android.nfc.Tag;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Switch;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;
    Switch sw_locationsupdates, sw_gps;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
//        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//        locationRequest = LocationRequest.

        sw_gps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (sw_gps.isChecked()){
                    locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS");
                }
                else{
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers/Wifi");
                }
            }
        });

        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "This app requires permission to be granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    /**
     * @brief Get permissions from user to track gps before getting location and updating UI
     */
    private void updateGPS(){
        // get permissions from user to track GPS
        // get current location from fused client
        // update UI with values

        // get associated location provider client for main activity
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        // checks if permission to access fine location is granted
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // get location stored in memory
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // use location class to update text views
                    if (location != null) {
                        updateUIValues(location);
                    } else
                        Log.d("location", "location is NULL");
                }
            });
        }
        else {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_COURSE_LOCATION);
            }
        }
    }

    /**
     * @brief Update text views with location values
     */
    private void updateUIValues(Location location) {
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));

        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_speed.setText("NOT AVAILABLE");
        }


    }
}