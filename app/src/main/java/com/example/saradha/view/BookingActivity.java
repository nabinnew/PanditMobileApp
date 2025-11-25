package com.example.saradha.view;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saradha.R;
import com.example.saradha.adapters.PanditBookedAdapter;
import com.example.saradha.databinding.ActivityBookingBinding;
import com.example.saradha.model.PanditBooked;
import com.example.saradha.model.PanditBookedResponce;
import com.example.saradha.utils.App;
import com.example.saradha.utils.Constants;
import com.example.saradha.utils.UtilsFunctions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {
ActivityBookingBinding binding  ;
    PanditBookedAdapter adapter;
    List<PanditBooked> panditList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        UtilsFunctions.setupBottomNav(this, binding.navView, R.id.navigation_booking);
        binding.recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        loadPandits();

    }

    private void loadPandits() {

        String token = getSharedPreferences(Constants.cache, MODE_PRIVATE)
                .getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token not found", Toast.LENGTH_SHORT).show();
            return;
        }

        App.api.getBookedPandits("Bearer " + token)
                .enqueue(new Callback<PanditBookedResponce>() {
                    @Override
                    public void onResponse(Call<PanditBookedResponce> call, Response<PanditBookedResponce> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            panditList = response.body().getData();

                            adapter = new PanditBookedAdapter(BookingActivity.this, panditList);
                            binding.recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<PanditBookedResponce> call, Throwable t) {
                        Toast.makeText(BookingActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}