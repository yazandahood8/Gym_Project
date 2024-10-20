package com.example.gym.ui.coaches;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.gym.Adapters.CoachAdapter;
import com.example.gym.Data.Coach;
import com.example.gym.R;

import java.util.ArrayList;
import java.util.List;

public class CoachesFragment extends Fragment {

    private ListView listView;
    private CoachAdapter coachListAdapter;
    private List<Coach> coachList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coaches, container, false);

        listView = view.findViewById(R.id.list_view_coaches);

        // Initialize coach list with sample data or fetch from your database
        coachList = new ArrayList<>();
        // Make sure the parameters match the Coach constructor you defined
        coachList.add(new Coach(1, "John Doe", "john.doe@example.com", "password123", 30, "123456789", "Male", 5, "https://example.com/john_doe_image.jpg"));
        coachList.add(new Coach(2, "Jane Smith", "jane.smith@example.com", "password456", 28, "987654321", "Female", 7, "https://example.com/jane_smith_image.jpg"));

        // Initialize the adapter and set it to the ListView
        coachListAdapter = new CoachAdapter(getContext(), coachList);
        listView.setAdapter(coachListAdapter);

        return view;
    }
}
