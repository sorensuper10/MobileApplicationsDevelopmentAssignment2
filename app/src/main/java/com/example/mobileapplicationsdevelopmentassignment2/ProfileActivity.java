package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    DBHandler dbHandler;
    TextView textUsername, textGender, textAge, textWeight, textHeight;
    Button buttonBack, buttonEditProfile, buttonDeleteProfile;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        textUsername = findViewById(R.id.textUsername);
        textGender = findViewById(R.id.textGender);
        textAge = findViewById(R.id.textAge);
        textWeight = findViewById(R.id.textWeight);
        textHeight = findViewById(R.id.textHeight);
        buttonBack = findViewById(R.id.buttonBack);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);
        buttonDeleteProfile = findViewById(R.id.buttonDeleteProfile);

        dbHandler = new DBHandler(this);
        username = getIntent().getStringExtra("username");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username missing!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadUserProfile();

        buttonBack.setOnClickListener(v -> finish());

        buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        buttonDeleteProfile.setOnClickListener(v -> {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            int rowsDeleted = db.delete("users", "username=?", new String[]{username});
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Profile deleted successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to delete profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

        @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();
    }

    private void loadUserProfile() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT gender, age, weight, height FROM users WHERE username = ?",
                new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            String gender = cursor.getString(0);
            int age = cursor.getInt(1);
            float weight = cursor.getFloat(2);
            float height = cursor.getFloat(3);

            textUsername.setText("Username: " + username);
            textGender.setText("Gender: " + gender);
            textAge.setText("Age: " + age);
            textWeight.setText("Weight: " + weight + " kg");
            textHeight.setText("Height: " + height + " cm");
        } else {
            Toast.makeText(this, "User not found in database.", Toast.LENGTH_LONG).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}
