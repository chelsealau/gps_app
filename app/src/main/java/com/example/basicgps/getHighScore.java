package com.example.basicgps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.w3c.dom.Text;

public class getHighScore extends AppCompatActivity {
    AppCompatButton back_button;
    TextView max_speed, min_speed, max_distance, min_distance, max_time, min_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_high_score);

        back_button = findViewById(R.id.back_button);
        max_distance = findViewById(R.id.max_dist_val);
        min_distance = findViewById(R.id.min_dist_val);
        max_time = findViewById(R.id.max_time_val);
        min_time = findViewById(R.id.min_time_val);
        max_speed = findViewById(R.id.max_speed_val);
        min_speed = findViewById(R.id.min_speed_val);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getHighScore.this, Group3.class);
                startActivity(intent);
            }
        });
        Intent main = getIntent();
        String maxDist = main.getStringExtra("maxDist");
        String minDist = main.getStringExtra("minDist");
        String maxSpeed = main.getStringExtra("maxSpeed");
        String minSpeed = main.getStringExtra("minSpeed");
        String maxTime = main.getStringExtra("maxTime");
        String minTime = main.getStringExtra("minTime");

        Log.d("HIGH SCORE VALS", "maxS: " + maxSpeed);

        max_time.setText(maxTime);
        min_time.setText(minTime);
        max_speed.setText(maxSpeed);
        min_speed.setText(minSpeed);
        max_distance.setText(maxDist);
        min_distance.setText(minDist);
    }
}