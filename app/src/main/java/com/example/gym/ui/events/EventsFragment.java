package com.example.gym.ui.events;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gym.Adapters.EventsAdapter;
import com.example.gym.Adapters.EventsAdapter;
import com.example.gym.Data.Event;
import com.example.gym.HelperDB;
import com.example.gym.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private ListView listView;
    private EventsAdapter eventsAdapter;
    private List<Event> eventList;
    private HelperDB helperDB;
    private TextView noEventsTextView;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        // Initialize the DB Helper
        helperDB = new HelperDB(getContext());

        // Initialize UI components
        calendarView = view.findViewById(R.id.calendarView);
        listView = view.findViewById(R.id.eventsListView);
        noEventsTextView = view.findViewById(R.id.no_events_text);

        // Handle date selection
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            String selectedDate = formatDate(date);
            eventList = getEventsForDate(selectedDate);

            if (eventList.isEmpty()) {
                noEventsTextView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                noEventsTextView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                // Set adapter to display events
                eventsAdapter = new EventsAdapter(getContext(), eventList);
                listView.setAdapter(eventsAdapter);
            }
        });

        return view;
    }

    // Method to format CalendarDay to String (yyyy-MM-dd)
    private String formatDate(CalendarDay date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth() - 1, date.getDay());
        return sdf.format(calendar.getTime());
    }

    // Method to fetch events for the selected date from the database
    private List<Event> getEventsForDate(String date) {
        List<Event> events = new ArrayList<>();
        Cursor cursor = helperDB.getEventsByDateCursor(date);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String beginTime = cursor.getString(cursor.getColumnIndexOrThrow("begin_time"));
                int durationMinutes = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));

                events.add(new Event(title, date, description, beginTime, durationMinutes));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return events;
    }
}
