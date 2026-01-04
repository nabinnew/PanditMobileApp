package com.example.saradha.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saradha.R;
import com.example.saradha.controller.ProfileController;
import com.example.saradha.databinding.ActivityProfileBinding;
import com.example.saradha.utils.UtilsFunctions;
import com.example.saradha.utils.Constants;

public class ProfileActivity extends AppCompatActivity {

    private Uri selectedImageUri;
    private SharedPreferences prefs;
    private ActivityProfileBinding binding;
    private ProfileController controller;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize components
        prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);
        controller = new ProfileController(this);

        // Setup UI
        UtilsFunctions.fetchProfile(this);
        UtilsFunctions.setupBottomNav(this, binding.navView, R.id.navigation_profile);

        String name = prefs.getString("userName", "");
        if (name != null && !name.isEmpty()) {
            binding.appTitle.setText(name);
        }

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            controller.setSelectedImageUri(selectedImageUri);
                            Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        setupClickListeners();
    }

    private void setupClickListeners() {
        // Logout Card
        binding.Logout.setOnClickListener(view -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Pandit Card
        binding.panditcard.setOnClickListener(view -> {
            if (controller != null) {
                controller.showAddPanditDialog(imagePickerLauncher);
            }
        });

        // View Profile Card
        binding.viewprofile.setOnClickListener(view -> {
            if (controller != null) {
                controller.viewProfile();
            }
        });

        // Terms & Conditions Card
        binding.termsCard.setOnClickListener(view -> navigateToTerms());

        // Privacy Policy Card
        binding.privacyCard.setOnClickListener(view -> navigateToPrivacy());


        // Rate App Card
        binding.rateCard.setOnClickListener(view -> navigateToRate());

        // About Us Card
        binding.aboutCard.setOnClickListener(view -> navigateToAbout());
    }

    private void navigateToTerms() {
        try {
            Intent intent = new Intent(ProfileActivity.this, TermsAndConditionActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Terms & Conditions", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void navigateToPrivacy() {
        try {
            Intent intent = new Intent(ProfileActivity.this, Activity_privacyActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Privacy Policy", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void navigateToRate() {
        try {
            Intent intent = new Intent(ProfileActivity.this, activity_rate.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Rate App", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void navigateToAbout() {
        try {
            Intent intent = new Intent(ProfileActivity.this, activity_about.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open About Us", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up binding
        binding = null;
    }
}