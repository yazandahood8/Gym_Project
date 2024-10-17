package com.example.gym.Data;

public class Event {
    private String title;
    private String date;
    private String description;
    private String beginTime; // New field for start time
    private int durationMinutes; // Duration of the event in minutes

    // Constructor
    public Event(String title, String date, String description, String beginTime, int durationMinutes) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.beginTime = beginTime;
        this.durationMinutes = durationMinutes;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    // Method to calculate the end time based on begin time and duration
    public String getEndTime() {
        // Assuming time format is "HH:mm" for beginTime
        String[] timeParts = beginTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Add duration to minutes and calculate overflow to hours
        int totalMinutes = minute + durationMinutes;
        hour += totalMinutes / 60;
        minute = totalMinutes % 60;

        // Format hour and minute to "HH:mm"
        return String.format("%02d:%02d", hour, minute);
    }
}
