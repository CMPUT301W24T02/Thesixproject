package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class AttendeeProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private TextView nameTextView, contactTextView, homePageTextView;
    private Button back2AttendeeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_screen);

        // Initialize views
        profilePicture = findViewById(R.id.profile_picture);
        nameTextView = findViewById(R.id.name_textView);
        contactTextView = findViewById(R.id.contact_textView);
        homePageTextView = findViewById(R.id.homePage_textView);
        back2AttendeeButton = findViewById(R.id.backButton);

        // Setup button to go back to AttendeeMainActivity
        back2AttendeeButton.setOnClickListener(v -> finish());

        // Setup ImageView click to navigate to AttendeeProfileUpdate for editing
        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(AttendeeProfileActivity.this, AttendeeProfileUpdate.class);
            startActivityForResult(intent, 1);
        });

        // Load profile information including image
        displayAttendeeInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure data is refreshed when coming back to this activity
        displayAttendeeInfo();
    }

    private void displayAttendeeInfo() {
        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        nameTextView.setText(prefs.getString("name", ""));
        contactTextView.setText(prefs.getString("contact", ""));
        homePageTextView.setText(prefs.getString("homePage", ""));

        // Retrieve the path for the profile image
        String imagePath = prefs.getString("profileImagePath", null);
        if (imagePath != null) {
            // Set the profile image using its path
            profilePicture.setImageURI(Uri.fromFile(new File(imagePath)));
        }
    }
}
