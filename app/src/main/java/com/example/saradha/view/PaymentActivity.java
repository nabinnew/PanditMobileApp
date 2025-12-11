package com.example.saradha.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saradha.MainActivity;
import com.example.saradha.databinding.ActivityPaymentBinding;
import com.example.saradha.model.PaymentRequest;
import com.example.saradha.model.ResponseBody;
import com.example.saradha.utils.App;
import com.example.saradha.utils.Constants;
import com.example.saradha.utils.NotificationUtils;
import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    EsewaConfiguration eSewaConfiguration;
    final String client_id= "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R" ;
    final String client_secret= "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";

    int REQUEST_CODE_PAYMENT = 1001;
    String TAG = "payment";
    String callbackUrl = "https://somecallbackurl.com";
    String bookingDate;
    String bookingTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

             eSewaConfiguration = new EsewaConfiguration(
                    client_id,
           client_secret,
            EsewaConfiguration.ENVIRONMENT_TEST
            );



            binding.cardEsewa.setOnClickListener(v ->
            {
                    Intent intent = getIntent();
        int price = intent.getIntExtra("panditPrice", 0);
        String productName = intent.getStringExtra("panditName");
        String productID = intent.getStringExtra("panditID");
                bookingDate = intent.getStringExtra("selectedDate");
         bookingTime = intent.getStringExtra("selectedTime");




        Log.i(TAG, "Proof of Payment " + price +" name "+ productName +" id "+ productID);

        String amount = String.valueOf(price);


        if (amount.equals("0") || productName == null || productID.equals("0")) {
                    Toast.makeText(this, "Invalid payment details", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Proof of Payment " + amount +" name "+ productName +" id "+ productID);

                    return;
                }

                makePayment(amount, productName, productID);

            });



    }

    private void makePayment(String amount, String productName, String productID) {
        // Create additional params map - put your callback_url here or leave empty if you already passed it as param
        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put("callback_url", callbackUrl);

        EsewaPayment eSewaPayment = new EsewaPayment(
                amount,
                productName,
                productID,
                callbackUrl,
                additionalParams
        );

        Intent intent = new Intent(PaymentActivity.this, EsewaPaymentActivity.class);
        intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
        intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

// Handle payment result here

@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_PAYMENT) {
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) return;
            String message = data.getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
            Log.i(TAG, "Proof of Payment " + message);
            savePayment(message);
             Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();



        } else if (resultCode == Activity.RESULT_CANCELED) {

            Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show();
        } else if (resultCode == EsewaPayment.RESULT_EXTRAS_INVALID) {
            if (data == null) return;

            String message = data.getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
            Toast.makeText(this, "Server Error try later", Toast.LENGTH_SHORT).show();

        }
    }
}

    public void savePayment(String message){
        try {
            SharedPreferences pref = this.getSharedPreferences(Constants.cache, Context.MODE_PRIVATE);

            JSONObject json = new JSONObject(message);
            String user_id =  pref.getString("id", null);

            // Extract main fields
            String panditid = json.optString("productId");
            String productName = json.optString("productName");
            String totalAmount = json.optString("totalAmount");

            // Extract nested fields from transactionDetails
            JSONObject transactionDetails = json.optJSONObject("transactionDetails");
            String status = "";
            String referenceId = "";
            String paymentdate = "";

            if (transactionDetails != null) {
                status = transactionDetails.optString("status");
                referenceId = transactionDetails.optString("referenceId");
                paymentdate = transactionDetails.optString("date");
            }

            // Log extracted values
            Log.i(TAG, "Product ID: " + panditid);
            Log.i(TAG, "Product Name: " + productName);
            Log.i(TAG, "Total Amount: " + totalAmount);
            Log.i(TAG, "Status: " + status);
            Log.i(TAG, "Reference ID: " + referenceId);
            Log.i(TAG, "PaymentDate: " + paymentdate);
            Log.i(TAG,"pandit booking date: "+bookingDate+"time: "+bookingTime);

//            call api to save these data
            saveBooking(panditid, productName, totalAmount, status, referenceId,
                    paymentdate, bookingDate, bookingTime);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to parse payment JSON: " + e.getMessage());
        }

    }

public void home(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
}

    private void saveBooking(String panditid, String productName,
                             String totalAmount, String status, String referenceId,
                             String paymentDate, String bookingDate, String bookingTime) {


        SharedPreferences prefs = getSharedPreferences(Constants.cache, MODE_PRIVATE);
        String token = prefs.getString("token", "");


// Log it to verify
            Log.i("TAG", panditid);
        Log.i("TAG", "Product Name: " + productName);
        Log.i("TAG", "Total Amount: " + totalAmount);
        Log.i("TAG", "Status: " + status);
        Log.i("TAG", "Reference ID: " + referenceId);
        Log.i("TAG", "Payment Date: " + paymentDate);
        Log.i("TAG", "Booking Date: " + bookingDate);
        Log.i("TAG", "Booking Time: " + bookingTime);


                    PaymentRequest payment = new PaymentRequest(
                            panditid,  productName, totalAmount,
                status, referenceId, paymentDate, bookingDate, bookingTime
        );

        App.api.savePayment("Bearer " + token, payment)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i("TAG", "Response received 1"+response);
                        Log.i("TAG", response.toString());
                        if (response.isSuccessful()) {

                            Gson gson = new Gson();
                            String jsonString = gson.toJson(response.body());
                            Log.i("TAG", "✅ RESPONSE PARSED = " + jsonString);

                            Log.i("TAG", "Payment saved successfully!");
                            Toast.makeText(getApplicationContext(), "Payment saved successfully", Toast.LENGTH_SHORT).show();
                            NotificationUtils.showNotification(getApplicationContext(), "Booking Successful", "Your booking for pandit is confirmed!");

                            home();
                        } else {
                            Log.e("TAG", "Failed to save payment: " + response.code());
                            Toast.makeText(getApplicationContext(), "Failed:1 " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("TAG", "Error saving payment: " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: 2" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

