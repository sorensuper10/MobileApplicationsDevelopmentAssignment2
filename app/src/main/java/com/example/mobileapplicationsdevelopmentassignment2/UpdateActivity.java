package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText inputSteps, inputWater;
    Button buttonUpdate, buttonCancel;
    DBHandler dbHandler;
    int activityId;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        inputSteps = findViewById(R.id.inputSteps);
        inputWater = findViewById(R.id.inputWater);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonCancel = findViewById(R.id.buttonCancel);
        dbHandler = new DBHandler(this);

        Intent intent = getIntent();
        activityId = intent.getIntExtra("id", -1);
        int currentSteps = intent.getIntExtra("steps", 0);
        int currentWater = intent.getIntExtra("water", 0);
        username = intent.getStringExtra("username");

        inputSteps.setText(String.valueOf(currentSteps));
        inputWater.setText(String.valueOf(currentWater));

        buttonUpdate.setOnClickListener(v -> {
            int newSteps = Integer.parseInt(inputSteps.getText().toString());
            int newWater = Integer.parseInt(inputWater.getText().toString());

            boolean success = dbHandler.updateActivity(activityId, newSteps, newWater);
            Toast.makeText(this, success ? "Activity updated" : "Update failed", Toast.LENGTH_SHORT).show();

            finish();  // Go back to HistoryActivity
        });

        buttonCancel.setOnClickListener(v -> finish());
    }
}
