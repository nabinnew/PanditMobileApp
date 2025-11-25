package com.example.saradha.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saradha.MainActivity;
import com.example.saradha.R;
import com.example.saradha.databinding.ActivitySplashBinding;
import com.example.saradha.utils.Constants;
import com.example.saradha.utils.UtilsFunctions;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UtilsFunctions.fetchProfile(this);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);

            String token = prefs.getString("token", null);
            String role = prefs.getString("role", null);
            Log.i("SplashActivity", "Token: " + token + ", Role: " + role);

            Intent intent = null;

            if (token != null && !token.isEmpty()) {
                // Token exists, decide based on role
                if ("pandit".equals(role)) {
                    try {
                        intent = new Intent(SplashActivity.this, PanditDashboardActivity.class);
                    } catch (Exception e) {
                        Log.e("SplashActivity", "PanditDashboardActivity not found!", e);
                    }
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
            } else {
                // No token, go to login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                finish();
            } else {
                Log.e("SplashActivity", "Intent is null, cannot navigate!");
            }

        }, 1500);
    }
    }
