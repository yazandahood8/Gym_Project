package com.example.gym;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gym.Data.User;
import com.example.gym.Data.Event;

import java.util.ArrayList;
import java.util.List;

public class HelperDB extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "info.db";
    private static final int DATABASE_VERSION = 1;

    // User table info
    public static final String TABLE_USER = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_AGE = "age";
    public static final String COLUMN_USER_PHONE = "phone_number";
    public static final String COLUMN_IS_ADMIN = "is_admin"; // New column for admin flag

    // Event table info
    public static final String TABLE_EVENT = "events";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_EVENT_TITLE = "title";
    public static final String COLUMN_EVENT_DATE = "date";
    public static final String COLUMN_EVENT_DESCRIPTION = "description";

    // SQL query to create the User table
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_USER_PASSWORD + " TEXT, " +
                    COLUMN_USER_AGE + " INTEGER, " +
                    COLUMN_USER_PHONE + " TEXT, " +
                    COLUMN_IS_ADMIN + " INTEGER DEFAULT 0);";  // Default 0 for non-admin

    // SQL query to create the Event table
    private static final String CREATE_TABLE_EVENT =
            "CREATE TABLE " + TABLE_EVENT + " (" +
                    COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_TITLE + " TEXT, " +
                    COLUMN_EVENT_DATE + " TEXT, " +
                    COLUMN_EVENT_DESCRIPTION + " TEXT);";

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_EVENT); // Create event table as well
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If upgrading the database, drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        onCreate(db); // Create new tables
    }

    // Method to add a new user to the database
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_AGE, user.getAge());
        values.put(COLUMN_USER_PHONE, user.getPhoneNumber());
        values.put(COLUMN_IS_ADMIN, user.isAdmin() ? 1 : 0); // Store 1 for admin, 0 for regular user

        // Insert row into the table
        return db.insert(TABLE_USER, null, values);
    }

    // Method to fetch user by email and password
    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_AGE, COLUMN_USER_PHONE, COLUMN_IS_ADMIN};
        String selection = COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN)) == 1; // Check admin status

            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_AGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)),
                    isAdmin // Initialize isAdmin field
            );
            cursor.close();
            return user;
        }

        return null;
    }

    // Method to add a new event to the database
    public long addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, event.getTitle());
        values.put(COLUMN_EVENT_DATE, event.getDate());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());

        // Insert row into the events table
        return db.insert(TABLE_EVENT, null, values);
    }

    // Method to fetch all events from the database
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EVENT;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION))
                );
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventList;
    }
}
