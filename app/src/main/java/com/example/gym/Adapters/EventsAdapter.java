package com.example.gym.Adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gym.Data.Event;
import com.example.gym.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends BaseAdapter {

    private List<Event> eventList;
    private LayoutInflater inflater;

    public EventsAdapter(Context context, List<Event> eventList) {
        this.eventList = eventList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_card_item, parent, false);
            holder = new ViewHolder();
            holder.eventTitle = convertView.findViewById(R.id.eventTitle);
            holder.eventDate = convertView.findViewById(R.id.eventDate);
            holder.eventDescription = convertView.findViewById(R.id.eventDescription);
            holder.eventTimer = convertView.findViewById(R.id.eventTimer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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
            if (beginTime != null) {
                // Start countdown for the event
                startCountdown(beginTime, holder.eventTimer);
            } else {
                holder.eventTimer.setText("Invalid date/time");
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.eventTimer.setText("Error parsing time");
        }

        return convertView;
    }

    // ViewHolder class for the event list item
    private static class ViewHolder {
        TextView eventTitle, eventDate, eventDescription, eventTimer;
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
                    timerTextView.setText("Event started!");
                }
            }.start();
        } else {
            timerTextView.setText("Event started!");
        }
    }
}
