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

    private static final int PROFILE_UPDATE_REQUEST_CODE = 1; // Request code for startActivityForResult

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

        //updateAttendeeInfo(attendee);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AttendeeProfileUpdate for result
                Intent intent = new Intent(AttendeeProfileActivity.this, AttendeeProfileUpdate.class);
                startActivityForResult(intent, PROFILE_UPDATE_REQUEST_CODE);
            }
        });

        //Sets up the 'back2AttendeeButton' to return to the Attendee main screen when clicked.
        back2AttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the AttendeeMainActivity
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Extract the data from the intent
            String name = data.getStringExtra("name");
            String contact = data.getStringExtra("contact");
            String homePage = data.getStringExtra("homePage");

            // Update the TextViews with the new data
            nameTextView.setText(name);
            contactTextView.setText(contact);
            homePageTextView.setText(homePage);
        }
    }

//    private void updateAttendeeInfo(Attendee attendee) {
//        nameTextView.setText(attendee.getName());
//        contactTextView.setText(attendee.getContact());
//        homePageTextView.setText(attendee.getHomePage());
//    }
}
