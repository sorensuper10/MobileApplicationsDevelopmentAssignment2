package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddCoinsActivity extends AppCompatActivity {

    public static final String PREF_NAME = "PetPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coins);

        Button buttonBuy = findViewById(R.id.buttonBuyCoins);
        Button buttonBack = findViewById(R.id.buttonBack);

        buttonBuy.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            int current = prefs.getInt("total_points", 0);
            prefs.edit().putInt("total_points", current + 100).apply();

            Toast.makeText(this, "100 coins added! 💰", Toast.LENGTH_SHORT).show();
        });

        buttonBack.setOnClickListener(v -> finish());
    }
}
