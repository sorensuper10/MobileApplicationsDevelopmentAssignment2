package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChoosePetActivity extends AppCompatActivity {

    private RadioGroup petOptions;
    private Button buttonConfirm, buttonBack;
    private String username;

    public static final String KEY_SELECTED_PET = "selected_pet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pet);

        petOptions = findViewById(R.id.petOptions);
        buttonConfirm = findViewById(R.id.buttonConfirmPet);
        buttonBack = findViewById(R.id.buttonBack);

        username = getIntent().getStringExtra("username");

        buttonConfirm.setOnClickListener(v -> {
            int selectedId = petOptions.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Please choose a pet", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadio = findViewById(selectedId);
            String petName = selectedRadio.getText().toString();

            // Save to per-user SharedPreferences
            SharedPreferences prefs = getSharedPreferences(username + "_Prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_SELECTED_PET, petName);
            editor.apply();

            Toast.makeText(this, petName + " selected!", Toast.LENGTH_SHORT).show();
            finish();
        });

        buttonBack.setOnClickListener(v -> finish());
    }
}
