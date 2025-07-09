package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckStatusActivity extends AppCompatActivity {

    TextView statusSteps, statusWater, statusMood, statusPet;
    ImageView petImage;
    Button buttonBack;
    DBHandler db;

    public static final String KEY_SELECTED_PET = "selected_pet";
    public static final String KEY_POINTS = "total_points";
    public static final String KEY_LAST_REWARDED_DATE = "last_rewarded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);

        statusSteps = findViewById(R.id.statusSteps);
        statusWater = findViewById(R.id.statusWater);
        statusMood = findViewById(R.id.statusMood);
        statusPet = findViewById(R.id.statusPet);
        petImage = findViewById(R.id.statusPetImage);
        buttonBack = findViewById(R.id.buttonBack);
        db = new DBHandler(this);

        String username = getIntent().getStringExtra("username");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(username + "_Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Pet
        String selectedPet = prefs.getString(KEY_SELECTED_PET, "None");
        statusPet.setText("Your Pet: " + selectedPet);

        // Today's totals
        int totalSteps = db.getTodayTotalSteps(username);
        int totalWater = db.getTodayTotalWater(username);

        // Load user profile from DB for personalized goals
        SQLiteDatabase dbUser = db.getReadableDatabase();
        Cursor cursor = dbUser.rawQuery(
                "SELECT gender, age, weight, height FROM users WHERE username = ?",
                new String[]{username});

        int targetSteps = 10000;  // Default fallback
        int targetWater = 3000;

        if (cursor != null && cursor.moveToFirst()) {
            String gender = cursor.getString(0);
            int age = cursor.getInt(1);
            float weight = cursor.getFloat(2);
            float height = cursor.getFloat(3);

            // Step Target (Age-based)
            if (age <= 4) {
                targetSteps = 0;
            } else if (age <= 12) {
                targetSteps = 7000;
            } else if (age <= 17) {
                targetSteps = 8500;
            } else if (age <= 39) {
                targetSteps = 10000;
            } else if (age <= 59) {
                targetSteps = 8000;
            } else {
                targetSteps = 7000;
            }

            // Water Target (Age-based + Weight/Gender-based)
            if (age <= 3) {
                targetWater = 1300;
            } else if (age <= 8) {
                targetWater = 1700;
            } else if (age <= 13) {
                targetWater = Math.max((int) (weight * 30), 2000);
            } else {
                if (gender.equalsIgnoreCase("Male")) {
                    targetWater = (int) (weight * 35);
                } else {
                    targetWater = (int) (weight * 30);
                }
            }

            cursor.close();
        }

        // Show in UI
        statusSteps.setText("Steps: " + totalSteps + " / " + targetSteps);
        statusWater.setText("Water Intake: " + totalWater + " ml / " + targetWater + " ml");

        // Mood Logic
        String mood = "Unknown";
        if (totalSteps > 0 || totalWater > 0) {
            if (totalSteps >= targetSteps && totalWater >= targetWater) {
                mood = "Great!";
            } else if (totalSteps >= targetSteps * 0.5 || totalWater >= targetWater * 0.5) {
                mood = "Okay";
            } else {
                mood = "Sad";
            }
        }
        statusMood.setText("Mood: " + mood);

        // Pet Mood Image Logic
        boolean isHalfwayOrMore = totalSteps >= targetSteps * 0.5 && totalWater >= targetWater * 0.5;

        if (selectedPet.equalsIgnoreCase("Dog")) {
            if (isHalfwayOrMore) {
                petImage.setImageResource(R.drawable.dog_normal);
            } else {
                petImage.setImageResource(R.drawable.dog_thirsty);
            }
        } else if (selectedPet.equalsIgnoreCase("Cat")) {
            if (isHalfwayOrMore) {
                petImage.setImageResource(R.drawable.cat_normal);
            } else {
                petImage.setImageResource(R.drawable.cat_thirsty);
            }
        } else if (selectedPet.equalsIgnoreCase("Bird")) {
            if (isHalfwayOrMore) {
                petImage.setImageResource(R.drawable.bird_normal);
            } else {
                petImage.setImageResource(R.drawable.bird_thirsty);
            }
        } else {
            petImage.setImageDrawable(null);
        }

        // Reward Logic
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastRewarded = prefs.getString(KEY_LAST_REWARDED_DATE, "");

        if (!today.equals(lastRewarded)) {
            int reward = 0;
            if (totalSteps >= targetSteps) reward += 50;
            if (totalWater >= targetWater) reward += 50;

            if (reward > 0) {
                int currentPoints = prefs.getInt(KEY_POINTS, 0);
                editor.putInt(KEY_POINTS, currentPoints + reward);
                editor.putString(KEY_LAST_REWARDED_DATE, today);
                editor.apply();

                Toast.makeText(this, "You earned " + reward + " points today!", Toast.LENGTH_LONG).show();
            }
        }

        buttonBack.setOnClickListener(v -> finish());
    }
}
