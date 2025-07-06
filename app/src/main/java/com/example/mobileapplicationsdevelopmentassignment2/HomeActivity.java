package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView welcomeText;
    Button buttonChoosePet, buttonCheckStatus, buttonAddActivity, buttonShop, buttonLogout, buttonHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        buttonChoosePet = findViewById(R.id.buttonChoosePet);
        buttonCheckStatus = findViewById(R.id.buttonCheckStatus);
        buttonAddActivity = findViewById(R.id.buttonAddActivity);
        buttonShop = findViewById(R.id.buttonShop);
        buttonLogout = findViewById(R.id.logoutButton);
        buttonHistory = findViewById(R.id.buttonHistory);

        // Get username
        String username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            welcomeText.setText("Welcome, " + username + "!");
        } else {
            welcomeText.setText("Welcome!");
        }

        // Pass username to other activities:
        buttonChoosePet.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChoosePetActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        buttonCheckStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckStatusActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        buttonAddActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddActivityActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        buttonShop.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShopActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        buttonHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Logout
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
