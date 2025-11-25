package com.example.saradha.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.saradha.model.RegistrationRequest;
import com.example.saradha.model.RegistrationResponse;
import com.example.saradha.utils.App;
import com.example.saradha.view.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationController {

    private Context context;

    public RegistrationController(Context context) {
        this.context = context;
    }

    /**
     * Call this function to register a new user
     */
    public void registerUser(String name, String email, String phone, String address, String password) {
        // Create request object
        RegistrationRequest request = new RegistrationRequest(name, email, phone, address, password);

        // Make Retrofit call
        App.api.registerUser(request).enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().isSuccess()) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
