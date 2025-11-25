package com.example.saradha.utils;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class App extends Application {

    public static RestApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofit();
    }

    private void initRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create()) // first for raw String
                .addConverterFactory(GsonConverterFactory.create(gson))  // then Gson
                .build();

        api = retrofit.create(RestApi.class);
    }
}
