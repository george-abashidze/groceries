package com.example.groceries;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.groceries.databinding.ActivityMainBinding;
import com.example.groceries.ui.manager_activity.ManagerActivity;
import com.example.groceries.ui.user_activity.UserActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        binding.btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagerActivity.Start(MainActivity.this);
            }
        });

        binding.btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserActivity.Start(MainActivity.this);
            }
        });
    }
}