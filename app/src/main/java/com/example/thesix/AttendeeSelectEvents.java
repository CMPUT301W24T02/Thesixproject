package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AttendeeSelectEvents extends AppCompatActivity {
    private Button allEvents;
    private Button signedupEvents;
    private Button checkedinEvents;
    private Button backbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_select_events);
        allEvents = findViewById(R.id.allEvents);
        signedupEvents = findViewById(R.id.signedUpEvents);
        checkedinEvents = findViewById(R.id.checkedInEvents);
        backbtn = findViewById(R.id.backButton);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSelectEvents.this, AttendeeMainActivity.class));
            }
        });

        allEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSelectEvents.this, AttendeeAllEventsActivity.class));
            }
        });

        signedupEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSelectEvents.this, AttendeeSignedupEventsActivity.class));
            }
        });

    }
}

