package com.example.basicgps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.CompoundButton;

import android.util.Log;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    protected LocationManager locationManager;
    SwitchCompat sw_metric;
    SwitchCompat sw_fontsize;
    private float currSpeed;
    private String strCurrentSpeed;

    //    protected LocationListener locationListener;
//    protected Context context;
    TextView tv_lat, tv_lon, tv_speed;

    int LOCATION_REFRESH_TIME = 500; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 100; // 500 meters to update

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            tv_lat.setText(String.valueOf(location.getLatitude()));
            tv_lon.setText(String.valueOf(location.getLongitude()));
            updateSpeed();
            tv_speed.setText(String.valueOf(location.getSpeed()));
            strCurrentSpeed = String.valueOf(location.getSpeed());
            currSpeed = location.getSpeed();

        }
    };

    private void updateSpeed() {
//        Formatter fmt = new Formatter(new StringBuilder());
//        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
//        String strCurrentSpeed = fmt.toString();
//        strCurrentSpeed = strCurrentSpeed.replace(" ", "0");
//        float floatSpeed=Float.parseFloat(strCurrentSpeed);
        int intSpeed = Math.round(currSpeed);
//        System.out.println(intSpeed);
        if(intSpeed == 0){
            tv_speed.setTextColor(Color.parseColor("#77FF33"));
        }
        if(intSpeed > 0 && intSpeed<=10){
            tv_speed.setTextColor(Color.parseColor("#DFFF00"));
        }
        if(intSpeed > 10 && intSpeed<=15){
            tv_speed.setTextColor(Color.parseColor("#FFBF00"));
        }
        if(intSpeed > 15 && intSpeed<=20){
            tv_speed.setTextColor(Color.parseColor("#FF7F50"));
        }
        if(intSpeed > 20 && intSpeed<=25){
            tv_speed.setTextColor(Color.parseColor("#DE3163"));
        }
        if(intSpeed > 25 && intSpeed<=30){
            tv_speed.setTextColor(Color.parseColor("#9FE2BF"));
        }
        if(intSpeed > 30 && intSpeed<=35){
            tv_speed.setTextColor(Color.parseColor("#339CFF"));
        }
        if(intSpeed > 35 && intSpeed<=40){
            tv_speed.setTextColor(Color.parseColor("#9B33FF"));
        }
        if(intSpeed > 40 && intSpeed<=45){
            tv_speed.setTextColor(Color.parseColor("#FF33EE"));
        }
        if(intSpeed > 45 && intSpeed<=50){
            tv_speed.setTextColor(Color.parseColor("#FF6D33"));
        }
        if(intSpeed > 50 && intSpeed<=55){
            tv_speed.setTextColor(Color.parseColor("#5DFF33"));
        }
        if(intSpeed > 55 && intSpeed<=60){
            tv_speed.setTextColor(Color.parseColor("#EAFF33"));
        }
        if(intSpeed > 60){
            tv_speed.setTextColor(Color.parseColor("#FF3333"));
        }
        if(this.useMetricUnits()){
            tv_speed.setText(strCurrentSpeed+"km/h");
        }
        else{
            tv_speed.setText(strCurrentSpeed+"mph");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_speed = findViewById(R.id.tv_speed);
        sw_metric = findViewById(R.id.sw_metric);
        sw_fontsize = findViewById(R.id.sw_fontsize);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }

        }

        sw_metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.this.updateSpeed();
            }
        });

        sw_fontsize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (bigFont()) {
                    tv_speed.setTextSize(20);  // textSize="24sp"
                } else {
                    tv_speed.setTextSize(14);
                }
            }
        });
    }

    private boolean useMetricUnits() {
        return sw_metric.isChecked();
    }
    private boolean bigFont() {
        return sw_fontsize.isChecked();
    }
}








