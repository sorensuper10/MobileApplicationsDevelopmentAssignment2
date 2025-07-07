package com.example.mobileapplicationsdevelopmentassignment2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    DBHandler dbHandler;
    RadioGroup radioGender;
    RadioButton radioMale, radioFemale;
    EditText editAge, editWeight, editHeight;
    Button buttonSave, buttonBack;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        radioGender = findViewById(R.id.radioGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
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
            String gender = cursor.getString(0);
            int age = cursor.getInt(1);
            float weight = cursor.getFloat(2);
            float height = cursor.getFloat(3);

            // Pre-select gender radio button
            if (gender.equalsIgnoreCase("Male")) {
                radioMale.setChecked(true);
            } else if (gender.equalsIgnoreCase("Female")) {
                radioFemale.setChecked(true);
            }

            editAge.setText(String.valueOf(age));
            editWeight.setText(String.valueOf(weight));
            editHeight.setText(String.valueOf(height));
        } else {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void updateUserProfile() {
        String ageStr = editAge.getText().toString().trim();
        String weightStr = editWeight.getText().toString().trim();
        String heightStr = editHeight.getText().toString().trim();

        int selectedGenderId = radioGender.getCheckedRadioButtonId();
        String gender;
        if (selectedGenderId == R.id.radioMale) {
            gender = "Male";
        } else if (selectedGenderId == R.id.radioFemale) {
            gender = "Female";
        } else {
            Toast.makeText(this, "Please select a gender.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
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
