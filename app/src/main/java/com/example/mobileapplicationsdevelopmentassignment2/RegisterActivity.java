package com.example.mobileapplicationsdevelopmentassignment2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button buttonRegister, buttonBack;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize database and UI components
        db = new DBHandler(this);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(v -> finish()); // Go back to MainActivity

        buttonRegister.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else if (db.registerUser(username, password)) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
