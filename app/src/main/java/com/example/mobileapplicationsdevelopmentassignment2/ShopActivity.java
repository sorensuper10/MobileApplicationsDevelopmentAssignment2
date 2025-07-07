package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {

    private static final String KEY_POINTS = "total_points";
    private static final String KEY_INVENTORY = "inventory";

    private TextView textPoints;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        username = getIntent().getStringExtra("username");

        textPoints = findViewById(R.id.textPoints);
        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonAddCoins = findViewById(R.id.buttonAddCoins);
        Button buttonViewInventory = findViewById(R.id.buttonViewInventory);

        // Shop item buttons
        Button toyBallButton = findViewById(R.id.button1);
        Button energyDrinkButton = findViewById(R.id.button2);
        Button petTreatButton = findViewById(R.id.button3);


        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", username);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Reset stack
            startActivity(intent);
        });


        buttonAddCoins.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCoinsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });


        buttonViewInventory.setOnClickListener(v -> {
            Intent intent = new Intent(this, InventoryActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Purchase buttons
        toyBallButton.setOnClickListener(v -> handlePurchase("Toy Ball", 100));
        energyDrinkButton.setOnClickListener(v -> handlePurchase("Energy Drink", 50));
        petTreatButton.setOnClickListener(v -> handlePurchase("Pet Treat", 75));

        updatePointsDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePointsDisplay();  // Refresh points when returning from AddCoinsActivity
    }

    private void updatePointsDisplay() {
        SharedPreferences prefs = getSharedPreferences(username + "_Prefs", MODE_PRIVATE);
        int points = prefs.getInt(KEY_POINTS, 0);
        textPoints.setText("Points: " + points);
    }

    private void handlePurchase(String itemName, int cost) {
        SharedPreferences prefs = getSharedPreferences(username + "_Prefs", MODE_PRIVATE);
        int currentPoints = prefs.getInt(KEY_POINTS, 0);

        if (currentPoints >= cost) {
            int newPoints = currentPoints - cost;
            prefs.edit().putInt(KEY_POINTS, newPoints).apply();

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
