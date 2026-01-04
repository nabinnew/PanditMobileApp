package com.example.saradha.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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

        String imageUrl = prefs.getString("profile_image", null);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl) // URL from SharedPreferences
                    .placeholder(R.drawable.pandit) // optional placeholder
                    .error(R.drawable.pandit)       // optional error image
                    .circleCrop()                             // optional circular crop
                    .into(binding.profilePicture);            // your ImageView
        } else {
            // If no image saved, show default
            binding.profilePicture.setImageResource(R.drawable.pandit);
        }


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
            Intent intent = new Intent(this, TermsAndConditionActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Terms & Conditions", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void navigateToPrivacy() {
        try {
            Intent intent = new Intent(this, Activity_privacyActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Privacy Policy", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void navigateToRate() {
        try {
            Intent intent = new Intent(this, activity_rate.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Rate App", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void navigateToAbout() {
        try {
            Intent intent = new Intent(this, activity_about.class);
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