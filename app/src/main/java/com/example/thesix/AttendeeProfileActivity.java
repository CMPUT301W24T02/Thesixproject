package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    private Button back2AttendeeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_screen);


        back2AttendeeButton = findViewById(R.id.backButton);

        //Sets up the 'back2AttendeeButton' to return to the Attendee main screen when clicked.
        back2AttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the AttendeeMainActivity
                startActivity(new Intent(AttendeeProfileActivity.this, AttendeeMainActivity.class));
            }
        });
    }
}
