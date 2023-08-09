package com.example.bloomroom;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bloomroom.databinding.ActivityAdminHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {

    private ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin_home);
        NavigationUI.setupWithNavController(binding.navView, navController);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedTab")) {
            int selectedTab = intent.getIntExtra("selectedTab", 0);
            // Select the desired tab based on the value received from the intent
            navView.setSelectedItemId(navView.getMenu().getItem(selectedTab).getItemId());
        }
    }

}