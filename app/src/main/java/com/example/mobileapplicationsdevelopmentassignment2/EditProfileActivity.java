package com.example.mobileapplicationsdevelopmentassignment2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    DBHandler dbHandler;
    EditText editGender, editAge, editWeight, editHeight;
    Button buttonSave, buttonBack;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI components
        editGender = findViewById(R.id.editGender);
        editAge = findViewById(R.id.editAge);
        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.buttonBack);

        dbHandler = new DBHandler(this);
        username = getIntent().getStringExtra("username");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username missing!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadUserProfile();

        buttonSave.setOnClickListener(v -> updateUserProfile());
        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadUserProfile() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT gender, age, weight, height FROM users WHERE username = ?",
                new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            editGender.setText(cursor.getString(0));
            editAge.setText(String.valueOf(cursor.getInt(1)));
            editWeight.setText(String.valueOf(cursor.getFloat(2)));
            editHeight.setText(String.valueOf(cursor.getFloat(3)));
        } else {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void updateUserProfile() {
        String gender = editGender.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();
        String weightStr = editWeight.getText().toString().trim();
        String heightStr = editHeight.getText().toString().trim();

        if (gender.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        float weight = Float.parseFloat(weightStr);
        float height = Float.parseFloat(heightStr);

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.execSQL("UPDATE users SET gender = ?, age = ?, weight = ?, height = ? WHERE username = ?",
                new Object[]{gender, age, weight, height, username});

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
