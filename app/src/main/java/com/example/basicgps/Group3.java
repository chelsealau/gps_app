/** REFERENCES
 * https://javapapers.com/android/get-current-location-in-android/
 * https://stackoverflow.com/questions/17591147/how-to-get-current-location-in-android
 * https://developer.android.com/training/location/request-updates
 */
package com.example.basicgps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.example.basicgps.database.GPSDatabase;
import com.example.basicgps.database.Units;
import com.example.basicgps.database.entities.Metric;
import com.example.basicgps.database.entities.Score;
import com.example.basicgps.database.entityDAOs.ScoreDAO;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Group3 extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    protected LocationManager locationManager;
    private static final String TAG = "Group3";

    SwitchCompat sw_fontsize;
    SwitchCompat sw_pause;
    AppCompatButton sw_test;
    private float currSpeed;
    private String strCurrentSpeed;
    private String strLong, strLat, strAlt, strSecTime, strMinTime, strHrTime, strDayTime,
            strSecTimeDist, strMinTimeDist, strHrTimeDist, strDayTimeDist;
    private double raw_long, raw_lat, raw_alt, raw_speed, mile_speed;
    private int  meter_speed, metric_speed, mph_speed, intSpeed;
    private double meter_alt, kilometer_alt, mile_alt, feet_alt;
    private double distance=0, tmp_distance, dist_diff=0, old_distance=0;
    private long startTime, startTimeDist, distTimeNow, reStartTime, stopTime, timeElapsed, totalMovingTime,  time_diff;
    boolean hasStopped = false;
    private double pre_lat=0, pre_lon=0, pre_alt=0, pre_speed;
    private double max_dist, min_dist, max_speed, min_speed;
    private long max_time, min_time;

    Timer timer;
    TimerTask timerTask;

    Timer distanceTimer;
    TimerTask distanceTimerTask;

    TextView tv_lat, tv_lon, tv_speed, tv_alt, diff_lat, diff_lon, diff_speed, diff_alt, tv_distance, tv_time, tv_time_distance;

    AppCompatButton help_button, reset_button, highscore_button;

    RadioButton chbx_seconds, chkbx_minutes, chkbx_hours,chkbx_days,
            chkbx_meters,chkbx_kilometers,chkbx_miles,chkbx_feet,chkbx_dist_meters,
            chkbx_dist_kilometers,chkbx_dist_miles,chkbx_dist_feet, chkbx_meterPerSec,
            chkbx_kmh, chkbx_mph, chkbx_minPermile;
    ImageView up_arrow_lat, down_arrow_lat, up_arrow_lon, down_arrow_lon, up_arrow_alt, down_arrow_alt, up_arrow_speed, down_arrow_speed;

    int LOCATION_REFRESH_TIME = 1; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 1; // 500 meters to update
    int GPS_INITIALIZATION = 0;

    Score appScores = null;

//    public void startThread() {
//        Log.d(TAG, "startThread: ");
//        LocationThread thread = new LocationThread(this);
//        thread.start();
//    }

    /**
     * @brief Background thread that calls bLocationListener directly
     */
    class LocationThread extends Thread {
        Location location;

        LocationThread(Location location, String name) {
            this.location = location;
            this.setName(name);
        }

        @Override
        public void run() {
            Looper.prepare();
            bLocationListener.onLocationChanged(location);
        }
    }

    /**
     * @brief Main(UI) thread LocationListener that starts background thread
     */
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            LocationThread thread = new LocationThread(location, "LocationThread");
            thread.start();
        }
    };

    /**
     * @brief Fetches measurements based on location passed to background thread, passes values back to UI thread to update UI
     */
    private final LocationListener bLocationListener = new LocationListener() {

        double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
            double R = 6371; // Radius of the earth in km
            double dLat = deg2rad(lat2 - lat1);  // deg2rad below
            double dLon = deg2rad(lon2 - lon1);
            double a =
                    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                    Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double d = R * c; // Distance in km
            return d;
        }

        double deg2rad(double deg) {
            return deg * (Math.PI / 180);
        }

        @Override
        public void onLocationChanged(final Location location) {
//            double tmp_diff;
            if (!isPaused()) {
                Log.d(TAG, "onLocationChanged: " + Thread.currentThread());
                raw_long = location.getLongitude();
                raw_lat = location.getLatitude();
                raw_speed = location.getSpeed();
                raw_alt = location.getAltitude();
                strLong = String.format("%.4f", raw_long);
                strLat = String.format("%.4f", raw_lat);
                strAlt = String.format("%.4f", raw_alt);
                if (pre_lat == 0.0) {
                    pre_lat = raw_lat;
                }
                if (pre_lon == 0.0) {
                    pre_lon = raw_long;
                }
                // calculate distance in KM
                old_distance = distance;
                distance += getDistanceFromLatLonInKm(pre_lat, pre_lon, raw_lat, raw_long);
                double new_distance = distance;
                dist_diff = new_distance - old_distance;
                if (dist_diff != 0) {
                    if (GPS_INITIALIZATION > 0) {
                        if (GPS_INITIALIZATION == 1) {
                            distanceTimer = new Timer();
                            startTimeDist = System.currentTimeMillis();
                        }
                        startDistanceTimer();
                    }
                }
                // get difference of Latitude
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Units.Time timeUnits = null;
                        Units.Distance altitudeUnits = null;
                        double altitudeToSave = 0;
                        Units.Distance distanceUnits = null;
                        double distanceToSave = 0;
                        Units.Speed speedUnits = null;
                        double speedToSave = 0;

                        double tmp_diff;
                        tmp_diff = raw_lat - pre_lat;
                        if (tmp_diff < 0) {
                            down_arrow_lat.setVisibility(View.VISIBLE);
                            up_arrow_lat.setVisibility(View.GONE);
                            diff_lat.setVisibility(View.VISIBLE);
//                    diff_lat.setText(String.format("%.4f",tmp_diff));
                            diff_lat.setText(String.valueOf(tmp_diff));
                        } else if (tmp_diff > 0) {
                            down_arrow_lat.setVisibility(View.GONE);
                            up_arrow_lat.setVisibility(View.VISIBLE);
                            diff_lat.setVisibility(View.VISIBLE);
//                    diff_lat.setText(String.format("%.4f",tmp_diff));
                            diff_lat.setText(String.valueOf(tmp_diff));
                        } else {
                            down_arrow_lat.setVisibility(View.GONE);
                            up_arrow_lat.setVisibility(View.GONE);
                            diff_lat.setVisibility(View.GONE);
                        }
                        pre_lat = raw_lat;
                        // get difference of Longitude
                        tmp_diff = raw_long - pre_lon;
                        if (tmp_diff < 0) {
                            down_arrow_lon.setVisibility(View.VISIBLE);
                            up_arrow_lon.setVisibility(View.GONE);
                            diff_lon.setVisibility(View.VISIBLE);
//                    diff_lon.setText(String.format("%.4f",tmp_diff));
                            diff_lon.setText(String.valueOf(tmp_diff));
                        } else if (tmp_diff > 0) {
                            down_arrow_lon.setVisibility(View.GONE);
                            up_arrow_lon.setVisibility(View.VISIBLE);
                            diff_lon.setVisibility(View.VISIBLE);
//                    diff_lon.setText(String.format("%.4f",tmp_diff));
                            diff_lon.setText(String.valueOf(tmp_diff));
                        } else {
                            down_arrow_lon.setVisibility(View.GONE);
                            up_arrow_lon.setVisibility(View.GONE);
                            diff_lon.setVisibility(View.GONE);
                        }
                        pre_lon = raw_long;

                        // get difference of altitude
                        tmp_diff = raw_alt - pre_alt;
                        if (tmp_diff < 0) {
                            down_arrow_alt.setVisibility(View.VISIBLE);
                            up_arrow_alt.setVisibility(View.GONE);
                            diff_alt.setVisibility(View.VISIBLE);
//                    diff_alt.setText(String.format("%.4f",tmp_diff));
                            diff_alt.setText(String.valueOf(tmp_diff));
                        } else if (tmp_diff > 0) {
                            down_arrow_alt.setVisibility(View.GONE);
                            up_arrow_alt.setVisibility(View.VISIBLE);
                            diff_alt.setVisibility(View.VISIBLE);
//                    diff_alt.setText(String.format("%.4f",tmp_diff));
                            diff_alt.setText(String.valueOf(tmp_diff));
                        } else {
                            down_arrow_alt.setVisibility(View.GONE);
                            up_arrow_alt.setVisibility(View.GONE);
                            diff_alt.setVisibility(View.GONE);
                        }
                        pre_alt = raw_alt;

                        tv_lat.setText(strLat);
                        tv_lon.setText(strLong);
                        tv_alt.setText(strAlt);

                        if (location != null) {
                            if (Math.round(currSpeed) == 0) {
                                tv_speed.setText("0.00");
                            } else {
                                if (location.hasAltitude()) {
                                    if (chkbx_meters.isChecked()) {
                                        meter_alt = (raw_alt);
                                        String strCurrentAlt = String.valueOf(meter_alt);
                                        tv_alt.setText(strCurrentAlt + " meters");
                                        altitudeUnits = Units.Distance.METERS;
                                        altitudeToSave = meter_alt;
                                    }

                                    if (chkbx_kilometers.isChecked()) {
                                        kilometer_alt = (raw_alt / 1000);
                                        String strCurrentAlt = String.valueOf(kilometer_alt);
                                        tv_alt.setText(strCurrentAlt + " kilometers");
                                        altitudeUnits = Units.Distance.KILOMETERS;
                                        altitudeToSave = kilometer_alt;
                                    }

                                    if (chkbx_miles.isChecked()) {
                                        mile_alt = (raw_alt / 1609);
                                        String strCurrentAlt = String.valueOf(mile_alt);
                                        tv_alt.setText(strCurrentAlt + " miles");
                                        altitudeUnits = Units.Distance.MILES;
                                        altitudeToSave = mile_alt;
                                    }

                                    if (chkbx_feet.isChecked()) {
                                        feet_alt = (raw_alt * 3.281);
                                        String strCurrentAlt = String.valueOf(feet_alt);
                                        tv_alt.setText(strCurrentAlt + " feet");
                                        altitudeUnits = Units.Distance.FEET;
                                        altitudeToSave = feet_alt;
                                    }

                                    pre_lat = raw_lat;
                                    // get difference of Longitude
                                    tmp_diff = raw_long - pre_lon;
                                    if (tmp_diff < 0) {
                                        down_arrow_lon.setVisibility(View.VISIBLE);
                                        up_arrow_lon.setVisibility(View.GONE);
                                        diff_lon.setVisibility(View.VISIBLE);
//                    diff_lon.setText(String.format("%.4f",tmp_diff));
                                        diff_lon.setText(String.valueOf(tmp_diff));
                                    } else if (tmp_diff > 0) {
                                        down_arrow_lon.setVisibility(View.GONE);
                                        up_arrow_lon.setVisibility(View.VISIBLE);
                                        diff_lon.setVisibility(View.VISIBLE);
//                    diff_lon.setText(String.format("%.4f",tmp_diff));
                                        diff_lon.setText(String.valueOf(tmp_diff));
                                    } else {
                                        down_arrow_lon.setVisibility(View.GONE);
                                        up_arrow_lon.setVisibility(View.GONE);
                                        diff_lon.setVisibility(View.GONE);
                                    }
                                    pre_lon = raw_long;

                                    // get difference of Speed
                                    tmp_diff = raw_alt - pre_alt;
                                    if (tmp_diff < 0) {
                                        down_arrow_alt.setVisibility(View.VISIBLE);
                                        up_arrow_alt.setVisibility(View.GONE);
                                        diff_alt.setVisibility(View.VISIBLE);
//                    diff_alt.setText(String.format("%.4f",tmp_diff));
                                        diff_alt.setText(String.valueOf(tmp_diff));
                                    } else if (tmp_diff > 0) {
                                        down_arrow_alt.setVisibility(View.GONE);
                                        up_arrow_alt.setVisibility(View.VISIBLE);
                                        diff_alt.setVisibility(View.VISIBLE);
//                    diff_alt.setText(String.format("%.4f",tmp_diff));
                                        diff_alt.setText(String.valueOf(tmp_diff));
                                    } else {
                                        down_arrow_alt.setVisibility(View.GONE);
                                        up_arrow_alt.setVisibility(View.GONE);
                                        diff_alt.setVisibility(View.GONE);
                                    }
                                    pre_alt = raw_alt;

                                    tv_lat.setText(strLat);
                                    tv_lon.setText(strLong);
                                    tv_alt.setText(strAlt);

                                    if (location != null) {
                                        if (Math.round(currSpeed) == 0) {
                                            tv_speed.setText("0.00");
                                        } else {
                                            if (location.hasAltitude()) {
                                                if (chkbx_meters.isChecked()) {
                                                    meter_alt = (raw_alt);
                                                    String strCurrentAlt = String.valueOf(meter_alt);
                                                    tv_alt.setText(strCurrentAlt + " meters");
                                                    altitudeUnits = Units.Distance.METERS;
                                                    altitudeToSave = meter_alt;
                                                }

                                                if (chkbx_kilometers.isChecked()) {
                                                    kilometer_alt = (raw_alt / 1000);
                                                    String strCurrentAlt = String.valueOf(kilometer_alt);
                                                    tv_alt.setText(strCurrentAlt + " kilometers");
                                                    altitudeUnits = Units.Distance.KILOMETERS;
                                                    altitudeToSave = kilometer_alt;
                                                }

                                                if (chkbx_miles.isChecked()) {
                                                    mile_alt = (raw_alt / 1609);
                                                    String strCurrentAlt = String.valueOf(mile_alt);
                                                    tv_alt.setText(strCurrentAlt + " miles");
                                                    altitudeUnits = Units.Distance.MILES;
                                                    altitudeToSave = mile_alt;
                                                }

                                                if (chkbx_feet.isChecked()) {
                                                    feet_alt = (raw_alt * 3.281);
                                                    String strCurrentAlt = String.valueOf(feet_alt);
                                                    tv_alt.setText(strCurrentAlt + " feet");
                                                    altitudeUnits = Units.Distance.FEET;
                                                    altitudeToSave = feet_alt;
                                                }
                                            } else {
                                                tv_alt.setText("Not Available");
                                            }

                                            if (chkbx_dist_meters.isChecked()) {

                                                tmp_distance = distance * 1000;
                                                tv_distance.setText(String.format("%.4f", tmp_distance) + " m");
                                                distanceUnits = Units.Distance.METERS;
                                                distanceToSave = tmp_distance;
//                            tv_distance.setText(String.valueOf(tmp_distance) + " m");

                                            } else if (chkbx_dist_kilometers.isChecked()) {
                                                tv_distance.setText(String.format("%.4f", distance) + " km");
                                                distanceUnits = Units.Distance.KILOMETERS;
                                                distanceToSave = distance;
//                            tv_distance.setText(String.valueOf(distance) + " km");
                                            } else if (chkbx_dist_miles.isChecked()) {
                                                tmp_distance = distance * 0.621371;
                                                tv_distance.setText(String.format("%.4f", tmp_distance) + " miles");
                                                distanceUnits = Units.Distance.MILES;
                                                distanceToSave = tmp_distance;
//                            tv_distance.setText(String.valueOf(tmp_distance) + " miles");
                                            } else if (chkbx_dist_feet.isChecked()) {
                                                tmp_distance = distance * 3280.838879986877;
                                                tv_distance.setText(String.format("%.4f", tmp_distance) + " feet");
                                                distanceUnits = Units.Distance.FEET;
                                                distanceToSave = tmp_distance;
//                            tv_distance.setText(String.valueOf(tmp_distance) + " feet");
                                            }

                                            if (chkbx_meterPerSec.isChecked()) {
                                                meter_speed = (int) (raw_speed);
                                                strCurrentSpeed = String.valueOf(meter_speed);
                                                tv_speed.setText(strCurrentSpeed + " m/sec");
                                                intSpeed = meter_speed;
                                                speedUnits = Units.Speed.METERS_PER_SECOND;
                                                speedToSave = meter_speed;
                                            }
                                            if (chkbx_kmh.isChecked()) {
                                                metric_speed = (int) ((raw_speed * 3600) / 1000);
                                                strCurrentSpeed = String.valueOf(metric_speed);
                                                tv_speed.setText(strCurrentSpeed + " km/h");
                                                intSpeed = metric_speed;
                                                speedUnits = Units.Speed.KILOMETERS_PER_HOUR;
                                                speedToSave = metric_speed;
                                            }
                                            if (chkbx_mph.isChecked()) {
                                                mph_speed = (int) (raw_speed * 2.2369);
                                                strCurrentSpeed = String.valueOf(mph_speed);
                                                tv_speed.setText(strCurrentSpeed + " mph");
                                                intSpeed = mph_speed;
                                                speedUnits = Units.Speed.MILES_PER_HOUR;
                                                speedToSave = mph_speed;
                                            }
                                            if (chkbx_minPermile.isChecked()) {
                                                mile_speed = (double) (raw_speed / 26.822);
//                            strCurrentSpeed = String.valueOf(mile_speed);j
                                                strCurrentSpeed = String.format("%.2f", mile_speed);
                                                tv_speed.setText(strCurrentSpeed + " mile/min");
                                                intSpeed = (int) mile_speed;
                                                speedUnits = Units.Speed.MILES_PER_MINUTE;
                                                speedToSave = mile_speed;
                                            }
                                        }
                                    }
                                }
                            }

                            currSpeed = location.getSpeed();
                            updateSpeed(intSpeed);
                        }

                        GPSDatabase.databaseWriteExecutor.execute(() -> {
                            GPSDatabase.getInstance(getApplicationContext()).metricDAO()
                                    .insert(new Metric(
                                            raw_lat,
                                            raw_long,
                                            raw_alt,
                                            Units.Distance.METERS,
                                            raw_speed,
                                            Units.Speed.METERS_PER_SECOND,
                                            distance,
                                            Units.Distance.KILOMETERS,
                                            Long.valueOf(strSecTime),
                                            Units.Time.SECONDS
                                            ));
                        });
                    }
                });
                GPS_INITIALIZATION += 1;
            }



        }
    };

    private void updateSpeed(int intSpeed) {
        intSpeed = Math.round(intSpeed);
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
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble("max_dist", max_dist);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Toast.makeText(Group3.this, "savedInstanceState EXISTS!!!", Toast.LENGTH_LONG).show();
            // Restore value of members from saved state
            max_dist = savedInstanceState.getDouble("max_dist");
            max_time = savedInstanceState.getLong("max_time");
            max_speed = savedInstanceState.getDouble("max_speed");
        } else {
            Toast.makeText(Group3.this, "savedInstanceState is Null", Toast.LENGTH_LONG).show();
            // Probably initialize members with default values for a new instance
            max_dist=0;
            max_time=0;
            max_speed=0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("max_dist", String.valueOf(max_dist));

        setContentView(R.layout.activity_main);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_speed = findViewById(R.id.tv_speed);
        tv_alt = findViewById(R.id.tv_alt);

        tv_distance = findViewById(R.id.tv_distance);
        tv_time_distance = findViewById(R.id.tv_time_distance);
        tv_time = findViewById(R.id.tv_time);
        sw_fontsize = findViewById(R.id.sw_fontsize);
        sw_test = findViewById(R.id.sw_test);
        sw_pause = findViewById(R.id.sw_pause);
        reset_button = findViewById(R.id.reset_button);
        help_button = findViewById(R.id.help_button);
        highscore_button = findViewById(R.id.highscore_button);
        // Time unit selection
        chbx_seconds = findViewById(R.id.chbx_seconds);
        chkbx_minutes = findViewById(R.id.chkbx_minutes);
        chkbx_hours = findViewById(R.id.chkbx_hours);
        chkbx_days = findViewById(R.id.chkbx_days);
        // Altitude unit selection
        chkbx_meters = findViewById(R.id.chkbx_meters);
        chkbx_kilometers = findViewById(R.id.chkbx_kilometers);
        chkbx_miles = findViewById(R.id.chkbx_miles);
        chkbx_feet = findViewById(R.id.chkbx_feet);
        // Distance unit selection
        chkbx_dist_meters = findViewById(R.id.chkbx_dist_meters);
        chkbx_dist_kilometers = findViewById(R.id.chkbx_dist_kilometers);
        chkbx_dist_miles = findViewById(R.id.chkbx_dist_miles);
        chkbx_dist_feet = findViewById(R.id.chkbx_dist_feet);
        // Speed unit selection
        chkbx_meterPerSec = findViewById(R.id.chkbx_meterPerSec);
        chkbx_kmh = findViewById(R.id.chkbx_kmh);
        chkbx_mph = findViewById(R.id.chkbx_mph);
        chkbx_minPermile = findViewById(R.id.chkbx_minPermile);

        // Difference
        up_arrow_lat = findViewById(R.id.up_arrow_lat);
        down_arrow_lat = findViewById(R.id.down_arrow_lat);
        diff_lat = findViewById(R.id.diff_lat);

        up_arrow_lon = findViewById(R.id.up_arrow_lon);
        down_arrow_lon = findViewById(R.id.down_arrow_lon);
        diff_lon = findViewById(R.id.diff_lon);

        up_arrow_alt = findViewById(R.id.up_arrow_alt);
        down_arrow_alt = findViewById(R.id.down_arrow_alt);
        diff_alt = findViewById(R.id.diff_alt);

        up_arrow_speed = findViewById(R.id.up_arrow_speed);
        down_arrow_speed = findViewById(R.id.down_arrow_speed);
        diff_speed = findViewById(R.id.diff_speed);
        Intent max_page = new Intent(Group3.this, getHighScore.class);

        startTime = System.currentTimeMillis();
        timer = new Timer();
        startTimer();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }

        }

        sw_fontsize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (bigFont()) {
                    tv_speed.setTextSize(20);
                } else {
                    tv_speed.setTextSize(14);
                }
            }
        });

        sw_pause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String speedText = strCurrentSpeed;
                String latText = strLat;
                String lonText = strLong;
                if (isPaused()) {
                    tv_lat.setText(latText);
                    tv_lon.setText(lonText);
                    if(chkbx_meterPerSec.isChecked()){
                        tv_speed.setText(speedText +" m/sec");
                    }
                    if(chkbx_kmh.isChecked()) {
                        tv_speed.setText(speedText +" km/h");
                    }
                    if(chkbx_mph.isChecked()) {
                        tv_speed.setText(speedText+" mph");                        }
                    if(chkbx_minPermile.isChecked()) {
                        tv_speed.setText(speedText+" min/mile");
                    }
                    if(chbx_seconds.isChecked()){
                        tv_time.setText(strSecTime+" seconds");
                    }
                    if(chkbx_minutes.isChecked()){
                        tv_time.setText(strMinTime+" minutes");
                    }
                    if(chkbx_hours.isChecked()){
                        tv_time.setText(strHrTime+" hours");
                    }
                    if(chkbx_days.isChecked()){
                        tv_time.setText(strDayTime+" days");
                    }
                }
                else {
                    updateSpeed(intSpeed);
                    long timeNow = System.currentTimeMillis();
                    updateTime(timeNow);
                }
            }
        });

        help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Group3.this, "This app measures your speed using GPS in mph or kph", Toast.LENGTH_LONG).show();
                Toast.makeText(Group3.this, "Use selectors to choose units and font size", Toast.LENGTH_LONG).show();
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strSecTimeDist = "0:00"; strMinTime = "0:00";strHrTime="0:00"; strDayTime="0:00";
                if (distance > max_dist){
                    max_dist = distance;
                }
                max_page.putExtra("dist", max_dist+" km");
                Log.v("max_dist", String.valueOf(max_dist));
                if (raw_speed>max_speed){
                    max_speed = raw_speed;
                }
                max_page.putExtra("speed", max_speed+" m/sec");

                if (timeElapsed>max_time){
                    max_time = timeElapsed;
                }
                max_page.putExtra("time", max_time/1000+" seconds");


                tv_alt.setText("0.0");tv_time.setText("0:00");
                distance = 0; tv_distance.setText("0.0"); tv_speed.setText("0.0"); tv_time_distance.setText("0:00");
                diff_alt.setText("0.0"); diff_alt.setVisibility(View.GONE);
                diff_lat.setText("0.0"); diff_lat.setVisibility(View.GONE);
                diff_lon.setText("0.0"); diff_lon.setVisibility(View.GONE);
                diff_speed.setText("0.0"); diff_speed.setVisibility(View.GONE);
                currSpeed = 0;
                updateSpeed((int)currSpeed);
                pre_alt = 0; pre_lat = 0; pre_lon = 0; pre_speed = 0;
                totalMovingTime = 0;
                timeElapsed = 0;
                down_arrow_lat.setVisibility(View.GONE); up_arrow_lat.setVisibility(View.GONE);
                down_arrow_lon.setVisibility(View.GONE); up_arrow_lon.setVisibility(View.GONE);
                down_arrow_alt.setVisibility(View.GONE); up_arrow_alt.setVisibility(View.GONE);
                startTime = System.currentTimeMillis();
                timer = new Timer();
                startTimer();

                distTimeNow = 0;
                dist_diff = 0;
                updateDistanceTime(distTimeNow, dist_diff);
//                startTimeDist = System.currentTimeMillis();
//                distanceTimer = new Timer();
//                startDistanceTimer();
//                stopDistanceTimer();
                GPSDatabase.databaseWriteExecutor.execute(() -> {
                    GPSDatabase.getInstance(getApplicationContext()).metricDAO().clearTable();
                });
            }
        });

        highscore_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Group3.this, getHighScore.class);
                startActivity(max_page);
            }
        });

        sw_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location("Test");
                double lat = 10.0;
                double log = 20.0;

                Handler handler1 = new Handler();
                for (int a = 1; a <= 10; a++) {
                    double finalLat = lat;
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            location.setLatitude(finalLat);
                            location.setLongitude(log);
                            location.setSpeed(10);
                            tv_lat.setText(String.valueOf(location.getLatitude()));
                            tv_lon.setText(String.valueOf(location.getLongitude()));
                            currSpeed = 10;
                            updateSpeed(intSpeed);
                            double kmSpeed = (10 * 1.609);
                            double mspeed = (10*2.237);
                            double minPermile = (60/currSpeed);

                            int int_kmSpeed = (int) kmSpeed;
                            int int_mSpeed = (int) mspeed;
                            int int_minPermile = (int) minPermile;
                            String speedText = "10";
                            if(chkbx_meterPerSec.isChecked()){
                                tv_speed.setText(String.valueOf(int_mSpeed) +" m/sec");
                            }
                            if(chkbx_kmh.isChecked()) {
                                tv_speed.setText(String.valueOf(int_kmSpeed) +" km/h");
                            }
                            if(chkbx_mph.isChecked()) {
                                tv_speed.setText(speedText+" mph");                        }
                            if(chkbx_minPermile.isChecked()) {
                                tv_speed.setText(String.valueOf(int_minPermile)+" min/mile");                        }
                            }
                    }, 1000 * a);
                    lat = lat + 4.47038888888889;
                }
            }
        });
        GPSDatabase.databaseWriteExecutor.execute(() -> {
            ScoreDAO appScoreDAO = GPSDatabase.getInstance(getApplicationContext()).scoreDAO();
            int numScores = appScoreDAO.documentCount();
            if (numScores > 0) {
                appScores = appScoreDAO.getSavedScore();
                min_dist = appScores.minDistance;
                max_dist = appScores.maxDistance;
                min_speed = appScores.minSpeed;
                max_speed = appScores.maxSpeed;
                min_time = appScores.minTime;
                max_time = appScores.maxTime;
            }
            else {
                min_dist = 0.0;
                max_dist = 0.0;
                min_speed = 0.0;
                max_speed = 0.0;
                min_time = 0;
                max_time = 0;
                appScoreDAO.insertScore(new Score(
                            max_speed,
                            min_speed,
                            max_dist,
                            min_dist,
                            max_time,
                            min_time
                        )
                );
            }
        });

    }
    private boolean bigFont() {
        return sw_fontsize.isChecked();
    }
    private boolean isPaused() {
        return sw_pause.isChecked();
    }

    private void updateTime(long timeNow){
        if(!isPaused()){
            if(chbx_seconds.isChecked()) {
                long timeElapsed = timeNow - startTime;
                long time_seconds = timeElapsed/1000;
                strSecTime = String.valueOf(time_seconds);
                tv_time.setText(String.valueOf(time_seconds)+ " seconds");
            }
            if(chkbx_minutes.isChecked()) {
                long timeElapsed = timeNow - startTime;
                long time_seconds = timeElapsed/1000;
                double time_minutes = (double) time_seconds/60;
                strMinTime = String.format("%.4f", time_minutes);
                tv_time.setText(String.format("%.4f", time_minutes)+ " minutes");
            }
            if(chkbx_hours.isChecked()) {
                long timeElapsed = timeNow - startTime;
                long time_seconds = timeElapsed/1000;
                long time_minutes = time_seconds/60;
                double time_hours = (double) time_minutes/60;
                strHrTime = String.format("%.6f", time_hours);
                tv_time.setText(String.format("%.6f", time_hours) + " hours");
            }
            if(chkbx_days.isChecked()) {
                long timeElapsed = timeNow - startTime;
                long time_seconds = timeElapsed/1000;
                long time_minutes = time_seconds/60;
                long time_hours = time_minutes/60;
                double time_days = (double) time_hours/24;
                strDayTime = String.format("%.8f", time_days);
                tv_time.setText(String.format("%.3f", time_days) + " days");
            }
        }
        else{
            if(chbx_seconds.isChecked()){
                tv_time.setText(strSecTime+" seconds");
            }
            if(chkbx_minutes.isChecked()){
                tv_time.setText(strMinTime+" minutes");
            }
            if(chkbx_hours.isChecked()){
                tv_time.setText(strHrTime+" hours");
            }
            if(chkbx_days.isChecked()){
                tv_time.setText(strDayTime+" days");
            }
        }
    }

    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        long timeNow = System.currentTimeMillis();
                        updateTime(timeNow);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }
    private void startDistanceTimer()
    {
        distanceTimerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (ismoving()){
                            if(hasStopped){
                                time_diff = stopTime - reStartTime;
                                timeElapsed = timeElapsed - time_diff;
                                totalMovingTime = timeElapsed;
                                distTimeNow = System.currentTimeMillis();
                                updateDistanceTime(distTimeNow, dist_diff);

                            }
                            else{
                                distTimeNow = System.currentTimeMillis();
                                updateDistanceTime(distTimeNow, dist_diff);
//                                totalMovingTime+= time;
                                old_distance = distance;
                            }

                        }
                    }
                });

            }
        };
        distanceTimer.scheduleAtFixedRate(distanceTimerTask, 0 ,1000);
    }

    private void stopDistanceTimer(){
        stopTime = System.currentTimeMillis();
        distanceTimerTask.cancel();
    }

    private boolean ismoving(){
        if(old_distance == distance){
            stopDistanceTimer();
            hasStopped = true;
            return false;
        }
        else if(old_distance != distance){
            reStartTime = System.currentTimeMillis();
            old_distance = distance;
            return true;
        }
        else{
            stopDistanceTimer();
            old_distance = distance;
            return false;
        }
    }
    private void updateDistanceTime(long distTimeNow, double dist_diff){
        if(dist_diff!=0){
            if(chbx_seconds.isChecked()) {
                long time_seconds = totalMovingTime/1000;
                strSecTimeDist = String.valueOf(time_seconds);
                tv_time_distance.setText(strSecTimeDist + " seconds");
            }
            if(chkbx_minutes.isChecked()) {
                long time_seconds = totalMovingTime/1000;
                double time_minutes = (double) time_seconds/60;
                strMinTimeDist = String.format("%.4f", time_minutes);
                tv_time_distance.setText(strMinTimeDist + " minutes");
            }
            if(chkbx_hours.isChecked()) {
                long time_seconds = totalMovingTime/1000;
                long time_minutes = time_seconds/60;
                double time_hours = (double) time_minutes/60;
                strHrTimeDist = String.format("%.6f", time_hours);
                tv_time_distance.setText(strHrTimeDist + " hours");
            }
            if(chkbx_days.isChecked()) {
                long time_seconds = totalMovingTime/1000;
                long time_minutes = time_seconds/60;
                long time_hours = time_minutes/60;
                double time_days = (double) time_hours;
                strDayTimeDist = String.format("%.3f", time_days);
                tv_time_distance.setText(strDayTimeDist + " days");
            }
        }
        if (dist_diff==0){
            if(chbx_seconds.isChecked()){
                tv_time_distance.setText("0:00 seconds");
            }
            if(chkbx_minutes.isChecked()){
                tv_time_distance.setText("0:00 minutes");
            }
            if(chkbx_hours.isChecked()){
                tv_time_distance.setText("0:00 hours");
            }
            if(chkbx_days.isChecked()){
                tv_time_distance.setText("0:00 days");
            }
        }
        else{
            if(chbx_seconds.isChecked()){
                tv_time_distance.setText(strSecTimeDist+" seconds");
            }
            if(chkbx_minutes.isChecked()){
                tv_time_distance.setText(strMinTimeDist+" minutes");
            }
            if(chkbx_hours.isChecked()){
                tv_time_distance.setText(strHrTimeDist+" hours");
            }
            if(chkbx_days.isChecked()){
                tv_time_distance.setText(strDayTimeDist+" days");
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.chbx_seconds:
                if (checked)
                    Toast.makeText(Group3.this, "Time Units: seconds", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_minutes:
                if (checked)
                    Toast.makeText(Group3.this, "Time Units: minutes", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_hours:
                if (checked)
                    Toast.makeText(Group3.this, "Time Units: hours", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_days:
                if (checked)
                    Toast.makeText(Group3.this, "Time Units: days", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_meters:
                if (checked)
                    Toast.makeText(Group3.this, "Altitude Units: meters", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_kilometers:
                if (checked)
                    Toast.makeText(Group3.this, "Altitude Units: kilometers", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_miles:
                if (checked)
                    Toast.makeText(Group3.this, "Altitude Units: miles", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_feet:
                if (checked)
                    Toast.makeText(Group3.this, "Altitude Units: feet", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_dist_meters:
                if (checked)
                    Toast.makeText(Group3.this, "Distance Units: meters", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_dist_kilometers:
                if (checked)
                    Toast.makeText(Group3.this, "Distance Units: kilometers", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_dist_miles:
                if (checked)
                    Toast.makeText(Group3.this, "Distance Units: miles", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_dist_feet:
                if (checked)
                    Toast.makeText(Group3.this, "Distance Units: feet", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_meterPerSec:
                if (checked)
                    Toast.makeText(Group3.this, "Speed Units: meters per second", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_kmh:
                if (checked)
                    Toast.makeText(Group3.this, "Speed Units: kilometers per hour", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_mph:
                if (checked)
                    Toast.makeText(Group3.this, "Speed Units: miles per hour", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.chkbx_minPermile:
                if (checked)
                    Toast.makeText(Group3.this, "Speed Units: minutes per mile", Toast.LENGTH_SHORT).show();
                    break;
        }
    }
}








