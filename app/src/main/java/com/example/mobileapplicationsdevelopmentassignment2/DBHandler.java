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
        // Not needed – using prebuilt DB
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not needed – using prebuilt DB
    }

    public void copyDatabaseIfNeeded(Context context) {
        File dbFile = context.getDatabasePath(DB_NAME);

        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
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
                e.printStackTrace();
                Log.e("DBHandler", "Failed to copy database: " + e.getMessage());
            }
        } else {
            Log.d("DBHandler", "Database already exists, no need to copy.");
        }
    }

    public void addActivity(String date, int steps, int waterIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DATE_COL, date);
        values.put(STEPS_COL, steps);
        values.put(WATER_COL, waterIntake);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getAllActivities() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
