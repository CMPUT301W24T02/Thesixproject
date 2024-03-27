package com.example.thesix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.collect.Sets;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 *
 * Continue JavaDocs
 *
 */
public class AttendeeProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private TextView nameTextView, contactTextView, homePageTextView;
    private Button back2AttendeeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_screen);

        profilePicture = findViewById(R.id.profile_picture);
        nameTextView = findViewById(R.id.name_textView);
        contactTextView = findViewById(R.id.contact_textView);
        homePageTextView = findViewById(R.id.homePage_textView);
        back2AttendeeButton = findViewById(R.id.backButton);

        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(AttendeeProfileActivity.this, AttendeeProfileUpdate.class);
            startActivityForResult(intent, 1); // Use a request code consistent with onActivityResult
        });

        back2AttendeeButton.setOnClickListener(v -> finish());

        // Initially display data
        displayAttendeeInfo();
    }

    private void displayAttendeeInfo() {
        SharedPreferences prefs = getSharedPreferences("AttendeePrefs", MODE_PRIVATE);
        nameTextView.setText(prefs.getString("name", ""));
        contactTextView.setText(prefs.getString("contact", ""));
        homePageTextView.setText(prefs.getString("homePage", ""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Data might have been updated, refresh display
            displayAttendeeInfo();
        }
    }
}
