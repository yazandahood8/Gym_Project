package com.example.gym.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.util.List;

public class HomeFragment extends Fragment {
    private ListView eventsRecyclerView;
    private EventsAdapter eventsAdapter;

    private HelperDB dataHelperDB;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        eventsRecyclerView=view.findViewById(R.id.eventsListView);
        dataHelperDB=new HelperDB(getContext());

        List<Event> eventList = dataHelperDB.getAllEvents();

        // Initialize the adapter and set it to the ListView
        eventsAdapter = new EventsAdapter(getContext(), eventList);
        eventsRecyclerView.setAdapter(eventsAdapter);

        return view;

    }


}
