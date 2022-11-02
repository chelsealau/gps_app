/** REFERENCES
 * https://javapapers.com/android/get-current-location-in-android/
 * https://stackoverflow.com/questions/17591147/how-to-get-current-location-in-android
 * https://developer.android.com/training/location/request-updates
 */
package com.example.basicgps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.graphics.Color;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    protected LocationManager locationManager;
    SwitchCompat sw_metric;
    SwitchCompat sw_fontsize;
    private float currSpeed;
    private String strCurrentSpeed;
    AppCompatButton help_button, pause_button;
    TextView tv_lat, tv_lon, tv_speed;

    int LOCATION_REFRESH_TIME = 1; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 1; // 500 meters to update

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            tv_lat.setText(String.valueOf(location.getLatitude()));
            tv_lon.setText(String.valueOf(location.getLongitude()));
            updateSpeed();
            if(location != null){
                if(Math.round(currSpeed) == 0){
                    tv_speed.setText("0.00");
                }
                else{
                    if(useMetricUnits()){
                        int metric_speed = (int) ((location.getSpeed() * 3600) / 1000);
                        strCurrentSpeed = String.valueOf(metric_speed);
                        tv_speed.setText(strCurrentSpeed+" km/h");
                    }
                    else{
                        int mph_speed=(int) (location.getSpeed()*2.2369);
                        strCurrentSpeed = String.valueOf(mph_speed);
                        tv_speed.setText(strCurrentSpeed+" mph");
                    }
                }
            }
            currSpeed = location.getSpeed();
            updateSpeed();
        }
    };

    private void updateSpeed() {
        int intSpeed = 0;
        if(useMetricUnits()){
            intSpeed = (int) ((currSpeed * 3600) / 1000);
        }
        else{
            intSpeed=(int) (currSpeed*2.2369);
        }
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
        if(this.useMetricUnits()){
            if(intSpeed == 0){
                tv_speed.setText("0.00 km/h");
            }
            if(strCurrentSpeed == null){
                tv_speed.setText("0.00 km/h");
            }
            else{
                tv_speed.setText(strCurrentSpeed+" km/h");
            }
        }
        else {
            if (intSpeed == 0) {
                tv_speed.setText("0.00 mph");
            }
            if(strCurrentSpeed == null){
                tv_speed.setText("0.00 mph");
            }
            else{
                tv_speed.setText(strCurrentSpeed + " mph");
            }
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

        help_button = findViewById(R.id.help_button);
        pause_button = findViewById(R.id.pause_button);


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
                    tv_speed.setTextSize(20);
                } else {
                    tv_speed.setTextSize(14);
                }
            }
        });

        help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "This app measures your speed using GPS in mph or kph", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "Use selectors to choose units and font size", Toast.LENGTH_LONG).show();
            }
        });

    }
//    private boolean updatesRequested = true;
//    @OnClick(R.id.pause_button)
//    public void toggleUpdates(){
//        if(updatesRequested){
//            removeLocationUpdates
//        }
//        else{
//
//        }
//    }
//    @Override
//    protected void onPause(){
//        super.onPause();
//        removeLocationUpdates();
//    }
//    private void removeLocationUpdates(){
//        LocationListener mLocationListener = new LocationListener()
//
//        if(location != null){
//            locationManager.removeUpdates();
//        }
//    }

    private boolean useMetricUnits() {
        return sw_metric.isChecked();
    }
    private boolean bigFont() {
        return sw_fontsize.isChecked();
    }
}








