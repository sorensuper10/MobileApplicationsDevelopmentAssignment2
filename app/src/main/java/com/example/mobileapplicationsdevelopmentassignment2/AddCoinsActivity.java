package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddCoinsActivity extends AppCompatActivity {

    public static final String KEY_POINTS = "total_points";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coins);

        username = getIntent().getStringExtra("username");  // ✅ Receive username

        Button buttonBuy = findViewById(R.id.buttonBuyCoins);
        Button buttonBack = findViewById(R.id.buttonBack);

        buttonBuy.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(username + "_Prefs", MODE_PRIVATE);  // ✅ Per-user prefs
            int current = prefs.getInt(KEY_POINTS, 0);
            prefs.edit().putInt(KEY_POINTS, current + 100).apply();

            Toast.makeText(this, "100 coins added! 💰", Toast.LENGTH_SHORT).show();
        });

        buttonBack.setOnClickListener(v -> finish());
    }
}
