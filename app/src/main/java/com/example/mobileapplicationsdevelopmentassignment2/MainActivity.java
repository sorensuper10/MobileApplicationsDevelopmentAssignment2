package com.example.mobileapplicationsdevelopmentassignment2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonLogin, buttonRegister;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);
        db.copyDatabaseIfNeeded(this);

        db.getWritableDatabase();

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Login clicked (not implemented)", Toast.LENGTH_SHORT).show()
        );

        buttonRegister.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Register clicked (not implemented)", Toast.LENGTH_SHORT).show()
        );
    }

    /*private void clearFields() {
        .setText("");
        .setText("");
        .setText("");
        .setText("");
        .setText("");
    }*/

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}

