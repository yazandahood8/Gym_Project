package com.example.gym;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gym.Data.Event;

public class AdminActivity extends AppCompatActivity {

    private EditText etEventTitle, etEventDate, etBeginTime, etDuration, etEventDescription;
    private Button btnAddEvent, btnEditEvent, btnDeleteEvent;

    //database 
    private HelperDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        etEventTitle = findViewById(R.id.etEventTitle);
        etEventDate = findViewById(R.id.etEventDate);
        etBeginTime = findViewById(R.id.etBeginTime);
        etDuration = findViewById(R.id.etDuration);
        etEventDescription = findViewById(R.id.etEventDescription);

        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnEditEvent = findViewById(R.id.btnEditEvent);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        db = new HelperDB(this);

        btnAddEvent.setOnClickListener(v -> addEvent());
        btnEditEvent.setOnClickListener(v -> editEvent());
        btnDeleteEvent.setOnClickListener(v -> deleteEvent());
    }

    private void addEvent() {
        String title = etEventTitle.getText().toString();
        String date = etEventDate.getText().toString();
        String beginTime = etBeginTime.getText().toString();
        String description = etEventDescription.getText().toString();
        int duration;

        try {
            duration = Integer.parseInt(etDuration.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid duration format", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, date, description, beginTime, duration);
        long result = db.addEvent(event);

        if (result != -1) {
            Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
        }
    }

    private void editEvent() {
        String title = etEventTitle.getText().toString();
        String date = etEventDate.getText().toString();
        String beginTime = etBeginTime.getText().toString();
        String description = etEventDescription.getText().toString();
        int duration;

        try {
            duration = Integer.parseInt(etDuration.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid duration format", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, date, description, beginTime, duration);
        int rowsAffected = db.updateEvent(event);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating event", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteEvent() {
        String title = etEventTitle.getText().toString();
        int rowsDeleted = db.deleteEvent(title);

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting event", Toast.LENGTH_SHORT).show();
        }
    }
}
