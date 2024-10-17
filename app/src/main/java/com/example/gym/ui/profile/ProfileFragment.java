package com.example.gym.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gym.HelperDB;
import com.example.gym.R;
import com.example.gym.Data.User;

public class ProfileFragment extends Fragment {

    private TextView nameTextView, emailTextView, phoneTextView, ageTextView, passwordTextView;
    private ImageView profileImageView;
    private Button editButton;

    private HelperDB helperDB;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImageView = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.textViewName);
        emailTextView = view.findViewById(R.id.textViewEmail);
        phoneTextView = view.findViewById(R.id.textViewPhone);
        ageTextView = view.findViewById(R.id.textViewAge);
        passwordTextView = view.findViewById(R.id.textViewPassword);
        editButton = view.findViewById(R.id.buttonEdit);

        // Initialize database helper
        helperDB = new HelperDB(getActivity());

        // Load current user details
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);

        if (userEmail != null) {
            currentUser = helperDB.getUserByEmail(userEmail);
            if (currentUser != null) {
                loadUserDetails();
            }
        }

        // Edit button to open dialogs to update fields
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        return view;
    }

    // Method to load user details
    private void loadUserDetails() {
        nameTextView.setText("Name: " + currentUser.getName());
        emailTextView.setText("Email: " + currentUser.getEmail());
        phoneTextView.setText("Phone: " + currentUser.getPhoneNumber());
        ageTextView.setText("Age: " + currentUser.getAge());
        passwordTextView.setText("Password: ••••••••");
    }

    // Show dialog to edit user details
    private void showEditDialog() {
        // Create an AlertDialog to edit user details
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Profile");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        final EditText nameEditText = dialogView.findViewById(R.id.editTextName);
        final EditText phoneEditText = dialogView.findViewById(R.id.editTextPhone);
        final EditText ageEditText = dialogView.findViewById(R.id.editTextAge);
        final EditText passwordEditText = dialogView.findViewById(R.id.editTextPassword);

        // Populate current data
        nameEditText.setText(currentUser.getName());
        phoneEditText.setText(currentUser.getPhoneNumber());
        ageEditText.setText(String.valueOf(currentUser.getAge()));
        passwordEditText.setText(currentUser.getPassword());

        // Set positive button (Save)
        builder.setPositiveButton("Save", null); // We will override this later

        // Set negative button (Cancel)
        builder.setNegativeButton("Cancel", null);

        // Create the dialog
        final AlertDialog dialog = builder.create();

        // Override the positive button to handle validation
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the input values
                        String newName = nameEditText.getText().toString().trim();
                        String newPhone = phoneEditText.getText().toString().trim();
                        String newAgeStr = ageEditText.getText().toString().trim();
                        String newPassword = passwordEditText.getText().toString().trim();

                        // Validate inputs
                        if (newName.isEmpty()) {
                            nameEditText.setError("Name cannot be empty");
                            nameEditText.requestFocus();
                            return;
                        }

                        if (newPhone.isEmpty()) {
                            phoneEditText.setError("Phone number cannot be empty");
                            phoneEditText.requestFocus();
                            return;
                        }

                        if (newAgeStr.isEmpty()) {
                            ageEditText.setError("Age cannot be empty");
                            ageEditText.requestFocus();
                            return;
                        }

                        int newAge;
                        try {
                            newAge = Integer.parseInt(newAgeStr);
                            if(newAge<18 || newAge>60){
                                ageEditText.setError("Please enter a valid age between 18 - 60");
                                ageEditText.requestFocus();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            ageEditText.setError("Please enter a valid age");
                            ageEditText.requestFocus();
                            return;
                        }

                        if (newPassword.isEmpty()) {
                            passwordEditText.setError("Password cannot be empty");
                            passwordEditText.requestFocus();
                            return;
                        }

                        // All inputs are valid, update the user in the database
                        currentUser.setName(newName);
                        currentUser.setPhoneNumber(newPhone);
                        currentUser.setAge(newAge);
                        currentUser.setPassword(newPassword);

                        long result = helperDB.updateUser(currentUser);

                        if (result > 0) {
                            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            loadUserDetails(); // Update UI with new details
                            dialog.dismiss(); // Close the dialog after successful update
                        } else {
                            Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

}
