package com.example.basicgps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.basicgps.database.GPSDatabase;
import com.example.basicgps.database.entities.Metric;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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


public class sendEmail extends AppCompatActivity {
    private String subject = "All Database Data";
    private String body = "Find Database info in attachment";

    AppCompatButton back_button, send_button;
    EditText email;
    double num_vals, avg_alt, avg_long, avg_lat, avg_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email);
        back_button = findViewById(R.id.back_button);
        email = findViewById(R.id.email_addr);
        send_button = findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    }
                });
                if (!email.getText().toString().isEmpty()) {
                    body = String.format("Average Altitude: "+"%.4f",avg_alt) + String.format("\nAverage Speed: "+"%.4f",avg_speed) + (String.format("\nAverage Longitude: "+"%.4f",avg_long)) + (String.format("\nAverage Latitude: "+"%.4f",avg_lat));
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(Intent.createChooser(intent, "Send Email"));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(sendEmail.this, "There is no application that support this action", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(sendEmail.this, "please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sendEmail.this, Group3.class);
                startActivity(intent);
            }
        });
        Intent main = getIntent();
    }
}

