package com.example.saradha.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.saradha.R;
import com.example.saradha.adapters.UserNotificationAdapter;
import com.example.saradha.databinding.ActivityPanditNotificationBinding;
import com.example.saradha.model.NotificationModel;
import com.example.saradha.model.NotificationResponce;
import com.example.saradha.utils.App;
import com.example.saradha.utils.UtilsFunctions;
import com.example.saradha.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanditNotificationActivity extends AppCompatActivity {
    ActivityPanditNotificationBinding binding;
    UserNotificationAdapter adapter;
    List<NotificationModel> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanditNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        UtilsFunctions.panditnav(this, binding.navView, R.id.navigation_profile);
        setupRecyclerView();
        loadNotifications();
    }

    private void setupRecyclerView() {
        binding.recyclerNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserNotificationAdapter(this, notificationList);
        binding.recyclerNotifications.setAdapter(adapter);
    }

    private void loadNotifications() {

        SharedPreferences prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        App.api.getNotifications("Bearer " + token).enqueue(new Callback<NotificationResponce>() {
            @Override
            public void onResponse(Call<NotificationResponce> call, Response<NotificationResponce> response) {
                Log.i("TAG", "✅ RESPONSE RECEIVED " + response);

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isSuccess()) {
                        Gson json = new Gson();
                        String jsonString = json.toJson(response.body());
                        Log.i("TAG", "✅ RESPONSE PARSED = " + jsonString);
                        notificationList.clear();
                        notificationList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(PanditNotificationActivity.this, "No notifications found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponce> call, Throwable t) {
                Toast.makeText(PanditNotificationActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}