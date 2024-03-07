package com.example.thesix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

/**
 * EventDetailsConnector class manages the display of event details and provides options for generating guest lists within an Android application.
 * Initializes UI components such as TextView (eventName, eventDescription), ImageView (eventPoster), and Button (backButton, generateGuestButton) in the onCreate method.
 * Retrieves event details from the intent extras passed from the previous activity (eventName and eventDescription) and sets them to the respective TextViews.
 * Handles button clicks to navigate to other activities (EventDetailsAdapter, AttendeeListActivity) using intents.
 * Upon clicking the backButton, it navigates to the EventDetailsAdapter activity.
 * Upon clicking the generateGuestButton, it navigates to the AttendeeListActivity activity.
 */

public class EventDetailsConnector extends AppCompatActivity {

    private TextView eventName;
    private TextView eventDescription;
    private ImageView eventPoster;
    private Button generateGuestButton;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_connector);

        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventPoster = findViewById(R.id.eventPoster);
        backButton = findViewById(R.id.backButton);
        generateGuestButton = findViewById(R.id.guestListButton);



        Bundle bundle = getIntent().getExtras();
        String eventName1 = bundle.getString("eventName");
        String eventDescription1 = bundle.getString("eventDescription");
        eventName.setText(eventName1);
        eventDescription.setText(eventDescription1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsConnector.this, EventDetailsAdapter.class));
            }
        });

        generateGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventDetailsConnector.this, AttendeeListActivity.class));
            }
        });
    }

}
