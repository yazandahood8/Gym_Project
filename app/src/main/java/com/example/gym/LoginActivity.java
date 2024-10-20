package com.example.gym;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gym.Data.Event;
import com.example.gym.Data.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private HelperDB databaseHelper;

    // SharedPreferences to store login state
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        addNewEvent();
        // Check if the user is already logged in
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            // User is already logged in, proceed to the main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
            startActivity(intent);
            finish(); // Close the login activity
            return;
        }

        databaseHelper = new HelperDB(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db != null) {
            Toast.makeText(LoginActivity.this, "Database created or opened successfully.", Toast.LENGTH_SHORT).show();
            db.close();
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        addAdmin();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    User user = databaseHelper.getUser(email, password);
                    if (user != null) {
                        // Save login state in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_IS_LOGGED_IN, true);
                        editor.putString(KEY_EMAIL, email); // Optionally store the user's email
                        editor.apply(); // Apply the changes

                        if (user.isAdmin()) {
                            Toast.makeText(LoginActivity.this, "Admin login successful", Toast.LENGTH_SHORT).show();
                           //  Redirect to AdminActivity (if you have one)
                             Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                             startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                            startActivity(intent);
                        }
                        finish(); // Close the login activity
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private void addNewEvent() {
        // Ensure context is properly initialized
        Context context = LoginActivity.this; // If this is in a fragment, use `getContext()` instead.

        // Create a new instance of HelperDB to interact with the database
        HelperDB db = new HelperDB(context);

        db.deleteAllEvents();

        // Define event details for four different events
        String[] eventTitles = {"Yoga Session", "Meditation Session", "Strength Training", "Cardio Workout"};
        String[] eventDates = {"2024-10-16", "2024-10-17", "2024-10-18", "2024-10-20"};
        String[] beginTimes = {"08:00", "10:30", "15:00", "18:30"};
        String[] eventDescriptions = {
                "A relaxing yoga session for all levels.",
                "A guided meditation session to calm the mind.",
                "Strength training for building muscle.",
                "A cardio workout to boost endurance."
        };
        int[] durations = {60, 45, 90, 60}; // Durations in minutes

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        // Loop to add each event
        for (int i = 0; i < eventTitles.length; i++) {
            String dateTimeString = eventDates[i] + " " + beginTimes[i]; // Combine date and time

            try {
                // Parse the datetime string
                Date eventDateTime = dateFormat.parse(dateTimeString);

                // Now you can proceed to add the event to the database
                if (eventDateTime != null) {
                    // You can store the formatted date in the database
                    String formattedDateTime = dateFormat.format(eventDateTime);

                    // Create an Event object
                    Event newEvent = new Event(eventTitles[i], eventDates[i], eventDescriptions[i], beginTimes[i], durations[i]);

                    // Add the event to the database
                    long result = db.addEvent(newEvent);

                    // Check if the event was added successfully
                    if (result != -1) {
                        // Event added successfully
                     //   Toast.makeText(context, eventTitles[i] + " added!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error adding event
                        Toast.makeText(context, "Failed to add " + eventTitles[i], Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing date and time for " + eventTitles[i], Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addAdmin() {
        String name = "Tala";
        String email = "tala@gmail.com";
        String password = "123456789";
        int age=18;
        String phone = "0599999999";
        boolean isAdmin = true;

        // Validate age input


        // Create a new user
        User user = new User(1,name, email, password, age, phone,"Female", isAdmin);

        // Add the user to the database
        long result = databaseHelper.addUser(user);

        if (result != -1) {
            Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding user.", Toast.LENGTH_SHORT).show();
        }
    }



}
