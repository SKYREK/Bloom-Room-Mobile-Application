package com.example.bloomroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.navigation.ui.NavigationUI;

import com.example.bloomroom.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.navView, navController);
        Intent intent = getIntent();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        if (intent != null && intent.hasExtra("selectedTab")) {
            int selectedTab = intent.getIntExtra("selectedTab", 0);
            // Select the desired tab based on the value received from the intent
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(selectedTab).getItemId());
        }
    }

}