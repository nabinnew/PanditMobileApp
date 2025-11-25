package com.example.saradha.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saradha.R;
import com.example.saradha.databinding.ActivityPanditBookRequestBinding;
import com.example.saradha.utils.UtilsFunctions;

public class PanditBookRequestActivity extends AppCompatActivity {
    ActivityPanditBookRequestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanditBookRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        UtilsFunctions.panditnav(this, binding.navView, R.id.navigation_profile);

    }
}