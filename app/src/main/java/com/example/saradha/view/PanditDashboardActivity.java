package com.example.saradha.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.saradha.R;
import com.example.saradha.adapters.PanditAdapter;
import com.example.saradha.databinding.ActivityPanditDashboardBinding;
import com.example.saradha.model.Pandit;
import com.example.saradha.model.ResponseBody;
import com.example.saradha.utils.App;
import com.example.saradha.utils.UtilsFunctions;
import com.example.saradha.utils.Constants;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanditDashboardActivity extends AppCompatActivity {

    private ActivityPanditDashboardBinding binding;
    private PanditAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanditDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UtilsFunctions.panditnav(this, binding.navView, R.id.navigation_profile);

        binding.recyclerPandit.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);
//
//// Get all key-value pairs
//        for (String key : prefs.getAll().keySet()) {
//            Object value = prefs.getAll().get(key);
//            Log.i("LocalStorage", key + " = " + value);
//        }
//
//        // Load Pandits
        loadPandits();
    }

    private void loadPandits() {

        SharedPreferences prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String userId = prefs.getString("userId", ""); // get the saved user ID

        if (userId.isEmpty()) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        App.api.getPanditsbooked("Bearer " + token, userId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i("profile","Response received"+ response);
                        Log.i("profile", "API hit URL: " + call.request().url());
                        Log.i("profile",response.message());


                        if (response.isSuccessful() && response.body() != null) {
                            ResponseBody body = response.body();
                            List<Pandit> list = body.getData();
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(response.body());
                            Log.i("profile", "✅ RESPONSE PARSED = " + jsonString);
                            PanditAdapter adapter = new PanditAdapter(PanditDashboardActivity.this, list);
                            binding.recyclerPandit.setAdapter(adapter);
                            binding.recyclerPandit.setVisibility(View.VISIBLE);
                            binding.textNoData.setVisibility(View.GONE);

                        } else {
                            binding.recyclerPandit.setVisibility(View.GONE);
                            binding.textNoData.setVisibility(View.VISIBLE);
                            Toast.makeText(PanditDashboardActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        binding.recyclerPandit.setVisibility(View.GONE);
                        binding.textNoData.setVisibility(View.VISIBLE);
                        Toast.makeText(PanditDashboardActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        Log.i("profile",t.getMessage());
                        t.printStackTrace();
                    }
                });
    }
}
