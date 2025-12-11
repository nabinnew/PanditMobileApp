package com.example.saradha;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.saradha.adapters.HomeAdapter;
import com.example.saradha.controller.HomeController;
import com.example.saradha.databinding.ActivityMainBinding;
import com.example.saradha.model.PanditModel;
import com.example.saradha.utils.NotificationUtils;
import com.example.saradha.utils.UtilsFunctions;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    ActivityMainBinding binding;
    List<PanditModel> fullList = new ArrayList<>();
    HomeAdapter adapter;
    private Chip lastSelectedChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        UtilsFunctions.setupBottomNav(this, binding.navView, R.id.navigation_home);

        // Initialize notification permission launcher
        initNotificationPermissionLauncher();

        // Request notification permission
        requestNotificationPermission();

        binding.recyclerPandit.setLayoutManager(new LinearLayoutManager(this));

        HomeController controller = new HomeController();

        controller.fetchPandits(new HomeController.PanditCallback() {
            @Override
            public void onSuccess(List<PanditModel> list) {

                // Fix services array in each PanditModel
                for (PanditModel p : list) {
                    if (p.services != null && p.services.size() == 1) {
                        try {
                            JSONArray arr = new JSONArray(p.services.get(0));
                            List<String> fixed = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                fixed.add(arr.getString(i));
                            }
                            p.services = fixed;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                fullList = list;

                // Load all pandits
                adapter = new HomeAdapter(MainActivity.this, fullList);
                binding.recyclerPandit.setAdapter(adapter);

                // Generate unique chips
                generateServiceChips(fullList);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // -------------------- GENERATE UNIQUE SERVICE CHIPS --------------------
    private void generateServiceChips(List<PanditModel> list) {

        Set<String> uniqueServices = new HashSet<>();

        // Collect all unique services from all pandits
        for (PanditModel model : list) {
            if (model.services != null) {
                uniqueServices.addAll(model.services);
            }
        }

        binding.chipContainer.removeAllViews();

        // -------------------- FIRST CHIP : "ALL" --------------------
        Chip allChip = new Chip(this);
        allChip.setText("All");
        allChip.setCheckable(true);
        allChip.setClickable(true);
        allChip.setChipBackgroundColorResource(R.color.chip_color);
        allChip.setChipStrokeColorResource(R.color.black);
        allChip.setChipStrokeWidth(2f);
        allChip.setChipCornerRadius(8f);

        LinearLayout.LayoutParams allParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        allParams.setMargins(0, 0, 16, 0); // right margin 16dp
        allChip.setLayoutParams(allParams);

        allChip.setOnClickListener(v -> {
            adapter.updateList(fullList);
            highlightSelectedChip(allChip);
        });

        binding.chipContainer.addView(allChip);
        lastSelectedChip = allChip; // default selected

        // -------------------- OTHER SERVICE CHIPS --------------------
        for (String service : uniqueServices) {

            Chip chip = new Chip(this);
            chip.setText(service);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setChipBackgroundColorResource(R.color.chip_color);
            chip.setChipStrokeColorResource(R.color.black);
            chip.setChipStrokeWidth(2f);
            chip.setChipCornerRadius(8f);

            LinearLayout.LayoutParams chipParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            chipParams.setMargins(0, 0, 16, 0); // right margin 16dp
            chip.setLayoutParams(chipParams);

            chip.setOnClickListener(v -> {
                filterPandits(service);
                highlightSelectedChip(chip);
            });

            binding.chipContainer.addView(chip);
        }
    }

    // -------------------- HIGHLIGHT SELECTED CHIP --------------------
    private void highlightSelectedChip(Chip chip) {
        if (lastSelectedChip != null) {
            lastSelectedChip.setChipBackgroundColorResource(R.color.chip_color);
        }
        chip.setChipBackgroundColorResource(R.color.card_stroke);
        lastSelectedChip = chip;
    }

    // -------------------- FILTER PANDITS BASED ON SERVICE --------------------
    private void filterPandits(String service) {
        List<PanditModel> filtered = new ArrayList<>();
        for (PanditModel m : fullList) {
            if (m.services != null && m.services.contains(service)) {
                filtered.add(m);
            }
        }
        adapter.updateList(filtered);
    }

    // -------------------- INIT LAUNCHER --------------------
    private void initNotificationPermissionLauncher() {
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                    } else {
                    }
                }
        );
    }

    // -------------------- REQUEST PERMISSION --------------------
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
            }
        } else {
        }
    }
}
