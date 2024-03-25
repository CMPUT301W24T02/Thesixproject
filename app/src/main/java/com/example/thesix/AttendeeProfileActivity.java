package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.collect.Sets;

/**
 *
 *
 * Continue JavaDocs
 *
 */
public class AttendeeProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView nameTextView;
    private TextView contactTextView;
    private TextView homePageTextView;
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

        Attendee attendee = new Attendee("JESSE", "780-225-2535", "@JESSEBUILDS");

        updateAttendeeInfo(attendee);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeProfileActivity.this, AttendeeProfileUpdate.class));
            }
        });

        //Sets up the 'back2AttendeeButton' to return to the Attendee main screen when clicked.
        back2AttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the AttendeeMainActivity
                startActivity(new Intent(AttendeeProfileActivity.this, AttendeeMainActivity.class));
            }
        });
    }

    private void updateAttendeeInfo(Attendee attendee) {
        nameTextView.setText(attendee.getName());
        contactTextView.setText(attendee.getContact());
        homePageTextView.setText(attendee.getHomePage());
    }
}
