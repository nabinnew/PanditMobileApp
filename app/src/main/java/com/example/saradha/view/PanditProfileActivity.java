package com.example.saradha.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saradha.R;
import com.example.saradha.controller.ProfileController;
import com.example.saradha.databinding.ActivityPanditProfileBinding;
import com.example.saradha.utils.UtilsFunctions;
import com.example.saradha.utils.Constants;

public class PanditProfileActivity extends AppCompatActivity {
    ActivityPanditProfileBinding binding;
    SharedPreferences prefs;
    ProfileController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UtilsFunctions.fetchProfile(this);

        prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);

        UtilsFunctions.panditnav(this, binding.navView, R.id.navigation_profile);
        String name = prefs.getString("userName", "");

        binding.txtname.setText("pandit."+name);


        binding.Logout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        controller = new ProfileController(this);

        binding.viewprofile.setOnClickListener(view -> controller.viewProfile());

    }
}