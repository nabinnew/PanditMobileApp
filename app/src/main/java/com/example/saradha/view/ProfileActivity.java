package com.example.saradha.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
    SharedPreferences prefs;
    private ActivityProfileBinding binding;
    private ProfileController controller;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UtilsFunctions.fetchProfile(this);


        prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);
        controller = new ProfileController(this);

        UtilsFunctions.setupBottomNav(this, binding.navView, R.id.navigation_profile);

        String name = prefs.getString("userName", "");
        binding.appTitle.setText(name);

        // ✅ Proper launcher init
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        controller.setSelectedImageUri(selectedImageUri);
                        Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
                    }
                });

        binding.Logout.setOnClickListener(view -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        binding.panditcard.setOnClickListener(view -> {
            controller.showAddPanditDialog(imagePickerLauncher);
        });

        binding.viewprofile.setOnClickListener(view -> controller.viewProfile());
    }





}
