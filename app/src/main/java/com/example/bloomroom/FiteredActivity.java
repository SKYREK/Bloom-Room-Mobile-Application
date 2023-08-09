package com.example.bloomroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bloomroom.Adaptors.FlowerAdapter;
import com.example.bloomroom.Models.Flower;

import java.util.List;

public class FiteredActivity extends AppCompatActivity {
    public static final String CATEGORY = "category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitered);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
        String category = getIntent().getStringExtra(CATEGORY);
        TextView title = findViewById(R.id.titleView);
        title.setText(category);
        RecyclerView recyclerView = findViewById(R.id.categorized_recylcer);
        Flower.getFlowersByCategory(category, new Flower.FlowerCallback() {
            @Override
            public void onFlowersLoaded(List<Flower> flowerList) {
                recyclerView.setLayoutManager(new GridLayoutManager(FiteredActivity.this, 2));
                recyclerView.setAdapter(new FlowerAdapter(flowerList, FiteredActivity.this));
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}