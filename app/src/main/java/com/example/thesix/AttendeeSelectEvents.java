package com.example.thesix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
 * An activity allowing attendees to select different types of events.
 */

public class AttendeeSelectEvents extends AppCompatActivity {
    private Button allEvents;
    private Button signedupEvents;
    private Button checkedinEvents;
    private Button backbtn;

    /** Create Activity Instance
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_select_events);

        // Initialize buttons
        allEvents = findViewById(R.id.allEvents);
        signedupEvents = findViewById(R.id.signedUpEvents);
        checkedinEvents = findViewById(R.id.checkedInEvents);
        backbtn = findViewById(R.id.backButton);

        // Set click listeners for buttons
        backbtn.setOnClickListener(new View.OnClickListener() {
            /**Take to Attendee Main
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSelectEvents.this, AttendeeMainActivity.class));
            }
        });

        allEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            /**Take to AttendeeAllEvents
             * @param v The view that was clicked.
             */
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSelectEvents.this, AttendeeAllEventsActivity.class));
            }
        });

        checkedinEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            /**Take to AttendeeCheckedinEventsActivity
             * @param v The view that was clicked.
             */
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSelectEvents.this, AttendeeCheckedinEventsActivity.class));
            }
        });

        signedupEvents.setOnClickListener(new View.OnClickListener() {
            /**Take to AttendeeSignedupEventsActivity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeSelectEvents.this, AttendeeSignedupEventsActivity.class));
            }
        });

    }
}

