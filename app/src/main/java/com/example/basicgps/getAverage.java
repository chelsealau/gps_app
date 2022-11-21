package com.example.basicgps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.basicgps.database.GPSDatabase;
import com.example.basicgps.database.entities.Metric;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class getAverage extends AppCompatActivity {
    AppCompatButton back_button;
    TextView speed, alt, lon, lat;
    Spinner speed_spinner;

    double num_vals, avg_alt, avg_long, avg_lat, avg_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.average);

        back_button = findViewById(R.id.back_button);
        speed = (TextView) findViewById(R.id.speed_val);
        alt = (TextView) findViewById(R.id.alt_val);
        lon = (TextView) findViewById(R.id.long_val);
        lat = (TextView) findViewById(R.id.lat_val);

        // spinners
        speed_spinner = (Spinner) findViewById(R.id.speed_spinner);
        String[] items = new String[]{"m/s", "km/h", "mph", "min/mile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        speed_spinner.setAdapter(adapter);

        speed_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        speed.setText(String.format("%.4f",avg_speed));
                        break;
                    case 1:
                        speed.setText(String.format("%.4f",((avg_speed * 3600) / 1000)));
                        break;
                    case 2:
                        speed.setText(String.format("%.4f",(avg_speed * 2.2369)));
                        break;
                    case 3:
                        speed.setText(String.format("%.4f",(avg_speed / 26.822)));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getAverage.this, Group3.class);
                startActivity(intent);
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<Metric> metrics = GPSDatabase.getInstance(getApplicationContext()).metricDAO().getAllMetrics();

                List<Double> alt_list = metrics.stream().map(Metric::getAltitude).collect(Collectors.toList());
                List<Double> long_list = metrics.stream().map(Metric::getLongitude).collect(Collectors.toList());
                List<Double> lat_list = metrics.stream().map(Metric::getLatitude).collect(Collectors.toList());

                List<Double> speed_list = metrics.stream().map(Metric::getSpeed).collect(Collectors.toList());

                num_vals = alt_list.size();
                avg_alt = alt_list.stream().reduce((double) 0, (a, b)->a+b)/num_vals;
                avg_long = long_list.stream().reduce((double) 0, (a, b)->a+b)/num_vals;
                avg_lat = lat_list.stream().reduce((double) 0, (a, b)->a+b)/num_vals;
                avg_speed = speed_list.stream().reduce((double) 0, (a, b)->a+b)/num_vals;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alt.setText(String.format("%.4f",avg_alt));
                        speed.setText(String.format("%.4f",avg_speed));
                        lon.setText(String.format("%.4f",avg_long));
                        lat.setText(String.format("%.4f",avg_lat));
                    }
                });
            }
        });
    }
}
