package com.example.saradha.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.saradha.MainActivity;
import com.example.saradha.R;
import com.example.saradha.model.ResponseBody;
import com.example.saradha.model.UserModel;
import com.example.saradha.view.BookingActivity;
import com.example.saradha.view.NotificationActivity;
import com.example.saradha.view.PanditDashboardActivity;
import com.example.saradha.view.PanditNotificationActivity;
import com.example.saradha.view.PanditProfileActivity;
import com.example.saradha.view.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.function.LongFunction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UtilsFunctions {

        public static void setupBottomNav(Activity activity, BottomNavigationView navView, int currentItemId) {

             navView.post(() -> navView.setSelectedItemId(currentItemId));

            navView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.navigation_home && currentItemId != R.id.navigation_home) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.overridePendingTransition(0, 0);
                    activity.finish();
                    return true;
                }

                if (id == R.id.navigation_notifications && currentItemId != R.id.navigation_notifications) {
                    activity.startActivity(new Intent(activity, NotificationActivity.class));
                    activity.overridePendingTransition(0, 0);
                    activity.finish();
                    return true;
                }

                if (id == R.id.navigation_booking && currentItemId != R.id.navigation_booking) {
                    activity.startActivity(new Intent(activity, BookingActivity.class));
                    activity.overridePendingTransition(0, 0);
                    activity.finish();
                    return true;
                }

                if (id == R.id.navigation_profile && currentItemId != R.id.navigation_profile) {
                    activity.startActivity(new Intent(activity, ProfileActivity.class));
                    activity.overridePendingTransition(0, 0);
                    activity.finish();
                    return true;
                }

                return true;
            });
        }



        public static void panditnav(Activity activity, BottomNavigationView navView, int currentItemId) {

            // Highlight current item safely
            navView.post(() -> navView.setSelectedItemId(currentItemId));

            navView.setOnItemSelectedListener(item -> {

                int id = item.getItemId();

                // Prevent reloading same activity
                if (id == currentItemId) {
                    return false;
                }

                Intent intent = null;

                if (id == R.id.navigation_home) {
                    intent = new Intent(activity, PanditDashboardActivity.class);
                }
                else if (id == R.id.navigation_notifications) {
                    intent = new Intent(activity, PanditNotificationActivity.class);
                }
                else if (id == R.id.panditprofile) {
                    intent = new Intent(activity, PanditProfileActivity.class);
                }

                if (intent != null) {
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                    activity.finish();
                }

                return true;
            });
        }


    public static void fetchProfile(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(Constants.cache, Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(context, "Token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        App.api.getUserProfile("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("TAG", "✅ RESPONSE RECEIVED " + response);
                if (response.isSuccessful() && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getUser() != null) {

                    UserModel user = response.body().getUser();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("userId", safe(user.getId()));
                    editor.putString("userName", safe(user.getName()));
                    editor.putString("userEmail", safe(user.getEmail()));
                    editor.putString("phone", safe(user.getPhone()));
                    editor.putString("address", safe(user.getAddress()));
                    editor.putString("role", safe(user.getRole()));
                    editor.apply();

                } else {
                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }


    }

