package com.example.mobileapplicationsdevelopmentassignment2;

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
    Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textUsername = findViewById(R.id.textUsername);
        textGender = findViewById(R.id.textGender);
        textAge = findViewById(R.id.textAge);
        textWeight = findViewById(R.id.textWeight);
        textHeight = findViewById(R.id.textHeight);
        buttonBack = findViewById(R.id.buttonBack);

        dbHandler = new DBHandler(this);
        String username = getIntent().getStringExtra("username");

        // Defensive check before loading profile
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username missing!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadUserProfile(username);

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadUserProfile(String username) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT gender, age, weight, height FROM users WHERE username = ?", new String[]{username});

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