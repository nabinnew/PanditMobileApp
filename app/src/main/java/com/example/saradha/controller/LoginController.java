package com.example.saradha.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.saradha.MainActivity;
import com.example.saradha.model.LoginRequest;
import com.example.saradha.model.LoginResponse;
import com.example.saradha.model.UserProfileResponse;
import com.example.saradha.utils.App;
import com.example.saradha.utils.Constants;
import com.example.saradha.view.PanditDashboardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {

    private Context context;

    public LoginController(Context context) {
        this.context = context;
    }

    public void loginUser(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        App.api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i("LoginResponse", "Response received");

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess() && loginResponse.getUser() != null) {

                        // Save token & user info
                        SharedPreferences prefs = context.getSharedPreferences(Constants.cache, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("token", loginResponse.getToken() != null ? loginResponse.getToken() : "");


                        editor.putString("phone", loginResponse.getUser().getPhone() != null ? loginResponse.getUser().getPhone() : "");
                        editor.putString("address", loginResponse.getUser().getAddress() != null ? loginResponse.getUser().getAddress() : "");


                        editor.putString("userName", loginResponse.getUser().getName() != null ? loginResponse.getUser().getName() : "");
                        editor.putString("userEmail", loginResponse.getUser().getEmail() != null ? loginResponse.getUser().getEmail() : "");
                        editor.putString("userId", loginResponse.getUser().getId() != null ? loginResponse.getUser().getId() : "");
                        editor.putString("role", loginResponse.getUser().getRole() != null ? loginResponse.getUser().getRole() : "");
                        editor.apply();

                        Log.i("LoginController", "Token saved: " + (loginResponse.getToken() != null ? loginResponse.getToken() : "null"));

                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();

                        // Navigate based on role
                        String role = loginResponse.getUser().getRole() != null ? loginResponse.getUser().getRole() : "";
                        Log.i("LoginController", "Role: " + role);

                        Intent intent;
                        if ("pandit".equalsIgnoreCase(role)) {
                            intent = new Intent(context, PanditDashboardActivity.class);
                        } else {
                            intent = new Intent(context, MainActivity.class);
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                    } else {
                        Toast.makeText(context, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LoginController", "Login failed: " + loginResponse.getMessage());
                    }
                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                    Log.e("LoginController", "Response unsuccessful: " + (response != null ? response.message() : "null response"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
