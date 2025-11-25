package com.example.saradha.utils;

import com.example.saradha.model.LoginRequest;
import com.example.saradha.model.LoginResponse;
import com.example.saradha.model.NotificationResponce;
import com.example.saradha.model.PanditBookedResponce;
import com.example.saradha.model.PanditResponse;
import com.example.saradha.model.PaymentRequest;
import com.example.saradha.model.RegistrationRequest;
import com.example.saradha.model.RegistrationResponse;
import com.example.saradha.model.ResponseBody;
import com.example.saradha.model.UserProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestApi {
    @GET("pandits")
    Call<PanditResponse> getPandits();

    @POST("users")
    Call<RegistrationResponse> registerUser(@Body RegistrationRequest request);

    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/bookings")
    Call<ResponseBody> savePayment(
            @Header("Authorization") String token,
            @Body PaymentRequest payment
    );

    @GET("/api/bookings/panditbook/{id}")
    Call<ResponseBody> getPanditsbooked(
            @Header("Authorization") String token,
            @Path("id") String userId
    );

    @Multipart
    @POST("pandits")
    Call<ResponseBody> addPandit(
            @Header("Authorization") String token,
            @Part("experience") RequestBody experience,
            @Part("price") RequestBody price,
            @Part("description") RequestBody desc,
            @Part("services") RequestBody services,
            @Part MultipartBody.Part photo
    );

    //    view profile and updatee
    @GET("users")
    Call<UserProfileResponse> getProfile(@Header("Authorization") String token);

    @PUT("users/update")
    Call<ResponseBody> updateProfile(
            @Header("Authorization") String token,
            @Body RegistrationRequest body
    );

    @GET("notifications/user")
    Call<NotificationResponce> getNotifications(
            @Header("Authorization") String token
    );

    @GET("users/single")
    Call<ResponseBody> getUserProfile(
            @Header("Authorization") String token
    );

        @GET("bookings/userbooking")
            // change endpoint if different
        Call<PanditBookedResponce> getBookedPandits(
                @Header("Authorization") String token);

}
