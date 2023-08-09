package com.example.bloomroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class CompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        Button button = findViewById(R.id.loadHome);
        button.setOnClickListener(v -> {
            startActivity(new Intent(this,HomeActivity.class));
        });

    }
}