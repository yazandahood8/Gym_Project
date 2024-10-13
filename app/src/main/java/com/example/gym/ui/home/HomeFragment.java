package com.example.gym.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gym.Adapters.EventsAdapter;
import com.example.gym.Data.Event;
import com.example.gym.HelperDB;
import com.example.gym.R;
import com.example.gym.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView eventsRecyclerView;
    private EventsAdapter eventsAdapter;
    private HelperDB databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        databaseHelper = new HelperDB(getContext());

        // Fetch events from the database
        List<Event> eventList = databaseHelper.getAllEvents();

        // Set up RecyclerView
        eventsAdapter = new EventsAdapter(eventList);
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
