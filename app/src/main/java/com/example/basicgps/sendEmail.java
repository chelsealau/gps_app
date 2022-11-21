package com.example.basicgps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class sendEmail extends AppCompatActivity {
    AppCompatButton back_button, send_button;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email);
        email = findViewById(R.id.email_addr);
        back_button = findViewById(R.id.back_button);
        send_button = findViewById(R.id.send_button);
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
//    AppCompatButton back_button;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.send_email);
//
//        back_button = findViewById(R.id.back_button);
//
//
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(sendEmail.this, Group3.class);
//                startActivity(intent);
//            }
//        });
//        Intent main = getIntent();
//    }
//}



//
//public class getHighScore extends AppCompatActivity {
//    AppCompatButton back_button;
//    TextView max_speed, max_distance, max_time;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_high_score);
//
//        back_button = findViewById(R.id.back_button);
//        max_distance = (TextView) findViewById(R.id.max_dist_val);
//        max_time = (TextView) findViewById(R.id.max_time_val);
//        max_speed = (TextView) findViewById(R.id.max_speed_val);
//
//        back_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getHighScore.this, Group3.class);
//                startActivity(intent);
//            }
//        });
//        Intent main = getIntent();
//        String distance = main.getStringExtra("dist");
//        String speed = main.getStringExtra("speed");
//        String time = main.getStringExtra("time");
//
//        max_time.setText(time);
//        max_speed.setText(speed);
//        max_distance.setText(distance);
//
////        String speed = main.getStringExtra("speed");
////        String time = main.getStringExtra("time");
////        Toast.makeText(getHighScore.this, distance, Toast.LENGTH_LONG).show();
////        if (Double.parseDouble(distance) > Double.parseDouble((String) max_distance.getText())){
////            max_distance.setText(distance);
////        }
//
////        Toast.makeText(getHighScore.this, "distance", Toast.LENGTH_LONG).show();
//    }
//}