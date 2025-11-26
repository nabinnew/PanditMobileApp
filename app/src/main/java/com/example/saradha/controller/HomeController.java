package com.example.saradha.controller;


import android.util.Log;

import com.example.saradha.model.PanditModel;
import com.example.saradha.model.PanditResponse;
import com.example.saradha.utils.App;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeController {

    public interface PanditCallback {
        void onSuccess(List<PanditModel> list);
        void onError(String error);
    }

    public void fetchPandits(PanditCallback callback) {

        App.api.getPandits().enqueue(new Callback<PanditResponse>() {
            @Override
            public void onResponse(Call<PanditResponse> call, Response<PanditResponse> response) {
                Log.d("TAG", "onResponse:IS  " + response);


                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.i("TAG",json);
                    callback.onSuccess(response.body().pandits);
                } else {
                    callback.onError("Invalid Response");
                }
            }

            @Override
            public void onFailure(Call<PanditResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
                callback.onError(t.getLocalizedMessage());
            }
        });
    }
}
