package com.example.mobileapplicationsdevelopmentassignment2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "PetFitness.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "UserActivity";

    private static final String ID_COL = "id";
    private static final String DATE_COL = "date";
    private static final String STEPS_COL = "steps";
    private static final String WATER_COL = "water_intake";

    private final Context context;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        copyDatabaseIfNeeded(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Using prebuilt DB — no schema creation needed
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required for prebuilt DB
    }

    // Copy database from assets folder to app's DB path if not already present
    public void copyDatabaseIfNeeded(Context context) {
        File dbFile = context.getDatabasePath(DB_NAME);

        if (dbFile.exists()) {
            Log.d("DBHandler", "Database already exists, no need to copy.");
            return;
        }

        try (InputStream input = context.getAssets().open(DB_NAME);
             OutputStream output = new FileOutputStream(dbFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            Log.d("DBHandler", "Database copied from assets.");
        } catch (IOException e) {
            Log.e("DBHandler", "Failed to copy database: " + e.getMessage());
        }
    }

    // Register a new user
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    // Check login credentials
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Add user activity
    public void addActivity(String date, int steps, int waterIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_COL, date);
        values.put(STEPS_COL, steps);
        values.put(WATER_COL, waterIntake);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get all activity logs
    public Cursor getAllActivities() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Update an activity by ID
    public boolean updateActivity(int id, int newSteps, int newWater) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STEPS_COL, newSteps);
        values.put(WATER_COL, newWater);

        int rowsAffected = db.update(TABLE_NAME, values, ID_COL + "=?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // Delete an activity by ID
    public boolean deleteActivity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, ID_COL + "=?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Get total steps for today
    public int getTodayTotalSteps() {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalSteps = 0;
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + STEPS_COL + ") FROM " + TABLE_NAME + " WHERE " + DATE_COL + " = ?",
                new String[]{today}
        );

        if (cursor.moveToFirst()) {
            totalSteps = cursor.getInt(0);
        }

        cursor.close();
        return totalSteps;
    }

    // Get total water intake for today
    public int getTodayTotalWater() {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalWater = 0;
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + WATER_COL + ") FROM " + TABLE_NAME + " WHERE " + DATE_COL + " = ?",
                new String[]{today}
        );

        if (cursor.moveToFirst()) {
            totalWater = cursor.getInt(0);
        }

        cursor.close();
        return totalWater;
    }
}
