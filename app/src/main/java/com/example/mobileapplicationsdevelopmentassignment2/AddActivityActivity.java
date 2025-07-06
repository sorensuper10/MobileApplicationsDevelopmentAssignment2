package com.example.mobileapplicationsdevelopmentassignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivityActivity extends AppCompatActivity {

    EditText editSteps, editWater;
    Button buttonSave, buttonBack;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        editSteps = findViewById(R.id.editSteps);
        editWater = findViewById(R.id.editWater);
        buttonSave = findViewById(R.id.buttonSaveActivity);
        buttonBack = findViewById(R.id.buttonBack);
        db = new DBHandler(this);

        String username = getIntent().getStringExtra("username");

        buttonSave.setOnClickListener(v -> {
            String stepsStr = editSteps.getText().toString().trim();
            String waterStr = editWater.getText().toString().trim();

            if (stepsStr.isEmpty() || waterStr.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int steps = Integer.parseInt(stepsStr);
            int water = Integer.parseInt(waterStr);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            db.addActivity(username, currentDate, steps, water);
            Toast.makeText(this, "Activity saved!", Toast.LENGTH_SHORT).show();
            finish();
        });

        buttonBack.setOnClickListener(v -> finish());
    }
}
