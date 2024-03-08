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
 * AttendeeListActivity is an activity class responsible for displaying a list of attendees.
 * It provides options to navigate back to the MainActivity, view the map, and access notifications.
 * @Author Danielle
 * @See AppCompatActivity
 */
public class AttendeeListActivity extends AppCompatActivity {

    private Button backButton;
    private Button mapButton;
    private Button notificationButton;
    ListView attendeeList;
    ArrayAdapter<Attendee> attendeeAdapter;   // acts as a communication bridge between front and back end
    ArrayList<Attendee> dataList;
    /**
     * Called when the activity is starting. Initializes UI components and sets up event listeners.
     *
     * @param savedInstanceState    If the activity is being re-initialized after previously being shut down
     *                              then this Bundle contains the data it most recently supplied in
     *                              onSaveInstanceState(Bundle). Note: Otherwise, it is null.
     */
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
                startActivity(new Intent(AttendeeListActivity.this, MainActivity.class));
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
