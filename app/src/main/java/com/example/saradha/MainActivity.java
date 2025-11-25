package com.example.saradha;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.saradha.adapters.HomeAdapter;
import com.example.saradha.controller.HomeController;
import com.example.saradha.databinding.ActivityMainBinding;
import com.example.saradha.model.PanditModel;
import com.example.saradha.utils.UtilsFunctions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UtilsFunctions.setupBottomNav(this, binding.navView, R.id.navigation_home);

        binding.recyclerPandit.setLayoutManager(new LinearLayoutManager(this));

        HomeController controller = new HomeController();

        controller.fetchPandits(new HomeController.PanditCallback() {
            @Override
            public void onSuccess(List<PanditModel> list) {
                HomeAdapter adapter = new HomeAdapter(MainActivity.this, list);
                binding.recyclerPandit.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
