package com.example.gym.Adapters;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gym.Data.Event;
import com.example.gym.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventsAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventDate.setText(event.getDate());
        holder.eventDescription.setText(event.getDescription());

        // Assuming event.getBeginTime() returns a String in the format "HH:mm"
        String eventBeginTime = event.getBeginTime();
        String eventDate = event.getDate();  // Assuming date is "yyyy-MM-dd"

        // Combine date and time into a single DateTime string
        String eventDateTime = eventDate + " " + eventBeginTime;

        // Parse the String to a Date object
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        try {
            Date beginTime = dateTimeFormat.parse(eventDateTime);
            Date currentTime = new Date(); // Current date and time

            // Calculate the time difference between the current time and the event's start time
            long timeUntilStart = beginTime.getTime() - currentTime.getTime();

            if (timeUntilStart > 0) {
                // Start countdown
                new CountDownTimer(timeUntilStart, 1000) { // Count down in milliseconds
                    public void onTick(long millisUntilFinished) {
                        // Convert milliseconds into hours, minutes, and seconds
                        long hours = millisUntilFinished / (1000 * 60 * 60);
                        long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                        long seconds = (millisUntilFinished % (1000 * 60)) / 1000;

                        // Update the timer TextView with the remaining time
                        holder.eventTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
                    }

                    public void onFinish() {
                        // Event has started, update UI to indicate the event is live
                        holder.eventTimer.setText("Event has started!");
                    }
                }.start();
            } else {
                // If the event has already started, show the event is live
                holder.eventTimer.setText("Event has started!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            holder.eventTimer.setText("Error parsing time");
        }
    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // ViewHolder class for the event card
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle, eventDate, eventDescription, eventTimer;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventTimer = itemView.findViewById(R.id.eventTimer); // TextView for the countdown timer
        }
    }

    // Method to start the countdown
    private void startCountdown(Date beginTime, TextView timerTextView) {
        // Get the current time and the event start time
        long currentTimeMillis = System.currentTimeMillis();
        long eventStartTimeMillis = beginTime.getTime();
        long timeUntilStart = eventStartTimeMillis - currentTimeMillis;

        // If the event is in the future, start the countdown timer
        if (timeUntilStart > 0) {
            new CountDownTimer(timeUntilStart, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Calculate hours, minutes, and seconds remaining
                    long hours = millisUntilFinished / (1000 * 60 * 60);
                    long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                    long seconds = (millisUntilFinished / 1000) % 60;

                    // Format the time as HH:mm:ss and set it in the TextView
                    String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                    timerTextView.setText(time);
                }

                @Override
                public void onFinish() {
                    // Once the countdown is finished, you can display a message or change the text
                    timerTextView.setText("Event started!");
                }
            }.start();
        } else {
            // If the event has already started, display the appropriate message
            timerTextView.setText("Event started!");
        }
    }
}
