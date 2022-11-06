/** REFERENCES
 * https://javapapers.com/android/get-current-location-in-android/
 * https://stackoverflow.com/questions/17591147/how-to-get-current-location-in-android
 * https://developer.android.com/training/location/request-updates
 */
package com.example.basicgps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;


public class Group3 extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    protected LocationManager locationManager;

    SwitchCompat sw_metric;
    SwitchCompat sw_fontsize;
    SwitchCompat sw_pause;
    AppCompatButton sw_test;
    private float currSpeed;
    private String strCurrentSpeed;
    private String strLong, strLat, strAlt;
    private double raw_long, raw_lat, raw_alt, raw_speed;
    private int metric_speed, mph_speed;
    private double meter_alt, kilometer_alt, mile_alt, feet_alt;
    AppCompatButton help_button;
    TextView tv_lat, tv_lon, tv_speed, tv_alt;
    RadioButton chbx_seconds, chkbx_minutes, chkbx_hours,chkbx_days,
            chkbx_meters,chkbx_kilometers,chkbx_miles,chkbx_feet,chkbx_dist_meters,
            chkbx_dist_kilometers,chkbx_dist_miles,chkbx_dist_feet, chkbx_meterPerSec,
            chkbx_kmh, chkbx_mph, chkbx_minPermile;


    int LOCATION_REFRESH_TIME = 1; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 1; // 500 meters to update

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if(!isPaused()){
                raw_long = location.getLongitude();
                raw_lat = location.getLatitude();
                raw_speed = location.getSpeed();
                raw_alt = location.getAltitude();
                strLong = String.valueOf(raw_long);
                strLat = String.valueOf(raw_lat);
                strAlt = String.valueOf(raw_alt);
                tv_lat.setText(strLat);
                tv_lon.setText(strLong);
                tv_alt.setText(strAlt);


                if(location != null){
                    if(Math.round(currSpeed) == 0){
                        tv_speed.setText("0.00");
                    }
                    else{
                        if(chbx_seconds.isChecked()) {
                            //elapsed_time = gettime()
                            // seconds time = convert_time_toSec(elapsed_time)
                        }
                        if(chkbx_minutes.isChecked()) {

                        }

                        if(chkbx_hours.isChecked()) {

                        }

                        if(chkbx_days.isChecked()) {

                        }
                        if(location.hasAltitude()) {

                            if (chkbx_meters.isChecked()) {
                                meter_alt = (raw_alt);
                                String strCurrentAlt = String.valueOf(meter_alt);
                                tv_alt.setText(strCurrentAlt + " meters");
                            }

                            if (chkbx_kilometers.isChecked()) {
                                kilometer_alt = (raw_alt / 1000);
                                String strCurrentAlt = String.valueOf(kilometer_alt);
                                tv_alt.setText(strCurrentAlt + " kilometers");
                            }

                            if (chkbx_miles.isChecked()) {
                                mile_alt = (raw_alt / 1609);
                                String strCurrentAlt = String.valueOf(mile_alt);
                                tv_alt.setText(strCurrentAlt + " miles");
                            }

                            if (chkbx_feet.isChecked()) {
                                feet_alt = (raw_alt * 3.281);
                                String strCurrentAlt = String.valueOf(feet_alt);
                                tv_alt.setText(strCurrentAlt + " feet");
                            }
                        }
                        else{
                            tv_alt.setText("Not Available");
                        }

                        if(chkbx_dist_meters.isChecked()) {

                        }

                        if(chkbx_dist_kilometers.isChecked()) {

                        }

                        if(chkbx_dist_miles.isChecked()) {

                        }

                        if(chkbx_dist_feet.isChecked()) {

                        }

                        if(chkbx_meterPerSec.isChecked()) {

                        }
                        if(chkbx_kmh.isChecked()) {
                            metric_speed = (int) ((raw_speed * 3600) / 1000);
                            strCurrentSpeed = String.valueOf(metric_speed);
                            tv_speed.setText(strCurrentSpeed+" km/h");
                        }
                        if(chkbx_mph.isChecked()) {
                            mph_speed=(int) (raw_speed*2.2369);
                            strCurrentSpeed = String.valueOf(mph_speed);
                            tv_speed.setText(strCurrentSpeed+" mph");                        }
                        if(chkbx_minPermile.isChecked()) {
                        }
                    }
                }
                currSpeed = location.getSpeed();
                updateSpeed();
            }

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
        tv_alt = findViewById(R.id.tv_alt);
        sw_metric = findViewById(R.id.sw_metric);
        sw_fontsize = findViewById(R.id.sw_fontsize);
        sw_test = findViewById(R.id.sw_test);
        sw_pause = findViewById(R.id.sw_pause);

        help_button = findViewById(R.id.help_button);
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


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }

        }

//        sw_metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Group3.this.updateSpeed();
//            }
//        });

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
                    if (useMetricUnits()) {
                        tv_speed.setText(speedText + " km/h");
                    } else {
                        tv_speed.setText(speedText + " mph");
                    }
                } else {
                    updateSpeed();
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
                            updateSpeed();
                            double kmSpeed = (10 * 1.609);
                            int int_kmSpeed = (int) kmSpeed;
                            String speedText = "10";
                            if (useMetricUnits()) {
                                tv_speed.setText(String.valueOf(int_kmSpeed) + " km/h");
                            } else {
                                tv_speed.setText(speedText + " mph");
                            }
//                            tv_speed.setText("10" + " mph");
                        }
                    }, 1000 * a);
                    lat = lat + 4.47038888888889;
                }
                strCurrentSpeed = "0";
                tv_speed.setText(strCurrentSpeed + " mph");
            }
        });
    }
    private boolean useMetricUnits() {
        return sw_metric.isChecked();
    }
    private boolean bigFont() {
        return sw_fontsize.isChecked();
    }
    private boolean isPaused() {
        return sw_pause.isChecked();
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








