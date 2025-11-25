package com.example.saradha.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;

import com.example.saradha.R;
import com.example.saradha.model.RegistrationRequest;
import com.example.saradha.model.ResponseBody;
import com.example.saradha.model.UserProfileResponse;
import com.example.saradha.utils.App;
import com.example.saradha.utils.Constants;
import com.example.saradha.utils.UtilsFunctions;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileController {

    private Activity activity;
    private Uri selectedImageUri;

    public ProfileController(Activity activity) {
        this.activity = activity;
    }

    public void setSelectedImageUri(Uri uri) {
        this.selectedImageUri = uri;
    }

    public void showAddPanditDialog(ActivityResultLauncher<Intent> imagePickerLauncher) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.panditadd_dialogue, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText etExperience = view.findViewById(R.id.etExperience);
        EditText etPrice = view.findViewById(R.id.etPrice);
        EditText etDesc = view.findViewById(R.id.etDesc);

        CheckBox cbSaradha = view.findViewById(R.id.cbSaradha);
        CheckBox cbBihe = view.findViewById(R.id.cbBihe);
        CheckBox cbBartabandan = view.findViewById(R.id.cbBartabandan);
        CheckBox cbRudri = view.findViewById(R.id.cbRudri);
        CheckBox cbOthers = view.findViewById(R.id.cbOthers);

        Button btnUpload = view.findViewById(R.id.btnUploadPhoto);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnSubmit.setOnClickListener(v -> {

            String experience = etExperience.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            JSONArray services = new JSONArray();
            if (cbSaradha.isChecked()) services.put("saradha");
            if (cbBihe.isChecked()) services.put("bihe");
            if (cbBartabandan.isChecked()) services.put("bartabandan");
            if (cbRudri.isChecked()) services.put("rudri");
            if (cbOthers.isChecked()) services.put("others");

            SharedPreferences prefs = activity.getSharedPreferences(Constants.cache, Activity.MODE_PRIVATE);
            String token = prefs.getString("token", "");

            uploadPanditDataWithToken(token, experience, price, desc, services);
            dialog.dismiss();
        });
    }

    private void uploadPanditDataWithToken(String token, String experience, String price, String desc, JSONArray servicesArray) {

        if (selectedImageUri == null) {
            Toast.makeText(activity, "Please select a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(selectedImageUri);
            byte[] bytes = readBytes(inputStream);

            RequestBody expBody = RequestBody.create(MediaType.parse("text/plain"), experience);
            RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
            RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
            RequestBody servicesBody = RequestBody.create(MediaType.parse("application/json"), servicesArray.toString());
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), bytes);

            MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", "pandit.jpg", imageBody);
            Log.d("UPLOAD_DEBUG", "======= DATA BEING SENT =======");
            Log.d("UPLOAD_DEBUG", "experience = " + experience);
            Log.d("UPLOAD_DEBUG", "price = " + price);
            Log.d("UPLOAD_DEBUG", "desc = " + desc);
            Log.d("UPLOAD_DEBUG", "services JSON = " + servicesArray.toString());
            Log.d("UPLOAD_DEBUG", "image bytes size = " + bytes.length + " bytes");
            Log.d("UPLOAD_DEBUG", "photo filename = pandit.jpg");
            Log.d("UPLOAD_DEBUG", "===============================");

            App.api.addPandit("Bearer " + token, expBody, priceBody, descBody, servicesBody, photoPart)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.i("UPLOAD_DEBUG", "✅ RESPONSE RECEIVED "+response);
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    // Convert response body to string
                                    Log.i("UPLOAD_DEBUG", "✅ RESPONSE ids = " + response.body().getId());
                                    Gson gson = new Gson();
                                    String jsonString = gson.toJson(response.body());
                                    Log.i("UPLOAD_DEBUG", "✅ RESPONSE PARSED = " + jsonString);
                                } else {
                                    Log.e("UPLOAD_DEBUG", "❌ HTTP CODE = " + response.code());
                                    Log.e("UPLOAD_DEBUG", "❌ MESSAGE = " + response.message());
                                    if (response.errorBody() != null) {
                                        String error = response.errorBody().string();
                                        Log.e("UPLOAD_DEBUG", "❌ ERROR BODY = " + error);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("UPLOAD_DEBUG", "❌ NETWORK ERROR : " + t.getMessage());
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(activity, "Error1: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private byte[] readBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = stream.read(data)) != -1) buffer.write(data, 0, nRead);
        return buffer.toByteArray();
    }

    public void viewProfile() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.profileviewdialogue, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText etName = view.findViewById(R.id.etName);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etAddress = view.findViewById(R.id.etAddress);
        EditText etPhone = view.findViewById(R.id.etPhone);
        Button btnSave = view.findViewById(R.id.btnSave);

        SharedPreferences prefs = activity.getSharedPreferences(Constants.cache, Activity.MODE_PRIVATE);

        String token = prefs.getString("token", "");
        String name = prefs.getString("userName", "");
        String email = prefs.getString("userEmail", "");
        String address = prefs.getString("address", "");
        String phone = prefs.getString("phone", "");

// Set values to EditTexts
        etName.setText(name);
        etEmail.setText(email);
        etAddress.setText(address);
        etPhone.setText(phone);


        Toast.makeText(activity, "Load Successful", Toast.LENGTH_SHORT).show();




        //  UPDATE PROFILE
        btnSave.setOnClickListener(v -> {

            RegistrationRequest request = new RegistrationRequest(
                    etName.getText().toString(),
                    etEmail.getText().toString(),
                    etAddress.getText().toString(),
                    etPhone.getText().toString()
            );

            App.api.updateProfile("Bearer " + token, request)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Gson json = new Gson();
                            String jsonString = json.toJson(response.body());
                            Log.i("TAG", "✅ RESPONSE PARSED = " + jsonString);
                            if (response.isSuccessful()) {

                                Toast.makeText(activity, "Profile Updated", Toast.LENGTH_SHORT).show();
                                UtilsFunctions.fetchProfile(activity);

                                dialog.dismiss();
                            } else {
                                Toast.makeText(activity, "Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

}

