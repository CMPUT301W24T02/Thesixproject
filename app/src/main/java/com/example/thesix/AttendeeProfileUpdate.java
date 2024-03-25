package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AttendeeProfileUpdate extends AppCompatActivity {

    private Button back2AttendeeProfileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_profile_update);

        back2AttendeeProfileButton = findViewById(R.id.backButton);

        //Sets up the 'back2AttendeeButton' to return to the Attendee main screen when clicked.
        back2AttendeeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the AttendeeMainActivity
                startActivity(new Intent(AttendeeProfileUpdate.this, AttendeeProfileActivity.class));
            }
        });
    }

}
