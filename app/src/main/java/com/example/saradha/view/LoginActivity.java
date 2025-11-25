package com.example.saradha.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.saradha.MainActivity;
import com.example.saradha.R;
import com.example.saradha.controller.LoginController;
import com.example.saradha.databinding.ActivityLoginBinding;
import com.example.saradha.databinding.ActivitySplashBinding;

public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
LoginController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new LoginController(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            // Basic validation
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call login method from controller
            controller.loginUser(email, password);
        });
        binding.textSignUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            finish();
        });
    }
}