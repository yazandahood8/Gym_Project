package com.example.gym;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gym.Data.User;
import com.example.gym.HelperDB;

public class SignUpActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, passwordEditText, phoneEditText;
    Spinner ageSpinner;
    RadioGroup genderRadioGroup;
    Button signUpButton;
    HelperDB databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize the DatabaseHelper
        databaseHelper = new HelperDB(this);

        // Initialize the views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        ageSpinner = findViewById(R.id.ageSpinner);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        signUpButton = findViewById(R.id.signUpButton);

        // Set up the Spinner for age selection (values from 18 to 60)
        Integer[] ages = new Integer[43];
        for (int i = 0; i < 43; i++) {
            ages[i] = 18 + i; // Age range from 18 to 60
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);

        // Set up the sign-up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phoneNumber = phoneEditText.getText().toString();
                int age = (int) ageSpinner.getSelectedItem();

                // Get selected gender
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedGenderButton = findViewById(selectedGenderId);
                String gender = selectedGenderButton != null ? selectedGenderButton.getText().toString() : "";

                // Check for empty fields
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check for duplicate emails
                User existingUser = databaseHelper.getUserByEmail(email);
                if (existingUser != null) {
                    Toast.makeText(SignUpActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new User object
                User newUser = new User(0, name, email, password, age, phoneNumber, gender, false); // Remove admin flag

                // Add the new user to the database
                long result = databaseHelper.addUser(newUser);

                if (result != -1) {
                    // Registration successful
                    Toast.makeText(SignUpActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                    // Redirect to MainActivity2
                    Intent intent = new Intent(SignUpActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();  // Close the activity
                } else {
                    // Registration failed
                    Toast.makeText(SignUpActivity.this, "Sign Up failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
