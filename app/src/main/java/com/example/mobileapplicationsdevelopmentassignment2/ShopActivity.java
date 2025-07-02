package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {

    public static final String PREF_NAME = "PetPrefs";
    private static final String KEY_POINTS = "total_points";
    private static final String KEY_INVENTORY = "inventory";

    private TextView textPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        textPoints = findViewById(R.id.textPoints);
        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonAddCoins = findViewById(R.id.buttonAddCoins);

        // Shop item buttons
        Button toyBallButton = findViewById(R.id.button1);
        Button energyDrinkButton = findViewById(R.id.button2);
        Button petTreatButton = findViewById(R.id.button3);

        // Back to home
        buttonBack.setOnClickListener(v -> finish());

        // Add coins screen
        buttonAddCoins.setOnClickListener(v ->
                startActivity(new Intent(this, AddCoinsActivity.class)));

        // Purchase: Toy Ball
        toyBallButton.setOnClickListener(v -> handlePurchase("Toy Ball", 100));

        // Purchase: Energy Drink
        energyDrinkButton.setOnClickListener(v -> handlePurchase("Energy Drink", 50));

        // Purchase: Pet Treat
        petTreatButton.setOnClickListener(v -> handlePurchase("Pet Treat", 75));

        // Load points
        updatePointsDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePointsDisplay(); // Refresh coins when returning from AddCoinsActivity
    }

    private void updatePointsDisplay() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int points = prefs.getInt(KEY_POINTS, 0);
        textPoints.setText("Points: " + points);
    }

    private void handlePurchase(String itemName, int cost) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int currentPoints = prefs.getInt(KEY_POINTS, 0);

        if (currentPoints >= cost) {
            // Deduct coins
            int newPoints = currentPoints - cost;
            prefs.edit().putInt(KEY_POINTS, newPoints).apply();

            // Add to inventory
            String inventory = prefs.getString(KEY_INVENTORY, "");
            inventory = inventory.isEmpty() ? itemName : inventory + "," + itemName;
            prefs.edit().putString(KEY_INVENTORY, inventory).apply();

            Toast.makeText(this, itemName + " purchased!", Toast.LENGTH_SHORT).show();
            updatePointsDisplay();
        } else {
            Toast.makeText(this, "Not enough points!", Toast.LENGTH_SHORT).show();
        }
    }
}
