package com.example.divye.atlas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Divye10 on 06-03-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

        // All Static variables
// Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "places";

        // Contacts table name
        private static final String TABLE_CITY = "city";

        // Contacts Table Columns names
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";

        public DatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CITY_TABLE = "CREATE TABLE IF NOT EXISTS" + TABLE_CITY + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT"+")";
            db.execSQL(CREATE_CITY_TABLE);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);

        // Create tables again
        onCreate(db);
    }
    public void addCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, city.getID());
        values.put(KEY_NAME, city.getName()); // Contact Name
         // Contact Phone

        // Inserting Row
        db.insert(TABLE_CITY, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public int getCityCount(String name) {
        String countQuery = "SELECT  * FROM " + TABLE_CITY +"WHERE name="+name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
