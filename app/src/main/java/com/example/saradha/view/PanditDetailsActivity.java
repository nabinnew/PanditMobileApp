package com.example.saradha.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.saradha.R;
import com.example.saradha.databinding.ActivityPanditDetailsBinding;

import java.util.Calendar;

public class PanditDetailsActivity extends AppCompatActivity {

    private ActivityPanditDetailsBinding binding;
    String id;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanditDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get data from intent
        Intent intent = getIntent();
         id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
         price = intent.getIntExtra("price", 0);

        String photo = intent.getStringExtra("photo");
        String description = intent.getStringExtra("description");
        String experience = intent.getStringExtra("experience");

        String location = intent.getStringExtra("location");
         String phone = intent.getStringExtra("phone");

        // Set data to views
        binding.name.setText(name);
        binding.price.setText("Rs." + price);
        binding.description.setText(description);
        binding.experience.setText(experience);
         binding.location.setText(location);

        // Load image using Glide
        Glide.with(this)
                .load(photo)
                .placeholder(R.drawable.pandit) // default placeholder
                .into(binding.image);

        // Book Now button click
        binding.btnBookNow.setOnClickListener(v -> {

            // Step 1: Open Date Picker
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view1, year, month, dayOfMonth) -> {

                        // Format date
                        String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                        // Step 2: After date → open time picker
                        openTimePicker(selectedDate);

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Disable past dates
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis());

            datePicker.show();
        });



        // Call Now button click
        binding.btnCallNow.setOnClickListener(v -> {
            if (phone != null && !phone.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });
    }
    private void openTimePicker(String selectedDate) {

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePicker = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {

                    String selectedTime = String.format("%02d:%02d", hourOfDay, minute);

                    // After choosing date & time → go to PaymentActivity
                    openPaymentActivity(selectedDate, selectedTime);

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        timePicker.show();
    }
    private void openPaymentActivity(String date, String time) {

        Intent paymentIntent = new Intent(this, PaymentActivity.class);

        // Your existing data
        paymentIntent.putExtra("panditName", binding.name.getText().toString());
        paymentIntent.putExtra("panditPrice", price);
        paymentIntent.putExtra("panditID", id);

        // New data
        paymentIntent.putExtra("selectedDate", date);
        paymentIntent.putExtra("selectedTime", time);

        startActivity(paymentIntent);
    }


}
