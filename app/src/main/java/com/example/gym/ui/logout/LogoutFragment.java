package com.example.gym.ui.logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gym.LoginActivity;
import com.example.gym.R;

public class LogoutFragment extends Fragment {

    private static final String PREFS_NAME = "LoginPrefs";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Clear all stored data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all saved data
        editor.apply();

        // Show the Toast message
        Toast.makeText(getActivity(), "You have successfully logged out.", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // Close the current activity
        getActivity().finish();
    }
}
