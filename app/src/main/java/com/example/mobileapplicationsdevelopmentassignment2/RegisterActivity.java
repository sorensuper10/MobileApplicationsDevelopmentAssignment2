package com.example.mobileapplicationsdevelopmentassignment2;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername, editPassword, editAge, editWeight, editHeight;
    RadioGroup genderGroup;
    Button buttonRegister, buttonBack;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHandler(this);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editAge = findViewById(R.id.editAge);
        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        genderGroup = findViewById(R.id.genderGroup);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> finish());

        buttonRegister.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String ageStr = editAge.getText().toString().trim();
            String weightStr = editWeight.getText().toString().trim();
            String heightStr = editHeight.getText().toString().trim();

            int selectedGenderId = genderGroup.getCheckedRadioButtonId();

            if (username.isEmpty() || password.isEmpty() || ageStr.isEmpty() ||
                    weightStr.isEmpty() || heightStr.isEmpty() || selectedGenderId == -1) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for duplicate username
            SQLiteDatabase dbReadable = db.getReadableDatabase();
            Cursor cursor = dbReadable.rawQuery(
                    "SELECT * FROM users WHERE username = ?",
                    new String[]{username}
            );

            if (cursor.moveToFirst()) {
                Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }
            cursor.close();

            // Insert new user
            SQLiteDatabase dbWritable = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            values.put("gender", ((RadioButton) findViewById(selectedGenderId)).getText().toString());
            values.put("age", Integer.parseInt(ageStr));
            values.put("weight", Float.parseFloat(weightStr));
            values.put("height", Float.parseFloat(heightStr));

            long result = dbWritable.insert("users", null, values);
            if (result != -1) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
