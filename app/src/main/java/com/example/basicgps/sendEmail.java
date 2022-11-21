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

public class sendEmail extends AppCompatActivity {
    private String subject = "All Database Data";
    private String body = "Find Database info in attachment";

    AppCompatButton back_button, send_button;
    EditText email;

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
                if (!email.getText().toString().isEmpty()) {
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

