package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 *AttendeeListActivity class manages the display of a list of attendees within an Android application.
 *Initializes UI components such as buttons and a list view in the onCreate method.
 *Utilizes buttons (backButton, mapButton, notificationButton) to navigate to different activities.
 *Temporarily creates an Attendee object (testAttendee) for testing purposes.
 *Creates an array of Attendee objects (attendeeArray) and initializes an ArrayList (dataList) with these objects.
 *Initializes an ArrayAdapter (attendeeAdapter) to act as a communication bridge between the front-end UI and back-end data.
 *Sets the adapter to display the list of attendees (dataList) in the attendeeList ListView.
 *Handles button clicks to navigate to other activities (EventDetailsConnector, MapActivity, NotificationActivity)
 **/

public class AttendeeListActivity extends AppCompatActivity {
    private Button backButton;
    private Button mapButton;
    private Button notificationButton;
    ListView attendeeList;
    ArrayAdapter<Attendee> attendeeAdapter;   // acts as a communication bridge between front and back end
    ArrayList<Attendee> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list_screen);
        backButton = findViewById(R.id.backButton);
        mapButton = findViewById(R.id.mapButton);
        notificationButton = findViewById(R.id.notificationButton);
        attendeeList = findViewById(R.id.attendee_list_view);

        // Temp
        Attendee testAttendee = new Attendee("Jane");

        // Create the array to hold invited guests
        Attendee[] attendeeArray = {testAttendee, testAttendee, testAttendee, testAttendee, testAttendee};
        dataList = new ArrayList<>(Arrays.asList(attendeeArray));

        // Find the ListView item from front-end to communicate with
        attendeeAdapter = new ArrayAdapter<>(this, R.layout.attendee_list_content, dataList);

        // Set your adapter to show the List of Attendees
        attendeeList.setAdapter(attendeeAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, EventDetailsConnector.class));
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, MapActivity.class));
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeListActivity.this, NotificationActivity.class));
            }
        });
    }



}
