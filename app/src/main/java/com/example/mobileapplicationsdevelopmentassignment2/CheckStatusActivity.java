package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckStatusActivity extends AppCompatActivity {

    TextView statusSteps, statusWater, statusMood, statusPet;
    Button buttonBack;
    DBHandler db;

    public static final String PREF_NAME = "PetPrefs";
    public static final String KEY_SELECTED_PET = "selected_pet";
    public static final String KEY_POINTS = "total_points";
    public static final String KEY_LAST_REWARDED_DATE = "last_rewarded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);

        // Bind UI
        statusSteps = findViewById(R.id.statusSteps);
        statusWater = findViewById(R.id.statusWater);
        statusMood = findViewById(R.id.statusMood);
        statusPet = findViewById(R.id.statusPet);
        buttonBack = findViewById(R.id.buttonBack);
        db = new DBHandler(this);

        // Load SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get selected pet
        String selectedPet = prefs.getString(KEY_SELECTED_PET, "None");
        statusPet.setText("Your Pet: " + selectedPet);

        // Get today’s totals
        int totalSteps = db.getTodayTotalSteps();
        int totalWater = db.getTodayTotalWater();

        // Show status
        if (totalSteps > 0 || totalWater > 0) {
            statusSteps.setText("Steps: " + totalSteps + " / 10000");
            statusWater.setText("Water Intake: " + totalWater + "ml / 3000ml");

            String mood;
            if (totalSteps >= 10000 && totalWater >= 3000) {
                mood = "Great!";
            } else if (totalSteps >= 5000 || totalWater >= 1500) {
                mood = "Okay";
            } else {
                mood = "Sad";
            }
            statusMood.setText("Mood: " + mood);
        } else {
            statusSteps.setText("Steps: No data");
            statusWater.setText("Water Intake: No data");
            statusMood.setText("Mood: Unknown");
        }

        // Reward logic (once per day)
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastRewarded = prefs.getString(KEY_LAST_REWARDED_DATE, "");

        if (!today.equals(lastRewarded)) {
            int reward = 0;
            if (totalSteps >= 10000) reward += 50;
            if (totalWater >= 3000) reward += 50;

            if (reward > 0) {
                int currentPoints = prefs.getInt(KEY_POINTS, 0);
                editor.putInt(KEY_POINTS, currentPoints + reward);
                editor.putString(KEY_LAST_REWARDED_DATE, today);
                editor.apply();

                Toast.makeText(this, "You earned " + reward + " points today!", Toast.LENGTH_LONG).show();
            }
        }

        // Back button
        buttonBack.setOnClickListener(v -> finish());
    }
}