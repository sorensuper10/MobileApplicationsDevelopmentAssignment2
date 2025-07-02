package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InventoryActivity extends AppCompatActivity {

    public static final String PREF_NAME = "PetPrefs";
    private static final String KEY_INVENTORY = "inventory";

    private LinearLayout inventoryContainer;
    private Button buttonBack, buttonClearInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        inventoryContainer = findViewById(R.id.inventoryContainer);
        buttonBack = findViewById(R.id.buttonBack);
        buttonClearInventory = findViewById(R.id.buttonClearInventory);

        // Load and display inventory
        displayInventory();

        // Back button
        buttonBack.setOnClickListener(v -> finish());

        // Clear inventory button
        buttonClearInventory.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(KEY_INVENTORY);
            editor.apply();

            Toast.makeText(this, "Inventory cleared", Toast.LENGTH_SHORT).show();

            // Refresh screen
            recreate();
        });
    }

    private void displayInventory() {
        inventoryContainer.removeAllViews();
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String inventory = prefs.getString(KEY_INVENTORY, "");

        if (!inventory.isEmpty()) {
            String[] items = inventory.split(",");
            for (String item : items) {
                TextView itemView = new TextView(this);
                itemView.setText("• " + item.trim());
                itemView.setTextSize(16);
                itemView.setPadding(0, 0, 0, 12);
                inventoryContainer.addView(itemView);
            }
        } else {
            TextView emptyView = new TextView(this);
            emptyView.setText("No items purchased yet.");
            emptyView.setTextSize(16);
            inventoryContainer.addView(emptyView);
        }
    }
}
