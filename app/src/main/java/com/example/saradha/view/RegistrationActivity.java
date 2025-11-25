package com.example.saradha.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saradha.controller.RegistrationController;
import com.example.saradha.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private RegistrationController registrationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         registrationController = new RegistrationController(this);

         binding.btnRegister.setOnClickListener(view -> {
            String name = binding.name.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String phone = binding.phone.getText().toString().trim();
            String address = binding.address.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

             if (TextUtils.isEmpty(name)) {
                binding.name.setError("Full name is required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                binding.email.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                binding.phone.setError("Phone number is required");
                return;
            }
            if (TextUtils.isEmpty(address)) {
                binding.address.setError("Address is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.password.setError("Password is required");
                return;
            }

            // Call API via controller
            registrationController.registerUser(name, email, phone, address, password);
        });

        // Go to Login Activity
        binding.textSignIn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
