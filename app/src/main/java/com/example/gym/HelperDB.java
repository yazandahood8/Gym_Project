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
    private static final int DATABASE_VERSION = 2; // Incremented for schema changes

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
    public static final String COLUMN_EVENT_BEGIN_TIME = "begin_time"; // New field for event start time
    public static final String COLUMN_EVENT_DURATION = "duration"; // New field for event duration in minutes

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
                    COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                    COLUMN_EVENT_BEGIN_TIME + " TEXT, " +
                    COLUMN_EVENT_DURATION + " INTEGER);"; // Include begin time and duration

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
        if (oldVersion < 2) {
            // Add new columns if upgrading from version 1 to 2
            db.execSQL("ALTER TABLE " + TABLE_EVENT + " ADD COLUMN " + COLUMN_EVENT_BEGIN_TIME + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_EVENT + " ADD COLUMN " + COLUMN_EVENT_DURATION + " INTEGER");
        }
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
        return db.insert(TABLE_USER, null, values);
    }
    // Method to fetch user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_AGE, COLUMN_USER_PHONE, COLUMN_IS_ADMIN};
        String selection = COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {email};

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

        return null; // Return null if user is not found
    }
    // Method to delete all events from the database
    public void deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT, null, null); // Deletes all rows from the 'events' table
        db.close(); // Close the database connection after operation
    }

    // Method to update user details in the database
    public long updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_AGE, user.getAge());
        values.put(COLUMN_USER_PHONE, user.getPhoneNumber());
        values.put(COLUMN_IS_ADMIN, user.isAdmin() ? 1 : 0); // Store 1 for admin, 0 for regular user

        // Update the user's information where the email matches
        return db.update(TABLE_USER, values, COLUMN_USER_EMAIL + "=?", new String[]{user.getEmail()});
    }
    // Method to update an event based on its title
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_DATE, event.getDate());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());
        values.put(COLUMN_EVENT_BEGIN_TIME, event.getBeginTime());
        values.put(COLUMN_EVENT_DURATION, event.getDurationMinutes());

        // Update the event where the title matches
        return db.update(TABLE_EVENT, values, COLUMN_EVENT_TITLE + "=?", new String[]{event.getTitle()});
    }

    // Method to delete an event based on its title
    public int deleteEvent(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EVENT, COLUMN_EVENT_TITLE + "=?", new String[]{title});
    }

    // Method to fetch user by email and password
    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_AGE, COLUMN_USER_PHONE, COLUMN_IS_ADMIN};
        String selection = COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            boolean isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ADMIN)) == 1;
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_AGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)),
                    isAdmin
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
        values.put(COLUMN_EVENT_BEGIN_TIME, event.getBeginTime()); // Store begin time
        values.put(COLUMN_EVENT_DURATION, event.getDurationMinutes()); // Store duration
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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_BEGIN_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DURATION))
                );
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventList;
    }

    // Method to fetch events by date
    public Cursor getEventsByDateCursor(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "date = ?";
        String[] selectionArgs = {date};
        return db.query(TABLE_EVENT, null, selection, selectionArgs, null, null, null);
    }
}
