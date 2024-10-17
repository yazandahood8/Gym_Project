package com.example.gym.ui.coaches;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gym.Adapters.CoachAdapter;
import com.example.gym.R;
import com.example.gym.Data.Coach;

import java.util.ArrayList;
import java.util.List;

public class CoachesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CoachAdapter coachAdapter;
    private List<Coach> coachList;

    public CoachesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coaches, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_coaches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize data and adapter
        coachList = getCoachesData();
        coachAdapter = new CoachAdapter(coachList);
        recyclerView.setAdapter(coachAdapter);

        return view;
    }

    // Method to get the list of coaches (Dummy data for now)
    private List<Coach> getCoachesData() {
        List<Coach> coaches = new ArrayList<>();
        coaches.add(new Coach(1, "John Doe", "john.doe@example.com", "password123", 35, "123-456-7890", 10));
        coaches.add(new Coach(2, "Jane Smith", "jane.smith@example.com", "password456", 28, "987-654-3210", 8));
        coaches.add(new Coach(3, "Michael Johnson", "michael.johnson@example.com", "password789", 40, "555-555-5555", 15));
        return coaches;
    }
}
