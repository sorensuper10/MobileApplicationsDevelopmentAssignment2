package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    DBHandler dbHandler;
    LinearLayout historyContainer;
    Button buttonBack;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHandler = new DBHandler(this);
        historyContainer = findViewById(R.id.historyContainer);
        buttonBack = findViewById(R.id.buttonBack);

        username = getIntent().getStringExtra("username");

        // Optional: Show top-left back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        buttonBack.setOnClickListener(v -> finish());

        loadHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadHistory() {
        historyContainer.removeAllViews();
        Cursor cursor = dbHandler.getAllActivities(username);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                int steps = cursor.getInt(cursor.getColumnIndexOrThrow("steps"));
                int water = cursor.getInt(cursor.getColumnIndexOrThrow("water_intake"));

                LinearLayout entryLayout = new LinearLayout(this);
                entryLayout.setOrientation(LinearLayout.VERTICAL);
                entryLayout.setPadding(0, 0, 0, 40);

                TextView entryView = new TextView(this);
                entryView.setText("Date: " + date + "\nSteps: " + steps + "\nWater: " + water + " ml");
                entryView.setTextSize(16);

                Button updateBtn = new Button(this);
                updateBtn.setText("UPDATE");
                updateBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(HistoryActivity.this, UpdateActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("steps", steps);
                    intent.putExtra("water", water);
                    intent.putExtra("username", username);
                    startActivity(intent);
                });

                Button deleteBtn = new Button(this);
                deleteBtn.setText("DELETE");
                deleteBtn.setOnClickListener(v -> {
                    dbHandler.deleteActivity(id);
                    Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show();
                    loadHistory();
                });

                entryLayout.addView(entryView);
                entryLayout.addView(updateBtn);
                entryLayout.addView(deleteBtn);

                historyContainer.addView(entryLayout);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            TextView noData = new TextView(this);
            noData.setText("No activity data found.");
            noData.setTextSize(16);
            historyContainer.addView(noData);
        }
    }
}
